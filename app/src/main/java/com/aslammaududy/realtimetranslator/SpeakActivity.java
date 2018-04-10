package com.aslammaududy.realtimetranslator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class SpeakActivity extends AppCompatActivity {

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

        final TextView text = findViewById(R.id.speak_text);
        final TextView textToTranslate = findViewById(R.id.translate_text);
        final TextView translatedText = findViewById(R.id.translated_text);
        final ImageButton mic = findViewById(R.id.mic_button);
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
                    textToTranslate.setText(matches.get(0));
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
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        //hold and release speak button
        mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        recognizer.stopListening();
                        String s = "";
                        try {
                            s = Translator.translate(textToTranslate.getText().toString(), "en", "id");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        translatedText.setText(s);
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
