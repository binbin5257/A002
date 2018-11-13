package cn.lds.chatcore.common;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

public class StringHelper {

	public static String getBestName(String name) {
		return name;
	}

	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (null == str || "".equals(str) || " ".equals(str)
				|| "null".equals(str)) {
			return true;
		}
		return false;
	}
	
	@SuppressLint("DefaultLocale")
	public static boolean isUrl(String url) {
		if (url != null
				&& (url.trim().toLowerCase().startsWith("http://") || url
						.trim().toLowerCase().startsWith("https://"))) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 将列表转换成字符串
	 * 
	 * @param list
	 * @return
	 */
	public static String listToStr(List<String> list) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				str = list.get(i);
			} else {
				str += "|" + list.get(i);
			}
		}
		return str;
	}

	/**
	 * 将”|“组合的字符串解析成List组数
	 * @param str 
	 * @return
	 */
	public static List<String> strToList(String str) {
		String[] strings =  str.split("\\|");
		List<String> list = new ArrayList<>();
		for (String s: strings){
			list.add(s);
		}
		return list;
	}

	public static String toStringFromDouble(double d) {
		return String.valueOf(d).toString();
	}
}
