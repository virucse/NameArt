package nonworkingcode.effects.viewpanel;

import android.content.Context;
import android.util.AttributeSet;

public abstract class BaseFilterPanel extends BasicPanel {
    public BaseFilterPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFilterPanel(Context context) {
        super(context);
    }

    public abstract void onApplied();

    public abstract void onDiscarded();
}
