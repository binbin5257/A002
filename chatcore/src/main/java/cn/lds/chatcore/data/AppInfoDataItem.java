package cn.lds.chatcore.data;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chatcore.R;


public class AppInfoDataItem {

	private static final String officeUrl = "http://eucp.dhc.com.cn:8087/mo/a/do?p_name=ss_mobile&p_action=1&operation=mobile_show_front_page";
	private static final String publicUrl = "http://112.124.23.78:9090/eucp/public/index.html";
	// private static final String crmUrl = "http://219.149.12.140:8084/crm";

	public int id;
	public String appName;
	public int appIconResId;
	public String appUrl;

	public AppInfoDataItem(int id, String appName, int iconResId, String url) {
		this.id = id;
		this.appName = appName;
		this.appIconResId = iconResId;
		this.appUrl = url;

	}

	public static List<AppInfoDataItem> createAList() {
		List<AppInfoDataItem> list = new ArrayList<AppInfoDataItem>();

		list.add(new AppInfoDataItem(2, "扫一扫", R.drawable.icon_saoyisao, null));
		list.add(new AppInfoDataItem(3, "摇一摇", R.drawable.icon_yaoyiyao, null));
		// list.add(new AppInfoDataItem(4, "地图", R.drawable.icon_map, null));
		list.add(new AppInfoDataItem(5, "应用", R.drawable.icon_yingyong, null));
		return list;
	}

}
