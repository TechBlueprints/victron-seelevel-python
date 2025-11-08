package cn.xmliu.speedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.jkcq.homebike.R;
import com.jkcq.util.DateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import no.nordicsemi.android.ble.error.GattError;
import no.nordicsemi.android.log.LogContract;

/* compiled from: SpeedView.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001a\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0012\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0002J\u0012\u0010-\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0002J\u0012\u0010.\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0002J\u0012\u0010/\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0002J\u0012\u00100\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0002J\u0012\u00101\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0002J\u0012\u00102\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0002J\u0010\u00103\u001a\u00020\t2\u0006\u00104\u001a\u00020\tH\u0002J\u0010\u00105\u001a\u00020\t2\u0006\u00104\u001a\u00020\tH\u0002J\u0012\u00106\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0014J(\u00107\u001a\u00020*2\u0006\u00108\u001a\u00020\r2\u0006\u00109\u001a\u00020\r2\u0006\u0010:\u001a\u00020\r2\u0006\u0010;\u001a\u00020\rH\u0014J\u000e\u0010<\u001a\u00020*2\u0006\u0010=\u001a\u00020\rJ\u000e\u0010>\u001a\u00020*2\u0006\u0010?\u001a\u00020\rJ\u000e\u0010@\u001a\u00020*2\u0006\u0010=\u001a\u00020\rJ\u000e\u0010A\u001a\u00020*2\u0006\u0010?\u001a\u00020\rJ\u000e\u0010B\u001a\u00020*2\u0006\u0010=\u001a\u00020\rJ\u000e\u0010C\u001a\u00020*2\u0006\u0010=\u001a\u00020\rJ\u0010\u0010D\u001a\u00020\t2\u0006\u0010E\u001a\u00020\tH\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0013R\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u0015\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\rX\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\rX\u0082D¢\u0006\u0002\n\u0000R\u0012\u0010 \u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0013R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\rX\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\rX\u0082D¢\u0006\u0002\n\u0000¨\u0006F"}, d2 = {"Lcn/xmliu/speedview/SpeedView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "all", "", "", "arcPath", "Landroid/graphics/Path;", "bgColor", "", "bigMarkArr", "bigOffset", "bottom", "", "centerX", "Ljava/lang/Float;", "centerY", "curSpeed", "left", "lineColor", "maxSpeed", "numberArr", "", "numberOffset", "paint", "Landroid/graphics/Paint;", "pointColor", "pointerOffset", "radius", "rectF", "Landroid/graphics/RectF;", "right", "smallMarkArr", "smallOffset", "textColor", "top", "unitOffset", "drawBig", "", "canvas", "Landroid/graphics/Canvas;", "drawCenter", "drawHalf", "drawNumber", "drawPointer", "drawSmall", "drawUnit", "getRoundX", "radian", "getRoundY", "onDraw", "onSizeChanged", "w", "h", "oldw", "oldh", "setBgColor", "color", "setCurSpeed", "speed", "setLineColor", "setMaxSpeed", "setPointerColor", "setTextColor", "toRadians", "degree", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class SpeedView extends View {
    private HashMap _$_findViewCache;
    private final List<Double> all;
    private Path arcPath;
    private int bgColor;
    private final List<Double> bigMarkArr;
    private final int bigOffset;
    private float bottom;
    private Float centerX;
    private Float centerY;
    private int curSpeed;
    private float left;
    private int lineColor;
    private int maxSpeed;
    private final List<String> numberArr;
    private final int numberOffset;
    private final Paint paint;
    private int pointColor;
    private final int pointerOffset;
    private Float radius;
    private RectF rectF;
    private float right;
    private final List<Double> smallMarkArr;
    private final int smallOffset;
    private int textColor;
    private float top;
    private final int unitOffset;

    private final double toRadians(double degree) {
        return (degree / 180.0d) * 3.141592653589793d;
    }

    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View viewFindViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), viewFindViewById);
        return viewFindViewById;
    }

    public SpeedView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.curSpeed = -1;
        this.paint = new Paint();
        this.left = -1.0f;
        this.top = -1.0f;
        this.right = -1.0f;
        this.bottom = -1.0f;
        this.pointerOffset = 20;
        this.unitOffset = GattError.GATT_SERVICE_STARTED;
        this.numberOffset = 70;
        this.smallOffset = 25;
        this.bigOffset = 40;
        this.bigMarkArr = new ArrayList();
        this.all = new ArrayList();
        this.smallMarkArr = new ArrayList();
        this.numberArr = new ArrayList();
        if (context == null) {
            Intrinsics.throwNpe();
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SpeedView);
        Intrinsics.checkExpressionValueIsNotNull(typedArrayObtainStyledAttributes, "context!!.obtainStyledAt…s, R.styleable.SpeedView)");
        int i = 0;
        this.bgColor = typedArrayObtainStyledAttributes.getColor(0, ContextCompat.getColor(context, com.ble.vanomize12.R.color.black));
        this.lineColor = typedArrayObtainStyledAttributes.getColor(1, ContextCompat.getColor(context, com.ble.vanomize12.R.color.white));
        this.textColor = typedArrayObtainStyledAttributes.getColor(4, ContextCompat.getColor(context, com.ble.vanomize12.R.color.white));
        this.pointColor = typedArrayObtainStyledAttributes.getColor(3, ContextCompat.getColor(context, com.ble.vanomize12.R.color.common_red));
        this.maxSpeed = typedArrayObtainStyledAttributes.getInteger(2, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        typedArrayObtainStyledAttributes.recycle();
        this.all.clear();
        for (float f = 130.0f; f <= 410; f += 4) {
            this.all.add(Double.valueOf(f));
            this.numberArr.add(String.valueOf(i));
            i++;
        }
        IntProgression intProgressionStep = RangesKt.step(new IntRange(180, 360), 45);
        int first = intProgressionStep.getFirst();
        int last = intProgressionStep.getLast();
        int step = intProgressionStep.getStep();
        if (step < 0 ? first >= last : first <= last) {
            while (true) {
                this.bigMarkArr.add(Double.valueOf(first));
                if (first == last) {
                    break;
                } else {
                    first += step;
                }
            }
        }
        IntProgression intProgressionStep2 = RangesKt.step(new IntRange(150, 390), 15);
        int first2 = intProgressionStep2.getFirst();
        int last2 = intProgressionStep2.getLast();
        int step2 = intProgressionStep2.getStep();
        if (step2 >= 0) {
            if (first2 > last2) {
                return;
            }
        } else if (first2 < last2) {
            return;
        }
        while (true) {
            this.smallMarkArr.add(Double.valueOf(first2));
            if (first2 == last2) {
                return;
            } else {
                first2 += step2;
            }
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float f = 2;
        this.centerX = Float.valueOf(getWidth() / f);
        this.centerY = Float.valueOf(getHeight() / f);
        this.radius = Float.valueOf((getHeight() / 2) - DateUtil.dip2px(5.0f));
        Float f2 = this.centerX;
        if (f2 == null) {
            Intrinsics.throwNpe();
        }
        float fFloatValue = f2.floatValue();
        Float f3 = this.radius;
        if (f3 == null) {
            Intrinsics.throwNpe();
        }
        this.left = fFloatValue - f3.floatValue();
        Float f4 = this.centerY;
        if (f4 == null) {
            Intrinsics.throwNpe();
        }
        float fFloatValue2 = f4.floatValue();
        Float f5 = this.radius;
        if (f5 == null) {
            Intrinsics.throwNpe();
        }
        this.top = fFloatValue2 - f5.floatValue();
        Float f6 = this.centerX;
        if (f6 == null) {
            Intrinsics.throwNpe();
        }
        float fFloatValue3 = f6.floatValue();
        Float f7 = this.radius;
        if (f7 == null) {
            Intrinsics.throwNpe();
        }
        this.right = fFloatValue3 + f7.floatValue();
        Float f8 = this.centerY;
        if (f8 == null) {
            Intrinsics.throwNpe();
        }
        float fFloatValue4 = f8.floatValue();
        Float f9 = this.radius;
        if (f9 == null) {
            Intrinsics.throwNpe();
        }
        this.bottom = fFloatValue4 + f9.floatValue();
        this.rectF = new RectF(this.left, this.top, this.right, this.bottom);
        this.arcPath = new Path();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {
            canvas.drawColor(this.bgColor);
        }
        drawHalf(canvas);
        drawCenter(canvas);
        drawBig(canvas);
        drawSmall(canvas);
        drawNumber(canvas);
        drawUnit(canvas);
        drawPointer(canvas);
    }

    private final void drawHalf(Canvas canvas) {
        this.paint.setAntiAlias(true);
        this.paint.setColor(this.lineColor);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(10.0f);
        Path path = this.arcPath;
        if (path == null) {
            Intrinsics.throwNpe();
        }
        path.rewind();
        RectF rectF = this.rectF;
        if (rectF != null) {
            Path path2 = this.arcPath;
            if (path2 == null) {
                Intrinsics.throwNpe();
            }
            path2.arcTo(rectF, 120.0f, 300.0f);
        }
        if (canvas != null) {
            Path path3 = this.arcPath;
            if (path3 == null) {
                Intrinsics.throwNpe();
            }
            canvas.drawPath(path3, this.paint);
        }
    }

    private final void drawCenter(Canvas canvas) {
        this.paint.setColor(this.lineColor);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (canvas != null) {
            Float f = this.centerX;
            if (f == null) {
                Intrinsics.throwNpe();
            }
            float fFloatValue = f.floatValue();
            Float f2 = this.centerY;
            if (f2 == null) {
                Intrinsics.throwNpe();
            }
            canvas.drawCircle(fFloatValue, f2.floatValue(), 3.0f, this.paint);
        }
    }

    private final void drawBig(Canvas canvas) {
        this.paint.setColor(this.lineColor);
        this.paint.setStrokeWidth(10.0f);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        IntProgression intProgressionStep = RangesKt.step(new IntRange(0, this.all.size() - 1), 1);
        int first = intProgressionStep.getFirst();
        int last = intProgressionStep.getLast();
        int step = intProgressionStep.getStep();
        if (step >= 0) {
            if (first > last) {
                return;
            }
        } else if (first < last) {
            return;
        }
        while (true) {
            if (first % 5 == 0) {
                double radians = toRadians(this.all.get(first).doubleValue());
                float roundX = (float) getRoundX(radians);
                float roundY = (float) getRoundY(radians);
                float fCos = (float) (roundX - (Math.cos(radians) * this.bigOffset));
                float fSin = (float) (roundY - (Math.sin(radians) * this.bigOffset));
                if (canvas != null) {
                    canvas.drawLine(roundX, roundY, fCos, fSin, this.paint);
                }
            } else {
                double radians2 = toRadians(this.all.get(first).doubleValue());
                float roundX2 = (float) getRoundX(radians2);
                float roundY2 = (float) getRoundY(radians2);
                float fCos2 = (float) (roundX2 - (Math.cos(radians2) * this.smallOffset));
                float fSin2 = (float) (roundY2 - (Math.sin(radians2) * this.smallOffset));
                if (canvas != null) {
                    canvas.drawLine(roundX2, roundY2, fCos2, fSin2, this.paint);
                }
            }
            if (first == last) {
                return;
            } else {
                first += step;
            }
        }
    }

    private final void drawSmall(Canvas canvas) {
        this.paint.setColor(this.lineColor);
        this.paint.setStrokeWidth(5.0f);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private final void drawNumber(Canvas canvas) {
        this.paint.setColor(this.textColor);
        this.paint.setTextSize(25.0f);
        this.paint.setStrokeWidth(5.0f);
        this.paint.setTextSkewX(0.0f);
        this.paint.setStyle(Paint.Style.FILL);
        IntProgression intProgressionStep = RangesKt.step(new IntRange(0, this.numberArr.size() - 1), 1);
        int first = intProgressionStep.getFirst();
        int last = intProgressionStep.getLast();
        int step = intProgressionStep.getStep();
        if (step >= 0) {
            if (first > last) {
                return;
            }
        } else if (first < last) {
            return;
        }
        while (true) {
            if (first % 5 == 0) {
                double radians = toRadians(this.all.get(first).doubleValue());
                double roundX = getRoundX(radians);
                double roundY = getRoundY(radians);
                float fCos = (float) ((roundX - (Math.cos(radians) * this.numberOffset)) - (r9 * 3));
                float fSin = (float) ((roundY - (Math.sin(radians) * this.numberOffset)) + (first / 5));
                if (canvas != null) {
                    canvas.drawText(this.numberArr.get(first), fCos, fSin, this.paint);
                }
                Log.e(LogContract.SessionColumns.NUMBER, "number=" + this.numberArr.get(first) + "all=" + this.all.get(first).doubleValue());
            }
            if (first == last) {
                return;
            } else {
                first += step;
            }
        }
    }

    private final void drawUnit(Canvas canvas) {
        this.paint.setTextSize(20.0f);
        this.paint.setTextSkewX(-0.25f);
        double radians = toRadians(270.0d);
        double roundX = getRoundX(radians);
        double roundY = getRoundY(radians);
        float fCos = (float) ((roundX - (Math.cos(radians) * this.unitOffset)) - 27);
        float fSin = (float) ((roundY - (Math.sin(radians) * this.unitOffset)) + 9);
        if (canvas != null) {
            canvas.drawText("km/h", fCos, fSin, this.paint);
        }
    }

    private final void drawPointer(Canvas canvas) {
        this.paint.setColor(this.pointColor);
        this.paint.setStrokeWidth(8.0f);
        double radians = toRadians(180 + ((180.0d / this.maxSpeed) * this.curSpeed));
        double roundX = getRoundX(radians);
        double roundY = getRoundY(radians);
        float fCos = (float) (roundX - (Math.cos(radians) * this.pointerOffset));
        float fSin = (float) (roundY - (Math.sin(radians) * this.pointerOffset));
        if (canvas != null) {
            Float f = this.centerX;
            if (f == null) {
                Intrinsics.throwNpe();
            }
            float fFloatValue = f.floatValue();
            Float f2 = this.centerY;
            if (f2 == null) {
                Intrinsics.throwNpe();
            }
            canvas.drawLine(fFloatValue, f2.floatValue(), fCos, fSin, this.paint);
        }
    }

    private final double getRoundX(double radian) {
        Float f = this.centerX;
        if (f == null) {
            Intrinsics.throwNpe();
        }
        double dFloatValue = f.floatValue();
        double dCos = Math.cos(radian);
        if (this.radius == null) {
            Intrinsics.throwNpe();
        }
        return dFloatValue + (dCos * r2.floatValue());
    }

    private final double getRoundY(double radian) {
        Float f = this.centerY;
        if (f == null) {
            Intrinsics.throwNpe();
        }
        double dFloatValue = f.floatValue();
        double dSin = Math.sin(radian);
        if (this.radius == null) {
            Intrinsics.throwNpe();
        }
        return dFloatValue + (dSin * r2.floatValue());
    }

    public final void setCurSpeed(int speed) {
        this.curSpeed = speed;
        invalidate();
    }

    public final void setMaxSpeed(int speed) {
        this.maxSpeed = speed;
        invalidate();
    }

    public final void setLineColor(int color) {
        this.lineColor = color;
        invalidate();
    }

    public final void setTextColor(int color) {
        this.textColor = color;
        invalidate();
    }

    public final void setPointerColor(int color) {
        this.pointColor = color;
        invalidate();
    }

    public final void setBgColor(int color) {
        this.bgColor = color;
        invalidate();
    }
}
