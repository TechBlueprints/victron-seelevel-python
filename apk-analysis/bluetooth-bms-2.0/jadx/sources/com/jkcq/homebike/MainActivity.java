package com.jkcq.homebike;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;
import com.jkcq.base.app.BaseApp;
import com.jkcq.base.app.Preference;
import com.jkcq.base.base.BaseVMActivity;
import com.jkcq.base.constants.ConstantLanguages;
import com.jkcq.base.utils.AppLanguageUtils;
import com.jkcq.homebike.BMSCountTimer;
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice;
import com.jkcq.homebike.fragment.adapter.fragmentAdapter;
import com.jkcq.util.StatusBarUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import kotlin.text.StringsKt;

/* compiled from: MainActivity.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\b\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J\u0012\u0010'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010*H\u0014J\u0010\u0010+\u001a\u00020\f2\u0006\u0010,\u001a\u00020*H\u0016J\b\u0010-\u001a\u00020 H\u0016J\b\u0010.\u001a\u00020(H\u0016J\b\u0010/\u001a\u00020(H\u0016J\b\u00100\u001a\u00020(H\u0002J\u000e\u00101\u001a\u00020(2\u0006\u00102\u001a\u00020\fJ\u0010\u00103\u001a\u00020(2\u0006\u00104\u001a\u000205H\u0016J\b\u00106\u001a\u00020(H\u0014J\b\u00107\u001a\u00020(H\u0014J\b\u00108\u001a\u00020(H\u0002J\b\u00109\u001a\u00020(H\u0016J\u000e\u0010:\u001a\u00020(2\u0006\u0010;\u001a\u00020 J\b\u0010<\u001a\u00020(H\u0016R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR+\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\u0016\u001a\u00020\u00178FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u001a\u0010\u001b\u001a\u0004\b\u0018\u0010\u0019R\u001b\u0010\u001c\u001a\u00020\u00028FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u001f\u0010\u001b\u001a\u0004\b\u001d\u0010\u001eR+\u0010!\u001a\u00020 2\u0006\u0010\u000b\u001a\u00020 8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b&\u0010\u0013\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%¨\u0006="}, d2 = {"Lcom/jkcq/homebike/MainActivity;", "Lcom/jkcq/base/base/BaseVMActivity;", "Lcom/jkcq/homebike/BMSDeviceScanViewModel;", "Lcom/jkcq/homebike/BMSCountTimer$OnCountTimerListener;", "()V", "adapter", "Lcom/jkcq/homebike/fragment/adapter/fragmentAdapter;", "getAdapter", "()Lcom/jkcq/homebike/fragment/adapter/fragmentAdapter;", "setAdapter", "(Lcom/jkcq/homebike/fragment/adapter/fragmentAdapter;)V", "<set-?>", "", "clickeLanguge", "getClickeLanguge", "()Ljava/lang/String;", "setClickeLanguge", "(Ljava/lang/String;)V", "clickeLanguge$delegate", "Lcom/jkcq/base/app/Preference;", "countTimer", "Lcom/jkcq/homebike/BMSCountTimer;", "mBMSDeviceConnectViewModel", "Lcom/jkcq/homebike/BMSDeviceConnectViewModel;", "getMBMSDeviceConnectViewModel", "()Lcom/jkcq/homebike/BMSDeviceConnectViewModel;", "mBMSDeviceConnectViewModel$delegate", "Lkotlin/Lazy;", "mBMSDeviceScanViewModel", "getMBMSDeviceScanViewModel", "()Lcom/jkcq/homebike/BMSDeviceScanViewModel;", "mBMSDeviceScanViewModel$delegate", "", "selectType", "getSelectType", "()I", "setSelectType", "(I)V", "selectType$delegate", "attachBaseContext", "", "newBase", "Landroid/content/Context;", "getAppLanguage", "context", "getLayoutResId", "initData", "initView", "initViewPager", "onChangeAppLanguage", "newLanguage", "onCountTimerChanged", "millisecond", "", "onDestroy", "onResume", "restartAct", "setStatusBar", "showLine", "position", "startObserver", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class MainActivity extends BaseVMActivity<BMSDeviceScanViewModel> implements BMSCountTimer.OnCountTimerListener {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(MainActivity.class), "clickeLanguge", "getClickeLanguge()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(MainActivity.class), "selectType", "getSelectType()I"))};
    private HashMap _$_findViewCache;
    private fragmentAdapter adapter;

    /* renamed from: clickeLanguge$delegate, reason: from kotlin metadata */
    private final Preference clickeLanguge = new Preference(Preference.clicklaug, "");

    /* renamed from: selectType$delegate, reason: from kotlin metadata */
    private final Preference selectType = new Preference(Preference.selecttype, 0);

    /* renamed from: mBMSDeviceScanViewModel$delegate, reason: from kotlin metadata */
    private final Lazy mBMSDeviceScanViewModel = LazyKt.lazy(new Function0<BMSDeviceScanViewModel>() { // from class: com.jkcq.homebike.MainActivity$mBMSDeviceScanViewModel$2
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final BMSDeviceScanViewModel invoke() {
            return (BMSDeviceScanViewModel) this.this$0.createViewModel(BMSDeviceScanViewModel.class);
        }
    });

    /* renamed from: mBMSDeviceConnectViewModel$delegate, reason: from kotlin metadata */
    private final Lazy mBMSDeviceConnectViewModel = LazyKt.lazy(new Function0<BMSDeviceConnectViewModel>() { // from class: com.jkcq.homebike.MainActivity$mBMSDeviceConnectViewModel$2
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final BMSDeviceConnectViewModel invoke() {
            return (BMSDeviceConnectViewModel) this.this$0.createViewModel(BMSDeviceConnectViewModel.class);
        }
    });
    private final BMSCountTimer countTimer = new BMSCountTimer(2000, this);

    @Override // com.jkcq.base.base.BaseVMActivity, com.jkcq.base.base.BaseActivity
    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    @Override // com.jkcq.base.base.BaseVMActivity, com.jkcq.base.base.BaseActivity
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

    public final String getClickeLanguge() {
        return (String) this.clickeLanguge.getValue(this, $$delegatedProperties[0]);
    }

    @Override // com.jkcq.base.base.BaseActivity
    public int getLayoutResId() {
        return com.ble.vanomize12.R.layout.activity_main;
    }

    public final BMSDeviceConnectViewModel getMBMSDeviceConnectViewModel() {
        return (BMSDeviceConnectViewModel) this.mBMSDeviceConnectViewModel.getValue();
    }

    public final BMSDeviceScanViewModel getMBMSDeviceScanViewModel() {
        return (BMSDeviceScanViewModel) this.mBMSDeviceScanViewModel.getValue();
    }

    public final int getSelectType() {
        return ((Number) this.selectType.getValue(this, $$delegatedProperties[1])).intValue();
    }

    public final void setClickeLanguge(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.clickeLanguge.setValue(this, $$delegatedProperties[0], str);
    }

    public final void setSelectType(int i) {
        this.selectType.setValue(this, $$delegatedProperties[1], Integer.valueOf(i));
    }

    public final fragmentAdapter getAdapter() {
        return this.adapter;
    }

    public final void setAdapter(fragmentAdapter fragmentadapter) {
        this.adapter = fragmentadapter;
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void initView() {
        getMBMSDeviceConnectViewModel().setCallBack(this);
        this.countTimer.start();
        ((TextView) _$_findCachedViewById(R.id.iv_change_luage)).setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.homebike.MainActivity.initView.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                try {
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                    popupMenu.inflate(com.ble.vanomize12.R.menu.menu_layout);
                    final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    if (StringsKt.equals$default(defaultSharedPreferences.getString(MainActivity.this.getString(com.ble.vanomize12.R.string.app_language_pref_key), ConstantLanguages.ENGLISH), ConstantLanguages.GERMAN, false, 2, null)) {
                        popupMenu.getMenu().findItem(com.ble.vanomize12.R.id.menu_english).setIcon((Drawable) null);
                        popupMenu.getMenu().findItem(com.ble.vanomize12.R.id.menu_france).setIcon(com.ble.vanomize12.R.mipmap.icon_laug_select);
                        popupMenu.getMenu().findItem(com.ble.vanomize12.R.id.menu_english).setIcon(com.ble.vanomize12.R.drawable.shape_bind_scale_bg);
                    } else {
                        popupMenu.getMenu().findItem(com.ble.vanomize12.R.id.menu_english).setIcon(com.ble.vanomize12.R.mipmap.icon_laug_select);
                        popupMenu.getMenu().findItem(com.ble.vanomize12.R.id.menu_france).setIcon(com.ble.vanomize12.R.drawable.shape_bind_scale_bg);
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.jkcq.homebike.MainActivity.initView.1.1
                        @Override // android.widget.PopupMenu.OnMenuItemClickListener
                        public final boolean onMenuItemClick(MenuItem item) {
                            Intrinsics.checkExpressionValueIsNotNull(item, "item");
                            switch (item.getItemId()) {
                                case com.ble.vanomize12.R.id.menu_english /* 2131230924 */:
                                    MainActivity.this.onChangeAppLanguage(ConstantLanguages.ENGLISH);
                                    MainActivity.this.setClickeLanguge("true");
                                    defaultSharedPreferences.edit().putString(MainActivity.this.getString(com.ble.vanomize12.R.string.app_language_pref_key), ConstantLanguages.ENGLISH).commit();
                                    break;
                                case com.ble.vanomize12.R.id.menu_france /* 2131230925 */:
                                    MainActivity.this.setClickeLanguge("true");
                                    MainActivity.this.onChangeAppLanguage(ConstantLanguages.GERMAN);
                                    defaultSharedPreferences.edit().putString(MainActivity.this.getString(com.ble.vanomize12.R.string.app_language_pref_key), ConstantLanguages.GERMAN).commit();
                                    break;
                            }
                            return true;
                        }
                    });
                    try {
                        try {
                            Field fieldMPopup = PopupMenu.class.getDeclaredField("mPopup");
                            Intrinsics.checkExpressionValueIsNotNull(fieldMPopup, "fieldMPopup");
                            fieldMPopup.setAccessible(true);
                            Object obj = fieldMPopup.get(popupMenu);
                            obj.getClass().getDeclaredMethod("setForceShowIcon", Boolean.TYPE).invoke(obj, true);
                        } catch (Exception e) {
                            Log.e("Main", "Error showing menu icons.", e);
                        }
                        popupMenu.show();
                    } catch (Throwable th) {
                        popupMenu.show();
                        throw th;
                    }
                } catch (Exception unused) {
                }
            }
        });
        ((ImageView) _$_findCachedViewById(R.id.iv_seach_device)).setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.homebike.MainActivity.initView.2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, (Class<?>) BMSDeviceScanActivity.class));
            }
        });
        ((ViewPager2) _$_findCachedViewById(R.id.viewpager_rank)).registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // from class: com.jkcq.homebike.MainActivity.initView.3
            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    MainActivity.this.showLine(0);
                    ((RadioGroup) MainActivity.this._$_findCachedViewById(R.id.rg_main)).check(com.ble.vanomize12.R.id.rbtn_setting);
                } else if (position == 1) {
                    MainActivity.this.showLine(1);
                    ((RadioGroup) MainActivity.this._$_findCachedViewById(R.id.rg_main)).check(com.ble.vanomize12.R.id.rbtn_data);
                } else {
                    if (position != 2) {
                        return;
                    }
                    MainActivity.this.showLine(2);
                    ((RadioGroup) MainActivity.this._$_findCachedViewById(R.id.rg_main)).check(com.ble.vanomize12.R.id.rbtn_history);
                }
            }

            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        ((RadioGroup) _$_findCachedViewById(R.id.rg_main)).check(com.ble.vanomize12.R.id.rbtn_setting);
        ((RadioGroup) _$_findCachedViewById(R.id.rg_main)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.jkcq.homebike.MainActivity.initView.4
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case com.ble.vanomize12.R.id.rbtn_data /* 2131230986 */:
                        MainActivity.this.showLine(1);
                        ((ViewPager2) MainActivity.this._$_findCachedViewById(R.id.viewpager_rank)).setCurrentItem(1, false);
                        break;
                    case com.ble.vanomize12.R.id.rbtn_history /* 2131230987 */:
                        MainActivity.this.showLine(2);
                        ((ViewPager2) MainActivity.this._$_findCachedViewById(R.id.viewpager_rank)).setCurrentItem(2, false);
                        break;
                    case com.ble.vanomize12.R.id.rbtn_setting /* 2131230988 */:
                        MainActivity.this.showLine(0);
                        ((ViewPager2) MainActivity.this._$_findCachedViewById(R.id.viewpager_rank)).setCurrentItem(0, false);
                        break;
                }
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context newBase) {
        if (newBase == null) {
            Intrinsics.throwNpe();
        }
        super.attachBaseContext(AppLanguageUtils.attachBaseContext(newBase, getAppLanguage(newBase)));
    }

    public String getAppLanguage(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = defaultSharedPreferences.getString(context.getString(com.ble.vanomize12.R.string.app_language_pref_key), "");
        Log.e("getAppLanguage", "curLanguage=" + string);
        if (TextUtils.isEmpty(string)) {
            Locale locale = Locale.getDefault();
            Iterator<String> it = AppLanguageUtils.mAllLanguages.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String next = it.next();
                if (AppLanguageUtils.mAllLanguages.get(next) != null) {
                    Locale locale2 = AppLanguageUtils.mAllLanguages.get(next);
                    if (locale2 == null) {
                        Intrinsics.throwNpe();
                    }
                    String language = locale2.getLanguage();
                    Intrinsics.checkExpressionValueIsNotNull(locale, "locale");
                    if (TextUtils.equals(language, locale.getLanguage())) {
                        string = locale.getLanguage();
                        break;
                    }
                }
            }
            if (!TextUtils.isEmpty(string)) {
                defaultSharedPreferences.edit().putString(context.getString(com.ble.vanomize12.R.string.app_language_pref_key), string).apply();
            }
        }
        if (TextUtils.isEmpty(string)) {
            return ConstantLanguages.ENGLISH;
        }
        if (string != null) {
            return string;
        }
        Intrinsics.throwNpe();
        return string;
    }

    public final void showLine(int position) {
        setSelectType(position);
        if (position == 0) {
            View view_3 = _$_findCachedViewById(R.id.view_3);
            Intrinsics.checkExpressionValueIsNotNull(view_3, "view_3");
            view_3.setVisibility(4);
            View view_2 = _$_findCachedViewById(R.id.view_2);
            Intrinsics.checkExpressionValueIsNotNull(view_2, "view_2");
            view_2.setVisibility(4);
            View view_1 = _$_findCachedViewById(R.id.view_1);
            Intrinsics.checkExpressionValueIsNotNull(view_1, "view_1");
            view_1.setVisibility(0);
            return;
        }
        if (position == 1) {
            View view_32 = _$_findCachedViewById(R.id.view_3);
            Intrinsics.checkExpressionValueIsNotNull(view_32, "view_3");
            view_32.setVisibility(4);
            View view_22 = _$_findCachedViewById(R.id.view_2);
            Intrinsics.checkExpressionValueIsNotNull(view_22, "view_2");
            view_22.setVisibility(0);
            View view_12 = _$_findCachedViewById(R.id.view_1);
            Intrinsics.checkExpressionValueIsNotNull(view_12, "view_1");
            view_12.setVisibility(4);
            return;
        }
        if (position != 2) {
            return;
        }
        View view_33 = _$_findCachedViewById(R.id.view_3);
        Intrinsics.checkExpressionValueIsNotNull(view_33, "view_3");
        view_33.setVisibility(0);
        View view_23 = _$_findCachedViewById(R.id.view_2);
        Intrinsics.checkExpressionValueIsNotNull(view_23, "view_2");
        view_23.setVisibility(4);
        View view_13 = _$_findCachedViewById(R.id.view_1);
        Intrinsics.checkExpressionValueIsNotNull(view_13, "view_1");
        view_13.setVisibility(4);
    }

    @Override // com.jkcq.base.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(getMBikeName())) {
            TextView tv_name = (TextView) _$_findCachedViewById(R.id.tv_name);
            Intrinsics.checkExpressionValueIsNotNull(tv_name, "tv_name");
            tv_name.setText(getMBikeName());
            if (BMSConfig.BikeConnState == BMSConfig.BIKE_CONN_SUCCESS) {
                ((ImageView) _$_findCachedViewById(R.id.iv_seach_device)).setImageResource(com.ble.vanomize12.R.mipmap.bms_lianjie);
                return;
            } else {
                ((ImageView) _$_findCachedViewById(R.id.iv_seach_device)).setImageResource(com.ble.vanomize12.R.mipmap.bms_duankai);
                getMBMSDeviceScanViewModel().startLeScan();
                return;
            }
        }
        TextView tv_name2 = (TextView) _$_findCachedViewById(R.id.tv_name);
        Intrinsics.checkExpressionValueIsNotNull(tv_name2, "tv_name");
        tv_name2.setText("Bluetooth");
        ((ImageView) _$_findCachedViewById(R.id.iv_seach_device)).setImageResource(com.ble.vanomize12.R.mipmap.bms_duankai);
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void initData() {
        Constant.INSTANCE.setMBMSContext(this);
        initViewPager();
    }

    private final void initViewPager() {
        this.adapter = new fragmentAdapter(this);
        ViewPager2 viewpager_rank = (ViewPager2) _$_findCachedViewById(R.id.viewpager_rank);
        Intrinsics.checkExpressionValueIsNotNull(viewpager_rank, "viewpager_rank");
        viewpager_rank.setAdapter(this.adapter);
        if (!TextUtils.isEmpty(getClickeLanguge())) {
            setClickeLanguge("");
            showLine(getSelectType());
            ((ViewPager2) _$_findCachedViewById(R.id.viewpager_rank)).setCurrentItem(getSelectType(), false);
            return;
        }
        showLine(0);
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void setStatusBar() throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        MainActivity mainActivity = this;
        StatusBarUtil.setTransparentForImageView(mainActivity, (RelativeLayout) _$_findCachedViewById(R.id.layout_top));
        StatusBarUtil.setLightMode(mainActivity);
    }

    public final void onChangeAppLanguage(String newLanguage) {
        Intrinsics.checkParameterIsNotNull(newLanguage, "newLanguage");
        AppLanguageUtils.changeAppLanguage(BaseApp.INSTANCE.getSApplicaton(), newLanguage);
        restartAct();
    }

    private final void restartAct() {
        finish();
        startActivity(new Intent(this, (Class<?>) MainActivity.class));
        overridePendingTransition(0, 0);
    }

    @Override // com.jkcq.base.base.BaseVMActivity
    public void startObserver() {
        MainActivity mainActivity = this;
        getMBMSDeviceConnectViewModel().getMDeviceConnState().observe(mainActivity, new Observer<Integer>() { // from class: com.jkcq.homebike.MainActivity.startObserver.1
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Integer num) {
                int i = BMSConfig.BIKE_CONN_SUCCESS;
                if (num != null && num.intValue() == i) {
                    ((ImageView) MainActivity.this._$_findCachedViewById(R.id.iv_seach_device)).setImageResource(com.ble.vanomize12.R.mipmap.bms_lianjie);
                } else {
                    ((ImageView) MainActivity.this._$_findCachedViewById(R.id.iv_seach_device)).setImageResource(com.ble.vanomize12.R.mipmap.bms_duankai);
                }
            }
        });
        getMBMSDeviceScanViewModel().getMDeviceLiveData().observe(mainActivity, new Observer<List<? extends ExtendedBluetoothDevice>>() { // from class: com.jkcq.homebike.MainActivity.startObserver.2
            @Override // androidx.lifecycle.Observer
            public final void onChanged(List<? extends ExtendedBluetoothDevice> it) {
                Intrinsics.checkExpressionValueIsNotNull(it, "it");
                for (ExtendedBluetoothDevice extendedBluetoothDevice : it) {
                    if (!TextUtils.isEmpty(extendedBluetoothDevice.name) && extendedBluetoothDevice.name.equals(MainActivity.this.getMBikeName())) {
                        MainActivity.this.getMBMSDeviceScanViewModel().stopLeScan();
                        BMSDeviceConnectViewModel mBMSDeviceConnectViewModel = MainActivity.this.getMBMSDeviceConnectViewModel();
                        BluetoothDevice bluetoothDevice = extendedBluetoothDevice.device;
                        Intrinsics.checkExpressionValueIsNotNull(bluetoothDevice, "it.device");
                        mBMSDeviceConnectViewModel.conectBikeDevice(bluetoothDevice);
                        return;
                    }
                }
            }
        });
    }

    @Override // com.jkcq.base.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.countTimer.stop();
    }

    @Override // com.jkcq.homebike.BMSCountTimer.OnCountTimerListener
    public void onCountTimerChanged(long millisecond) {
        getMBMSDeviceConnectViewModel().sendQuitData();
    }
}
