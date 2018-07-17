package com.aslammaududy.realtimetranslator.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (cm != null) {
                networkInfo = cm.getActiveNetworkInfo();
            }

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
