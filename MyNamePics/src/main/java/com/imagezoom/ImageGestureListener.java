package com.imagezoom;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.FrameLayout.LayoutParams;

public class ImageGestureListener extends SimpleOnGestureListener {
    final ImageViewTouch mImageViewTouch;

    public ImageGestureListener(ImageViewTouch imageViewTouch) {
        this.mImageViewTouch = imageViewTouch;
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.i(ImageViewTouchBase.LOG_TAG, "onDoubleTap. double tap enabled? " + this.mImageViewTouch.mDoubleTapToZoomEnabled);
        if (this.mImageViewTouch.mDoubleTapToZoomEnabled) {
            float f2 = this.mImageViewTouch.onDoubleTapPost(this.mImageViewTouch.getScale(), this.mImageViewTouch.getMaxZoom());
            ImageViewTouch imageViewTouch = this.mImageViewTouch;
            f2 = Math.min(this.mImageViewTouch.getMaxZoom(), Math.max(f2, this.mImageViewTouch.MIN_ZOOM));
            imageViewTouch.mCurrentScaleFactor = f2;
            LayoutParams layoutParams = (LayoutParams) this.mImageViewTouch.getLayoutParams();
            this.mImageViewTouch.zoomTo(f2, motionEvent.getX() - ((float) layoutParams.leftMargin), motionEvent.getY() - ((float) layoutParams.topMargin), 200.0f);
            this.mImageViewTouch.invalidate();
        }
        if (ImageViewTouch.access$100(mImageViewTouch) != null) {
            ImageViewTouch.access$100(mImageViewTouch).onDoubleTap();
        }
        return super.onDoubleTap(motionEvent);
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f2, float f3) {
        if (!this.mImageViewTouch.mScrollEnabled || motionEvent.getPointerCount() > 1 || motionEvent2.getPointerCount() > 1 || this.mImageViewTouch.mScaleDetector.isInProgress()) {
            return false;
        }
        float f4 = motionEvent2.getX();
        float f5 = motionEvent.getX();
        float f6 = motionEvent2.getY();
        float f7 = motionEvent.getY();
        if (Math.abs(f2) > 800.0f || Math.abs(f3) > 800.0f) {
            this.mImageViewTouch.scrollBy((f4 - f5) / 2.0f, (f6 - f7) / 2.0f, 300.0d);
            this.mImageViewTouch.invalidate();
        }
        return super.onFling(motionEvent, motionEvent2, f2, f3);
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.mImageViewTouch.isLongClickable() && !this.mImageViewTouch.mScaleDetector.isInProgress()) {
            if (ImageViewTouch.access$200(this.mImageViewTouch) != null) {
                ImageViewTouch.access$200(this.mImageViewTouch).onLongPress();
            }
            this.mImageViewTouch.setPressed(true);
            this.mImageViewTouch.performLongClick();
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f2, float f3) {
        if (ImageViewTouch.access$300(this.mImageViewTouch)) {
            ImageViewTouch.access$302(this.mImageViewTouch, false);
            return false;
        } else if (!this.mImageViewTouch.mScrollEnabled || motionEvent == null || motionEvent2 == null || motionEvent.getPointerCount() > 1 || motionEvent2.getPointerCount() > 1 || this.mImageViewTouch.mScaleDetector.isInProgress()) {
            return false;
        } else {
            this.mImageViewTouch.scrollBy(-f2, -f3);
            this.mImageViewTouch.invalidate();
            return super.onScroll(motionEvent, motionEvent2, f2, f3);
        }
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        if (ImageViewTouch.access$000(this.mImageViewTouch) != null) {
            ImageViewTouch.access$000(this.mImageViewTouch).onSingleTap();
        }
        this.mImageViewTouch.performClick();
        return super.onSingleTapConfirmed(motionEvent);
    }
}
