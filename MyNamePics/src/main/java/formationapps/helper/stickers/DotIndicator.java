package formationapps.helper.stickers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.formationapps.nameart.R;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class DotIndicator extends View implements OnPageChangeListener {
    private static final int INVALID_POINTER = -1;
    private final Paint mPaintFill;
    private final Paint mPaintPageFill;
    private final Paint mPaintStroke;
    private final Paint mPaintStrokeFill;
    private int mActivePointerId;
    private boolean mCentered;
    private int mCurrentPage;
    private boolean mIsDragging;
    private float mLastMotionX;
    private OnPageChangeListener mListener;
    private int mOrientation;
    private float mPageOffset;
    private float mRadius;
    private int mScrollState;
    private boolean mSnap;
    private int mSnapPage;
    private int mTouchSlop;
    private ViewPager mViewPager;

    public DotIndicator(Context context) {
        this(context, null);
    }

    public DotIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPaintPageFill = new Paint(1);
        this.mPaintStroke = new Paint(1);
        this.mPaintStrokeFill = new Paint(1);
        this.mPaintFill = new Paint(1);
        this.mLastMotionX = -1f;
        this.mActivePointerId = INVALID_POINTER;
        if (!isInEditMode()) {
            Resources res = getResources();
            float defaultStrokeWidth = res.getDimension(R.dimen.default_circle_indicator_stroke_width);
            float defaultRadius = res.getDimension(R.dimen.default_circle_indicator_radius);
            this.mCentered = true;
            this.mOrientation = 0;
            this.mPaintPageFill.setStyle(Style.FILL);
            this.mPaintPageFill.setColor(0);
            this.mPaintStroke.setStyle(Style.STROKE);
            this.mPaintStroke.setColor(INVALID_POINTER);
            this.mPaintStroke.setStrokeWidth(defaultStrokeWidth);
            this.mPaintStrokeFill.setStyle(Style.STROKE);
            this.mPaintStrokeFill.setColor(Color.parseColor("#FFFD8034"));//-163788
            this.mPaintStrokeFill.setStrokeWidth(defaultStrokeWidth);
            this.mPaintFill.setStyle(Style.FILL);
            this.mPaintFill.setColor(Color.parseColor("#FFFD8034"));//-163788
            this.mRadius = defaultRadius;
            this.mSnap = true;
            this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        }
    }

    public boolean isCentered() {
        return this.mCentered;
    }

    public void setCentered(boolean centered) {
        this.mCentered = centered;
        invalidate();
    }

    public int getPageColor() {
        return this.mPaintPageFill.getColor();
    }

    public void setPageColor(int pageColor) {
        this.mPaintPageFill.setColor(pageColor);
        invalidate();
    }

    public int getFillColor() {
        return this.mPaintFill.getColor();
    }

    public void setFillColor(int fillColor) {
        this.mPaintFill.setColor(fillColor);
        invalidate();
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setOrientation(int orientation) {
        switch (orientation) {
            case HORIZONTAL /*0*/:
            case VERTICAL /*1*/:
                this.mOrientation = orientation;
                requestLayout();
            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
        }
    }

    public int getStrokeColor() {
        return this.mPaintStroke.getColor();
    }

    public void setStrokeColor(int strokeColor) {
        this.mPaintStroke.setColor(strokeColor);
        invalidate();
    }

    public float getStrokeWidth() {
        return this.mPaintStroke.getStrokeWidth();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mPaintStroke.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
    }

    public boolean isSnap() {
        return this.mSnap;
    }

    public void setSnap(boolean snap) {
        this.mSnap = snap;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mViewPager != null) {
            int count = this.mViewPager.getAdapter().getCount();
            if (count != 0) {
                int r0 = this.mCurrentPage;
                if (r0 >= count) {
                    setCurrentItem(count + INVALID_POINTER);
                    return;
                }
                int longSize;
                int longPaddingBefore;
                int longPaddingAfter;
                int shortPaddingBefore;
                float dX;
                float dY;
                if (this.mOrientation == 0) {
                    longSize = getWidth();
                    longPaddingBefore = getPaddingLeft();
                    longPaddingAfter = getPaddingRight();
                    shortPaddingBefore = getPaddingTop();
                } else {
                    longSize = getHeight();
                    longPaddingBefore = getPaddingTop();
                    longPaddingAfter = getPaddingBottom();
                    shortPaddingBefore = getPaddingLeft();
                }
                float threeRadius = this.mRadius * 3.0f;
                float shortOffset = ((float) shortPaddingBefore) + this.mRadius;
                float longOffset = ((float) longPaddingBefore) + this.mRadius;
                if (this.mCentered) {
                    longOffset += (((float) ((longSize - longPaddingBefore) - longPaddingAfter)) / 2.0f) - ((((float) count) * threeRadius) / 2.0f);
                }
                float pageFillRadius = this.mRadius;
                if (this.mPaintStroke.getStrokeWidth() > 0.0f) {
                    pageFillRadius -= this.mPaintStroke.getStrokeWidth() / 2.0f;
                }
                for (int iLoop = 0; iLoop < count; iLoop++) {
                    float drawLong = longOffset + (((float) iLoop) * threeRadius);
                    if (this.mOrientation == 0) {
                        dX = drawLong;
                        dY = shortOffset;
                    } else {
                        dX = shortOffset;
                        dY = drawLong;
                    }
                    if (this.mPaintPageFill.getAlpha() > 0) {
                        canvas.drawCircle(dX, dY, pageFillRadius, this.mPaintPageFill);
                    }
                    if (pageFillRadius != this.mRadius) {
                        r0 = this.mSnapPage;
                        if (iLoop == r0) {
                            canvas.drawCircle(dX, dY, this.mRadius, this.mPaintStrokeFill);
                        } else {
                            canvas.drawCircle(dX, dY, this.mRadius, this.mPaintStroke);
                        }
                    }
                }
                float cx = ((float) (this.mSnap ? this.mSnapPage : this.mCurrentPage)) * threeRadius;
                if (!this.mSnap) {
                    cx += this.mPageOffset * threeRadius;
                }
                if (this.mOrientation == 0) {
                    dX = longOffset + cx;
                    dY = shortOffset;
                } else {
                    dX = shortOffset;
                    dY = longOffset + cx;
                }
                canvas.drawCircle(dX, dY, this.mRadius, this.mPaintFill);
            }
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent ev) {
        if (super.onTouchEvent(ev)) {
            return true;
        }
        if (this.mViewPager == null || this.mViewPager.getAdapter().getCount() == 0) {
            return false;
        }
        int action = ev.getAction() & 255;
        switch (action) {
            case 0 /*0*/:
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                this.mLastMotionX = ev.getX();
                break;
            case 1 /*1*/:
            case 3 /*3*/:
                if (!this.mIsDragging) {
                    int count = this.mViewPager.getAdapter().getCount();
                    int width = getWidth();
                    float halfWidth = ((float) width) / 2.0f;
                    float sixthWidth = ((float) width) / 6.0f;
                    if (this.mCurrentPage > 0 && ev.getX() < halfWidth - sixthWidth) {
                        if (action != 3) {
                            this.mViewPager.setCurrentItem(this.mCurrentPage + INVALID_POINTER);
                        }
                        return true;
                    } else if (this.mCurrentPage < count + INVALID_POINTER && ev.getX() > halfWidth + sixthWidth) {
                        if (action != 3) {
                            this.mViewPager.setCurrentItem(this.mCurrentPage + 1);
                        }
                        return true;
                    }
                }
                this.mIsDragging = false;
                this.mActivePointerId = INVALID_POINTER;
                if (this.mViewPager.isFakeDragging()) {
                    this.mViewPager.endFakeDrag();
                    break;
                }
                break;
            case 2 /*2*/:
                float x = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
                float deltaX = x - this.mLastMotionX;
                if (!this.mIsDragging && Math.abs(deltaX) > ((float) this.mTouchSlop)) {
                    this.mIsDragging = true;
                }
                if (this.mIsDragging) {
                    this.mLastMotionX = x;
                    if (this.mViewPager.isFakeDragging() || this.mViewPager.beginFakeDrag()) {
                        this.mViewPager.fakeDragBy(deltaX);
                        break;
                    }
                }
                break;
            case 5 /*5*/:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = MotionEventCompat.getX(ev, index);
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case 6 /*6*/:
                int pointerIndex = MotionEventCompat.getActionIndex(ev);
                if (MotionEventCompat.getPointerId(ev, pointerIndex) == this.mActivePointerId) {
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, pointerIndex == 0 ? 1 : 0);
                }
                this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
                break;
        }
        return true;
    }

    public void setViewPager(ViewPager view) {
        if (this.mViewPager != view) {
            if (this.mViewPager != null) {
                this.mViewPager.addOnPageChangeListener(null);
            }
            if (view.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.mViewPager = view;
            this.mViewPager.addOnPageChangeListener(this);
            invalidate();
        }
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    public void setCurrentItem(int item) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.mViewPager.setCurrentItem(item);
        this.mCurrentPage = item;
        invalidate();
    }

    public void notifyDataSetChanged() {
        invalidate();
    }

    public void onPageScrollStateChanged(int state) {
        this.mScrollState = state;
        if (this.mListener != null) {
            this.mListener.onPageScrollStateChanged(state);
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.mCurrentPage = position;
        this.mPageOffset = positionOffset;
        invalidate();
        if (this.mListener != null) {
            this.mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    public void onPageSelected(int position) {
        if (this.mSnap || this.mScrollState == 0) {
            this.mCurrentPage = position;
            this.mSnapPage = position;
            invalidate();
        }
        if (this.mListener != null) {
            this.mListener.onPageSelected(position);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOrientation == 0) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
        }
    }

    private int measureLong(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824 || this.mViewPager == null) {
            return specSize;
        }
        int count = this.mViewPager.getAdapter().getCount();
        int result = (int) (((((float) (getPaddingLeft() + getPaddingRight())) + (((float) (count * 2)) * this.mRadius)) + (((float) count) * this.mRadius)) + 1.0f);
        if (specMode == ExploreByTouchHelper.INVALID_ID) {
            return Math.min(result, specSize);
        }
        return result;
    }

    private int measureShort(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            return specSize;
        }
        int result = (int) ((((2.0f * this.mRadius) + ((float) getPaddingTop())) + ((float) getPaddingBottom())) + 3.0f);
        if (specMode == ExploreByTouchHelper.INVALID_ID) {
            return Math.min(result, specSize);
        }
        return result;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCurrentPage = savedState.currentPage;
        this.mSnapPage = savedState.currentPage;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentPage = this.mCurrentPage;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;

        static {
            CREATOR = new Creator<SavedState>() {
                @Override
                public SavedState createFromParcel(Parcel source) {
                    return new SavedState(source);
                }

                @Override
                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
        }

        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentPage = in.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.currentPage);
        }
    }
}
