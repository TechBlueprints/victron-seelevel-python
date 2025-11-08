package com.jkcq.base.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.utillibrary.PermissionUtil;
import com.jkcq.base.app.ActivityLifecycleController;
import com.jkcq.base.app.Preference;
import com.jkcq.util.LoginOutDialog;
import com.jkcq.util.OnButtonListener;
import com.jkcq.util.StatusBarUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;

/* compiled from: BaseActivity.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0014\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\b&\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u001f\u001a\u00020 H&J\b\u0010!\u001a\u00020\"H\u0016J\b\u0010#\u001a\u00020\"H&J\b\u0010$\u001a\u00020\"H\u0016J\b\u0010%\u001a\u00020\"H\u0016J\b\u0010&\u001a\u00020\"H&J\u0012\u0010'\u001a\u00020\"2\b\u0010(\u001a\u0004\u0018\u00010)H\u0014J\b\u0010*\u001a\u00020\"H\u0014J\b\u0010+\u001a\u00020\"H\u0014J-\u0010,\u001a\u00020\"2\u0006\u0010-\u001a\u00020 2\u000e\u0010.\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000b0/2\u0006\u00100\u001a\u000201H\u0016¢\u0006\u0002\u00102J\b\u00103\u001a\u00020\"H\u0014J\b\u00104\u001a\u00020\"H\u0016J\b\u00105\u001a\u00020\"H\u0016J\b\u00106\u001a\u00020\"H\u0016J\u001c\u00107\u001a\u00020\"2\b\u00108\u001a\u0004\u0018\u0001092\b\u0010:\u001a\u0004\u0018\u00010;H\u0016R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR+\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R+\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u0016\u0010\u0012\u001a\u0004\b\u0014\u0010\u000e\"\u0004\b\u0015\u0010\u0010R+\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u001a\u0010\u0012\u001a\u0004\b\u0018\u0010\u000e\"\u0004\b\u0019\u0010\u0010R+\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u001e\u0010\u0012\u001a\u0004\b\u001c\u0010\u000e\"\u0004\b\u001d\u0010\u0010¨\u0006<"}, d2 = {"Lcom/jkcq/base/base/BaseActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Ljava/util/Observer;", "()V", "loginOutDialog", "Lcom/jkcq/util/LoginOutDialog;", "getLoginOutDialog", "()Lcom/jkcq/util/LoginOutDialog;", "setLoginOutDialog", "(Lcom/jkcq/util/LoginOutDialog;)V", "<set-?>", "", "mBikeMac", "getMBikeMac", "()Ljava/lang/String;", "setMBikeMac", "(Ljava/lang/String;)V", "mBikeMac$delegate", "Lcom/jkcq/base/app/Preference;", "mBikeName", "getMBikeName", "setMBikeName", "mBikeName$delegate", "mUserId", "getMUserId", "setMUserId", "mUserId$delegate", "mVersion", "getMVersion", "setMVersion", "mVersion$delegate", "getLayoutResId", "", "hideLoginOutDialog", "", "initData", "initEvent", "initHeander", "initView", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onPause", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "setCustomContentView", "setStatusBar", "showLoginOutDialog", "update", "o", "Ljava/util/Observable;", "arg", "", "base_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public abstract class BaseActivity extends AppCompatActivity implements Observer {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(BaseActivity.class), "mUserId", "getMUserId()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(BaseActivity.class), "mBikeName", "getMBikeName()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(BaseActivity.class), "mBikeMac", "getMBikeMac()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(BaseActivity.class), "mVersion", "getMVersion()Ljava/lang/String;"))};
    private HashMap _$_findViewCache;
    private LoginOutDialog loginOutDialog;

    /* renamed from: mUserId$delegate, reason: from kotlin metadata */
    private final Preference mUserId = new Preference(Preference.USER_ID, "");

    /* renamed from: mBikeName$delegate, reason: from kotlin metadata */
    private final Preference mBikeName = new Preference(Preference.BIKENAME, "");

    /* renamed from: mBikeMac$delegate, reason: from kotlin metadata */
    private final Preference mBikeMac = new Preference(Preference.BIKEMAC, "");

    /* renamed from: mVersion$delegate, reason: from kotlin metadata */
    private final Preference mVersion = new Preference(Preference.BIKEVERSION, "");

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

    public abstract int getLayoutResId();

    public final String getMBikeMac() {
        return (String) this.mBikeMac.getValue(this, $$delegatedProperties[2]);
    }

    public final String getMBikeName() {
        return (String) this.mBikeName.getValue(this, $$delegatedProperties[1]);
    }

    public final String getMUserId() {
        return (String) this.mUserId.getValue(this, $$delegatedProperties[0]);
    }

    public final String getMVersion() {
        return (String) this.mVersion.getValue(this, $$delegatedProperties[3]);
    }

    public abstract void initData();

    public void initEvent() {
    }

    public void initHeander() {
    }

    public abstract void initView();

    public final void setMBikeMac(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mBikeMac.setValue(this, $$delegatedProperties[2], str);
    }

    public final void setMBikeName(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mBikeName.setValue(this, $$delegatedProperties[1], str);
    }

    public final void setMUserId(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mUserId.setValue(this, $$delegatedProperties[0], str);
    }

    public final void setMVersion(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mVersion.setValue(this, $$delegatedProperties[3], str);
    }

    @Override // java.util.Observer
    public void update(Observable o, Object arg) {
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super.onCreate(savedInstanceState);
        setCustomContentView();
        initHeander();
        initView();
        initEvent();
        setStatusBar();
        initData();
    }

    public void setCustomContentView() {
        setContentView(getLayoutResId());
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setStatusBar() throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        BaseActivity baseActivity = this;
        StatusBarUtil.setTransparentForImageView(baseActivity, null);
        StatusBarUtil.setLightMode(baseActivity);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Intrinsics.checkParameterIsNotNull(permissions, "permissions");
        Intrinsics.checkParameterIsNotNull(grantResults, "grantResults");
        PermissionUtil.INSTANCE.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public final LoginOutDialog getLoginOutDialog() {
        return this.loginOutDialog;
    }

    public final void setLoginOutDialog(LoginOutDialog loginOutDialog) {
        this.loginOutDialog = loginOutDialog;
    }

    public void hideLoginOutDialog() {
        LoginOutDialog loginOutDialog;
        LoginOutDialog loginOutDialog2 = this.loginOutDialog;
        if (loginOutDialog2 != null) {
            if (loginOutDialog2 == null) {
                Intrinsics.throwNpe();
            }
            if (!loginOutDialog2.isShowing() || (loginOutDialog = this.loginOutDialog) == null) {
                return;
            }
            loginOutDialog.dismiss();
        }
    }

    public void showLoginOutDialog() {
        Log.e("show", "show------updatemsg.what=" + this.loginOutDialog);
        if (this.loginOutDialog == null) {
            this.loginOutDialog = new LoginOutDialog(this);
        }
        LoginOutDialog loginOutDialog = this.loginOutDialog;
        if (loginOutDialog == null) {
            Intrinsics.throwNpe();
        }
        if (loginOutDialog.isShowing()) {
            return;
        }
        LoginOutDialog loginOutDialog2 = this.loginOutDialog;
        if (loginOutDialog2 != null) {
            loginOutDialog2.setBtnOnclick(new OnButtonListener() { // from class: com.jkcq.base.base.BaseActivity.showLoginOutDialog.1
                @Override // com.jkcq.util.OnButtonListener
                public void onCancleOnclick() {
                }

                @Override // com.jkcq.util.OnButtonListener
                public void onSureOnclick() {
                    BaseActivity.this.setMUserId("");
                    BaseActivity.this.setMBikeMac("");
                    BaseActivity.this.setMBikeName("");
                    ARouter.getInstance().build("/app/LoginActivity").navigation();
                    ActivityLifecycleController.finishAllActivity("LoginActivity");
                }
            });
        }
        LoginOutDialog loginOutDialog3 = this.loginOutDialog;
        if (loginOutDialog3 != null) {
            loginOutDialog3.show();
        }
    }
}
