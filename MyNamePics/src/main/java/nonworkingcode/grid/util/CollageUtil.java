package nonworkingcode.grid.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.formationapps.nameart.R;

import nonworkingcode.grid.mcacheloader.ImageLoaderInMemory;

/**
 * Created by Caliber Fashion on 12/9/2016.
 */

public class CollageUtil {
    public static final int REQ_REPLACE_IMAGE = 8;
    public static final int REQ_EDIT_IMAGE = 9;
    public static final String EXTRA_INDEX = "index";
    public static int screenHeight;
    public static int screenWidth;
    public static Context _appContext;
    public static int collageHeight = 0;
    public static int collageWidth = 0;
    public static int statusBarHeight;
    private static DisplayMetrics dm;

    public static void onCreate(Context c) {
        _appContext = c.getApplicationContext();
        initDisplayConfig();
        ImageLoaderInMemory.getInstance();
    }

    public static void onDestroy() {
        ImageLoaderInMemory.getInstance().clearCache();
    }

    public static void initDisplayConfig() {
        double factor;
        dm = _appContext.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        int result = 25;
        int resourceId = _appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = _appContext.getResources().getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
        double width = (double) Math.min((((float) screenHeight) * 440.0f) / 800.0f, ((float) screenWidth) - dpToPx(40.0f));
        if (isTablet(_appContext)) {
            factor = 0.8d;
        } else {
            factor = 1.0d;
        }
        int collageBaseWidth = (int) (factor * width);
        collageWidth = collageBaseWidth;
        collageHeight = collageBaseWidth;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 4;
    }

    public static float dpToPx(float f) {
        if (_appContext == null) {
            return f * 2.0f;
        }
        if (dm == null) {
            dm = _appContext.getResources().getDisplayMetrics();
        }
        try {
            return TypedValue.applyDimension(1, f, dm);
        } catch (Exception e) {
            return f;
        }
    }

    public static void setImage(ImageButton viewById, int ic_clear_all) {
        setImage(viewById, ic_clear_all, getColor(viewById.getContext(), R.attr.color));
    }

    public static int getColor(Context context, int attrId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    public static void setImage(ImageButton viewById, int res, int color) {
        Drawable stateButtonDrawable = ContextCompat.getDrawable(viewById.getContext(), res).mutate();
        stateButtonDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        viewById.setImageDrawable(stateButtonDrawable);
        viewById.setBackgroundResource(R.drawable.btn_selector);
    }

    @SuppressLint({"NewApi"})
    public static void setSeekBarThumb(SeekBar seek) {
        if (Build.VERSION.SDK_INT >= 16) {
            Drawable d = seek.getThumb();
            if (d != null) {
                d.setColorFilter(getColor(seek.getContext(), R.attr.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                seek.setThumb(d);
            }
        }
    }

    public static void setSelector(ImageButton ib, int resId) {
        int color = getColor(ib.getContext(), R.attr.colorControlHighlight);
        Drawable stateButtonDrawable = ContextCompat.getDrawable(ib.getContext(), resId).mutate();
        Drawable stateButtonDrawablePress = ContextCompat.getDrawable(ib.getContext(), resId).mutate();
        stateButtonDrawablePress.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{16842913}, stateButtonDrawablePress);
        drawable.addState(new int[]{16842919}, stateButtonDrawablePress);
        drawable.addState(StateSet.WILD_CARD, stateButtonDrawable);
        ib.setImageDrawable(drawable);
        ib.setBackgroundResource(R.drawable.btn_selector);
    }

    public static void hideAndShow(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }

    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
}
