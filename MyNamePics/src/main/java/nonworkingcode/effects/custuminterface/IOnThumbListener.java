package nonworkingcode.effects.custuminterface;

import android.app.Activity;
import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by caliber fashion on 9/21/2017.
 */

public interface IOnThumbListener {
    public Activity getActivity();

    void onThumbLoaded(List<Bitmap> list);
}
