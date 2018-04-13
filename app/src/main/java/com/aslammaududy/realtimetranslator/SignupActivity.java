package com.aslammaududy.realtimetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

private EditText inputConfirmPassword, inputEmail, inputPassword;
private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth=FirebaseAuth.getInstance();

        inputConfirmPassword=findViewById(R.id.conf_pass_signup);
        inputEmail=findViewById(R.id.email_signup);
        inputPassword=findViewById(R.id.password_signup);
    }

    public void signupAndToLoginPage(View view) {
        String confirmPassword=inputConfirmPassword.getText().toString();
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Enter your email",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Enter your password",Toast.LENGTH_SHORT).show();
        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_SHORT).show();
        }

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(SignupActivity.this,"Failed to sign up "+task.getException(),Toast.LENGTH_SHORT).show();
                        }else {
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });
    }
}
