package cn.lds.chatcore.manager;

import android.app.Activity;
import android.os.Handler;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.OrderPayModel;
import cn.lds.chatcore.data.PayAliInfoModel;
import cn.lds.chatcore.data.PayModel;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.MessageArrivedEvent;
import cn.lds.chatcore.event.MessagePay;
import cn.lds.chatcore.event.RequestPay;
import cn.lds.chatcore.event.StartPay;
import cn.lds.chatcore.imtp.message.Message;
import de.greenrobot.event.EventBus;

/**
 * Created by quwei on 2015/12/24.
 */
public class PayManager extends AbstractManager {
    public static String _TAG = PayManager.class.getSimpleName();
    protected static PayManager instance;
    private IWXAPI iwxapi;
    private Activity mActivity;

    public String businessId = "";

    public static PayManager getInstance() {
        if (instance == null) {
            try {
                instance = new PayManager();
                MyApplication.getInstance().addManager(instance);
                EventBus.getDefault().register(instance);
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    public IWXAPI getIwxapi() {
        if (null == iwxapi)
            iwxapi = WXAPIFactory.createWXAPI(mApplicationContext, AppIndependentConfigure.appId);
        return iwxapi;
    }

    /**
     * API请求处理
     *
     * @param event
     */
    public void onEventBackgroundThread(MessageArrivedEvent event) {
        Message message = event.getMessage();
        switch (message.getType()) {
            case FOREGIFT_RECHARGE:
                EventBus.getDefault().post(new MessagePay(0, true));
                break;
            case ACCOUNT_RECHARGE:
                EventBus.getDefault().post(new MessagePay(1, true));
                break;
            case ORDER_PAY:
                EventBus.getDefault().post(new MessagePay(2, true));
                break;
        }
    }


//    /**
//     * 支付订单
//     */
//    public void reservationOrdersPay(final String id, String type, final Activity activity) {
//        this.mActivity = activity;
//        switch (type) {
//            case "微信支付":
//                boolean isPaySupported = getIwxapi().getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//                if (isPaySupported) {
//                    MyApplication.getInstance().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            LoadingDialog.showDialog(mActivity, "正在获取订单信息,请稍候");
//                            CoreHttpApi.reservationOrdersWxPay(id);
//                        }
//                    });
//                } else {
//                    MyApplication.getInstance().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToolsHelper.showStatus(mActivity, false, "未安装微信,或微信版本不支持");
//                            LoadingDialog.dismissDialog();
//                        }
//                    });
//                }
//                break;
//            case "支付宝":
////                MyApplication.getInstance().runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        LoadingDialog.showDialog(activity, "正在获取订单信息,请稍候");
//                CoreHttpApi.reservationOrdersAlipay(id);
//
////                    }
////                });
//                break;
//            case "余额支付":
//                CoreHttpApi.reservationOrdersWalletpay(id);
//                break;
//            default:
//                LoadingDialog.dismissDialog();
//                ToolsHelper.showStatus(mApplicationContext, false, "请选择支付方式");
//                break;
//        }
//    }
    /**
     * 支付订单
     */
    public void reservationOrdersPay(final OrderPayModel mOrderModel, String type, final Activity activity) {
        this.mActivity = activity;
        switch (type) {
            case "微信支付":
                boolean isPaySupported = getIwxapi().getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                if (isPaySupported) {
                    CoreHttpApi.reservationOrdersWxPay(mOrderModel);
                } else {
                    MyApplication.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToolsHelper.showStatus(mActivity, false, "未安装微信,或微信版本不支持");
                            LoadingDialog.dismissDialog();
                        }
                    });
                }
                break;
            case "支付宝":
                CoreHttpApi.reservationOrdersAlipay(mOrderModel);
                break;
            default:
                LoadingDialog.dismissDialog();
                ToolsHelper.showStatus(mApplicationContext, false, "请选择支付方式");
                break;
        }
    }

