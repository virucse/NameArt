package nonworkingcode.pip.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.PointF;
import android.graphics.Rect;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PIPFileModel {
    public File cover;
    public File icon;
    public int id;
    public File mask;
    public String name;
    public String sizeFile;
    public Rect rect;
    public PointF size;

    public PIPFileModel(int id) {
        this.id = id;
    }

    public PIPFileModel(int id, File icon, File mask, File cover, Rect rect, PointF size) {
        this.id = id;
        this.icon = icon;
        this.mask = mask;
        this.cover = cover;
        this.rect = rect;
        this.size = size;
    }

    public PIPFileModel(int id, String name, File icon, EResType asset, File cover, PointF size, Rect rect, File mask) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.mask = mask;
        this.cover = cover;
        this.rect = rect;
        this.size = size;
    }

    public PIPFileModel(File icon, File cover, File mask, String pointString) {
        this.icon = icon;
        this.cover = cover;
        this.mask = mask;
        this.sizeFile = pointString;
        size = new PointF(612, 612);
    }

    public Bitmap getIconBitmap(Context context) {

        try {

            Bitmap decodeStream = BitmapFactory.decodeFile(icon.getAbsolutePath());

            return decodeStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getCoverBitmap(Context context) {
        try {
            Bitmap decodeStream = BitmapFactory.decodeFile(cover.getAbsolutePath());
            return decodeStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getMaskBitmap(Context context) {
        try {

            Options opts = new Options();
            opts.inPreferredConfig = Config.ARGB_8888;
            Bitmap source = BitmapFactory.decodeFile(mask.getAbsolutePath(), opts);
            Bitmap mask = source.extractAlpha();
            source.recycle();
            return mask;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rect getSize(Context context) {
        int left = 0, top = 0, right = 0, bootom = 0;
        if (sizeFile == null) {
            return null;
        }
        String str = sizeFile;//readTextFromAssets(context,"pip/"+sizeFile);
        if (str != null) {
            String[] list = str.split("_");
            left = Integer.parseInt(list[0].trim());
            top = Integer.parseInt(list[1].trim());
            if (mask != null) {
                Bitmap b = getMaskBitmap(context);
                right = b != null ? b.getWidth() : 0;
                bootom = b != null ? b.getHeight() : 0;
                if (b != null) b.recycle();
            }
        }
        rect = new Rect(left, top, left + right, top + bootom);
        return rect;

    }

    private String readTextFromAssets(Context context, String fileName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            String mLine = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (mLine != null) {
                sb.append(mLine);
                mLine = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum EResType {
        ASSET,
        RES
    }
}
