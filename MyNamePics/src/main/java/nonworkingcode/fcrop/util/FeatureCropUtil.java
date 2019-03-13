package nonworkingcode.fcrop.util;

import android.graphics.Bitmap;

/**
 * Created by Caliber Fashion on 12/17/2016.
 */

public class FeatureCropUtil {
    public static FeatureCropUtil instance;
    private Bitmap tempBitmap;

    public static FeatureCropUtil getInstance() {
        if (instance == null)
            instance = new FeatureCropUtil();
        return instance;
    }

    public Bitmap getBitmap(boolean copy, boolean recyle) {

        Bitmap result = null;
        try {
            if (tempBitmap != null && !tempBitmap.isRecycled()) {
                result = Bitmap.createBitmap(tempBitmap);
                //tempBitmap.recycle();
            }
        }catch (OutOfMemoryError e){

        }

        return result;
    }

    public boolean setBitmap(Bitmap bitmap) {

        try {
            if (tempBitmap != null && !tempBitmap.isRecycled()) {
                //tempBitmap.recycle();
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                tempBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                return true;
            } else {
                return false;
            }
        }catch (OutOfMemoryError e){
          return false;
        }catch (Exception e){
            return false;
        }


    }
}
