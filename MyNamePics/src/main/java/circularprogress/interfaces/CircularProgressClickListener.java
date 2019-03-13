package circularprogress.interfaces;

import circularprogress.customViews.CircularProgressButton;
import circularprogress.customViews.CircularProgressImageButton;

/**
 * Created by caliber fashion on 9/9/2017.
 */

public interface CircularProgressClickListener {
    public void onClickButton(CircularProgressButton button, int index);

    public void onClickImageButton(CircularProgressImageButton button, int index);
}
