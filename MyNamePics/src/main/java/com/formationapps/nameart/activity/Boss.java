package com.formationapps.nameart.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.OpenPlayStore;

/**
 * Created by caliber fashion on 3/25/2017.
 */

public class Boss extends AppCompatActivity {
    public void showException(String tag, Throwable e) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, tag + "=>" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isNetworkConnected() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
    private Dialog appRestrictDialog;
    public void showAppRestrictDialog(){
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setTitle(R.string.alert);
        b.setCancelable(false);
        b.setMessage(R.string.apprestrictmsg);
        b.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String str="https://play.google.com/store/apps/details?id="+getPackageName();
                new OpenPlayStore(Boss.this).execute(str);
            }
        });
        appRestrictDialog=b.create();
        appRestrictDialog.show();
    }
    public void dismissAppRestrictDialog(){
        if(appRestrictDialog!=null){
            if(appRestrictDialog.isShowing()){
                appRestrictDialog.dismiss();
            }
        }
    }
}
