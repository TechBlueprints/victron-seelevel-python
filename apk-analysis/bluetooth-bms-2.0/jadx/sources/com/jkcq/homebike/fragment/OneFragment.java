package com.jkcq.homebike.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import com.ble.vanomize12.R;
import com.jkcq.base.base.BaseVMFragment;
import com.jkcq.homebike.BMSDataBean;
import com.jkcq.homebike.observable.BMSClearDataObservable;
import com.jkcq.homebike.observable.BMSRealDataObservable;
import com.jkcq.homebike.view.CirclebarAnimatorView;
import com.jkcq.util.DateUtil;
import com.jkcq.util.StatusBarUtil;
import com.jkcq.viewlibrary.BikeItemView;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: OneFragment.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\u0018\u0000 #2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001#B\u0005¢\u0006\u0002\u0010\u0004J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0006\u0010\u0012\u001a\u00020\u0011J\b\u0010\u0013\u001a\u00020\u0011H\u0016J\u0012\u0010\u0014\u001a\u00020\u00112\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0011H\u0016J\u0010\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0011H\u0016J\b\u0010\u001c\u001a\u00020\u0011H\u0016J\b\u0010\u001d\u001a\u00020\u0011H\u0016J\u001c\u0010\u001e\u001a\u00020\u00112\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lcom/jkcq/homebike/fragment/OneFragment;", "Lcom/jkcq/base/base/BaseVMFragment;", "Lcom/jkcq/homebike/fragment/UserModel;", "Ljava/util/Observer;", "()V", "hand", "Landroid/os/Handler;", "getHand", "()Landroid/os/Handler;", "setHand", "(Landroid/os/Handler;)V", "param1", "", "param2", "getLayoutResId", "", "initData", "", "initEvent", "initView", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onHiddenChanged", "hidden", "", "onResume", "setStatusBar", "startObserver", "update", "o", "Ljava/util/Observable;", "arg", "", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class OneFragment extends BaseVMFragment<UserModel> implements Observer {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private HashMap _$_findViewCache;
    private Handler hand = new Handler();
    private String param1;
    private String param2;

    @JvmStatic
    public static final OneFragment newInstance(String str, String str2) {
        return INSTANCE.newInstance(str, str2);
    }

    @Override // com.jkcq.base.base.BaseVMFragment, com.jkcq.base.base.BaseFragment
    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    @Override // com.jkcq.base.base.BaseVMFragment, com.jkcq.base.base.BaseFragment
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

    @Override // com.jkcq.base.base.BaseFragment
    public int getLayoutResId() {
        return R.layout.fragment_one;
    }

    public final void initEvent() {
    }

    @Override // com.jkcq.base.base.BaseFragment
    public void initView() {
    }

    @Override // com.jkcq.base.base.BaseVMFragment, com.jkcq.base.base.BaseFragment, androidx.fragment.app.Fragment
    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    @Override // com.jkcq.base.base.BaseVMFragment
    public void startObserver() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("onHiddenChanged", "onHiddenChanged" + hidden);
    }

    /* compiled from: OneFragment.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007¨\u0006\b"}, d2 = {"Lcom/jkcq/homebike/fragment/OneFragment$Companion;", "", "()V", "newInstance", "Lcom/jkcq/homebike/fragment/OneFragment;", "param1", "", "param2", "app_release"}, k = 1, mv = {1, 1, 16})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final OneFragment newInstance(String param1, String param2) {
            Intrinsics.checkParameterIsNotNull(param1, "param1");
            Intrinsics.checkParameterIsNotNull(param2, "param2");
            OneFragment oneFragment = new OneFragment();
            Bundle bundle = new Bundle();
            bundle.putString("param1", param1);
            bundle.putString("param2", param2);
            oneFragment.setArguments(bundle);
            return oneFragment;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneFragment oneFragment = this;
        BMSRealDataObservable.getInstance().addObserver(oneFragment);
        BMSClearDataObservable.getInstance().addObserver(oneFragment);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.param1 = arguments.getString("param1");
            this.param2 = arguments.getString("param2");
        }
    }

    @Override // com.jkcq.base.base.BaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        OneFragment oneFragment = this;
        BMSRealDataObservable.getInstance().deleteObserver(oneFragment);
        BMSClearDataObservable.getInstance().deleteObserver(oneFragment);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.jkcq.base.base.BaseFragment
    public void initData() {
        initEvent();
    }

    @Override // com.jkcq.base.base.BaseFragment
    public void setStatusBar() throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        StatusBarUtil.setLightMode(getActivity());
    }

    public final Handler getHand() {
        return this.hand;
    }

    public final void setHand(Handler handler) {
        Intrinsics.checkParameterIsNotNull(handler, "<set-?>");
        this.hand = handler;
    }

    @Override // java.util.Observer
    public void update(final Observable o, final Object arg) {
        try {
            this.hand.post(new Runnable() { // from class: com.jkcq.homebike.fragment.OneFragment.update.1
                @Override // java.lang.Runnable
                public final void run() {
                    Observable observable = o;
                    if (observable instanceof BMSRealDataObservable) {
                        if (arg instanceof BMSDataBean) {
                            TextView tv_pre = (TextView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_pre);
                            Intrinsics.checkExpressionValueIsNotNull(tv_pre, "tv_pre");
                            tv_pre.setText("" + ((BMSDataBean) arg).getSoc() + "%");
                            CirclebarAnimatorView progress_circular = (CirclebarAnimatorView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.progress_circular);
                            Intrinsics.checkExpressionValueIsNotNull(progress_circular, "progress_circular");
                            progress_circular.setProgress((float) ((BMSDataBean) arg).getSoc());
                            ((BikeItemView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.item_voltage)).setRightText(DateUtil.formatThreeoint(((BMSDataBean) arg).getVoltage()) + ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
                            ((BikeItemView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.item_capacity)).setRightText("" + ((BMSDataBean) arg).getCapacity() + "Ah");
                            ((BikeItemView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.item_status)).setRightText("" + ((BMSDataBean) arg).getStatus());
                            ((BikeItemView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.item_cycle_life)).setRightText("" + ((BMSDataBean) arg).getTimes());
                            return;
                        }
                        return;
                    }
                    if (observable instanceof BMSClearDataObservable) {
                        TextView tv_pre2 = (TextView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_pre);
                        Intrinsics.checkExpressionValueIsNotNull(tv_pre2, "tv_pre");
                        tv_pre2.setText("0%");
                        CirclebarAnimatorView progress_circular2 = (CirclebarAnimatorView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.progress_circular);
                        Intrinsics.checkExpressionValueIsNotNull(progress_circular2, "progress_circular");
                        progress_circular2.setProgress(0);
                        ((BikeItemView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.item_voltage)).setRightText("0.000V");
                        ((BikeItemView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.item_capacity)).setRightText("0.00Ah");
                        ((BikeItemView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.item_status)).setRightText("Standby");
                        ((BikeItemView) OneFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.item_health)).setRightText("0");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
