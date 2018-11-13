package cn.lds.im.view;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewClient;
import org.apache.cordova.engine.SystemWebViewEngine;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.GraphicHelper;
import cn.lds.chatcore.common.HttpHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.view.BaseCordovaActivity;

public class WebViewHelpActivity extends BaseCordovaActivity {
    public static final String START_URL = "file:///android_asset/www/index.html";

    protected TextView top_title_tv;
    protected Button top_back_btn;
    protected ImageView mService;

    protected Context mContext;

    protected ImageView cordova_err;
    protected Cookie cookie = null;

    protected int layoutID = R.layout.activity_cordova_webview;
    protected WebViewHelpActivity activity;
    private String url;

    protected void setActivity(WebViewHelpActivity activity) {
        this.activity = activity;
    }
    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        initConfig();
    }

    protected void initConfig() {
        mContext = WebViewHelpActivity.this;

        top_title_tv = (TextView) findViewById(R.id.top_title_tv);
        top_back_btn = (Button) findViewById(R.id.top_back_btn);
        cordova_err = (ImageView) findViewById(R.id.cordova_err);
        mService = (ImageView)findViewById(R.id.top_menu_btn_del);
        top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
            }
        });
        super.init();
        url = getIntent().getStringExtra("url");
//        String url = "";
        if (ToolsHelper.isNull(url)) {
            url = START_URL;
        }
//        startLoadUrl(url);
        webView.setWebChromeClient(new MyWebChromeClient((SystemWebViewEngine) appView.getEngine()));
        webView.setWebViewClient(new MyWebViewClient((SystemWebViewEngine) appView.getEngine()));
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setTextZoom(100);
        super.loadUrl(url);
        syncCookie();
        initData();
    }

    public class MyWebViewClient extends SystemWebViewClient {

        public MyWebViewClient(SystemWebViewEngine parentEngine) {
            super(parentEngine);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebView.HitTestResult hit = view.getHitTestResult();
            int hitType = hit.getType();
            if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE) {//点击超链接
                //这里执行自定义的操作
//                finish();
                return true;//返回true浏览器不再执行默认的操作
            }else if(hitType == 0){//重定向时hitType为0
//                Toast.makeText(mContext,"网络开小差了...",Toast.LENGTH_SHORT).show();
//                WebViewHelpActivity.this.finish();
                return false;//不捕获302重定向
            }else{
                Toast.makeText(mContext,"网络开小差了...",Toast.LENGTH_SHORT).show();
                WebViewHelpActivity.this.finish();
                return false;
            }
        }

        @Override
        public void onPageStarted(WebView view, final String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    CookieSyncManager.createInstance(getApplicationContext());

                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
//                    String oldCookie = cookieManager.getCookie(Constants.getCoreUrls().H5_SERVER_HOST());
                    handler.sendEmptyMessage(getInputStream(url, ""));

                }
            }).start();


        }
    }

    public int getInputStream(String urlPath, String oldCookie) {
        int code = -1000;
        try {
            //参数拼接在urlPath中
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("Charsert", "utf-8");
//            conn.setRequestProperty("Cookie", oldCookie);
            code = conn.getResponseCode();
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return code;
    }

    public void startLoadUrl(final String url) {


        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain="
                + cookie.getDomain();
        cookieManager.setCookie(url, cookieString);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    protected void initData() {
        String appName = getIntent().getStringExtra("title");
        top_title_tv.setText(appName);
        top_back_btn.setVisibility(View.VISIBLE);
        if(getString(R.string.btn_submenu_customerservice).equals(appName)
                || getString(R.string.identifying_help_detail).equals(appName)
                || getString(R.string.settingactivity_useualproblem).equals(appName)
                || getString(R.string.settingactivity_customerrule).equals(appName)
                || getString(R.string.eventactivity_detail).equals(appName)){
            mService.setVisibility(View.VISIBLE);
            mService.setImageResource(R.drawable.ic_phone);
        }else {
            mService.setVisibility(View.GONE);
        }
    }


    public class MyWebChromeClient extends SystemWebChromeClient {

        public MyWebChromeClient(SystemWebViewEngine parentEngine) {
            super(parentEngine);
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            // TODO Auto-generated method stub
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {

            super.onReceivedIcon(view, icon);

        }


        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {

            callback.invoke(origin, true, false);

            super.onGeolocationPermissionsShowPrompt(origin, callback);

        }

        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathsCallback, FileChooserParams fileChooserParams) {
            openPop(filePathsCallback);
            return true;
        }

        @Override
        public void openFileChooser(final ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openPops(uploadMsg);
        }
    }

    protected void openPops(final ValueCallback<Uri> uploadMsg) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_type_file, null);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
//        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(findViewById(R.id.top__iv));

        LinearLayout bg = (LinearLayout) contentView.findViewById(R.id.type_file_pop_bg);
        TextView cancel = (TextView) contentView.findViewById(R.id.type_file_pop_cancel);
        TextView formCamera = (TextView) contentView.findViewById(R.id.type_file_pop_camera);
        TextView formPhoto = (TextView) contentView.findViewById(R.id.type_file_pop_photo);
        TextView formFile = (TextView) contentView.findViewById(R.id.type_file_pop_file);


        bg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    uploadMsg.onReceiveValue(null);
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMsg.onReceiveValue(null);
                popupWindow.dismiss();
            }
        });

        formCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoPath = FileHelper.getTakePhotoPath();
                if (FileHelper.existSDCard()) {
                    Intent intent = GraphicHelper.getPhotoIntent(photoPath);
                    try {
                        cordovaInterface.startActivityForResult(new CordovaPlugin() {
                            public void onActivityResult(int requestCode, int resultCode, Intent intent) {
                                if (5173 == requestCode) {
                                    if (-1 == resultCode) {
                                        if (ToolsHelper.isNull(photoPath) || null == new File(photoPath)) {
                                            uploadMsg.onReceiveValue(null);
                                        } else {
                                            Uri uri = Uri.fromFile(new File(photoPath));
                                            uploadMsg.onReceiveValue(uri);
                                        }
                                    } else {
                                        uploadMsg.onReceiveValue(null);
                                    }
                                }
                            }
                        }, intent, 5173);
                    } catch (ActivityNotFoundException var6) {
                        uploadMsg.onReceiveValue(null);
                    }
                } else {
                    ToolsHelper.showStatus(mContext,false,getString(R.string.switchimageactivity_no_sdkcard));
                    uploadMsg.onReceiveValue(null);
                }
                popupWindow.dismiss();
            }
        });
