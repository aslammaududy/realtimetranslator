package com.aslammaududy.realtimetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Spinner contacts;
    private RadioGroup langGroup;
    private String langCode;
    private FirebaseUser firebaseUser;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isUserLoggedIn()) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                    .setAllowNewEmailAccounts(true)
                    .setIsSmartLockEnabled(true)
                    .build(), RC_SIGN_IN);
        }

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

    private void instantiateUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private boolean isUserLoggedIn() {
        return firebaseUser != null;
    }

    private void setContacts() {

    }

    public void toLoginPage(View view) {
        finish();
    }

    public void toSpeakPage(View view) {
        Intent intent = new Intent(this, SpeakActivity.class);
        intent.putExtra("lang", langCode);
        startActivity(intent);
    }
}
