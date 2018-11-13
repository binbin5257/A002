package cn.lds.chatcore.imtp;

import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.DeviceHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.enums.ConnectionStatus;
import cn.lds.chatcore.enums.NetworkState;
import cn.lds.chatcore.event.ApplicationInitializedEvent;
import cn.lds.chatcore.event.ConnectionStatusChangedEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AbstractManager;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.NetworkManager;
import cn.lds.im.sdk.api.ImConnectOptions;
import cn.lds.im.sdk.bean.SendMessage;
import cn.lds.im.sdk.enums.DeviceType;
import cn.lds.im.sdk.enums.OsType;
import cn.lds.im.sdk.message.util.MessageUtil;
import de.greenrobot.event.EventBus;

/**
 * IMTP管理
 * Created by user on 2016/1/4.
 */
public class ImtpManager extends AbstractManager {
    public static String TAG = ImtpManager.class.getSimpleName();
    /* ImtpManager单例 */
    protected static ImtpManager instance;

    public static ImtpManager getInstance()
    {
        if(instance==null){
            try{
                instance = new ImtpManager();
                MyApplication.getInstance().addManager(instance);
                EventBus.getDefault().register(instance);
            }catch (Exception ex){
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    //上下文
    //private Context mContext = MyApplication.getInstance().getApplicationContext();
    // 消息服务
    private ImtpService service = null;
    // 服务连接
    //private ImtpServiceConnection serviceConn = new ImtpServiceConnection();
    private ImConnectOptions mImConnectOptions;
    //默认IM连接超时时间间隔为10秒
    private int defaultConnectTimeOutInterval = 10;
    //默认ping时间间隔为20秒
    private int defaultPingInterval = 20;
    //默认超时时间间隔为60秒
    private int defaultTimeoutInterval = 60;
    //默认连接检查时间间隔为10秒
    private int defaultTaskInterval = 5;
    // 注册设备API控制条件（如果已经发起了IM注册，则没有结果返回前，不发起下一次注册）
    private boolean isDoRegisterDevice = true;
    private int intDoRegisterDeviceCount = 0;
    private Intent intent;

    public void setService(ImtpService service){
        this.service = service;
    }
    /**
     * 开启服务
     */
    public void startService(Context context) {
        try {
            LogHelper.d("IM连接：ImtpManager::startService()");
            if (intent == null) {
                //intent = new Intent(MyApplication.getInstance(), ImtpService.class);
                intent = new Intent(context, ImtpService.class);
            }
            MyApplication.getInstance().startService(intent);
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 停止服务
     */
    public void stopService() {
        try {
            LogHelper.d("IM连接：ImtpManager::stopService()");
            if (service != null) {
                service.disConnect();
            }
            if (intent != null) {
                MyApplication.getInstance().stopService(intent);
            }
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 开启timer
     */
    public void startTimer() {
        try {
            LogHelper.d("IM连接：ImtpManager::startTimer()");
            if (service != null) {
                service.startTimer();
            }
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 停止timer
     */
    public void stopTimer() {
        try {
            LogHelper.d("IM连接：ImtpManager::stopTimer()");
            if (service != null) {
                service.stopTimer();
            }
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 服务连接
     */
//    private final class ImtpServiceConnection implements ServiceConnection {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder binder) {
//            LogHelper.d("IM连接：服务已绑定，准备连接");
//            service = ((ImtpBinder) binder).getService();
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            LogHelper.d("IM连接：服务断开");
//            service = null;
//            //startService();
//        }
//    }

    /**
     * 应用初始化完成，准备连接IM
     *
     * @param event 应用初始化完成事件
     */
    public void onEventBackgroundThread(ApplicationInitializedEvent event) {
        LogHelper.d("IM连接：ImtpManager::onEventBackgroundThread(ApplicationInitializedEvent event) 应用初始化完成，准备连接IM");
        registerDevice(true);
    }

    /**
     * 注册设备（获取IM服务器地址）
     * <p/>
     * 连接IM的唯一入口
     */
    public void registerDevice(boolean isFirstConnect) {
        try {
            // 如果未登录、则禁止发起注册动作
            if (!AccountManager.getInstance().isLogin()) {
                LogHelper.d("IM连接：ImtpManager::registerDevice() 用户未登录，禁止发起新的注册");
                return;
            }
            // 如果之前的注册无结果，则禁止发起新的注册
            if (!isDoRegisterDevice) {
                // API请求超过60秒无结果、则强制发起注册功能
                if (defaultTaskInterval * intDoRegisterDeviceCount > 60) {
                    LogHelper.d("IM连接：ImtpManager::registerDevice() API请求超过60秒无结果、则强制发起注册功能");
                    isDoRegisterDevice = true;
                    intDoRegisterDeviceCount = 0;
                } else {
                    LogHelper.d("IM连接：ImtpManager::registerDevice() 前次注册无结果，禁止发起新的注册");
                    intDoRegisterDeviceCount++;
                    return;
                }
            } else {
                isDoRegisterDevice = false;
            }
            LogHelper.d("IM连接：ImtpManager::registerDevice()发送注册设备API");
            if (!isFirstConnect) {
                EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.CONNECTING));
            }

            String msg = "消息跟踪 网络连接状态="+ NetworkManager.getInstance().getState();
            msg+="    IM连接状态 isConnect="+ ImtpManager.getInstance().isConnect();
            msg+="    IM连接状态 isConnecting="+ ImtpManager.getInstance().isConnecting();
            LogHelper.d(msg);
            CoreHttpApi.registerDevice();
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 注册设备成功
     *
     * @param event REST请求成功事件
     */
    public void onEventBackgroundThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        if (!CoreHttpApiKey.CONFIG_SERVER_URL.equals(httpResult.getApiNo()))
            return;
        LogHelper.d("IM连接：ImtpManager::onEventBackgroundThread(HttpRequestEvent event) 注册设备API请求完成");
        LogHelper.d(String.format("%s\n%s", httpResult.getUrl(), httpResult.getResult()));
        isDoRegisterDevice = true;
        try {
            JSONObject data = httpResult.getJsonResult().getJSONObject("data");

            // 获取配置信息
            String host = data.optString("host");
            int port = data.optInt("socketPort");
            String token = data.optString("token");
            String clientId = data.optString("clientId");
            if (null == mImConnectOptions) {
                mImConnectOptions = new ImConnectOptions(host, port, clientId, DeviceHelper.getDeviceId(), token, "sessionKey", OsType.ANDROID, DeviceHelper.getOsVer(), DeviceType.PHONE);

                mImConnectOptions.setConnectTimeOutSecond(defaultConnectTimeOutInterval);
                mImConnectOptions.setPingInterval(defaultPingInterval);
                mImConnectOptions.setTimeOut(defaultTimeoutInterval);
                mImConnectOptions.setCallback(new ImtpCallbackListener());
            } else {
                mImConnectOptions.setClientId(clientId);
                mImConnectOptions.setHost(host);
                mImConnectOptions.setPort(port);
                mImConnectOptions.setToken(token);
            }
            this.connect(mImConnectOptions);
            this.startTimer();

        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }


    /**
     * 注册设备失败
     *
     * @param event REST请求失败事件
     */
    public void onEventBackgroundThread(HttpRequestErrorEvent event) {
        try {
            HttpResult httpResult = event.getResult();
            if (!CoreHttpApiKey.CONFIG_SERVER_URL.equals(httpResult.getApiNo()))
                return;
            LogHelper.d("IM连接：onEventBackgroundThread(HttpRequestErrorEvent event) 注册设备API请求失败");
            LogHelper.d(String.format("%s\n%s", httpResult.getUrl(), httpResult.getResult()));
            isDoRegisterDevice = true;
            this.startTimer();
        } catch (Exception ex) {
            LogHelper.e("IM连接：注册设备失败：", ex);
        }

    }

    /**
     * 发送消息
     * @param msg
     */
    public void sendMessage(final SendMessage msg) {
        if (service != null) {
            service.sendMessage(msg);
        }
    }

    /**
     * 生成消息ID
     *
     * @return
     */
    public String generateMessageId() {
        return MessageUtil.generateMessageId();
    }


    /**
     * 网络变化处理
     */
    public void networkChange() {
        try {
            LogHelper.d("IM连接：ImtpManager::networkChange: was called ");
//            if (MyApplication.mAccount == null) {
//                MyApplication.mAccount = AccountManager.getInstance().getAccountsTable();
//            }
            if (service != null) {
                LogHelper.d("IM连接：ImtpManager::networkChange service is not null");
                service.networkChange();
            } else {
                LogHelper.d("IM连接：ImtpManager::networkChange service is null");
            }
            String msg = "消息跟踪 网络连接状态="+ NetworkManager.getInstance().getState();
            msg+="    IM连接状态 isConnect="+ ImtpManager.getInstance().isConnect();
            msg+="    IM连接状态 isConnecting="+ ImtpManager.getInstance().isConnecting();
            LogHelper.d(msg);
            // 判断网络状态、如果是连接上了、直接触发IM连接动作。
            if (NetworkState.available == NetworkManager.getInstance().getState()) {
                LogHelper.d("IM连接：网络监控->已经连接，执行PING,刷新session。");
                CoreHttpApi.ping();
                LogHelper.d("IM连接：网络监控->已经连接，执行IM连接动作1。");
                if (service != null) {
                    // 如果网络发生变化时、连接还在。则不触发重新连接。
                    if (!service.isConnect()) {
                        LogHelper.d("IM连接：网络监控->已经连接，执行IM连接动作2。");
                        registerDevice(false);
                    }
//                    LogHelper.d("IM连接：网络监控->已经连接 service!=null 强制断开IM。");
//                    service.disConnect();
//                    LogHelper.d("IM连接：网络监控->已经连接 service!=null 发起注销API请求。");
//                    CoreHttpApi.unregister(null);
//                    LogHelper.d("IM连接：网络监控->已经连接 service!=null 发起重新注册流程");
//                    registerDevice(false);
                } else {
                    LogHelper.d("IM连接：网络监控->已经连接 service==null 发起重新注册流程");
                    registerDevice(false);
                }
            } else {
                LogHelper.d("IM连接：网络监控->连接断开，提示连接断开。停止timer");
                this.stopTimer();
                EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.DISCONNECTED));
            }
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
    }


    /**
     * 获取IM是否已经连接
     * @return
     */
    private void connect(ImConnectOptions mImConnectOptions){
        try {
            LogHelper.d("IM连接：ImtpManager::connect()");
            if (service != null) {
                service.connect(mImConnectOptions);
            }
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
    }
    /**
     * 获取IM是否已经连接
     * @return
     */
    public boolean isConnect(){
        boolean isConnect = false;
        try {
            if (service != null) {
                LogHelper.d("IM连接：ImtpManager::isConnect service is not null");
                isConnect = service.isConnect();
            }else{
                LogHelper.d("IM连接：ImtpManager::isConnect service is null");
            }
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
        return isConnect;
    }

    /**
     * 获取IM是否已经连接中
     * @return
     */
    public boolean isConnecting(){
        boolean isConnecting = false;
        try {
            if (service != null) {
                LogHelper.d("IM连接：ImtpManager::isConnecting service is not null");
                isConnecting = service.isConnecting();
            }else {
                LogHelper.d("IM连接：ImtpManager::isConnecting service is null");
            }
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
        return isConnecting;
    }

    /**
     * 重启服务
     */
    public void reStartService() {
        try {
            if (service == null) {
                LogHelper.d("IM连接：重启服务");
//                if(MyApplication.mAccount==null){
//                    MyApplication.mAccount = AccountManager.getInstance().getAccountsTable();
//                }
                registerDevice(false);
            }
        }catch (Exception ex){
            LogHelper.e("IM连接：", ex);
        }
    }

}
