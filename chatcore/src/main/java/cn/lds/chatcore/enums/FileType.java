package cn.lds.chatcore.enums;

public enum FileType {

    UNKNOWN("00")/* 未知 */, IMAGE("01")/* 图片 */, VOICE("02")/* 音频 */, VEDIO("03")/* 视频 */, APK("04")/* apk */, QRCODE("05"), FILE("06");

    private String value = "00";

    private FileType(final String value) {
        this.value = value;
    }

    public static FileType getValue(final String value) {
        switch (value) {
            case "00":
                return UNKNOWN;
            case "01":
                return IMAGE;
            case "02":
                return VOICE;
            case "03":
                return VEDIO;
            case "04":
                return APK;
            case "05":
                return QRCODE;
            case "06":
                return FILE;
            default:
                return UNKNOWN;
        }
    }

    public String value() {
        return this.value;
    }
}
