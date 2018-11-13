package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import cn.lds.chat.R;

/**
 * Created by E0608 on 2017/7/17.
 */

public class ChoicePayWayAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;

    private int PAY_WAY;

    public ChoicePayWayAdapter(Context context, int payway) {
        this.mContext = context;
        this.PAY_WAY = payway;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_pay_way,null);
        ImageView checkView = (ImageView) view.findViewById(R.id.iv_check);
        ImageView payIv = (ImageView) view.findViewById(R.id.iv_pay);
        TextView payTv = (TextView) view.findViewById(R.id.tv_pay);
        if(position == 0){
            payIv.setImageResource(R.drawable.ic_pay);
            payTv.setText("余额支付");
        }else if(position == 1){
            payIv.setImageResource(R.drawable.ic_wx);
            payTv.setText("微信支付");
        }else if(position == 2){
            payIv.setImageResource(R.drawable.ic_alipay);
            payTv.setText("支付宝支付");
        }
        if(position == PAY_WAY){
            checkView.setVisibility(View.VISIBLE);
        }else{
            checkView.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
