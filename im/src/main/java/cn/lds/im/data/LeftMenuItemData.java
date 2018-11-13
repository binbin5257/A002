package cn.lds.im.data;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;

/**
 * 侧滑菜单左侧菜单栏条目数据
 * Created by E0608 on 2017/12/21.
 */

public class LeftMenuItemData {


    public static List<LeftMenuBean> getNotLoggedIn(){
        List<LeftMenuBean> list = new ArrayList<>();
        LeftMenuBean walletBean = new LeftMenuBean();
        walletBean.setId("wallet");
        walletBean.setName("钱包");
        walletBean.setImageRes(R.drawable.bg_submenu_wallet);
        LeftMenuBean activeBean = new LeftMenuBean();
        activeBean.setId("active");
        activeBean.setName("活动");
        activeBean.setImageRes(R.drawable.bg_submenu_event);
        LeftMenuBean tripBean = new LeftMenuBean();
        tripBean.setId("trip");
        tripBean.setName("行程");
        tripBean.setImageRes(R.drawable.bg_submenu_trip);
        LeftMenuBean messageBean = new LeftMenuBean();
        messageBean.setId("message");
        messageBean.setName("消息");
        messageBean.setImageRes(R.drawable.ic_menu_news);
        LeftMenuBean serviceBean = new LeftMenuBean();
        serviceBean.setId("service");
        serviceBean.setName("客服");
        serviceBean.setImageRes(R.drawable.bg_submenu_service);
        LeftMenuBean illegalBean = new LeftMenuBean();
        illegalBean.setId("illegal");
        illegalBean.setName("违章");
        illegalBean.setImageRes(R.drawable.bg_submenu_illegal);
        LeftMenuBean settingBean = new LeftMenuBean();
        settingBean.setId("setting");
        settingBean.setName("设置");
        settingBean.setImageRes(R.drawable.bg_submenu_setting);
        list.add(walletBean);
        list.add(activeBean);
        list.add(tripBean);
        list.add(messageBean);
        list.add(serviceBean);
        list.add(illegalBean);
        list.add(settingBean);
        return list;

    }
    public static List<LeftMenuBean> getNotMarking(){
        List<LeftMenuBean> list = new ArrayList<>();
        LeftMenuBean walletBean = new LeftMenuBean();
        walletBean.setId("wallet");
        walletBean.setName("钱包");
        walletBean.setImageRes(R.drawable.bg_submenu_wallet);
        LeftMenuBean activeBean = new LeftMenuBean();
        activeBean.setId("active");
        activeBean.setName("活动");
        activeBean.setImageRes(R.drawable.bg_submenu_event);
        LeftMenuBean applyBean = new LeftMenuBean();
        applyBean.setId("apply");
        applyBean.setName("申请");
        applyBean.setImageRes(R.drawable.bg_submenu_apply);
        LeftMenuBean tripBean = new LeftMenuBean();
        tripBean.setId("trip");
        tripBean.setName("行程");
        tripBean.setImageRes(R.drawable.bg_submenu_trip);
        LeftMenuBean messageBean = new LeftMenuBean();
        messageBean.setId("message");
        messageBean.setName("消息");
        messageBean.setImageRes(R.drawable.ic_menu_news);
        LeftMenuBean serviceBean = new LeftMenuBean();
        serviceBean.setId("service");
        serviceBean.setName("客服");
        serviceBean.setImageRes(R.drawable.bg_submenu_service);
        LeftMenuBean illegalBean = new LeftMenuBean();
        illegalBean.setId("illegal");
        illegalBean.setName("违章");
        illegalBean.setImageRes(R.drawable.bg_submenu_illegal);
        LeftMenuBean settingBean = new LeftMenuBean();
        settingBean.setId("setting");
        settingBean.setName("设置");
        settingBean.setImageRes(R.drawable.bg_submenu_setting);
        list.add(walletBean);
        list.add(applyBean);
        list.add(activeBean);
        list.add(tripBean);
        list.add(messageBean);
        list.add(serviceBean);
        list.add(illegalBean);
        list.add(settingBean);
        return list;

    }

    public static List<LeftMenuBean> getAllData(){
        List<LeftMenuBean> list = new ArrayList<>();
        LeftMenuBean walletBean = new LeftMenuBean();
        walletBean.setId("wallet");
        walletBean.setName("钱包");
        walletBean.setImageRes(R.drawable.bg_submenu_wallet);
        LeftMenuBean activeBean = new LeftMenuBean();
        activeBean.setId("active");
        activeBean.setName("活动");
        activeBean.setImageRes(R.drawable.bg_submenu_event);
        LeftMenuBean applyBean = new LeftMenuBean();
        applyBean.setId("apply");
        applyBean.setName("申请");
        applyBean.setImageRes(R.drawable.bg_submenu_apply);
        LeftMenuBean markBean = new LeftMenuBean();
        markBean.setId("mark");
        markBean.setName("审批");
        markBean.setImageRes(R.drawable.bg_submenu_marking);
        LeftMenuBean departmentRechargeBean = new LeftMenuBean();
        departmentRechargeBean.setId("recharge");
        departmentRechargeBean.setName("部门充值");
        departmentRechargeBean.setImageRes(R.drawable.bg_department_charge);
        LeftMenuBean tripBean = new LeftMenuBean();
        tripBean.setId("trip");
        tripBean.setName("行程");
        tripBean.setImageRes(R.drawable.bg_submenu_trip);
        LeftMenuBean otherTripBean = new LeftMenuBean();
        otherTripBean.setId("otherTrip");
        otherTripBean.setName("他人行程");
        otherTripBean.setImageRes(R.drawable.bg_submenu_other_trip);
        LeftMenuBean messageBean = new LeftMenuBean();
        messageBean.setId("message");
        messageBean.setName("消息");
        messageBean.setImageRes(R.drawable.ic_menu_news);
        LeftMenuBean serviceBean = new LeftMenuBean();
        serviceBean.setId("service");
        serviceBean.setName("客服");
        serviceBean.setImageRes(R.drawable.bg_submenu_service);
        LeftMenuBean illegalBean = new LeftMenuBean();
        illegalBean.setId("illegal");
        illegalBean.setName("违章");
        illegalBean.setImageRes(R.drawable.bg_submenu_illegal);
        LeftMenuBean settingBean = new LeftMenuBean();
        settingBean.setId("setting");
        settingBean.setName("设置");
        settingBean.setImageRes(R.drawable.bg_submenu_setting);
        list.add(walletBean);
        list.add(applyBean);
        list.add(markBean);
        list.add(departmentRechargeBean);
        list.add(activeBean);
        list.add(tripBean);
        list.add(otherTripBean);
        list.add(messageBean);
        list.add(serviceBean);
        list.add(illegalBean);
        list.add(settingBean);

        return list;

    }

}
