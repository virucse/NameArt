package nonworkingcode.effects.viewpanel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.formationapps.nameart.R;

/**
 * Created by Caliber Fashion on 12/1/2016.
 */

@SuppressWarnings("ALL")
public class BasicPanel extends FrameLayout {
    private boolean hiding = false;

    public BasicPanel(Context context) {
        super(context);
        init();
    }

    public BasicPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BasicPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BasicPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        hiding = false;
    }

    public void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public void show() {
        if (getVisibility() != View.VISIBLE) {
            showAndHide(this, true, null);
        }
    }

    public void onDeAttach() {
    }

    public void hide() {
        if (!hiding && getVisibility() != View.GONE) {
            hide(null);
        }
    }

    public void hide(OnHideListener onHideListener) {
        if (!hiding && getVisibility() != View.GONE) {
            showAndHide(this, false, onHideListener);
        }
    }

    public boolean onBackPressed() {
        return hiding ? false : false;
    }

    private void showAndHide(final View view, final boolean visible, final OnHideListener onHideListener) {
        Animation animation;
        boolean z = false;
        view.setVisibility(View.VISIBLE);
        if (!visible) {
            z = true;
        }
        this.hiding = z;
        if (visible) {
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_down_in);
        } else {
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_down_out);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (visible) {
                    view.setVisibility(View.VISIBLE);
                    return;
                }
                hiding = false;
                view.setVisibility(View.GONE);
                if (onHideListener != null) {
                    onHideListener.onHideFinished_BasicPanel();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }

    public interface OnHideListener {
        public void onHideFinished_BasicPanel();
    }
}
