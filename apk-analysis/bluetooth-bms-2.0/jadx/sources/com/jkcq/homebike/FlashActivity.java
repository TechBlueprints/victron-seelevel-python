package com.jkcq.homebike;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.jkcq.base.base.BaseActivity;
import com.jkcq.util.StatusBarUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: FlashActivity.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016J\u0012\u0010\f\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0014J\b\u0010\u000f\u001a\u00020\nH\u0016J\u0006\u0010\u0010\u001a\u00020\nR\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lcom/jkcq/homebike/FlashActivity;", "Lcom/jkcq/base/base/BaseActivity;", "()V", "BMS_DELAYED_TIME", "", "getBMS_DELAYED_TIME", "()J", "getLayoutResId", "", "initData", "", "initView", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setStatusBar", "toMain", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class FlashActivity extends BaseActivity {
    private final long BMS_DELAYED_TIME = 1000;
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

    @Override // com.jkcq.base.base.BaseActivity
    public int getLayoutResId() {
        return com.ble.vanomize12.R.layout.activity_flash;
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void initData() {
    }

    public final long getBMS_DELAYED_TIME() {
        return this.BMS_DELAYED_TIME;
    }

    @Override // com.jkcq.base.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super.onCreate(savedInstanceState);
        if (isTaskRoot()) {
            return;
        }
        finish();
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void initView() {
        if (Build.VERSION.SDK_INT >= 19) {
            Window window = getWindow();
            Intrinsics.checkExpressionValueIsNotNull(window, "window");
            View decorView = window.getDecorView();
            Intrinsics.checkExpressionValueIsNotNull(decorView, "window.decorView");
            decorView.setSystemUiVisibility(5894);
        } else {
            Window window2 = getWindow();
            Intrinsics.checkExpressionValueIsNotNull(window2, "window");
            View decorView2 = window2.getDecorView();
            Intrinsics.checkExpressionValueIsNotNull(decorView2, "window.decorView");
            decorView2.setSystemUiVisibility(4);
        }
        new Handler().postDelayed(new Runnable() { // from class: com.jkcq.homebike.FlashActivity.initView.1
            @Override // java.lang.Runnable
            public final void run() {
                FlashActivity.this.toMain();
            }
        }, this.BMS_DELAYED_TIME);
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void setStatusBar() throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        FlashActivity flashActivity = this;
        StatusBarUtil.setTransparentForImageView(flashActivity, (ImageView) _$_findCachedViewById(R.id.iv_calender));
        StatusBarUtil.setLightMode(flashActivity);
    }

    public final void toMain() {
        startActivity(new Intent(this, (Class<?>) MainActivity.class));
        finish();
    }
}
