package nonworkingcode.grid.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.formationapps.nameart.helper.AppUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.media.ExifInterface.ORIENTATION_FLIP_HORIZONTAL;
import static android.media.ExifInterface.ORIENTATION_FLIP_VERTICAL;
import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.media.ExifInterface.ORIENTATION_TRANSPOSE;
import static android.media.ExifInterface.ORIENTATION_TRANSVERSE;

public class ImageUtils {
    public static final String DOWNLOAD_IMAGE_PATH;
    public static final String DOWNLOAD_IMAGE_PATH_TEMP;
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_FILE = "file";
    static ImageUtils _ImageUtils;

    static {
        DOWNLOAD_IMAGE_PATH_TEMP = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/formationapps/.temp";
        DOWNLOAD_IMAGE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/formationapps/MyNamePicsNameArt";
        _ImageUtils = new ImageUtils();
    }

    private ImageUtils() {

    }

    public static ImageUtils getinstance() {
        return _ImageUtils;
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String getSaveMediaFileName() {
        return "Beautify_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    @SuppressLint({"SimpleDateFormat"})
    public static File getTempMediaFile() {
        File mediaStorageDir = new File(DOWNLOAD_IMAGE_PATH_TEMP);
        if (!(mediaStorageDir.exists() || mediaStorageDir.mkdirs())) {
            mediaStorageDir = AppUtils.mContext.getFilesDir();
        }
        return new File(mediaStorageDir.getAbsolutePath() + File.separator + "temp_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
    }

    public static void closeSilently(@Nullable Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable th) {
            }
        }
    }

    @Nullable
    public static File getFromMediaUri(Context context, ContentResolver resolver, Uri uri) {
        File file;
        if (uri == null) {
            return null;
        }
        if (SCHEME_FILE.equals(uri.getScheme())) {
            return new File(uri.getPath());
        }
        if (SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, new String[]{"_data", "_display_name"}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex;
                    if (uri.toString().startsWith("content://com.google.android.gallery3d")) {
                        columnIndex = cursor.getColumnIndex("_display_name");
                    } else {
                        columnIndex = cursor.getColumnIndex("_data");
                    }
                    if (columnIndex != -1) {
                        String filePath = cursor.getString(columnIndex);
                        if (!TextUtils.isEmpty(filePath)) {
                            file = new File(filePath);
                            if (cursor == null) {
                                return file;
                            }
                            cursor.close();
                            return file;
                        }
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (IllegalArgumentException e) {
                file = getFromMediaUriPfd(context, resolver, uri);
                if (cursor == null) {
                    return file;
                }
                cursor.close();
                return file;
            } catch (SecurityException e2) {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return null;
    }

    /*public Uri saveFile(Context context, Bitmap finalBitmap, boolean high) {
        if (finalBitmap == null) {
            return null;
        }
        long timeStamp = System.currentTimeMillis();
        File myDir = new File(DOWNLOAD_IMAGE_PATH);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fileName = getSaveMediaFileName();
        File file = new File(myDir, new StringBuilder(String.valueOf(fileName)).append(high ? ".png" : ".jpg").toString());
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (high) {
                finalBitmap.compress(CompressFormat.PNG, 100, out);
            } else {
                finalBitmap.compress(CompressFormat.JPEG, 100, out);
            }
            out.flush();
            out.close();
            if (file == null || !file.exists()) {
                return null;
            }
            ContentValues values = new ContentValues();
            values.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, fileName);
            values.put("_display_name", fileName);
            values.put("datetaken", Long.valueOf(timeStamp));
            if (high) {
                values.put("mime_type", "image/png");
            } else {
                values.put("mime_type", "image/jpeg");
            }
            values.put("_data", file.getAbsolutePath());
            values.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, "PIP Photo Editor @Beautify");
            context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    private static String getTempFilename(Context context) throws IOException {

        return File.createTempFile("formationapps", "tmp", context.getCacheDir()).getAbsolutePath();
    }

    @Nullable
    private static File getFromMediaUriPfd(Context context, ContentResolver resolver, Uri uri) {
        Throwable th;
        if (uri == null) {
            return null;
        }
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            FileInputStream input2 = new FileInputStream(resolver.openFileDescriptor(uri, "r").getFileDescriptor());
            try {
                String tempFilename = getTempFilename(context);
                FileOutputStream output2 = new FileOutputStream(tempFilename);
                try {
                    byte[] bytes = new byte[4096];
                    while (true) {
                        int read = input2.read(bytes);
                        if (read == -1) {
                            File file = new File(tempFilename);
                            closeSilently(input2);
                            closeSilently(output2);
                            return file;
                        }
                        output2.write(bytes, 0, read);
                    }
                } catch (IOException e) {
                    output = output2;
                    input = input2;
                } catch (Throwable th2) {
                    th = th2;
                    output = output2;
                    input = input2;
                }
            } catch (IOException e2) {
                input = input2;
                closeSilently(input);
                closeSilently(output);
                return null;
            } catch (Throwable th3) {
                th = th3;
                input = input2;
                closeSilently(input);
                closeSilently(output);
                throw th;
            }
        } catch (IOException e3) {
            closeSilently(input);
            closeSilently(output);
            return null;
        } catch (Throwable th4) {
            th = th4;
            closeSilently(input);
            closeSilently(output);
            return null;
        }
        return null;
    }

    private Bitmap loadImageBitmap(Context context, Uri var2, int maxW, int maxH) {
        try {
            InputStream is = context.getContentResolver().openInputStream(var2);
            Options option = new Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, option);
            is.close();
            is = context.getContentResolver().openInputStream(var2);
            option.inSampleSize = calculateSampleSize(option, maxW, maxH);
            option.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, option);
            is.close();
            try {
                return getOrientedBitmap(bitmap, new ExifInterface(getFromMediaUri(context, context.getContentResolver(), var2).getAbsolutePath()).getAttributeInt("Orientation", 0), true);
            } catch (Exception e) {
                Log.e("Exif error", "");
                return bitmap;
            }
        } catch (IOException e2) {
            return null;
        }
    }

    public Bitmap loadBitmap(Context param1, Uri param2, int num) {
        if (num == 0) {
            num = 1;
        }
        int size = (int) ((((float) AppUtils.screenHeight) * 3.0f) / ((float) num));
        return loadImageBitmap(param1, param2, size, size);
    }

    public Bitmap loadImage(Context context, Uri var2, int maxW, int maxH) {
        if (var2 == null) {
            Toast.makeText(context, "var2 null", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            if (SCHEME_FILE.equals(var2.getScheme())) {
                Toast.makeText(context, "SCHEME_FILE", Toast.LENGTH_SHORT).show();
                return loadImage(context, new File(var2.getPath()), maxW, maxH);
            }
            var2 = Uri.fromFile(new File(var2.toString()));
            InputStream is = context.getContentResolver().openInputStream(var2);
            Options option = new Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, option);
            is.close();
            is = context.getContentResolver().openInputStream(var2);
            option.inSampleSize = calculateSampleSize(option, maxW, maxH);
            option.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, option);
            is.close();
            try {
                return getOrientedBitmap(bitmap, new ExifInterface(getFromMediaUri(context, context.getContentResolver(), var2).getAbsolutePath()).getAttributeInt("Orientation", 0), true);
            } catch (Exception e) {
                Log.e("Exif error", "");
                Toast.makeText(context, "Exif Error", Toast.LENGTH_SHORT).show();
                return bitmap;
            }
        } catch (IOException e2) {
            Toast.makeText(context, "" + e2.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public Bitmap loadImage(Context context, File f, int maxW, int maxH) {
        try {
            InputStream is = context.getContentResolver().openInputStream(Uri.fromFile(f));
            Options option = new Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, option);
            is.close();
            is = context.getContentResolver().openInputStream(Uri.fromFile(f));
            option.inSampleSize = calculateSampleSize(option, maxW, maxH);
            option.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, option);
            is.close();
            try {
                return getOrientedBitmap(bitmap, new ExifInterface(f.getAbsolutePath()).getAttributeInt("Orientation", 0), true);
            } catch (Exception e) {
                Log.e("Exif error", "");
                return bitmap;
            }
        } catch (IOException e2) {
            return null;
        }
    }

    public File getFile(Context context, Uri uri) {
        return getFromMediaUri(context, context.getContentResolver(), uri);
    }

    public Bitmap getOrientedBitmap(Bitmap source, int orientation, boolean reset) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ORIENTATION_FLIP_HORIZONTAL /*2*/:
                matrix.setScale(-1f, 1.0f);
                break;
            case ORIENTATION_ROTATE_180 /*3*/:
                matrix.setRotate(180.0f);
                break;
            case ORIENTATION_FLIP_VERTICAL/*4*/:
                matrix.setRotate(180.0f);
                matrix.postScale(-1f, 1.0f);
                break;
            case ORIENTATION_TRANSPOSE /*5*/:
                matrix.setRotate(90.0f);
                matrix.postScale(-1f, 1.0f);
                break;
            case ORIENTATION_ROTATE_90 /*6*/:
                matrix.setRotate(90.0f);
                break;
            case ORIENTATION_TRANSVERSE /*7*/:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1f, 1.0f);
                break;
            case ORIENTATION_ROTATE_270 /*8*/:
                matrix.setRotate(-90.0f);
                break;
            default:
                return source;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            if (reset) {
                source.recycle();
            }
            return bitmap;
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            return null;
        }
    }

    private int calculateSampleSize(Options options, int sizex, int sizey) throws IOException {
        int sampleSize = 1;
        while (true) {
            if (options.outHeight / sampleSize <= sizey && options.outWidth / sampleSize <= sizex) {
                return sampleSize;
            }
            sampleSize <<= 1;
        }
    }
}
