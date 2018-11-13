package cn.lds.im.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.im.data.CityBean;
import cn.lds.im.data.LocationCtiyModel;

/**
 * Created by E0608 on 2017/9/25.
 */

public class CityListAdapter extends BaseAdapter {

    private Context mContext;

    private List<?> mList;

    private LayoutInflater inflater;


    public CityListAdapter(Context context, List<?> list){
        this.mContext = context;
        this.mList = list;
        this.inflater = LayoutInflater.from(context);
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

        View view = inflater.inflate(R.layout.item_city,null);
        TextView name = (TextView) view.findViewById(R.id.city_name);
        LocationCtiyModel.DataBean bean = (LocationCtiyModel.DataBean) mList.get(position);
        if(bean.isSelected()){
            name.setTextColor(mContext.getResources().getColor(R.color.important_color_blue));
        }else{
            name.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        name.setText(bean.getName());
        return view;
    }
}
