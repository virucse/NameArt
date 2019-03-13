package nonworkingcode.grid.panel;

import android.content.Context;
import android.util.AttributeSet;

import nonworkingcode.grid.util.CollageConst;

/**
 * Created by Caliber Fashion on 12/12/2016.
 */

public class FramePanel extends BaseFramePanel {
    public FramePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FramePanel(Context context) {
        super(context);
    }

    @Override
    public int getSelected() {
        return CollageConst.frameBorderAssetId;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        CollageConst.collageView.setCustomBorderId(indexClicked - 1, getFramePath(indexClicked));
    }
}
