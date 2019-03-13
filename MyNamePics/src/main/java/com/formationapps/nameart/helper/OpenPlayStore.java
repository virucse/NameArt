package com.formationapps.nameart.helper;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.List;

public class OpenPlayStore extends AsyncTask<String, Void, String> {
    String url;
    boolean marketFound = false;
    Intent rateIntent;

    private Context context;
    private ProgressDialog pDialog;

    public OpenPlayStore(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please Wait...");
        pDialog.show();
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... urls) {
        url = urls[0];
        rateIntent = new Intent(Intent.ACTION_VIEW, Uri
                .parse(url));

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {
                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name);
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);

                marketFound = true;
                break;

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (!marketFound) {
            Intent webIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url));
            context.startActivity(webIntent);
        } else {
            context.startActivity(rateIntent);
        }
    }
}
