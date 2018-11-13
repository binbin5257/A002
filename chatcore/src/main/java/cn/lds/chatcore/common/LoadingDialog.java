/**
 * com.leadingsoft.leaderaide.activity
 *
 * @ClassName: LoadingDialog
 * @Description: LoadingDialog等待页面
 * @author: macq    macq@leadingsoft.cn
 * @date: 2014-06-16
 */
package cn.lds.chatcore.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.lds.chatcore.R;
import cn.lds.chatcore.view.widget.SectorProgressView.ColorfulRingProgressView;

public class LoadingDialog {

    private static Dialog mDialog;
    private static Dialog dialog;

    public static void showDialog(Context context, String msg) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mDialog.dismiss();
                }
                return false;

            }
        };
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.setOnKeyListener(keyListener);
        mDialog.setCancelable(false);
        mDialog.show();
        mDialog.setContentView(R.layout.loading_process_dialog_icon);
        TextView TvLoading = (TextView) mDialog
                .findViewById(R.id.loading_process_dialog_text);
        TvLoading.setText("请稍候…");
        if (ToolsHelper.isNull(msg)) {
            TvLoading.setVisibility(View.GONE);
        }

    }
    public static void showDialog(Context context, String msg,String s) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    dialog.dismiss();
                }
                return false;

            }
        };
        if(dialog == null){
            dialog = new AlertDialog.Builder(context).create();
        }
        dialog.setOnKeyListener(keyListener);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.loading_process_dialog_icon);
        TextView TvLoading = (TextView) dialog
                .findViewById(R.id.loading_process_dialog_text);
        TvLoading.setText("请稍候…");
        if (ToolsHelper.isNull(msg)) {
            TvLoading.setVisibility(View.GONE);
        }

    }

    public static void dialogIsNull(){
        if(dialog != null){
            dialog = null;
        }
    }

    public static void showDialog(Context context, String msg, boolean isShowBackGround) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mDialog.dismiss();
                }
                return false;

            }
        };
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.setOnKeyListener(keyListener);
        mDialog.setCancelable(false);
        mDialog.show();
        mDialog.setContentView(R.layout.loading_process_dialog_icon);
        LinearLayout ll_loading_process_dialog = (LinearLayout) mDialog.findViewById(R.id.ll_loading_process_dialog);
        // 清空背景
        if (!isShowBackGround) {
            ll_loading_process_dialog.setBackgroundResource(0);
        }
        TextView TvLoading = (TextView) mDialog.findViewById(R.id.loading_process_dialog_text);
        TvLoading.setText(msg);
        if (ToolsHelper.isNull(msg)) {
            TvLoading.setVisibility(View.GONE);
        }
    }

    public static void dismissDialog() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                try {
                    mDialog.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                    mDialog.dismiss();
                }
            }
        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                    dialog.dismiss();
                }
            }
        }
    }

    public static boolean isShow() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取进度条的Dialog
     */
    public static Dialog getCircleProgressDialog(Context context) {
        Dialog dialog = new AlertDialog.Builder(context).create();
        return dialog;
    }

    /**
     * 获取进度条的VIEW
     */
    public static View getCircleProgressView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_circle_process_dialog_icon, null);
        return view;
    }

    /**
     * 显示带进度条的dialog
     */
    public static void showCircleProgressDialog(Context context, Dialog dialog, View view) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    dialog.dismiss();
                }
                return false;

            }
        };
        try {
            //dialog = new AlertDialog.Builder(context).create();
            dialog.setOnKeyListener(keyListener);
            dialog.setCancelable(false);
            dialog.show();
            dialog.setContentView(view);
        } catch (Exception ex) {
            LogHelper.e("创建圆形进度条", ex);
        }
    }

    /**
     * 显示带进度条的dialog
     */
    public static void updateCircleProgressDialog(int percent, Dialog dialog, View view) {
        TextView tvPercent = (TextView) view.findViewById(R.id.tvPercent);
        ColorfulRingProgressView isDownload = (ColorfulRingProgressView) view.findViewById(R.id.crpv);
        isDownload.setPercent(percent);
        tvPercent.setText("" + percent);
    }

    /********************************* APK下载圆形进度条 ***********************************************/

    /**
     * 获取进度条的VIEW
     */
    public static View getProgressViewByTextView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_circle_process_dialog_icon_by_textview, null);
        return view;
    }

    /**
     * 显示带进度条的dialog
     */
    public static void showCircleProgressDialog(final Context context, Dialog dialog, View view, final Handler handler, String msg) {

        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    handler.obtainMessage(9999).sendToTarget();
                }
                return false;
            }
        };
        try {
            //dialog = new AlertDialog.Builder(context).create();
            dialog.setOnKeyListener(keyListener);
            dialog.setCancelable(false);
            dialog.show();

            TextView textView = (TextView) view.findViewById(R.id.tip_msg);
            textView.setText(msg);
            dialog.setContentView(view);
        } catch (Exception ex) {
            LogHelper.e("创建圆形进度条", ex);
        }
    }
}
