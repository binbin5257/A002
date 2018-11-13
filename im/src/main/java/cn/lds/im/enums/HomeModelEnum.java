package cn.lds.im.enums;

/**
 * 首页模式选择枚举类
 * Created by E0608 on 2017/11/7.
 */

public enum HomeModelEnum {
    MODEBOOK("预约用车",0),MODEUSUAL("立即用车",1),CHARGINGPILE("充电站",2),BUSINESSUSERCAR("公务用车",3),PRIVATEUSECAR("私人用车",4);

    private String name;
    private int index;
    HomeModelEnum(String name, int index){
        this.name = name;
        this.index = index;
    }

    public static String getName(int index){
        for(HomeModelEnum h : HomeModelEnum.values()){
            if(h.index == index){
                return h.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