//        formPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (FileHelper.existSDCard()) {
//                    Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
//                    // whether show camera
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
//                    // max select image amount
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
//                    // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
//                    // default select images (support array list)
//                    ArrayList<String> defaultDataArray = new ArrayList<String>();
//                    intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
//                            defaultDataArray);
//                    try {
//                        cordovaInterface.startActivityForResult(new CordovaPlugin() {
//                            public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//                                if (5173 == requestCode) {
//                                    if (-1 == resultCode) {
//                                        List<String> d = intent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                                        if (null != d && d.size() > 0) {
//                                            if (4 == cn.lds.chatcore.common.FileHelper.getFileTypeIcon(d.get(0))) {
//                                                Uri uri = Uri.fromFile(new File(d.get(0)));
//                                                uploadMsg.onReceiveValue(uri);
//                                            } else {
//                                                uploadMsg.onReceiveValue(null);
//                                            }
//                                        }
//                                    } else {
//                                        uploadMsg.onReceiveValue(null);
//                                    }
//                                }
//                            }
//                        }, intent, 5173);
//                    } catch (ActivityNotFoundException var6) {
//                        uploadMsg.onReceiveValue(null);
//                    }
//                } else {
//                    ToolsHelper.showStatus(mContext,false, getString(R.string.switchimageactivity_no_sdkcard));
//                    uploadMsg.onReceiveValue(null);
//                }
//                popupWindow.dismiss();
//            }
//        });
//        formFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, SelectFileActivity.class);
//                try {
//                    cordovaInterface.startActivityForResult(new CordovaPlugin() {
//                        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//
//                            if (5173 == requestCode) {
//                                if (-1 == resultCode) {
//                                    String path = intent.getStringExtra("path");
//
//                                    if (ToolsHelper.isNull(path) || null == new File(path)) {
//                                        uploadMsg.onReceiveValue(null);
//                                    } else {
//                                        Uri uri = Uri.fromFile(new File(path));
//                                        uploadMsg.onReceiveValue(uri);
//                                    }
//                                } else {
//                                    uploadMsg.onReceiveValue(null);
//                                }
//                            }
//                        }
//                    }, intent, 5173);
//                } catch (ActivityNotFoundException var6) {
//                    uploadMsg.onReceiveValue(null);
//                }
//                popupWindow.dismiss();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadMsg.onReceiveValue(null);
//                popupWindow.dismiss();
//            }
//        });
    }

    protected String photoPath;

    @TargetApi(21)
    protected void openPop(final ValueCallback<Uri[]> filePathsCallback) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_type_file, null);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
