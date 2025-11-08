package com.jkcq.homebike.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.jkcq.homebike.R;

/* loaded from: classes.dex */
public class NoiseboardView2 extends View {
    final String TAG;
    private int[] color;
    float curSpeed;
    float curSpeed2;
    float curSpeed3;
    private float initAngle;
    private float mBigScaleAngle;
    private int mBigScaleRadius;
    private int mBigSliceCount;
    private float mCenterX;
    private float mCenterY;
    private int mCircleRadius;
    private String[] mGraduations;
    private int mMaxValue;
    private int mMinValue;
    private int mNumScaleRadius;
    private Paint mPaintCirclePointer;
    private Paint mPaintRibbon;
    private Paint mPaintScale;
    private Paint mPaintScaleText;
    private Paint mPaintValue;
    private int mPointerRadius;
    private int mRadius;
    private float mRealTimeValue;
    private RectF mRectRibbon;
    private Rect mRectScaleText;
    private int mRibbonWidth;
    private int mScaleColor;
    private int mScaleCountInOneBigScale;
    private int mScaleTextSize;
    private float mSmallScaleAngle;
    private int mSmallScaleCount;
    private int mSmallScaleRadius;
    private int mStartAngle;
    private int mSweepAngle;
    private SweepGradient mSweepGradient;
    private int mTargetScaleRadius;
    private String mUnitText;
    private int mUnitTextSize;
    private int mViewColor_green;
    private int mViewColor_orange;
    private int mViewColor_red;
    private int mViewColor_yellow;
    private int mViewHeight;
    private int mViewWidth;
    private Path path;
    RectF rectF;

    public NoiseboardView2(Context context) {
        this(context, null);
    }

    public NoiseboardView2(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NoiseboardView2(Context context, AttributeSet attributeSet, int i) throws Resources.NotFoundException {
        super(context, attributeSet, i);
        this.TAG = "NoiseboardView";
        this.mUnitText = "";
        this.mRealTimeValue = -0.0f;
        this.color = new int[7];
        this.rectF = new RectF();
        this.curSpeed = 65.0f;
        this.curSpeed2 = 66.0f;
        this.curSpeed3 = 66.0f;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.NoiseboardView, i, 0);
        this.mRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(3, dpToPx(80));
        this.mBigSliceCount = typedArrayObtainStyledAttributes.getInteger(0, 5);
        this.mScaleCountInOneBigScale = typedArrayObtainStyledAttributes.getInteger(9, 5);
        this.mScaleColor = typedArrayObtainStyledAttributes.getColor(8, -1);
        this.mScaleTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(4, spToPx(12));
        this.mUnitText = typedArrayObtainStyledAttributes.getString(5);
        this.mUnitTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(6, spToPx(14));
        this.mMinValue = typedArrayObtainStyledAttributes.getInteger(2, 0);
        this.mMaxValue = typedArrayObtainStyledAttributes.getInteger(1, 150);
        this.mRibbonWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(7, 0);
        typedArrayObtainStyledAttributes.recycle();
        init();
    }

