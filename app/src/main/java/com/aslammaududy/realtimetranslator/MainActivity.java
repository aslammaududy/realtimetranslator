package com.aslammaududy.realtimetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.aslammaududy.realtimetranslator.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private Spinner contacts;
    private RadioGroup langGroup;
    private String langCode;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 123;
    private User user;

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
        }

        setContentView(R.layout.activity_main);

        contacts = findViewById(R.id.contact_list);
        langGroup = findViewById(R.id.lang_group);

        langGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (langGroup.getCheckedRadioButtonId()) {
                    case R.id.lang_arabic:
                        langCode = "ar";
                        break;
                    case R.id.lang_english:
                        langCode = "en";
                        break;
                    case R.id.lang_indonesian:
                        langCode = "id";
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            instantiateUser();
            if (isLoggedIn()) {
                user = new User(firebaseUser.getDisplayName(), user.INITIAL_MESSAGE, user.INITIAL_LANGUAGE);
                user.setUid(firebaseUser.getUid());

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
                dbReference.child(user.NODE_USERS).child(user.getUid()).setValue(user);
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
        Intent intent = new Intent(this, SpeakActivity.class);
        intent.putExtra("dataLoad", new String[]{langCode, user.getUid()});
        startActivity(intent);
    }
}
