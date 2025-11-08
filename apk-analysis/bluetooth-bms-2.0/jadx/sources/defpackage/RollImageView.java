package defpackage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.jkcq.viewlibrary.R;

/* loaded from: classes.dex */
public class RollImageView extends View {
    private Bitmap mBitmap;
    private int mBitmapH;
    private int mBitmapW;
    private Rect mDestRect;
    private int mH;
    private Rect mSrcRect;
    private int mValue;
    private ValueAnimator mValueAnimator;
    private int mW;

    private void init() {
    }

    public RollImageView(Context context) {
        super(context);
        this.mSrcRect = new Rect();
        this.mDestRect = new Rect();
        init();
    }

    public RollImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSrcRect = new Rect();
        this.mDestRect = new Rect();
        init();
    }

    public RollImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSrcRect = new Rect();
        this.mDestRect = new Rect();
        init();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mW = i;
        this.mH = i2;
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.brvah_sample_footer_loading);
        this.mBitmap = bitmapDecodeResource;
        this.mBitmapH = bitmapDecodeResource.getHeight();
        int width = this.mBitmap.getWidth();
        this.mBitmapW = width;
        int i5 = this.mH;
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(this.mBitmap, (int) ((i5 / this.mBitmapH) * width), i5, true);
        this.mBitmap = bitmapCreateScaledBitmap;
        this.mBitmapH = bitmapCreateScaledBitmap.getHeight();
        this.mBitmapW = this.mBitmap.getWidth();
        setLayerType(1, null);
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(0, this.mBitmapW);
        this.mValueAnimator = valueAnimatorOfInt;
        valueAnimatorOfInt.setDuration(this.mBitmapW * 5);
        this.mValueAnimator.setRepeatCount(-1);
        this.mValueAnimator.setInterpolator(new LinearInterpolator());
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: RollImageView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                RollImageView.this.mValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                RollImageView.this.invalidate();
            }
        });
        this.mValueAnimator.start();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = this.mSrcRect;
        int i = this.mValue;
        rect.set(i, 0, this.mW + i, this.mBitmapH);
        this.mDestRect.set(0, 0, this.mW, this.mH);
        canvas.drawBitmap(this.mBitmap, this.mSrcRect, this.mDestRect, (Paint) null);
        int i2 = this.mValue;
        int i3 = this.mBitmapW;
        int i4 = this.mW;
        if (i2 >= i3 - i4) {
            this.mSrcRect.set(0, 0, i2 - (i3 - i4), this.mBitmapH);
            Rect rect2 = this.mDestRect;
            int i5 = this.mW;
            float f = this.mValue - (this.mBitmapW - i5);
            float f2 = this.mBitmapH;
            int i6 = this.mH;
            rect2.set(i5 - ((int) (f / (f2 / i6))), 0, i5, i6);
            canvas.drawBitmap(this.mBitmap, this.mSrcRect, this.mDestRect, (Paint) null);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(measureWidth(i), measureHeight(i2));
    }

    private int measureWidth(int i) {
        View.MeasureSpec.getMode(i);
        return View.MeasureSpec.getSize(i);
    }

    private int measureHeight(int i) {
        View.MeasureSpec.getMode(i);
        return View.MeasureSpec.getSize(i);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.mValueAnimator.cancel();
    }
}