//        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(findViewById(R.id.top__iv));
        LinearLayout bg = (LinearLayout) contentView.findViewById(R.id.type_file_pop_bg);
        TextView cancel = (TextView) contentView.findViewById(R.id.type_file_pop_cancel);
        TextView formCamera = (TextView) contentView.findViewById(R.id.type_file_pop_camera);
        TextView formPhoto = (TextView) contentView.findViewById(R.id.type_file_pop_photo);
        TextView formFile = (TextView) contentView.findViewById(R.id.type_file_pop_file);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePathsCallback.onReceiveValue(null);
                popupWindow.dismiss();
            }
        });

        formCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoPath = FileHelper.getTakePhotoPath();
                if (FileHelper.existSDCard()) {
                    Intent intent = GraphicHelper.getPhotoIntent(photoPath);
                    try {
                        cordovaInterface.startActivityForResult(new CordovaPlugin() {
                            public void onActivityResult(int requestCode, int resultCode, Intent intent) {
                                if (5173 == requestCode) {
                                    if (-1 == resultCode) {
                                        if (ToolsHelper.isNull(photoPath) || null == new File(photoPath)) {
                                            filePathsCallback.onReceiveValue(new Uri[]{});
                                        } else {
                                            Uri uri = Uri.fromFile(new File(photoPath));
                                            filePathsCallback.onReceiveValue(new Uri[]{uri});
                                        }
                                    } else {
                                        filePathsCallback.onReceiveValue(new Uri[]{});
                                    }
                                }
                            }
                        }, intent, 5173);
                    } catch (ActivityNotFoundException var6) {
                        filePathsCallback.onReceiveValue(null);
                    }
                } else {
                    ToolsHelper.showStatus(mContext,false, getString(R.string.switchimageactivity_no_sdkcard));
                    filePathsCallback.onReceiveValue(new Uri[]{});
                }
                popupWindow.dismiss();
            }
        });
