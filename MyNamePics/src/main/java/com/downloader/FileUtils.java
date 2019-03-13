package com.downloader;

import android.content.Context;

import java.io.File;

/**
 * Created by photomediaapps on 8/15/2017.
 */

public class FileUtils {
    public static File getDataDir(Context context) {

        String path = context.getFilesDir().getAbsolutePath() + "/SampleZip";

        File file = new File(path);

        if (!file.exists()) {

            file.mkdirs();
        }

        return file;
    }

    public static File getDataDir(Context context, String folder) {

        String path = context.getFilesDir().getAbsolutePath() + "/" + folder;

        File file = new File(path);

        if (!file.exists()) {

            file.mkdirs();
        }

        return file;
    }
}
