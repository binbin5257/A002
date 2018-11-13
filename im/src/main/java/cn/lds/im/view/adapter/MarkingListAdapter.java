package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.im.data.MarkingListModel;

/**
 * Created by E0608 on 2017/12/26.
 */

public class MarkingListAdapter extends BaseAdapter {


    private List<MarkingListModel.DataBean> mList;
    private Context mContext;
    private final LayoutInflater inflater;

    public MarkingListAdapter(Context context, List<MarkingListModel.DataBean> list) {
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
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_apply_marking,null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        MarkingListModel.DataBean dataBean = mList.get(position);
        holder.title.setText(dataBean.getDescription());
        holder.time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT11, Long.parseLong(dataBean.getUserDate()))
                + "  "+TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, dataBean.getStartTime())
                + "~"+TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, dataBean.getEndTime()));        if(dataBean.getStatus().equals("APPOVING")){
            holder.status.setText("待审批");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.important_color_blue));
        }else if(dataBean.getStatus().equals("APPROVED")){
            holder.status.setText("已批准");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.textcolor80));
        }else if(dataBean.getStatus().equals("REJECTED")){
            holder.status.setText("已驳回");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.red));
        }else if(dataBean.getStatus().equals("WITHDRAW")){
            holder.status.setText("已撤回");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.textcolor80));
        }else if(dataBean.getStatus().equals("USED")){
            holder.status.setText("已使用");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.textcolor80));
        }

        return convertView;
    }
    public class ViewHolder{
        private TextView title;
        private TextView time;
        private TextView status;
    }
}
