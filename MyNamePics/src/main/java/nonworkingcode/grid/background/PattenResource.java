package nonworkingcode.grid.background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.File;

public class PattenResource implements IBgResource {
    private final String TAG = PattenResource.class.getName();
    private File iconName;
    private File imageFile;
    private Context mContext;
    private String name;
    private int type;

    public PattenResource() {
        this.name = "";
        this.type = 0;
    }

    @Override
    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setIconName(String var2) {
        //this.iconName = var2;
    }

    public void setImageName(String var4) {
        //this.imageFile = var4;
    }

    public void setIconFileName(File name) {
        iconName = name;
    }

    public void setImageFileName(File name) {
        imageFile = name;
    }

    public Bitmap getIcon() {
        try {
            return BitmapFactory.decodeFile(iconName.getAbsolutePath());
            //return BitmapFactory.decodeStream(this.mContext.getAssets().open("imagess/" + this.iconName));
        } catch (Exception e) {
            Log.d(TAG, "getIcon: " + e.getMessage());
            return null;
        }
    }

    public BitmapDrawable getDrawable() {
        try {
            BitmapDrawable d = new BitmapDrawable(this.mContext.getResources(),
                    BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            //BitmapDrawable d = new BitmapDrawable(this.mContext.getResources(),
            // BitmapFactory.decodeStream(this.mContext.getAssets().open("imagess/" + this.imageFile)));
            d.setDither(true);
            if (this.type == 0) {
                d.setTileModeX(TileMode.MIRROR);
                d.setTileModeY(TileMode.MIRROR);
                return d;
            }
            if (this.type == 1) {
                d.setTileModeX(TileMode.REPEAT);
                d.setTileModeY(TileMode.REPEAT);
                return d;
            }

        } catch (Exception e) {
            Log.d(TAG, "getDrawable: " + e.getMessage());
        }
        return null;
    }

    public void setType(int type) {
        this.type = type;
    }
}
