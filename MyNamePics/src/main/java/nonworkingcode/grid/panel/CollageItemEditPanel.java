package nonworkingcode.grid.panel;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.formationapps.nameart.R;
import com.gallery.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.grid.custominterface.IOnColageItemClickListener;
import nonworkingcode.grid.util.CollageConst;

public class CollageItemEditPanel extends BaseOnlyScrollPanel<Integer[]> {
    public static final Integer[] edit_res;
    public static final Integer[] edit_type;
    private static CollageItemEditPanel _ration_view;

    static {
        edit_type = new Integer[]{Integer.valueOf(R.string.rot_left), Integer.valueOf(R.string.rot_right), Integer.valueOf(R.string.flip_h),
                Integer.valueOf(R.string.flip_v), Integer.valueOf(R.string.filter), Integer.valueOf(R.string.change), Integer.valueOf(R.string.reset)};
        edit_res = new Integer[]{Integer.valueOf(R.mipmap.ic_rotate_left), Integer.valueOf(R.mipmap.ic_rotate_right),
                Integer.valueOf(R.mipmap.ic_flip_horizontal), Integer.valueOf(R.mipmap.ic_flip_vertical),
                Integer.valueOf(R.mipmap.ic_filters), Integer.valueOf(R.mipmap.ic_replace_image),
                Integer.valueOf(R.mipmap.ic_reset_image)};
    }

    private IOnColageItemClickListener listener;
    private int clickedPosition;

    public CollageItemEditPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollageItemEditPanel(Context context) {
        super(context);
    }

    public static CollageItemEditPanel getInstance() {
        return _ration_view;
    }

    public static CollageItemEditPanel init(Activity activity, int clickedPos, IOnColageItemClickListener l) {
        if (_ration_view != null) {
            _ration_view.onDeAttach();
        }
        _ration_view = new CollageItemEditPanel(activity);
        _ration_view.initView(activity, clickedPos);
        _ration_view.setOnColageItemClickListener(l);
        return _ration_view;
    }

    public void setOnColageItemClickListener(IOnColageItemClickListener l) {
        this.listener = l;
    }

    public void onDeAttach() {
        _ration_view = null;
    }

    private void initView(Activity activity, int clickedPos) {
        clickedPosition = clickedPos;
        List<Integer[]> items = new ArrayList<>();
        for (int i = 0; i < edit_res.length; i++) {
            items.add(new Integer[]{edit_res[i], edit_type[i]});
        }
        super.initView(activity, items);
    }

    @Override
    public void onItemClicked(int tag) {
        int orientation;
        switch (tag) {
            case 0 /*0*/:
                orientation = 6;
                fun(orientation);
                break;
            case 1 /*1*/:
                orientation = 8;
                fun(orientation);
                break;
            case 2 /*2*/:
                orientation = 2;
                fun(orientation);
                break;
            case 3 /*3*/:
                orientation = 4;
                fun(orientation);
                break;
            case 4 /*4*/:
                listener.onEdit(clickedPosition);
                break;
            case 5 /*5*/:
                listener.onChange(clickedPosition);
                break;
            case 6 /*6*/:
                listener.onReset(clickedPosition);
                break;
            default:
                return;
        }
    }

    private void fun(int orientation) {
        if (CollageConst.collageIds[clickedPosition] != null) {
            CollageConst.collageBitmaps[clickedPosition] = ImageUtils.getinstance().getOrientedBitmap
                    (CollageConst.collageBitmaps[clickedPosition], orientation, false);
            CollageConst.effectApplied[clickedPosition] = Boolean.valueOf(true);
            if (CollageConst.collageView != null) {
                CollageConst.collageView.update(CollageConst.collageBitmaps[clickedPosition], clickedPosition);
                return;
            }
            return;
        }
    }

    public boolean isSelectable() {
        return false;
    }

}
