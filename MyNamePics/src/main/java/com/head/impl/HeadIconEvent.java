package com.head.impl;

import android.view.MotionEvent;

/**
 * Created by caliber fashion on 5/4/2017.
 */

public interface HeadIconEvent {
    void onActionDown(HeadView stickerView, MotionEvent event);

    void onActionMove(HeadView stickerView, MotionEvent event);

    void onActionUp(HeadView stickerView, MotionEvent event);
}
