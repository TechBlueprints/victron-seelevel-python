package com.jkcq.homebike.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;
import com.ble.vanomize12.R;
import com.jkcq.base.app.Preference;
import com.jkcq.base.base.BaseVMFragment;
import com.jkcq.homebike.BMSDataBean;
import com.jkcq.homebike.observable.BMSClearDataObservable;
import com.jkcq.homebike.observable.BMSRealDataObservable;
import com.jkcq.homebike.view.NoiseboardView;
import com.jkcq.homebike.view.NoiseboardView2;
import com.jkcq.homebike.view.TempView;
import com.jkcq.util.DateUtil;
import com.jkcq.util.StatusBarUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;

/* compiled from: TwoFragment.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\u0018\u0000 02\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u00010B\u0005¢\u0006\u0002\u0010\u0004J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u001eH\u0016J\u0006\u0010\u001f\u001a\u00020\u001eJ\b\u0010 \u001a\u00020\u001eH\u0016J\u0012\u0010!\u001a\u00020\u001e2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010$\u001a\u00020\u001eH\u0016J\u0010\u0010%\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020'H\u0016J\b\u0010(\u001a\u00020\u001eH\u0016J\b\u0010)\u001a\u00020\u001eH\u0016J\b\u0010*\u001a\u00020\u001eH\u0016J\u001c\u0010+\u001a\u00020\u001e2\b\u0010,\u001a\u0004\u0018\u00010-2\b\u0010.\u001a\u0004\u0018\u00010/H\u0016R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR+\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001b\u0010\u0014\u001a\u00020\u00028FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0015\u0010\u0016R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"Lcom/jkcq/homebike/fragment/TwoFragment;", "Lcom/jkcq/base/base/BaseVMFragment;", "Lcom/jkcq/homebike/fragment/UserModel;", "Ljava/util/Observer;", "()V", "hand", "Landroid/os/Handler;", "getHand", "()Landroid/os/Handler;", "setHand", "(Landroid/os/Handler;)V", "<set-?>", "", "mBikeName", "getMBikeName", "()Ljava/lang/String;", "setMBikeName", "(Ljava/lang/String;)V", "mBikeName$delegate", "Lcom/jkcq/base/app/Preference;", "mUserModel", "getMUserModel", "()Lcom/jkcq/homebike/fragment/UserModel;", "mUserModel$delegate", "Lkotlin/Lazy;", "param1", "param2", "getLayoutResId", "", "initData", "", "initEvent", "initView", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onHiddenChanged", "hidden", "", "onResume", "setStatusBar", "startObserver", "update", "o", "Ljava/util/Observable;", "arg", "", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class TwoFragment extends BaseVMFragment<UserModel> implements Observer {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(TwoFragment.class), "mBikeName", "getMBikeName()Ljava/lang/String;"))};

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private HashMap _$_findViewCache;
    private String param1;
    private String param2;

    /* renamed from: mBikeName$delegate, reason: from kotlin metadata */
    private final Preference mBikeName = new Preference(Preference.BIKENAME, "");

    /* renamed from: mUserModel$delegate, reason: from kotlin metadata */
    private final Lazy mUserModel = LazyKt.lazy(new Function0<UserModel>() { // from class: com.jkcq.homebike.fragment.TwoFragment$mUserModel$2
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final UserModel invoke() {
            return (UserModel) this.this$0.createViewModel(UserModel.class);
        }
    });
    private Handler hand = new Handler();

    @JvmStatic
    public static final TwoFragment newInstance(String str, String str2) {
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
        return R.layout.fragment_two;
    }

    public final String getMBikeName() {
        return (String) this.mBikeName.getValue(this, $$delegatedProperties[0]);
    }

    public final UserModel getMUserModel() {
        return (UserModel) this.mUserModel.getValue();
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

    public final void setMBikeName(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mBikeName.setValue(this, $$delegatedProperties[0], str);
    }

    @Override // com.jkcq.base.base.BaseVMFragment
    public void startObserver() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("onHiddenChanged", "onHiddenChanged" + hidden);
    }

    /* compiled from: TwoFragment.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007¨\u0006\b"}, d2 = {"Lcom/jkcq/homebike/fragment/TwoFragment$Companion;", "", "()V", "newInstance", "Lcom/jkcq/homebike/fragment/TwoFragment;", "param1", "", "param2", "app_release"}, k = 1, mv = {1, 1, 16})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final TwoFragment newInstance(String param1, String param2) {
            Intrinsics.checkParameterIsNotNull(param1, "param1");
            Intrinsics.checkParameterIsNotNull(param2, "param2");
            TwoFragment twoFragment = new TwoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("param1", param1);
            bundle.putString("param2", param2);
            twoFragment.setArguments(bundle);
            return twoFragment;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.param1 = arguments.getString("param1");
            this.param2 = arguments.getString("param2");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.jkcq.base.base.BaseFragment
    public void initData() {
        TwoFragment twoFragment = this;
        BMSRealDataObservable.getInstance().addObserver(twoFragment);
        BMSClearDataObservable.getInstance().addObserver(twoFragment);
        TextView tv_voltage = (TextView) _$_findCachedViewById(com.jkcq.homebike.R.id.tv_voltage);
        Intrinsics.checkExpressionValueIsNotNull(tv_voltage, "tv_voltage");
        StringBuilder sb = new StringBuilder();
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
        sb.append(activity.getResources().getString(R.string.voltage));
        sb.append("0.000V");
        tv_voltage.setText(sb.toString());
        TextView tv_current = (TextView) _$_findCachedViewById(com.jkcq.homebike.R.id.tv_current);
        Intrinsics.checkExpressionValueIsNotNull(tv_current, "tv_current");
        StringBuilder sb2 = new StringBuilder();
        FragmentActivity activity2 = getActivity();
        if (activity2 == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(activity2, "activity!!");
        sb2.append(activity2.getResources().getString(R.string.current));
        sb2.append("0.00A");
        tv_current.setText(sb2.toString());
        String interger = DateUtil.formatInterger(32);
        TextView tv_temp_value = (TextView) _$_findCachedViewById(com.jkcq.homebike.R.id.tv_temp_value);
        Intrinsics.checkExpressionValueIsNotNull(tv_temp_value, "tv_temp_value");
        StringBuilder sb3 = new StringBuilder();
        FragmentActivity activity3 = getActivity();
        if (activity3 == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(activity3, "activity!!");
        sb3.append(activity3.getResources().getString(R.string.temperature));
        sb3.append(0);
        sb3.append("℃/");
        sb3.append(interger);
        sb3.append("℉");
        tv_temp_value.setText(sb3.toString());
        initEvent();
    }

    @Override // com.jkcq.base.base.BaseFragment
    public void setStatusBar() throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        StatusBarUtil.setLightMode(getActivity());
    }

    @Override // com.jkcq.base.base.BaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        TwoFragment twoFragment = this;
        BMSRealDataObservable.getInstance().deleteObserver(twoFragment);
        BMSClearDataObservable.getInstance().deleteObserver(twoFragment);
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
            this.hand.post(new Runnable() { // from class: com.jkcq.homebike.fragment.TwoFragment.update.1
                @Override // java.lang.Runnable
                public final void run() {
                    Observable observable = o;
                    if (observable instanceof BMSRealDataObservable) {
                        if (arg instanceof BMSDataBean) {
                            String calvoltage = DateUtil.formatThreeoint(((BMSDataBean) r1).getVoltage());
                            String calCurrent = DateUtil.formatTwoPoint(((BMSDataBean) arg).getElectric());
                            TextView tv_voltage = (TextView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_voltage);
                            Intrinsics.checkExpressionValueIsNotNull(tv_voltage, "tv_voltage");
                            StringBuilder sb = new StringBuilder();
                            FragmentActivity activity = TwoFragment.this.getActivity();
                            if (activity == null) {
                                Intrinsics.throwNpe();
                            }
                            Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
                            sb.append(activity.getResources().getString(R.string.voltage));
                            sb.append(calvoltage);
                            sb.append(ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
                            tv_voltage.setText(sb.toString());
                            TextView tv_current = (TextView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_current);
                            Intrinsics.checkExpressionValueIsNotNull(tv_current, "tv_current");
                            StringBuilder sb2 = new StringBuilder();
                            FragmentActivity activity2 = TwoFragment.this.getActivity();
                            if (activity2 == null) {
                                Intrinsics.throwNpe();
                            }
                            Intrinsics.checkExpressionValueIsNotNull(activity2, "activity!!");
                            sb2.append(activity2.getResources().getString(R.string.current));
                            sb2.append(calCurrent);
                            sb2.append(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS);
                            tv_current.setText(sb2.toString());
                            String interger = DateUtil.formatInterger(((((BMSDataBean) arg).getTem() * 9) / 5) + 32);
                            TextView tv_temp_value = (TextView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_temp_value);
                            Intrinsics.checkExpressionValueIsNotNull(tv_temp_value, "tv_temp_value");
                            StringBuilder sb3 = new StringBuilder();
                            FragmentActivity activity3 = TwoFragment.this.getActivity();
                            if (activity3 == null) {
                                Intrinsics.throwNpe();
                            }
                            Intrinsics.checkExpressionValueIsNotNull(activity3, "activity!!");
                            sb3.append(activity3.getResources().getString(R.string.temperature));
                            sb3.append(((BMSDataBean) arg).getTem());
                            sb3.append("℃/");
                            sb3.append(interger);
                            sb3.append("℉");
                            tv_temp_value.setText(sb3.toString());
                            ((TempView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tempView)).setCurrentTemp(((BMSDataBean) arg).getTem());
                            NoiseboardView noiseboardView = (NoiseboardView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.noiseboardView);
                            Intrinsics.checkExpressionValueIsNotNull(noiseboardView, "noiseboardView");
                            Intrinsics.checkExpressionValueIsNotNull(calvoltage, "calvoltage");
                            noiseboardView.setRealTimeValue(Float.parseFloat(calvoltage));
                            NoiseboardView2 speedView2 = (NoiseboardView2) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.speedView2);
                            Intrinsics.checkExpressionValueIsNotNull(speedView2, "speedView2");
                            Intrinsics.checkExpressionValueIsNotNull(calCurrent, "calCurrent");
                            speedView2.setRealTimeValue(Float.parseFloat(calCurrent));
                            return;
                        }
                        return;
                    }
                    if (observable instanceof BMSClearDataObservable) {
                        String calvoltage2 = DateUtil.formatThreeoint(0.0d);
                        String calCurrent2 = DateUtil.formatTwoPoint(0.0d);
                        TextView tv_voltage2 = (TextView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_voltage);
                        Intrinsics.checkExpressionValueIsNotNull(tv_voltage2, "tv_voltage");
                        StringBuilder sb4 = new StringBuilder();
                        FragmentActivity activity4 = TwoFragment.this.getActivity();
                        if (activity4 == null) {
                            Intrinsics.throwNpe();
                        }
                        Intrinsics.checkExpressionValueIsNotNull(activity4, "activity!!");
                        sb4.append(activity4.getResources().getString(R.string.voltage));
                        sb4.append(calvoltage2);
                        sb4.append(ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
                        tv_voltage2.setText(sb4.toString());
                        TextView tv_current2 = (TextView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_current);
                        Intrinsics.checkExpressionValueIsNotNull(tv_current2, "tv_current");
                        StringBuilder sb5 = new StringBuilder();
                        FragmentActivity activity5 = TwoFragment.this.getActivity();
                        if (activity5 == null) {
                            Intrinsics.throwNpe();
                        }
                        Intrinsics.checkExpressionValueIsNotNull(activity5, "activity!!");
                        sb5.append(activity5.getResources().getString(R.string.current));
                        sb5.append(calCurrent2);
                        sb5.append(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS);
                        tv_current2.setText(sb5.toString());
                        String interger2 = DateUtil.formatInterger(32);
                        TextView tv_temp_value2 = (TextView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_temp_value);
                        Intrinsics.checkExpressionValueIsNotNull(tv_temp_value2, "tv_temp_value");
                        StringBuilder sb6 = new StringBuilder();
                        FragmentActivity activity6 = TwoFragment.this.getActivity();
                        if (activity6 == null) {
                            Intrinsics.throwNpe();
                        }
                        Intrinsics.checkExpressionValueIsNotNull(activity6, "activity!!");
                        sb6.append(activity6.getResources().getString(R.string.temperature));
                        sb6.append(0);
                        sb6.append("℃/");
                        sb6.append(interger2);
                        sb6.append("℉");
                        tv_temp_value2.setText(sb6.toString());
                        ((TempView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tempView)).setCurrentTemp(0.0f);
                        NoiseboardView noiseboardView2 = (NoiseboardView) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.noiseboardView);
                        Intrinsics.checkExpressionValueIsNotNull(noiseboardView2, "noiseboardView");
                        Intrinsics.checkExpressionValueIsNotNull(calvoltage2, "calvoltage");
                        noiseboardView2.setRealTimeValue(Float.parseFloat(calvoltage2));
                        NoiseboardView2 speedView22 = (NoiseboardView2) TwoFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.speedView2);
                        Intrinsics.checkExpressionValueIsNotNull(speedView22, "speedView2");
                        Intrinsics.checkExpressionValueIsNotNull(calCurrent2, "calCurrent");
                        speedView22.setRealTimeValue(Float.parseFloat(calCurrent2));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