    /**
     * 支付押金
     */
    public void depositPay(final OrderPayModel mOrderModel, String type, final Activity activity) {
        this.mActivity = activity;
        switch (type) {
            case "微信支付":
                boolean isPaySupported = getIwxapi().getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                if (isPaySupported) {
                    CoreHttpApi.foregiftAccountsWxPay(mOrderModel);
                } else {
                    MyApplication.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToolsHelper.showStatus(mActivity, false, "未安装微信,或微信版本不支持");
                            LoadingDialog.dismissDialog();
                        }
                    });
                }
                break;
            case "支付宝":
                CoreHttpApi.foregiftAccountsAlipay(mOrderModel);
                break;
            default:
                LoadingDialog.dismissDialog();
                ToolsHelper.showStatus(mApplicationContext, false, "请选择支付方式");
                break;
        }
    }
    public void foregiftAccountsWalletpay() {
        CoreHttpApi.foregiftAccountsWalletpay();
    }

    /**
     * 充值
     */
    public void accounts(final OrderPayModel mOrderModel, String type, final Activity activity) {
        this.mActivity = activity;
        switch (type) {
            case "微信支付":
                boolean isPaySupported = getIwxapi().getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                if (isPaySupported) {
                    CoreHttpApi.accountsWxPay(mOrderModel);
                } else {
                    MyApplication.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToolsHelper.showStatus(mActivity, false, "未安装微信,或微信版本不支持");
                        }
                    });
                }
                break;
            case "支付宝支付":
                CoreHttpApi.accountsAlipay(mOrderModel);
                break;
            default:
