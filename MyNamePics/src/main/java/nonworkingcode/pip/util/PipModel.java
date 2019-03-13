package nonworkingcode.pip.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.PointF;
import android.graphics.Rect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PipModel {
    public String cover;
    public String icon;
    public int id;
    public String mask;
    public String name;
    public String sizeFile;
    public Rect rect;
    public PointF size;

    public PipModel(int id) {
        this.id = id;
    }

    public PipModel(int id, String icon, String mask, String cover, Rect rect, PointF size) {
        this.id = id;
        this.icon = icon;
        this.mask = mask;
        this.cover = cover;
        this.rect = rect;
        this.size = size;
    }

    public PipModel(int id, String name, String icon, EResType asset, String cover, PointF size, Rect rect, String mask) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.mask = mask;
        this.cover = cover;
        this.rect = rect;
        this.size = size;
    }

    public PipModel(String icon, String cover, String mask, String pointString) {
        this.icon = icon;
        this.cover = cover;
        this.mask = mask;
        this.sizeFile = pointString;
        size = new PointF(612, 612);
    }

    public Bitmap getIconBitmap(Context context) {
        InputStream stream;
        try {
            stream = context.getAssets().open("pip/" + icon);
            Bitmap decodeStream = BitmapFactory.decodeStream(stream);
            stream.close();
            return decodeStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getCoverBitmap(Context context) {
        InputStream stream;
        try {
            stream = context.getAssets().open("pip/" + cover);
            Bitmap decodeStream = BitmapFactory.decodeStream(stream);
            stream.close();
            return decodeStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getMaskBitmap(Context context) {
        try {
            InputStream stream = context.getAssets().open("pip/" + mask);
            Options opts = new Options();
            opts.inPreferredConfig = Config.ARGB_8888;
            Bitmap source = BitmapFactory.decodeStream(stream, null, opts);
            Bitmap mask = source.extractAlpha();
            stream.close();
            source.recycle();
            return mask;
        } catch (IOException e) {
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
