package cn.lds.im.carlife;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.lds.chat.R;

/**
 * 车生活详情页面
 * Created by sibinbin on 18-3-20.
 */

public class NaviDetailActivity extends Activity {

    private String url;
    private String title;
    private ProgressBar progressBar;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            url = bundle.getString("URL");
            title = bundle.getString("TITLE");
//            double lng = bundle.getDouble("LNG");
//            double lat = bundle.getDouble("LNG");


        }
        TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
        titleTv.setText(title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        ImageView backIv = (ImageView) findViewById(R.id.top_back_iv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                finish();
            }
        });
        WebView webView = (WebView) findViewById(R.id.webview);
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
        // 是否使用overview mode加载页面，默认值 false
        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        settings.setLoadWithOverviewMode(true);
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
        if(!TextUtils.isEmpty(url)){
            webView.loadUrl(url);
        }
        //参数2：Java对象名
//        webView.addJavascriptInterface(new AndroidtoJs(), "AndroidWebView");
        //参数2：Java对象名
//        webView.addJavascriptInterface(new AndroidtoJs(), "AndroidWebView");
        //设置经度条
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged( WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if(newProgress==100){
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }

            }
        });
    }
//    // 继承自Object类
//    public class AndroidtoJs extends Object {
//
//        // 定义JS需要调用的方法
//        // 被JS调用的方法必须加入@JavascriptInterface注解
//        @JavascriptInterface
//        public void sendLoadUrl( final String url, final String title) {
//            String imgUrl = url;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("URL",url);
//                    bundle.putString("TITLE",title);
//                    intent.putExtras(bundle);
//                    MainActivity.this.startActivity(intent);
//
//                }
//            });
//        }
//    }

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
}
