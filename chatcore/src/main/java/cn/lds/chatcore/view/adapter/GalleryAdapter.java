package cn.lds.chatcore.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.lds.chatcore.R;
import cn.lds.chatcore.common.BitmapHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.ImageInfo;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.widget.PhotoView.PhotoView;
import cn.lds.chatcore.view.widget.PhotoView.PhotoViewAttacher;

public class GalleryAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<PhotoView> imageViews = new ArrayList<PhotoView>();

    private List<ImageInfo> mItems;
    private File bmp = null;
    private OnItemClickListener mOnItemClickListener;//adapter点击事件


    public GalleryAdapter(Context context, List<ImageInfo> mItems) {
        this.mContext = context;
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoView view = new PhotoView(mContext);
        view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        final ImageInfo item = mItems.get(position);
        if (item != null) {
            view.setTag(item);
            Drawable drawable = null;
            if (!ToolsHelper.isNull(item.getThumbnail())) {
                byte[] bytes = Base64.decode(item.getThumbnail(), Base64.DEFAULT);
                drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
            final BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils(drawable);
            if (!ToolsHelper.isNull(item.getPath())) {
                bitmapUtils.display(view, item.getPath(), new BitmapLoadCallBack<PhotoView>() {
                    @Override
                    public void onLoadCompleted(PhotoView view, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                        if (item.getPath().equals(s)) {
                            LoadingDialog.dismissDialog();
                            view.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onLoadFailed(PhotoView view, String s, Drawable drawable) {
                        if (item.getPath().equals(s)) {
                            LoadingDialog.dismissDialog();
                        }
                    }

                    @Override
                    public void onLoadStarted(PhotoView container, String uri, BitmapDisplayConfig config) {
                        super.onLoadStarted(container, uri, config);
                        // 加载本地图片不用显示圈圈。
                        //LoadingDialog.showDialog(ZoomImageViewActivity.this, "", false);
                        //LoadingDialog.showCircleProgressDialog(ZoomImageViewActivity.this);
                    }

                    @Override
                    public void onLoading(PhotoView container, String uri, BitmapDisplayConfig config, long total, long current) {

                    }


                });
            } else if (!ToolsHelper.isNull(item.getImageStorageId())) {
                ImageLoaderManager.getInstance().displayImage(mContext, item.getImageStorageId(), bitmapUtils, view);
            }
//            view.setImageBitmap(item);
            if (!this.imageViews.contains(view)) {
                imageViews.add(view);
            }

            view.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (null != mOnItemClickListener)
                        mOnItemClickListener.onItemClick();
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!ToolsHelper.isNull(item.getPath())) {
                        bmp = bitmapUtils.getBitmapFileFromDiskCache(item.getPath());
                    } else if (!ToolsHelper.isNull(item.getImageStorageId())) {
                        String fullUrl = Constants.getCoreUrls().getDownloadUrl(item.getImageStorageId());
                        fullUrl = fullUrl + "?type=original";//设置下载type original-原图  standard-标准图  thumbnail-缩略图
                        bmp = bitmapUtils.getBitmapFileFromDiskCache(fullUrl);
                    }
                    if (null == bmp) {
                        ToolsHelper.showStatus(mContext, false, mContext.getString(R.string.galleryadapter_saveimage_failed));
                        return false;
                    }
                    String[] menu2 = {mContext.getString(R.string.common_save_image)};
                    AlertDialog dialog2 = new AlertDialog.Builder(mContext).setItems(menu2, new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 保存图片到本地
                            File file = new File(FileHelper.getSaveBitmapPath());
                            if (file.exists())
                                file.delete();
                            File f = file.getParentFile();
                            if (!f.exists())
                                f.mkdirs();
                            if (!f.isDirectory() && f.canWrite()) {
                                f.delete();
                                f.mkdirs();
                            }
                            try {
                                FileHelper.copyFile(bmp, file);
                                ToolsHelper.showStatus(mContext, true,mContext.getString(R.string.galleryadapter_saveimage_success));
                            } catch (IOException e) {
                                e.printStackTrace();
                                ToolsHelper.showStatus(mContext, false, mContext.getString(R.string.galleryadapter_saveimage_failed));
                            }
                        }
                    }).create();
                    dialog2.show();

                    return false;
                }
            });


        }
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }
}
