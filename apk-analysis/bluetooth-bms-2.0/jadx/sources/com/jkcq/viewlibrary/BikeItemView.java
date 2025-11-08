package com.jkcq.viewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import no.nordicsemi.android.log.LogContract;

/* compiled from: BikeItemView.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B%\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\u0011\u001a\u00020\nJ\u0006\u0010\u0012\u001a\u00020\nJ\"\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\b\u0010\u0015\u001a\u00020\u0014H\u0002J\b\u0010\u0016\u001a\u00020\u0014H\u0002J\b\u0010\u0017\u001a\u00020\u0014H\u0014J\u000e\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\nJ\u000e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\nR\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lcom/jkcq/viewlibrary/BikeItemView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "mLeftText", "", "mLeftTextColor", "mLeftTextSize", "", "mRightText", "mRightTextColor", "mRightTextSize", "getLeftText", "getRightText", "init", "", "initData", "initView", "onFinishInflate", "setLeftText", LogContract.Session.Content.CONTENT, "setRightText", "viewlibrary_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class BikeItemView extends LinearLayout {
    private HashMap _$_findViewCache;
    private String mLeftText;
    private int mLeftTextColor;
    private float mLeftTextSize;
    private String mRightText;
    private int mRightTextColor;
    private float mRightTextSize;

    public BikeItemView(Context context) {
        this(context, null, 0, 6, null);
    }

    public BikeItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
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

    public /* synthetic */ BikeItemView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? (AttributeSet) null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BikeItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkParameterIsNotNull(context, "context");
        init(context, attributeSet, i);
    }

    private final void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ItemView, defStyleAttr, 0);
        Intrinsics.checkExpressionValueIsNotNull(typedArrayObtainStyledAttributes, "context.obtainStyledAttr…defStyleAttr, 0\n        )");
        this.mLeftText = typedArrayObtainStyledAttributes.getString(R.styleable.ItemView_itemLeftText);
        this.mLeftTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.ItemView_itemLeftTextColor, context.getResources().getColor(R.color.common_page_color1));
        this.mLeftTextSize = typedArrayObtainStyledAttributes.getDimension(R.styleable.ItemView_itemLeftTextSize, context.getResources().getDimension(R.dimen.sp16));
        this.mRightText = typedArrayObtainStyledAttributes.getString(R.styleable.ItemView_itemRightText);
        this.mRightTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.ItemView_itemRightTextColor, context.getResources().getColor(R.color.common_page_color1));
        this.mRightTextSize = typedArrayObtainStyledAttributes.getDimension(R.styleable.ItemView_itemRightTextSize, context.getResources().getDimension(R.dimen.sp16));
        typedArrayObtainStyledAttributes.recycle();
        initView();
    }

    private final void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bike_itemview, (ViewGroup) this, true);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
    }

    private final void initData() {
        TextView textView = (TextView) _$_findCachedViewById(R.id.tv_left);
        textView.setText(this.mLeftText);
        textView.setTextColor(this.mLeftTextColor);
        textView.setTextSize(0, this.mLeftTextSize);
        TextView textView2 = (TextView) _$_findCachedViewById(R.id.tv_right);
        textView2.setText(this.mRightText);
        textView2.setTextColor(this.mRightTextColor);
        textView2.setTextSize(0, this.mRightTextSize);
    }

    public final void setLeftText(String content) {
        Intrinsics.checkParameterIsNotNull(content, "content");
        this.mLeftText = content;
        TextView tv_left = (TextView) _$_findCachedViewById(R.id.tv_left);
        Intrinsics.checkExpressionValueIsNotNull(tv_left, "tv_left");
        tv_left.setText(content);
    }

    public final void setRightText(String content) {
        Intrinsics.checkParameterIsNotNull(content, "content");
        this.mRightText = content;
        TextView tv_right = (TextView) _$_findCachedViewById(R.id.tv_right);
        Intrinsics.checkExpressionValueIsNotNull(tv_right, "tv_right");
        tv_right.setText(content);
    }

    public final String getLeftText() {
        String str = this.mLeftText;
        return str != null ? str : "";
    }

    public final String getRightText() {
        String str = this.mRightText;
        return str != null ? str : "";
    }
}
