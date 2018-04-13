package com.aslammaududy.realtimetranslator.utility;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Translator {
    public static final String API_KEY = "trnsl.1.1.20180409T135813Z.bae86cd702eb461e.3269509a708560b2198ac7afbdabb4677121a0a2";
    Context context;
    String result;
    String lang;

    public Translator(Context context) {
        this.context = context;
    }

    public String translate(String text, String sourceLang, String targetLang) {
        String tag_json = "json_req"; // for cancelling the request
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + API_KEY + "&text=" + text + "&lang=" + sourceLang + "-" + targetLang;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    result = response.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        VolleySingleton.getInstance().addToRequestQueue(request, tag_json);
        return result;
    }

    public String detectLanguage(String text) {
        String tag_json = "json_req";
        String url = "https://translate.yandex.net/api/v1.5/tr.json/detect?key=" + API_KEY + "&text=" + text;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    lang = response.getString("lang");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        VolleySingleton.getInstance().addToRequestQueue(request, tag_json);
        return lang;
    }
}
