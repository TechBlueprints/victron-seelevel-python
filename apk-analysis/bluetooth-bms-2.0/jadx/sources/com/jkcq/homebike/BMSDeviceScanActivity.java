package com.jkcq.homebike;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.utillibrary.PermissionUtil;
import com.jkcq.base.app.BaseApp;
import com.jkcq.base.base.BaseVMActivity;
import com.jkcq.base.constants.ConstantLanguages;
import com.jkcq.base.utils.AppLanguageUtils;
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice;
import com.jkcq.util.AppUtil;
import com.jkcq.util.LocationUtils;
import com.jkcq.util.LogUtil;
import com.jkcq.util.OnButtonListener;
import com.jkcq.util.YesOrNoDialog;
import com.jkcq.util.ktx.ToastUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: BMSDeviceScanActivity.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0006\u0010.\u001a\u00020/J\u0012\u00100\u001a\u00020/2\b\u00101\u001a\u0004\u0018\u000102H\u0014J\u0010\u00103\u001a\u0002042\u0006\u00105\u001a\u000202H\u0016J\b\u00106\u001a\u000207H\u0016J\b\u00108\u001a\u00020/H\u0016J\b\u00109\u001a\u00020/H\u0016J\u0006\u0010:\u001a\u00020/J\u0006\u0010;\u001a\u00020/J\b\u0010<\u001a\u00020/H\u0016J\b\u0010=\u001a\u00020/H\u0014J\b\u0010>\u001a\u00020/H\u0014J\b\u0010?\u001a\u00020/H\u0014J\u0006\u0010@\u001a\u00020/J\u0006\u0010A\u001a\u00020/J\u0006\u0010B\u001a\u00020/J\b\u0010C\u001a\u00020/H\u0016J\b\u0010D\u001a\u00020/H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001b\u0010\u0017\u001a\u00020\u00188FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001d\u001a\u00020\u001eX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001b\u0010#\u001a\u00020\u00028FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b&\u0010\u001c\u001a\u0004\b$\u0010%R \u0010'\u001a\b\u0012\u0004\u0012\u00020)0(X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-¨\u0006E"}, d2 = {"Lcom/jkcq/homebike/BMSDeviceScanActivity;", "Lcom/jkcq/base/base/BaseVMActivity;", "Lcom/jkcq/homebike/BMSDeviceScanViewModel;", "()V", "broadcastReceiver", "Landroid/content/BroadcastReceiver;", "dialog", "Lcom/jkcq/util/YesOrNoDialog;", "getDialog", "()Lcom/jkcq/util/YesOrNoDialog;", "setDialog", "(Lcom/jkcq/util/YesOrNoDialog;)V", "filter", "Landroid/content/IntentFilter;", "getFilter", "()Landroid/content/IntentFilter;", "setFilter", "(Landroid/content/IntentFilter;)V", "isFirstComming", "", "()Z", "setFirstComming", "(Z)V", "mBMSDeviceConnectViewModel", "Lcom/jkcq/homebike/BMSDeviceConnectViewModel;", "getMBMSDeviceConnectViewModel", "()Lcom/jkcq/homebike/BMSDeviceConnectViewModel;", "mBMSDeviceConnectViewModel$delegate", "Lkotlin/Lazy;", "mBMSDeviceListAdapter", "Lcom/jkcq/homebike/BMSDeviceListAdapter;", "getMBMSDeviceListAdapter", "()Lcom/jkcq/homebike/BMSDeviceListAdapter;", "setMBMSDeviceListAdapter", "(Lcom/jkcq/homebike/BMSDeviceListAdapter;)V", "mBMSDeviceScanViewModel", "getMBMSDeviceScanViewModel", "()Lcom/jkcq/homebike/BMSDeviceScanViewModel;", "mBMSDeviceScanViewModel$delegate", "mDataList", "", "Lcom/jkcq/homebike/ble/scanner/ExtendedBluetoothDevice;", "getMDataList", "()Ljava/util/List;", "setMDataList", "(Ljava/util/List;)V", "addConnectDevice", "", "attachBaseContext", "newBase", "Landroid/content/Context;", "getAppLanguage", "", "context", "getLayoutResId", "", "initData", "initEvent", "initPermission", "initSportDetailRec", "initView", "onPause", "onResume", "onStart", "openBlueDialog", "openLocationDialog", "showDisDilog", "startObserver", "startScan", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class BMSDeviceScanActivity extends BaseVMActivity<BMSDeviceScanViewModel> {
    private HashMap _$_findViewCache;
    private YesOrNoDialog dialog;
    private IntentFilter filter;
    public BMSDeviceListAdapter mBMSDeviceListAdapter;

    /* renamed from: mBMSDeviceScanViewModel$delegate, reason: from kotlin metadata */
    private final Lazy mBMSDeviceScanViewModel = LazyKt.lazy(new Function0<BMSDeviceScanViewModel>() { // from class: com.jkcq.homebike.BMSDeviceScanActivity$mBMSDeviceScanViewModel$2
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
    private final Lazy mBMSDeviceConnectViewModel = LazyKt.lazy(new Function0<BMSDeviceConnectViewModel>() { // from class: com.jkcq.homebike.BMSDeviceScanActivity$mBMSDeviceConnectViewModel$2
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final BMSDeviceConnectViewModel invoke() {
            return (BMSDeviceConnectViewModel) this.this$0.createViewModel(BMSDeviceConnectViewModel.class);
        }
    });
    private List<ExtendedBluetoothDevice> mDataList = new ArrayList();
    private boolean isFirstComming = true;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.jkcq.homebike.BMSDeviceScanActivity$broadcastReceiver$1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(intent, "intent");
            if (Intrinsics.areEqual(intent.getAction(), "android.bluetooth.adapter.action.STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
                Log.e("BleService", "ACTION_STATE_CHANGED" + intExtra + 12);
                if (intExtra == 12) {
                    this.this$0.startScan();
                }
            }
        }
    };

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

    @Override // com.jkcq.base.base.BaseActivity
    public int getLayoutResId() {
        return com.ble.vanomize12.R.layout.activity_bike_device_scan;
    }

    public final BMSDeviceConnectViewModel getMBMSDeviceConnectViewModel() {
        return (BMSDeviceConnectViewModel) this.mBMSDeviceConnectViewModel.getValue();
    }

    public final BMSDeviceScanViewModel getMBMSDeviceScanViewModel() {
        return (BMSDeviceScanViewModel) this.mBMSDeviceScanViewModel.getValue();
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void initView() {
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

    public final List<ExtendedBluetoothDevice> getMDataList() {
        return this.mDataList;
    }

    public final void setMDataList(List<ExtendedBluetoothDevice> list) {
        Intrinsics.checkParameterIsNotNull(list, "<set-?>");
        this.mDataList = list;
    }

    public final BMSDeviceListAdapter getMBMSDeviceListAdapter() {
        BMSDeviceListAdapter bMSDeviceListAdapter = this.mBMSDeviceListAdapter;
        if (bMSDeviceListAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBMSDeviceListAdapter");
        }
        return bMSDeviceListAdapter;
    }

    public final void setMBMSDeviceListAdapter(BMSDeviceListAdapter bMSDeviceListAdapter) {
        Intrinsics.checkParameterIsNotNull(bMSDeviceListAdapter, "<set-?>");
        this.mBMSDeviceListAdapter = bMSDeviceListAdapter;
    }

    public final void addConnectDevice() {
        if (BMSConfig.BIKE_CONN_SUCCESS == BMSConfig.BikeConnState) {
            this.mDataList.add(new ExtendedBluetoothDevice(BMSConfig.device, true));
        }
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void initData() {
        BMSConfig.isBikeScanPage = true;
        addConnectDevice();
        IntentFilter intentFilter = new IntentFilter();
        this.filter = intentFilter;
        if (intentFilter == null) {
            Intrinsics.throwNpe();
        }
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        initSportDetailRec();
        initPermission();
        getMBMSDeviceConnectViewModel().setCallBack(this);
    }

    /* renamed from: isFirstComming, reason: from getter */
    public final boolean getIsFirstComming() {
        return this.isFirstComming;
    }

    public final void setFirstComming(boolean z) {
        this.isFirstComming = z;
    }

    @Override // com.jkcq.base.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        try {
            if (AppUtil.INSTANCE.isOpenBle()) {
                unregisterReceiver(this.broadcastReceiver);
            }
        } catch (Exception unused) {
        }
    }

    public final IntentFilter getFilter() {
        return this.filter;
    }

    public final void setFilter(IntentFilter intentFilter) {
        this.filter = intentFilter;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (LocationUtils.isLocationEnabled()) {
                registerReceiver(this.broadcastReceiver, this.filter);
                return;
            }
            return;
        }
        registerReceiver(this.broadcastReceiver, this.filter);
    }

    @Override // com.jkcq.base.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.isFirstComming) {
                if (LocationUtils.isLocationEnabled()) {
                    return;
                }
                openLocationDialog();
                this.isFirstComming = false;
                return;
            }
            if (LocationUtils.isLocationEnabled()) {
                return;
            }
            finish();
        }
    }

    public final void initPermission() {
        if (Build.VERSION.SDK_INT >= 31) {
            PermissionUtil.checkPermission$default(PermissionUtil.INSTANCE, this, new String[]{"android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT"}, new PermissionUtil.OnPermissonCallback() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.initPermission.1
                @Override // com.example.utillibrary.PermissionUtil.OnPermissonCallback
                public void isGrant(boolean grant) {
                    if (grant) {
                        BMSDeviceScanActivity.this.startScan();
                    } else {
                        ToastUtil.showTextToast(BaseApp.INSTANCE.getSApplicaton(), BMSDeviceScanActivity.this.getString(com.ble.vanomize12.R.string.no_required_permission));
                    }
                }
            }, 0, 8, null);
        } else {
            PermissionUtil.checkPermission$default(PermissionUtil.INSTANCE, this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, new PermissionUtil.OnPermissonCallback() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.initPermission.2
                @Override // com.example.utillibrary.PermissionUtil.OnPermissonCallback
                public void isGrant(boolean grant) {
                    if (grant) {
                        BMSDeviceScanActivity.this.startScan();
                    } else {
                        ToastUtil.showTextToast(BaseApp.INSTANCE.getSApplicaton(), BMSDeviceScanActivity.this.getString(com.ble.vanomize12.R.string.no_required_permission));
                    }
                }
            }, 0, 8, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void startScan() {
        if (AppUtil.INSTANCE.isOpenBle()) {
            getMBMSDeviceScanViewModel().startLeScan();
        } else {
            openBlueDialog();
        }
    }

    public final void openLocationDialog() {
        YesOrNoDialog yesOrNoDialog = new YesOrNoDialog(this, "", getResources().getString(com.ble.vanomize12.R.string.app_loaction_server), "", getResources().getString(com.ble.vanomize12.R.string.app_loaction_on));
        yesOrNoDialog.show();
        yesOrNoDialog.setBtnOnclick(new OnButtonListener() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.openLocationDialog.1
            @Override // com.jkcq.util.OnButtonListener
            public void onCancleOnclick() {
            }

            @Override // com.jkcq.util.OnButtonListener
            public void onSureOnclick() {
                LocationUtils.openGpsSettings();
            }
        });
    }

    public final void openBlueDialog() {
        YesOrNoDialog yesOrNoDialog = new YesOrNoDialog(this, "", getResources().getString(com.ble.vanomize12.R.string.app_open_blue_tips), "", getResources().getString(com.ble.vanomize12.R.string.app_open_blue));
        yesOrNoDialog.setBtnOnclick(new OnButtonListener() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.openBlueDialog.1
            @Override // com.jkcq.util.OnButtonListener
            public void onCancleOnclick() {
            }

            @Override // com.jkcq.util.OnButtonListener
            public void onSureOnclick() {
                BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                if (defaultAdapter != null) {
                    defaultAdapter.enable();
                }
            }
        });
        yesOrNoDialog.show();
    }

    public final YesOrNoDialog getDialog() {
        return this.dialog;
    }

    public final void setDialog(YesOrNoDialog yesOrNoDialog) {
        this.dialog = yesOrNoDialog;
    }

    public final void showDisDilog() {
        YesOrNoDialog yesOrNoDialog = this.dialog;
        if (yesOrNoDialog != null) {
            if (yesOrNoDialog == null) {
                Intrinsics.throwNpe();
            }
            if (yesOrNoDialog.isShowing()) {
                return;
            }
        }
        YesOrNoDialog yesOrNoDialog2 = new YesOrNoDialog(this, "", getResources().getString(com.ble.vanomize12.R.string.device_disconnect), getResources().getString(com.ble.vanomize12.R.string.dialog_cancel), getResources().getString(com.ble.vanomize12.R.string.dialog_ok));
        this.dialog = yesOrNoDialog2;
        if (yesOrNoDialog2 == null) {
            Intrinsics.throwNpe();
        }
        yesOrNoDialog2.setBtnOnclick(new OnButtonListener() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.showDisDilog.1
            @Override // com.jkcq.util.OnButtonListener
            public void onCancleOnclick() {
            }

            @Override // com.jkcq.util.OnButtonListener
            public void onSureOnclick() {
                BMSDeviceScanActivity.this.setDialog((YesOrNoDialog) null);
                BMSDeviceScanActivity.this.getMBMSDeviceScanViewModel().stopLeScan();
                BMSDeviceScanActivity.this.getMBMSDeviceConnectViewModel().disconectDevice();
                BMSDeviceScanActivity.this.setMBikeName("");
                BMSDeviceScanActivity.this.setMBikeMac("");
                BMSConfig.device = (BluetoothDevice) null;
                ToastUtil.showTextToast(BaseApp.INSTANCE.getSApplicaton(), BMSDeviceScanActivity.this.getResources().getString(com.ble.vanomize12.R.string.device_unbind_success));
                BMSDeviceScanActivity.this.getMBMSDeviceScanViewModel().startLeScan();
            }
        });
        YesOrNoDialog yesOrNoDialog3 = this.dialog;
        if (yesOrNoDialog3 == null) {
            Intrinsics.throwNpe();
        }
        yesOrNoDialog3.show();
    }

    public final void initSportDetailRec() {
        RecyclerView recycle_device_list = (RecyclerView) _$_findCachedViewById(R.id.recycle_device_list);
        Intrinsics.checkExpressionValueIsNotNull(recycle_device_list, "recycle_device_list");
        recycle_device_list.setLayoutManager(new LinearLayoutManager(this));
        this.mBMSDeviceListAdapter = new BMSDeviceListAdapter(this.mDataList);
        RecyclerView recycle_device_list2 = (RecyclerView) _$_findCachedViewById(R.id.recycle_device_list);
        Intrinsics.checkExpressionValueIsNotNull(recycle_device_list2, "recycle_device_list");
        BMSDeviceListAdapter bMSDeviceListAdapter = this.mBMSDeviceListAdapter;
        if (bMSDeviceListAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBMSDeviceListAdapter");
        }
        recycle_device_list2.setAdapter(bMSDeviceListAdapter);
        BMSDeviceListAdapter bMSDeviceListAdapter2 = this.mBMSDeviceListAdapter;
        if (bMSDeviceListAdapter2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBMSDeviceListAdapter");
        }
        bMSDeviceListAdapter2.setOnItemClickListener(new OnItemClickListener() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.initSportDetailRec.1
            @Override // com.chad.library.adapter.base.listener.OnItemClickListener
            public final void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int i) {
                Intrinsics.checkParameterIsNotNull(adapter, "adapter");
                Intrinsics.checkParameterIsNotNull(view, "view");
                if (BMSDeviceScanActivity.this.getMDataList().get(i).isConnect) {
                    BMSDeviceScanActivity.this.showDisDilog();
                    return;
                }
                BMSDeviceScanActivity.this.getMBMSDeviceScanViewModel().stopLeScan();
                BMSDeviceConnectViewModel mBMSDeviceConnectViewModel = BMSDeviceScanActivity.this.getMBMSDeviceConnectViewModel();
                BluetoothDevice bluetoothDevice = BMSDeviceScanActivity.this.getMDataList().get(i).device;
                Intrinsics.checkExpressionValueIsNotNull(bluetoothDevice, "mDataList.get(position).device");
                mBMSDeviceConnectViewModel.conectBikeDevice(bluetoothDevice);
            }
        });
    }

    @Override // com.jkcq.base.base.BaseActivity
    public void initEvent() {
        super.initEvent();
        ((TextView) _$_findCachedViewById(R.id.tv_scan)).setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.initEvent.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                if (!TextUtils.isEmpty(BMSDeviceScanActivity.this.getMBikeName())) {
                    BMSDeviceScanActivity.this.showDisDilog();
                } else {
                    BMSDeviceScanActivity.this.getMBMSDeviceScanViewModel().stopLeScan();
                    BMSDeviceScanActivity.this.startScan();
                }
            }
        });
        ((TextView) _$_findCachedViewById(R.id.tv_back)).setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.initEvent.2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BMSDeviceScanActivity.this.getMBMSDeviceScanViewModel().stopLeScan();
                BMSDeviceScanActivity.this.finish();
            }
        });
    }

    @Override // com.jkcq.base.base.BaseVMActivity
    public void startObserver() {
        BMSDeviceScanActivity bMSDeviceScanActivity = this;
        getMBMSDeviceConnectViewModel().getMDeviceConnStateTips().observe(bMSDeviceScanActivity, new Observer<String>() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.startObserver.1
            @Override // androidx.lifecycle.Observer
            public final void onChanged(String str) {
                ToastUtil.showTextToast(BMSDeviceScanActivity.this, str);
            }
        });
        getMBMSDeviceConnectViewModel().getMDeviceConnState().observe(bMSDeviceScanActivity, new Observer<Integer>() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.startObserver.2
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Integer num) {
                if (BMSConfig.BikeConnState == BMSConfig.BIKE_CONN_SUCCESS) {
                    BMSDeviceScanActivity.this.finish();
                }
            }
        });
        getMBMSDeviceScanViewModel().getMDeviceLiveData().observe(bMSDeviceScanActivity, new Observer<List<? extends ExtendedBluetoothDevice>>() { // from class: com.jkcq.homebike.BMSDeviceScanActivity.startObserver.3
            @Override // androidx.lifecycle.Observer
            public final void onChanged(List<? extends ExtendedBluetoothDevice> it) {
                LogUtil.e("startLeScan6");
                BMSDeviceScanActivity.this.getMDataList().clear();
                try {
                    BMSDeviceScanActivity.this.addConnectDevice();
                    Intrinsics.checkExpressionValueIsNotNull(it, "it");
                    for (ExtendedBluetoothDevice extendedBluetoothDevice : it) {
                        if (!TextUtils.isEmpty(extendedBluetoothDevice.name)) {
                            String bleName = extendedBluetoothDevice.name;
                            Intrinsics.checkExpressionValueIsNotNull(bleName, "bleName");
                            if (bleName == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            String upperCase = bleName.toUpperCase();
                            Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase()");
                            Log.e("bleUpperCase", upperCase);
                            if (StringsKt.contains$default((CharSequence) upperCase, (CharSequence) "G", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) upperCase, (CharSequence) "M", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) upperCase, (CharSequence) ExifInterface.GPS_MEASUREMENT_INTERRUPTED, false, 2, (Object) null) || StringsKt.contains$default((CharSequence) upperCase, (CharSequence) "C", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) upperCase, (CharSequence) "LIBAT", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) upperCase, (CharSequence) ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, false, 2, (Object) null) || StringsKt.contains$default((CharSequence) upperCase, (CharSequence) "VANOMIZE", false, 2, (Object) null)) {
                                BMSDeviceScanActivity.this.getMDataList().add(extendedBluetoothDevice);
                            }
                        }
                    }
                    BMSDeviceScanActivity.this.getMBMSDeviceListAdapter().notifyDataSetChanged();
                } catch (Exception unused) {
                }
            }
        });
    }
}
