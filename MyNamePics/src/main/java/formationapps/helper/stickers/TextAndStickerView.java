package formationapps.helper.stickers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.TypefacesUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;

public class TextAndStickerView extends RelativeLayout implements OnTouchListener {
    private final String TAG = TextAndStickerView.class.getSimpleName();
    public int height;
    public int type;
    public int width;
    private boolean active;
    private int backgroundColor;
    private int backgroundResource;
    private Bitmap controlBitmap;
    private Bitmap deleteBitmap;
    private float factor;
    private int fontIndex;
    private GestureDetector gestureDetector;
    private boolean isResponse;
    private float left;
    private int f70o;
    private int f71p;
    private float shadowAlpha;
    private float shadowSize;
    private int shandowColor;
    private float stickerAlpha;
    private Bitmap stickerBitmap;
    private int f72t;
    private String text;
    private float textAlpha;
    private float top;
    private Typeface typeface;
    private float f73u;
    private float f74v;

    //for image as stickers
    public TextAndStickerView(final Context context, final String stpath) {
        super(context);
        this.type = -1;
        this.stickerAlpha = 1.0f;
        this.typeface = Typeface.create("System", Typeface.BOLD);
        this.backgroundColor = -1;
        this.shandowColor = -1;
        this.shadowSize = 0.0f;
        this.textAlpha = 1.0f;
        this.shadowAlpha = 1.0f;
        this.isResponse = true;
        this.active = true;
        this.f70o = (AppUtils.screenWidth * 25) / 480;
        this.f71p = Math.min(100, (AppUtils.screenWidth * 50) / 480);
        this.f72t = 0;
        this.f73u = 0.0f;
        this.f74v = 0.0f;
        this.left = 0.0f;
        this.top = 0.0f;
        this.fontIndex = 0;
        this.backgroundResource = 0;
        this.factor = 1.0f;
        this.gestureDetector = null;
        this.type = 0;
        if (stpath.contains("http")) {
            /*AppUtils.loadImage(stpath, new AppUtils.CloudImageLoadListener() {
                @Override
                public void onCloudImageLoaded(Bitmap bitmap) {
                    stickerBitmap = bitmap;
                    initRemainigItem();
                }
            });*/
        } else {
            String p = stpath.replace("/", "");
            final File file = new File(context.getFilesDir(), p);
            if (file.isFile()) {
                loadFileAndInit(file);
            } else if (isNetworkConnected(context)) {
                try {
                        file.createNewFile();
                        final String baseUrl = AppUtils.downloadableRootUrl;
                        new AsyncTask<Void, Void, Bitmap>() {
                            Bitmap theBitmap;
                            @Override
                            protected Bitmap doInBackground(Void... params) {
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }
                                try {
                                   /* Bitmap theBitmap = Glide.with(context.getApplicationContext()).load(baseUrl + stpath).asBitmap()
                                            .into(-1, -1).get();*/
                                   Glide.with(context.getApplicationContext()).asBitmap().load(baseUrl+stpath).into(new SimpleTarget<Bitmap>() {
                                       @Override
                                       public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                           theBitmap=resource;
                                       }
                                   });
                                    try {
                                        FileOutputStream fos = new FileOutputStream(file);
                                        theBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                                        fos.close();
                                    } catch (FileNotFoundException e) {
                                        Log.d(TAG, "File not found: " + e.getMessage());
                                        return null;
                                    } catch (IOException e) {
                                        Log.d(TAG, "Error accessing file: " + e.getMessage());
                                        return null;
                                    }
                                    return theBitmap;
                                } catch (final Exception e) {
                                    Log.e(TAG, e.getMessage());
                                    return null;
                                }
                            }

                            @Override
                            protected void onPostExecute(Bitmap bitmap) {
                                if (bitmap != null && !bitmap.isRecycled()) {
                                    bitmap.recycle();
                                    loadFileAndInit(file);
                                }
                            }
                        }.execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //this is for text manupulation
    public TextAndStickerView(final Context context, String text, int var3) {
        super(context);
        this.type = -1;
        this.stickerAlpha = 1.0f;
        this.typeface = Typeface.create("System", Typeface.BOLD);
        this.backgroundColor = -1;
        this.shandowColor = -1;
        this.shadowSize = 0.0f;
        this.textAlpha = 1.0f;
        this.shadowAlpha = 1.0f;
        this.isResponse = true;
        this.active = true;
        this.f70o = (AppUtils.screenWidth * 25) / 480;
        this.f71p = Math.min(100, (AppUtils.screenWidth * 50) / 480);
        this.f72t = 0;
        this.f73u = 0.0f;
        this.f74v = 0.0f;
        this.left = 0.0f;
        this.top = 0.0f;
        this.fontIndex = 0;
        this.backgroundResource = 0;
        this.factor = 1.0f;
        this.gestureDetector = null;
        this.type = 1;
        try {
            this.stickerBitmap = BitmapFactory.decodeStream(context.getAssets().open("imagess/ground/ground_" + var3 + ".png"));
        } catch (IOException e) {
            this.stickerBitmap = null;
            e.printStackTrace();
        }
        this.controlBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.image_control_button);
        this.deleteBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.image_delete_button);
        setWillNotDraw(false);
        setText(text);
        this.gestureDetector = new GestureDetector(new SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                StickerConst.selectedSticker = TextAndStickerView.this;
                //((EditorActivity)context).showText();
                return super.onDoubleTap(e);
            }
        });
    }

    private void loadFileAndInit(File file) {
        try {
            stickerBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            initRemainigItem();
        } catch (Exception e) {
            file.delete();
        }
    }

    private void initRemainigItem() {
        this.controlBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.image_control_button);
        this.deleteBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.image_delete_button);
        float var4 = (float) this.stickerBitmap.getWidth();
        float var5 = (float) this.stickerBitmap.getHeight();
        float var6 = Math.min(((float) AppUtils.screenWidth) / var4, ((float) AppUtils.screenWidth) / var5);
        this.width = ((int) (var4 * var6)) + (this.f70o * 2);
        this.height = ((int) (var5 * var6)) + (this.f70o * 2);
        LayoutParams param = new LayoutParams(this.width, this.height);
        param.addRule(13);
        setLayoutParams(param);
        this.factor = 0.5f;
        setWillNotDraw(false);
        this.left = (float) getLeft();
        this.top = (float) getTop();
        setScaleX(this.factor);
        setScaleY(this.factor);
    }

    void refreshCal(float var1, float var2) {
        var1 = (var1 - this.f73u) * this.factor;
        var2 = (var2 - this.f74v) * this.factor;
        float var3 = (float) Math.cos((((double) getRotation()) * 3.141592653589793d) / 180.0d);
        float var4 = (float) Math.sin((((double) getRotation()) * 3.141592653589793d) / 180.0d);
        this.left += (var3 * var1) - (var4 * var2);
        this.top += (var1 * var4) + (var2 * var3);
        setX(this.left);
        setY(this.top);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean var1) {
        this.active = var1;
        if (this.active) {
            StickerConst.drawingLayout.bringChildToFront(this);
        }
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    void refresh() {
        float var1 = Math.min(((float) getWidth()) / ((float) this.width), ((float) getHeight()) / ((float) this.height));
        this.width = (int) (((float) this.width) * var1);
        this.height = (int) (((float) this.height) * var1);
        setLayoutParams(new LayoutParams(this.width, this.height));
    }

    void refreshPos(float var1, float var2) {
        float var10 = (float) (this.width / 2);
        float var11 = (float) (this.height / 2);
        float var9 = (float) Math.sqrt((double) (((this.f73u - var10) * (this.f73u - var10)) + ((this.f74v - var11) * (this.f74v - var11))));
        var10 = (float) Math.sqrt((double) (((var1 - var10) * (var1 - var10)) + ((var2 - var11) * (var2 - var11))));
        if (var9 != 0.0f) {
            this.factor = (getScaleX() * var10) / var9;
            this.factor = Math.min(1.1f, Math.max(this.factor, 0.01f));
            setScaleX(this.factor);
            setScaleY(this.factor);
            double var3 = Math.atan2((double) this.f74v, (double) this.f73u);
            setRotation((float) ((((Math.atan2((double) var2, (double) var1) - var3) * 180.0d) / 3.141592653589793d) + ((double) getRotation())));
            invalidate();
        }
    }

    public int getFontNum() {
        return this.fontIndex;
    }

    public float getMyAlpha() {
        return this.stickerAlpha;
    }

    public float getShadowAlpha() {
        return this.shadowAlpha;
    }

    public void setShadowAlpha(float var1) {
        this.shadowAlpha = var1;
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public int getShadowColor() {
        return this.shandowColor;
    }

    public void setShadowColor(int var1) {
        this.shandowColor = var1;
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public float getShadowSize() {
        return this.shadowSize;
    }

    public void setShadowSize(float var1) {
        this.shadowSize = var1;
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        Paint paint = new Paint();
        paint.setTypeface(this.typeface);
        paint.setTextSize(100.0f);
        float wi = paint.measureText(text);
        float var3 = Math.min(((float) AppUtils.screenWidth) / wi, ((float) AppUtils.screenHeight) / 100.0f);
        this.width = ((int) (wi * var3)) + (this.f70o * 2);
        this.height = ((int) (var3 * 100.0f)) + (this.f70o * 2);
        LayoutParams param = new LayoutParams(this.width, this.height);
        param.addRule(13);
        setLayoutParams(param);
        this.left = (float) getLeft();
        this.top = (float) getTop();
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public float getTextAlpha() {
        return this.textAlpha;
    }

    public void setTextAlpha(float var1) {
        this.textAlpha = var1;
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public int getTextBackground() {
        return this.backgroundResource;
    }

    public void setTextBackground(int index) {
        this.backgroundResource = index;
        if (this.backgroundResource < 0) {
            this.stickerBitmap = null;
            invalidate(new Rect(0, 0, getWidth(), getHeight()));
            return;
        }
        try {
            this.stickerBitmap = BitmapFactory.decodeStream(getContext().getAssets().open("imagess/ground/ground_" + index + ".png"));
        } catch (IOException var3) {
            this.stickerBitmap = null;
            var3.printStackTrace();
            Log.d(TAG, "setTextBackground: " + var3.getMessage());
        }
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public int getTextColor() {
        return this.backgroundColor;
    }

    public void setTextColor(int var1) {
        this.backgroundColor = var1;
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAlpha((int) (this.stickerAlpha * 255.0f));
        float var2 = (float) getWidth();
        float var3 = (float) getHeight();
        if (this.type == 0) {
            if (var2 / ((float) this.width) != var3 / ((float) this.height)) {
                refresh();
                return;
            } else if (this.stickerBitmap != null) {
                canvas.drawBitmap(this.stickerBitmap, new Rect(0, 0, this.stickerBitmap.getWidth(), this.stickerBitmap.getHeight()), new RectF((float) this.f70o, (float) this.f70o, var2 - ((float) this.f70o), var3 - ((float) this.f70o)), paint);
            }
        } else if (this.type == 1) {
            if (this.stickerBitmap != null) {
                canvas.drawBitmap(this.stickerBitmap, new Rect(0, 0, this.stickerBitmap.getWidth(), this.stickerBitmap.getHeight()), new RectF((float) this.f70o, (float) this.f70o, var2 - ((float) this.f70o), var3 - ((float) this.f70o)), paint);
            }
            float var4 = (float) (getHeight() - (this.f70o * 2));
            paint.setTypeface(this.typeface);
            paint.setTextSize(0.6f * var4);
            paint.setStyle(Style.FILL);
            paint.setAntiAlias(true);
            paint.setTextAlign(Align.CENTER);
            if (this.text != null && this.text.length() > 0) {
                if (this.shadowSize != 0.0f) {
                    paint.setColor(this.shandowColor);
                    paint.setAlpha((int) (this.shadowAlpha * 255.0f));
                    canvas.drawText(this.text, ((var2 / 2.0f) * 0.9f) - (this.shadowSize * 8.0f), (((2.0f * var3) / 3.0f) - ((float) (this.f70o / 2))) - (this.shadowSize * 8.0f), paint);
                }
                paint.setColor(this.backgroundColor);
                paint.setAlpha((int) (this.textAlpha * 255.0f));
                canvas.drawText(this.text, (var2 / 2.0f) * 0.9f, ((2.0f * var3) / 3.0f) - ((float) (this.f70o / 2)), paint);
            }
        }
        paint.setAlpha(255);
        if (this.active) {
            paint.setColor(Color.parseColor("#FF4C4C4C"));//-11776948
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(2.0f);
            canvas.drawRoundRect(new RectF((float) this.f70o, (float) this.f70o, var2 - ((float) this.f70o), var3 - ((float) this.f70o)), 5.0f, 5.0f, paint);
            canvas.drawBitmap(this.controlBitmap, new Rect(0, 0, this.controlBitmap.getWidth(), this.controlBitmap.getHeight()), new RectF(var2 - (((float) this.f71p) / this.factor), var3 - (((float) this.f71p) / this.factor), var2, var3), paint);
            canvas.drawBitmap(this.deleteBitmap, new Rect(0, 0, this.deleteBitmap.getWidth(), this.deleteBitmap.getHeight()), new RectF(0.0f, 0.0f, ((float) this.f71p) / this.factor, ((float) this.f71p) / this.factor), paint);
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isResponse) {
            return false;
        }
        if (this.gestureDetector != null && this.gestureDetector.onTouchEvent(event)) {
            return true;
        } else {

        }
        float var2 = event.getX();
        float var3 = event.getY();
        switch (event.getAction()) {
            case ACTION_DOWN/*0*/:
            case ACTION_POINTER_DOWN /*5*/:
            case 261:
                StickerConst.invalidateStickers();
                if (event.getX() < ((float) this.f71p) / this.factor && event.getY() < ((float) this.f71p) / this.factor) {
                    this.f72t = 2;
                } else if (event.getX() <= ((float) getWidth()) - (((float) this.f71p) / this.factor) ||
                        event.getY() <= ((float) getHeight()) - (((float) this.f71p) / this.factor)) {
                    this.f72t = 0;
                } else {
                    this.f72t = 1;
                }
                this.left = getX();
                this.top = getY();
                this.f73u = var2;
                this.f74v = var3;
                setActive(true);
                break;
        }
        if (this.f72t == 0) {
            refreshCal(var2, var3);
        } else if (this.f72t == 1) {
            refreshPos(var2, var3);
        } else if (this.f72t == 2 && event.getX() < ((float) this.f71p) / this.factor && event.getY() <
                ((float) this.f71p) / this.factor) {
            StickerConst.drawingLayout.removeView(this);
        }
        return true;
    }

    public void setAlpha(float var1) {
        this.stickerAlpha = var1;
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public void setFont(int var1) {
        this.fontIndex = var1;
        if (this.fontIndex == 0) {
            this.typeface = Typeface.create("System", Typeface.BOLD);
        } else {
            this.typeface = TypefacesUtils.get(getContext(), "fonts/" + StickerConst.fonts[var1]);
        }
        setText(this.text);
    }

    public void setIsResponse(boolean var1) {
        this.isResponse = var1;
        if (!var1) {
            setActive(false);
        }
    }

    private boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
