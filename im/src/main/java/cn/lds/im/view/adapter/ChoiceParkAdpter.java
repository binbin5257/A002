package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.lds.chat.R;
import cn.lds.chatcore.data.MapAllParkModel;

public class ChoiceParkAdpter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MapAllParkModel.DataBean> mlist;
    private int mType = -1;//  1 历史取车点  |  2 实时取还车点

    public ChoiceParkAdpter(Context context, ArrayList<MapAllParkModel.DataBean> list, int type) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mlist = list;
        mType = type;
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
            convertView = mInflater.inflate(R.layout.item_choicepark, null);

            holder.parkaddress = (TextView) convertView.findViewById(R.id.item_choicepark_parkaddress);
            holder.parkname = (TextView) convertView.findViewById(R.id.item_choicepark_parkname);
            holder.parkcount = (TextView) convertView.findViewById(R.id.item_choicepark_parkcount);
            holder.distance = (TextView) convertView.findViewById(R.id.item_choicepark_distance);
            holder.lastline = (ImageView) convertView.findViewById(R.id.item_choicepark_lastline);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MapAllParkModel.DataBean item = mlist.get(position);
        holder.parkaddress.setText(item.getAddress());
        if (mType == 1) {
            holder.parkname.setText(item.getName());
            if (position == mlist.size() - 1)
                holder.lastline.setVisibility(View.GONE);
            else
                holder.lastline.setVisibility(View.VISIBLE);
        } else {
            if (0 == position) {
                holder.parkname.setText("推荐：" + item.getName());
                holder.parkname.setTextColor(mContext.getResources().getColor(R.color.textcolor_orange));
                holder.parkcount.setTextColor(mContext.getResources().getColor(R.color.textcolor_orange));
            } else
                holder.parkname.setText(item.getName());

            holder.parkcount.setText(" ( " + item.getAvailableVehicle() + " 辆车）");
            DecimalFormat df = new DecimalFormat("0.0");
            double a = item.getDistance() / (double) 1000;
            holder.distance.setText("距离" + df.format(a) + "km");
        }
        return convertView;
    }

    final private class ViewHolder {
        public TextView distance;
        public TextView parkaddress;
        public TextView parkcount;
        public TextView parkname;
        public ImageView lastline;
    }

}
