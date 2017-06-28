package com.hummingwave.upgrade;

import android.content.Context;

/*
 * Created by Sneha on 27-Jun-17.
 */

public class Upgrade {
    private static Context context;

    public static void init(Context ctx) {
        context = ctx;
        new UpgradeApp().getCurrentVersion(context);
    }
}
