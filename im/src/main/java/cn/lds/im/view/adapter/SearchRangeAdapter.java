package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.im.data.RangeModel;

/**
 * Created by E0608 on 2017/10/27.
 */

public class SearchRangeAdapter extends BaseAdapter {

    private List<?> mList;
    private Context mContext;
    private final LayoutInflater inflater;

    public SearchRangeAdapter(Context context,List<?> list) {
        this.mList = list;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
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
        RangeModel rangeModel = (RangeModel) mList.get(position);
        TextView name = (TextView) view.findViewById(R.id.city_name);
        if(rangeModel.getMile() == 0){
            name.setText("不限范围");
        }else{
            name.setText(rangeModel.getMile()/1000 + "km");
        }

        if(rangeModel.isSelected()){
            name.setTextColor(mContext.getResources().getColor(R.color.important_color_blue));
        }else{
            name.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        return view;
    }
}
