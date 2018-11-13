package cn.lds.chatcore.manager;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;
import cn.lds.chatcore.common.DateHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.data.MessageNotification;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.db.MessageTable;
import cn.lds.chatcore.enums.DateTimeType;
import cn.lds.chatcore.view.BaseActivity;


/**
 * 通知栏管理器
 *
 * @author user
 */

public class NotificationManager extends AbstractManager {

    /**
     * 震动时长
     */
    protected static final long VIBRATION_DURATION = 500;
    /**
     * 最长提示文本长度
     */
    protected static final int MAX_NOTIFICATION_TEXT = 80;

    protected final Application application;
    protected final android.app.NotificationManager notificationManager;
    protected final Notification persistentNotification;
    protected final Handler handler;
    /**
     * 开始震动的线程
     */
    protected final Runnable startVibro;
    /**
     * 强制停止震动线程
     */
    protected final Runnable stopVibro;
    protected final List<MessageNotification> messageNotifications;
    private long time = 0;
    private boolean isTimeout = true;//超时震动提示
    private final static long TWO_SECOND_TIME = 2000;
    protected static NotificationManager instance;
    private static Context context;
    private static Map<String, Integer> mapNoticeKey;
    private static int intNoticeKey;
//    static {
//        instance = new NotificationManager();
//        MyApplication.getInstance().addManager(instance);
//        context = MyApplication.getInstance().getApplicationContext();
//    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            try {
                instance = new NotificationManager();
                MyApplication.getInstance().addManager(instance);
                context = MyApplication.getInstance().getApplicationContext();
                mapNoticeKey = new HashMap<String, Integer>();
                intNoticeKey = 1;
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    protected NotificationManager() {
        this.application = MyApplication.getInstance();
        notificationManager = (android.app.NotificationManager) application.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        persistentNotification = new Notification();
        handler = new Handler();
        messageNotifications = new ArrayList<MessageNotification>();

        stopVibro = new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(startVibro);
                handler.removeCallbacks(stopVibro);
                ((Vibrator) NotificationManager.this.application.getSystemService(Context.VIBRATOR_SERVICE)).cancel();
            }
        };
        startVibro = new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(startVibro);
                handler.removeCallbacks(stopVibro);
                ((Vibrator) NotificationManager.this.application.getSystemService(Context.VIBRATOR_SERVICE)).cancel();
                ((Vibrator) NotificationManager.this.application.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_DURATION);
                handler.postDelayed(stopVibro, VIBRATION_DURATION);
            }
        };
    }


    /**
     * 弹出通知
     *
     * @param id
     * @param notification
     */
    protected void notify(int id, Notification notification) {
        notificationManager.notify(id, notification);
    }

    public void cancel(int id) {
        notificationManager.cancel(id);
    }

    public void cancelAll() {
        notificationManager.cancelAll();
    }

    /**
     * 普通消息通知
     *
     * @param messageTable
     * @param isvibrate
     * @param issound
     */
    public void updateMessageNotification(int id, MessageTable messageTable, boolean isvibrate, boolean issound) {
        LogHelper.d("消息跟踪（通知调试）：NotificationManager::updateMessageNotification()");
        String user = messageTable.getFromClientId();
        String title = mApplicationContext.getString(R.string.app_name);

        Class<?> c = null;
        try {

            if(messageTable.getMessageType().equals("CREATE_APPLICATION")){
                c = (Class<?>) Class.forName("cn.lds.im.view.MarkingActivity");
            }else{
                c = (Class<?>) Class.forName("cn.lds.im.view.MessageActivity");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (c == null) {
            c = BaseActivity.class;
        }
        List<MessageTable> tables = MessageManager.getInstance().findByNo();
        int count = 0;
        if (null != tables && !tables.isEmpty())
            count = tables.size();
        Intent intent = new Intent(context, c);
        intent.putExtra("contact", user);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Notification notification = getNotification(id, count, isvibrate, issound, title, intent, messageTable);
        //notify(id, notification);
        notify(messageTable.getId(), notification);
    }

    /**
     * 获得配置后的notification对象
     *
     * @param isvibrate 是否振动
     * @param issound   是否响铃
     * @return
     */
    private Notification getNotification(int id, int count, boolean isvibrate, boolean issound, String title, Intent intent, MessageTable messageItem) {
        LogHelper.d("消息跟踪（通知调试）：NotificationManager::getNotification()");
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 提示消息计数器
        String str_count = "";

//        if (count > 1) {
//            str_count = "[" + count + "条]";
//        }

        // 构建提示消息
        String strTicker = title + ": " + messageItem.getTitle();
        String strContentText = str_count + messageItem.getContent();
        String strMemberName = "";

        LogHelper.d("消息跟踪（通知调试）：NotificationManager::getNotification() step1");

        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notification;
        notification = new Notification.Builder(context).setSmallIcon(R.drawable.ic_stat_message_n)
                // 设置状态栏中的小图片，尺寸一般建议在24×24，
                // 这个图片同样也是在下拉状态栏中所显示，
                // 如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmapicon)

                // 设置在status bar上显示的提示文字
                .setTicker(strTicker)
                        // 设置在下拉status bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentTitle(title)
                        // TextView中显示的详细内容
                .setContentText(strContentText)
                        //通知产生的时间，会在通知信息里显示
                .setWhen(System.currentTimeMillis())
                        // 关联PendingIntent
                .setContentIntent(pendingIntent2)
                        // 设置分组
                        //.setGroup(messageItem.getUser())
                        // 设置点击后通知消失
                .setAutoCancel(true)
                        // setOngoing(boolean)设为true,notification将无法通过左右滑动的方式清除
                        //.setOngoing(true)
                        // 在TextView的右方显示的数字，可放大图片看，在最右侧。
                        // 这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .setNumber(1)
                        // 需要注意build()是在API level16及之后增加的，在API11中可以使用getNotificatin()来代替
                .getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        AccountsTable account = AccountManager.getInstance().findByNo();//获取当前用户的基本信息
        boolean isDisturb = account.isEnableNoDisturb();
        boolean isnotify = true;
        //是否在免打扰时间段
        if (isDisturb) {
            int nowTime = Integer.valueOf(DateHelper.getSystemDate(DateTimeType.HHmm).replace(":", ""));
            int startTime = Integer.valueOf(account.getStartTimeOfNoDisturb());
            int endTime = Integer.valueOf(account.getEndTimeOfNoDisturb());

            if (startTime < endTime) {//当天内24时，当前时间在其范围中是不接受提醒
                if (startTime < nowTime && nowTime < endTime) {
                    isnotify = false;
                } else {
                    isnotify = true;
                }
            } else {//隔天，24时，当前时间在其区间内是接受提醒的
                if (endTime < nowTime && nowTime < startTime) {
                    isnotify = true;
                } else {
                    isnotify = false;
                }
            }
        }
        //频繁震动控制，2秒控制
        long tmptime = new Date().getTime();

        if (tmptime - time < TWO_SECOND_TIME) {
            isTimeout = false;
        } else {
            isTimeout = true;
        }
        time = tmptime;

        LogHelper.d("消息跟踪（通知调试）：NotificationManager::getNotification() step4 isTimeout=" + isTimeout);
        LogHelper.d("消息跟踪（通知调试）：NotificationManager::getNotification() step4 isvibrate=" + isvibrate);
        LogHelper.d("消息跟踪（通知调试）：NotificationManager::getNotification() step4 isnotify=" + isnotify);

        if (issound && isTimeout && isnotify) {
            notification.defaults |= Notification.DEFAULT_SOUND;// Notification.DEFAULT_SOUND
        }
        if (isvibrate && isTimeout && isnotify) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }

        return notification;
    }

    /*----------------------------重新封装的通知 消息以外的通知 sta -----------------------------------*/

    /*----------------------------重新封装的通知 消息以外的通知 ends -----------------------------------*/
}
