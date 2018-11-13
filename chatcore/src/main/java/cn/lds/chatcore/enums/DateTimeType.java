package cn.lds.chatcore.enums;

/**
 * Created by zhoupeng on 2016/1/18.
 */
public enum DateTimeType {

    UNKNOWN("unknown"),
    yyyyMMddHHmmss("yyyy-MM-dd HH:mm:ss"),
    yyyyMMddHHmm("yyyy-MM-dd HH:mm"),
    VyyyyMMddHHmm("yyyy年MM月dd日 HH:mm"),
    _yyyyMMdd("yyyy-MM-dd"),
    yyyyMMdd("yyyy.MM.dd"),
    HHmmss("HH:mm:ss"),
    HHmm("HH:mm");

    private String value = "";

    private DateTimeType(final String value) {
        this.value = value;
    }

    public static DateTimeType getValues(final String value) {
        switch (value) {
            case "yyyy-MM-dd HH:mm:ss":
                return yyyyMMddHHmmss;
            case "yyyy年MM月dd日 HH:mm":
                return VyyyyMMddHHmm;
            case "yyyy-MM-dd HH:mm":
                return yyyyMMddHHmm;
            case "yyyy-MM-dd":
                return _yyyyMMdd;
            case "yyyy.MM.dd":
                return yyyyMMdd;
            case "HH:mm:ss":
                return HHmmss;
            case "HHmm":
                return HHmm;
            default:
                return UNKNOWN;
        }
    }

    public String value() {
        return this.value;
    }

}
