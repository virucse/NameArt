package nonworkingcode.pip.panel;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.pip.util.PIPFileModel;
import nonworkingcode.pip.util.PipManager;
import nonworkingcode.pip.views.PIPView;

public class PipIconPanel extends BaseOnlyScrollPanel<File> {
    private static PipIconPanel _instance;
    private static int selected;

    static {
        selected = -1;
    }

    private List<PIPFileModel> items;
    private PIPView pipView;

    public PipIconPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.items = new ArrayList();
    }

    public PipIconPanel(Context context) {
        super(context);
        this.items = new ArrayList();
    }

    static PipIconPanel init(Activity activity, PIPView pipView) {
        if (_instance != null) {
            _instance.onDeAttach();
        }
        _instance = new PipIconPanel(activity);
        _instance.initView(activity, pipView);
        return _instance;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        onItemSelected(indexClicked);
    }

    protected void initView(Activity activity, PIPView pipView) {
        this.items = PipManager.getFileModelList();
        this.pipView = pipView;
        List<File> arr = new ArrayList();
        for (PIPFileModel item : this.items) {
            //arr.add("pip/" + item.icon);
            arr.add(item.icon);
        }
        super.initView(activity, arr);
        if (selected == -1) {
            selected = 0;
        }
        onItemSelected(selected);
    }

    void onItemSelected(int index) {
        if (index != selected) {
            selected = index;
            onItemSelected(this.items.get(selected));
        }
    }

    protected void onItemSelected(PIPFileModel item) {
        this.pipView.onPipSelected(item);
    }
}