    private void init() throws Resources.NotFoundException {
        this.mStartAngle = 150;
        this.mSweepAngle = 240;
        int i = this.mRadius;
        this.mPointerRadius = i;
        this.mCircleRadius = i / 17;
        this.mSmallScaleRadius = i - dpToPx(10);
        this.mBigScaleRadius = this.mRadius - dpToPx(18);
        this.mTargetScaleRadius = this.mRadius - dpToPx(25);
        this.mNumScaleRadius = this.mRadius - dpToPx(25);
        int i2 = this.mBigSliceCount;
        this.mSmallScaleCount = i2 * 5;
        float f = this.mSweepAngle / i2;
        this.mBigScaleAngle = f;
        this.mSmallScaleAngle = f / this.mScaleCountInOneBigScale;
        this.mGraduations = getMeasureNumbers();
        int paddingLeft = getPaddingLeft() + (this.mRadius * 2) + getPaddingRight() + dpToPx(4);
        this.mViewWidth = paddingLeft;
        this.mViewHeight = paddingLeft;
        this.mCenterX = paddingLeft / 2.0f;
        this.mCenterY = paddingLeft / 2.0f;
        Paint paint = new Paint();
        this.mPaintScale = paint;
        paint.setAntiAlias(true);
        this.mPaintScale.setColor(this.mScaleColor);
        this.mPaintScale.setStyle(Paint.Style.STROKE);
        this.mPaintScale.setStrokeCap(Paint.Cap.ROUND);
        Paint paint2 = new Paint();
        this.mPaintScaleText = paint2;
        paint2.setAntiAlias(true);
        this.mPaintScaleText.setColor(this.mScaleColor);
        this.mPaintScaleText.setStyle(Paint.Style.FILL);
        Paint paint3 = new Paint();
        this.mPaintCirclePointer = paint3;
        paint3.setAntiAlias(true);
        this.mRectScaleText = new Rect();
        this.path = new Path();
        Paint paint4 = new Paint();
        this.mPaintValue = paint4;
        paint4.setAntiAlias(true);
        this.mPaintValue.setStyle(Paint.Style.STROKE);
        this.mPaintValue.setTextAlign(Paint.Align.CENTER);
        this.mPaintValue.setTextSize(this.mUnitTextSize);
        this.initAngle = getAngleFromResult(this.mRealTimeValue);
        this.mViewColor_green = getResources().getColor(com.ble.vanomize12.R.color.white_50);
        this.mViewColor_yellow = getResources().getColor(com.ble.vanomize12.R.color.white);
        this.mViewColor_orange = getResources().getColor(com.ble.vanomize12.R.color.white);
        int color = getResources().getColor(com.ble.vanomize12.R.color.white_50);
        this.mViewColor_red = color;
        int[] iArr = this.color;
        iArr[0] = color;
        iArr[1] = color;
        int i3 = this.mViewColor_green;
        iArr[2] = i3;
        iArr[3] = i3;
        iArr[4] = this.mViewColor_yellow;
        iArr[5] = this.mViewColor_orange;
        iArr[6] = color;
        Paint paint5 = new Paint();
        this.mPaintRibbon = paint5;
        paint5.setAntiAlias(true);
        this.mPaintRibbon.setStyle(Paint.Style.STROKE);
        this.mPaintRibbon.setColor(getResources().getColor(com.ble.vanomize12.R.color.white));
        this.mPaintRibbon.setStrokeWidth(this.mRibbonWidth);
        int i4 = this.mRibbonWidth;
        if (i4 > 0) {
            int iDpToPx = (this.mRadius - (i4 / 2)) + dpToPx(1);
            float f2 = this.mCenterX;
            float f3 = iDpToPx;
            float f4 = this.mCenterY;
            this.mRectRibbon = new RectF(f2 - f3, f4 - f3, f2 + f3, f4 + f3);
        }
    }

    private String[] getMeasureNumbers() {
        String[] strArr = new String[this.mBigSliceCount + 1];
        for (int i = 0; i <= this.mBigSliceCount; i++) {
            strArr[i] = String.valueOf(this.mMinValue + (i * 50));
        }
        return strArr;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        if (mode == 1073741824) {
            this.mViewWidth = size;
        }
        if (mode == Integer.MIN_VALUE) {
            this.mViewWidth = Math.min(this.mViewWidth, size);
        }
        if (mode2 == 1073741824) {
            this.mViewHeight = size2;
        } else {
            int iMax = (int) (Math.max(Math.max(Math.abs(getCoordinatePoint(this.mRadius, this.mStartAngle)[1]) - this.mCenterY, Math.abs(getCoordinatePoint(this.mRadius, this.mStartAngle + this.mSweepAngle)[1]) - this.mCenterY), this.mCircleRadius + dpToPx(2) + dpToPx(25)) + this.mRadius + getPaddingTop() + getPaddingBottom() + (dpToPx(2) * 2));
            this.mViewHeight = iMax;
            if (mode2 == Integer.MIN_VALUE) {
                this.mViewHeight = Math.min(iMax, size2);
            }
        }
        setMeasuredDimension(this.mViewWidth, this.mViewHeight);
    }

