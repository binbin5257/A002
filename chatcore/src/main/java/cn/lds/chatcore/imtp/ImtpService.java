package cn.lds.chatcore.imtp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.SystemConfig;
import cn.lds.chatcore.data.AppInfoDataItem;
import cn.lds.chatcore.enums.ConnectionStatus;
import cn.lds.chatcore.enums.NetworkState;
import cn.lds.chatcore.event.ConnectionStatusChangedEvent;
import cn.lds.chatcore.event.IMSdkDebugEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.FileManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.manager.MasterManager;
import cn.lds.chatcore.manager.MessageManager;
import cn.lds.chatcore.manager.NetworkManager;
import cn.lds.chatcore.manager.NotificationManager;
import cn.lds.chatcore.manager.PayManager;
import cn.lds.chatcore.manager.SystemManager;
import cn.lds.chatcore.manager.TimestampManager;
import cn.lds.chatcore.manager.VersionManager;
import cn.lds.im.sdk.api.ImClient;
import cn.lds.im.sdk.api.ImConnectOptions;
import cn.lds.im.sdk.bean.SendMessage;
import de.greenrobot.event.EventBus;

//import cn.lds.chatcore.manager.ChatSessionManager;
//import cn.lds.chatcore.manager.ContactsManager;
//import cn.lds.chatcore.manager.ContactsRequestManager;
//import cn.lds.chatcore.manager.MucManager;
//import cn.lds.chatcore.manager.OrganizationManager;
//import cn.lds.chatcore.manager.OrganizationMemberManager;
//import cn.lds.chatcore.manager.PublicAccountManager;
//import cn.lds.chatcore.manager.PublicWebManager;
//import cn.lds.chatcore.manager.SettingManager;
//import cn.lds.chatcore.manager.TagManager;
//import cn.lds.chatcore.manager.ThirdAppClassManager;
//import cn.lds.chatcore.manager.TodoTaskManager;
//import cn.lds.chatcore.manager.VcardsManager;

/**
 * IMTP服务
 * Created by user on 2016/1/4.
 */
public class ImtpService extends Service {

    private String TASKEXECUTOR_COMMON = "common";
    private String TASKEXECUTOR_PROTECT = "protect";

    private ImClient mImClient = null;
    //private ImConnectOptions mImConnectOptions;
    private static ExecutorService pool = Executors.newCachedThreadPool();

    private ArrayList<Object> registeredManagers;
    // 连接判断计数器（20秒检测一次IM状态，如果是连接中的话，则强制重置成false，false）
    private int count = 1;
    //登录TASK
    private ScheduledExecutorService schedule = null;
    private int defaultTaskInterval = 5;
    public boolean isStart = false;

    //守护TASK（300秒）
    private ScheduledExecutorService protectTaskschedule = null;
    private int defaultProtectTaskInterval = 300;
    public boolean isProtectTaskStart = false;

