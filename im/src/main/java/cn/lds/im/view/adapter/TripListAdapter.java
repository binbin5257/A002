package cn.lds.im.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.im.data.TripListModel;
import cn.lds.im.enums.TripStatus;
import cn.lds.im.enums.TripStatusEnum;

/**
 * 行程列表 适配器
 * Created by sibinbin on 2017/7/19.
 */

public class TripListAdapter extends BaseAdapter {

    private Context mContext;
    private List<TripListModel.DataBean> mTripBeanList;
    private LayoutInflater inflater;
    private DecimalFormat df;
    private String flag;

    public TripListAdapter(Context context, List<TripListModel.DataBean> tripBeens,String flag) {
        this.mContext = context;
        this.mTripBeanList = tripBeens;
        this.inflater = LayoutInflater.from(mContext);
        this.flag = flag;
        df = new DecimalFormat("#,##0.00");

    }

    @Override
    public int getCount() {
        return mTripBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTripBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        TripListModel.DataBean dataBean = (TripListModel.DataBean) getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_trip_list, null);
            holder.car_model = (TextView) convertView.findViewById(R.id.car_model);
            holder.trip_status = (TextView) convertView.findViewById(R.id.trip_status);
            holder.trip_time = (TextView) convertView.findViewById(R.id.trip_time);
            holder.trip_start = (TextView) convertView.findViewById(R.id.trip_start);
            holder.trip_end = (TextView) convertView.findViewById(R.id.trip_end);
            holder.tv_order_cost = (TextView) convertView.findViewById(R.id.tv_order_cost);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.car_model.setText(dataBean.getPlateLicenseNo() +"("+ToolsHelper.isNullString(dataBean.getVehicleBrandName())
                + ToolsHelper.isNullString(dataBean.getVehicleSeriesName())+")");
        String status = dataBean.getStatus();
        if (!ToolsHelper.isNull(status)) {
            switch (status) {
                case TripStatus.OPEN://待分配
                    holder.tv_order_cost.setVisibility( View.GONE );
                    holder.trip_status.setText(TripStatusEnum.valueOf(status).value());
                    holder.trip_status.setTextColor(mContext.getResources().getColor(R.color.submenu_identification));
                    break;
                case TripStatus.ALLOCATED://已分配
                    holder.tv_order_cost.setVisibility( View.GONE );
                    holder.trip_status.setText(TripStatusEnum.valueOf(status).value());
                    holder.trip_status.setTextColor(mContext.getResources().getColor(R.color.submenu_identification));
                    break;
                case TripStatus.PICKED_UP://已取车
                    holder.tv_order_cost.setVisibility( View.GONE );
                    holder.trip_status.setText(TripStatusEnum.valueOf(status).value());
                    holder.trip_status.setTextColor(mContext.getResources().getColor(R.color.submenu_identification));
                    break;
                case TripStatus.DROPPED_OFF://已还车
                    holder.tv_order_cost.setVisibility( View.VISIBLE );
                    holder.trip_status.setText(TripStatusEnum.valueOf(status).value());
                    holder.trip_status.setTextColor(mContext.getResources().getColor(R.color.submenu_identification));
                    break;
                case TripStatus.RETURN_OVERDUE://逾期未还车
                    holder.tv_order_cost.setVisibility( View.GONE );
                    holder.trip_status.setText(TripStatusEnum.valueOf(status).value());
                    holder.trip_status.setTextColor(mContext.getResources().getColor(R.color.coupon_color));
                    break;
                case TripStatus.CANCELLED://已取消
                    holder.tv_order_cost.setVisibility( View.GONE );
                    holder.trip_status.setText(TripStatusEnum.valueOf(status).value());
                    holder.trip_status.setTextColor(mContext.getResources().getColor(R.color.login_hint_color));
                    break;
                case TripStatus.PAID://已支付
                    holder.tv_order_cost.setVisibility( View.VISIBLE );
                    holder.trip_status.setText(TripStatusEnum.valueOf(status).value());
                    holder.trip_status.setTextColor(mContext.getResources().getColor(R.color.login_hint_color));
                    break;
                case TripStatus.PAYMENT_OVERDUE://逾期未支付
                    holder.tv_order_cost.setVisibility( View.VISIBLE );
                    holder.trip_status.setText(TripStatusEnum.valueOf(status).value());
                    holder.trip_status.setTextColor(mContext.getResources().getColor(R.color.coupon_color));
                    break;

            }
            if("OTHER".equals(flag)){
                holder.tv_order_cost.setVisibility( View.VISIBLE );
                holder.tv_order_cost.setText(dataBean.getEnrolleeName()+"  " + dataBean.getEnrolleePhone());
            }else if("OWNER".equals(flag)){
                if(holder.tv_order_cost.getVisibility() == View.VISIBLE){
                    if("PERSONAL".equals( dataBean.getOrderVehicleType() )){
                        holder.tv_order_cost.setText( "私人用车费用：¥ " + df.format( dataBean.getPersonalAmount()*1.0/100 ));
                    }else{
                        holder.tv_order_cost.setText( "公务用车费用：¥ " + df.format( dataBean.getPersonalAmount()*1.0/100 ));
                    }
                }
            }

        }
        if(TextUtils.isEmpty(dataBean.getPickedUpTime())){
            holder.trip_time.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getReservationTime()), TimeHelper.FORMAT12));
        }else{
            holder.trip_time.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getPickedUpTime()), TimeHelper.FORMAT12));
        }
        //是否需要送车上门（true--需要）
        boolean delivered = dataBean.isDelivered();
        //是否需要上门取车（true--需要）
        boolean pickuped = dataBean.isPickuped();
        if (delivered && pickuped) {
            holder.trip_start.setText(ToolsHelper.isNullString(dataBean.getDeliverAddress()));
            holder.trip_end.setText(ToolsHelper.isNullString(dataBean.getPickupAddress()));
        } else if ((!delivered) && pickuped) {
            holder.trip_start.setText(ToolsHelper.isNullString(dataBean.getReservationLocationAddress()));
            holder.trip_end.setText(ToolsHelper.isNullString(dataBean.getPickupAddress()));
        } else if (delivered && (!pickuped)) {
            holder.trip_start.setText(ToolsHelper.isNullString(dataBean.getDeliverAddress()));
            holder.trip_end.setText(ToolsHelper.isNullString(dataBean.getReturnLocationAddress()));
        } else {
            holder.trip_start.setText(dataBean.getReservationLocationAddress());
            holder.trip_end.setText(dataBean.getReturnLocationAddress());
        }


        return convertView;
    }

    class ViewHolder {
        private TextView car_model;
        private TextView trip_status;
        private TextView trip_time;
        private TextView trip_start;
        private TextView trip_end;
        private TextView tv_order_cost;
    }
}
