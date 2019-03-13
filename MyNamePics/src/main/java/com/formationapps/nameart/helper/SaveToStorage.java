package com.formationapps.nameart.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;

import com.formationapps.nameart.R;

public class SaveToStorage {
	private static File checkPath(File paramFile, String paramString) {
		File localFile = new File(paramFile, paramString);
		if (!localFile.exists())
			localFile.mkdir();
		return localFile;
	}

	public static String generateImageName() {
		return "IMG_" + DateFormat.format("yyyyMMdd_kkmmss", new Date())
				+ ".jpg";
	}
	//acceptable format is jpg,png,jpeg
	public static String generateImageName(String format){
		return "IMG_" + DateFormat.format("yyyyMMdd_kkmmss", new Date())
		+ "."+format;
	}

	public static String getFolderPath(Context paramcontext) {
		File localFile = checkPath(Environment.getExternalStorageDirectory(),
				getAppName(paramcontext));
		return localFile.getPath();
	}
	public static File getFolderFile(Context paramcontext) {
		File localFile = checkPath(Environment.getExternalStorageDirectory(),
				getAppName(paramcontext));
		return localFile;
	}
	public static String getAppName(Context context){
		return context.getResources().getString(R.string.app_name);
	}

	public synchronized static String save(Bitmap paramBitmap, Context paramContext,OnSaveListener listener) throws FileNotFoundException {
		String str = getFolderPath(paramContext);
		return save(paramBitmap, str+ "/"+ generateImageName(), paramContext,listener);
	}

	public synchronized static String save(Bitmap paramBitmap, String paramString,
			Context paramContext,OnSaveListener listener) throws FileNotFoundException {
		long timeStamp = System.currentTimeMillis();
		FileOutputStream localFileOutputStream = null;

			localFileOutputStream = new FileOutputStream(paramString);
			paramBitmap.compress(Bitmap.CompressFormat.PNG, 100,
					localFileOutputStream);

		ContentValues values = new ContentValues();
		values.put("datetaken", Long.valueOf(timeStamp));

		values.put("mime_type", "image/jpeg");
		values.put("_data", paramString);
		values.put("description", getAppName(paramContext)+" @Formationapps");
		values.put("_size",paramBitmap.getWidth()+" X "+paramBitmap.getHeight());
		paramContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		try {
			localFileOutputStream.flush();
			localFileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(listener!=null){
			listener.onSaveComplete(paramString);
		}

		return paramString;
	}
	public interface OnSaveListener{
		public void onSaveComplete(String path);
	}
}
