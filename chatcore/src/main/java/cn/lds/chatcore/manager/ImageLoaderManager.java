package cn.lds.chatcore.manager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;
import cn.lds.chatcore.common.BitmapHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.StringHelper;
import cn.lds.chatcore.view.widget.PhotoView.PhotoView;

/**
 * 图片管理器
 */
public class ImageLoaderManager extends AbstractManager {

    private DisplayImageOptions defaultOptions;
    private DisplayImageOptions defaultActiveOptions;
    private File cacheDir = StorageUtils.getOwnCacheDirectory(mApplicationContext, FileHelper.rootPathName + "/Cache");

    protected static ImageLoaderManager instance;
    private final RoundedBitmapDisplayer roundedBitmapDisplayer;

//    static {
//        instance = new ImageLoaderManager();
//        MyApplication.getInstance().addManager(instance);
//    }

    public ImageLoaderManager() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(mApplicationContext);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCache(new UnlimitedDiskCache(cacheDir));
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());


        defaultOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.slidemenu_avtar)
                .showImageForEmptyUri(R.drawable.slidemenu_avtar).showImageOnFail(R.drawable.slidemenu_avtar)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer
                        (5, 0)).imageScaleType(ImageScaleType.EXACTLY).build();
//圆角大小通过 dp2px转换 使得 不同分辨率设备上呈现一致显示效果
        roundedBitmapDisplayer = new RoundedBitmapDisplayer(dip2px(MyApplication.getInstance().getApplicationContext(),2));

        defaultActiveOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_active_default)
                .showImageForEmptyUri(R.drawable.bg_active_default)
                .showImageOnFail(R.drawable.bg_active_default)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(roundedBitmapDisplayer)
                .build();
    }
    /**
     * dip px 转换工具类 将圆角进行转换 以实现不同分辨率设备上呈现相同效果
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }


    public static ImageLoaderManager getInstance() {
        if (instance == null) {
            try {
                instance = new ImageLoaderManager();
                MyApplication.getInstance().addManager(instance);
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    public Bitmap loadImageSync(String uri) {
        return ImageLoader.getInstance().loadImageSync(Constants.getCoreUrls().getDownloadUrl(uri) + "?type=original");
    }
    /**
     * 活动图片
     *
     * @param uri       头像ID
     * @param container ImageView
     */
    public void displayActive(String uri, ImageView container) {

        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getFileloadUrl(uri);
            }
            ImageLoader.getInstance().displayImage(fullUrl, container,defaultActiveOptions);
        } catch (Exception ex) {
            LogHelper.e("显示头像", ex);
        }
    }
    /**
     * 首页活动图片
     *
     * @param uri       头像ID
     * @param container ImageView
     */
    public void displayHomeActive(String uri, ImageView container) {

        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getFileloadUrl(uri);
            }
            ImageLoader.getInstance().displayImage(fullUrl, container,defaultActiveOptions);
        } catch (Exception ex) {
            LogHelper.e("显示头像", ex);
        }
    }
    /**
     * 显示头像
     *
     * @param uri       头像ID
     * @param container ImageView
     */
    public void displayAvatar(String uri, ImageView container) {
        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getFileloadUrl(uri) + "?type=original";
            }
            ImageLoader.getInstance().displayImage(fullUrl, container, defaultOptions);
        } catch (Exception ex) {
            LogHelper.e("显示头像", ex);
        }
    }


    /**
     * 显示图片详情
     *
     * @param uri       图片ID
     * @param container ImageView
     */
    public void displayImage(final Context context, final String uri, BitmapUtils bitmapUtils, PhotoView container) {
        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getFileloadUrl(uri);
                fullUrl = fullUrl + "?type=original";//设置下载type original-原图  standard-标准图  thumbnail-缩略图
            }
            final String finalFullUrl = fullUrl;
            // 获取圆形进度条
            final Dialog dialog = LoadingDialog.getCircleProgressDialog(context);
            final View view = LoadingDialog.getCircleProgressView(context);
            bitmapUtils.display(container, fullUrl, new BitmapLoadCallBack<PhotoView>() {

                @Override
                public void onPreLoad(PhotoView container, String uri, BitmapDisplayConfig config) {
                    super.onPreLoad(container, uri, config);
                    LogHelper.d("图片下载进度：onPreLoad");
                }

                @Override
                public void onLoadCompleted(PhotoView view, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    LogHelper.d("图片下载进度：onLoadCompleted");
                    if (finalFullUrl.equals(s)) {
                        dialog.dismiss();
                        view.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onLoadFailed(PhotoView view, String s, Drawable drawable) {
                    LogHelper.d("图片下载进度：onLoadFailed");
                    if (finalFullUrl.equals(s)) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onLoadStarted(PhotoView container, String uri, BitmapDisplayConfig config) {
                    super.onLoadStarted(container, uri, config);
                    //LoadingDialog.showDialog(context, "", false);
                    LogHelper.d("图片下载进度：onLoadStarted");
                    LoadingDialog.showCircleProgressDialog(context, dialog, view);

                }

                @Override
                public void onLoading(PhotoView container, String uri, BitmapDisplayConfig config, long total, long current) {
                    try {
                        LogHelper.d("图片下载进度：onLoadStarted");
                        LogHelper.d("图片下载进度:" + current + "/" + total);
                        int percent = ((int) Math.floor(current * 100 / total));
                        LogHelper.d("图片下载进度:" + percent);
                        LoadingDialog.updateCircleProgressDialog(percent, dialog, view);
                    } catch (Exception ex) {
                        LogHelper.e("图片下载进度", ex);
                    }
                }

            });//加载图片的方法，ImageLoader是按照image的宽高设置的，在这里不合适
        } catch (Exception ex) {
            LogHelper.e("显示图片详情", ex);
        }
    }

    /**
     * 显示图片详情
     *
     * @param uri       图片ID
     * @param container ImageView
     */
    public void displayImage(String uri, ImageView container) {
        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getFileloadUrl(uri);
                fullUrl = fullUrl + "?type=original";//设置下载type original-原图  standard-标准图  thumbnail-缩略图
            }
            BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils(null);
            bitmapUtils.display(container, fullUrl);//加载图片的方法，ImageLoader是按照image的宽高设置的，在这里不合适
        } catch (Exception ex) {
            LogHelper.e("显示图片详情", ex);
        }
    }
    /**
     * 显示活动图片
     *
     * @param uri       图片ID
     * @param container ImageView
     */
    public void displayImage(String uri, ImageView container,Drawable drawable) {
        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getDownloadUrl(uri);
                fullUrl = fullUrl + "?type=original";//设置下载type original-原图  standard-标准图  thumbnail-缩略图
            }
            BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils(drawable);
            bitmapUtils.display(container, fullUrl);//加载图片的方法，ImageLoader是按照image的宽高设置的，在这里不合适
        } catch (Exception ex) {
            LogHelper.e("显示图片详情", ex);
        }
    }

    /**
     * 显示审核图片
     *
     * @param uri       图片ID
     * @param container ImageView
     */
    public void displayImageForIdentifying(Context mContext, String uri, ImageView container) {
        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getFileloadUrl(uri);
                fullUrl = fullUrl + "?type=original";//设置下载type original-原图  standard-标准图  thumbnail-缩略图
            }
            BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils(mContext.getResources().getDrawable(R.drawable.identifying_loading), mContext.getResources().getDrawable(R.drawable.identifying_er));
            bitmapUtils.display(container, fullUrl);//加载图片的方法，ImageLoader是按照image的宽高设置的，在这里不合适
        } catch (Exception ex) {
            LogHelper.e("显示图片详情", ex);
        }
    }

    /**
     * 显示车辆图片
     *
     * @param uri       图片ID
     * @param container ImageView
     */
    public void displayImageForCar(Context mContext, String uri, ImageView container) {
        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getFileloadUrl(uri);
                fullUrl = fullUrl + "?type=original";//设置下载type original-原图  standard-标准图  thumbnail-缩略图
            }
            BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils(mContext.getResources().getDrawable(R.drawable.car_loading), mContext.getResources().getDrawable(R.drawable.car_loaderr));
            bitmapUtils.display(container, fullUrl);//加载图片的方法，ImageLoader是按照image的宽高设置的，在这里不合适
        } catch (Exception ex) {
            LogHelper.e("显示图片详情", ex);
        }
    }

    /**
     * 显示聊天图片
     *
     * @param uri       聊天图片ID或本地文件路径
     * @param container ImageView
     */
    public void displayChatImage(ImageView container, String uri, String base64Thumbnail) {
        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = Constants.getCoreUrls().getDownloadUrl(uri) + "?type=original";
            }

            byte[] bytes = Base64.decode(base64Thumbnail, Base64.DEFAULT);
            Drawable drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            container.setImageDrawable(drawable);
        } catch (Exception ex) {
            LogHelper.e("显示聊天图片", ex);
        }
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(drawable)
//                .showImageForEmptyUri(drawable)
//                .showImageOnFail(drawable)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(5, 0))
//                .build();

//        ImageLoader.getInstance().displayImage(fullUrl, container, options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//            }
//        }, new ImageLoadingProgressListener() {
//            @Override
//            public void onProgressUpdate(String imageUri, View view, int current, int total) {
//            }
//        });
    }

    public void displayQRcode(String uri, ImageView container) {
        try {
            String fullUrl;
            if (StringHelper.isNull(uri)) {
                fullUrl = null;
            } else {
                fullUrl = "file://" + uri;
            }

            ImageLoader.getInstance().displayImage(fullUrl, container, defaultOptions);
        } catch (Exception ex) {
            LogHelper.e("显示QRcode", ex);
        }
    }

    /**
     * 清除缓存
     */
    public void clearMemoryCache() {
        try {
            ImageLoader.getInstance().clearMemoryCache();
        } catch (Exception ex) {
            LogHelper.e("释放图片缓存", ex);
        }
    }
}
