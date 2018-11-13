package cn.lds.chatcore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.http.HttpHandler;

import org.apache.http.client.CookieStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import cn.lds.chatcore.common.ACache;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.SystemConfig;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.BaseManagerInterface;
import cn.lds.chatcore.data.Essential;
import cn.lds.chatcore.data.KeyValueInfo;
import cn.lds.chatcore.data.RevertCarModel;

/**
 * @author user
 */
public class MyApplication extends MultiDexApplication {
    public List<Activity> activityList = new LinkedList<>();
    public static MyApplication instance = null;
    public static String lastCity = null;
    public static DbUtils dbUtils;
    //用户当前位置
    public static LatLng myLocation;
    //地图中心点位置
    public static CookieStore cookieStore;


    private String SERVER_HOST;

    public String getSERVER_HOST() {
        if (ToolsHelper.isNull(SERVER_HOST))
            SERVER_HOST = getCache().getAsString("SERVER_HOST");
        return SERVER_HOST;
    }

    public void setSERVER_HOST(String SERVER_HOST) {
        getCache().put("SERVER_HOST", SERVER_HOST);
        this.SERVER_HOST = SERVER_HOST;
    }

    // 准备退出
    public static boolean isExiting = false;
    // 缓存
    private ACache aCache = null;

    private final ArrayList<Object> registeredManagers;

    private Map<Class<? extends BaseManagerInterface>, Collection<? extends BaseManagerInterface>> managerInterfaces;

    // 后台线程
    private final ExecutorService backgroundExecutor;

    // 主线程
    private final Handler handler;

    // 宽高
    public int width, height;

    //存储还车信息
    public HashMap<String, RevertCarModel> map_RevertCarModel = new HashMap<>();

    public void setRevertCarModel(String key, RevertCarModel revertCarModel) {
        map_RevertCarModel.put(key, revertCarModel);
    }

    public RevertCarModel getRevertCarModel(String key) {
        return map_RevertCarModel.get(key);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /* 登录者基本信息 */
    //public static AccountsTable mAccount = null;

    /* 必要数据加载完成情况 */
    public static Essential essential = Essential.getInstance();

    //public static CookieStore cookieStore;

    //记录文件下载的handler

    public HashMap<String, HttpHandler> handlerDownload = new HashMap<>();
    //记录文件下载的进度

    public HashMap<String, Integer> progressDownload = new HashMap<>();
    private int current = -1;//首页切换

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public static boolean confirmStatus = true;//是否已加入组织
    public static boolean org_cancel_order = false;//是否有组织还车的消息

    public static List<KeyValueInfo> parameter = new ArrayList<>();//打开第三方应用时传递的参数

    // 单例模式中获取唯一的MyApplication实例
    public synchronized static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


    public MyApplication() {
        instance = this;
        handler = new Handler();

        managerInterfaces = new HashMap<>();
        registeredManagers = new ArrayList<>();

        backgroundExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "Background executor service");
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    /**
     * 实例化*manager类
     */
    @Override
    public void onCreate() {
        super.onCreate();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
        height = wm.getDefaultDisplay().getHeight();// 屏幕高度

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        // 地图初始化
        try {
            SDKInitializer.initialize(getApplicationContext());
        } catch (Error e) {
        }
    }


    /**
     * 注册manager
     *
     * @param manager
     */
    public void addManager(Object manager) {
        registeredManagers.add(manager);
    }

    /**
     * 获取manager
     */
    public ArrayList<Object> getManager() {
        return registeredManagers;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseManagerInterface> Collection<T> getManagers(Class<T> cls) {

        Collection<T> collection = (Collection<T>) managerInterfaces.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            for (Object manager : registeredManagers)
                if (cls.isInstance(manager))
                    collection.add((T) manager);
            collection = Collections.unmodifiableCollection(collection);
            managerInterfaces.put(cls, collection);
        }
        return collection;
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    private Activity mActivity;
    private ImageView iv__top;

    public ImageView getIv__top() {
        return iv__top;
    }

    public void setIv__top(ImageView iv__top) {
        this.iv__top = iv__top;
    }

    public void setMainActivity(Activity activity) {
        this.mActivity = activity;
    }

    public Activity getMainActivity() {
        return mActivity;
    }

    /**
     * 遍历所有Activity并finish
     */
    public void exit() {
        try {
            for (Activity activity : activityList) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    /**
     * 提交主线程处理
     *
     * @param runnable
     */
    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }

    /**
     * 提交主线程，延迟后处理
     *
     * @param runnable
     * @param delayMillis
     */
    public void runOnUiThreadDelay(final Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

    /**
     * 获取cache实例
     *
     * @return
     */
    public ACache getCache() {

        if (null != aCache) {
            return aCache;
        }

        aCache = ACache.get(this);

        return aCache;
    }

    /**
     * 首次登录的用户，需要等待个人信息初始化完成
     */
    public void requestEssentialData() {
        //获取个人消息
        CoreHttpApi.enrolleesGet();
        //获取后台系统配置
//        CoreHttpApi.getSystemConfig();
    }


    /**
     * 发送退出应用的广播
     */
    public void sendLogoutBroadcast(String filter, String log) {
        Intent intent = new Intent();
        intent.setAction(AppIndependentConfigure.SYS_CONFIG_APP_PACKAGE + ".logout");
        intent.putExtra("filter", filter);
        intent.putExtra("log", log);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().sendBroadcast(intent);
    }

}
