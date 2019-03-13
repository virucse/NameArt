package com.formationapps.nameart.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.formationapps.nameart.R;

/**
 * Created by caliber fashion on 3/29/2017.
 */

public class FontHolderView extends FrameLayout {
    public FontHolderView(@NonNull Context context) {
        super(context);
    }

    public FontHolderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FontHolderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FontHolderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setFont(Activity activity, String fontName, FONTSOURCE fontsource) {
        if (fontsource == FONTSOURCE.ASSETS) {
            View view1 = activity.getLayoutInflater().inflate(R.layout.text_raw_item, null);
            try {
                TextView text = (TextView) view1.findViewById(R.id.txtFont);
                text.setText("name");
                text.setTypeface(TypefacesUtils.get(activity, fontName));
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sample_text_font_size));
                /*view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppUtils.textSticker == null) {
                            Toast.makeText(NameEditorActivity.this.getApplication(),
                                    "Please select text first", Toast.LENGTH_LONG).show();
                            return;
                        }
                        AppUtils.textSticker.setTypeface(TypefacesUtils.get(NameEditorActivity.this, fontName));
                        stickerView.replace(AppUtils.textSticker);
                    }
                });*/
            } catch (Exception e) {
            }
        } else if (fontsource == FONTSOURCE.FIREBASE) {

        }
    }

    private void onFontSourceFirebase() {

    }

    public void setClickListener() {

    }

    public enum FONTSOURCE {ASSETS, HTTP, FIREBASE}
}
