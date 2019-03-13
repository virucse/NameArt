package billingmodule.billing;

import android.view.View;

/**
 * Created by caliber fashion on 11/9/2017.
 */

public interface PurchaseItemClickListener {
    public void onPermanentPurchaseClick(View view);

    public void onYearlyPurchaseClick(View view);
}