//        formPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (FileHelper.existSDCard()) {
//                    Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
//                    // whether show camera
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
//                    // max select image amount
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
//                    // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
//                    // default select images (support array list)
//                    ArrayList<String> defaultDataArray = new ArrayList<String>();
//                    intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
//                            defaultDataArray);
//                    try {
//                        cordovaInterface.startActivityForResult(new CordovaPlugin() {
//                            public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//                                if (5173 == requestCode) {
//                                    if (-1 == resultCode) {
//                                        List<String> d = intent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                                        if (null != d && d.size() > 0) {
//                                            if (4 == cn.lds.chatcore.common.FileHelper.getFileTypeIcon(d.get(0))) {
//                                                Uri uri = Uri.fromFile(new File(d.get(0)));
//                                                filePathsCallback.onReceiveValue(new Uri[]{uri});
//                                            } else {
//                                                filePathsCallback.onReceiveValue(new Uri[]{});
//                                            }
//                                        }
//                                    } else {
//                                        filePathsCallback.onReceiveValue(new Uri[]{});
//                                    }
//                                }
//                            }
//                        }, intent, 5173);
//                    } catch (ActivityNotFoundException var6) {
//                        filePathsCallback.onReceiveValue(null);
//                    }
//                } else {
//                    ToolsHelper.showStatus(mContext,false, getString(R.string.switchimageactivity_no_sdkcard));
//                    filePathsCallback.onReceiveValue(new Uri[]{});
//                }
//                popupWindow.dismiss();
//            }
//        });
        formFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, SelectFileActivity.class);
                try {
//                    cordovaInterface.startActivityForResult(new CordovaPlugin() {
//                        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//
//                            if (5173 == requestCode) {
//                                if (-1 == resultCode) {
//                                    String path = intent.getStringExtra("path");
//
//                                    if (ToolsHelper.isNull(path) || null == new File(path)) {
//                                        filePathsCallback.onReceiveValue(new Uri[]{});
//                                    } else {
//                                        Uri uri = Uri.fromFile(new File(path));
//                                        filePathsCallback.onReceiveValue(new Uri[]{uri});
//                                    }
//                                } else {
//                                    filePathsCallback.onReceiveValue(new Uri[]{});
//                                }
//                            }
//                        }
//                    }, intent, 5173);
                } catch (ActivityNotFoundException var6) {
                    filePathsCallback.onReceiveValue(null);
                }
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePathsCallback.onReceiveValue(null);
                popupWindow.dismiss();
            }
        });
    }

    SystemWebView webView;

    @Override
    protected CordovaWebView makeWebView() {
        webView = (SystemWebView) findViewById(R.id.cordovaWebView);
        webView.setDownloadListener(new MyWebViewDownLoadListener());//下载监听
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭缓存
        return new CordovaWebViewImpl(new SystemWebViewEngine(webView));
    }

    protected class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (Exception ex) {
                LogHelper.e("打开外部浏览器", ex);
                ToolsHelper.showStatus(mContext,false, getString(R.string.switchimageactivity_openerror));
            }

        }

    }

    @Override
    protected void createViews() {
        //Why are we setting a constant as the ID? This should be investigated
//        appView.getView().setId(100);
//        appView.getView().setLayoutParams(new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//
//        setContentView(appView.getView());

//        if (preferences.contains(BackgroundColor)) {
//            int backgroundColor = preferences.getInteger(BackgroundColor, Color.BLACK);
//            // Background of activity:
//            appView.getView().setBackgroundColor(backgroundColor);
//        }

        appView.getView().requestFocusFromTouch();
    }

    /**
     * Sync Cookie
     */
    protected void syncCookie() {
        try {

            CookieSyncManager.createInstance(getApplicationContext());

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
//			cookieManager.removeSessionCookie();// 移除
//			cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(Constants.getCoreUrls().H5_SERVER_HOST());
            if (oldCookie != null) {
                System.out.println("-----oldCookie:" + oldCookie);
            }
            DefaultHttpClient dh = (DefaultHttpClient) HttpHelper.getHttpInstance().getHttpClient();
            CookieStore cs = dh.getCookieStore();
            List<Cookie> cookies = cs.getCookies();
            String jsessionid = null;
            for (int i = 0; i < cookies.size(); i++) {
                if ("JSESSIONID".equals(cookies.get(i).getName())) {
                    jsessionid = cookies.get(i).getValue();
                    break;
                }
            }
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", jsessionid));
            sbCookie.append(String.format(";domain=%s", Constants.getCoreUrls().H5_SERVER_DOMAIN()));
            sbCookie.append(String.format(";path=%s", "/"));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(Constants.getCoreUrls().H5_SERVER_HOST(), cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(Constants.getCoreUrls().H5_SERVER_HOST());
            if (newCookie != null) {
                System.out.println("-----newCookie:" + newCookie);
            }
        } catch (Exception e) {
            System.out.println("-----Exception:" + e.getMessage());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        loadTimes = 0;
        handler = null;
//        clearCookie();//退出时清除cookie
//        clearCache();//退出时清除缓存---------没打开缓存还清的什么卵子。。。
    }

    protected void clearCache() {
        getApplicationContext().deleteDatabase("webview.db");
        getApplicationContext().deleteDatabase("webviewCache.db");
    }

    protected void clearCookie() {
        CookieSyncManager.createInstance(getApplicationContext());

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        cookieManager.removeAllCookie();

    }


    private int loadTimes = 0;
    @Override
    public void onReceivedError(int errorCode, String description, String failingUrl) {
//        cordova_err.setVisibility(View.VISIBLE);
//        webView.setVisibility(View.GONE);
//        switch (errorCode) {
//            case -1:
//                cordova_err.setImageResource(R.drawable.html_err_404_h);
//
//                break;
//            default:
//
//
//        }
        if(loadTimes == 0){
            loadTimes++;
            super.loadUrl(url);
        }else{
            loadTimes = 0;
            Toast.makeText(mContext,"网络开小差了...",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            switch (msg.what) {
//                case 403:
////                    cordova_err.setVisibility(View.VISIBLE);
////                    webView.setVisibility(View.GONE);
////                    cordova_err.setImageResource(R.drawable.html_err_403_h);
//                    break;
//                case 404:
//                    if(loadTimes == 0){
//                        loadTimes++;
//                        webView.loadUrl(url);
//                    }else{
//                        loadTimes = 0;
//
//                        cordova_err.setVisibility(View.VISIBLE);
//                        webView.setVisibility(View.GONE);
//                        cordova_err.setImageResource(R.drawable.html_err_404_h);
//                    }
//                    break;
//            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (View.VISIBLE == cordova_err.getVisibility()) {
            if (appView.canGoBack())
                webView.setVisibility(View.VISIBLE);
            else
                finish();
            cordova_err.setVisibility(View.GONE);
        }
        return super.onKeyDown(keyCode, event);
    }
}
