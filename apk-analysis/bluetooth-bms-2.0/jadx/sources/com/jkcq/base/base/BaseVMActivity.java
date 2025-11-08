package com.jkcq.base.base;

import android.os.Bundle;
import android.view.View;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jkcq.base.app.BaseApp;
import com.jkcq.base.base.BaseViewModel;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BaseVMActivity.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J%\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0001\u0010\u0006*\u00020\u00022\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00060\bH\u0004¢\u0006\u0002\u0010\tJ\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0014J\b\u0010\u000e\u001a\u00020\u000bH&¨\u0006\u000f"}, d2 = {"Lcom/jkcq/base/base/BaseVMActivity;", "VM", "Lcom/jkcq/base/base/BaseViewModel;", "Lcom/jkcq/base/base/BaseActivity;", "()V", "createViewModel", ExifInterface.GPS_DIRECTION_TRUE, "vmClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Lcom/jkcq/base/base/BaseViewModel;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "startObserver", "base_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public abstract class BaseVMActivity<VM extends BaseViewModel> extends BaseActivity {
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

    public abstract void startObserver();

    @Override // com.jkcq.base.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super.onCreate(savedInstanceState);
        startObserver();
    }

    protected final <T extends BaseViewModel> T createViewModel(Class<T> vmClass) {
        Intrinsics.checkParameterIsNotNull(vmClass, "vmClass");
        ViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(BaseApp.INSTANCE.getSApplicaton())).get(vmClass);
        Intrinsics.checkExpressionValueIsNotNull(viewModel, "ViewModelProvider(\n     …n)\n        ).get(vmClass)");
        return (T) viewModel;
    }
}
