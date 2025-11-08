package com.jkcq.homebike.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import com.ble.vanomize12.R;
import com.jkcq.util.DateUtil;

/* loaded from: classes.dex */
public class CirclebarAnimatorView extends View {
    Paint arcCirclepaint;
    RectF arcRectF;
    Paint circlepaint;
    private int endColor;
    LinearGradient lg;
    Context mContext;
    Paint paint;
    float progress;
    float radius;
    private int startColor;
    Paint textUnitPaint;
    Paint textValuePaint;

    public void setCurrentType(int i) {
    }

    public CirclebarAnimatorView(Context context) {
        super(context);
        this.progress = 0.0f;
        this.arcRectF = new RectF();
        this.paint = new Paint(1);
        this.circlepaint = new Paint(1);
        this.arcCirclepaint = new Paint(1);
        this.textUnitPaint = new Paint(1);
        this.textValuePaint = new Paint(1);
        this.mContext = context;
        initPaint();
    }

    public CirclebarAnimatorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.progress = 0.0f;
        this.arcRectF = new RectF();
        this.paint = new Paint(1);
        this.circlepaint = new Paint(1);
        this.arcCirclepaint = new Paint(1);
        this.textUnitPaint = new Paint(1);
        this.textValuePaint = new Paint(1);
        this.mContext = context;
        initPaint();
    }

    public CirclebarAnimatorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.progress = 0.0f;
        this.arcRectF = new RectF();
        this.paint = new Paint(1);
        this.circlepaint = new Paint(1);
        this.arcCirclepaint = new Paint(1);
        this.textUnitPaint = new Paint(1);
        this.textValuePaint = new Paint(1);
        this.mContext = context;
        initPaint();
    }

    private void initPaint() {
        this.startColor = this.mContext.getResources().getColor(R.color.common_page_color1);
        this.endColor = this.mContext.getResources().getColor(R.color.common_page_color1);
        this.textUnitPaint.setTextSize(DateUtil.dip2px(20.0f));
        this.textUnitPaint.setColor(-1);
        this.textUnitPaint.setStyle(Paint.Style.FILL);
        this.textValuePaint.setTextSize(DateUtil.dip2px(40.0f));
        this.textUnitPaint.setColor(this.mContext.getResources().getColor(R.color.common_page_color1));
        this.textValuePaint.setColor(this.mContext.getResources().getColor(R.color.common_page_color1));
        this.textUnitPaint.setStyle(Paint.Style.FILL);
        this.circlepaint.setStyle(Paint.Style.STROKE);
        this.circlepaint.setStrokeWidth(DateUtil.dip2px(10.0f));
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setTextSize(DateUtil.dip2px(40.0f));
        this.paint.setStrokeWidth(DateUtil.dip2px(10.0f));
        this.arcCirclepaint.setStrokeWidth(DateUtil.dip2px(10.0f));
        this.arcCirclepaint.setStyle(Paint.Style.STROKE);
        this.arcCirclepaint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setColor(this.mContext.getResources().getColor(R.color.common_view_color));
        this.circlepaint.setColor(this.mContext.getResources().getColor(R.color.common_page_color));
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.radius = (getWidth() - DateUtil.dip2px(10.0f)) / 2;
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float f) {
        this.progress = f;
        invalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth() / 2;
        float height = getHeight() / 2;
        canvas.drawCircle(width, height, this.radius, this.circlepaint);
        RectF rectF = this.arcRectF;
        float f = this.radius;
        rectF.set(width - f, height - f, width + f, f + height);
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 100.0f, this.arcRectF.right, this.endColor, this.startColor, Shader.TileMode.MIRROR);
        this.lg = linearGradient;
        this.arcCirclepaint.setShader(linearGradient);
        canvas.rotate(-90.0f, width, height);
        canvas.drawArc(this.arcRectF, 0.0f, 3.6f * this.progress, false, this.arcCirclepaint);
    }
}
