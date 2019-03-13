package nonworkingcode.brusheffects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class OverlayBrushView extends AppCompatImageView {
    public static boolean shouldNotDraw;
    private BrushEffectLoad mBrushEffectLoad;
    private List<GetRandomEffect> mRandomEffectList;
    private List<BrushEffectLoad> mBrushEffectLoadList;
    private Stack<List<GetRandomEffect>> mRandomEffectStackList;
    private Bitmap screenBitmap;
    private float screenScale;

    public OverlayBrushView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRandomEffectList = new ArrayList();
        this.mBrushEffectLoadList = new ArrayList();
        this.mRandomEffectStackList = new Stack();
        this.screenScale = 0.8f;
        shouldNotDraw = true;
    }

    public void setBrushResFoundMap(BrushEffectLoad brushEffectLoad) {
        if (brushEffectLoad != null) {
            this.mBrushEffectLoad = brushEffectLoad;
            this.mBrushEffectLoad.m3809a(getContext());
        } else {
            this.mBrushEffectLoad = null;
        }
        if (this.screenBitmap == null || this.screenBitmap.isRecycled()) {
            int width = (int) (((float) getWidth()) * this.screenScale);
            this.screenBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
            setImageBitmap(this.screenBitmap);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        try {
            if (screenBitmap != null && !screenBitmap.isRecycled()) {
                //screenBitmap.recycle();
            }
            if(getWidth()>0&&getHeight()>0){
                screenBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
                setImageBitmap(this.screenBitmap);
            }
        }catch (Exception e){
            //may be illegalargumentexception thrown
        }

    }

    public void clearScreen() {
        if (screenBitmap != null && !screenBitmap.isRecycled()) {
            //screenBitmap.recycle();
        }
        screenBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        setImageBitmap(this.screenBitmap);
    }

    public void setBrushRes(BrushEffectLoad brushEffectLoad) {
        if (brushEffectLoad != null) {
            this.mBrushEffectLoad = brushEffectLoad;
            this.mBrushEffectLoad.m3809a(getContext());
            return;
        }
        this.mBrushEffectLoad = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (shouldNotDraw || this.mBrushEffectLoad == null) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN/*0*/:
                onActionDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE /*2*/:
                if (System.currentTimeMillis() % 4 == 0) {
                    onActionMove(x, y);
                    break;
                }
                break;
        }
        return true;
    }

    public void onActionDown(float f, float f2) {
        List arrayList = new ArrayList();
        GetRandomEffect getRandomEffect = new GetRandomEffect(getContext(), f, f2, this.mBrushEffectLoad);
        arrayList.add(getRandomEffect);
        mRandomEffectList.add(getRandomEffect);
        mRandomEffectStackList.push(arrayList);
        mBrushEffectLoadList.add(this.mBrushEffectLoad);
        setImageToScreen();
    }

    public void onActionMove(float f, float f2) {
        GetRandomEffect getRandomEffect = new GetRandomEffect(getContext(), f, f2, this.mBrushEffectLoad);
        mRandomEffectList.add(getRandomEffect);
        if (!mRandomEffectStackList.isEmpty()) {
            ((List) mRandomEffectStackList.peek()).add(getRandomEffect);
        }
        setImageToScreen();
    }

    public void m3799a() {
        if (!this.mRandomEffectStackList.isEmpty()) {
            BrushEffectLoad brushEffectLoad = this.mBrushEffectLoad;
            this.mRandomEffectStackList.pop();
            this.mBrushEffectLoadList.remove(this.mBrushEffectLoadList.size() - 1);
            Canvas canvas = new Canvas(this.screenBitmap);
            canvas.drawColor(0, Mode.CLEAR);
            float width = ((float) canvas.getWidth()) / ((float) getWidth());
            float height = ((float) canvas.getHeight()) / ((float) getHeight());
            Iterator it = this.mRandomEffectStackList.iterator();
            int i = 0;
            while (it.hasNext()) {
                List<GetRandomEffect> list = (List) it.next();
                BrushEffectLoad brushEffectLoad2 = (BrushEffectLoad) this.mBrushEffectLoadList.get(i);
                if (brushEffectLoad2 != null) {
                    setBrushResFoundMap(brushEffectLoad2);
                }
                for (GetRandomEffect a : list) {
                    drawEffect(canvas, a, width, height);
                }
                i++;
            }
            setBrushResFoundMap(brushEffectLoad);
            setImageBitmap(this.screenBitmap);
        }
    }

    public void setImageToScreen() {
        if (this.screenBitmap != null && !this.screenBitmap.isRecycled()) {
            Canvas canvas = new Canvas(this.screenBitmap);
            Iterator it = this.mRandomEffectList.iterator();
            float width = ((float) canvas.getWidth()) / ((float) getWidth());
            float height = ((float) canvas.getHeight()) / ((float) getHeight());
            if (it.hasNext()) {
                drawEffect(canvas, (GetRandomEffect) it.next(), width, height);
                it.remove();
            }
            setImageBitmap(this.screenBitmap);
        }
    }

    public void drawEffect(Canvas canvas, GetRandomEffect randomEffect, float width, float height) {
        this.mBrushEffectLoad.drawEffect(canvas, randomEffect.f2027e * width, randomEffect.f2028f * height, randomEffect.color, randomEffect.f2025c, randomEffect.f2026d, randomEffect.f2023a);
    }

    public Bitmap getScreenBitmap() {
        return this.screenBitmap;
    }

    public float getScreenScale() {
        return this.screenScale;
    }

    public void setScreenScale(float f) {
    }

    public void m3804c() {
        if (!(this.screenBitmap == null || this.screenBitmap.isRecycled())) {
            new Canvas(this.screenBitmap).drawColor(Color.GREEN, Mode.CLEAR);
            setImageBitmap(this.screenBitmap);
        }
        this.mBrushEffectLoadList.clear();
        this.mRandomEffectStackList.clear();
    }
}