//                LoadingDialog.dismissDialog();
                ToolsHelper.showStatus(mApplicationContext, false, "请选择支付方式");
                break;
        }

    }


    public void onEventBackgroundThread(HttpRequestEvent event) {

        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(CoreHttpApiKey.reservationOrdersWxPay.equals(apiNo)
                || CoreHttpApiKey.reservationOrdersAlipay.equals(apiNo)
                || CoreHttpApiKey.accountsWxPay.equals(apiNo)
                || CoreHttpApiKey.accountsAlipay.equals(apiNo)
                || CoreHttpApiKey.foregiftAccountsWxPay.equals(apiNo)
                || CoreHttpApiKey.foregiftAccountsWalletpay.equals(apiNo)
                || CoreHttpApiKey.reservationOrdersWalletpay.equals(apiNo)
                || CoreHttpApiKey.foregiftAccountsAlipay.equals(apiNo))) {
            return;
        }
        String result = httpResult.getResult();
        LogHelper.e(result);
        switch (apiNo) {
            case CoreHttpApiKey.reservationOrdersWxPay:
                payInWx(2, result);
                break;
            case CoreHttpApiKey.reservationOrdersAlipay:
                payInAli(2, result);
                break;
            case CoreHttpApiKey.foregiftAccountsAlipay://支付宝--押金
                payInAli(0, result);
                break;
            case CoreHttpApiKey.foregiftAccountsWxPay://微信--押金
                payInWx(0, result);
                break;
            case CoreHttpApiKey.accountsWxPay://微信充值
                payInWx(1, result);
                break;
            case CoreHttpApiKey.accountsAlipay://支付宝充值
                payInAli(1, result);
                break;
            case CoreHttpApiKey.foregiftAccountsWalletpay:
                EventBus.getDefault().post(new StartPay(0, true));
                EventBus.getDefault().post(new MessagePay(0, true));
                break;
            case CoreHttpApiKey.reservationOrdersWalletpay:
                EventBus.getDefault().post(new StartPay(2, true));
                EventBus.getDefault().post(new MessagePay(2, true));
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void payInAli(final int type, String result) {
        PayAliInfoModel payAliInfoModel = GsonImplHelp.get().toObject(result, PayAliInfoModel.class);
        if (null == payAliInfoModel) {
            EventBus.getDefault().post(new StartPay(type, false));
            return;
        }
        businessId = payAliInfoModel.getData().getBusinessId();
        final String orderInfo = payAliInfoModel.getData().getAliPayParaStr();
        EventBus.getDefault().post(new StartPay(type, true, null));
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                int resultStatus = Integer.parseInt(result.get("resultStatus"));
//                mHandler.sendEmptyMessage(resultStatus);
                android.os.Message msg = new android.os.Message();
                msg.what = resultStatus;
                msg.obj = type;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 微信支付
     */
    private void payInWx(int type, String result) {
        PayModel payModel = GsonImplHelp.get().toObject(result, PayModel.class);
        if (null == payModel) {
            EventBus.getDefault().post(new StartPay(type, false));
            return;
        }
        businessId = payModel.getData().getBusinessId();
        PayReq req = new PayReq();
        req.appId = AppIndependentConfigure.appId;
        req.partnerId = payModel.getData().getMchId();
        req.prepayId = payModel.getData().getPrePayId();
        req.nonceStr = payModel.getData().getNonceStr();
        req.timeStamp = payModel.getData().getTimestamp();
        req.packageValue = "Sign=WXPay";
        req.sign = payModel.getData().getSign();
        req.extData = "app data"; // optional
//                    ToolsHelper.showInfo(mContext, "支付");
//                    api.registerApp(Constants.appId);
        getIwxapi().sendReq(req);
        EventBus.getDefault().post(new StartPay(type, true));
    }

    public void onEventBackgroundThread(HttpRequestErrorEvent event) {

        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(CoreHttpApiKey.reservationOrdersWxPay.equals(apiNo)
                || CoreHttpApiKey.accountsWxPay.equals(apiNo)
                || CoreHttpApiKey.accountsAlipay.equals(apiNo)
                || CoreHttpApiKey.reservationOrdersAlipay.equals(apiNo)
                || CoreHttpApiKey.foregiftAccountsWxPay.equals(apiNo)
                || CoreHttpApiKey.foregiftAccountsWalletpay.equals(apiNo)
                || CoreHttpApiKey.reservationOrdersWalletpay.equals(apiNo)
                || CoreHttpApiKey.foregiftAccountsAlipay.equals(apiNo))) {
            return;
        }
        switch (apiNo) {
            case CoreHttpApiKey.reservationOrdersWxPay:
            case CoreHttpApiKey.reservationOrdersAlipay:
            case CoreHttpApiKey.reservationOrdersWalletpay:
                EventBus.getDefault().post(new StartPay(2, false, httpResult));
                break;
            case CoreHttpApiKey.accountsWxPay:
            case CoreHttpApiKey.accountsAlipay:
                EventBus.getDefault().post(new StartPay(1, false, httpResult));
                break;
            case CoreHttpApiKey.foregiftAccountsAlipay:
            case CoreHttpApiKey.foregiftAccountsWxPay:
            case CoreHttpApiKey.foregiftAccountsWalletpay:
                EventBus.getDefault().post(new StartPay(0, false, httpResult));
                break;
        }
    }

    /**
     * 支付宝返回处理位置
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 9000://订单支付成功
//                    Toast.makeText(mContext, "订单支付成功", Toast.LENGTH_SHORT).show();
                    ToolsHelper.showStatus(mApplicationContext, true, "支付成功");
                    EventBus.getDefault().post(new RequestPay((int) msg.obj, 0));
                    if ((int) msg.obj == 2) {//订单支付完成，关闭订单页面，返回到行程列表。
                        mActivity.finish();
                    }
                    break;
                case 8000:// 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态break;
                    break;
                case 4000:// 订单支付失败
                    ToolsHelper.showStatus(mApplicationContext, false, "支付失败");
                    EventBus.getDefault().post(new RequestPay((int) msg.obj, -1));
                    break;
                case 5000://重复请求
                    break;
                case 6001:// 用户中途取消
                    ToolsHelper.showStatus(mApplicationContext, true, "用户取消支付");
                    EventBus.getDefault().post(new RequestPay((int) msg.obj, -2));
                    break;
                case 6002://网络连接出错
                    break;
                case 6004://支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    break;
                default://其他错误
                    ToolsHelper.showStatus(mApplicationContext, false, "未知支付错误");
                    EventBus.getDefault().post(new RequestPay((int) msg.obj, -3));
                    break;
            }
        }
    };

}
