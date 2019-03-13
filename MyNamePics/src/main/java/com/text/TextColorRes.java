package com.text;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by caliber fashion on 9/19/2017.
 */

public class TextColorRes implements TextRes {
    private Context mContext;
    private int colorPosition;
    private String colorValue;

    @Override
    public String getName() {
        return null;
    }

    public void setName(String str) {

    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setValueColor(String color) {
        colorValue = color;
    }

    @Override
    public int getColorValue() {
        return Color.parseColor(colorValue);
    }

    @Override
    public int getPosition() {
        return colorPosition;
    }

    public void setPosition(int pos) {
        colorPosition = pos;
    }
}
