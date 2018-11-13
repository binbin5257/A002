package cn.lds.chatcore.data;

public class PublicCancelModel {

	private String appId = null;
	private String appName = null;
	private String portrait = null;

	public PublicCancelModel(String appId, String appName, String portrait) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.portrait = portrait;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

}
