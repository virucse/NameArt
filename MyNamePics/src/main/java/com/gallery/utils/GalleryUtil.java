package com.gallery.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageButton;

import com.formationapps.nameart.R;

/**
 * Created by Caliber Fashion on 12/14/2016.
 */

public class GalleryUtil {
    public static final int APP_TYPE_DEFAULT = -1;
    public static final int APP_TYPE_COLLAGE = 0;
    public static final int APP_TYPE_PIP = 1;
    public static final int APP_TYPE_EDITOR = 2;
    public static final int APP_TYPE_GIF = 3;
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_INDEX = "index";
    public static final int ALBUM_TYPE = 100;
    public static final int PHOTO_TYPE = 101;
    public static final int SELECTED_TYPE = 102;
    public static final int MAX_IMAGE_SELECTED = 10;
    private static final int REQ_COLLAGE = 121;
    private static final int REQ_PIP = 124;
    public static Context context;
    public static int screenWidth, screenHeight;
    private static DisplayMetrics dm;

    public static void onCreate(Context c) {
        context = c;
        initDisplayConfig();
    }

    public static void initDisplayConfig() {
        double factor;
        dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        int result = 25;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
       /* statusBarHeight = result;
        double width = (double) Math.min((((float) screenHeight) * 440.0f) / 800.0f, ((float) screenWidth) - dpToPx(40.0f));
        if (isTablet(_appContext)) {
            factor = 0.8d;
        } else {
            factor = 1.0d;
        }
        int collageBaseWidth = (int) (factor * width);
        collageWidth = collageBaseWidth;
        collageHeight = collageBaseWidth;*/
    }

    public static int getColor(Context context, int attrId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    public static void setImage(ImageButton viewById, int res) {
        setImage(viewById, res, getColor(viewById.getContext(), R.attr.color));
    }

    public static void setImage(ImageButton viewById, int res, int color) {
        Drawable stateButtonDrawable = ContextCompat.getDrawable(viewById.getContext(), res).mutate();
        stateButtonDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        viewById.setImageDrawable(stateButtonDrawable);
        viewById.setBackgroundResource(R.drawable.btn_selector);
    }
}
