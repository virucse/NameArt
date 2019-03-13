package nonworkingcode.brusheffects;

import android.content.Context;

public class GetDeviceWidth {
    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
