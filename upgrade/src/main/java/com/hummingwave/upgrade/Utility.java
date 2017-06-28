package com.hummingwave.upgrade;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * Created by Sneha on 27-Jun-17.
 */

class Utility {
    static boolean isInternetConnectivityAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo isConnectivityAvailable = connectivityManager.getActiveNetworkInfo();
            return isConnectivityAvailable != null;
        }
        return true;
    }

    static boolean isValidString(String s) {
        return (s != null && !s.trim().isEmpty() && !s.equalsIgnoreCase("") && !s.equalsIgnoreCase("null"));
    }

}
