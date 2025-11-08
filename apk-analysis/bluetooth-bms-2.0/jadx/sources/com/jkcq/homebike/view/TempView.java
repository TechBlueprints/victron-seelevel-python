package com.jkcq.homebike.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import com.ble.vanomize12.R;
import com.jkcq.util.DateUtil;
import java.util.ArrayList;
import no.nordicsemi.android.ble.error.GattError;

/* loaded from: classes.dex */
public class TempView extends View {
    Paint arcCirclepaint;
    RectF arcRectF;
    Paint circlepaint;
    float currentvalue;
    private int endColor;
    LinearGradient lg;
    Paint linePant;
    Context mContext;
    int maxtemp;
    int mintemp;
    Paint paint;
    float progress;
    float radius;
    int recheith;
    private int startColor;
    int startleft;
    int startright;
    int sum;
    ArrayList<Integer> tempFvalues;
    ArrayList<Integer> tempvalues;
    Paint textUnitPaint;
    Paint textValuePaint;
    ArrayList<Integer> values;

    public TempView(Context context) {
        super(context);
        this.progress = 0.0f;
        this.arcRectF = new RectF();
        this.values = new ArrayList<>();
        this.paint = new Paint(1);
        this.circlepaint = new Paint(1);
        this.linePant = new Paint(1);
        this.arcCirclepaint = new Paint(1);
        this.textUnitPaint = new Paint(1);
        this.textValuePaint = new Paint(1);
        this.maxtemp = 120;
        this.mintemp = -50;
        this.sum = 0;
        this.tempvalues = new ArrayList<>();
        this.tempFvalues = new ArrayList<>();
        this.currentvalue = 0.0f;
        this.mContext = context;
        initPaint();
    }

