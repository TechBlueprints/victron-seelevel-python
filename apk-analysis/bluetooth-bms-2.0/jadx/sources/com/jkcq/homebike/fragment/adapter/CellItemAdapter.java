package com.jkcq.homebike.fragment.adapter;

import com.ble.vanomize12.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkcq.homebike.fragment.CellBean;
import com.jkcq.util.LogUtil;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import no.nordicsemi.android.log.LogContract;

/* compiled from: CellItemAdapter.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0002H\u0014¨\u0006\u000b"}, d2 = {"Lcom/jkcq/homebike/fragment/adapter/CellItemAdapter;", "Lcom/chad/library/adapter/base/BaseQuickAdapter;", "Lcom/jkcq/homebike/fragment/CellBean;", "Lcom/chad/library/adapter/base/viewholder/BaseViewHolder;", LogContract.LogColumns.DATA, "", "(Ljava/util/List;)V", "convert", "", "holder", "item", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class CellItemAdapter extends BaseQuickAdapter<CellBean, BaseViewHolder> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CellItemAdapter(List<CellBean> data) {
        super(R.layout.item_cell, data);
        Intrinsics.checkParameterIsNotNull(data, "data");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(BaseViewHolder holder, CellBean item) {
        Intrinsics.checkParameterIsNotNull(holder, "holder");
        Intrinsics.checkParameterIsNotNull(item, "item");
        LogUtil.e(String.valueOf(holder.getLayoutPosition()));
        holder.setText(R.id.tv_name, item.name);
        holder.setText(R.id.tv_value, item.value);
    }
}
