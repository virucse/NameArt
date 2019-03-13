package nonworkingcode.grid.panel;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.grid.activity.CollageActivity;
import nonworkingcode.grid.util.CollageConst;

public class CollageLayoutContainer extends BaseOnlyScrollPanel<String> {
    private static final String TAG = CollageLayoutContainer.class.getName();
    private static CollageLayoutContainer _ration_view;
    int[] arr;
    private IOnGridSelectListener listener;
    private List<String> values;

    public CollageLayoutContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.values = new ArrayList();
    }

    public CollageLayoutContainer(Context context) {
        super(context);
        this.values = new ArrayList();
    }

    public static CollageLayoutContainer getInstance() {
        return _ration_view;
    }

    public static CollageLayoutContainer init(Activity activity, int imageCount) {
        if (_ration_view != null) {
            _ration_view.onDeAttach();
        }
        _ration_view = new CollageLayoutContainer(activity);
        _ration_view.initView(activity, imageCount);
        return _ration_view;
    }

    public static CollageLayoutContainer init(Activity activity, int imageCount, IOnGridSelectListener listener) {
        if (_ration_view != null) {
            _ration_view.onDeAttach();
        }
        _ration_view = new CollageLayoutContainer(activity);
        _ration_view.initView(activity, imageCount);
        _ration_view.setOnGridSelectListener(listener);
        return _ration_view;
    }

    public void setOnGridSelectListener(IOnGridSelectListener l) {
        this.listener = l;
    }

    public void onDeAttach() {
        _ration_view = null;
    }

    private void initView(Activity activity, int imageCount) {
        this.arr = CollageConst.collages[imageCount];
        this.values.clear();
        //Log.d(TAG, "initView: " + Arrays.toString(this.arr).toString());
        for (int index : this.arr) {
            try {
                int index2 = index;
                String pre = "btnFrame";
                if (index2 >= 256) {
                    pre = "fancyBtnFrame";
                    index2 = index2 - 256;
                }
                this.values.add("imagess/collages/" + pre + index2 + ".png");
            } catch (Exception e) {
                //Log.d(TAG, "initView: " + e.getMessage());
            }
        }
        super.initView(activity, this.values);
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (this.listener != null) {
            this.listener.onGridSelected(this.arr[indexClicked]);
        }
        CollageActivity activity = null;
        if (getContext() instanceof CollageActivity) {
            activity = (CollageActivity) getContext();
        }
        if (activity != null) {
            activity.setGrid(this.arr[indexClicked]);
        }
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    public interface IOnGridSelectListener {
        public void onGridSelected(int i);
    }
}
