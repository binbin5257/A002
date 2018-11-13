package cn.lds.chatcore.view.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lds.chatcore.R;
import cn.lds.chatcore.data.PopData;

/**
 * Created by xuqm on 2016/6/8.
 */
public class PopMapAdapter extends BaseAdapter {

    private List<PopData> dataList;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isShowIcon = false;

    public PopMapAdapter(Context mContext, List<PopData> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setShowIcon(boolean showIcon) {
        isShowIcon = showIcon;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public PopData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopData item = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_pop_map, null);
            holder.item_pop_menu_icon_iv = (ImageView) convertView.findViewById(R.id.item_pop_map_icon_iv);
            holder.item_pop_menu_select_iv = (TextView) convertView.findViewById(R.id.item_pop_map_select_iv);
            holder.item_pop_menu_text_tv = (TextView) convertView.findViewById(R.id.item_pop_map_text_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (isShowIcon) {
            holder.item_pop_menu_icon_iv.setImageResource(item.getImageID());
            holder.item_pop_menu_text_tv.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            holder.item_pop_menu_icon_iv.setVisibility(View.GONE);
            holder.item_pop_menu_select_iv.setVisibility(View.GONE);
            holder.item_pop_menu_text_tv.setGravity(Gravity.CENTER);
        }

        holder.item_pop_menu_text_tv.setText(item.getText());
        if (item.isSelect() && isShowIcon)
            holder.item_pop_menu_select_iv.setText("打开");
        else
            holder.item_pop_menu_select_iv.setText("未下载");

        return convertView;
    }

    private class ViewHolder {
        public ImageView item_pop_menu_icon_iv;
        public TextView item_pop_menu_select_iv;
        public TextView item_pop_menu_text_tv;
    }

}
