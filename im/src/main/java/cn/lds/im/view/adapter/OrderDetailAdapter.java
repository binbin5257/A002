package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.im.data.OrderDetailInfoModel;
import cn.lds.im.view.OrderDetailActivity;

/**
 * Created by E0608 on 2017/12/28.
 */

public class OrderDetailAdapter extends BaseAdapter {

    private Context mContext;
    List<OrderDetailInfoModel.DataBean.CourseListBean> mCourseList;
    private final DecimalFormat df;

    public OrderDetailAdapter(Context context, List<OrderDetailInfoModel.DataBean.CourseListBean> courseList) {
        this.mContext = context;
        this.mCourseList = courseList;
        df = new DecimalFormat("#,##0.00");
    }

    @Override
    public int getCount() {
        return mCourseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_order_detail,null);
            holder = new ViewHolder();
            holder.orderDataTv = (TextView) convertView.findViewById(R.id.order_data_tv);
            holder.datTimeTv = (TextView) convertView.findViewById(R.id.day_night);
            holder.odometerTv = (TextView) convertView.findViewById(R.id.tv_odometer);
            holder.odometerCostTv = (TextView) convertView.findViewById(R.id.tv_odometer_cost);
            holder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.timeCostTv = (TextView) convertView.findViewById(R.id.tv_time_cost);
            holder.cappedPriceTv = (TextView) convertView.findViewById(R.id.tv_capped_price);



            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        OrderDetailInfoModel.DataBean.CourseListBean courseListBean = mCourseList.get(position);
        holder.orderDataTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT11, courseListBean.getStartTime()).toString().trim());
        if(courseListBean.isDayTime()){
            if(courseListBean.isPublicUsage()){
                holder.datTimeTv.setText("白天公务");
            }else{
                holder.datTimeTv.setText("白天私用");
            }
        }else{
            holder.datTimeTv.setText("夜间私用");
        }
        holder.odometerTv.setText(courseListBean.getOdometer() + "km");
        holder.odometerCostTv.setText(df.format(courseListBean.getDistanceAmount()/100 )+ "元");
        int min = 0;
        if(courseListBean.getTimeMilli() / 1000 % 60 == 0){
             min = courseListBean.getTimeMilli() / 1000 / 60;
        }else{
             min = courseListBean.getTimeMilli() / 1000 / 60 + 1;
        }

        if(min > 60){
            if(min % 60 == 0){
                holder.timeTv.setText(min / 60 + "小时");
            }else{
                holder.timeTv.setText(min / 60 + "小时" + min % 60 +"分钟");
            }

        }else{
            holder.timeTv.setText(min + "分钟");
        }
        holder.timeCostTv.setText(df.format(courseListBean.getTimeAmount()/100) + "元");
        holder.cappedPriceTv.setText(df.format(courseListBean.getCappedPrice()/100) + "元");
        return convertView;
    }

    public class ViewHolder{
        private TextView orderDataTv;
        private TextView datTimeTv;
        private TextView odometerTv;
        private TextView odometerCostTv;
        private TextView timeTv;
        private TextView timeCostTv;
        private TextView cappedPriceTv;
    }
}
