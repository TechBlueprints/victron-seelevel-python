package com.jkcq.base.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jkcq.base.R;
import com.jkcq.util.StatusBarUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BaseTitleActivity.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\b\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0004H\u0016J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\b\u0010\n\u001a\u00020\u0004H\u0016J\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\tJ\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000e¨\u0006\u000f"}, d2 = {"Lcom/jkcq/base/base/BaseTitleActivity;", "Lcom/jkcq/base/base/BaseActivity;", "()V", "initHeander", "", "ivRight", "setCustomContentView", "setIvRightIcon", "iconRes", "", "setStatusBar", "setTitleText", "titleResId", "title", "", "base_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public abstract class BaseTitleActivity extends BaseActivity {
    private HashMap _$_findViewCache;

    @Override // com.jkcq.base.base.BaseActivity
    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    @Override // com.jkcq.base.base.BaseActivity
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

    public abstract void ivRight();

    @Override // com.jkcq.base.base.BaseActivity
    public void setCustomContentView() {
        BaseTitleActivity baseTitleActivity = this;
        View viewInflate = LayoutInflater.from(baseTitleActivity).inflate(R.layout.activity_base_title_layout, (ViewGroup) null);
        View viewInflate2 = LayoutInflater.from(baseTitleActivity).inflate(getLayoutResId(), (ViewGroup) null);
        View viewFindViewById = viewInflate.findViewById(R.id.fl_real_content);
        if (viewFindViewById == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.widget.FrameLayout");
        }
        FrameLayout frameLayout = (FrameLayout) viewFindViewById;
        frameLayout.removeAllViews();
        frameLayout.addView(viewInflate2);
        setContentView(viewInflate);
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void initHeander() {
        ((ImageView) _$_findCachedViewById(R.id.iv_title_back)).setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.base.base.BaseTitleActivity.initHeander.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BaseTitleActivity.this.finish();
            }
        });
        ((ImageView) _$_findCachedViewById(R.id.iv_right)).setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.base.base.BaseTitleActivity.initHeander.2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BaseTitleActivity.this.ivRight();
            }
        });
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void setStatusBar() throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        BaseTitleActivity baseTitleActivity = this;
        StatusBarUtil.setTransparentForImageView(baseTitleActivity, (RelativeLayout) _$_findCachedViewById(R.id.rl_title));
        StatusBarUtil.setLightMode(baseTitleActivity);
    }

    public final void setTitleText(String title) {
        Intrinsics.checkParameterIsNotNull(title, "title");
        TextView tv_title = (TextView) _$_findCachedViewById(R.id.tv_title);
        Intrinsics.checkExpressionValueIsNotNull(tv_title, "tv_title");
        tv_title.setText(title);
    }

    public final void setTitleText(int titleResId) {
        setTitle(getString(titleResId));
    }

    public final void setIvRightIcon(int iconRes) {
        ImageView iv_right = (ImageView) _$_findCachedViewById(R.id.iv_right);
        Intrinsics.checkExpressionValueIsNotNull(iv_right, "iv_right");
        iv_right.setVisibility(0);
        ((ImageView) _$_findCachedViewById(R.id.iv_right)).setImageResource(iconRes);
    }
}
