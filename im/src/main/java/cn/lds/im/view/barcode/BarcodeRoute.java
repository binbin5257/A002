package cn.lds.im.view.barcode;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.JsonHelper;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.view.CaptureActivity;
import cn.lds.im.view.WebViewHelpActivity;
import de.greenrobot.event.EventBus;

/**
 * 二维码路由器
 */
public class BarcodeRoute {
    // 上下文对象
    private BaseActivity mContext;

    public BarcodeRoute(BaseActivity context) {
        this.mContext = context;
        EventBus.getDefault().register(this);
    }

    // 二维码内容
    private String strBarcode = "";


    /**
     * 根据二维码处理业务
     *
     * @param resultString 二维码内容
     */
    public void handleResult(String resultString) {
        this.strBarcode = resultString;
        // 截取二维码编号
        int index = resultString.lastIndexOf("/");
        if (index < 0) {
            // 弹出其他提示
            //this.alertDialog(resultString);
            //this.alertDialog("非绿信二维码，请重新扫描！");
            this.gotoWebView();
            return;
        }
        String strCode = resultString.substring(index + 1);
        if (ToolsHelper.isNull(strCode)) {
            // 弹出其他提示
            //this.alertDialog(resultString);
            //this.alertDialog("非绿信二维码，请重新扫描！");
            this.gotoWebView();
            return;
        } else {
            LoadingDialog.showDialog(mContext, mContext.getString(R.string.contactpersoninfoactivity_loading));
            // 请求API，检查二维码
//            CoreHttpApi.checkQRcode(mContext, strCode);
        }
    }

    /**
     * 获取弹出Dialog对象(其他)
     *
     * @param strMessage 提示消息
     * @return AlertDialog对象
     */
    private void alertDialog(String strMessage) {

        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                resetCamera();
            }

            @Override
            public void cancel() {
                resetCamera();
            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(strMessage).show(mContext.findViewById(R.id.top_title_tv));

    }

    /**
     * 网络请求,加入群组
     *
     * @param event http请求完成事件
     */
    public void onEvent(HttpRequestEvent event) {
        // REST API请求返回结果
        HttpResult httpResult = event.getResult();
        // API区分
        String strApiKey = httpResult.getApiNo();
        if (!(CoreHttpApiKey.checkQRcode.equals(strApiKey))) {
            return;
        }
        // 检查二维码
        if (CoreHttpApiKey.checkQRcode.equals(strApiKey)) {
            this.checkQrCode(httpResult);
        }
    }

    /**
     * 网络请求,加入群组
     *
     * @param event http请求完成事件
     */
    public void onEvent(HttpRequestErrorEvent event) {
        // 只要是本画面的处理。都异常时都关闭等待
        LoadingDialog.dismissDialog();
        // REST API请求返回结果
        HttpResult httpResult = event.getResult();
        // API区分
        String strApiKey = httpResult.getApiNo();
        if (!(CoreHttpApiKey.checkQRcode.equals(strApiKey))) {
            return;
        }
        //ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
        this.resetCamera();
        this.gotoWebView();
    }

    public void gotoWebView() {
        if (this.strBarcode.startsWith("http")) {
            WebViewHelpActivity webViewHelpActivity = new WebViewHelpActivity();
            webViewHelpActivity.startLoadUrl(this.strBarcode);
        } else {
            WebViewHelpActivity webViewHelpActivity = new WebViewHelpActivity();
            webViewHelpActivity.startLoadUrl(this.strBarcode);
        }
        //this.resetCamera();
        this.mContext.finish();
    }

    /**
     * 检查二维码
     *
     * @param httpResult
     */
    private void checkQrCode(HttpResult httpResult) {
        try {
            LoadingDialog.dismissDialog();
            JSONObject result = httpResult.getJsonResult();
            JSONObject json = result.getJSONObject("data");
            String qrType = JsonHelper.getString(json, "qrType");
            // TODO 将来业务代码可以写接口处理。
        } catch (JSONException e) {
            LogHelper.e(this.getClass().getSimpleName(), e);
        }
    }

    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 重置摄像头
     */
    private void resetCamera() {
        // 长按识别二维码的时候，就不需要关闭摄像头了。加上try catch 避免错误。
        try {
            ((CaptureActivity) mContext).prepareScan();
        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }
}
