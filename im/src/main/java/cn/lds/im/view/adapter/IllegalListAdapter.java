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
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.im.data.IllegalListModel;

/**
 * 违章适配器
 */

public class IllegalListAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected ArrayList<IllegalListModel.DataBean> mlist;

    public IllegalListAdapter(Context context, ArrayList<IllegalListModel.DataBean> list) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        IllegalListModel.DataBean dataBean = (IllegalListModel.DataBean) getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_illegallistview, null);

            holder.model = (TextView) convertView.findViewById(R.id.tv_car_model);
            holder.status = (TextView) convertView.findViewById(R.id.tv_car_status);
            holder.btn_status = (ImageView) convertView.findViewById(R.id.status_iv);
            holder.reason = (TextView) convertView.findViewById(R.id.tv_reason);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.model.setText(ToolsHelper.isNullString(dataBean.getBrandName())
                + ToolsHelper.isNullString(dataBean.getSeriesName()) );
        holder.reason.setText(dataBean.getIllegalDescription());
        if (dataBean.isCleaned()) {
            holder.status.setText("已处理");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.illegal_unclean));
            holder.btn_status.setBackground(mContext.getResources().getDrawable(R.drawable.bg_illegal_cleaned));
        } else {
            holder.status.setText("未处理");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.illegal_cleaned));
            holder.btn_status.setBackground(mContext.getResources().getDrawable(R.drawable.bg_illegal_unclean));
        }
        holder.date.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getIllegalTime()),TimeHelper.FORMAT11));
        return convertView;
    }

    class ViewHolder {
        protected TextView model;
        protected TextView status;
        protected ImageView btn_status;
        protected TextView reason;
        protected TextView date;
    }
}
