package nonworkingcode.pip.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;
import com.xiaopo.flying.sticker.StickerView;

import java.io.File;

import nonworkingcode.grid.util.ColorFilterGenerator;
import nonworkingcode.pip.custominterface.IOnFilterFinishedListener;
import nonworkingcode.pip.custominterface.PIPInterface;
import nonworkingcode.pip.util.PIPFileModel;
import nonworkingcode.pip.util.PipModel;

/**
 * Created by Caliber Fashion on 12/15/2016.
 */

public class PIPView extends RelativeLayout implements PIPInterface {
    public Drawable backgroundDrawable;
    StickerView stickerView;
    private AlphaAnimation mAlphaAnimation;
    private int backgroundColor;
    private int frameBoderId;
    private Context mContext;
    private float mHueValue;
    private ImageView mBottomImageView, mTopImageView, mCoverImageView;
    private RelativeLayout mTopLayer;
    private PIPTouchImageView mPIPTouchImageView;
    private BitmapDrawable blurBitmapDrawable;
    private Bitmap sourceBitmap, bitmapCover, filterBitmap;

    public PIPView(Context context) {
        super(context);
        init(context);
    }

    public PIPView(Context context, AttributeSet var2) {
        super(context, var2);
        init(context);
    }

    public PIPView(Context context, AttributeSet var2, int var3) {
        super(context, var2, var3);
        init(context);
    }

    @SuppressLint({"NewApi"})
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private void init(Context context) {
        this.mAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        this.backgroundColor = -1;
        this.mHueValue = 0.0f;
        this.frameBoderId = -1;
        this.mContext = context;
        initView();
    }

    private void initView() {
        View view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.pip_view, this, true);

        mBottomImageView = (ImageView) view.findViewById(R.id.img_bgTemp);
        mTopLayer = (RelativeLayout) view.findViewById(R.id.rel_topView);
        mTopImageView = (ImageView) view.findViewById(R.id.img_bg);
        mCoverImageView = (ImageView) view.findViewById(R.id.pip_cover);
        mPIPTouchImageView = (PIPTouchImageView) view.findViewById(R.id.img_pic);
        backgroundDrawable = new ColorDrawable(-1);

