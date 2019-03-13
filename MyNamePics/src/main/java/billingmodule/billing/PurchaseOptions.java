package billingmodule.billing;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;

import com.formationapps.nameart.R;

/**
 * Created by caliber fashion on 11/9/2017.
 */

public class PurchaseOptions extends Dialog {
    private PurchaseItemClickListener mPurchaseItemClickListener;
    private int selectedId;

    public PurchaseOptions(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PurchaseOptions(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected PurchaseOptions(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public void setmPurchaseItemClickListener(PurchaseItemClickListener listener) {
        mPurchaseItemClickListener = listener;
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.purchase_option);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                selectedId = checkedId;
            }
        });
        Button upgrade = (Button) findViewById(R.id.upgradeToPremium);
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpgradeClick(v);
            }
        });
        selectedId = R.id.yearly_purchase;
    }

    private void onUpgradeClick(View view) {
        if (selectedId == R.id.yearly_purchase) {
            if (mPurchaseItemClickListener != null) {
                mPurchaseItemClickListener.onYearlyPurchaseClick(view);
            }
        } else if (selectedId == R.id.permanent_purchase) {
            mPurchaseItemClickListener.onPermanentPurchaseClick(view);
        }
    }
}