    public void setTargetSpeed(int i) {
        float f = i;
        this.curSpeed = f;
        this.curSpeed2 = f - 1.0f;
        this.curSpeed3 = f + 1.0f;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        this.mPaintCirclePointer.setShader(null);
        this.mPaintScale.setStrokeWidth(dpToPx(1));
        this.mPaintScale.setColor(getResources().getColor(com.ble.vanomize12.R.color.common_page_color));
        this.mPaintScaleText.setColor(getResources().getColor(com.ble.vanomize12.R.color.white));
        for (int i = 0; i <= this.mBigSliceCount; i++) {
            float f = (i * this.mBigScaleAngle) + this.mStartAngle;
            float[] coordinatePoint = getCoordinatePoint(this.mRadius - dpToPx(6), f);
            float[] coordinatePoint2 = getCoordinatePoint(this.mBigScaleRadius, f);
            canvas.drawLine(coordinatePoint[0], coordinatePoint[1], coordinatePoint2[0], coordinatePoint2[1], this.mPaintScale);
            this.mPaintScaleText.setTextSize(this.mScaleTextSize);
            String str = this.mGraduations[i];
            this.mPaintScaleText.getTextBounds(str, 0, str.length(), this.mRectScaleText);
            float f2 = f % 360.0f;
            if (f2 >= 135.0f && f2 < 215.0f) {
                this.mPaintScaleText.setTextAlign(Paint.Align.LEFT);
            } else if ((f2 >= 0.0f && f2 <= 45.0f) || (f2 > 325.0f && f2 <= 360.0f)) {
                this.mPaintScaleText.setTextAlign(Paint.Align.RIGHT);
            } else {
                this.mPaintScaleText.setTextAlign(Paint.Align.CENTER);
            }
            float[] coordinatePoint3 = getCoordinatePoint(this.mNumScaleRadius, f);
            if (i == 0 || i == this.mBigSliceCount) {
                canvas.drawText(str, coordinatePoint3[0], coordinatePoint3[1] + (this.mRectScaleText.height() / 2), this.mPaintScaleText);
            } else {
                canvas.drawText(str, coordinatePoint3[0], coordinatePoint3[1] + this.mRectScaleText.height(), this.mPaintScaleText);
            }
        }
        this.mPaintScale.setColor(getResources().getColor(com.ble.vanomize12.R.color.common_page_color1));
        this.mPaintScale.setStrokeWidth(dpToPx(1));
        for (int i2 = 0; i2 < this.mSmallScaleCount; i2++) {
            if (i2 % this.mScaleCountInOneBigScale != 0) {
                float f3 = (i2 * this.mSmallScaleAngle) + this.mStartAngle;
                float[] coordinatePoint4 = getCoordinatePoint(this.mSmallScaleRadius, f3);
                float[] coordinatePoint5 = getCoordinatePoint(this.mBigScaleRadius, f3);
                this.mPaintScale.setStrokeWidth(dpToPx(1));
                canvas.drawLine(coordinatePoint4[0], coordinatePoint4[1], coordinatePoint5[0], coordinatePoint5[1], this.mPaintScale);
            }
        }
        this.mPaintCirclePointer.setColor(getResources().getColor(com.ble.vanomize12.R.color.common_page_color));
        Double dValueOf = Double.valueOf((this.mStartAngle * 1.0d) / 200.0d);
        float fDoubleValue = (float) ((dValueOf.doubleValue() * this.curSpeed) + 135.0d);
        float[] coordinatePoint6 = getCoordinatePoint(this.mRadius, (float) (this.mSweepAngle + (dValueOf.doubleValue() * this.curSpeed2)));
        float[] coordinatePoint7 = getCoordinatePoint(this.mTargetScaleRadius, fDoubleValue);
        float[] coordinatePoint8 = getCoordinatePoint(this.mRadius, (float) (this.mSweepAngle + (dValueOf.doubleValue() * this.curSpeed3)));
        this.path.reset();
        this.mPaintCirclePointer.setStyle(Paint.Style.FILL);
        this.path.moveTo(coordinatePoint6[0], coordinatePoint6[1]);
        this.path.lineTo(coordinatePoint7[0], coordinatePoint7[1]);
        this.path.lineTo(coordinatePoint8[0], coordinatePoint8[1]);
        this.path.close();
        canvas.drawPath(this.path, this.mPaintCirclePointer);
        this.mPaintCirclePointer.setColor(getResources().getColor(com.ble.vanomize12.R.color.common_page_color1));
        this.path.reset();
        this.mPaintCirclePointer.setStyle(Paint.Style.FILL);
        float[] coordinatePoint9 = getCoordinatePoint(this.mCircleRadius / 2, this.initAngle + 25.0f);
        this.path.moveTo(coordinatePoint9[0], coordinatePoint9[1]);
        Log.e("绘制三角形指针", "point1[0]=" + coordinatePoint9[0] + ",point1[1]=" + coordinatePoint9[1] + ",initAngle=" + this.initAngle);
        float[] coordinatePoint10 = getCoordinatePoint(this.mCircleRadius / 2, this.initAngle - 25.0f);
        this.path.lineTo(coordinatePoint10[0], coordinatePoint10[1]);
        Log.e("绘制三角形指针", "point2[0]=" + coordinatePoint10[0] + ",point2[1]=" + coordinatePoint10[1]);
        float[] coordinatePoint11 = getCoordinatePoint(this.mPointerRadius, this.initAngle);
        this.path.lineTo(coordinatePoint11[0], coordinatePoint11[1]);
        Log.e("绘制三角形指针", "point3[0]=" + coordinatePoint11[0] + ",point3[1]=" + coordinatePoint11[1]);
        this.path.close();
        canvas.drawPath(this.path, this.mPaintCirclePointer);
        canvas.drawCircle((coordinatePoint9[0] + coordinatePoint10[0]) / 2.0f, (coordinatePoint9[1] + coordinatePoint10[1]) / 2.0f, (float) (this.mCircleRadius / 2), this.mPaintCirclePointer);
        this.mPaintCirclePointer.setColor(getResources().getColor(com.ble.vanomize12.R.color.common_page_color));
        this.mPaintCirclePointer.setStyle(Paint.Style.FILL);
        this.mPaintCirclePointer.setStrokeWidth(dpToPx(4));
        canvas.drawCircle(this.mCenterX, this.mCenterY, dpToPx(4), this.mPaintCirclePointer);
        RectF rectF = this.rectF;
        float f4 = this.mCenterX;
        int i3 = this.mRadius;
        float f5 = this.mCenterY;
        rectF.set(f4 - i3, f5 - i3, f4 + i3, f5 + i3);
        this.mPaintCirclePointer.setStyle(Paint.Style.STROKE);
        this.mPaintCirclePointer.setStrokeWidth(8.0f);
        Log.e("pointerDegree", "pointerDegree=" + fDoubleValue);
        this.path.rewind();
        this.path.arcTo(this.rectF, 135.0f, 270.0f);
        SweepGradient sweepGradient = new SweepGradient(this.mCenterX, this.mCenterY, this.color, (float[]) null);
        this.mSweepGradient = sweepGradient;
        this.mPaintCirclePointer.setShader(sweepGradient);
        canvas.drawPath(this.path, this.mPaintCirclePointer);
    }

