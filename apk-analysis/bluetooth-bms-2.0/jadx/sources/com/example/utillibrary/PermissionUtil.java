package com.example.utillibrary;

import android.app.Activity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PermissionUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\u0015\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001 B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J3\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\u0006\u0010\u0014\u001a\u00020\b2\b\b\u0002\u0010\u0015\u001a\u00020\u0004¢\u0006\u0002\u0010\u0016J'\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00130\u00182\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012¢\u0006\u0002\u0010\u0019J-\u0010\u001a\u001a\u00020\u000e2\b\b\u0002\u0010\u001b\u001a\u00020\u00042\u000e\u0010\u001c\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130\u00122\u0006\u0010\u001d\u001a\u00020\u001e¢\u0006\u0002\u0010\u001fR\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u0006!"}, d2 = {"Lcom/example/utillibrary/PermissionUtil;", "", "()V", "REQUEST_DEFAULT", "", "getREQUEST_DEFAULT", "()I", "mPermissonCallback", "Lcom/example/utillibrary/PermissionUtil$OnPermissonCallback;", "getMPermissonCallback", "()Lcom/example/utillibrary/PermissionUtil$OnPermissonCallback;", "setMPermissonCallback", "(Lcom/example/utillibrary/PermissionUtil$OnPermissonCallback;)V", "checkPermission", "", "activity", "Landroid/app/Activity;", "permissons", "", "", "permissonCallback", "code", "(Landroid/app/Activity;[Ljava/lang/String;Lcom/example/utillibrary/PermissionUtil$OnPermissonCallback;I)V", "findDeniedPermissions", "", "(Landroid/app/Activity;[Ljava/lang/String;)Ljava/util/List;", "onRequestPermissionsResult", "requestCode", "permissions", "grantResults", "", "(I[Ljava/lang/String;[I)V", "OnPermissonCallback", "util_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class PermissionUtil {
    public static final PermissionUtil INSTANCE = new PermissionUtil();
    private static final int REQUEST_DEFAULT = 11;
    private static OnPermissonCallback mPermissonCallback;

    /* compiled from: PermissionUtil.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lcom/example/utillibrary/PermissionUtil$OnPermissonCallback;", "", "isGrant", "", "grant", "", "util_release"}, k = 1, mv = {1, 1, 16})
    public interface OnPermissonCallback {
        void isGrant(boolean grant);
    }

    private PermissionUtil() {
    }

    public final int getREQUEST_DEFAULT() {
        return REQUEST_DEFAULT;
    }

    public final OnPermissonCallback getMPermissonCallback() {
        return mPermissonCallback;
    }

    public final void setMPermissonCallback(OnPermissonCallback onPermissonCallback) {
        mPermissonCallback = onPermissonCallback;
    }

    public static /* synthetic */ void checkPermission$default(PermissionUtil permissionUtil, Activity activity, String[] strArr, OnPermissonCallback onPermissonCallback, int i, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            i = REQUEST_DEFAULT;
        }
        permissionUtil.checkPermission(activity, strArr, onPermissonCallback, i);
    }

    public final void checkPermission(Activity activity, String[] permissons, OnPermissonCallback permissonCallback, int code) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(permissons, "permissons");
        Intrinsics.checkParameterIsNotNull(permissonCallback, "permissonCallback");
        mPermissonCallback = permissonCallback;
        List<String> listFindDeniedPermissions = findDeniedPermissions(activity, permissons);
        if (listFindDeniedPermissions.size() <= 0) {
            OnPermissonCallback onPermissonCallback = mPermissonCallback;
            if (onPermissonCallback != null) {
                onPermissonCallback.isGrant(true);
                return;
            }
            return;
        }
        Object[] array = listFindDeniedPermissions.toArray(new String[0]);
        if (array != null) {
            ActivityCompat.requestPermissions(activity, (String[]) array, code);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    public final List<String> findDeniedPermissions(Activity activity, String[] permissons) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(permissons, "permissons");
        ArrayList arrayList = new ArrayList();
        for (String str : permissons) {
            if (ContextCompat.checkSelfPermission(activity, str) != 0) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public static /* synthetic */ void onRequestPermissionsResult$default(PermissionUtil permissionUtil, int i, String[] strArr, int[] iArr, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = REQUEST_DEFAULT;
        }
        permissionUtil.onRequestPermissionsResult(i, strArr, iArr);
    }

    public final void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Intrinsics.checkParameterIsNotNull(permissions, "permissions");
        Intrinsics.checkParameterIsNotNull(grantResults, "grantResults");
        if (requestCode == REQUEST_DEFAULT) {
            boolean z = true;
            for (int i : grantResults) {
                if (i != 0) {
                    z = false;
                }
            }
            OnPermissonCallback onPermissonCallback = mPermissonCallback;
            if (onPermissonCallback != null) {
                onPermissonCallback.isGrant(z);
            }
        }
    }
}
