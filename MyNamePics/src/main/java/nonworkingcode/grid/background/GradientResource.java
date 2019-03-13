package nonworkingcode.grid.background;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

public class GradientResource implements IBgResource {
    private int[] mColors;
    private Orientation mDefacultOrientation;
    private int mGraType;
    private Orientation mOrientation;
    private String name;

    public GradientResource() {
        this.mGraType = 0;
        this.name = "";
        this.mOrientation = Orientation.TOP_BOTTOM;
        this.mDefacultOrientation = Orientation.TOP_BOTTOM;
        this.mColors = new int[2];
    }

    public int[] getColors() {
        return this.mColors;
    }

    public void setColors(int[] var1) {
        this.mColors = var1;
    }

    public Orientation getDefaultOrientation() {
        return this.mDefacultOrientation;
    }

    public void setDefaultOrientation(Orientation var1) {
        this.mDefacultOrientation = var1;
    }

    public int getGraType() {
        return this.mGraType;
    }

    public void setGraType(int var1) {
        this.mGraType = var1;
    }

    public GradientDrawable getGradientDrawable() {
        GradientDrawable gradientDrawable = new GradientDrawable(this.mOrientation, this.mColors);
        if (this.mGraType == 2) {
            int color1 = this.mColors[0];
            int color2 = this.mColors[1];
            int color3 = this.mColors[0];
            gradientDrawable = new GradientDrawable(this.mOrientation, new int[]{color1, color2, color3});
        }
        if (this.mGraType == 1) {
            int color1 = this.mColors[1];
            int color2 = this.mColors[0];
            gradientDrawable = new GradientDrawable(this.mOrientation, new int[]{color1, color2});
            gradientDrawable.setGradientRadius(360.0f);
        }
        gradientDrawable.setGradientType(this.mGraType);
        return gradientDrawable;
    }

    public Orientation getOrientation() {
        return this.mOrientation;
    }

    public void setOrientation(Orientation var1) {
        this.mOrientation = var1;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setContext(Context mContext) {
    }
}
