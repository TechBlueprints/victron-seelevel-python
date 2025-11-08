package com.jkcq.homebike.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ble.vanomize12.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jkcq.base.base.BaseVMFragment;
import com.jkcq.homebike.BMSDataBean;
import com.jkcq.homebike.fragment.adapter.CellItemAdapter;
import com.jkcq.homebike.observable.BMSClearDataObservable;
import com.jkcq.homebike.observable.BMSRealDataObservable;
import com.jkcq.util.DateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ThreeFragment.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\u0018\u0000 02\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u00010B\u0005¢\u0006\u0002\u0010\u0004J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u001eH\u0016J\u0006\u0010\u001f\u001a\u00020\u001eJ\u0006\u0010 \u001a\u00020\u001eJ\b\u0010!\u001a\u00020\u001eH\u0016J\u0012\u0010\"\u001a\u00020\u001e2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u0010%\u001a\u00020\u001eH\u0016J\u0010\u0010&\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020(H\u0016J\b\u0010)\u001a\u00020\u001eH\u0016J\b\u0010*\u001a\u00020\u001eH\u0016J\u001c\u0010+\u001a\u00020\u001e2\b\u0010,\u001a\u0004\u0018\u00010-2\b\u0010.\u001a\u0004\u0018\u00010/H\u0016R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R \u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u0019X\u0082\u000e¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"Lcom/jkcq/homebike/fragment/ThreeFragment;", "Lcom/jkcq/base/base/BaseVMFragment;", "Lcom/jkcq/homebike/fragment/UserModel;", "Ljava/util/Observer;", "()V", "bikeSportDetailAdapter", "Lcom/jkcq/homebike/fragment/adapter/CellItemAdapter;", "getBikeSportDetailAdapter", "()Lcom/jkcq/homebike/fragment/adapter/CellItemAdapter;", "setBikeSportDetailAdapter", "(Lcom/jkcq/homebike/fragment/adapter/CellItemAdapter;)V", "hand", "Landroid/os/Handler;", "getHand", "()Landroid/os/Handler;", "setHand", "(Landroid/os/Handler;)V", "mDetialBean", "", "Lcom/jkcq/homebike/fragment/CellBean;", "getMDetialBean", "()Ljava/util/List;", "setMDetialBean", "(Ljava/util/List;)V", "param1", "", "param2", "getLayoutResId", "", "initData", "", "initEvent", "initSportDetailRec", "initView", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onHiddenChanged", "hidden", "", "onResume", "startObserver", "update", "o", "Ljava/util/Observable;", "arg", "", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class ThreeFragment extends BaseVMFragment<UserModel> implements Observer {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private HashMap _$_findViewCache;
    public CellItemAdapter bikeSportDetailAdapter;
    private String param1;
    private String param2;
    private List<CellBean> mDetialBean = new ArrayList();
    private Handler hand = new Handler();

    @JvmStatic
    public static final ThreeFragment newInstance(String str, String str2) {
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
        return R.layout.fragment_three;
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

    public final List<CellBean> getMDetialBean() {
        return this.mDetialBean;
    }

    public final void setMDetialBean(List<CellBean> list) {
        Intrinsics.checkParameterIsNotNull(list, "<set-?>");
        this.mDetialBean = list;
    }

    public final CellItemAdapter getBikeSportDetailAdapter() {
        CellItemAdapter cellItemAdapter = this.bikeSportDetailAdapter;
        if (cellItemAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bikeSportDetailAdapter");
        }
        return cellItemAdapter;
    }

    public final void setBikeSportDetailAdapter(CellItemAdapter cellItemAdapter) {
        Intrinsics.checkParameterIsNotNull(cellItemAdapter, "<set-?>");
        this.bikeSportDetailAdapter = cellItemAdapter;
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("onHiddenChanged", "onHiddenChanged" + hidden);
    }

    public final void initSportDetailRec() {
        RecyclerView recyclerview_sport = (RecyclerView) _$_findCachedViewById(com.jkcq.homebike.R.id.recyclerview_sport);
        Intrinsics.checkExpressionValueIsNotNull(recyclerview_sport, "recyclerview_sport");
        recyclerview_sport.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        for (int i = 0; i < 16; i++) {
            this.mDetialBean.add(new CellBean("Cell" + i, "0.000V"));
        }
        this.bikeSportDetailAdapter = new CellItemAdapter(this.mDetialBean);
        RecyclerView recyclerview_sport2 = (RecyclerView) _$_findCachedViewById(com.jkcq.homebike.R.id.recyclerview_sport);
        Intrinsics.checkExpressionValueIsNotNull(recyclerview_sport2, "recyclerview_sport");
        CellItemAdapter cellItemAdapter = this.bikeSportDetailAdapter;
        if (cellItemAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bikeSportDetailAdapter");
        }
        recyclerview_sport2.setAdapter(cellItemAdapter);
        CellItemAdapter cellItemAdapter2 = this.bikeSportDetailAdapter;
        if (cellItemAdapter2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bikeSportDetailAdapter");
        }
        cellItemAdapter2.setOnItemClickListener(new OnItemClickListener() { // from class: com.jkcq.homebike.fragment.ThreeFragment.initSportDetailRec.1
            @Override // com.chad.library.adapter.base.listener.OnItemClickListener
            public final void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int i2) {
                Intrinsics.checkParameterIsNotNull(adapter, "adapter");
                Intrinsics.checkParameterIsNotNull(view, "view");
            }
        });
    }

    /* compiled from: ThreeFragment.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007¨\u0006\b"}, d2 = {"Lcom/jkcq/homebike/fragment/ThreeFragment$Companion;", "", "()V", "newInstance", "Lcom/jkcq/homebike/fragment/ThreeFragment;", "param1", "", "param2", "app_release"}, k = 1, mv = {1, 1, 16})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final ThreeFragment newInstance(String param1, String param2) {
            Intrinsics.checkParameterIsNotNull(param1, "param1");
            Intrinsics.checkParameterIsNotNull(param2, "param2");
            ThreeFragment threeFragment = new ThreeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("param1", param1);
            bundle.putString("param2", param2);
            threeFragment.setArguments(bundle);
            return threeFragment;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThreeFragment threeFragment = this;
        BMSRealDataObservable.getInstance().addObserver(threeFragment);
        BMSClearDataObservable.getInstance().addObserver(threeFragment);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.param1 = arguments.getString("param1");
            this.param2 = arguments.getString("param2");
        }
    }

    @Override // com.jkcq.base.base.BaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        ThreeFragment threeFragment = this;
        BMSRealDataObservable.getInstance().deleteObserver(threeFragment);
        BMSClearDataObservable.getInstance().deleteObserver(threeFragment);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.jkcq.base.base.BaseFragment
    public void initData() {
        initEvent();
        initSportDetailRec();
        TextView tv_time = (TextView) _$_findCachedViewById(com.jkcq.homebike.R.id.tv_time);
        Intrinsics.checkExpressionValueIsNotNull(tv_time, "tv_time");
        tv_time.setText(DateUtil.getYYMMDDHHmmss(System.currentTimeMillis()));
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
            this.hand.post(new Runnable() { // from class: com.jkcq.homebike.fragment.ThreeFragment.update.1
                @Override // java.lang.Runnable
                public final void run() {
                    Observable observable = o;
                    if (observable instanceof BMSRealDataObservable) {
                        if (arg instanceof BMSDataBean) {
                            for (int i = 0; i < ((BMSDataBean) arg).getvArray().size(); i++) {
                                if (ThreeFragment.this.getMDetialBean().size() > i) {
                                    ThreeFragment.this.getMDetialBean().get(i).value = "" + DateUtil.formatThreeoint(((BMSDataBean) arg).getvArray().get(i).intValue()) + ExifInterface.GPS_MEASUREMENT_INTERRUPTED;
                                }
                            }
                            TextView tv_time = (TextView) ThreeFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_time);
                            Intrinsics.checkExpressionValueIsNotNull(tv_time, "tv_time");
                            tv_time.setText(DateUtil.getYYMMDDHHmmss(System.currentTimeMillis()));
                            if (((BMSDataBean) arg).getAlarm()) {
                                TextView textView = (TextView) ThreeFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_state);
                                FragmentActivity activity = ThreeFragment.this.getActivity();
                                if (activity == null) {
                                    Intrinsics.throwNpe();
                                }
                                Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
                                textView.setTextColor(activity.getResources().getColor(R.color.red_value));
                            } else {
                                TextView textView2 = (TextView) ThreeFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_state);
                                FragmentActivity activity2 = ThreeFragment.this.getActivity();
                                if (activity2 == null) {
                                    Intrinsics.throwNpe();
                                }
                                Intrinsics.checkExpressionValueIsNotNull(activity2, "activity!!");
                                textView2.setTextColor(activity2.getResources().getColor(R.color.common_page_color));
                            }
                            TextView tv_state = (TextView) ThreeFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_state);
                            Intrinsics.checkExpressionValueIsNotNull(tv_state, "tv_state");
                            tv_state.setText(((BMSDataBean) arg).getWrokStateDetail());
                            ThreeFragment.this.getBikeSportDetailAdapter().notifyDataSetChanged();
                            return;
                        }
                        return;
                    }
                    if (observable instanceof BMSClearDataObservable) {
                        ThreeFragment.this.getMDetialBean().clear();
                        ThreeFragment.this.initSportDetailRec();
                        TextView tv_time2 = (TextView) ThreeFragment.this._$_findCachedViewById(com.jkcq.homebike.R.id.tv_time);
                        Intrinsics.checkExpressionValueIsNotNull(tv_time2, "tv_time");
                        tv_time2.setText(DateUtil.getYYMMDDHHmmss(System.currentTimeMillis()));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
