package cn.lds.chatcore.common;

import android.view.View;

/**
 * 系统配置Helper类
 * Created by quwei on 2016/5/30.
 */
public class SystemConfigHelper {

    /**
     * 视图显示隐藏控制
     * @param view
     * @param isShow
     */
    public static void viewVisibleOrGone(View view,boolean isShow){
        try{
            if(isShow){
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.GONE);
            }
        }catch (Exception ex){
            LogHelper.e("视图控制",ex);
        }
    }

    /**
     * 视图显示隐藏控制
     * @param view
     * @param isShow
     */
    public static void viewVisibleOrInvisible(View view,boolean isShow){
        try{
            if(isShow){
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.INVISIBLE);
            }
        }catch (Exception ex){
            LogHelper.e("视图控制",ex);
        }
    }
}
