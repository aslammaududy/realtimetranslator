package com.aslammaududy.realtimetranslator.utility;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton extends Application {
    private static RequestQueueSingleton requestQueueSingleton;

    private RequestQueue requestQueue;
    private static Context context;

    public void onCreate() {
        super.onCreate();
        requestQueueSingleton = this;
    }

    public static synchronized RequestQueueSingleton getInstance() {
        return requestQueueSingleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
