package com.imagezoom;

import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.widget.FrameLayout.LayoutParams;

public class ImageScaleListener extends SimpleOnScaleGestureListener {
    final ImageViewTouch mImageViewTouch;

    public ImageScaleListener(ImageViewTouch imageViewTouch) {
        this.mImageViewTouch = imageViewTouch;
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        scaleGestureDetector.getCurrentSpan();
        scaleGestureDetector.getPreviousSpan();
        float f2 = this.mImageViewTouch.mCurrentScaleFactor;
        float f3 = scaleGestureDetector.getScaleFactor();
        if (!this.mImageViewTouch.mScaleEnabled) {
            return false;
        }
        f2 = Math.min(this.mImageViewTouch.getMaxZoom(), Math.max(f2 * f3, this.mImageViewTouch.MIN_ZOOM));
        LayoutParams layoutParams = (LayoutParams) this.mImageViewTouch.getLayoutParams();
        this.mImageViewTouch.zoomTo(f2, scaleGestureDetector.getFocusX() - ((float) layoutParams.leftMargin), scaleGestureDetector.getFocusY() - ((float) layoutParams.topMargin));
        this.mImageViewTouch.mCurrentScaleFactor = Math.min(this.mImageViewTouch.getMaxZoom(), Math.max(f2, this.mImageViewTouch.MIN_ZOOM));
        this.mImageViewTouch.mDoubleTapDirection = 1;
        this.mImageViewTouch.invalidate();
        return true;
    }
}
