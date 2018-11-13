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
import cn.lds.chatcore.MyApplication;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.data.ChargingModel;
import cn.lds.im.data.NearStationModel;
import cn.lds.im.enums.HomeModelEnum;

/**
 * 附近场站列表适配器
 * Created by E0608 on 2017/10/25.
 */

public class NearStationAdapter extends BaseAdapter{

    private List<NearStationModel.DataBean> mList;
    private Context mContext;
    private final LayoutInflater inflater;
    private final DecimalFormat df;
    private int type;

    public NearStationAdapter(Context context, List<NearStationModel.DataBean> list, int type) {
        this.mList = list;
        this.mContext = context;
        this.type = type;
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
            convertView = inflater.inflate(R.layout.item_near_station,null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.availableCar = (TextView) convertView.findViewById(R.id.tv_available_car);
            holder.availablePark = (TextView) convertView.findViewById(R.id.tv_available_park);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        NearStationModel.DataBean dataBean = mList.get(position);
        holder.name.setText(dataBean.getName());
        holder.address.setText(dataBean.getAddress());
        if(type == HomeModelEnum.BUSINESSUSERCAR.getIndex()){
            holder.availableCar.setText(dataBean.getInstantAvailableVehicle() + "");
        }else if(type == HomeModelEnum.PRIVATEUSECAR.getIndex()){
            holder.availableCar.setText(dataBean.getScheduledAvailableVehicle() + "");

        }
        holder.availablePark.setText(dataBean.getParkingLots()  + "");
        holder.distance.setText(df.format(dataBean.getDistance()/1000) + "km");
        return convertView;
    }


    class ViewHolder{
        private TextView name;
        private TextView address;
        private TextView distance;
        private TextView availableCar;
        private TextView availablePark;

    }
}
