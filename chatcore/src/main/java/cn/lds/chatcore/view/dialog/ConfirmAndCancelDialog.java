package cn.lds.chatcore.view.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.lds.chatcore.R;
import cn.lds.chatcore.view.dialog.annotation.ClickPosition;
import cn.lds.chatcore.view.dialog.base.CenterNormalDialog;

/**
 * Created by sibinbin on 18-1-22.
 */

public class ConfirmAndCancelDialog extends CenterNormalDialog<ConfirmAndCancelDialog> {

    private String mContent;
    private TextView contentTx;
    private Activity mActivity;
    private TextView cancelTx;
    private TextView confirmTx;
    private String confirmText;
    private String cancelText;

//    public ConfirmAndCancelDialog(Activity context) {
//        super(context);
//        mActivity = context;
//        init();
//    }
    public ConfirmAndCancelDialog(Activity context,int theme) {
        super(context,theme);
        mActivity = context;
        init();
    }


    private void init() {
        cancelTx = (TextView) findViewById(R.id.pop_cancel);
        confirmTx = (TextView) findViewById(R.id.eventselected_popsend);
        contentTx = (TextView) findViewById(R.id.eventselected_popcontent);
        setOnCilckListener(R.id.eventselected_popsend,R.id.pop_cancel);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setBackGroundColor(mActivity,1.0f);
            }
        });
    }

    public void setPromptContent(String content){
        this.mContent = content;
        contentTx.setText(mContent);

    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
        confirmTx.setText(confirmText);

    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
        cancelTx.setText(cancelText);

    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_popwindowhelper_remind;
    }

    @Override
    public void onCreateData() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v, int id) {
        if (id == R.id.eventselected_popsend) {
            dismiss();
            onDialogClickListener(ClickPosition.ACCEPT);
        }else if(id == R.id.pop_cancel){
            dismiss();
            onDialogClickListener(ClickPosition.CANCEL);
        }
    }
    /**
     * 设置窗体透明度
     */
    public void setBackGroundColor(Activity act, float alpha){
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.alpha = alpha;
        act.getWindow().setAttributes(lp);
    }


    public void show() {

        super.show();
    }
    public void showDialog(final View view){
        final Handler mHandler = new Handler();
        Runnable showPopWindowRunnable = new Runnable() {

            @Override
            public void run() {
                // 如何根元素的width和height大于0说明activity已经初始化完毕
                if( view != null && view.getWidth() > 0 && view.getHeight() > 0) {
                    // 显示popwindow
                    setBackGroundColor(mActivity,0.7f);
                    ConfirmAndCancelDialog.super.show();
                    // 停止检测
                    mHandler.removeCallbacks(this);
                } else {
                    // 如果activity没有初始化完毕则等待5毫秒再次检测
                    mHandler.postDelayed(this, 5);
                }
            }
        };
        showPopWindowRunnable.run();
    }
}
