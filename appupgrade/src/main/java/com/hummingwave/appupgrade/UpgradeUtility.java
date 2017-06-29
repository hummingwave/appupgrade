package com.hummingwave.appupgrade;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * Created by Sneha on 27-Jun-17.
 */

class UpgradeUtility {
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



    static SharedPreferences getSharedPref(Context context) {
        if (context == null) {
           return null;
        }
        return context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
    }


}
