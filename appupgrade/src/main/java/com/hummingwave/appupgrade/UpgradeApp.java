package com.hummingwave.appupgrade;

/*
 * Created by Sneha on 27-Jun-17.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Date;


class UpgradeApp {
    private String TAG = UpgradeApp.class.getCanonicalName();
    private Context context;
    private String currentVersion, latestVersion, packageName;
    private Drawable icon;
    private Handler handler;
    private Runnable runnable;
    private CountDownTimer countDownTimer;
    private boolean dialogShowed;

    void getCurrentVersion(Context ctx) {
        try {
            context = ctx;
            if (context != null) {
                if(UpgradeUtility.getSharedPref(context).getBoolean("DontShowAgain", false)) return;
                PackageManager pm = context.getPackageManager();
                PackageInfo pInfo;
                pInfo = pm.getPackageInfo(context.getPackageName(), 0);
                packageName = pInfo.packageName;
                icon = pm.getApplicationIcon(packageName);
                currentVersion = pInfo.versionName;
                Log.d(TAG, packageName + pInfo.versionName + pInfo.versionCode + "");
                if (UpgradeUtility.isInternetConnectivityAvailable(context)) {
                    if (UpgradeUtility.isValidString(packageName) && UpgradeUtility.isValidString(currentVersion)) {
                        countDownTimer = new CountDownTimer(2000, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if (!dialogShowed)
                                    dialogShowed = showDialog();
                            }
                        };
                        countDownTimer.start();
                        new GetLatestVersion().execute();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.upgrade_generic_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.upgrade_net_connection_error), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String playStoreURL = "http://play.google.com/store/apps/details?id=" + packageName;
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect(playStoreURL).get();
                if (doc != null) {
                    latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();
                    Log.d(TAG, latestVersion + " " + new Date());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (UpgradeUtility.isValidString(latestVersion) && currentVersion.compareTo(latestVersion) < 0) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!dialogShowed)
                                dialogShowed = showDialog();
                        }
                    };
                    handler.postDelayed(runnable, 2000);
                } else {
                    // do nothing
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(jsonObject);
        }
    }

    private boolean showDialog() {
        try {
            if (context != null && UpgradeUtility.isValidString(latestVersion) && currentVersion.compareTo(latestVersion) < 0) {
                if (countDownTimer != null) countDownTimer.cancel();
                Log.d(TAG, new Date() + "");
                handler.removeCallbacks(runnable);
                final Dialog upgradeDialog = new Dialog(context, R.style.UpgradeMyDialogTheme);
                if (upgradeDialog.getWindow() != null) {
                    upgradeDialog.getWindow().setGravity(Gravity.CENTER);
                    upgradeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    upgradeDialog.setContentView(R.layout.dialog_upgrade_app);
                    upgradeDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.upgrade_background_curved));
                    upgradeDialog.setCancelable(false);

                    TextView txtUpdateNow = (TextView) upgradeDialog.findViewById(R.id.txt_update_now);
                    TextView txtRemindLater = (TextView) upgradeDialog.findViewById(R.id.txt_remind_me_later);
                    LinearLayout linearDontShow = (LinearLayout) upgradeDialog.findViewById(R.id.linear_dont_show);
                    CheckBox checkBoxDontShow = (CheckBox) upgradeDialog.findViewById(R.id.checkbox_dont_show);

                    if ((latestVersion).toLowerCase().contains("f")) {
                        txtRemindLater.setVisibility(View.GONE);
                        linearDontShow.setVisibility(View.GONE);
                    }

                    ImageView imgAppIcon = (ImageView) upgradeDialog.findViewById(R.id.img_app_icon);
                    if (icon != null) imgAppIcon.setImageDrawable(icon);
                    else imgAppIcon.setImageResource(R.drawable.upgrade_logo_googleplay);

                    txtUpdateNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                            upgradeDialog.dismiss();
                        }
                    });
                    txtRemindLater.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            upgradeDialog.dismiss();
                        }
                    });
                    checkBoxDontShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                UpgradeUtility.getSharedPref(context).edit().putBoolean("DontShowAgain", true).apply();
                            } else {
                                UpgradeUtility.getSharedPref(context).edit().putBoolean("DontShowAgain", false).apply();
                            }
                        }
                    });
                    upgradeDialog.show();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
