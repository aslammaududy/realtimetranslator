package com.aslammaududy.realtimetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Spinner contacts;
    RadioGroup langGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = findViewById(R.id.contact_list);
        langGroup = findViewById(R.id.lang_group);
    }

    private void moveToPage(String langCode) {
        Intent intent = new Intent(this, SpeakActivity.class);
        intent.putExtra("language", langCode);
        startActivity(intent);
    }

    public void toLoginPage(View view) {
        finish();
    }

    public void toSpeakPage(View view) {
        langGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (langGroup.getCheckedRadioButtonId()) {
                    case R.id.lang_arabic:
                        moveToPage("ar");
                        break;
                    case R.id.lang_english:
                        moveToPage("en");
                        break;
                    case R.id.lang_indonesian:
                        moveToPage("id");
                        break;
                }
            }
        });
    }
}
