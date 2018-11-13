package cn.lds.chatcore.data;

import java.util.Map;

/**
 * HTTP请求封装
 */
public class HttpRequestInfo {
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_PUT = "PUT";
	public static final String METHOD_DELETE = "DELETE";
	public static final String METHOD_DOWNLOAD = "DOWNLOAD";
	public static final String METHOD_UPLOAD = "UPLOAD";

	/* 可以带通配符的地址 */
	String url;
	/* HttpHelper中的方法 */
	String method;
	/* URL中的参数对 */
	Map<String, String> params;
	/* 本次请求携带的标志参数，不提交服务器，仅作为本次请求的跟踪处理 */
	Map<String, Object> extras;
	/* json数据体，可为空 */
	String json;
	/* 是否发生session过期错误 */
	boolean sessionError = false;
	/* 发生正常的请求错误 */
	boolean requestError = false;

	/**
	 * 构造函数
	 * @param url
	 * @param method
	 * @param params
	 * @param json
	 * @param extras
	 */
	public HttpRequestInfo(String url, String method, Map<String, String> params, String json, Map<String, Object> extras) {
		super();
		this.url = url;
		this.method = method;
		this.params = params;
		this.json = json;
		this.extras = extras;
	}

	/**
	 * session过期等发生时，刷新完token后，重新发送
	 */
	public void resend(){
		
	}

	public Map<String, Object> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}

	public boolean isSessionError() {
		return sessionError;
	}

	public void setSessionError(boolean sessionError) {
		this.sessionError = sessionError;
	}

	public boolean isRequestError() {
		return requestError;
	}

	public void setRequestError(boolean requestError) {
		this.requestError = requestError;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		sb.append("|");
		sb.append(method);
		return sb.toString();
	}

}
