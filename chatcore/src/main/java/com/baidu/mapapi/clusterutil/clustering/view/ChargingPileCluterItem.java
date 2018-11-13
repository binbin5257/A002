package com.baidu.mapapi.clusterutil.clustering.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;

import cn.lds.chatcore.R;
import cn.lds.chatcore.data.ChargingModel;
import cn.lds.chatcore.data.MapAllParkModel;

/**
 * Created by Administrator on 2016/7/8.
 */
public class ChargingPileCluterItem implements ClusterItem {
    private final LatLng mPosition;
    private ChargingModel.DataBean mDataBean;
    private Context mActivity;

    public ChargingPileCluterItem(LatLng latLng, ChargingModel.DataBean dataBean, Context mActivity) {
        mPosition = latLng;
        mDataBean = dataBean;
        this.mActivity = mActivity;
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
        textView.setBackgroundResource(R.drawable.location_mark);
        textView.setTextColor(mActivity.getResources().getColor(R.color.textgray));
        textView.setTextSize(12);
        textView.setText(mDataBean.getAvailableDc() + mDataBean.getAvailableAc() + "");
        BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(textView);
        return bdA;
    }

    public ChargingModel.DataBean getmDataBean() {
        return mDataBean;
    }
}
