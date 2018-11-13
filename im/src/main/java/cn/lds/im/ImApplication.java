package cn.lds.im;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.Constants;
import cn.lds.im.common.CoreUrls;

/**
 * Created by geese on 2015/11/23.
 */
public class ImApplication extends MyApplication {
    @Override
    public void onCreate() {
        // 初始化核心常量，提供给核心使用
        Constants.setCoreUrls(new CoreUrls());
        super.onCreate();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        CustomImageDownloader customImageDownloader = new CustomImageDownloader(this);

    }

    public class CustomImageDownloader extends BaseImageDownloader {//universal image loader获取图片时,若需要cookie，
        // 需在application中进行配置添加此类。
        public CustomImageDownloader(Context context) {
            super(context);
        }

        @Override
        protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
            // Super...
            HttpURLConnection connection = super.createConnection(url, extra);
//            connection.setRequestProperty("Cookie", MyApplication.getInstance().getCookieString());
            connection.setRequestProperty("Connection", "keep-Alive");
            connection.setRequestProperty("User-Agent", "jsgdMobile");
            return connection;
        }
    }
}
