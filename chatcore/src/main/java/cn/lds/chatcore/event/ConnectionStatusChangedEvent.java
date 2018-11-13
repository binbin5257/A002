package cn.lds.chatcore.event;

import cn.lds.chatcore.enums.ConnectionStatus;

public class ConnectionStatusChangedEvent {

	public ConnectionStatusChangedEvent(ConnectionStatus connectionStatus){
		this.connectionStatus = connectionStatus;
	}
	private ConnectionStatus connectionStatus;

	public ConnectionStatus getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(ConnectionStatus connectionStatus) {
		this.connectionStatus = connectionStatus;
	}
}
