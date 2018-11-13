package cn.lds.chatcore.common;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.enums.DateTimeType;
import cn.lds.chatcore.enums.FileType;
import cn.lds.chatcore.event.FileDownloadErrorEvent;
import cn.lds.chatcore.event.FileDownloadedEvent;
import cn.lds.chatcore.event.FileDownloadingEvent;
import cn.lds.chatcore.event.FileUploadErrorEvent;
import cn.lds.chatcore.event.FileUploadedEvent;
import cn.lds.chatcore.event.FileUploadingEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.imtp.ImtpManager;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.NetworkManager;
import de.greenrobot.event.EventBus;


/**
 * 网络请求帮助类
 *
 * @author user
 */
public class HttpHelper {

    public final static String TAG = "HttpHelper";

    private static HttpUtils httpInstance = null;

    private static boolean isAutoLoginToken = true;

    /**
     * 默认超时时间 10秒
     */
    private static int TIME_OUT = 15000;

    public static HttpUtils getHttpInstance() {
        if (httpInstance == null) {
            httpInstance = new HttpUtils(10000);
            httpInstance.configDefaultHttpCacheExpiry(0);
            httpInstance.configCurrentHttpCacheExpiry(0);
            httpInstance.configTimeout(TIME_OUT);
            httpInstance.configSoTimeout(TIME_OUT);
            httpInstance.configRequestThreadPoolSize(30);
        }
        if (AccountManager.getInstance().isLogin())
            httpInstance.configCookieStore(MyApplication.cookieStore);
        else
            httpInstance.configCookieStore(null);
        return httpInstance;
    }

    /**
     * GET 请求
     *
     * @param url   API地址
     * @param apiNo API编号
     */
    public static void get(final String url, final String apiNo) {
        get(url, apiNo, null, null);
    }

    /**
     * GET 请求
     *
     * @param url    API地址
     * @param apiNo  API编号
     * @param extras 跟踪参数
     */
    public static void get(final String url, final String apiNo, final Map<String, String> extras) {
        get(url, apiNo, null, extras);
    }