        this.mAlphaAnimation.setDuration(800);
        this.mAlphaAnimation.setFillAfter(true);
        this.mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Drawable drawable = mBottomImageView.getBackground();
                setBottomBackground(null);
                if (drawable instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    if (!(backgroundDrawable instanceof BitmapDrawable)) {
                        recycleDrawable(drawable);
                    } else if (bitmap != ((BitmapDrawable) backgroundDrawable).getBitmap()) {
                        recycleDrawable(drawable);
                    }
                }
                setBottomBackground(backgroundDrawable);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
       /* StickerConst.drawingLayout = (RelativeLayout)view.findViewById(R.id.sticker_topView);
        StickerConst.drawingLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    StickerConst.invalidateStickers();
                }
                return false;
            }
        });
        StickerConst.drawingLayout.bringToFront();*/
        stickerView = (StickerView) view.findViewById(R.id.sticker_view);
    }

    public StickerView getStickerView() {
        return stickerView;
    }

    private void setBottomBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            mBottomImageView.setBackgroundDrawable(drawable);
        } else {
            setBackground16(mBottomImageView, drawable);
        }
    }

    private void setTopBackground(Drawable drawable) {
        Drawable d = mTopImageView.getDrawable();
        mTopImageView.setImageDrawable(null);
        recycleDrawable(d);
        recycleDrawable(blurBitmapDrawable);
        blurBitmapDrawable = null;
        if (Build.VERSION.SDK_INT < 16) {
            mTopImageView.setBackgroundDrawable(drawable);
        } else {
            setBackground16(mTopImageView, drawable);
        }
    }

    private void setBackground16(ImageView imageView, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(drawable);
        }
    }

    private void recycleDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitamp = ((BitmapDrawable) drawable).getBitmap();
            if (bitamp != null && !bitamp.isRecycled()) {
                //bitamp.recycle();
            }
        }
    }

    @Override
    public Bitmap getBitmap() {
        if (filterBitmap != null && !filterBitmap.isRecycled()) {
            return this.filterBitmap;
        }
        if (sourceBitmap == null || sourceBitmap.isRecycled()) {
            return null;
        }
        return sourceBitmap.copy(sourceBitmap.getConfig(), true);
    }

    @Override
    public int getFrameId() {
        return this.frameBoderId;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void handleImage() {
        if (mTopImageView == null || mTopImageView.getBackground() == null) {
            mTopImageView.setColorFilter(ColorFilterGenerator.adjustHue(this.mHueValue));
            mTopImageView.invalidate();
            return;
        }
        mTopImageView.getBackground().setColorFilter(ColorFilterGenerator.adjustHue(this.mHueValue));
        mTopImageView.invalidate();
    }

    @Override
    public boolean hasShadow() {
        return false;
    }

    @Override
    public void picFill() {
    }

    @Override
    public void picToCenter() {
    }

    @Override
    public void resetToOriEffect(IOnFilterFinishedListener iOnFilterFinishedListener) {
    }

    @Override
    public void reversal(float f) {
    }

    @Override
    public void setCustomBorderId(int index, File frameFile) {
        if (index >= 0) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //setBackground(mTopLayer, Drawable.createFromStream(mContext.getAssets()
                    // .open(assetPath), null));
                    setBackground(mTopLayer, Drawable.createFromPath(frameFile.getAbsolutePath()));
                }
                this.frameBoderId = index;
                return;
            } catch (Exception var3) {
                var3.printStackTrace();
                return;
            }
        }
        setBackground(this.mTopLayer, null);
        this.frameBoderId = index;
    }

    @Override
    public void setHueValue(float f) {
        mHueValue = f;
    }

    @Override
    public void setRotateDegree(float f) {

    }

    @Override
    public void setShadow(int i) {

    }

    @Override
    public void setSquareBackground(Bitmap bitmap3) {
        BitmapDrawable bitmapdrawable = new BitmapDrawable(getResources(), bitmap3);
        bitmapdrawable.setDither(true);
        setBackgroudTopBitmap(bitmapdrawable);
    }

    private void setBackgroudTopBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            blurBitmapDrawable = (BitmapDrawable) drawable;
            mTopImageView.setImageDrawable(drawable);
            mTopImageView.setBackgroundColor(-1);
        } else if (Build.VERSION.SDK_INT < 16) {
            mTopImageView.setBackgroundDrawable(drawable);
        } else {
            setBackground16(mTopImageView, drawable);
        }
    }

    @Override
    public void setSquareBackground(Drawable drawable) {
        this.backgroundDrawable = drawable;
        setTopBackground(this.backgroundDrawable);
        mTopImageView.startAnimation(this.mAlphaAnimation);
    }

    @Override
    public void zoom(float f) {

    }

    public void setPictureImageBitmap(Bitmap pictureImageBitmap) {
        setImageBitmap(pictureImageBitmap, true);
    }

    public void setImageBitmap(Bitmap bitmap, boolean reset) {
        mPIPTouchImageView.setImageBitmap(bitmap, reset);
        if (sourceBitmap == null || sourceBitmap.isRecycled()) {
            sourceBitmap = bitmap.copy(bitmap.getConfig(), true);
            mBottomImageView.setBackgroundColor(backgroundColor);
            mTopImageView.setBackgroundColor(backgroundColor);
            return;
        }
        if (!(filterBitmap == null || filterBitmap.isRecycled())) {
            //filterBitmap.recycle();
        }
        filterBitmap = bitmap;
    }

    public void onPipSelected(PIPFileModel item) {
        try {
            if (!(bitmapCover == null || bitmapCover.isRecycled())) {
                //bitmapCover.recycle();
            }
            bitmapCover = null;
            bitmapCover = item.getCoverBitmap(this.mContext);
            mCoverImageView.setImageBitmap(bitmapCover);
            float w = (float) AppUtils.screenWidth;
            //Rect rect = item.rect;
            Rect rect = item.getSize(mContext);
            w /= 612.0f;
            int n3 = (int) (((float) rect.left) * w);
            int n2 = (int) (((float) rect.top) * w);
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams((int) (((float) (rect.right - rect.left)) * w),
                    (int) (((float) (rect.bottom - rect.top)) * w));
            param.setMargins(n3, n2, 0, 0);
            param.gravity = 51;
            mPIPTouchImageView.setLayoutParams(param);
            mPIPTouchImageView.setMask(item.getMaskBitmap(mContext));
            mPIPTouchImageView.setImageBitmap(getBitmap(), true);
            mPIPTouchImageView.setImageCenterPoint(item.size, 100, 100);
            mPIPTouchImageView.resetDisplayMatrix();
        } catch (OutOfMemoryError e) {
        }
    }

    public void onPipSelected(PipModel item) {
        try {
            if (!(bitmapCover == null || bitmapCover.isRecycled())) {
                bitmapCover.recycle();
            }
            bitmapCover = null;
            bitmapCover = item.getCoverBitmap(this.mContext);
            mCoverImageView.setImageBitmap(bitmapCover);
            float w = (float) AppUtils.screenWidth;
            //Rect rect = item.rect;
            Rect rect = item.getSize(mContext);
            w /= 612.0f;
            int n3 = (int) (((float) rect.left) * w);
            int n2 = (int) (((float) rect.top) * w);
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams((int) (((float) (rect.right - rect.left)) * w),
                    (int) (((float) (rect.bottom - rect.top)) * w));
            param.setMargins(n3, n2, 0, 0);
            param.gravity = 51;
            mPIPTouchImageView.setLayoutParams(param);
            mPIPTouchImageView.setMask(item.getMaskBitmap(mContext));
            mPIPTouchImageView.setImageBitmap(getBitmap(), true);
            mPIPTouchImageView.setImageCenterPoint(item.size, 100, 100);
            mPIPTouchImageView.resetDisplayMatrix();
        } catch (OutOfMemoryError e) {
        }
    }
}
