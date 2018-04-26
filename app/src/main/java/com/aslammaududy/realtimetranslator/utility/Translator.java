package com.aslammaududy.realtimetranslator.utility;

import android.util.Log;

import com.aslammaududy.realtimetranslator.model.User;
import com.aslammaududy.realtimetranslator.model.YandexTranslate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Translator {
    private final String API_KEY = "trnsl.1.1.20180409T135813Z.bae86cd702eb461e.3269509a708560b2198ac7afbdabb4677121a0a2";
    private final String BASE_URL = "translate.yandex.net";

    public Translator() {

    }

    public void translate(String text, String sourceLang, String targetLang) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegments("api/v1.5/tr.json/translate")
                .addQueryParameter("key", API_KEY)
                .addEncodedQueryParameter("text", text)
                .addQueryParameter("lang", sourceLang + "-" + targetLang)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) {
                Gson gson = new Gson();
                YandexTranslate translate = gson.fromJson(response.body().charStream(), YandexTranslate.class);
                addToDatabase(translate.getText().get(0));
                Log.i("lang", translate.getText().get(0) + "");
            }
        });
    }

    private void addToDatabase(String result) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            User user = new User();
            user.setUid(firebaseUser.getUid());
            user.setResult(result);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(user.NODE_USERS);
            reference.child(user.getUid()).child(user.NODE_RESULT).setValue(user.getResult());
        }
    }
}