    /**
     * GET 请求
     *
     * @param url    API地址
     * @param params URL参数
     * @param apiNo  API编号
     * @param extras 跟踪参数
     */
    public static void get(final String url, final String apiNo, final Map<String, String> params, final Map<String, String> extras) {
        LogHelper.d(String.format("%s", url));
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("organizationId", CacheHelper.getOrganizationId());
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                rp.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        getHttpInstance().send(HttpMethod.GET, url, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                 successHandler(url, apiNo, params, extras, responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                failureHandler(url, apiNo, params, extras, error, msg);
            }
        });

    }

    /**
     * POST 请求
     *
     * @param url   API地址
     * @param apiNo API编号
     */
    public static void post(final String url, final String apiNo) {
        post(url, apiNo, null, null, null);
    }

    /**
     * POST 请求
     *
     * @param url      API地址
     * @param apiNo    API编号
     * @param jsonData JSON数据
     */
    public static void post(final String url, final String apiNo, String jsonData) {
        post(url, apiNo, jsonData, null, null);
    }

    /**
     * POST 请求
     *
     * @param url      API地址
     * @param apiNo    API编号
     * @param jsonData JSON数据
     * @param extras   跟踪参数
     */
    public static void post(final String url, final String apiNo, String jsonData, final Map<String, String> extras) {
        post(url, apiNo, jsonData, null, extras);
    }

    /**
     * POST 请求
     *
     * @param url    API地址
     * @param params URL参数
     * @param apiNo  API编号
     * @param extras 跟踪参数
     */
    public static void post(final String url, final String apiNo, final Map<String, String> params, final Map<String, String> extras) {
        post(url, apiNo, null, params, extras);
    }

    /**
     * POST 请求
     *
     * @param url      API地址
     * @param params   URL参数
     * @param apiNo    API编号
     * @param jsonData JSON数据
     * @param extras   跟踪参数
     */
    public static void post(final String url, final String apiNo, String jsonData, final Map<String, String> params, final Map<String, String> extras) {
        LogHelper.d(String.format("%s\n%s", url, jsonData));
        RequestParams rp = new RequestParams();
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                rp.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }

        try {
            JSONObject d = null;
            if (null != jsonData) {
                d = new JSONObject(jsonData);

            } else {
                d = new JSONObject();
            }
            try {
                d.put("organizationId", CacheHelper.getOrganizationId());
                rp.setBodyEntity(new StringEntity(d.toString(), "utf-8"));
                rp.addHeader("Content-Type", "application/json;charset=utf-8");
                //rp.addHeader();
            } catch (UnsupportedEncodingException e) {
                //TODO: 日志
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        getHttpInstance().send(HttpMethod.POST, url, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                successHandler(url, apiNo, params, extras, responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                failureHandler(url, apiNo, params, extras, error, msg);
            }
        });
    }

    /**
     * PUT请求
     *
     * @param url   API地址
     * @param apiNo API编号
     */
    public static void put(final String url, final String apiNo) {
        put(url, apiNo, null, null, null);
    }

    /**
     * PUT请求
     *
     * @param url      API地址
     * @param apiNo    API编号
     * @param jsonData JSON数据
     */
    public static void put(final String url, final String apiNo, final String jsonData) {
        put(url, apiNo, jsonData, null, null);
    }

    /**
     * PUT请求
     *
     * @param url    API地址
     * @param apiNo  API编号
     * @param params URL参数
     */
    public static void put(final String url, final String apiNo, final Map<String, String> params) {
        put(url, apiNo, null, params, null);
    }

    /**
     * PUT请求
     *
     * @param url      API地址
     * @param apiNo    API编号
     * @param jsonData JSON数据
     * @param extras   跟踪参数
     */
    public static void put(final String url, final String apiNo, final String jsonData, final Map<String, String> extras) {
        put(url, apiNo, jsonData, null, extras);
    }

    /**
     * PUT请求
     *
     * @param url    API地址
     * @param params URL参数
     * @param apiNo  API编号
     * @param extras 跟踪参数
     */
    public static void put(final String url, final String apiNo, final Map<String, String> params, final Map<String, String> extras) {
        put(url, apiNo, null, params, extras);
    }

    /**
     * PUT请求
     *
     * @param url      API地址
     * @param params   URL参数
     * @param apiNo    API编号
     * @param jsonData JSON数据
     * @param extras   跟踪参数
     */
    public static void put(final String url, final String apiNo, String jsonData, final Map<String, String> params, final Map<String, String> extras) {
        LogHelper.d(String.format("%s\n%s", url, jsonData));
        RequestParams rp = new RequestParams();
//        rp.addQueryStringParameter("organizationId", CacheHelper.getOrganizationId());
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                rp.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        try {
            JSONObject d = null;
            if (null != jsonData) {
                d = new JSONObject(jsonData);

            } else {
                d = new JSONObject();
            }
            try {
                d.put("organizationId", CacheHelper.getOrganizationId());
                rp.setBodyEntity(new StringEntity(d.toString(), "utf-8"));
                rp.addHeader("Content-Type", "application/json;charset=utf-8");
                //rp.addHeader();
            } catch (UnsupportedEncodingException e) {
                //TODO: 日志
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getHttpInstance().send(HttpMethod.PUT, url, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                successHandler(url, apiNo, params, extras, responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                failureHandler(url, apiNo, params, extras, error, msg);
            }
        });
    }

    /**
     * DELETE 请求
     *
     * @param url   API地址
     * @param apiNo API编号
     */
    public static void delete(final String url, final String apiNo) {
        delete(url, apiNo, null, null, null);
    }

    /**
     * DELETE 请求
     *
     * @param url    API地址
     * @param apiNo  API编号
     * @param extras 跟踪参数
     */
    public static void delete(final String url, final String apiNo, final Map<String, String> extras) {
        delete(url, apiNo, null, null, extras);
    }

    /**
     * DELETE 请求
     *
     * @param url    API地址
     * @param params URL参数
     * @param apiNo  API编号
     * @param extras 跟踪参数
     */
    public static void delete(final String url, final String apiNo, Map<String, String> params, final Map<String, String> extras) {
        delete(url, apiNo, null, params, extras);
    }

    /**
     * DELETE 请求
     *
     * @param url      API地址
     * @param apiNo    API编号
     * @param jsonData JSON数据
     * @param extras   跟踪参数
     */
    public static void delete(final String url, final String apiNo, final String jsonData, final Map<String, String> extras) {
        delete(url, apiNo, jsonData, null, extras);
    }

    /**
     * DELETE 请求
     *
     * @param url      API地址
     * @param params   URL参数
     * @param apiNo    API编号
     * @param jsonData JSON数据
     * @param extras   跟踪参数
     */
    public static void delete(final String url, final String apiNo, final String jsonData, final Map<String, String> params, final Map<String, String> extras) {
        LogHelper.d(String.format("%s\n%s", url, jsonData));
        RequestParams rp = new RequestParams();
//        rp.addQueryStringParameter("organizationId", CacheHelper.getOrganizationId());
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                rp.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        try {
            JSONObject d = null;
            if (null != jsonData) {
                d = new JSONObject(jsonData);

            } else {
                d = new JSONObject();
            }
            try {
                d.put("organizationId", CacheHelper.getOrganizationId());
                rp.setBodyEntity(new StringEntity(d.toString(), "utf-8"));
                rp.addHeader("Content-Type", "application/json;charset=utf-8");
                //rp.addHeader();
            } catch (UnsupportedEncodingException e) {
                //TODO: 日志
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getHttpInstance().send(HttpMethod.DELETE, url, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                successHandler(url, apiNo, params, extras, responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                failureHandler(url, apiNo, params, extras, error, msg);
            }
        });

    }

    /**
     * @param url
     */
    public static void download(final String fileStorageId, final String url, final FileType fileType, final String owner, String strFileName) {
        LogHelper.d(String.format("http download start: ,%s", url));
        String sdcardPath = FileHelper.getSaveFilePath(fileType);
        String spliter = "fileSaveName=";
        String filename;
        if (ToolsHelper.isNull(strFileName)) {
            filename = url.substring(url.indexOf(spliter) + spliter.length());
        } else {
            filename = strFileName;
        }
        HttpHandler handler = getHttpInstance().download(url, sdcardPath + filename, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        if (current < total) {
                            long n = total / 100;
                            int p = (int) (current / n);
                            MyApplication.getInstance().progressDownload.put(owner, p);
                            EventBus.getDefault().post(new FileDownloadingEvent(fileStorageId, fileType, owner, p));
                        } else if (current == total) {
                            MyApplication.getInstance().progressDownload.put(owner, 100);
                            EventBus.getDefault().post(new FileDownloadingEvent(fileStorageId, fileType, owner, 100));
                        }
                        LogHelper.d(String.format("http download progress: %s,%s", current, total));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        MyApplication.getInstance().progressDownload.remove(owner);
                        LogHelper.d(String.format("http download success: ,%s, result:%n%s", url, responseInfo.result));
                        EventBus.getDefault().post(new FileDownloadedEvent(fileStorageId, fileType, owner, responseInfo.result.getPath()));
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MyApplication.getInstance().progressDownload.remove(owner);
                        LogHelper.e(String.format("http download error: ,%s, result:%n%s", url, msg));
                        EventBus.getDefault().post(new FileDownloadErrorEvent(fileStorageId, owner, fileType, msg));
                    }
                });
        MyApplication.getInstance().handlerDownload.put(owner, handler);

    }

    /**
     * 上传文件，应用中不会直接调用，在FileManger中调用
     * uploadUrl和apiNo应该没用
     *
     * @param path
     */
    public static void upload(String uploadUrl, final String path, final Map<String, String> extras) {

        File file = new File(path);
        upload(uploadUrl, file, extras);
    }

    /**
     * 上传文件
     *
     * @param uploadUrl 上传路径
     * @param file      上传文件
     * @param extras    uploadType|messageId
     */
    public static void upload(final String uploadUrl, final File file, final Map<String, String> extras) {
        LogHelper.d(String.format("%s", uploadUrl));
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("organizationId", CacheHelper.getOrganizationId());
        params.addBodyParameter("file", file);

        //HttpUtils http = new HttpUtils();
        getHttpInstance().send(HttpMethod.POST, uploadUrl, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (current < total) {
                    long n = total / 100;
                    int p = (int) (current / n);
                    EventBus.getDefault().post(new FileUploadingEvent(extras.get("owner"), extras.get("filePath"), p));
                } else if (current == total) {
                    EventBus.getDefault().post(new FileUploadingEvent(extras.get("owner"), extras.get("filePath"), 100));
                }
                LogHelper.d(String.format("http upload progress: ,%s", current));
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogHelper.d(String.format("%s\n%s", uploadUrl, responseInfo.result));

                String owner = extras.get("owner");
                String filePath = extras.get("filePath");
                LogHelper.d(String.format("extra params is: owner=%s, filePath=%s", owner, filePath));

//                    JSONObject json = new JSONObject(responseInfo.result);
                FileUploadedEvent fileUploadedEvent = new FileUploadedEvent(owner, filePath, responseInfo.result);
//                    fileUploadedEvent.setJsonResult(json);

                //发送事件总线
                EventBus.getDefault().post(fileUploadedEvent);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LogHelper.e(String.format("%s\n%s", uploadUrl, msg));

                String owner = extras.get("owner");
                String filePath = extras.get("filePath");
                LogHelper.e(String.format("extra params is: owner=%s, filePath=%s", owner, filePath));

                FileUploadErrorEvent fileUploadErrorEvent = new FileUploadErrorEvent(owner, filePath, msg);

                //发送事件总线
                EventBus.getDefault().post(fileUploadErrorEvent);
            }
        });
    }

    /**
     * CURD 请求成功回调
     *
     * @param url
     * @param params
     * @param apiNo
     * @param extras
     * @param responseResult
     */
    private static void successHandler(final String url, final String apiNo, Map<String, String> params, final Map<String, String> extras, String responseResult) {
        LogHelper.d(String.format("%s\n%s", url, responseResult));
        //登录后，存储cookie
        if (CoreHttpApiKey.login.equals(apiNo)) {
            DefaultHttpClient dh = (DefaultHttpClient) httpInstance.getHttpClient();
            MyApplication.cookieStore = dh.getCookieStore();
            isAutoLoginToken = true;
        }
        HttpResult httpResult = new HttpResult(apiNo, url, responseResult, extras);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(responseResult);
            httpResult.setJsonResult(jsonObject);
            if ("failure".equals(jsonObject.getString("status"))) {
                httpResult.setError(true);
                JSONArray errors = jsonObject.getJSONArray("errors");
                String errcode = "";
                for (int i = 0; i < errors.length(); i++) {
                    String code = errors.getJSONObject(i).optString("errcode");
                    if ("nonceCode.auth.fail".equals(code) || "403".equals(code) || "402".equals(code)) {
                        errcode = code;
                        break;
                    }
                }

                if ("nonceCode.auth.fail".equals(errcode)) {
                    //TODO: 怎么办？

                    LogHelper.d("退出登录：IM收到kicked消息。账号在别处登录！");
                    CacheHelper.setImKickedTime(DateHelper.getSystemDate(DateTimeType.yyyyMMddHHmm));
                    MyApplication.getInstance().sendLogoutBroadcast(Constants.getCoreUrls().getKickedFilter(), "");
                    ImtpManager.getInstance().stopTimer();
//                    EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.KICKED));

                }
            }
        } catch (JSONException e) {
            LogHelper.d(String.format("%s:%s:%s", HttpHelper.class.getName(), "get", responseResult));
        }
        if (!httpResult.isError()) {
            EventBus.getDefault().post(new HttpRequestEvent(httpResult));
        } else {
            EventBus.getDefault().post(new HttpRequestErrorEvent(httpResult));
        }
    }

    /**
     * CURD 请求失败回调
     *
     * @param url
     * @param params
     * @param apiNo
     * @param extras
     * @param error
     * @param msg
     */
    private static void failureHandler(final String url, final String apiNo, Map<String, String> params, final Map<String, String> extras, HttpException error, String msg) {
        LogHelper.e(String.format("%s\n%s", url, msg));
        try {
            if (401 == error.getExceptionCode() || 403 == error.getExceptionCode()) {
                if (CoreHttpApiKey.login.equals(apiNo)) {
                    // 判断是不是后台登录。
                    boolean background = Boolean.valueOf(extras.get("background"));
                    if (background) {
                        // 退出到登录画面
                        LogHelper.e("HttpHelper.failureHandler: 一次性登录token超时");
                        MyApplication.getInstance().sendLogoutBroadcast(Constants.getCoreUrls().getLogoutFilter(), "一次性登录token超时");
                    }
                } else {
                    LogHelper.e("HttpHelper.failureHandler: 使用一次性登录token登录");
                    if (isAutoLoginToken) {
                        isAutoLoginToken = false;
                        AccountManager.getInstance().autoLoginWithNonceToken();
                    }
                }
            } else {
                if (CoreHttpApiKey.login.equals(apiNo)) {
                    isAutoLoginToken = true;
                }
                LogHelper.e("HttpHelper.failureHandler: error.getExceptionCode()=" + error.getExceptionCode());
                LogHelper.e("HttpHelper.failureHandler: apiNo=" + apiNo);
                LogHelper.e("HttpHelper.failureHandler: NetworkManager.getInstance().getState()=" + NetworkManager.getInstance().getState());
            }
        } catch (Exception ex) {
            LogHelper.e("failureHandler", ex);
        }
        HttpResult httpResult = new HttpResult(apiNo, url, msg, extras);
        httpResult.setError(true);
        httpResult.setErrorCode(error.getExceptionCode());
        HttpRequestErrorEvent httpRequestErrorEvent = new HttpRequestErrorEvent(httpResult);
        httpRequestErrorEvent.setException(error);
        EventBus.getDefault().post(httpRequestErrorEvent);
    }

}
