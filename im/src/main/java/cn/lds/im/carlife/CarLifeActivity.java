package cn.lds.im.carlife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BaiduMapHelper;
import cn.lds.chatcore.common.ToolsHelper;

public class CarLifeActivity extends Activity {


    private boolean isFirstLoc = true;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_life);
        init();
    }

    private void init() {
        final WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        // 缓存(cache)
        settings.setAppCacheEnabled(true);      // 默认值 false
        settings.setAppCachePath(getCacheDir().getAbsolutePath());
        // 存储(storage)
        settings.setDomStorageEnabled(true);    // 默认值 false
        settings.setDatabaseEnabled(true);      // 默认值 false
        // 是否支持viewport属性，默认值 false
        // 页面通过`<meta name="viewport" ... />`自适应手机屏幕
        settings.setUseWideViewPort(true);
        webView.setVerticalScrollBarEnabled(false);
        // 是否使用overview mode加载页面，默认值 false
        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        //启用数据库
        settings.setDatabaseEnabled(true);
        //设置定位的数据库路径
        String dir = this.getApplicationContext().getDir("database", this.MODE_PRIVATE).getPath();
        settings.setGeolocationDatabasePath(dir);
        webView.requestFocus();
        settings.setGeolocationDatabasePath(getFilesDir().getPath());
        // 是否支持Javascript，默认值false
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (isNetworkConnected()) {
            // 根据cache-control决定是否从网络上取数据
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            // 没网，离线加载，优先加载缓存(即使已经过期)
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
//        String loadUrl = getLoadUrl();
//        final String loadUrl = "http://47.95.230.159:8888";

        //参数2：Java对象名
        webView.addJavascriptInterface(new AndroidtoJs(), "AndroidWebView");
        // 开启 Application Caches 功能

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading( WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }


        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt( String origin, GeolocationPermissions.Callback callback ) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);

            }
        });
        //初始化视图
        BaiduMapHelper baiduMapHelper = new BaiduMapHelper(this);
        baiduMapHelper.initLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation( BDLocation location ) {
                // map view 销毁后不在处理新接收的位置
                if (location == null)
                    return;
                MyLocationData locData = new MyLocationData.Builder().accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();

                //实时更新，定位信息
                MyApplication.myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                if (isFirstLoc) {
                    isFirstLoc = false;
                    String loadUrl = "file:///android_asset/carlife.html";
                    if(!TextUtils.isEmpty(loadUrl)){
                        webView.loadUrl(loadUrl);
                    }
                }
            }
        });

    }
    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public boolean isNetworkConnected() {
        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空
        if (networkInfo != null)
            return networkInfo.isAvailable();
        return false;
    }
    // 继承自Object类
    public class AndroidtoJs extends Object {

        private String imgUrl;

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void sendLoadUrl( final String url, final String title) {
            if(title.equals("充电服务")){
                imgUrl = url + "?lng=" + MyApplication.myLocation.longitude + "&lat=" + MyApplication.myLocation.latitude;
            }else{
                imgUrl = url;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(CarLifeActivity.this,CarLifeDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL",imgUrl);
                    bundle.putString("TITLE",title);
                    intent.putExtras(bundle);
                    CarLifeActivity.this.startActivity(intent);

                }
            });
        }
        @JavascriptInterface
        public void developing() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CarLifeActivity.this,"正在创建中...",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
