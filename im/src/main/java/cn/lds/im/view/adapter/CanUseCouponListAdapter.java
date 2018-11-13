package cn.lds.im.view.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.im.data.CouponListModel;
import cn.lds.im.enums.CouponStatus;
import cn.lds.im.enums.CouponStatusEnum;

/**
 * 优惠劵
 * Created by gaihd on 2017/7/20.
 */

public class CanUseCouponListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CouponListModel.DataBean> mList;
    private LayoutInflater inflater;
    private final DecimalFormat df;

    public CanUseCouponListAdapter(Context context, List<CouponListModel.DataBean> list) {
        this.mContext = context;
        this.mList = list;
        this.inflater = LayoutInflater.from(mContext);
        df = new DecimalFormat("#,##0.00");

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CouponListModel.DataBean dataBean = (CouponListModel.DataBean) getItem(position);

        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_can_use_coupon_list, null);
            holder.coupon_amount = (TextView) convertView.findViewById(R.id.coupon_amount);
            holder.coupon_amount_tv = (TextView) convertView.findViewById(R.id.coupon_amount_tv);
            holder.coupon_title = (TextView) convertView.findViewById(R.id.coupon_title);
            holder.coupon_date = (TextView) convertView.findViewById(R.id.coupon_date);
            holder.coupon_description = (TextView) convertView.findViewById(R.id.coupon_description);
            holder.useBtn = (Button) convertView.findViewById(R.id.btn_use);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String status = dataBean.getStatus();
        holder.useBtn.setText(CouponStatusEnum.valueOf(status).value());
        holder.useBtn.setBackgroundResource(R.drawable.bg_coupon_use);
        holder.useBtn.setTextColor(mContext.getResources().getColor(R.color.coupon_color));


        holder.coupon_amount.setText(df.format(ToolsHelper.stringTOdouble(dataBean.getPrice()+"")/100) + "");
        if(ToolsHelper.isNull(dataBean.getLimitedPrice())) {
            holder.coupon_amount_tv.setVisibility(View.GONE);
        }else {
            holder.coupon_amount_tv.setVisibility(View.VISIBLE);
            holder.coupon_amount_tv.setText("满" + ToolsHelper.stringTOdouble(dataBean.getLimitedPrice())/100 + "元可使用");
        }
        holder.coupon_title.setText(ToolsHelper.isNullString(dataBean.getTitle()));
        if(ToolsHelper.isNull(dataBean.getEndTime())){
            holder.coupon_date.setText("到期时间：");
        }else {
            holder.coupon_date.setText("到期时间：" + TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getEndTime()),TimeHelper.FORMAT11));
        }
        holder.useBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUseConponListener.immediatelyUseConponEvent(mList.get(position));
            }
        });
        holder.coupon_description.setText(Html.fromHtml(ToolsHelper.isNullString(dataBean.getDescription())));
        return convertView;
    }
    class ViewHolder{
        private TextView coupon_amount;
        private TextView coupon_amount_tv;
        private TextView coupon_title;
        private TextView coupon_date;
        private TextView coupon_description;
        private Button useBtn;

    }

    private OnUseConponListener onUseConponListener;

    public void setOnUseConponListener(OnUseConponListener onUseConponListener) {
        this.onUseConponListener = onUseConponListener;
    }

    public interface OnUseConponListener{
        void immediatelyUseConponEvent(CouponListModel.DataBean dataBean);
    }
}
