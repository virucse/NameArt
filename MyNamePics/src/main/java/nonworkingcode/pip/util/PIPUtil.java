package nonworkingcode.pip.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Caliber Fashion on 12/16/2016.
 */

public class PIPUtil {
    public static final int REQ_FOR_FILTER = 50;
    public static Context _appContext;
    public static int screenWidth, screenHeight;
    static DisplayMetrics dm;

    public static void onCreate(Context c) {
        _appContext = c.getApplicationContext();
        initDisplayConfig();
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

    }

    public static void showAndHideView(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
