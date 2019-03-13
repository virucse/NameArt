package nonworkingcode.grid.background;

import android.content.Context;
import android.support.v4.content.ContextCompat;

public class ColorResource implements IBgResource {
    private int color;
    private int colorId;
    private Context mContext;
    private String name;

    ColorResource() {
        this.colorId = 0;
        this.color = 0;
    }

    public ColorResource(Context mContext) {
        this.colorId = 0;
        this.color = 0;
        this.mContext = mContext;
    }

    @Override
    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setColorID(int colorId) {
        this.colorId = colorId;
    }

    public int getColorValue() {
        if (this.colorId != 0) {
            return ContextCompat.getColor(this.mContext, this.colorId);
        }
        return this.color;
    }

    public void setColorValue(int color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
