package com.jkcq.base.base;

import android.os.Bundle;
import android.view.View;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jkcq.base.app.BaseApp;
import com.jkcq.base.app.Preference;
import com.jkcq.base.base.BaseViewModel;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;

/* compiled from: BaseVMFragment.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J%\u0010\u0012\u001a\u0002H\u0013\"\b\b\u0001\u0010\u0013*\u00020\u00022\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00130\u0015H\u0004¢\u0006\u0002\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0018H&R+\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\f\u0010\r\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR+\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u0011\u0010\r\u001a\u0004\b\u000f\u0010\t\"\u0004\b\u0010\u0010\u000b¨\u0006\u001e"}, d2 = {"Lcom/jkcq/base/base/BaseVMFragment;", "VM", "Lcom/jkcq/base/base/BaseViewModel;", "Lcom/jkcq/base/base/BaseFragment;", "()V", "<set-?>", "", "mUserId", "getMUserId", "()Ljava/lang/String;", "setMUserId", "(Ljava/lang/String;)V", "mUserId$delegate", "Lcom/jkcq/base/app/Preference;", "mUserName", "getMUserName", "setMUserName", "mUserName$delegate", "createViewModel", ExifInterface.GPS_DIRECTION_TRUE, "vmClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Lcom/jkcq/base/base/BaseViewModel;", "onViewCreated", "", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "startObserver", "base_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public abstract class BaseVMFragment<VM extends BaseViewModel> extends BaseFragment {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(BaseVMFragment.class), "mUserId", "getMUserId()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(BaseVMFragment.class), "mUserName", "getMUserName()Ljava/lang/String;"))};
    private HashMap _$_findViewCache;

    /* renamed from: mUserId$delegate, reason: from kotlin metadata */
    private final Preference mUserId = new Preference(Preference.USER_ID, "");

    /* renamed from: mUserName$delegate, reason: from kotlin metadata */
    private final Preference mUserName = new Preference(Preference.USER_NAME, "");

    @Override // com.jkcq.base.base.BaseFragment
    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    @Override // com.jkcq.base.base.BaseFragment
    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View view2 = getView();
        if (view2 == null) {
            return null;
        }
        View viewFindViewById = view2.findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), viewFindViewById);
        return viewFindViewById;
    }

    public final String getMUserId() {
        return (String) this.mUserId.getValue(this, $$delegatedProperties[0]);
    }

    public final String getMUserName() {
        return (String) this.mUserName.getValue(this, $$delegatedProperties[1]);
    }

    @Override // com.jkcq.base.base.BaseFragment, androidx.fragment.app.Fragment
    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public final void setMUserId(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mUserId.setValue(this, $$delegatedProperties[0], str);
    }

    public final void setMUserName(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mUserName.setValue(this, $$delegatedProperties[1], str);
    }

    public abstract void startObserver();

    @Override // com.jkcq.base.base.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, savedInstanceState);
        startObserver();
    }

    protected final <T extends BaseViewModel> T createViewModel(Class<T> vmClass) {
        Intrinsics.checkParameterIsNotNull(vmClass, "vmClass");
        ViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(BaseApp.INSTANCE.getSApplicaton())).get(vmClass);
        Intrinsics.checkExpressionValueIsNotNull(viewModel, "ViewModelProvider(\n     …n)\n        ).get(vmClass)");
        return (T) viewModel;
    }
}
