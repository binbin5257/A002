package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.im.data.CostDetailModel;
import cn.lds.im.data.EventModel;
import cn.lds.im.enums.CostType;
import cn.lds.im.enums.CostTypeEnum;

import static cn.lds.chatcore.common.TimeHelper.FORMAT7;

/**
 * 费用明细适配器
 */
public class CostDetailAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected ArrayList<CostDetailModel.DataBean> mlist;
    private  DecimalFormat df;

    public CostDetailAdapter(Context context, ArrayList<CostDetailModel.DataBean> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mlist = list;
        df = new DecimalFormat("#,##0.00");
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
        CostDetailModel.DataBean dataBean = (CostDetailModel.DataBean) getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_costdeatillistview, null);

            holder.cost_date = (TextView) convertView.findViewById(R.id.cost_date);
            holder.cost_detail = (TextView) convertView.findViewById(R.id.cost_detail);
            holder.cost_type = (TextView) convertView.findViewById(R.id.cost_type);
            holder.cost_amount = (TextView) convertView.findViewById(R.id.cost_amount);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (ToolsHelper.isNull(dataBean.getCreatedDate())) {
            holder.cost_date.setText("");
        } else {
            holder.cost_date.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getCreatedDate()), TimeHelper.FORMAT8));
        }
        String type = dataBean.getChangeType();
        if(ToolsHelper.isNull(type)){
            holder.cost_type.setText("");
        } else {
            switch (type) {
                case CostType.ACCOUNT_RECHARGE://余额充值
                    notifyData("+",dataBean,holder,type);
                    break;
                case CostType.ACCOUNT_PAY://余额支付
                    notifyData("-",dataBean,holder,type);
                    break;
                case CostType.FOREGIFT_RECHARGE://押金充值
                    notifyData("+",dataBean,holder,type);
                    break;
                case CostType.FOREGIFT_WITHDRAWALS://押金提现
                    notifyData("-",dataBean,holder,type);
                    break;
                case CostType.FOREGIFT_CUT://押金扣款
                    notifyData("-",dataBean,holder,type);
                    break;
                case CostType.WEIXIN_PAY://微信支付
                    notifyData("-",dataBean,holder,type);
                    break;
                case CostType.ALIPAY://支付宝支付
                    notifyData("-",dataBean,holder,type);
                case CostType.FOREGIFT_WITHDRAWALS_APPLY:
                    notifyData("-",dataBean,holder,type);
                    break;
                case CostType.FOREGIFT_WITHDRAWALS_REFUSED:
                    notifyData("+",dataBean,holder,type);
                    break;
                default:
                    break;
            }
        }

        return convertView;
    }

    /**
     * 刷新数据
     */
    private void notifyData(String subOrLess, CostDetailModel.DataBean dataBean, ViewHolder holder, String type) {
        holder.cost_type.setText(CostTypeEnum.valueOf(type).value());
        holder.cost_detail.setText(CostTypeEnum.valueOf(type).value() + "成功");
        if(ToolsHelper.isNull(dataBean.getChangeAmount())){
            holder.cost_amount.setText("");
        } else {
            holder.cost_amount.setText(subOrLess + df.format(ToolsHelper.stringTOdouble(dataBean.getChangeAmount())/100));
        }
    }

    final protected class ViewHolder {
        protected TextView cost_date;
        protected TextView cost_detail;
        protected TextView cost_type;
        protected TextView cost_amount;
    }
}
