package com.head.impl;

import android.view.MotionEvent;

/**
 * Created by caliber fashion on 5/4/2017.
 */

public class ZoomHeadIconEvent implements HeadIconEvent {
    @Override
    public void onActionDown(HeadView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(HeadView stickerView, MotionEvent event) {
        stickerView.zoomAndRotateCurrentSticker(event);
    }

    @Override
    public void onActionUp(HeadView stickerView, MotionEvent event) {
        if (stickerView.getOnStickerOperationListener() != null) {
            stickerView.getOnStickerOperationListener()
                    .onStickerZoomFinished(stickerView.getCurrentSticker());
        }
    }
}
