package cn.lds.chatcore.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.data.Essential;
import cn.lds.chatcore.imtp.ImtpManager;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.ActivityStackManager;
import cn.lds.chatcore.manager.NotificationManager;

/**
 * 注销
 *
 * @author user
 */
public class LogoutReceiver extends BroadcastReceiver {
    public static String _TAG = LogoutReceiver.class.getSimpleName();
    private Context context;
    private String account;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        account = CacheHelper.getAccount();
        LogHelper.d("数据库共通：收到注销通知！");

        AccountManager.getInstance().logout();
        // 停止IM服务
        ImtpManager.getInstance().stopService();
        // 停止timer
        ImtpManager.getInstance().stopTimer();
        // 清除通知栏
        NotificationManager.getInstance().cancelAll();
        // 向服务器提交注销通知
        unregister();
        // 清除相关缓存
        CacheHelper.logout();
        // 初始化组织相关
        MyApplication.confirmStatus = true;
        MyApplication.org_cancel_order = false;
        try {
            // 清除API请求的cookie
            MyApplication.cookieStore = null;
            CookieManager cookieManager = CookieManager.getInstance();
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            cookieSyncManager.startSync();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();//移除
        } catch (Exception e) {
            LogHelper.e(LogoutReceiver.class.getName(), e);
        }
        // 清除必要数据拉取标志。
        Essential.getInstance().resetStatus();
        // SQLITE在注销时处理部分表数据

        ActivityStackManager.getInstance().finishAllActivity();

        android.app.NotificationManager manger = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manger.cancel(0);

        Intent i = new Intent();
        i.setAction(intent.getStringExtra("filter"));
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("log", intent.getStringExtra("log"));
        context.startActivity(i);


    }

    /**
     * 注销时，通知服务器
     */
    private void unregister() {
//        CoreHttpApi.unregister(context);
//        CoreHttpApi.logout();
    }

    /**
     * 杀掉所有进程。
     *
     * @param packageName
     */
    public static void killProcess(String packageName) {

        String processId = "";
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("ps");
            BufferedReader br = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            String inline;
            while ((inline = br.readLine()) != null) {
                if (inline.contains(packageName)) {
                    break;
                }
            }
            br.close();
            StringTokenizer processInfoTokenizer = new StringTokenizer(inline);
            int count = 0;
            while (processInfoTokenizer.hasMoreTokens()) {
                count++;
                processId = processInfoTokenizer.nextToken();
                if (count == 2) {
                    break;
                }
            }
            r.exec("kill -15 " + processId);
        } catch (IOException ex) {
        }
    }

}
