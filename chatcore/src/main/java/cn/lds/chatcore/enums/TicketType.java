package cn.lds.chatcore.enums;

/**
 * 代金卷类型
 * Created by quwei on 2016/1/19.
 */
public enum TicketType {
    UNKNOWN("unknown"),
    SHIYONG("tryout"),// TODO 试用券
    BAOYANG("maintenance"),//保养券
    WEIXIU("repair"),//维修券
    XICHE("wash"),//洗车券
    SHIJIA("testdrive"),//试乘试驾券
    ZHUANCHE("special");//专车券

    private String value = "unknown";

    private TicketType(final String value) {
        this.value = value;
    }

    public static TicketType getValues(String value) {
        switch (value) {
            case "tryout":
                return SHIYONG;
            case "maintenance":
                return BAOYANG;
            case "repair":
                return WEIXIU;
            case "wash":
                return XICHE;
            case "testdrive":
                return SHIJIA;
            case "special":
                return ZHUANCHE;
            default:
                return UNKNOWN;
        }
    }

    public String value() {
        return this.value;
    }
}
