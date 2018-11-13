package cn.lds.chatcore.manager;

import com.lidroid.xutils.util.LogUtils;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.SystemConfigModel;
import cn.lds.chatcore.event.HttpRequestEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by quwei on 2015/12/24.
 */
public class SystemManager extends AbstractManager {
    public static String _TAG = SystemManager.class.getSimpleName();
    protected static SystemManager instance;


    public static SystemManager getInstance() {
        if (instance == null) {
            try {
                instance = new SystemManager();
                MyApplication.getInstance().addManager(instance);
                EventBus.getDefault().register(instance);
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }


    /**
     * API请求处理
     *
     * @param event
     */
    public void onEventBackgroundThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();

        if (!CoreHttpApiKey.getSystemConfig.equals(apiNo))
            return;
        SystemConfigModel s = GsonImplHelp.get().toObject(httpResult.getResult(), SystemConfigModel.class);

        if (null != s && s.getData() != null) {
            CacheHelper.setPickUpTime(s.getData().getInstantPickedUpTimeLimit());
            CacheHelper.setServiceTel(s.getData().getServiceTel());

//            Constants.setScheduledAutoCancelMinute(s.getData().getScheduledAutoCancelMinute());
//            Constants.setMinMinuteScheduledInterval(s.getData().getMinMinuteScheduledInterval());
//            Constants.setInstantAutoCancelMinute(s.getData().getInstantAutoCancelMinute());
//            LogUtils.d(s.getData().toString());
        }
    }

}
