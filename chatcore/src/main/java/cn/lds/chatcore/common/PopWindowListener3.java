package cn.lds.chatcore.common;

/**
 * Created by pengwb on 2016/2/24.
 * 弹窗监听
 */
public interface PopWindowListener3 {
    void confirm();

    void cancel();

    void failed();//未下载对应客户端，调到内置导航

    void onItemListener(int position);
}
