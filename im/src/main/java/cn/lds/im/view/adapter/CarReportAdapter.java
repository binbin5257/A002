package cn.lds.im.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.BitmapHelper;
import cn.lds.chatcore.common.ImageLoaderHelper;
import cn.lds.chatcore.manager.ImageLoaderManager;

/**
 * 车况上报适配器
 * Created by E0608 on 2017/12/22.
 */

public class CarReportAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mPathList;
    private final LayoutInflater mInflater;
    private final ImageLoader imageLoader;

    public CarReportAdapter(Context context, List<String> pathList){
        this.mContext = context;
        this.mPathList = pathList;
        mInflater = LayoutInflater.from(mContext);
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
                .diskCacheExtraOptions(480, 800, null)  // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
                .memoryCacheSizePercentage(13) // default
                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs() // 打印debug log
                .build(); //开始构建
        imageLoader.init(config);

    }
    @Override
    public int getCount() {
        return mPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_car_report, null);
        ImageView carIv = (ImageView) view.findViewById(R.id.car_iv);
        ImageView delPicture = (ImageView) view.findViewById(R.id.del_picture_iv);

//        BitmapHelper.displayPic(carIv,mPathList.get(position));
        ImageLoaderManager.getInstance().displayQRcode(mPathList.get(position),carIv);
//        imageLoader.displayImage("file://" + mPathList.get(position), carIv);
//        ImageLoaderHelper.getInstance(mContext).DisplayImage();
        delPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelPictureListener.onDelPictureEvent(position);
            }
        });
        return view;
    }

    private OnDelPictureListener onDelPictureListener;

    public void setOnDelPictureListener(OnDelPictureListener onDelPictureListener) {
        this.onDelPictureListener = onDelPictureListener;
    }

    public interface OnDelPictureListener{
        void onDelPictureEvent(int position);
    }


}
