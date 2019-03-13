package com.formationapps.nameart.helper;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static String getFolderName(String name) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), name);
        if (mediaStorageDir.exists() || mediaStorageDir.mkdirs()) {
            return mediaStorageDir.getAbsolutePath();
        }
        return name;
    }

    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static File getNewFile(Context context, String folderName) {
        String path;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        if (isSDAvailable()) {
            path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
        } else {
            path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
        }
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return new File(path);
    }
}
