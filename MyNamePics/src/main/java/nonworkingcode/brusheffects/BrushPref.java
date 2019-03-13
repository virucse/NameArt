package nonworkingcode.brusheffects;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;

/**
 * Created by caliber fashion on 11/2/2017.
 */

public class BrushPref {
    public static void setIsBrushRandomColor(Context context, String key, boolean isTrue) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, isTrue).apply();
    }

    public static boolean getIsBrushRandomColor(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, true);
    }

    public static void setColor(Context context, String key, int color) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key + "cc", color).apply();
    }

    public static int getColor(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key + "cc", Color.RED);
    }

    public static void setModifiedBrushPos(Context context, int pos) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("lmbp", pos).apply();
    }

    public static int getModifiedBrushPos(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("lmbp", 0);
    }
}
