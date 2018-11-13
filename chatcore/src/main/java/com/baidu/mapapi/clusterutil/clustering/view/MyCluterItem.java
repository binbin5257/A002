package com.baidu.mapapi.clusterutil.clustering.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.TextView;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;

import cn.lds.chatcore.R;
import cn.lds.chatcore.data.MapAllParkModel;

/**
 * Created by Administrator on 2016/7/8.
 */
public class MyCluterItem implements ClusterItem {
    private final LatLng mPosition;
    private MapAllParkModel.DataBean mDataBean;
    private Context mActivity;
    private int mType;

    public MyCluterItem(LatLng latLng, MapAllParkModel.DataBean dataBean, Context mActivity,int type) {
        mPosition = latLng;
        mDataBean = dataBean;
        this.mActivity = mActivity;
        this.mType = type;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        TextView textView = new TextView(mActivity);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setPadding(0, 15, 0, 0);
        textView.setBackgroundResource(mDataBean.getResId());
        textView.setTextColor(mActivity.getResources().getColor(R.color.textgray));
        textView.setTextSize(12);
        textView.setText(mDataBean.getAvailableVehicle() + "");
        if(mType == 0){
            return BitmapDescriptorFactory.fromResource(mDataBean.getResId());
        }else{
            return BitmapDescriptorFactory.fromView(textView);
        }


    }

    public MapAllParkModel.DataBean getmDataBean() {
        return mDataBean;
    }
}
