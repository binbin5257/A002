package cn.lds.im.data;


import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;

/**
 * 左侧菜单栏bean
 * Created by sibinbin on 17-8-8.
 */

public class LeftMenuBean {

    /**
     * 菜单id
     */
    private String id;
    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图片
     */
    private int imageRes;

    /**
     *  菜单是否显示
     */
    private boolean isShow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public List<LeftMenuBean> getMenuBeanList() {
        return menuBeanList;
    }

    public void setMenuBeanList(List<LeftMenuBean> menuBeanList) {
        this.menuBeanList = menuBeanList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    /**
     * 侧滑栏菜单列表数据
     * @return
     */
    private List<LeftMenuBean> menuBeanList = new ArrayList<>();

    public List<LeftMenuBean> getMenuListData() {
        LeftMenuBean walletBean = new LeftMenuBean();
        walletBean.setName("钱包");
        walletBean.setImageRes(R.drawable.bg_submenu_wallet);
        LeftMenuBean activeBean = new LeftMenuBean();
        activeBean.setName("活动");
        activeBean.setImageRes(R.drawable.bg_submenu_event);
        LeftMenuBean tripBean = new LeftMenuBean();
        tripBean.setName("行程");
        tripBean.setImageRes(R.drawable.bg_submenu_trip);
        LeftMenuBean serviceBean = new LeftMenuBean();
        serviceBean.setName("客服");
        serviceBean.setImageRes(R.drawable.bg_submenu_service);
        LeftMenuBean illegalBean = new LeftMenuBean();
        illegalBean.setName("违章");
        illegalBean.setImageRes(R.drawable.bg_submenu_illegal);
        LeftMenuBean settingBean = new LeftMenuBean();
        settingBean.setName("设置");
        settingBean.setImageRes(R.drawable.bg_submenu_setting);
        menuBeanList.add(walletBean);
        menuBeanList.add(activeBean);
        menuBeanList.add(tripBean);
        menuBeanList.add(serviceBean);
        menuBeanList.add(illegalBean);
        menuBeanList.add(settingBean);
        return menuBeanList;
    }
}
