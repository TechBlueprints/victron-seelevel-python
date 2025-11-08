package com.jkcq.base.app;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import com.jkcq.util.AppUtil;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;

/* compiled from: BaseApp.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u00062\u00020\u0001:\u0001\u0006B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\u0007"}, d2 = {"Lcom/jkcq/base/app/BaseApp;", "Landroid/app/Application;", "()V", "onCreate", "", "registerNetwork", "Companion", "base_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public class BaseApp extends Application {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final ReadWriteProperty sApplicaton$delegate = Delegates.INSTANCE.notNull();

    /* compiled from: BaseApp.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R+\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00048F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\n\u0010\u000b\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t¨\u0006\f"}, d2 = {"Lcom/jkcq/base/app/BaseApp$Companion;", "", "()V", "<set-?>", "Landroid/app/Application;", "sApplicaton", "getSApplicaton", "()Landroid/app/Application;", "setSApplicaton", "(Landroid/app/Application;)V", "sApplicaton$delegate", "Lkotlin/properties/ReadWriteProperty;", "base_release"}, k = 1, mv = {1, 1, 16})
    public static final class Companion {
        static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Companion.class), "sApplicaton", "getSApplicaton()Landroid/app/Application;"))};

        public final Application getSApplicaton() {
            return (Application) BaseApp.sApplicaton$delegate.getValue(BaseApp.INSTANCE, $$delegatedProperties[0]);
        }

        public final void setSApplicaton(Application application) {
            Intrinsics.checkParameterIsNotNull(application, "<set-?>");
            BaseApp.sApplicaton$delegate.setValue(BaseApp.INSTANCE, $$delegatedProperties[0], application);
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        INSTANCE.setSApplicaton(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleController());
        registerNetwork();
        AppUtil.INSTANCE.init(INSTANCE.getSApplicaton());
    }

    public final void registerNetwork() {
        if (Build.VERSION.SDK_INT >= 24) {
            Object systemService = getSystemService("connectivity");
            if (systemService == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.net.ConnectivityManager");
            }
            ((ConnectivityManager) systemService).registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() { // from class: com.jkcq.base.app.BaseApp.registerNetwork.1
                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onAvailable(Network network) {
                    Intrinsics.checkParameterIsNotNull(network, "network");
                }

                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onLost(Network network) {
                    Intrinsics.checkParameterIsNotNull(network, "network");
                }

                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onUnavailable() {
                    super.onUnavailable();
                }
            });
        }
    }
}
