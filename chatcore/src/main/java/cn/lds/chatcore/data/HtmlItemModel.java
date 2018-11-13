package cn.lds.chatcore.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 位置消息格式化存储
 * 
 * @author user
 * 
 */
public class HtmlItemModel {

	public static final String HTML_TITLE = "title";
	public static final String HTML_DATE = "date";
	public static final String HTML_CONTENT = "content";
	public static final String HTML_IMAGE = "image";
	public static final String HTML_URL = "url";
	public static final String HTML_COUNT = "count";
	/** 标题 */
	private String title;
	/** 日期 */
	private String content;
	/** 概要 */
	private String image;
	/** 提示 */
	private String date;
	/** 链接 */
	private String url;
	/** 内容 */
	private String count;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public HtmlItemModel(String title, String content, String image, String date, String url, String count) {
		this.title = title;
		this.date = date;
		this.content = content;
		this.image = image;
		this.url = url;
		this.count = count;
	}

	/**
	 * content转json串
	 * @param title
	 * @param content
	 * @param image
	 * @param date
	 * @param url
	 * @param count
	 * @return json String
	 */
	public static String createContentJsonStr(String title, String content, String image, String date, String url,
			String count) {
		JSONObject json = new JSONObject();
		try {
			json.put(HTML_TITLE, title);
			json.put(HTML_DATE, date);
			json.put(HTML_CONTENT, content);
			json.put(HTML_IMAGE, image);
			json.put(HTML_URL, url);
			json.put(HTML_COUNT, count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 解析JSON串
	 * 
	 * @param content
	 * @return
	 */
	public static HtmlItemModel parse(String content) {
		HtmlItemModel htemlitemmodel = null;
		try {
			final JSONObject json = new JSONObject(content);
			String contents = "";
			// if(json.getString("count").equals("1")){
			// contents = json.getString("content");
			// }
			htemlitemmodel = new HtmlItemModel(json.getString("title"), contents, json.getString("image"),
					json.getString("date"), json.getString("url"), json.getString("count"));
		} catch (final JSONException e) {
			Log.e("aa", e.getMessage());
		}
		return htemlitemmodel;
	}
}
