package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;

/**
 * Created by E0608 on 2017/10/26.
 */

public class CallChargingAndServiceAdapter extends BaseAdapter{

    private final LayoutInflater inflater;
    private final List<String> mList;

    public CallChargingAndServiceAdapter(Context context,List<String> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list;
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
        View view = inflater.inflate(R.layout.item_call_charging,null);
        TextView tv = (TextView) view.findViewById(R.id.name);
        tv.setText(mList.get(position));
        return view;
    }
}
