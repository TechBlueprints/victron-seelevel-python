package jp.wasabeef.glide.transformations.internal;

/* loaded from: classes.dex */
public class RSBlur {
    /* JADX WARN: Removed duplicated region for block: B:29:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x007b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.graphics.Bitmap blur(android.content.Context r6, android.graphics.Bitmap r7, int r8) throws java.lang.Throwable {
        /*
            r0 = 23
            r1 = 0
            android.renderscript.RenderScript r6 = android.renderscript.RenderScript.create(r6)     // Catch: java.lang.Throwable -> L5e
            android.renderscript.RenderScript$RSMessageHandler r2 = new android.renderscript.RenderScript$RSMessageHandler     // Catch: java.lang.Throwable -> L58
            r2.<init>()     // Catch: java.lang.Throwable -> L58
            r6.setMessageHandler(r2)     // Catch: java.lang.Throwable -> L58
            android.renderscript.Allocation$MipmapControl r2 = android.renderscript.Allocation.MipmapControl.MIPMAP_NONE     // Catch: java.lang.Throwable -> L58
            r3 = 1
            android.renderscript.Allocation r2 = android.renderscript.Allocation.createFromBitmap(r6, r7, r2, r3)     // Catch: java.lang.Throwable -> L58
            android.renderscript.Type r3 = r2.getType()     // Catch: java.lang.Throwable -> L55
            android.renderscript.Allocation r3 = android.renderscript.Allocation.createTyped(r6, r3)     // Catch: java.lang.Throwable -> L55
            android.renderscript.Element r4 = android.renderscript.Element.U8_4(r6)     // Catch: java.lang.Throwable -> L50
            android.renderscript.ScriptIntrinsicBlur r1 = android.renderscript.ScriptIntrinsicBlur.create(r6, r4)     // Catch: java.lang.Throwable -> L50
            r1.setInput(r2)     // Catch: java.lang.Throwable -> L50
            float r8 = (float) r8     // Catch: java.lang.Throwable -> L50
            r1.setRadius(r8)     // Catch: java.lang.Throwable -> L50
            r1.forEach(r3)     // Catch: java.lang.Throwable -> L50
            r3.copyTo(r7)     // Catch: java.lang.Throwable -> L50
            if (r6 == 0) goto L40
            int r8 = android.os.Build.VERSION.SDK_INT
            if (r8 < r0) goto L3d
            android.renderscript.RenderScript.releaseAllContexts()
            goto L40
        L3d:
            r6.destroy()
        L40:
            if (r2 == 0) goto L45
            r2.destroy()
        L45:
            if (r3 == 0) goto L4a
            r3.destroy()
        L4a:
            if (r1 == 0) goto L4f
            r1.destroy()
        L4f:
            return r7
        L50:
            r7 = move-exception
            r5 = r1
            r1 = r6
            r6 = r5
            goto L62
        L55:
            r7 = move-exception
            r3 = r1
            goto L5b
        L58:
            r7 = move-exception
            r2 = r1
            r3 = r2
        L5b:
            r1 = r6
            r6 = r3
            goto L62
        L5e:
            r7 = move-exception
            r6 = r1
            r2 = r6
            r3 = r2
        L62:
            if (r1 == 0) goto L6f
            int r8 = android.os.Build.VERSION.SDK_INT
            if (r8 < r0) goto L6c
            android.renderscript.RenderScript.releaseAllContexts()
            goto L6f
        L6c:
            r1.destroy()
        L6f:
            if (r2 == 0) goto L74
            r2.destroy()
        L74:
            if (r3 == 0) goto L79
            r3.destroy()
        L79:
            if (r6 == 0) goto L7e
            r6.destroy()
        L7e:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: jp.wasabeef.glide.transformations.internal.RSBlur.blur(android.content.Context, android.graphics.Bitmap, int):android.graphics.Bitmap");
    }
}
