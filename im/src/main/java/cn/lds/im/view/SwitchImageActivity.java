package cn.lds.im.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lds.chatcore.ViewPage.HackyViewPager;
import cn.lds.chatcore.common.BitmapHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.ImageInfo;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chat.R;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.chatcore.view.widget.PhotoView.PhotoView;
import cn.lds.chatcore.view.widget.PhotoView.PhotoViewAttacher;

public class SwitchImageActivity extends BaseActivity {
    protected String TAG = "SwitchImageActivity";
    protected Context mContext;
    protected static final String ISLOCKED_ARG = "isLocked";

    protected ViewPager mViewPager;
    public int mPosition = 0;//定位显示点击的图片

    protected List<ImageInfo> items;
    protected Map<String, ViewItem> map_view = new HashMap<>();

    protected int layoutID = R.layout.activity_switch_image;
    protected SwitchImageActivity activity;

    protected void setActivity(SwitchImageActivity activity) {
        this.activity = activity;
    }
    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        initConfig(savedInstanceState);
    }

    protected void initConfig(Bundle savedInstanceState) {
        init(savedInstanceState);
    }

    protected void init(Bundle savedInstanceState) {
        mContext = SwitchImageActivity.this;
        mViewPager = (HackyViewPager) findViewById(R.id.aview_pager);
//        setContentView(mViewPager);
        //获取到所有图片
        items = (ArrayList<ImageInfo>) getIntent().getExtras().getSerializable("items");
        mPosition = getIntent().getIntExtra("position", 0);
        if (null == items)
            return;
        map_view.clear();
        SamplePagerAdapter samplePagerAdapter = new SamplePagerAdapter(items, map_view, mPosition);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(samplePagerAdapter);
        samplePagerAdapter.setOnItemClickListener(new SamplePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                finish();
            }
        });

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position != mPosition) {//显示控制  因为第一次进入时  数据是空的  所以第一张图片的显示不在这里设置
//                    PhotoView view = (PhotoView) mViewPager.getChildAt(position);
                    PhotoView view = map_view.get(items.get(position).getImageStorageId() + position).view;
                    final ImageInfo item = items.get(position);
                    if (!ToolsHelper.isNull(item.getPath())) {
                        map_view.get(items.get(position).getImageStorageId() + position).bitmapUtils.display(view, item.getPath(), new BitmapLoadCallBack<PhotoView>() {
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
                            }

                            @Override
                            public void onLoading(PhotoView container, String uri, BitmapDisplayConfig config, long total, long current) {

                            }


                        });
                    } else if (!ToolsHelper.isNull(item.getImageStorageId())) {
                        ImageLoaderManager.getInstance().displayImage(mContext, item.getImageStorageId(), map_view.get(items.get(position).getImageStorageId() + position).bitmapUtils, view);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(mPosition);

    }

    static class SamplePagerAdapter extends PagerAdapter {
        File bmp = null;
        List<ImageInfo> items;
        Map<String, ViewItem> map_view;
        int mPosition;
        protected OnItemClickListener mOnItemClickListener;//adapter点击事件

        public void setOnItemClickListener(OnItemClickListener l) {
            mOnItemClickListener = l;
        }

        public interface OnItemClickListener {
            void onItemClick();
        }

        public SamplePagerAdapter(List<ImageInfo> item, Map<String, ViewItem> map_view, int mPosition) {
            this.items = item;
            this.map_view = map_view;
            this.mPosition = mPosition;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public View instantiateItem(final ViewGroup container, int position) {


            final ImageInfo item = items.get(position);

            final PhotoView view = new PhotoView(container.getContext());
//            view.setImageResource(sDrawables[position]);
            Drawable drawable = null;
            if (!ToolsHelper.isNull(item.getThumbnail())) {
                byte[] bytes = Base64.decode(item.getThumbnail(), Base64.DEFAULT);
                drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
            view.setImageDrawable(drawable);
            final BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils(drawable);
            if (position == mPosition) {//显示控制   默认显示的图片在这里显示  其他的在onchang监听里面显示
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
                        }

                        @Override
                        public void onLoading(PhotoView container, String uri, BitmapDisplayConfig config, long total, long current) {

                        }


                    });
                } else if (!ToolsHelper.isNull(item.getImageStorageId())) {
                    ImageLoaderManager.getInstance().displayImage(container.getContext(), item.getImageStorageId(), bitmapUtils, view);
                }
            } else {
                map_view.put(item.getImageStorageId() + position, new ViewItem(bitmapUtils, view));
            }

            // Now just add PhotoView to ViewPager and return it
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
                        ToolsHelper.showStatus(container.getContext(),false,container.getContext().getString(R.string.switchimageactivity_savepicture_failed));
                        return false;
                    }
                    String[] menu2 = {container.getContext().getString(cn.lds.chatcore.R.string.common_save_image)};
                    AlertDialog dialog2 = new AlertDialog.Builder(container.getContext()).setItems(menu2, new DialogInterface
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
                                ToolsHelper.showStatus(container.getContext(),true, container.getContext().getString(R.string.switchimageactivity_saveas) + f.getAbsolutePath() + container.getContext().getString(R.string.switchimageactivity_plan));
                            } catch (IOException e) {
                                e.printStackTrace();
                                ToolsHelper.showStatus(container.getContext(),false, container.getContext().getString(R.string.galleryadapter_saveimage_failed));
                            }
                        }
                    }).create();
                    dialog2.show();

                    return false;
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    protected boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    public static class ViewItem {
        protected BitmapUtils bitmapUtils;
        protected PhotoView view;

        public ViewItem(BitmapUtils bitmapUtils, PhotoView view) {
            this.bitmapUtils = bitmapUtils;
            this.view = view;
        }
    }
}