    private int dpToPx(int i) {
        return (int) TypedValue.applyDimension(1, i, getResources().getDisplayMetrics());
    }

    private int spToPx(int i) {
        return (int) TypedValue.applyDimension(2, i, getResources().getDisplayMetrics());
    }

    public float[] getCoordinatePoint(int i, float f) {
        float[] fArr = new float[2];
        double radians = Math.toRadians(f);
        if (f < 90.0f) {
            double d = i;
            fArr[0] = (float) (this.mCenterX + (Math.cos(radians) * d));
            fArr[1] = (float) (this.mCenterY + (Math.sin(radians) * d));
        } else if (f == 90.0f) {
            fArr[0] = this.mCenterX;
            fArr[1] = this.mCenterY + i;
        } else if (f > 90.0f && f < 180.0f) {
            double d2 = ((180.0f - f) * 3.141592653589793d) / 180.0d;
            double d3 = i;
            fArr[0] = (float) (this.mCenterX - (Math.cos(d2) * d3));
            fArr[1] = (float) (this.mCenterY + (Math.sin(d2) * d3));
        } else if (f == 180.0f) {
            fArr[0] = this.mCenterX - i;
            fArr[1] = this.mCenterY;
        } else if (f > 180.0f && f < 270.0f) {
            double d4 = ((f - 180.0f) * 3.141592653589793d) / 180.0d;
            double d5 = i;
            fArr[0] = (float) (this.mCenterX - (Math.cos(d4) * d5));
            fArr[1] = (float) (this.mCenterY - (Math.sin(d4) * d5));
        } else if (f == 270.0f) {
            fArr[0] = this.mCenterX;
            fArr[1] = this.mCenterY - i;
        } else {
            double d6 = ((360.0f - f) * 3.141592653589793d) / 180.0d;
            double d7 = i;
            fArr[0] = (float) (this.mCenterX + (Math.cos(d6) * d7));
            fArr[1] = (float) (this.mCenterY - (Math.sin(d6) * d7));
        }
        Log.e("getCoordinatePoint", "radius=" + i + ",cirAngle=" + f + ",point[0]=" + fArr[0] + ",point[1]=" + fArr[1]);
        return fArr;
    }

    private float getAngleFromResult(float f) {
        if (f > this.mMaxValue) {
            return 360.0f;
        }
        return ((this.mSweepAngle * (f - this.mMinValue)) / (r0 - r2)) + this.mStartAngle;
    }

    public static String trimFloat(float f) {
        if (Math.round(f) - f == 0.0f) {
            return String.valueOf((long) f);
        }
        return String.valueOf(f);
    }

    public float getRealTimeValue() {
        return this.mRealTimeValue;
    }

    public void setRealTimeValue(float f) {
        if (f > this.mMaxValue) {
            return;
        }
        this.mRealTimeValue = f;
        this.initAngle = getAngleFromResult(f);
        invalidate();
    }
}
