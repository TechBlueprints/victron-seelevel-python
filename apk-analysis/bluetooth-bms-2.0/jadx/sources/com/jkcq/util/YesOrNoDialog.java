package com.jkcq.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/* loaded from: classes.dex */
public class YesOrNoDialog extends BaseDialog {
    Context context;
    boolean isShowCancel;
    private OnButtonListener listener;
    TextView tvCancel;
    TextView tvMessage;
    TextView tvSure;

    public YesOrNoDialog(Context context, String str, String str2, String str3, String str4) {
        super(context, R.style.SimpleHUD1);
        this.isShowCancel = true;
        setContentView(R.layout.dialog_yes_or_no_layout);
        this.context = context;
        this.isShowCancel = true;
        initView();
        initEvent();
        initData(str, str2, str3, str4);
    }

    public YesOrNoDialog(Context context, String str, String str2, String str3, String str4, boolean z) {
        super(context, R.style.SimpleHUD1);
        this.isShowCancel = true;
        setContentView(R.layout.dialog_yes_or_no_layout);
        this.context = context;
        this.isShowCancel = z;
        initView();
        initEvent();
        initData(str, str2, str3, str4);
    }

    private void initData(String str, String str2, String str3, String str4) {
        this.tvMessage.setText(str2);
        if (!TextUtils.isEmpty(str3)) {
            this.tvCancel.setText(str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            this.tvSure.setText(str4);
        }
        if (this.isShowCancel) {
            this.tvCancel.setVisibility(0);
        } else {
            this.tvCancel.setVisibility(8);
        }
    }

    public void initView() {
        this.tvCancel = (TextView) findViewById(R.id.cancel);
        this.tvSure = (TextView) findViewById(R.id.sure);
        this.tvMessage = (TextView) findViewById(R.id.tv_message);
    }

    public void initEvent() {
        this.tvCancel.setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.util.YesOrNoDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (YesOrNoDialog.this.listener != null) {
                    YesOrNoDialog.this.dismiss();
                    YesOrNoDialog.this.listener.onCancleOnclick();
                }
            }
        });
        this.tvSure.setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.util.YesOrNoDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (YesOrNoDialog.this.listener != null) {
                    YesOrNoDialog.this.dismiss();
                    YesOrNoDialog.this.listener.onSureOnclick();
                }
            }
        });
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        setCancelable(false);
    }

    public void setBtnOnclick(OnButtonListener onButtonListener) {
        this.listener = onButtonListener;
    }
}