    private TaskExecutor task_common = null;
    private TaskExecutor task_protect = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogHelper.d("IM连接：ImtpService::onBind()");
        //this.startTimer();
        return new ImtpBinder(this);
    }

    @Override
    public void onCreate() {
        LogHelper.d("IM连接：ImtpService::onCreate()");
        //this.startTimer();
        if (registeredManagers == null) {
            registeredManagers = new ArrayList<>();
            try {
                registeredManagers.add(MessageManager.getInstance());
                registeredManagers.add(AccountManager.getInstance());
                registeredManagers.add(SystemManager.getInstance());
//                registeredManagers.add(ChatSessionManager.getInstance());
//                registeredManagers.add(ContactsManager.getInstance());
//                registeredManagers.add(ContactsRequestManager.getInstance());
                registeredManagers.add(FileManager.getInstance());
                registeredManagers.add(ImageLoaderManager.getInstance());
                registeredManagers.add(MasterManager.getInstance());
//                registeredManagers.add(MucManager.getInstance());
                registeredManagers.add(NetworkManager.getInstance());
                registeredManagers.add(NotificationManager.getInstance());
//                registeredManagers.add(OrganizationManager.getInstance());
//                registeredManagers.add(OrganizationMemberManager.getInstance());
//                registeredManagers.add(PublicAccountManager.getInstance());
//                registeredManagers.add(PublicWebManager.getInstance());
//                registeredManagers.add(SettingManager.getInstance());
//                registeredManagers.add(TagManager.getInstance());
                registeredManagers.add(TimestampManager.getInstance());
//                registeredManagers.add(VcardsManager.getInstance());
                registeredManagers.add(VersionManager.getInstance());
//                registeredManagers.add(ThirdAppClassManager.getInstance());
//                registeredManagers.add(TodoTaskManager.getInstance());
                registeredManagers.add(ImtpManager.getInstance());
                registeredManagers.add(PayManager.getInstance());
                ImtpManager.getInstance().setService(this);

            } catch (Exception ex) {
                LogHelper.e("IM连接：", ex);
            }
        }
        this.startTimer();
//        this.startProtectTask();
        super.onCreate();
    }


    private ScheduledExecutorService scheduledExecutorService;


    @Override
    public void onStart(Intent intent, int startId) {
//        startGuard();
        LogHelper.d("IM连接：ImtpService::onStart()");

//        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        LogHelper.d("IM连接：ImtpService::onDestroy()");
        this.keepServiceAlive();
        //super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogHelper.d("IM连接：ImtpService::onStartCommand()");
        //this.startTimer();
        //return START_STICKY;
        flags = START_STICKY;

        if (Build.VERSION.SDK_INT < 18) {
            startForeground(-1000, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, GuardService.class);
            startService(innerIntent);
            startForeground(-1000, new Notification());
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogHelper.d("IM连接：ImtpService::onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        LogHelper.d("IM连接：ImtpService::onLowMemory()");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        LogHelper.d("IM连接：ImtpService::onTrimMemory() level = " + level);
        ImageLoaderManager.getInstance().clearMemoryCache();
        this.startTimer();
//        this.startProtectTask();
        NetworkManager.getInstance().printNewWorkStatus();
        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogHelper.d("IM连接：ImtpService::onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        LogHelper.d("IM连接：ImtpService::onRebind()");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        LogHelper.d("IM连接：ImtpService::onTaskRemoved()");
        //super.onTaskRemoved(rootIntent);
        this.keepServiceAlive();
    }

    /**
     * 保持服务
     */
    public void keepServiceAlive() {
        // WIFI保持释放
        try {
            NetworkManager.getInstance().releaseLock();
        } catch (Exception ex) {
            LogHelper.e("IM连接：WIFI保持释放", ex);
        }
        // 内部重启服务
        try {
            //super.onDestroy();
            Intent localIntent = new Intent();
            localIntent.setClass(MyApplication.getInstance(), ImtpService.class); // 销毁时重新启动Service
            MyApplication.getInstance().startService(localIntent);

        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
        }
        // 发送广播启动服务
        try {
            Intent intent = new Intent(AppIndependentConfigure.SYS_CONFIG_APP_PACKAGE + ".rebootservice");
            sendBroadcast(intent);
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
        }
        // 发送广播启动服务
        try {
            Intent intent = new Intent("cn.lds.kill.imtp");
            sendBroadcast(intent);
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 连接IM
     *
     * @param mImConnectOptions
     */
    public void connect(final ImConnectOptions mImConnectOptions) {
        //mImClient = client;
        if (null == mImClient) {
            mImClient = new ImClient(mImConnectOptions);
        } else {
            mImClient.getOptions().setClientId(mImConnectOptions.getClientId());
            mImClient.getOptions().setHost(mImConnectOptions.getHost());
            mImClient.getOptions().setPort(mImConnectOptions.getPort());
            mImClient.getOptions().setToken(mImConnectOptions.getToken());
        }
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mImClient != null) {
                        LogHelper.d("IM连接： ImtpService::connect()");
                        mImClient.connect();
                    } else {
                    }
                } catch (Exception e) {
                    try {
                        throw new Exception(e);
                    } catch (Exception e1) {
                    }
                }

            }
        });
    }

    public void sendMessage(final SendMessage msg) {
        try {
            if (mImClient == null) {
            } else {

                pool.execute(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            mImClient.sendMessage(msg);
                        } catch (Exception e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception e1) {
                            }
                        }
                    }
                });
            }

        } catch (Exception e) {

        }
    }

    public void disConnect() {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mImClient != null) {
                        LogHelper.d("IM连接：ImtpService::disConnect is called");
                        mImClient.disConnect();
                    }
                } catch (Exception e) {
                    try {
                        throw new Exception(e);
                    } catch (Exception e1) {
                    }
                }

            }
        });

    }

    /**
     * 是否连接
     *
     * @return
     */
    public boolean isConnect() {
        try {
            if (mImClient != null) {
                LogHelper.d("IM连接：ImtpService::isConnect: ImClient is not null");
                return mImClient.isConnected();
            } else {
                LogHelper.d("IM连接：ImtpService::isConnect: ImClient is null");
                return false;
            }
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
            return false;
        }
    }

    /**
     * 是否连接中
     *
     * @return
     */
    public boolean isConnecting() {
        try {
            if (mImClient != null) {
                LogHelper.d("IM连接：ImtpService::isConnecting: mImClient is not null");
                return mImClient.isConnecting();
            } else {
                LogHelper.d("IM连接：ImtpService::isConnecting: mImClient is null");
                return false;
            }
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
            return false;
        }
    }

    /**
     * 通知SDK网络变化
     *
     * @return
     */
    public void networkChange() {
        try {
            if (mImClient != null) {
                LogHelper.d("IM连接：ImtpService::通知SDK网络变化");
                mImClient.networkChange();
            } else {
                LogHelper.d("IM连接：ImtpService::mImClient is null");
            }
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
        }
    }


    /**
     * 登录TASK
     */
    public synchronized void startTimer() {
        try {
            LogHelper.d("IM连接：ImtpService::startTimer: was called and isStart=" + isStart);
            if (isStart) {
                return;
            }
            isStart = true;
            if (this.schedule == null) {
                this.schedule = Executors.newScheduledThreadPool(1);
            }

            LogHelper.d("IM连接：ImtpService::startTimer: this.schedule.isShutdown()=" + this.schedule.isShutdown());
            LogHelper.d("IM连接：ImtpManager::startTimer: this.schedule.isTerminated()=" + this.schedule.isTerminated());
            // 判断网络状态、如果是连接上了、执行IM连接动作。
            String msg = "消息跟踪 网络连接状态=" + NetworkManager.getInstance().getState();
            msg += "    IM连接状态 isConnect=" + ImtpManager.getInstance().isConnect();
            msg += "    IM连接状态 isConnecting=" + ImtpManager.getInstance().isConnecting();
            LogHelper.d(msg);
            if (NetworkState.available == NetworkManager.getInstance().getState()) {
//                try{
//                    if(this.schedule != null){
//                        this.schedule.shutdownNow();
//                    }
//                }catch (Exception ex){
//                    LogHelper.e("IM连接：",ex);
//                }
                if (task_common == null) {
                    task_common = new TaskExecutor(TASKEXECUTOR_COMMON);
                }
                LogHelper.d("IM连接：ImtpService::startTimer: 启动timer task_common");
                this.schedule.scheduleAtFixedRate(task_common, 1, defaultTaskInterval, TimeUnit.SECONDS);
            } else {
                isStart = false;
            }
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 停止登录TASK
     */
    public synchronized void stopTimer() {
        try {
            LogHelper.d("IM连接：ImtpService::stopTimer: was called ");
            if (this.schedule != null) {
                LogHelper.d("IM连接：ImtpService::stopTimer: this.schedule.isShutdown()=" + this.schedule.isShutdown());
                LogHelper.d("IM连接：ImtpService::stopTimer: this.schedule.isTerminated()=" + this.schedule.isTerminated());
                //this.schedule.shutdown();
                this.schedule.shutdownNow();
            }
            this.schedule = null;
            isStart = false;
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 开始守护进程
     */
    public synchronized void startProtectTask() {
        try {
            LogHelper.d("IM连接：ImtpService::startProtectTask: was called and isProtectTaskStart=" + isProtectTaskStart);
            if (isProtectTaskStart) {
                return;
            }
            isProtectTaskStart = true;
            if (this.protectTaskschedule == null) {
                this.protectTaskschedule = Executors.newScheduledThreadPool(1);
            }
//            try{
//                if(this.protectTaskschedule != null){
//                    this.protectTaskschedule.shutdownNow();
//                }
//            }catch (Exception ex){
//                LogHelper.e("IM连接：",ex);
//            }
            if (task_protect == null) {
                task_protect = new TaskExecutor(TASKEXECUTOR_PROTECT);
            }
            this.protectTaskschedule.scheduleAtFixedRate(task_protect, 1, defaultProtectTaskInterval, TimeUnit.SECONDS);
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
        }
    }

    /**
     * 停止守护进程
     */
    public synchronized void stopProtectTask() {
        try {
            LogHelper.d("IM连接：ImtpService::stopProtectTask: was called ");
            if (this.protectTaskschedule != null) {
                this.protectTaskschedule.shutdownNow();
            }
            this.protectTaskschedule = null;
            isProtectTaskStart = false;
        } catch (Exception ex) {
            LogHelper.e("IM连接：", ex);
        }
    }

    private class TaskExecutor implements Runnable {
        private final String tastType;

        public TaskExecutor(String type) {
            tastType = type;
        }

        @Override
        public void run() {
            // WIFI保持
            if (!AccountManager.getInstance().isLogin())//非登录状态返回
                return;
            try {
                if (TASKEXECUTOR_PROTECT.equals(tastType)) {
                    NetworkManager.getInstance().acquireLock();
                    VersionManager.getInstance().setSendApiFlag(true);
                }
            } catch (Exception ex) {
                LogHelper.e("IM连接(" + tastType + ")：WIFI保持", ex);
            }
            // 业务处理
            try {
                LogHelper.d("IM连接(" + tastType + ")：service.TaskExecutor is running");
                count++;
                boolean isConnect = isConnect();
                boolean isConnecting = isConnecting();
                LogHelper.d("IM连接(" + tastType + ")：service.isConnect()=" + isConnect);
                LogHelper.d("IM连接(" + tastType + ")：service.isConnecting()=" + isConnecting);
                EventBus.getDefault().post(new IMSdkDebugEvent(isConnect, isConnecting));

                // 判断网络状态、如果是连接上了、执行IM连接动作。
                if (NetworkState.available == NetworkManager.getInstance().getState()) {
                    // 推送通知到UI
                    // IM已连接
                    if (isConnect) {
                        // 隐藏状态栏
                        EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.CONNECTED));
                    }
                    // IM未连接
                    else {
                        // 连接中
                        if (isConnecting) {
                            EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.CONNECTING));
                        }
                        // 连接断开
                        else {
                            EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.DISCONNECTED));
                        }
                    }

                    // 使用IM本身的状态来控制连接。
                    if (!isConnect && !isConnecting) {
                        LogHelper.d("IM连接(" + tastType + ")：定时监控连接状态，连接断开、重新连接");
                        ImtpManager.getInstance().registerDevice(false);
                    } else {
                        if (isConnect) {
                            LogHelper.d("IM连接(" + tastType + ")：定时监控连接状态，连接正常");
                            stopTimer();
                        } else if (isConnecting) {
                            LogHelper.d("IM连接(" + tastType + ")：定时监控连接状态，连接中，不重新发起连接");
                        }
                    }

                    // 每间隔5次检测连接状态，判断是否重置SDK
                    if (count % 5 == 0) {
                        count = 1;
                        if (!isConnect && isConnecting) {
                            networkChange();
                        }
                    }
                }
                // 判断网络状态、如果是未连接、提示断开的黄色条。
                else {
                    EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.DISCONNECTED));
                }
            } catch (Exception ex) {
                LogHelper.e("IM连接(" + tastType + ")：", ex);
            }
        }
    }

    // 进程是否运行
    public boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
//            Log.i(TAG, "运行进程" + info.processName);
            if (info.processName.equals(proessName)) {
                //Log.i("Service1进程", "" + info.processName);
                isRunning = true;
            }
        }

        return isRunning;
    }

}
