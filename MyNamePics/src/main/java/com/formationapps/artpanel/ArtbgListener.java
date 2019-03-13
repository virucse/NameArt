package com.formationapps.artpanel;

import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 * Created by caliber fashion on 9/20/2017.
 */

public interface ArtbgListener {
    public void setSquareBackground(Drawable drawable);

    public void setHueValue(float value);

    public void handleImage();

    public void onImageSelectedFromGallery(Uri uri);
}
