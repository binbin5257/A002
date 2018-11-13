package cn.lds.im.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewClient;
import org.apache.cordova.engine.SystemWebViewEngine;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.HttpHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.ZMXYScore;
import cn.lds.chatcore.view.BaseCordovaActivity;
import de.greenrobot.event.EventBus;

public class ZhiMaAuthorizationActivity extends BaseCordovaActivity {
    public static final String START_URL = "file:///android_asset/www/index.html";
    protected TextView top_title_tv;
    protected Button top_back_btn;
    protected ImageView mService;
    protected Context mContext;
    protected ImageView cordova_err;
    protected int layoutID = R.layout.activity_cordova_webview;
    protected ZhiMaAuthorizationActivity activity;
    protected void setActivity(ZhiMaAuthorizationActivity activity) {
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirst = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirst = false;
    }

    protected void initConfig() {
        mContext = ZhiMaAuthorizationActivity.this;

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
        String url = getIntent().getStringExtra("url");
        if (ToolsHelper.isNull(url)) {
            url = START_URL;
        }
        webView.setWebChromeClient(new MyWebChromeClient((SystemWebViewEngine) appView.getEngine()));
        webView.setWebViewClient(new MyWebViewClient((SystemWebViewEngine) appView.getEngine()));

        WebSettings settings = webView.getSettings();
        // 设置与Js交互的权限
        settings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(false);

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
        public void onPageStarted(WebView view, final String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    CookieSyncManager.createInstance(getApplicationContext());

                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
//                    handler.sendEmptyMessage(getInputStream(url, ""));

                }
            }).start();


        }
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
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            CoreHttpApi.enrolleesGet();
            return true;
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

        public void onProgressChanged(final WebView view, int progress) {
            super.onProgressChanged(view, progress);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathsCallback, FileChooserParams fileChooserParams) {
            return true;
        }

        @Override
        public void openFileChooser(final ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        }
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
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirst = true;
    }



    @Override
    public void onReceivedError(int errorCode, String description, String failingUrl) {
        cordova_err.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
//        switch (errorCode) {
//            case -1:
//                cordova_err.setImageResource(R.drawable.html_err_404_h);
//                break;
//            default:
//                cordova_err.setImageResource(R.drawable.html_err_h);
//        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            switch (msg.what) {
//                case 403:
//                    cordova_err.setVisibility(View.VISIBLE);
//                    webView.setVisibility(View.GONE);
//                    cordova_err.setImageResource(R.drawable.html_err_403_h);
//                    break;
//                case 404:
//                    cordova_err.setVisibility(View.VISIBLE);
//                    webView.setVisibility(View.GONE);
//                    cordova_err.setImageResource(R.drawable.html_err_404_h);
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
        }else{
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onEventMainThread(ZMXYScore event) {
        CoreHttpApi.enrolleesGet();

    }
    boolean isFirst = true;
    public void onEventMainThread(AccountAvaliableEvent event) {

        //正常情况回进入两次，一次im成功回调，一次拦截js成功回调方法
        if(isFirst){
            Toast.makeText(ZhiMaAuthorizationActivity.this,"获取芝麻信用评分成功!",Toast.LENGTH_SHORT).show();
            isFirst = false;
            this.finish();
        }

    }

}
