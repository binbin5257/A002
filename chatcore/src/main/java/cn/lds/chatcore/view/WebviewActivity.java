//package cn.lds.chatcore.view;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.DownloadListener;
//import android.webkit.GeolocationPermissions.Callback;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebSettings.PluginState;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.lidroid.xutils.ViewUtils;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;
//
//import org.apache.http.client.CookieStore;
//import org.apache.http.cookie.Cookie;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import java.util.List;
//
//import cn.lds.chatcore.R;
//import cn.lds.chatcore.common.Constants;
//import cn.lds.chatcore.common.HttpHelper;
//import cn.lds.chatcore.common.ToolsHelper;
//import cn.lds.chatcore.common.WXMulHelper;
//
//
//@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
//public class WebviewActivity extends BaseWebviewActivity implements
//        DownloadListener {
//
//    private final int PROGRESS_SHOW = 0;
//    private final int PROGRESS_HIDE = 1;
//    protected final int FILECHOOSER_RESULTCODE = 20;
//
//    private Context mContext;
//    // WEB视图
//    private WebView mWebview;
//    // 标题栏
//    private RelativeLayout mTitleBar;
//    // 返回按钮
//    private Button btnBack;
//    // 刷新按钮
//    private ImageButton btnRefresh;
//    private ProgressBar mProgressBar;
//    // 标题
//    private TextView mTitleTextView;
//
//
//    // 标题
//    private String mTitle;
//    // 地址
//    private String mMainUrl;
//    // private int mid;
//    private boolean showTitlebar = false;
//    private boolean enableZoom = false;
//    private boolean ReceivedTitle = false;
//
//    private ValueCallback<Uri> mUploadMessage;
//
//    public static Cookie cookie = null;
//
//    private PopupWindow popupWindow;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_webview);
//        ViewUtils.inject(this);
//        //this.clearWebViewCache();
//        mContext = WebviewActivity.this;
//        // 获取传递的参数
//        mTitle = getIntent().getStringExtra("title");
//        mMainUrl = getIntent().getStringExtra("url");
//        showTitlebar = getIntent().getBooleanExtra("showtitlebar", false);
//        enableZoom = getIntent().getBooleanExtra("enablezoom", false);
//        ReceivedTitle = getIntent().getBooleanExtra("ReceivedTitle", false);
//        // mid = getIntent().getIntExtra("id", 1);
//        // 标题栏
//        mTitleBar = (RelativeLayout) findViewById(R.id.webview_titlebar);
//        // 返回按钮
//        btnBack = (Button) findViewById(R.id.title_back_btn);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//
//        // 刷新按钮
//        btnRefresh = (ImageButton) findViewById(R.id.title_refresh_btn);
//        btnRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.showAsDropDown(findViewById(R.id.top__iv));
//            }
//        });
//        // 标题
//        mTitleTextView = (TextView) findViewById(R.id.title_text);
//
//        // 设置标题
//        mTitleTextView.setText(mTitle);
//
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        // 设置标题栏显示状态
//        if (showTitlebar) {
//            mTitleBar.setVisibility(View.VISIBLE);
//        } else {
//            mTitleBar.setVisibility(View.GONE);
//        }
//        initWebView();
//        syncCookie();
//
//        initPopWindow();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                if (mWebview.canGoBack()) {
//                    mWebview.goBack();
//                } else {
//                    WebviewActivity.this.finish();
//                }
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        menu.add(0, 0, 0, getText(R.string.common_refresh));
//        menu.add(0, 1, 0, getText(R.string.common_open));
//        menu.add(0, 2, 0, getText(R.string.common_quit));
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case 0:
//                mWebview.reload();
//                mHandler.sendEmptyMessage(PROGRESS_SHOW);
//                return true;
//            case 1:
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mMainUrl));
//                startActivity(intent);
//                return true;
//            case 2:
//                WebviewActivity.this.finish();
//                return true;
//        }
//        return false;
//    }
//
//    // 初始化WebView
//    private void initWebView() {
//        mWebview = (WebView) findViewById(R.id.webview);
//
//        mWebview.setInitialScale(1);
//
//        mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebview.getSettings().setUseWideViewPort(true);
//        mWebview.getSettings().setLoadWithOverviewMode(true);
//
//        // 设置不缓存
//        mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        // 优先使用缓存
//        //mWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//
//        //mWebview.setScrollBarStyle(View.SCROLLBAR_POSITION_DEFAULT);
//        mWebview.getSettings().setJavaScriptEnabled(true);
//        // mWebview.getSettings().setPluginsEnabled(true);
//        mWebview.getSettings().setPluginState(PluginState.ON);
//
//        // 启用数据库
//        mWebview.getSettings().setDatabaseEnabled(true);
//        String dir = this.getApplicationContext()
//                .getDir("database", Context.MODE_PRIVATE).getPath();
//
//        // 启用地理定位
//        mWebview.getSettings().setGeolocationEnabled(true);
//        // 设置定位的数据库路径
//        mWebview.getSettings().setGeolocationDatabasePath(dir);
//
//        // 最重要的方法，一定要设置，这就是地图出不来的主要原因
//        mWebview.getSettings().setDomStorageEnabled(true);
//        mWebview.getSettings().setBlockNetworkImage(false);
//        mWebview.getSettings().setBlockNetworkLoads(false);
//
//        // webView自适应及缩放
//        if (enableZoom) {
//            // 设置可以支持缩放
//            mWebview.getSettings().setSupportZoom(true);
//            // 设置默认缩放方式尺寸是far
//            mWebview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//            // 设置出现缩放工具
//            mWebview.getSettings().setBuiltInZoomControls(true);
//            // 让网页自适应屏幕宽度
//            // WebSettings webSettings = mWebview.getSettings();
//            // webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//        }
//
//        mWebview.setWebViewClient(new MyWebViewClient());
//        mWebview.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                // TODO Auto-generated method stub
//                super.onReceivedTitle(view, title);
//                if (ReceivedTitle) {
//                    mTitleTextView.setText(title);
//                }
//            }
//
//            @Override
//            public void onReceivedIcon(WebView view, Bitmap icon) {
//
//                super.onReceivedIcon(view, icon);
//
//            }
//
//            @Override
//            public void onGeolocationPermissionsShowPrompt(String origin,
//                                                           Callback callback) {
//
//                callback.invoke(origin, true, false);
//
//                super.onGeolocationPermissionsShowPrompt(origin, callback);
//
//            }
//
//            public void onProgressChanged(WebView view, int progress) {
//                super.onProgressChanged(view, progress);
//                if (progress == 100) {
//                    mHandler.sendEmptyMessage(PROGRESS_HIDE);
//                } else {
//                    mProgressBar.setProgress(progress);
//                }
//            }
//
//            // js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
//            // Android > 4.1.1 调用这个方法
//            public void openFileChooser(ValueCallback<Uri> uploadMsg,
//                                        String acceptType, String capture) {
//                mUploadMessage = uploadMsg;
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");
//                WebviewActivity.this.startActivityForResult(
//                        Intent.createChooser(intent, "完成操作需要使用"),
//                        FILECHOOSER_RESULTCODE);
//
//            }
//
//            // 3.0 + 调用这个方法
//            public void openFileChooser(ValueCallback<Uri> uploadMsg,
//                                        String acceptType) {
//                mUploadMessage = uploadMsg;
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");
//                WebviewActivity.this.startActivityForResult(
//                        Intent.createChooser(intent, "完成操作需要使用"),
//                        FILECHOOSER_RESULTCODE);
//            }
//
//            // Android < 3.0 调用这个方法
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                mUploadMessage = uploadMsg;
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");
//                WebviewActivity.this.startActivityForResult(
//                        Intent.createChooser(intent, "完成操作需要使用"),
//                        FILECHOOSER_RESULTCODE);
//
//            }
//        });
//        mWebview.addJavascriptInterface(new JavaScriptInterface(this), "Android");
//
//        // 清除缓存的有效方法
//        // mWebview.loadDataWithBaseURL(null, "","text/html", "utf-8",null);
//        // 设置下载监听
//        mWebview.setDownloadListener(this);
//        startLoadUrl(mMainUrl);
//    }
//
//    public void startLoadUrl(final String url) {
//        mHandler.sendEmptyMessage(PROGRESS_SHOW);
//
//        CookieSyncManager.createInstance(WebviewActivity.this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
//        String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain="
//                + cookie.getDomain();
//        cookieManager.setCookie(url, cookieString);//cookies是在HttpClient中获得的cookie
//        CookieSyncManager.getInstance().sync();
//        mWebview.loadUrl(url);
//    }
//
//    /**
//     * 返回文件选择
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent intent) {
//        Tencent.onActivityResultData(requestCode, resultCode, intent, iUiListener);
//        if (requestCode == FILECHOOSER_RESULTCODE) {
//            if (null == mUploadMessage)
//                return;
//            Uri result = intent == null || resultCode != RESULT_OK ? null
//                    : intent.getData();
//            mUploadMessage.onReceiveValue(result);
//            mUploadMessage = null;
//
//        }
//    }
//
//    // WebView 客户端
//    public class MyWebViewClient extends WebViewClient {
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            mWebview.loadUrl(url);
//            return true;
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//        }
//
//    }
//
//    public class JavaScriptInterface {
//        Context mContext;
//
//        /**
//         * Instantiate the interface and set the context
//         */
//        JavaScriptInterface(Context c) {
//            mContext = c;
//        }
//
//        public void quit() {
//            WebviewActivity.this.finish();
//        }
//
//    }
//
//    // Handler线程
//    Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            if (!Thread.currentThread().isInterrupted()) {
//                switch (msg.what) {
//                    case PROGRESS_SHOW:
//                        mProgressBar.setVisibility(View.VISIBLE);
//                        break;
//                    case PROGRESS_HIDE:
//                        mProgressBar.setVisibility(View.GONE);
//                        break;
//                }
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    @Override
//    public void onDownloadStart(String url, String userAgent,
//                                String contentDisposition, String mimetype, long contentLength) {
//        Uri uri = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
//
//    }
//
//    /**
//     * Sync Cookie
//     */
//    private void syncCookie() {
//        try {
//
//            CookieSyncManager.createInstance(getApplicationContext());
//
//            CookieManager cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptCookie(true);
////			cookieManager.removeSessionCookie();// 移除
////			cookieManager.removeAllCookie();
//            String oldCookie = cookieManager.getCookie(Constants.getCoreUrls().H5_SERVER_HOST());
//            if (oldCookie != null) {
//                System.out.println("-----oldCookie:" + oldCookie);
//            }
//            DefaultHttpClient dh = (DefaultHttpClient) HttpHelper.getHttpInstance().getHttpClient();
//            CookieStore cs = dh.getCookieStore();
//            List<Cookie> cookies = cs.getCookies();
//            String jsessionid = null;
//            for (int i = 0; i < cookies.size(); i++) {
//                if ("JSESSIONID".equals(cookies.get(i).getName())) {
//                    jsessionid = cookies.get(i).getValue();
//                    break;
//                }
//            }
//            StringBuilder sbCookie = new StringBuilder();
//            sbCookie.append(String.format("JSESSIONID=%s", jsessionid));
//            sbCookie.append(String.format(";domain=%s", Constants.getCoreUrls().H5_SERVER_DOMAIN()));
//            sbCookie.append(String.format(";path=%s", "/"));
//
//            String cookieValue = sbCookie.toString();
//            cookieManager.setCookie(Constants.getCoreUrls().H5_SERVER_HOST(), cookieValue);
//            CookieSyncManager.getInstance().sync();
//
//            String newCookie = cookieManager.getCookie(Constants.getCoreUrls().H5_SERVER_HOST());
//            if (newCookie != null) {
//                System.out.println("-----newCookie:" + newCookie);
//            }
//        } catch (Exception e) {
//            System.out.println("-----Exception:" + e.getMessage());
//        }
//    }
//
//
//    /**
//     * 初始化弹出窗
//     */
//    public void initPopWindow() {
//        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_webview, null);
//        // 声明一个弹出框
//        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        // 声明一个弹出框
//        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
//                .getDefaultDisplay().getHeight());
//        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));
//
//        // 为弹出框设定自定义的布局
//        popupWindow.setContentView(contentView);
//
//        LinearLayout bg = (LinearLayout) contentView.findViewById(R.id.pop_bg);
//        TextView cancel = (TextView) contentView.findViewById(R.id.pop_cancel);
//        FrameLayout weixin = (FrameLayout) contentView.findViewById(R.id.fl_pop_weixin);
//        FrameLayout weixincircle = (FrameLayout) contentView.findViewById(R.id.fl_pop_weixincircle);
//        FrameLayout qq = (FrameLayout) contentView.findViewById(R.id.fl_pop_qq);
//        FrameLayout refresh = (FrameLayout) contentView.findViewById(R.id.fl_pop_refresh);
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWebview.reload();
//                mHandler.sendEmptyMessage(PROGRESS_SHOW);
//                popupWindow.dismiss();
//            }
//        });
//        weixin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WXMulHelper.getInstance(getIntent(), mContext).shareTxtToWx("分享测试，请忽略", WXMulHelper.WXSceneSession);
//                popupWindow.dismiss();
//            }
//        });
//        weixincircle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WXMulHelper.getInstance(getIntent(), mContext).shareTxtToWx("分享测试，请忽略", WXMulHelper.WXSceneTimeline);
//                popupWindow.dismiss();
//            }
//        });
//        qq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WXMulHelper.getInstance(getIntent(), mContext).shareToQQ(WebviewActivity.this, "fdafda", iUiListener);
//                popupWindow.dismiss();
//            }
//        });
//        // 隐藏弹出框
//        bg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
//
//        // 取消，隐藏弹出框
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
//    }
//
//    private IUiListener iUiListener = new IUiListener() {
//        @Override
//        public void onComplete(Object o) {
//            ToolsHelper.showInfo(mContext, getString(R.string.wxentryactivity_share_success));
//        }
//
//        @Override
//        public void onError(UiError uiError) {
//            ToolsHelper.showInfo(mContext, getString(R.string.wxentryactivity_share_failed));
//        }
//
//        @Override
//        public void onCancel() {
//            ToolsHelper.showInfo(mContext, getString(R.string.wxentryactivity_share_cancel));
//        }
//    };
//
//
//}
