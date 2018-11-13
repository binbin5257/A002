package cn.lds.chatcore.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 视频消息格式化存储
 * 
 * @author user
 * 
 */
public class VideoItemModel {

	public static final String VIDEO_NAME = "filename";
	public static final String VIDEO_SIZE = "filesize";
	public static final String VIDEO_URL_FIRST_FRAME = "frameurl";
	public static final String VIDEO_URL_FILE = "fileurl";

	/** 视频文件名称 */
	private String filename;
	/** 视频文件大小 */
	private long filesize;
	/** 缩略图url */
	private String frameurl;
	/** 视频url */
	private String fileurl;

	public VideoItemModel(String filename, long filesize, String frameurl, String fileurl) {
		super();
		this.filename = filename;
		this.filesize = filesize;
		this.frameurl = frameurl;
		this.fileurl = fileurl;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getFrameurl() {
		return frameurl;
	}

	public void setFrameurl(String frameurl) {
		this.frameurl = frameurl;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public static String createContentJsonStr(String filename, long filesize, String frameurl, String fileurl) {
		JSONObject json = new JSONObject();
		try {
			json.put(VIDEO_NAME, filename);
			json.put(VIDEO_SIZE, filesize);
			json.put(VIDEO_URL_FIRST_FRAME, frameurl);
			json.put(VIDEO_URL_FILE, fileurl);
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
	public static VideoItemModel parse(String content) {
		VideoItemModel videoItemModel = null;
		try {
			JSONObject json = new JSONObject(content);
			videoItemModel = new VideoItemModel(json.optString(VIDEO_NAME), json.optLong(VIDEO_SIZE),
					json.optString(VIDEO_URL_FIRST_FRAME), json.optString(VIDEO_URL_FILE));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return videoItemModel;
	}

	public static String getFileName(String content) {

		return parse(content).getFilename();
	}

	public static String getFrameurl(String content) {
		return parse(content).getFrameurl();
	}

	public static long getFilesize(String content) {
		return parse(content).getFilesize();
	}

	public static String getFileurl(String content) {
		return parse(content).getFileurl();
	}
}
