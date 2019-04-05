package com.removebg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BgRemoverActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;


public class BgEraserView extends AppCompatImageView implements OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    private static int pc;
    Bitmap Bitmap2 = null;
    Bitmap Bitmap3 = null;
    Bitmap Bitmap4 = null;
    public static int ERASE = 2;
    public static int FREEMODE = 5;
    private int MODE = 1;
    public static int ZOOM = 4;
    public static int RESTORE = 3;//restore
    public static int TARGET = 1;//may be auto erasing
    private int TOLERANCE = 30;
    float globX = 250.0f;
    float globY = 350.0f;
    private ActionListener actionListener;
    private ArrayList<Integer> brushIndx = new ArrayList();
    private int brushSize = 18;
    private int brushSize1 = 18;
    private ArrayList<Boolean> brushTypeIndx = new ArrayList();
    Canvas c2;
    private ArrayList<Path> changesIndx = new ArrayList();
    Context ctx;
    private int curIndx = -1;
    private boolean drawLasso = false;
    private boolean drawOnLasso = true;
    Paint erPaint = new Paint();
    Paint erPaint1 = new Paint();
    int erps = BgEraserImageUtils.dpToPx(getContext(), 2);
    int height;
    private boolean insidCutEnable = true;
    private boolean isAutoRunning = false;
    boolean isMoved = false;
    private boolean isNewPath = false;
    private boolean isRectBrushEnable = false;
    public boolean isRotateEnabled = true;
    public boolean isScaleEnabled = true;
    private boolean isSelected = true;
    private boolean isTouched = false;
    public boolean isTranslateEnabled = true;
    Path lPath = new Path();
    private ScaleGestureDetector mScaleGestureDetector;
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;
    private ArrayList<Integer> modeIndx = new ArrayList();
    private int offset =  200 ;
    private int offset1 =  200 ;
    private boolean onLeft = true;
    private Bitmap orgBit;
    /* renamed from: p */
    Paint f21p = new Paint();
    Paint paint = new Paint();
    BitmapShader patternBMPshader;
    public ProgressDialog pd = null;
    public Point point;
    float sX;
    float sY;
    float scale = 1.0f;
    private int screenWidth;
    private MagnifyView magnifyView = null;
    Path tPath = new Path();
    private int targetBrushSize = 18;
    private int targetBrushSize1 = 18;
    private UndoRedoListener undoRedoListener;
    private boolean updateOnly = false;
    int width;

    public interface ActionListener {
        void onAction(int i);

        void onActionCompleted(int i);
    }

    private class AsyncTaskRunner1 extends AsyncTask<Void, Void, Bitmap> {
        int ch;
        Vector<Point> targetPoints;

        public AsyncTaskRunner1(int i) {
            this.ch = i;
        }

        protected Bitmap doInBackground(Void... voids) {
            if (this.ch != 0) {
                this.targetPoints = new Vector();
                removeSelectedColor(new Point(BgEraserView.this.point.x, BgEraserView.this.point.y), this.ch, 0);
                if (BgEraserView.this.curIndx < 0) {
                    BgEraserView.this.changesIndx.add(BgEraserView.this.curIndx + 1, new Path());
                    BgEraserView.this.brushIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.brushSize);
                    BgEraserView.this.modeIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.TARGET);
                    BgEraserView.this.brushTypeIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.isRectBrushEnable);
                    BgEraserView.this.curIndx = BgEraserView.this.curIndx + 1;
                    clearNextChanges();
                } else if (!((Integer) BgEraserView.this.modeIndx.get(BgEraserView.this.curIndx) == BgEraserView.this.TARGET && BgEraserView.this.curIndx == BgEraserView.this.modeIndx.size() - 1)) {
                    BgEraserView.this.changesIndx.add(BgEraserView.this.curIndx + 1, new Path());
                    BgEraserView.this.brushIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.brushSize);
                    BgEraserView.this.modeIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.TARGET);
                    BgEraserView.this.brushTypeIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.isRectBrushEnable);
                    BgEraserView.this.curIndx = BgEraserView.this.curIndx + 1;
                    clearNextChanges();
                }
                //Log.i("testing", "Time : " + this.ch + "  " + EraseNView.this.curIndx + "   " + EraseNView.this.changesIndx.size());
            }
            return null;
        }

        private void removeSelectedColor(Point pt, int targetColor, int replacementColor) {
            if (targetColor != 0) {
                int[] pixels = new int[(BgEraserView.this.width * BgEraserView.this.height)];
                BgEraserView.this.Bitmap3.getPixels(pixels, 0, BgEraserView.this.width, 0, 0, BgEraserView.this.width, BgEraserView.this.height);
                Queue<Point> q = new LinkedList();
                q.add(pt);
                while (q.size() > 0) {
                    Point n = (Point) q.poll();
                    if (compareColor(pixels[BgEraserView.this.index(n.x, n.y, BgEraserView.this.width)], targetColor)) {
                        Point w = n;
                        Point e = new Point(n.x + 1, n.y);
                        while (w.x > 0 && compareColor(pixels[BgEraserView.this.index(w.x, w.y, BgEraserView.this.width)], targetColor)) {
                            pixels[BgEraserView.this.index(w.x, w.y, BgEraserView.this.width)] = replacementColor;
                            this.targetPoints.add(new Point(w.x, w.y));
                            if (w.y > 0 && compareColor(pixels[BgEraserView.this.index(w.x, w.y - 1, BgEraserView.this.width)], targetColor)) {
                                q.add(new Point(w.x, w.y - 1));
                            }
                            if (w.y < BgEraserView.this.height && compareColor(pixels[BgEraserView.this.index(w.x, w.y + 1, BgEraserView.this.width)], targetColor)) {
                                q.add(new Point(w.x, w.y + 1));
                            }
                            w.x--;
                        }
                        if (w.y > 0 && w.y < BgEraserView.this.height) {
                            pixels[BgEraserView.this.index(w.x, w.y, BgEraserView.this.width)] = replacementColor;
                            this.targetPoints.add(new Point(w.x, w.y));
                        }
                        while (e.x < BgEraserView.this.width && compareColor(pixels[BgEraserView.this.index(e.x, e.y, BgEraserView.this.width)], targetColor)) {
                            pixels[BgEraserView.this.index(e.x, e.y, BgEraserView.this.width)] = replacementColor;
                            this.targetPoints.add(new Point(e.x, e.y));
                            if (e.y > 0 && compareColor(pixels[BgEraserView.this.index(e.x, e.y - 1, BgEraserView.this.width)], targetColor)) {
                                q.add(new Point(e.x, e.y - 1));
                            }
                            if (e.y < BgEraserView.this.height && compareColor(pixels[BgEraserView.this.index(e.x, e.y + 1, BgEraserView.this.width)], targetColor)) {
                                q.add(new Point(e.x, e.y + 1));
                            }
                            e.x++;
                        }
                        if (e.y > 0 && e.y < BgEraserView.this.height) {
                            pixels[BgEraserView.this.index(e.x, e.y, BgEraserView.this.width)] = replacementColor;
                            this.targetPoints.add(new Point(e.x, e.y));
                        }
                    }
                }
                BgEraserView.this.Bitmap2.setPixels(pixels, 0, BgEraserView.this.width, 0, 0, BgEraserView.this.width, BgEraserView.this.height);
            }
        }

        public boolean compareColor(int color1, int color2) {
            if (color1 == 0 || color2 == 0) {
                return false;
            }
            if (color1 == color2) {
                return true;
            }
            int diffRed = Math.abs(Color.red(color1) - Color.red(color2));
            int diffGreen = Math.abs(Color.green(color1) - Color.green(color2));
            int diffBlue = Math.abs(Color.blue(color1) - Color.blue(color2));
            if (diffRed > BgEraserView.this.TOLERANCE || diffGreen > BgEraserView.this.TOLERANCE || diffBlue > BgEraserView.this.TOLERANCE) {
                return false;
            }
            return true;
        }

        private void clearNextChanges() {
            int size = BgEraserView.this.changesIndx.size();
            //Log.i("testings", " Curindx " + EraseNView.this.curIndx + " Size " + size);
            int i = BgEraserView.this.curIndx + 1;
            while (size > i) {
                //Log.i("testings", " indx " + i);
                BgEraserView.this.changesIndx.remove(i);
                BgEraserView.this.brushIndx.remove(i);
                BgEraserView.this.modeIndx.remove(i);
                BgEraserView.this.brushTypeIndx.remove(i);
                size = BgEraserView.this.changesIndx.size();
            }
            if (BgEraserView.this.undoRedoListener != null) {
                BgEraserView.this.undoRedoListener.enableUndo(true, BgEraserView.this.curIndx + 1);
                BgEraserView.this.undoRedoListener.enableRedo(false, BgEraserView.this.modeIndx.size() - (BgEraserView.this.curIndx + 1));
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(getContext(), null, null);
            ProgressBar spinner = new ProgressBar(getContext(), null,android.R.attr.progressBarStyle);
            spinner.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progressdialog_circle_color),
                    Mode.SRC_IN);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pd.setContentView(spinner);
            pd.setCancelable(false);
            pd.show();
        }

        protected void onPostExecute(Bitmap v) {
            BgEraserView.this.pd.dismiss();
            BgEraserView.this.invalidate();
            BgEraserView.this.isAutoRunning = false;
        }
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Bitmap> {
        int ch;
        Vector<Point> targetPoints;

        public AsyncTaskRunner(int i) {
            this.ch = i;
        }

        protected Bitmap doInBackground(Void... voids) {
            if (this.ch != 0) {
                this.targetPoints = new Vector();
                BgEraserView.this.Bitmap3 = BgEraserView.this.Bitmap2.copy(BgEraserView.this.Bitmap2.getConfig(), true);
                removeSelectedColor(BgEraserView.this.Bitmap2, new Point(BgEraserView.this.point.x, BgEraserView.this.point.y), this.ch, 0);
                BgEraserView.this.changesIndx.add(BgEraserView.this.curIndx + 1, new Path());
                BgEraserView.this.brushIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.brushSize);
                BgEraserView.this.modeIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.TARGET);
                BgEraserView.this.brushTypeIndx.add(BgEraserView.this.curIndx + 1, BgEraserView.this.isRectBrushEnable);
                BgEraserView.this.curIndx = BgEraserView.this.curIndx + 1;
                clearNextChanges();
                BgEraserView.this.updateOnly = true;
                //Log.i("testing", "Time : " + this.ch + "  " + EraseNView.this.curIndx + "   " + EraseNView.this.changesIndx.size());
            }
            return null;
        }

        private void removeSelectedColor(Bitmap bmp, Point pt, int targetColor, int replacementColor) {
            if (targetColor != 0) {
                int[] pixels = new int[(BgEraserView.this.width * BgEraserView.this.height)];
                bmp.getPixels(pixels, 0, BgEraserView.this.width, 0, 0, BgEraserView.this.width, BgEraserView.this.height);
                Queue<Point> q = new LinkedList();
                q.add(pt);
                while (q.size() > 0) {
                    Point n = (Point) q.poll();
                    if (compareColor(pixels[BgEraserView.this.index(n.x, n.y, BgEraserView.this.width)], targetColor)) {
                        Point w = n;
                        Point e = new Point(n.x + 1, n.y);
                        while (w.x > 0 && compareColor(pixels[BgEraserView.this.index(w.x, w.y, BgEraserView.this.width)], targetColor)) {
                            pixels[BgEraserView.this.index(w.x, w.y, BgEraserView.this.width)] = replacementColor;
                            this.targetPoints.add(new Point(w.x, w.y));
                            if (w.y > 0 && compareColor(pixels[BgEraserView.this.index(w.x, w.y - 1, BgEraserView.this.width)], targetColor)) {
                                q.add(new Point(w.x, w.y - 1));
                            }
                            if (w.y < BgEraserView.this.height && compareColor(pixels[BgEraserView.this.index(w.x, w.y + 1, BgEraserView.this.width)], targetColor)) {
                                q.add(new Point(w.x, w.y + 1));
                            }
                            w.x--;
                        }
                        if (w.y > 0 && w.y < BgEraserView.this.height) {
                            pixels[BgEraserView.this.index(w.x, w.y, BgEraserView.this.width)] = replacementColor;
                            this.targetPoints.add(new Point(w.x, w.y));
                        }
                        while (e.x < BgEraserView.this.width && compareColor(pixels[BgEraserView.this.index(e.x, e.y, BgEraserView.this.width)], targetColor)) {
                            pixels[BgEraserView.this.index(e.x, e.y, BgEraserView.this.width)] = replacementColor;
                            this.targetPoints.add(new Point(e.x, e.y));
                            if (e.y > 0 && compareColor(pixels[BgEraserView.this.index(e.x, e.y - 1, BgEraserView.this.width)], targetColor)) {
                                q.add(new Point(e.x, e.y - 1));
                            }
                            if (e.y < BgEraserView.this.height && compareColor(pixels[BgEraserView.this.index(e.x, e.y + 1, BgEraserView.this.width)], targetColor)) {
                                q.add(new Point(e.x, e.y + 1));
                            }
                            e.x++;
                        }
                        if (e.y > 0 && e.y < BgEraserView.this.height) {
                            pixels[BgEraserView.this.index(e.x, e.y, BgEraserView.this.width)] = replacementColor;
                            this.targetPoints.add(new Point(e.x, e.y));
                        }
                    }
                }
                bmp.setPixels(pixels, 0, BgEraserView.this.width, 0, 0, BgEraserView.this.width, BgEraserView.this.height);
            }
        }

        public boolean compareColor(int color1, int color2) {
            if (color1 == 0 || color2 == 0) {
                return false;
            }
            if (color1 == color2) {
                return true;
            }
            int diffRed = Math.abs(Color.red(color1) - Color.red(color2));
            int diffGreen = Math.abs(Color.green(color1) - Color.green(color2));
            int diffBlue = Math.abs(Color.blue(color1) - Color.blue(color2));
            if (diffRed > BgEraserView.this.TOLERANCE || diffGreen > BgEraserView.this.TOLERANCE || diffBlue > BgEraserView.this.TOLERANCE) {
                return false;
            }
            return true;
        }

        private void clearNextChanges() {
            int size = BgEraserView.this.changesIndx.size();
            //Log.i("testings", " Curindx " + EraseNView.this.curIndx + " Size " + size);
            int i = BgEraserView.this.curIndx + 1;
            while (size > i) {
                //Log.i("testings", " indx " + i);
                BgEraserView.this.changesIndx.remove(i);
                BgEraserView.this.brushIndx.remove(i);
                BgEraserView.this.modeIndx.remove(i);
                BgEraserView.this.brushTypeIndx.remove(i);
                size = BgEraserView.this.changesIndx.size();
            }
            if (BgEraserView.this.undoRedoListener != null) {
                BgEraserView.this.undoRedoListener.enableUndo(true, BgEraserView.this.curIndx + 1);
                BgEraserView.this.undoRedoListener.enableRedo(false, BgEraserView.this.modeIndx.size() - (BgEraserView.this.curIndx + 1));
            }
            if (BgEraserView.this.actionListener != null) {
                BgEraserView.this.actionListener.onActionCompleted(BgEraserView.this.MODE);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(getContext(), null, null);
            ProgressBar spinner = new ProgressBar(getContext(), null,android.R.attr.progressBarStyle);
            spinner.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progressdialog_circle_color),
                    Mode.SRC_IN);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pd.setContentView(spinner);
            pd.setCancelable(false);
            pd.show();
        }

        protected void onPostExecute(Bitmap v) {
            BgEraserView.this.pd.dismiss();
            BgEraserView.this.pd = null;
            BgEraserView.this.invalidate();
            BgEraserView.this.isAutoRunning = false;
        }
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    public interface UndoRedoListener {
        void enableRedo(boolean z, int i);

        void enableUndo(boolean z, int i);
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D();
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            this.mPivotX = detector.getFocusX();
            this.mPivotY = detector.getFocusY();
            this.mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, ScaleGestureDetector detector) {
            float angle;
            float f = 0.0f;
            TransformInfo info = new TransformInfo();
            info.deltaScale = BgEraserView.this.isScaleEnabled ? detector.getScaleFactor() : 1.0f;
            if (BgEraserView.this.isRotateEnabled) {
                angle = Vector2D.getAngle(this.mPrevSpanVector, detector.getCurrentSpanVector());
            } else {
                angle = 0.0f;
            }
            info.deltaAngle = angle;
            if (BgEraserView.this.isTranslateEnabled) {
                angle = detector.getFocusX() - this.mPivotX;
            } else {
                angle = 0.0f;
            }
            info.deltaX = angle;
            if (BgEraserView.this.isTranslateEnabled) {
                f = detector.getFocusY() - this.mPivotY;
            }
            info.deltaY = f;
            info.pivotX = this.mPivotX;
            info.pivotY = this.mPivotY;
            info.minimumScale = BgEraserView.this.minimumScale;
            info.maximumScale = BgEraserView.this.maximumScale;
            BgEraserView.this.move(view, info);
            return false;
        }
    }

    public void undoRedoListener(UndoRedoListener l) {
        this.undoRedoListener = l;
    }

    public void actionListener(ActionListener l) {
        this.actionListener = l;
    }

    public BgEraserView(Context context) {
        super(context);
        initPaint(context);
    }

    public BgEraserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    private void initPaint(Context context) {
        this.mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
        this.ctx = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.screenWidth = displaymetrics.widthPixels;
        this.brushSize = BgEraserImageUtils.dpToPx(getContext(), this.brushSize);
        this.brushSize1 = BgEraserImageUtils.dpToPx(getContext(), this.brushSize);
        this.targetBrushSize = BgEraserImageUtils.dpToPx(getContext(), 50);
        this.targetBrushSize1 = BgEraserImageUtils.dpToPx(getContext(), 50);

        this.paint.setAlpha(0);
        this.paint.setColor(0);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeJoin(Join.ROUND);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(updatebrushsize(this.brushSize1, this.scale));

        this.erPaint = new Paint();
        this.erPaint.setAntiAlias(true);
        this.erPaint.setColor(Color.BLUE);
        this.erPaint.setAntiAlias(true);
        this.erPaint.setStyle(Style.STROKE);
        this.erPaint.setStrokeJoin(Join.MITER);
        this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));

        this.erPaint1 = new Paint();
        this.erPaint1.setAntiAlias(true);
        this.erPaint1.setColor(Color.BLUE);
        this.erPaint1.setAntiAlias(true);
        this.erPaint1.setStyle(Style.STROKE);
        this.erPaint1.setStrokeJoin(Join.MITER);
        this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, this.scale));
        this.erPaint1.setPathEffect(new DashPathEffect(new float[]{10.0f, 20.0f}, 0.0f));
    }


    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            if (this.orgBit == null) {
                this.orgBit = bm.copy(bm.getConfig(), true);
            }
            this.width = bm.getWidth();
            this.height = bm.getHeight();
            this.Bitmap2 = Bitmap.createBitmap(this.width, this.height, bm.getConfig());
            this.c2 = new Canvas();
            this.c2.setBitmap(this.Bitmap2);
            this.c2.drawBitmap(bm, 0.0f, 0.0f, null);
            if (this.isSelected) {
                enableTouchClear(this.isSelected);
            }
            super.setImageBitmap(this.Bitmap2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.c2 != null) {
            Canvas canvas2;
            if (!this.updateOnly && this.isTouched) {
                this.paint = paintByMode(this.MODE, this.brushSize, this.isRectBrushEnable);
                if (this.tPath != null) {
                    this.c2.drawPath(this.tPath, this.paint);
                }
                this.isTouched = false;
            }
            if (this.MODE == this.TARGET) {
                this.f21p = new Paint();
                this.f21p.setColor(Color.BLUE);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                //canvas.drawCircle(this.globX, this.globY, (float) (this.targetBrushSize / 2), this.erPaint);
                canvas.drawCircle(this.globX, this.globY + ((float) this.offset), updatebrushsize(BgEraserImageUtils.dpToPx(getContext(), 9), this.scale), this.f21p);
                this.f21p.setStrokeWidth(updatebrushsize(BgEraserImageUtils.dpToPx(getContext(), 1), this.scale));
                canvas2 = canvas;
                canvas2.drawLine(this.globX - ((float) (this.targetBrushSize / 4)), this.globY, ((float) (this.targetBrushSize / 4)) + this.globX, this.globY, this.f21p);
                canvas2 = canvas;
                canvas2.drawLine(this.globX, this.globY - ((float) (this.targetBrushSize / 4)), this.globX, ((float) (this.targetBrushSize / 4)) + this.globY, this.f21p);
                this.drawOnLasso = true;
            }
            if (this.MODE == this.FREEMODE) {
                this.f21p = new Paint();
                this.f21p.setColor(Color.BLUE);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                //canvas.drawCircle(this.globX, this.globY, (float) (this.targetBrushSize / 2), this.erPaint);
                canvas.drawCircle(this.globX, this.globY + ((float) this.offset), updatebrushsize(BgEraserImageUtils.dpToPx(getContext(), 9), this.scale), this.f21p);
                this.f21p.setStrokeWidth(updatebrushsize(BgEraserImageUtils.dpToPx(getContext(), 1), this.scale));
                canvas2 = canvas;
                canvas2.drawLine(this.globX - ((float) (this.targetBrushSize / 4)), this.globY, ((float) (this.targetBrushSize / 4)) + this.globX, this.globY, this.f21p);
                canvas2 = canvas;
                canvas2.drawLine(this.globX, this.globY - ((float) (this.targetBrushSize / 4)), this.globX, ((float) (this.targetBrushSize / 4)) + this.globY, this.f21p);
                if (!this.drawOnLasso) {
                    this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                    canvas.drawPath(this.lPath, this.erPaint1);
                }
            }
            if (this.MODE == this.ERASE || this.MODE == this.RESTORE) {
                this.f21p = new Paint();
                this.f21p.setColor(Color.BLUE);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                if (this.isRectBrushEnable) {
                    int bs = this.brushSize / 2;
                    canvas2 = canvas;
                    canvas2.drawRect(this.globX - ((float) bs), this.globY - ((float) bs), ((float) bs) + this.globX, ((float) bs) + this.globY, this.erPaint);
                } else {
                    canvas.drawCircle(this.globX, this.globY, (float) (this.brushSize / 2), this.erPaint);
                }
                canvas.drawCircle(this.globX, this.globY + ((float) this.offset), updatebrushsize(BgEraserImageUtils.dpToPx(getContext(), 9), this.scale), this.f21p);
            }
            this.updateOnly = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int bs;
        if (event.getPointerCount() == 1) {
            if (this.actionListener != null) {
                this.actionListener.onAction(event.getAction());
            }
            if (this.MODE == this.TARGET) {
                this.drawOnLasso = false;
                this.globX = event.getX();
                this.globY = event.getY() - ((float) this.offset);
                switch (action) {
                    case 0:
                        invalidate();
                        break;
                    case 1:
                        if (this.globX >= 0.0f && this.globY >= 0.0f && this.globX < ((float) this.Bitmap2.getWidth()) && this.globY < ((float) this.Bitmap2.getHeight())) {
                            this.point = new Point((int) this.globX, (int) this.globY);
                            pc = this.Bitmap2.getPixel((int) this.globX, (int) this.globY);
                            if (!this.isAutoRunning) {
                                this.isAutoRunning = true;
                                new AsyncTaskRunner(pc).execute(new Void[0]);
                            }
                        }
                        invalidate();
                        break;
                    case 2:
                        invalidate();
                        break;
                }
            }
            if (this.MODE == this.FREEMODE) {
                this.globX = event.getX();
                this.globY = event.getY() - ((float) this.offset);
                switch (action) {
                    case 0:
                        this.isNewPath = true;
                        this.drawOnLasso = false;
                        this.sX = this.globX;
                        this.sY = this.globY;
                        this.lPath = new Path();
                        this.lPath.moveTo(this.globX, this.globY);
                        invalidate();
                        break;
                    case 1:
                        this.lPath.lineTo(this.globX, this.globY);
                        this.lPath.lineTo(this.sX, this.sY);
                        this.drawLasso = true;
                        invalidate();
                        if (this.actionListener != null) {
                            this.actionListener.onActionCompleted(FREEMODE);
                            break;
                        }
                        break;
                    case 2:
                        this.lPath.lineTo(this.globX, this.globY);
                        invalidate();
                        break;
                    default:
                        return false;
                }
            }
            if (this.MODE == this.ERASE || this.MODE == this.RESTORE) {
                bs = this.brushSize / 2;
                this.globX = event.getX();
                this.globY = event.getY() - ((float) this.offset);
                this.isTouched = true;
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                //onMagnifyView(this.globX, this.globY, event.getRawX(), event.getRawY(), this.erPaint, true);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        this.paint.setStrokeWidth((float) this.brushSize);
                        this.tPath = new Path();
                        if (this.isRectBrushEnable) {
                            this.tPath.addRect(this.globX - ((float) bs), this.globY - ((float) bs), this.globX + ((float) bs), this.globY + ((float) bs), Direction.CW);
                        } else {
                            this.tPath.moveTo(this.globX, this.globY);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        //onMagnifyView(this.globX, this.globY, event.getRawX(), event.getRawY(), this.erPaint, false);
                        if (this.tPath != null) {
                            if (this.isRectBrushEnable) {
                                this.tPath.addRect(this.globX - ((float) bs), this.globY - ((float) bs), this.globX + ((float) bs), this.globY + ((float) bs), Direction.CW);
                            } else {
                                this.tPath.lineTo(this.globX, this.globY);
                            }
                            invalidate();
                            this.changesIndx.add(this.curIndx + 1, new Path(this.tPath));
                            this.brushIndx.add(this.curIndx + 1, this.brushSize);
                            this.modeIndx.add(this.curIndx + 1, this.MODE);
                            this.brushTypeIndx.add(this.curIndx + 1, this.isRectBrushEnable);
                            this.tPath.reset();
                            this.curIndx++;
                            clearNextChanges();
                            this.tPath = null;
                            break;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (this.tPath != null) {
                            if (this.isRectBrushEnable) {
                                this.tPath.addRect(this.globX - ((float) bs), this.globY - ((float) bs), this.globX + ((float) bs), this.globY + ((float) bs), Direction.CW);
                            } else {
                                this.tPath.lineTo(this.globX, this.globY);
                            }
                            invalidate();
                            this.isMoved = true;
                            break;
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }
        if (this.tPath != null) {
            if (this.isMoved && (this.MODE == this.ERASE || this.MODE == this.RESTORE)) {
                bs = this.brushSize / 2;
                if (this.isRectBrushEnable) {
                    this.tPath.addRect(this.globX - ((float) bs), this.globY - ((float) bs), this.globX + ((float) bs), this.globY + ((float) bs), Direction.CW);
                } else {
                    this.tPath.lineTo(this.globX, this.globY);
                }
                invalidate();
                this.changesIndx.add(this.curIndx + 1, new Path(this.tPath));
                this.brushIndx.add(this.curIndx + 1, this.brushSize);
                this.modeIndx.add(this.curIndx + 1, this.MODE);
                this.brushTypeIndx.add(this.curIndx + 1, this.isRectBrushEnable);
                this.tPath.reset();
                this.curIndx++;
                clearNextChanges();
                this.tPath = null;
                this.isMoved = false;
            } else {
                this.tPath.reset();
                invalidate();
                this.tPath = null;
            }
        }
        this.mScaleGestureDetector.onTouchEvent((View) v.getParent(), event);
        invalidate();
        //onMagnifyView(this.globX, this.globY, event.getRawX(), event.getRawY(), this.erPaint, false);
        return true;
    }

    /*private void onMagnifyView(float x, float y, float rawX, float rawY, Paint erPaint, boolean needToDraw) {
        if (this.magnifyView != null) {
            Paint shaderPaint = new Paint();
            if (rawY - ((float) this.offset) < ((float) BgEraserImageUtils.dpToPx(this.ctx, 300))) {
                if (rawX < ((float) BgEraserImageUtils.dpToPx(this.ctx, 180))) {
                    this.onLeft = false;
                }
                if (rawX > ((float) (this.screenWidth - BgEraserImageUtils.dpToPx(this.ctx, 180)))) {
                    this.onLeft = true;
                }
            }
            Shader shader = new BitmapShader(this.Bitmap2, TileMode.CLAMP, TileMode.CLAMP);
            shaderPaint.setShader(shader);
            Matrix matrix = new Matrix();
            matrix.postScale(this.scale * 1.5f, this.scale * 1.5f, x, y);
            if (this.onLeft) {
                matrix.postTranslate(-(x - ((float) BgEraserImageUtils.dpToPx(this.ctx, 75))), -(y - ((float) BgEraserImageUtils.dpToPx(this.ctx, 75))));
            } else {
                matrix.postTranslate(-(x - ((float) (this.screenWidth - BgEraserImageUtils.dpToPx(this.ctx, 75)))), -(y - ((float) BgEraserImageUtils.dpToPx(this.ctx, 75))));
            }
            shader.setLocalMatrix(matrix);
            this.paint.setShader(shader);
            erPaint.setStrokeWidth(updatebrushsize(this.erps, 1.5f) / 1.5f);
            this.magnifyView.updateShaderView(shaderPaint, erPaint, (int) (((double) (this.brushSize1 / 2)) * 1.5d), needToDraw, this.onLeft, this.isRectBrushEnable);
        }
    }*/

    private void move(View view, TransformInfo info) {
        computeRenderOffset(view, info.pivotX, info.pivotY);
        adjustTranslation(view, info.deltaX, info.deltaY);
        float scale = Math.max(info.minimumScale, Math.min(info.maximumScale, view.getScaleX() * info.deltaScale));
        view.setScaleX(scale);
        view.setScaleY(scale);
        updateOnScale(scale);
        invalidate();
    }

    private static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = new float[]{deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private static void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() != pivotX || view.getPivotY() != pivotY) {
            float[] prevPoint = new float[]{0.0f, 0.0f};
            view.getMatrix().mapPoints(prevPoint);
            view.setPivotX(pivotX);
            view.setPivotY(pivotY);
            float[] currPoint = new float[]{0.0f, 0.0f};
            view.getMatrix().mapPoints(currPoint);
            float offsetY = currPoint[1] - prevPoint[1];
            view.setTranslationX(view.getTranslationX() - (currPoint[0] - prevPoint[0]));
            view.setTranslationY(view.getTranslationY() - offsetY);
        }
    }

    private void clearNextChanges() {
        int size = this.changesIndx.size();
        int i = this.curIndx + 1;
        while (size > i) {
            this.changesIndx.remove(i);
            this.brushIndx.remove(i);
            this.modeIndx.remove(i);
            this.brushTypeIndx.remove(i);
            size = this.changesIndx.size();
        }
        if (this.undoRedoListener != null) {
            this.undoRedoListener.enableUndo(true, this.curIndx + 1);
            this.undoRedoListener.enableRedo(false, this.modeIndx.size() - (this.curIndx + 1));
        }
        if (this.actionListener != null) {
            this.actionListener.onActionCompleted(this.MODE);
        }
    }

    public void setMODE(int m) {
        this.MODE = m;
        if (!(m == this.TARGET || this.Bitmap3 == null)) {
            this.Bitmap3.recycle();
            this.Bitmap3 = null;
        }
        if (m != this.FREEMODE) {
            this.drawOnLasso = true;
            this.drawLasso = false;
            if (this.Bitmap4 != null) {
                this.Bitmap4.recycle();
                this.Bitmap4 = null;
            }
        }
    }

    private Paint paintByMode(int mode, int brushSize, boolean isRectEnable) {
        this.paint = new Paint();
        this.paint.setAlpha(0);
        if (isRectEnable) {
            this.paint.setStyle(Style.FILL_AND_STROKE);
            this.paint.setStrokeJoin(Join.MITER);
            this.paint.setStrokeCap(Cap.SQUARE);
        } else {
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeJoin(Join.ROUND);
            this.paint.setStrokeCap(Cap.ROUND);
            this.paint.setStrokeWidth((float) brushSize);
        }
        this.paint.setAntiAlias(true);
        if (mode == this.ERASE) {
            this.paint.setColor(0);
            this.paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        }
        if (mode == this.RESTORE) {
            this.paint.setColor(Color.WHITE);
            this.patternBMPshader = BgRemoverActivity.patternBMPshader;
            this.paint.setShader(this.patternBMPshader);
        }
        return this.paint;
    }

    public void updateThreshHold() {
        if (this.Bitmap3 != null && !this.isAutoRunning) {
            this.isAutoRunning = true;
            new AsyncTaskRunner1(pc).execute(new Void[0]);
        }
    }

    public int getLastChangeMode() {
        if (this.curIndx < 0) {
            return this.ZOOM;
        }
        return (Integer) this.modeIndx.get(this.curIndx);
    }

    public void offset(int os) {
        this.offset1 = os;
        this.offset = (int) updatebrushsize(BgEraserImageUtils.dpToPx(this.ctx, os), this.scale);
        this.updateOnly = true;
    }

    public int offsetGet() {
        return this.offset1;
    }

    public void radius(int r) {
        this.brushSize1 = BgEraserImageUtils.dpToPx(getContext(), r);
        this.brushSize = (int) updatebrushsize(this.brushSize1, this.scale);
        this.updateOnly = true;
    }

    public float updatebrushsize(int currentbs, float scale) {
        return ((float) currentbs) / scale;
    }

    public void updateOnScale(float scale) {
        this.scale = scale;
        this.brushSize = (int) updatebrushsize(this.brushSize1, scale);
        this.targetBrushSize = (int) updatebrushsize(this.targetBrushSize1, scale);
        this.offset = (int) updatebrushsize(BgEraserImageUtils.dpToPx(this.ctx, this.offset1), scale);
    }

    public void threshold(int th) {
        this.TOLERANCE = th;

    }

    public boolean isTouchEnable() {
        return this.isSelected;
    }

    public void enableTouchClear(boolean b) {
        this.isSelected = b;
        if (b) {
            setOnTouchListener(this);
        } else {
            setOnTouchListener(null);
        }
    }

    public void enableInsideRemover(boolean enable) {
        this.insidCutEnable = enable;
        if (this.drawLasso) {
            if (this.isNewPath) {
                this.Bitmap4 = this.Bitmap2.copy(this.Bitmap2.getConfig(), true);
                drawLassoPath(this.lPath, this.insidCutEnable);
                this.changesIndx.add(this.curIndx + 1, new Path(this.lPath));
                this.brushIndx.add(this.curIndx + 1, this.brushSize);
                this.modeIndx.add(this.curIndx + 1, this.MODE);
                this.brushTypeIndx.add(this.curIndx + 1, this.isRectBrushEnable);
                this.curIndx++;
                clearNextChanges();
                invalidate();
                this.isNewPath = false;
                return;
            }
            setImageBitmap(this.Bitmap4);
            drawLassoPath(this.lPath, this.insidCutEnable);
            return;
        }

    }

    public boolean isRectBrushEnable() {
        return this.isRectBrushEnable;
    }

    public void enableRectBrush(boolean enable) {
        this.isRectBrushEnable = enable;
        this.updateOnly = true;
    }

    public void undoChange() {
        this.drawLasso = false;
        setImageBitmap(this.orgBit);
        if (this.curIndx >= 0) {
            this.curIndx--;
            redrawCanvas();
            if (this.undoRedoListener != null) {
                this.undoRedoListener.enableUndo(true, this.curIndx + 1);
                this.undoRedoListener.enableRedo(true, this.modeIndx.size() - (this.curIndx + 1));
            }
            if (this.curIndx < 0 && this.undoRedoListener != null) {
                this.undoRedoListener.enableUndo(false, this.curIndx + 1);
            }
        }
    }

    public void redoChange() {
        this.drawLasso = false;
        if (this.curIndx + 1 < this.changesIndx.size()) {
            setImageBitmap(this.orgBit);
            this.curIndx++;
            redrawCanvas();
            if (this.undoRedoListener != null) {
                this.undoRedoListener.enableUndo(true, this.curIndx + 1);
                this.undoRedoListener.enableRedo(true, this.modeIndx.size() - (this.curIndx + 1));
            }
            if (this.curIndx + 1 >= this.changesIndx.size() && this.undoRedoListener != null) {
                this.undoRedoListener.enableRedo(false, this.modeIndx.size() - (this.curIndx + 1));
            }
        }
    }

    private void redrawCanvas() {
        int i = 0;
        while (i <= this.curIndx) {
            if ((Integer) this.modeIndx.get(i) == this.ERASE || (Integer) this.modeIndx.get(i) == this.RESTORE) {
                this.tPath = new Path((Path) this.changesIndx.get(i));
                this.paint = paintByMode((Integer) this.modeIndx.get(i), (Integer) this.brushIndx.get(i), (Boolean) this.brushTypeIndx.get(i));
                this.c2.drawPath(this.tPath, this.paint);
                this.tPath.reset();
            }else if(this.modeIndx.get(i) == this.TARGET){

            }
            i = (Integer) this.modeIndx.get(i) == this.TARGET ? i + 1 : i + 1;
        }
    }

    private void drawLassoPath(Path path, boolean insidCut) {
        Paint paint;
        if (insidCut) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(0);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            this.c2.drawPath(path, paint);
        } else {
            Bitmap resultingImage = this.Bitmap2.copy(this.Bitmap2.getConfig(), true);
            new Canvas(resultingImage).drawBitmap(this.Bitmap2, 0.0f, 0.0f, null);
            this.c2.drawColor(this.ZOOM, Mode.CLEAR);
            paint = new Paint();
            paint.setAntiAlias(true);
            this.c2.drawPath(path, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            this.c2.drawBitmap(resultingImage, 0.0f, 0.0f, paint);
            resultingImage.recycle();
        }
        this.drawOnLasso = true;
    }

    public Bitmap finalBitmap() {
        return this.Bitmap2.copy(this.Bitmap2.getConfig(), true);
    }

    private int index(int x, int y, int w) {
        if (y == 0) {
            return x;
        }
        return x + ((y - 1) * w);
    }

   /* public void onSettingMgView(MagnifyView magnifyView) {
        this.magnifyView = magnifyView;
    }*/
}
