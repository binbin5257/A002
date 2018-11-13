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
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.db.MessageTable;

/**
 * 活动适配器
 */
public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MessageTable> mlist;

    public MessageAdapter(Context context, ArrayList<MessageTable> list) {
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
        MessageTable model = (MessageTable) getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_messagelistview, null);

            holder.title = (TextView) convertView.findViewById(R.id.item_messagelistview_title);
            holder.time = (TextView) convertView.findViewById(R.id.item_messagelistview_time);
            holder.content = (TextView) convertView.findViewById(R.id.item_messagelistview_content);
            holder.isnew = (ImageView) convertView.findViewById(R.id.item_messagelistview_isnew);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (model.isNew())
            holder.isnew.setVisibility(View.VISIBLE);
        else
            holder.isnew.setVisibility(View.GONE);

        holder.title.setText(model.getTitle());
        holder.time.setText(TimeHelper.getDateStringString(model.getTime()));
        holder.content.setText(model.getContent());


        return convertView;
    }

    final private class ViewHolder {
        private TextView title;
        private TextView time;
        private TextView content;
        private ImageView isnew;
    }
}
