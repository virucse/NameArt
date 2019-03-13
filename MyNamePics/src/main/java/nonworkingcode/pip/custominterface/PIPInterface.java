package nonworkingcode.pip.custominterface;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.File;

public interface PIPInterface {
    public Bitmap getBitmap();

    public int getFrameId();

    public View getView();

    public void handleImage();

    public boolean hasShadow();

    public void picFill();

    public void picToCenter();

    public void postInvalidate();

    public void resetToOriEffect(IOnFilterFinishedListener iOnFilterFinishedListener);

    public void reversal(float f);

    public void setCustomBorderId(int i, File frameFile);

    public void setHueValue(float f);

    public void setRotateDegree(float f);

    public void setShadow(int i);

    public void setSquareBackground(Bitmap bitmap);

    public void setSquareBackground(Drawable drawable);

    public void zoom(float f);
}
