package com.aslammaududy.realtimetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aslammaududy.realtimetranslator.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText contact;
    private RadioGroup langGroup;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 123;
    private User user, user1;
    private DatabaseReference dbReference;
    private Intent intent;
    private ArrayList<String> list;
    private HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instantiateUser();


        if (!isLoggedIn()) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                    .setAllowNewEmailAccounts(true)
                    .setIsSmartLockEnabled(true)
                    .build(), RC_SIGN_IN);
        } else {
            user = new User();
            user.setUid(firebaseUser.getUid());

            dbReference = FirebaseDatabase.getInstance().getReference(user.NODE_USERS);
            getContacts();
        }

        setContentView(R.layout.activity_main);

        contact = findViewById(R.id.show_contact);
        langGroup = findViewById(R.id.lang_group);
        list = new ArrayList<>();
        map = new HashMap<>();
        intent = getIntent();

        if (intent.getStringExtra("name") != null) {
            contact.setText(intent.getStringExtra("name"));
        } else {
            contact.setText("");
        }

        langGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (langGroup.getCheckedRadioButtonId()) {
                    case R.id.lang_arabic:
                        user.setLang("ar");
                        break;
                    case R.id.lang_english:
                        user.setLang("en");
                        break;
                    case R.id.lang_indonesian:
                        user.setLang("id");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IdpResponse response = IdpResponse.fromResultIntent(data);
        dbReference = FirebaseDatabase.getInstance().getReference(user.NODE_USERS);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            instantiateUser();
            if (isLoggedIn()) {
                user = new User();
                user.setUid(firebaseUser.getUid());
                user.setName(firebaseUser.getDisplayName());

                dbReference.child(user.getUid()).child(user.NODE_NAME).setValue(user.getName());
                dbReference.child(user.getUid()).child(user.NODE_MESSAGE).setValue(user.INITIAL_MESSAGE);
                dbReference.child(user.getUid()).child(user.NODE_LANG).setValue(user.INITIAL_LANG);
                dbReference.child(user.getUid()).child(user.NODE_UID).setValue(user.getUid());
            }
        } else {
            if (response == null) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setAllowNewEmailAccounts(true)
                        .setIsSmartLockEnabled(true)
                        .build(), RC_SIGN_IN);
            }
        }
        getContacts();
    }

    private void instantiateUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private boolean isLoggedIn() {
        return firebaseUser != null;
    }

    public void logOut(View view) {
        firebaseAuth.signOut();
        firebaseUser = firebaseAuth.getCurrentUser();
        finish();
    }

    public void toSpeakPage(View view) {
        user.setUid(map.get(intent.getStringExtra("name")));
        instantiateUser();
        if (user.getLang() != null && !contact.getText().toString().equals("")) {
            intent = new Intent(this, SpeakActivity.class);
            intent.putExtra("dataLoad", new String[]{user.getLang(), user.getUid()});
            startActivity(intent);
        } else if (user.getLang() == null) {
            Toast.makeText(this, "Please choose your language", Toast.LENGTH_SHORT).show();
        } else if (contact.getText().toString().equals("")) {
            Toast.makeText(this, "Please select a contact first", Toast.LENGTH_SHORT).show();
        }
    }

    public void showContact(View view) {
        intent = new Intent(this, ContactActivity.class);
        intent.putExtra("contacts", list);
        startActivity(intent);
    }

    private void getContacts() {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user1 = snapshot.getValue(User.class);
                    if (user1 != null) {
                        list.add(user1.getName());
                        map.put(user1.getName(), user1.getUid());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
