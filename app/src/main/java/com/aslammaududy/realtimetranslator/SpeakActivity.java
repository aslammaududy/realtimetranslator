package com.aslammaududy.realtimetranslator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aslammaududy.realtimetranslator.utility.Translator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class SpeakActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private String teks, text, sourceLang, targetLang;
    private Translator translator;
    private TextView langCode, translatedText;
    private Handler handler;
    private int delay;
    private Runnable runnable;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);
        final ImageButton mic = findViewById(R.id.mic_button);
        langCode = findViewById(R.id.lang_code);
        translatedText = findViewById(R.id.translated_text);
        delay = 3000;

        translator = new Translator(this);
        handler = new Handler();

        //permission check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }

        //speech recognition
        final SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
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
                        teks = URLEncoder.encode(matches.get(0), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        //text to speech
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(new Locale("id_ID"));
                }
            }
        });

        //hold and release speak button
        mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = getIntent();
                targetLang = intent.getStringExtra("lang");
                sourceLang = "en";
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        recognizer.stopListening();
                        //speak(translator.translate(teks, sourceLang, targetLang));
                       /* handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                text = translator.translate(teks, sourceLang, targetLang);
                                speak(text);
                            }
                        }, delay);*/
                        //Log.i("Text", text);
                        dbReference = FirebaseDatabase.getInstance().getReference();
                        dbReference.child("user").child("q0uBLzWwYzcUi1V2ZMnuChCRlhB2").child("message").setValue(teks);

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


    //speak method for tts
    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
