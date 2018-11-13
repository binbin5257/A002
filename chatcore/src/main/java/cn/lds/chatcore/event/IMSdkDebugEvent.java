package cn.lds.chatcore.event;

/**
 * Created by quwei on 2016/1/10.
 */
public class IMSdkDebugEvent {
    public boolean isConnect = false;
    public boolean isConnecting = false;

    public IMSdkDebugEvent(boolean isConnect, boolean isConnecting){
        this.isConnect = isConnect;
        this.isConnecting = isConnecting;
    }
    public boolean isConnect() {
        return isConnect;
    }

    public void setIsConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setIsConnecting(boolean isConnecting) {
        this.isConnecting = isConnecting;
    }
}
