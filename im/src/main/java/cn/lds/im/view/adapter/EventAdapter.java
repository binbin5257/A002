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
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.im.data.EventModel;

/**
 * 活动适配器
 */
public class EventAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected ArrayList<EventModel.DataBean> mlist;

    public EventAdapter(Context context, ArrayList<EventModel.DataBean> list) {
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
        EventModel.DataBean dataBean = (EventModel.DataBean) getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_eventlistview, null);

            holder.ivbg = (ImageView) convertView.findViewById(R.id.item_eventlistview_ivbg);
            holder.content = (TextView) convertView.findViewById(R.id.item_eventlistview_content);
            holder.date = (TextView) convertView.findViewById(R.id.item_eventlistview_date);
            holder.summary = (TextView) convertView.findViewById(R.id.item_eventlistview_summary);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.content.setText(ToolsHelper.isNullString(dataBean.getTitle()));
        if(ToolsHelper.isNull(dataBean.getPublishTime())){
            holder.date.setText("");
        }else {
            holder.date.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getPublishTime()),TimeHelper.FORMAT8));
        }
        holder.summary.setText(ToolsHelper.isNullString(dataBean.getSummary()));
        if(ToolsHelper.isNull(dataBean.getThumbnail())){
            holder.ivbg.setImageResource(R.drawable.bg_active_default);
        }else {

            //圆角加载效果
            ImageLoaderManager.getInstance().displayActive(dataBean.getThumbnail(), holder.ivbg);
        }
        return convertView;
    }

    final protected class ViewHolder {
        protected ImageView ivbg;
        protected TextView content;
        protected TextView summary;
        protected TextView date;
    }
}
