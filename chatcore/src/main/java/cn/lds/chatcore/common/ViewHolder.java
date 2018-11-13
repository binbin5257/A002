package cn.lds.chatcore.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;

/**
 * Created by Administrator on 2016/8/24.
 */
public class ViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private BitmapUtils bitmapUtils;
    private Context mContext;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mPosition = position;
        this.mContext = context;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        bitmapUtils = BitmapHelper.getBitmapUtils(mContext.getResources().getDrawable(R.drawable.car_loading), mContext
                .getResources().getDrawable(R.drawable.car_loaderr));
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为控件设置显隐
     *
     * @param viewId
     * @param value
     * @return
     */
    public ViewHolder setVisibility(int viewId, boolean value) {
        View view = getView(viewId);
        if (value)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param colorId
     * @return
     */
    public ViewHolder setTextColor(int viewId, int colorId) {
        TextView view = getView(viewId);
        view.setTextColor(colorId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public ViewHolder onClick(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ViewHolder setImageByUrl(int viewId, String url) {
        if (null == bitmapUtils)
            bitmapUtils = BitmapHelper.getBitmapUtils(mContext.getResources().getDrawable(R.drawable.car_loading),
                    mContext.getResources().getDrawable(R.drawable.car_loaderr));
        bitmapUtils.display((ImageView) getView(viewId), url);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ViewHolder setImageByUrlForMaxWidth(int viewId, String url) {
        if (null == bitmapUtils)
            bitmapUtils = BitmapHelper.getBitmapUtils(mContext.getResources().getDrawable(R.drawable.car_loading),
                    mContext.getResources().getDrawable(R.drawable.car_loaderr));

        int screenWidth = MyApplication.getInstance().getWidth();
        ImageView iv = (ImageView) getView(viewId);
        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        iv.setLayoutParams(lp);

        iv.setMaxWidth(screenWidth);
        iv.setMaxHeight((int) (screenWidth * 5));// 这里其实可以根据需求而定，我这里测试为最大宽度的1.5倍

        bitmapUtils.display(iv, url);

        return this;
    }

    public int getPosition() {
        return mPosition;
    }

}
