package nonworkingcode.effects.viewpanel.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;

import java.io.File;

/**
 * Created by Caliber Fashion on 12/1/2016.
 */

public class PanelItemView extends FrameLayout {
    private static final String TAG = PanelItemView.class.getName();
    int size;
    private ImageView imageview;
    private TextView textview;

    public PanelItemView(Context context) {
        super(context);
        init(context);
    }

    public PanelItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PanelItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PanelItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        size = AppUtils.dpToPx(getContext(), 20.0f);
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.panelitemview, this, true);
        imageview = (ImageView) findViewById(R.id.imageview);
        textview = (TextView) findViewById(R.id.textview);
        if (textview != null) {
            textview.setTypeface(AppUtils.webApiFont);
        }
        setBackgroundResource(R.drawable.btn_selector);

    }

    public void setData(int imgRes, int txtRes) {
        this.imageview.setImageResource(imgRes);
        this.textview.setText(txtRes);
    }

    public void setName(String name) {
        this.textview.setVisibility(View.VISIBLE);
        this.textview.setText(name);
    }

    public void setData(Bitmap btm) {
        this.textview.setVisibility(INVISIBLE);
        this.imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.imageview.setImageBitmap(btm);
    }

    public void setData(Drawable drawable) {
        this.textview.setVisibility(INVISIBLE);
        this.imageview.setImageDrawable(drawable);
    }

    public void setData(Bitmap btm, String text) {
        this.textview.setText(text);
        this.imageview.setImageBitmap(btm);
    }

    public void setData(String filename, String name) {
        try {
            Bitmap temp = BitmapFactory.decodeStream(getContext().getAssets().open(filename));
            if (temp.getWidth() > 200) {
                int w = 150;
                int h = (int) ((float) w * (((float) temp.getHeight()) / (((float) temp.getWidth()) * 1.0f)));
                Bitmap bitmap = Bitmap.createScaledBitmap(temp, 150, h, true);
                this.imageview.setImageBitmap(bitmap);
                temp.recycle();
            } else {
                this.imageview.setImageBitmap(temp);
            }
        } catch (Exception e) {
            Log.d(TAG, "setData: " + e.getMessage());
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        this.textview.setText(name);
    }

    public void setFile(File file) {
        try {
            if (getContext() == null) {
                return;
            }
            this.textview.setVisibility(View.GONE);
            if (file.getAbsolutePath().contains("pip")) {
                Glide.with(getContext().getApplicationContext()).load(file).into(imageview);
            } else {
                Glide.with(getContext().getApplicationContext()).load(file)
                        .apply(new RequestOptions().override(size, size)).into(imageview);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }


    }

    public void setData(String name) {
        this.textview.setVisibility(View.GONE);
        setData(name, "");
    }

    public void updateData(int imgRes) {
        this.imageview.setImageResource(imgRes);
    }

    public void setData(int imgRes) {
        this.textview.setVisibility(GONE);
        this.imageview.setImageResource(imgRes);
    }

    public void setSelected(boolean bl2) {
        super.setSelected(bl2);
        if (bl2) {
            setBackgroundColor(AppUtils.getColor(getContext(), R.attr.colorPrimary));
        } else {
            setBackgroundResource(R.drawable.btn_selector);
        }
    }

    public void setData(int imgRes, String string) {
        this.imageview.setImageResource(imgRes);
        this.textview.setText(string);
    }
}
