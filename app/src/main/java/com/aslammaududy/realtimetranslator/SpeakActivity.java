package com.aslammaududy.realtimetranslator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aslammaududy.realtimetranslator.model.User;
import com.aslammaududy.realtimetranslator.utility.Translator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapzen.speakerbox.Speakerbox;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class SpeakActivity extends AppCompatActivity {

    private Speakerbox speakerbox;
    private String[] dataLoad;
    private Translator translator;
    private TextView langCode, translatedText;
    private Handler handler;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer recognizer;
    private Intent recognizerIntent;
    private ImageButton mic;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);
        mic = findViewById(R.id.mic_button);
        langCode = findViewById(R.id.lang_code);
        translatedText = findViewById(R.id.translated_text);

        //user = new User();
        translator = new Translator(this);
        handler = new Handler();
        speakerbox = new Speakerbox(getApplication());
        speakerbox.setActivity(this);
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("users");
        Intent intent = getIntent();

        dataLoad = intent.getStringArrayExtra("dataLoad");
        //user.setUid(dataLoad[1]);

        //permission check
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            recordAudio();
        }

        //speech recognition
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        //use this for custom speech to text
        //means that we don't have to use google speech to text dialog
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            //method for getting the results of speech in form of text
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

//for testing purpose
                if (matches != null) {
                    try {
                        user.setMessage(URLEncoder.encode(matches.get(0), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //User.setMessage(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        dbReference.child("RrtVpXMIQYgNXv8IyFxBDkyfmK53").child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Log.i("message", user.getMessage());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recordAudio();
                }
            }
        }
    }

    public void recordAudio() {
        //hold and release speak button
        mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                instantiateUser();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        recognizer.stopListening();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isLoggedIn()) {
                                    //dbReference.child(user.getUid()).child(User.MESSAGE).setValue(user.getMessage());
                                }
                            }
                        }, 500);

                        break;
                    case MotionEvent.ACTION_DOWN:
                        recognizer.startListening(recognizerIntent);
                        mic.setBackgroundColor(Color.parseColor("#00ffffff"));
                        break;
                }
                return false;
            }
        });
    }

    private void instantiateUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private boolean isLoggedIn() {
        return firebaseUser != null;
    }
}
