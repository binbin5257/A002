package cn.lds.chatcore.common;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoaderHelper {

    private static ImageLoaderHelper instance;

    // private MemoryCache memoryCache = new MemoryCache();
    private Map<ImageView, Integer> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, Integer>());
    // 线程池
    private ExecutorService executorService;
    private boolean isHTMLMessage = false;

    private ContentResolver resolver;

    public static synchronized ImageLoaderHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ImageLoaderHelper(context);
        }
        return instance;
    }

    public ImageLoaderHelper(Context context) {
        executorService = Executors.newFixedThreadPool(10);
        resolver = context.getContentResolver();
    }

    // 最主要的方法
    public void DisplayImage(int id, int type, ImageView imageView) {
        imageViews.put(imageView, id);
        queuePhoto(id, type, imageView);
    }

    private void queuePhoto(int id, int type, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(id, type, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(int id, int type) {
        Bitmap thumbnail = null;
        switch (type) {
            case FileHelper.TYPE_IMAGE:
                thumbnail = MediaStore.Images.Thumbnails.getThumbnail(this.resolver, id,
                        MediaStore.Images.Thumbnails.MICRO_KIND, null);
                break;
            case FileHelper.TYPE_VIDEO:
                thumbnail = MediaStore.Video.Thumbnails.getThumbnail(this.resolver, id,
                        MediaStore.Images.Thumbnails.MICRO_KIND, null);
                break;
        }

        return thumbnail;
    }


    // Task for the queue
    private class PhotoToLoad {
        public int id;
        public int type;
        public ImageView imageView;

        public PhotoToLoad(int id, int type, ImageView i) {
            this.id = id;
            this.type = type;
            this.imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.id, photoToLoad.type);
            // memoryCache.put(photoToLoad.url, bmp);

            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            // 更新的操作放在UI线程中
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    /**
     * 防止图片错位
     *
     * @param photoToLoad
     * @return
     */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        int id = imageViews.get(photoToLoad.imageView) == null ? -1 : imageViews.get(photoToLoad.imageView);
        if (id == -1 || id != photoToLoad.id)
            return true;
        return false;
    }

    // 用于在UI线程中更新界面
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);

        }
    }


}