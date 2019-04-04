package com.removebg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;

import java.io.IOException;

public class BgEraserImageUtils {
    public static Bitmap getResampleImageBitmap(Uri uri, Context context) throws IOException {
        String pathInput = getRealPathFromURI(uri, context);
        try {
            return resampleImage(pathInput, 900);
        } catch (Exception e) {
            e.printStackTrace();
            return Media.getBitmap(context.getContentResolver(), Uri.parse(pathInput));
        }
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context, int maxDim) throws IOException {
        Bitmap bmp = null;
        try {
            bmp = resampleImage(getRealPathFromURI(uri, context), maxDim);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @SuppressLint({"UseValueOf"})
    public static Bitmap resampleImage(String path, int maxDim) throws Exception {
        try {
            Options bfo = new Options();
            bfo.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bfo);
            Options optsDownSample = new Options();
            optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim);
            Bitmap bmpt = BitmapFactory.decodeFile(path, optsDownSample);
            if (bmpt == null) {
                return null;
            }
            Matrix m = new Matrix();
            if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
                Options optsScale = getResampling(bmpt.getWidth(), bmpt.getHeight(), maxDim);
                m.postScale(((float) optsScale.outWidth) / ((float) bmpt.getWidth()), ((float) optsScale.outHeight) / ((float) bmpt.getHeight()));
            }
            if (new Integer(VERSION.SDK) > 4) {
                int rotation = 0;//ExifUtils.getExifRotation(path);
                if (rotation != 0) {
                    m.postRotate((float) rotation);
                }
            }
            return Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(), bmpt.getHeight(), m, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Options getResampling(int cx, int cy, int max) {
        float scaleVal;
        Options bfo = new Options();
        if (cx > cy) {
            scaleVal = ((float) max) / ((float) cx);
        } else if (cy > cx) {
            scaleVal = ((float) max) / ((float) cy);
        } else {
            scaleVal = ((float) max) / ((float) cx);
        }
        bfo.outWidth = (int) ((((float) cx) * scaleVal) + 0.5f);
        bfo.outHeight = (int) ((((float) cy) * scaleVal) + 0.5f);
        return bfo;
    }

    private static int getClosestResampleSize(int cx, int cy, int maxDim) {
        int max = Math.max(cx, cy);
        int resample = 1;
        while (resample < Integer.MAX_VALUE) {
            if (resample * maxDim > max) {
                resample--;
                break;
            }
            resample++;
        }
        return resample > 0 ? resample : 1;
    }

    public static Options getBitmapDims(Uri uri, Context context) {
        String path = getRealPathFromURI(uri, context);
        Options bfo = new Options();
        bfo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bfo);
        return bfo;
    }

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) {
                return contentURI.getPath();
            }
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex("_data"));
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return contentURI.toString();
        }
    }

    public static Bitmap resizeBitmap(Bitmap bit, int width, int height) {
        if (bit == null) {
            return null;
        }
        float wr = (float) width;
        float hr = (float) height;
        float wd = (float) bit.getWidth();
        float he = (float) bit.getHeight();
        float rat1 = wd / he;
        float rat2 = he / wd;
        if (wd > wr) {
            wd = wr;
            he = wd * rat2;
            if (he > hr) {
                he = hr;
                wd = he * rat1;
            } else {
                wd = wr;
                he = wd * rat2;
            }
        } else if (he > hr) {
            he = hr;
            wd = he * rat1;
            if (wd > wr) {
                wd = wr;
                he = wd * rat2;
            } else {

            }
        } else if (rat1 > 0.75f) {
            wd = wr;
            he = wd * rat2;
        } else if (rat2 > 1.5f) {
            he = hr;
            wd = he * rat1;
        } else {
            wd = wr;
            he = wd * rat2;
        }
        return Bitmap.createScaledBitmap(bit, (int) wd, (int) he, false);
    }

    public static int dpToPx(Context c, int dp) {
        float f = (float) dp;
        c.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
    }

    public static Bitmap mergelogo(Bitmap bitmap, Bitmap logo) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        float wr = (float) bitmap.getWidth();
        float hr = (float) bitmap.getHeight();
        float wd = (float) logo.getWidth();
        float he = (float) logo.getHeight();
        float rat1 = wd / he;
        float rat2 = he / wd;
        if (wd > wr) {
            wd = wr;
            logo = Bitmap.createScaledBitmap(logo, (int) wd, (int) (wd * rat2), false);
        } else if (he > hr) {
            he = hr;
            logo = Bitmap.createScaledBitmap(logo, (int) (he * rat1), (int) he, false);
        }
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(logo, (float) (bitmap.getWidth() - logo.getWidth()), (float) (bitmap.getHeight() - logo.getHeight()), null);
        return result;
    }

    public static Bitmap CropBitmapTransparency(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                if (((sourceBitmap.getPixel(x, y) >> 24) & 255) > 0) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
        if (maxX < minX || maxY < minY) {
            return null;
        }
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    public static Bitmap bitmapmasking(Bitmap original, Bitmap mask) {
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        mCanvas.drawBitmap(original, 0.0f, 0.0f, null);
        mCanvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return result;
    }

   /* public static Bitmap getInvertMask(Bitmap mask) {
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        mCanvas.drawColor(Color.parseColor("#303030"));
        mCanvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return result;
    }*/

    public static Bitmap bitmapmasking1(Bitmap original, Bitmap mask) {
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        if(original!=null&&!original.isRecycled())
            mCanvas.drawBitmap(original, 0.0f, 0.0f, null);
        if(mask!=null&&!mask.isRecycled())
            mCanvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return result;
    }

    public static Bitmap getTiledBitmap(Context ctx, int resId, int width, int height) {
        Rect rect = new Rect(0, 0, width, height);
        Paint paint = new Paint();
        Options options = new Options();
        options.inScaled = false;
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(ctx.getResources(), resId, options), TileMode.REPEAT, TileMode.REPEAT));
        Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        new Canvas(b).drawRect(rect, paint);
        return b;
    }

    /*public static Bitmap getBgCircleBit(Context ctx, int resId) {
        int bitmappx = dpToPx(ctx, 150);
        return bitmapmasking1(getTiledBitmap(ctx, resId, bitmappx, bitmappx),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.circle), bitmappx, bitmappx, true));
    }*/
}
