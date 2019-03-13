package nonworkingcode.effects.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.ImageButton;

import com.formationapps.nameart.R;

/**
 * Created by Caliber Fashion on 12/14/2016.
 */

public class EffectsUtils {
    public static final int APP_TYPE_COLLAGE = 0;
    public static final int APP_TYPE_PIP = 1;
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_INDEX = "index";
    public static Context appContext;
    private static EffectsUtils instance;
    private Bitmap tempBitmap;

    public static void onCreate(Context context) {
        appContext = context;
        getInstance();
    }

    public static EffectsUtils getInstance() {
        if (instance == null) {
            instance = new EffectsUtils();
        }
        return instance;
    }

    public static int getColor(Context context, int attrId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    public static void setImage(ImageButton viewById, int ic_clear_all) {
        setImage(viewById, ic_clear_all, getColor(viewById.getContext(), R.attr.color));
    }

    public static void setImage(ImageButton viewById, int res, int color) {
        Drawable stateButtonDrawable = ContextCompat.getDrawable(viewById.getContext(), res).mutate();
        stateButtonDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        viewById.setImageDrawable(stateButtonDrawable);
        viewById.setBackgroundResource(R.drawable.btn_selector);
    }

    public Bitmap getBitmap(boolean copy, boolean recyle) {
        try {
            if (!copy) {
                return tempBitmap;
            }
            if (tempBitmap != null && !tempBitmap.isRecycled()) {
                Bitmap result = tempBitmap.copy(Bitmap.Config.ARGB_8888, true);

                if (!recyle || tempBitmap == null || tempBitmap.isRecycled()) {
                    return result;
                }
                //tempBitmap.recycle();
                return result;
            }
        }catch (OutOfMemoryError e){

        }
        return null;
    }

    public void setBitmap(Bitmap bitmap, boolean copy, boolean recyle) {
        if (bitmap != null && !bitmap.isRecycled()) {
            if (copy) {
                if (!(!recyle || tempBitmap == null || tempBitmap.isRecycled())) {
                    //tempBitmap.recycle();
                }
                tempBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                return;
            }
            tempBitmap = bitmap;
        }
    }
}
