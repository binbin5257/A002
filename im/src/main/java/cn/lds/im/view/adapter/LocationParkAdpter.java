package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BaiduMapHelper;
import cn.lds.im.data.ReservationOrdersLocationsModel;

public class LocationParkAdpter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ReservationOrdersLocationsModel.DataBean> mlist;

    public LocationParkAdpter(Context context, ArrayList<ReservationOrdersLocationsModel.DataBean> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // convertView为null的时候初始化convertView。
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_locationpark, null);

            holder.parkaddress = (TextView) convertView.findViewById(R.id.item_locationpark_parkaddress);
            holder.parkname = (TextView) convertView.findViewById(R.id.item_locationpark_parkname);
            holder.isSelected = (ImageView) convertView.findViewById(R.id.item_locationpark_seletecd);
            holder.range = (TextView) convertView.findViewById(R.id.item_locationpark_range);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ReservationOrdersLocationsModel.DataBean item = mlist.get(position);
        holder.parkaddress.setText(item.getAddress());
        holder.parkname.setText(item.getName());
        if (item.isSelected()) {
            holder.parkname.setTextColor(mContext.getResources().getColor(R.color.important_color_blue));
            holder.range.setTextColor(mContext.getResources().getColor(R.color.important_color_blue));
            holder.isSelected.setBackgroundResource(R.drawable.icon_choice_star);
        } else {
            holder.parkname.setTextColor(mContext.getResources().getColor(R.color.normal_text_color_grey));
            holder.range.setTextColor(mContext.getResources().getColor(R.color.normal_text_color_light_grey));
            holder.isSelected.setBackgroundResource(R.drawable.icon_choice_location);
        }

        double distance = BaiduMapHelper.getDistance(MyApplication.myLocation.longitude, MyApplication.myLocation.latitude,
                item.getLongitude(), item.getLatitude());
        holder.range.setText(String.valueOf((int)distance / 1000) + "km");
        return convertView;
    }

    final private class ViewHolder {
        public ImageView isSelected;
        public TextView parkaddress;
        public TextView parkname;
        public TextView range;
    }

}
