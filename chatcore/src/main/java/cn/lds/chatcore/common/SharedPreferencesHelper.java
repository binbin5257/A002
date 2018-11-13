package cn.lds.chatcore.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import cn.lds.chatcore.MyApplication;

/**
 * 保存系统设置相关的SharedPreferences
 */

public class SharedPreferencesHelper {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String account;
    //是否关闭通知
    private boolean isnotice = true;
    //是否关闭声音
    private boolean issound = true;
    //是否关闭震动
    private boolean isvibration = true;
    //免打扰的起止时间
    private String cacheTimeStart, cacheTimeEnd;
    //字体大小
    private String textViewSize;
    //是否置顶
    private boolean topChat = false;
    //是否免打扰
    private boolean nodisturb = false;
    //是否开启我的足迹
    private boolean isOpenFootMark = false;
    //模式（我的足迹）30 60 180
    private String intervalTime;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesHelper() {
        super();
        sharedPreferences = MyApplication.getInstance().getSharedPreferences("SystemOption", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesHelper(Context context, String account) {
        super();
        this.account = account;
        sharedPreferences = context.getSharedPreferences("SystemOption", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isnotice() {
        isnotice = sharedPreferences.getBoolean("isnotice" + account, true);
        return isnotice;
    }

    public void setIsnotice(boolean isnotice) {
        editor.putBoolean("isnotice" + account, isnotice);
        editor.commit();
    }

    public boolean issound() {
        issound = sharedPreferences.getBoolean("issound" + account, true);
        return issound;
    }

    public void setIssound(boolean issound) {
        editor.putBoolean("issound" + account, issound);
        editor.commit();
    }

    public boolean isvibration() {
        isvibration = sharedPreferences.getBoolean("isvibration" + account, true);
        return isvibration;
    }

    public void setIsvibration(boolean isvibration) {
        editor.putBoolean("isvibration" + account, isvibration);
        editor.commit();
    }

    public String getCacheTimeStart() {
        cacheTimeStart = sharedPreferences.getString("cacheTimeStart" + account, "block");
        return cacheTimeStart;
    }

    public void setCacheTimeStart(String cacheTimeStart) {
        editor.putString("cacheTimeStart" + account, cacheTimeStart);
        editor.commit();
    }

    public String getCacheTimeEnd() {
        cacheTimeEnd = sharedPreferences.getString("cacheTimeEnd" + account, "block");
        return cacheTimeEnd;
    }

    public void setCacheTimeEnd(String cacheTimeEnd) {
        editor.putString("cacheTimeEnd" + account, cacheTimeEnd);
        editor.commit();
    }

    public String getTextViewSize() {
        textViewSize = sharedPreferences.getString("textViewSize" + account, "4");
        return textViewSize;
    }

    public void setTextViewSize(String textViewSize) {
        editor.putString("textViewSize" + account, textViewSize);
        editor.commit();
    }

    public boolean isTopChat() {
        topChat = sharedPreferences.getBoolean("topChat" + account, false);
        return topChat;
    }

    public void setTopChat(boolean topChat) {
        editor.putBoolean("topChat" + account, topChat);
        editor.commit();
    }

    public boolean isNodisturb() {
        nodisturb = sharedPreferences.getBoolean("isvibration" + account, false);
        return nodisturb;
    }

    public void setNodisturb(boolean nodisturb) {
        editor.putBoolean("nodisturb" + account, nodisturb);
        editor.commit();
    }

    public boolean isOpenFootMark() {
        isOpenFootMark = sharedPreferences.getBoolean("isopenfootmark" + account, false);
        return isOpenFootMark;
    }

    public void setIsOpenFootMark(boolean isOpenFootMark) {
        editor.putBoolean("isopenfootmark" + account, isOpenFootMark);
        editor.commit();
    }

    public String getIntervalTime() {
        intervalTime = sharedPreferences.getString("intervalTime" + account, "60");
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        editor.putString("intervalTime" + account, intervalTime);
        editor.commit();
    }
}
