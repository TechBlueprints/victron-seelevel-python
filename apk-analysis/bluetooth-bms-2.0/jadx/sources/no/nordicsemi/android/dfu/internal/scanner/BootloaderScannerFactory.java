package no.nordicsemi.android.dfu.internal.scanner;

import android.os.Build;

/* loaded from: classes.dex */
public class BootloaderScannerFactory {
    public static BootloaderScanner getScanner() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new BootloaderScannerLollipop();
        }
        return new BootloaderScannerJB();
    }
}
