package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.im.data.ReceiptListModel;

/**
 * 行程列表 适配器
 * Created by sibinbin on 2017/7/19.
 */

public class ReceiptListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ReceiptListModel.DataBean> mList;
    private LayoutInflater inflater;
    public ReceiptListAdapter(Context context, List<ReceiptListModel.DataBean> list) {
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
        ViewHolder holder = null;
        ReceiptListModel.DataBean dataBean = (ReceiptListModel.DataBean) getItem(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_receipt_list, null);
            holder = new ViewHolder();

            holder.car_model = (TextView) convertView.findViewById(R.id.car_model);
            holder.trip_status = (TextView) convertView.findViewById(R.id.trip_status);
            holder.receipt_name = (TextView) convertView.findViewById(R.id.receipt_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String amount = dataBean.getAmount();
        if(ToolsHelper.isNull(amount)){
            holder.car_model.setText("金额" + "0.00" + "元");
        }else {
            holder.car_model.setText("金额" + df.format(ToolsHelper.stringTOdouble(amount)/100) + "元");
        }
        String createDate = dataBean.getCreatedDate();
        if(ToolsHelper.isNull(createDate)){
            holder.trip_status.setText("");
        } else {
            if(TimeHelper.isToday(ToolsHelper.stringTOlong(createDate))){
                holder.trip_status.setText("今天");
            }else {
                holder.trip_status.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(createDate), TimeHelper.FORMAT11));
            }
        }
        holder.receipt_name.setText(ToolsHelper.isNullString(dataBean.getTitle()));
        return convertView;
    }
    class ViewHolder{
        protected TextView car_model;
        protected TextView trip_status;
        protected TextView receipt_name;
    }
}
