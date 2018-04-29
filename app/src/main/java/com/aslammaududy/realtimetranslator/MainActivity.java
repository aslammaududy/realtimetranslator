package com.aslammaududy.realtimetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.aslammaududy.realtimetranslator.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private Spinner contacts;
    private RadioGroup langGroup;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 123;
    private User user;
    private DatabaseReference dbReference;

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
        }

        setContentView(R.layout.activity_main);

        contacts = findViewById(R.id.contact_list);
        langGroup = findViewById(R.id.lang_group);

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
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            instantiateUser();
            if (isLoggedIn()) {
                user = new User();
                user.setUid(firebaseUser.getUid());
                user.setName(firebaseUser.getDisplayName());

                dbReference = FirebaseDatabase.getInstance().getReference(user.getUid());
                dbReference.child(user.getUid()).child(user.NODE_NAME).setValue(user.getName());
                dbReference.child(user.getUid()).child(user.NODE_MESSAGE).setValue(user.INITIAL_MESSAGE);
                dbReference.child(user.getUid()).child(user.NODE_LANG).setValue(user.INITIAL_LANG);
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
    }

    private void instantiateUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private boolean isLoggedIn() {
        return firebaseUser != null;
    }

    private void setContacts() {

    }

    public void logOut(View view) {
        firebaseAuth.signOut();
        firebaseUser = firebaseAuth.getCurrentUser();
        finish();
    }

    public void toSpeakPage(View view) {
        instantiateUser();
        if (isLoggedIn()) {
            dbReference.child(user.getUid()).child(user.NODE_LANG).setValue(user.getLang());
        }
        Intent intent = new Intent(this, SpeakActivity.class);
        intent.putExtra("dataLoad", new String[]{user.getLang(), user.getUid()});
        startActivity(intent);
    }
}
