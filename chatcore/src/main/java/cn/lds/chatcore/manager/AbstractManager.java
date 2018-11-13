package cn.lds.chatcore.manager;

import android.content.Context;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.data.RegistModel;

/**
 * 抽象管理类
 * 业务管理类的基类
 * Created by suncf on 2015/11/11.
 */
public abstract class AbstractManager {

    /* 应用上下文 */
    public Context mApplicationContext = MyApplication.getInstance().getApplicationContext();


}
