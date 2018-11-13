package cn.lds.im.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.data.PopData;

/**
 * 选择开票方式adapter
 * Created by E0608 on 2017/7/31.
 */

public class ChoiceReceiptTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PopData> mList;
    private LayoutInflater inflater;

    public ChoiceReceiptTypeAdapter(Context context, List<PopData> list) {
        this.mContext = context;
        this.mList = list;
        this.inflater = LayoutInflater.from(mContext);
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
        View view = inflater.inflate(R.layout.item_choice_receipt,null);
        TextView text = (TextView) view.findViewById(R.id.type_receiipt);
        text.setText(mList.get(position).getText());
        return view;
    }
}
