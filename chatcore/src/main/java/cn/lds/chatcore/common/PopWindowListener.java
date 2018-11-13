package cn.lds.chatcore.common;

/**
 * Created by pengwb on 2016/2/24.
 * 弹窗监听
 */
public interface PopWindowListener {
    void confirm();

    void cancel();

    void onItemListener(int position);
}
