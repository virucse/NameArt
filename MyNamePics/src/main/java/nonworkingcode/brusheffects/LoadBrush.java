package nonworkingcode.brusheffects;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class LoadBrush {
    private static LoadBrush mLoadBrush;
    List<BrushEffectLoad> mBrushEffectList;
    private Context mContext;

    private LoadBrush(Context context) {
        this.mBrushEffectList = new ArrayList();
        this.mContext = context;
        this.mBrushEffectList.add(m3823a("brush/brush1.png", 2, 3, true, 0.15f, 120));
        this.mBrushEffectList.add(m3823a("brush/brush2.png", 2, 2, true, 0.15f, 120));
        this.mBrushEffectList.add(m3823a("brush/brush3.png", 1, 2, true, 0.15f, 120));
        this.mBrushEffectList.add(m3823a("brush/brush4.png", 1, 3, true, 0.15f, 120));
        this.mBrushEffectList.add(m3823a("brush/brush5.png", 2, 2, true, 0.15f, 120));
        //this.mBrushEffectList.add(m3823a("brush/brush6.png", 2, 2, true, 0.15f, 120));
        //this.mBrushEffectList.add(m3823a("brush/brush7.png", 2, 2, true, 0.15f, 120));
        //this.mBrushEffectList.add(m3823a("brush/brush8.png", 2, 2, true, 0.15f, 120));
        //this.mBrushEffectList.add(m3823a("brush/brush9.png", 1, 3, true, 0.15f, 120));
        //this.mBrushEffectList.add(m3823a("brush/brush10.png", 2, 2, true, 0.15f, 120));
        for (int i = 6; i <= 52; i++) {
            mBrushEffectList.add(m3823a("brush/brush" + i + ".png", 1, 1, false, 0.1f, 170));
        }

    }

    public static LoadBrush load(Context context) {
        if (mLoadBrush == null) {
            mLoadBrush = new LoadBrush(context);
        }
        return mLoadBrush;
    }

    public static LoadBrush getInstanse(Context context) {
        return load(context);
    }

    protected BrushEffectLoad m3823a(String str, int column, int row, boolean z, float f, int alpha) {
        BrushEffectLoad brushEffectLoad = new BrushEffectLoad();
        brushEffectLoad.setContext(this.mContext);
        brushEffectLoad.setResLoc(BrushEffectLoad.BrushEffectLoadEnum.ASSETS);
        brushEffectLoad.setFileName(str);
        brushEffectLoad.setColumn(column);
        brushEffectLoad.setRow(row);
        brushEffectLoad.m3813a(z);
        brushEffectLoad.m3807a(f);
        brushEffectLoad.setAlpha(alpha);
        brushEffectLoad.setIndex(mBrushEffectList.size());
        return brushEffectLoad;
    }

    public BrushEffectLoad getBrushEffect(int i) {
        return this.mBrushEffectList.get(i);
    }
}