    public TempView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.progress = 0.0f;
        this.arcRectF = new RectF();
        this.values = new ArrayList<>();
        this.paint = new Paint(1);
        this.circlepaint = new Paint(1);
        this.linePant = new Paint(1);
        this.arcCirclepaint = new Paint(1);
        this.textUnitPaint = new Paint(1);
        this.textValuePaint = new Paint(1);
        this.maxtemp = 120;
        this.mintemp = -50;
        this.sum = 0;
        this.tempvalues = new ArrayList<>();
        this.tempFvalues = new ArrayList<>();
        this.currentvalue = 0.0f;
        this.mContext = context;
        initPaint();
    }

    public TempView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.progress = 0.0f;
        this.arcRectF = new RectF();
        this.values = new ArrayList<>();
        this.paint = new Paint(1);
        this.circlepaint = new Paint(1);
        this.linePant = new Paint(1);
        this.arcCirclepaint = new Paint(1);
        this.textUnitPaint = new Paint(1);
        this.textValuePaint = new Paint(1);
        this.maxtemp = 120;
        this.mintemp = -50;
        this.sum = 0;
        this.tempvalues = new ArrayList<>();
        this.tempFvalues = new ArrayList<>();
        this.currentvalue = 0.0f;
        this.mContext = context;
        initPaint();
    }

    private void initPaint() {
        this.startColor = this.mContext.getResources().getColor(R.color.common_page_color);
        this.endColor = this.mContext.getResources().getColor(R.color.common_page_color);
        this.textUnitPaint.setTextSize(DateUtil.dip2px(12.0f));
        this.textUnitPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.textUnitPaint.setStyle(Paint.Style.FILL);
        this.textValuePaint.setTextSize(DateUtil.dip2px(40.0f));
        this.textValuePaint.setColor(this.mContext.getResources().getColor(R.color.common_page_color));
        this.textUnitPaint.setStyle(Paint.Style.FILL);
        this.circlepaint.setStyle(Paint.Style.FILL);
        this.circlepaint.setColor(this.mContext.getResources().getColor(R.color.common_red));
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setTextSize(DateUtil.dip2px(40.0f));
        this.paint.setStrokeWidth(DateUtil.dip2px(10.0f));
        this.arcCirclepaint.setStrokeWidth(DateUtil.dip2px(10.0f));
        this.arcCirclepaint.setStyle(Paint.Style.FILL);
        this.arcCirclepaint.setStrokeCap(Paint.Cap.ROUND);
        this.arcCirclepaint.setColor(this.mContext.getResources().getColor(R.color.white));
        this.linePant.setStyle(Paint.Style.FILL);
        this.linePant.setStrokeWidth(3.0f);
        this.linePant.setColor(this.mContext.getResources().getColor(R.color.white));
        this.paint.setColor(this.mContext.getResources().getColor(R.color.common_view_color));
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
        this.tempvalues.clear();
        this.tempvalues.add(-50);
        this.tempvalues.add(-30);
        this.tempvalues.add(0);
        this.tempvalues.add(30);
        this.tempvalues.add(60);
        this.tempvalues.add(90);
        this.tempvalues.add(120);
        this.tempFvalues.clear();
        this.tempFvalues.add(-58);
        this.tempFvalues.add(-22);
        this.tempFvalues.add(32);
        this.tempFvalues.add(86);
        this.tempFvalues.add(Integer.valueOf(GattError.GATT_SERVICE_STARTED));
        this.tempFvalues.add(194);
        this.tempFvalues.add(248);
        this.startleft = DateUtil.dip2px(60.0f);
        this.startright = getWidth() - DateUtil.dip2px(20.0f);
        this.recheith = DateUtil.dip2px(10.0f);
        int i = this.maxtemp - this.mintemp;
        this.sum = i;
        float f = ((this.startright - this.startleft) * 1.0f) / i;
        int i2 = i / 2;
        if (i % 2 != 0) {
            i2++;
        }
        int i3 = i2;
        float f2 = ((this.startright - this.startleft) * 1.0f) / i3;
        int width = getWidth() / 2;
        float height = getHeight() / 2;
        for (int i4 = 0; i4 < i3; i4++) {
            if (i4 % 5 == 0) {
                int i5 = this.startleft;
                float f3 = i4 * f2;
                int i6 = this.recheith;
                canvas.drawLine(i5 + f3, (i6 * 2) + height, i5 + f3, height - (i6 * 2), this.linePant);
            } else {
                int i7 = this.startleft;
                float f4 = i4 * f2;
                int i8 = this.recheith;
                canvas.drawLine(i7 + f4, i8 + height, i7 + f4, height - i8, this.linePant);
            }
        }
        for (int i9 = 0; i9 < this.tempvalues.size(); i9++) {
            String str = this.tempvalues.get(i9) + "";
            String str2 = this.tempFvalues.get(i9) + "";
            this.textUnitPaint.setColor(-1);
            canvas.drawLine(((this.tempvalues.get(i9).intValue() - this.mintemp) * f) + this.startleft, height + (this.recheith * 2), ((this.tempvalues.get(i9).intValue() - this.mintemp) * f) + this.startleft, height - (this.recheith * 2), this.linePant);
            if (str.equals("0")) {
                this.textUnitPaint.setColor(SupportMenu.CATEGORY_MASK);
            }
            canvas.drawText(str2, (this.startleft + ((this.tempvalues.get(i9).intValue() - this.mintemp) * f)) - (this.textUnitPaint.measureText(str2) / 2.0f), (this.recheith * 3) + height, this.textUnitPaint);
            canvas.drawText(str, (this.startleft + ((this.tempvalues.get(i9).intValue() - this.mintemp) * f)) - (this.textUnitPaint.measureText(str) / 2.0f), height - (this.recheith * 3), this.textUnitPaint);
        }
        RectF rectF = new RectF();
        float f5 = this.startleft;
        int i10 = this.recheith;
        rectF.set(f5, (i10 / 2) + height, this.startright, height - (i10 / 2));
        canvas.drawRect(rectF, this.arcCirclepaint);
        canvas.drawCircle(this.startleft - r3, height, DateUtil.dip2px(20.0f), this.circlepaint);
        Log.e("setCurrentTemp", "" + this.currentvalue);
        int i11 = this.startleft;
        float f6 = ((float) i11) + (f * (this.currentvalue - ((float) this.mintemp)));
        int i12 = this.recheith;
        rectF.set(i11, (i12 / 2) + height, f6, height - (i12 / 2));
        canvas.drawRect(rectF, this.circlepaint);
    }

    public void setCurrentTemp(float f) {
        Log.e("setCurrentTemp", "" + f + "------");
        this.currentvalue = f;
        invalidate();
    }
}
