/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 * <p/>
 * This file is part of Xabber project; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License, Version 3.
 * <p/>
 * Xabber is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package cn.lds.chatcore.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.enums.NetworkState;
import cn.lds.chatcore.event.ImConnectedEvent;
import de.greenrobot.event.EventBus;

/**
 * Manage network connectivity.
 *
 * @author
 */
public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();

    //private final ConnectivityReceiver connectivityReceiver;

    private ConnectivityManager connectivityManager;

    /**
     * Type of last active network.
     */
    private Integer type;

    /**
     * Whether last active network was suspended.
     */
    private boolean suspended;

    private WifiLock wifiLock;

    private WakeLock wakeLock;

    /**
     * Current network state.
     */
    private NetworkState state;

    private static NetworkManager instance;

//    static {
//        instance = new NetworkManager(MyApplication.getInstance());
//        MyApplication.getInstance().addManager(instance);
//        EventBus.getDefault().register(instance);
//    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            try {
                instance = new NetworkManager();
                MyApplication.getInstance().addManager(instance);
                EventBus.getDefault().register(instance);
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    public void onEventBackgroundThread(ImConnectedEvent event) {
        LogHelper.d(TAG + "ImConnectedEvent is processing");
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        MyApplication.getInstance().registerReceiver(connectivityReceiver, filter);
        this.acquireLock();
    }

    private NetworkManager() {
        //connectivityReceiver = new ConnectivityReceiver();
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        type = getType(active);
        suspended = isSuspended(active);
        initWifiLock();
        initWakeLock();
        state = NetworkState.available;
    }

    /**
     * 打印网络状态日志
     */
    public void printNewWorkStatus() {
        try {
            if (connectivityManager == null) {
                connectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            }
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            LogHelper.d("网络状态信息，networkInfo.getExtraInfo()=" + networkInfo.getExtraInfo());
            LogHelper.d("网络状态信息，networkInfo.isRoaming()=" + networkInfo.isRoaming());
            LogHelper.d("网络状态信息，networkInfo.isFailover()=" + networkInfo.isFailover());
            LogHelper.d("网络状态信息，networkInfo.isConnectedOrConnecting()=" + networkInfo.isConnectedOrConnecting());
            LogHelper.d("网络状态信息，networkInfo.isConnected()=" + networkInfo.isConnected());
            LogHelper.d("网络状态信息，networkInfo.isAvailable()=" + networkInfo.isAvailable());
            LogHelper.d("网络状态信息，networkInfo.getState().name()=" + networkInfo.getState().name());
            LogHelper.d("网络状态信息，networkInfo.getSubtype()=" + networkInfo.getSubtype());
            LogHelper.d("网络状态信息，networkInfo.getSubtypeName()=" + networkInfo.getSubtypeName());
            LogHelper.d("网络状态信息，networkInfo.getReason()=" + networkInfo.getReason());
            LogHelper.d("网络状态信息，networkInfo.getType()=" + networkInfo.getType());
            LogHelper.d("网络状态信息，networkInfo.getTypeName()=" + networkInfo.getTypeName());
            LogHelper.d("网络状态信息，networkInfo.getDetailedState().name()=" + networkInfo.getDetailedState().name());
            if (wifiLock != null) {
                LogHelper.d("网络状态信息，wifiLock.isHeld()=" + wifiLock.isHeld());
            }
            if (wakeLock != null) {
                LogHelper.d("网络状态信息，wakeLock.isHeld()=" + wakeLock.isHeld());
            }
        } catch (Exception ex) {

        }
    }

    /**
     * 初始化wifiLock
     */
    private void initWifiLock() {
        if (wifiLock == null) {
            LogHelper.d("NetworkManager::initWifiLock() sta");
            //        wifiLock = ((WifiManager) application
//                .getSystemService(Context.WIFI_SERVICE)).createWifiLock(
//                WifiManager.WIFI_MODE_FULL, "Lvxin Wifi Lock");
            wifiLock = ((WifiManager) MyApplication.getInstance()
                    .getSystemService(Context.WIFI_SERVICE)).createWifiLock(
                    WifiManager.WIFI_MODE_FULL_HIGH_PERF, "Lvxin Wifi Lock");
            wifiLock.setReferenceCounted(false);
            LogHelper.d("NetworkManager::initWifiLock() end");
        }
    }

    /**
     * 初始化wakeLock
     */
    private void initWakeLock() {
        if (wakeLock == null) {
            LogHelper.d("NetworkManager::initWakeLock() sta");
            wakeLock = ((PowerManager) MyApplication.getInstance()
                    .getSystemService(Context.POWER_SERVICE)).newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, "Lvxin Wake Lock");
            wakeLock.setReferenceCounted(false);
            LogHelper.d("NetworkManager::initWakeLock() end");
        }
    }

    /**
     * @param networkInfo
     * @return Type of network. <code>null</code> if network is
     * <code>null</code> or it is not connected and is not suspended.
     */
    private Integer getType(NetworkInfo networkInfo) {
        if (networkInfo != null
                && (networkInfo.getState() == State.CONNECTED || networkInfo
                .getState() == State.SUSPENDED))
            return networkInfo.getType();
        return null;
    }

    /**
     * @param networkInfo
     * @return <code>true</code> if network is not <code>null</code> and is
     * suspended.
     */
    private boolean isSuspended(NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.getState() == State.SUSPENDED;
    }

    public NetworkState getState() {
        return state;
    }


    //@Override
    public void onClose() {
        //MyApplication.getInstance().unregisterReceiver(connectivityReceiver);
    }

    public void onNetworkChange(NetworkInfo networkInfo) {
        try {
            NetworkInfo active = connectivityManager.getActiveNetworkInfo();
            LogHelper.d(TAG + "Network: " + networkInfo + ", active: " + active);
            Integer type;
            boolean suspended;
            if (active == null && this.type != null
                    && this.type == networkInfo.getType()) {
                type = getType(networkInfo);
                suspended = isSuspended(networkInfo);
            } else {
                type = getType(active);
                suspended = isSuspended(active);
            }
            if (this.type == type) {
                if (this.suspended == suspended)
                    LogHelper.d(TAG + "State does not changed.");
                else if (suspended)
                    onSuspend();
                else
                    onResume();
            } else {
                if (suspended) {
                    type = null;
                    suspended = false;
                }
                if (type == null)
                    onUnavailable();
                else
                    onAvailable(type);
            }
            this.type = type;
            this.suspended = suspended;
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    /**
     * New network is available. Start connection.
     */
    private void onAvailable(int type) {
        try {
            state = NetworkState.available;
            LogHelper.d(TAG + "Available");
            //TODO: 暂时使用闹钟定时监控
            //ImtpManager.getInstance().registerDevice();
            if (type == ConnectivityManager.TYPE_WIFI) {
                //ConnectionManager.getInstance().forceReconnect();
            } else {
                //ConnectionManager.getInstance().updateConnections(false);
            }
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    /**
     * Network is temporary unavailable.
     */
    private void onSuspend() {
        try {
            state = NetworkState.suspended;
            LogHelper.d(TAG + "Suspend");
            // TODO: ConnectionManager.getInstance().pauseKeepAlive();
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    /**
     * Network becomes available after suspend.
     */
    private void onResume() {
        try {
            state = NetworkState.available;
            LogHelper.d(TAG + "Resume");
            //重新连接 TODO: 暂时使用闹钟定时监控
            //ImtpManager.getInstance().registerDevice();
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    /**
     * Network is not available. Stop connections.
     */
    private void onUnavailable() {
        try {
            state = NetworkState.unavailable;
            LogHelper.d(TAG + "Unavailable");
            //ConnectionManager.getInstance().updateConnections(false);
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    public void acquireLock() {
        this.acquireWifiLock();
        this.acquireWakeLock();
    }

    public void releaseLock() {
        this.releaseWifiLock();
        this.releaseWakeLock();
    }

    /**
     * 保持wifi
     */
    private void acquireWifiLock() {
        try {
            if (wifiLock == null) {
                this.initWifiLock();
            }
            wifiLock.acquire();
            LogHelper.d("NetworkManager::acquireWifiLock()");
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    /**
     * 保持唤醒
     */
    private void acquireWakeLock() {
        try {
            if (wakeLock == null) {
                initWakeLock();
            }
            wakeLock.acquire();
            LogHelper.d("NetworkManager::acquireWakeLock()");
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    /**
     * 释放唤醒
     */
    private void releaseWakeLock() {
        try {
            if (wakeLock != null) {
                wakeLock.release();
                LogHelper.d("NetworkManager::releaseWakeLock()");
            }
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    /**
     * 释放wifi
     */
    private void releaseWifiLock() {
        try {
            if (wifiLock != null) {
                wifiLock.release();
                LogHelper.d("NetworkManager::releaseWifiLock()");
            }
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }
}
