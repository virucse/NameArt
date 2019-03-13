package com.formationapps.nameart.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caliber fashion on 1/9/2017.
 */

public class MarseMallowPermission {

    public static void checkPermissions(Context context,int flag){
        List<String> pm=new ArrayList<>();
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!(result == PackageManager.PERMISSION_GRANTED)) {
            String s= Manifest.permission.READ_EXTERNAL_STORAGE;
            pm.add(s);
        }
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!(result2 == PackageManager.PERMISSION_GRANTED)) {
            String s= Manifest.permission.WRITE_EXTERNAL_STORAGE;
            pm.add(s);
        }
        int result3 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (!(result3 == PackageManager.PERMISSION_GRANTED)) {
            String s= Manifest.permission.CAMERA;
            pm.add(s);
        }
        /*int result4 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (!(result4 == PackageManager.PERMISSION_GRANTED)) {
            String s= Manifest.permission.READ_PHONE_STATE;
            pm.add(s);
        }*/
        int result5 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (!(result5 == PackageManager.PERMISSION_GRANTED)) {
            String s= Manifest.permission.ACCESS_COARSE_LOCATION;
            pm.add(s);
        }

        if(pm.size()>0){
            String[] as=new String[pm.size()];
            pm.toArray(as);
            ActivityCompat.requestPermissions((Activity) context,as,flag);
        }
    }

    public static boolean storagePermitted(Activity activity, int REQUESTCODE_STORAGE_PERMISSION) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission.Marse", "storage.permissionGranted");
            return true;
        }

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE_PERMISSION);
        Log.i("Permission.Marse", "storage.permission requested");
        return false;

    }

    public static boolean locationPermitted(Activity activity, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission.Marse", "location.permissionGranted");
            return true;
        }

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
        Log.i("Permission.Marse", "location.permission requested");
        return false;
    }

    public static boolean phonePermitted(Activity activity, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission.Marse", "phone.permissionGranted");
            return true;
        }

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, requestCode);
        Log.i("Permission.Marse", "phone.permission requested");
        return false;
    }

    public static boolean calenderPermitted(Activity activity, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission.Marse", "WRITE_CALENDAR.permissionGranted");
            return true;
        }

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR}, requestCode);
        Log.i("Permission.Marse", "WRITE_CALENDAR.permission requested");
        return false;
    }

    public static boolean smsPermitted(Activity activity, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission.Marse", "SEND_SMS.permissionGranted");
            return true;
        }

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, requestCode);
        Log.i("Permission.Marse", "SEND_SMS.permission requested");
        return false;
    }
}
