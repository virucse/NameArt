package nonworkingcode.grid.custominterface;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import java.io.File;

public interface CollageInterface {
    void addView(View view);

    int m41b(Point point);

    Bitmap getDrawingBitmap(int i, int i2);

    int getImageListSize();

    int getPositionAtPoint(Point point);

    View getView();

    View getViewAtPosition(int i);

    void handleImage();

    void recycleBackgroud();

    void recycleDrawable(Drawable drawable);

    void removeView(View view);

    void resetBitmap(Bitmap bitmap, int i);

    void setBackgroundTopColor(int i);

    void setCornerRadious(float f);

    void setCustomBorderId(int i, File frameFile);

    void setEditableCell(boolean z);

    void setGridNumber(int i);

    void setHueValue(float f);

    void setLayoutParams(LayoutParams layoutParams);

    void setLineThickness(float f);

    void setSelectedAtPosition(boolean selected, int selectedIndex);

    void setShadowSize(float f);

    void setSquareBackground(Drawable drawable);

    void update(Bitmap bitmap, int i);

    void updateRatio(int i, int i2);
}
