package com.jkcq.homebike;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import no.nordicsemi.android.log.LogContract;

/* compiled from: BMSDeviceListAdapter.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0002H\u0014¨\u0006\u000b"}, d2 = {"Lcom/jkcq/homebike/BMSDeviceListAdapter;", "Lcom/chad/library/adapter/base/BaseQuickAdapter;", "Lcom/jkcq/homebike/ble/scanner/ExtendedBluetoothDevice;", "Lcom/chad/library/adapter/base/viewholder/BaseViewHolder;", LogContract.LogColumns.DATA, "", "(Ljava/util/List;)V", "convert", "", "holder", "item", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class BMSDeviceListAdapter extends BaseQuickAdapter<ExtendedBluetoothDevice, BaseViewHolder> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BMSDeviceListAdapter(List<ExtendedBluetoothDevice> data) {
        super(com.ble.vanomize12.R.layout.item_scan_device, data);
        Intrinsics.checkParameterIsNotNull(data, "data");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(BaseViewHolder holder, ExtendedBluetoothDevice item) {
        Intrinsics.checkParameterIsNotNull(holder, "holder");
        Intrinsics.checkParameterIsNotNull(item, "item");
        holder.setText(com.ble.vanomize12.R.id.tv_device_type_name, item.name);
        if (item.isConnect) {
            holder.setVisible(com.ble.vanomize12.R.id.iv_device_state, true);
        } else {
            holder.setVisible(com.ble.vanomize12.R.id.iv_device_state, false);
        }
        if (item.rssi == -1000) {
            holder.setVisible(com.ble.vanomize12.R.id.tv_device_name, false);
            return;
        }
        holder.setVisible(com.ble.vanomize12.R.id.tv_device_name, true);
        holder.setText(com.ble.vanomize12.R.id.tv_device_name, "" + item.rssi);
    }
}
