package cn.lds.im.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.im.data.ChargingModel;

/**
 * Created by E0608 on 2017/10/25.
 */

public class ChargingAdapter extends BaseAdapter{

    private List<ChargingModel.DataBean> mList;
    private Context mContext;
    private final LayoutInflater inflater;
    private final DecimalFormat df;

    public ChargingAdapter(Context context, List<ChargingModel.DataBean> list) {
        this.mList = list;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        df = new DecimalFormat("#,##0.00");
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_charging,null);
            holder.onLineRlyt = (RelativeLayout) convertView.findViewById(R.id.rl_online);
            holder.unknowTv = (TextView) convertView.findViewById(R.id.tv_unknow);
            holder.serviceName = (TextView) convertView.findViewById(R.id.service_name);
            holder.chargingName = (TextView) convertView.findViewById(R.id.charging_name);
            holder.chargingAddress = (TextView) convertView.findViewById(R.id.charging_address);
            holder.chargingDistance = (TextView) convertView.findViewById(R.id.charging_distance);
            holder.chargingFastNum = (TextView) convertView.findViewById(R.id.tv_fast_charging);
            holder.chargingSlowNum = (TextView) convertView.findViewById(R.id.tv_slow_charging);
            holder.chargingStatus = (TextView) convertView.findViewById(R.id.charging_status);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ChargingModel.DataBean dataBean = mList.get(position);
        holder.chargingName.setText(dataBean.getName());
        holder.chargingAddress.setText(dataBean.getAddress());
        holder.chargingDistance.setText(df.format(dataBean.getDistance()/1000) + "km");
        holder.chargingFastNum.setText(dataBean.getAvailableDc() + "/" + dataBean.getDcNumCount());
        holder.chargingSlowNum.setText(dataBean.getAvailableAc() + "/" + dataBean.getAcNumCount());
        if(dataBean.getPaymentType().equals("ONLINE")){
            holder.onLineRlyt.setVisibility(View.VISIBLE);
            holder.unknowTv.setVisibility(View.GONE);
        }else{
            holder.onLineRlyt.setVisibility(View.GONE);
            holder.unknowTv.setVisibility(View.VISIBLE);
        }
        if(dataBean.getOperator().equals("TE_LAI_DIAN")){
            holder.serviceName.setText("特来电");
        }else if(dataBean.getOperator().equals("XING_XING_CHONG_DIAN")){
            holder.serviceName.setText("星星充电");
        }else if(dataBean.getOperator().equals("E_CHONG_WANG")){
            holder.serviceName.setText("E充网");
        }
        if(dataBean.getStatus().equals("FREE")){
            holder.chargingStatus.setText("空闲");
            holder.chargingStatus.setTextColor(Color.parseColor("#17D427"));
        }else if(dataBean.getStatus().equals("BUSY")){
            holder.chargingStatus.setText("忙碌");
            holder.chargingStatus.setTextColor(Color.parseColor("#F5A763"));
        }else if(dataBean.getStatus().equals("UNKNOW")){
            holder.chargingStatus.setTextColor(mContext.getResources().getColor(R.color.textcolor80));
            holder.chargingStatus.setText("未知");
        }

        return convertView;
    }


    class ViewHolder{
        private TextView chargingName;
        private TextView chargingAddress;
        private TextView chargingDistance;
        private TextView chargingFastNum;
        private TextView chargingSlowNum;
        private TextView chargingStatus;
        private TextView serviceName;
        private TextView unknowTv; //线下支付
        private RelativeLayout onLineRlyt;
    }
}
