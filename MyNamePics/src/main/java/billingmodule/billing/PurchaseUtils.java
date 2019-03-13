package billingmodule.billing;

import android.content.Context;

/**
 * Created by caliber fashion on 11/11/2017.
 */

public class PurchaseUtils {
    public static void setYearlyPurchase(Context context, boolean isYerly) {
        context.getSharedPreferences("purchasesp", Context.MODE_PRIVATE).edit().putBoolean("isYearlyPur", isYerly).commit();
    }

    public static boolean isYearlyPurchased(Context context) {
        return context.getSharedPreferences("purchasesp", Context.MODE_PRIVATE).getBoolean("isYearlyPur", false);
    }

    public static void setPermanentPurchase(Context context, boolean isPermanent) {
        context.getSharedPreferences("purchasesp", Context.MODE_PRIVATE).edit().putBoolean("isPermanentPur", isPermanent).commit();
    }

    public static boolean isPermanentPurchased(Context context) {
        return context.getSharedPreferences("purchasesp", Context.MODE_PRIVATE).getBoolean("isPermanentPur", false);
    }
}
