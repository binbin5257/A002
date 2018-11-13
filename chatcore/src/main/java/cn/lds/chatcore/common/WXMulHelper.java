//package cn.lds.chatcore.common;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.tencent.connect.share.QQShare;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.SendAuth;
//import com.tencent.mm.sdk.openapi.SendMessageToWX;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.mm.sdk.openapi.WXMediaMessage;
//import com.tencent.mm.sdk.openapi.WXTextObject;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;
//
///**
// * Created by pengwb on 2016/2/22.
// * <p/>
// * 微信功能类
// */
//public class WXMulHelper {
//
//
//    /**
//     * 微信登录
//     */
//    public static final String WEIXIN_APP_ID = "wx0e6b78b93cf33401";
//    public static final String WEIXIN_APP_SECRET = "f46ea910662d5b738d42a979fb770ff8";
//    private final String WEIXIN_SCOPE = "snsapi_userinfo";
//    private final String WEIXIN_STATE = "wechat_sdk_demo_test";
//    public static final String QQ_APP_ID = "1105234224";
//
//    protected final static WXMulHelper instance;
//
//    private static IWXAPI wxApi;
//
//    //分享类别 朋友圈
//    public static final int WXSceneTimeline = 1;
//    //分享类别 微信聊天窗口
//    public static final int WXSceneSession = 0;
//    private static Context mContext;
//
//    static {
//        instance = new WXMulHelper();
//    }
//
//    public static WXMulHelper getInstance(Intent getIntent, Context mContext) {
//        wxApi = WXAPIFactory.createWXAPI(mContext, WEIXIN_APP_ID, false);
//        wxApi.registerApp(WEIXIN_APP_ID);
//        wxApi.handleIntent(getIntent, null);
//        WXMulHelper.mContext = mContext;
//        return instance;
//    }
//
//
//    /**
//     * 微信登陆
//     */
//    public void wechatlogin() {
//        if (!wxApi.isWXAppInstalled()) {
//            //提醒用户没有按照微信
//            ToolsHelper.showInfo(mContext, "请安装微信！");
//            return;
//        }
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = WEIXIN_SCOPE;
//        req.state = WEIXIN_STATE;
//        wxApi.sendReq(req);
//    }
//
//    /**
//     * 发送文本到微信
//     *
//     * @param shareText 分享的文本
//     * @param type      分享的类型  0 ：聊天窗口 || 1 ：朋友圈
//     */
//    public void shareTxtToWx(String shareText, int type) {
//        if (!wxApi.isWXAppInstalled()) {
//            //提醒用户没有按照微信
//            ToolsHelper.showInfo(mContext, "请安装微信！");
//            return;
//        }
//        // 初始化一个WXTextObject对象
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = shareText;
//        // 用WXTextObject对象初始化一个WXMediaMessage对象
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;
//        // 发送文本类型的消息时，title字段不起作用
//        // msg.title = "Will be ignored";
//        msg.description = shareText;
//        // 构造一个Req请求
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        // transaction字段用于唯一标识一个请求
//        req.transaction = buildTransaction("text");
//        // 请求消息内容
//        req.message = msg;
//
//
//        // 分享到朋友圈还是会话当中
//        // req.scene = WXSceneTimeline 代表分享内容到朋友圈
//        // req.scene = WXSceneSession 代表分享内容到会话当中
//        switch (type) {
//            case WXSceneSession:
//                req.scene = SendMessageToWX.Req.WXSceneSession;
//                break;
//            case WXSceneTimeline:
//                req.scene = SendMessageToWX.Req.WXSceneTimeline;
//                break;
//        }
//
//        // 调用api接口发送数据到微信
//        wxApi.sendReq(req);
//    }
//
//    public void shareToQQ(Activity activity, String text, IUiListener iUiListener) {
//        Tencent mTencent = Tencent.createInstance(QQ_APP_ID, activity.getApplicationContext());
//        final Bundle params = new Bundle();
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "安装宝沃");
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "点击下载宝沃");
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.pgyer.com/6Xs8");
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://o1wh05aeh.qnssl.com/image/view/app_icons/ff379834974a972efd067377bf121522/120");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "宝沃");
//        mTencent.shareToQQ(activity, params, iUiListener);
//    }
//
//    private IUiListener iUiListener = new IUiListener() {
//        @Override
//        public void onComplete(Object o) {
//            ToolsHelper.showInfo(mContext, "分享成功");
//        }
//
//        @Override
//        public void onError(UiError uiError) {
//            ToolsHelper.showInfo(mContext, "分享失败");
//        }
//
//        @Override
//        public void onCancel() {
//            ToolsHelper.showInfo(mContext, "取消分享");
//        }
//    };
//
//
//    /**
//     * 格式化一下类型格式
//     *
//     * @param type
//     * @return
//     */
//    private String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis())
//                : type + System.currentTimeMillis();
//    }
//}
