package com.jkcq.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/* loaded from: classes.dex */
public class LoginOutDialog extends BaseDialog {
    String buttonValue;
    Context context;
    private OnButtonListener listener;
    String message;
    TextView tvSure;
    TextView tv_message;
    TextView tv_title;

    public LoginOutDialog(Context context) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_loginout_layout);
        this.context = context;
        initView();
        initEvent();
    }

    public LoginOutDialog(Context context, String str, String str2) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_loginout_layout);
        this.context = context;
        this.message = str;
        this.buttonValue = str2;
        initView();
        initEvent();
    }

    public void initView() {
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.tvSure = (TextView) findViewById(R.id.tv_sure);
        this.tv_message = (TextView) findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(this.message)) {
            this.tv_message.setText(this.message);
        }
        if (TextUtils.isEmpty(this.buttonValue)) {
            return;
        }
        this.tvSure.setText(this.buttonValue);
    }

    public void initEvent() {
        this.tvSure.setOnClickListener(new View.OnClickListener() { // from class: com.jkcq.util.LoginOutDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (LoginOutDialog.this.listener != null) {
                    LoginOutDialog.this.dismiss();
                    LoginOutDialog.this.listener.onSureOnclick();
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
