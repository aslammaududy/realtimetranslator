package com.aslammaududy.realtimetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aslammaududy.realtimetranslator.utility.ContactsAdapter;
import com.aslammaududy.realtimetranslator.utility.TouchListener;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        intent = getIntent();
        final ArrayList<String> contacts = intent.getStringArrayListExtra("contacts");
        RecyclerView recyclerView = findViewById(R.id.contact_list);
        RecyclerView.Adapter adapter = new ContactsAdapter(this, contacts);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new TouchListener(this, recyclerView, new TouchListener.OnTouchActionListener() {
            @Override
            public void onLeftSwipe(View view, int position) {

            }

            @Override
            public void onRightSwipe(View view, int position) {

            }

            @Override
            public void onClick(View view, int position) {
                toMainPage(contacts.get(position));
            }
        }));

    }

    void toMainPage(String name) {
        intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
