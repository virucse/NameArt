package nonworkingcode.grid.panel;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import java.io.File;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;

public abstract class BaseFramePanel extends BaseOnlyScrollPanel<File> {
    List<File> items;

    public BaseFramePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFramePanel(Context context) {
        super(context);
        initView((Activity) context);
    }

    public abstract int getSelected();

    public void onDeAttach() {
    }

    protected void initView(Activity activity) {
        items = FrameFile.getFrameFileList(activity);
        super.initView(activity, items);
        resetLinSelector(getSelected());
    }

    public boolean canSelectable() {
        return true;
    }

    public File getFramePath(int index) {
        return items.get(index);
    }
}
