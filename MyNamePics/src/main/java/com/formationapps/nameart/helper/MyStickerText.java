package com.formationapps.nameart.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.xiaopo.flying.sticker.TextSticker;

import java.util.Random;

/**
 * Created by caliber fashion on 4/26/2017.
 */

public class MyStickerText extends TextSticker {
    private final String emo_regex = "([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])";
    private Bitmap textureBitmap;

    public MyStickerText(Context context) {
        super(context);
    }

    public MyStickerText(Context context, Drawable drawable) {
        super(context, drawable);
    }

    public static SpannableString getSpannableString(String str) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(str);
        for (int i = 0; i < str.length(); i++) {
            int color = getRanColor();
            try {
                if (str.length() > 1 && Character.isSurrogatePair(str.charAt(i), str.charAt(i + 1))) {
                    int codePoint = Character.toCodePoint(str.charAt(i), str.charAt(i + 1));
                    char[] c = Character.toChars(codePoint);
                    //i=i+1;
                } else {
                    spannableString.setSpan(new ForegroundColorSpan(color), i, i + 1, 0);
                    spannableStringBuilder.append(spannableString);
                }
            } catch (StringIndexOutOfBoundsException e) {

            }
        }
        return spannableString;
    }

    private static int getRanColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public void setTextureBitmap(Bitmap b) {
        if (b == null) {
            return;
        }
        if (b != null && !b.isRecycled()) {

        } else {
            return;
        }
        if (textureBitmap != null && !textureBitmap.isRecycled()) {
            //textureBitmap.recycle();
        }
        textureBitmap = b;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.texture);
        Shader shader = new BitmapShader(textureBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        setShader(shader);
    }

    @Override
    public TextSticker setTextColor(int color) {
        setShader(null);
        return super.setTextColor(color);
    }

    public void setCreative() {
        SpannableString ss = getSpannableString(getText());
        setCreativeText(ss);
    }
}
