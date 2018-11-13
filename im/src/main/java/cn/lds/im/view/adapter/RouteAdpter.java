package cn.lds.im.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.im.data.RouteModel;
import cn.lds.im.view.MainActivity;
import cn.lds.im.view.PayActivity;
import cn.lds.im.view.RouteDetailActivity;
import cn.lds.im.view.TakeCarActivity;

/**
 * 行程适配器
 */
public class RouteAdpter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<RouteModel.DataBean> mlist;

    public RouteAdpter(Context context, List<RouteModel.DataBean> list) {
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
        final RouteModel.DataBean dataBean = (RouteModel.DataBean) getItem(position);
        final String status = dataBean.getStatus();


//        if (convertView == null) {
        holder = new ViewHolder();
        final Intent intent = new Intent();
        if (status.equals(Constants.CANCELLED))
            convertView = mInflater.inflate(R.layout.item_routelistview_cancel, null);
        else {
            convertView = mInflater.inflate(R.layout.item_routelistview, null);

            if (status.equals(Constants.PAID)) {
                try {
                    Class<?> c = (Class<?>) Class.forName("cn.lds.im.view.appview.AppRouteDetailActivity");
                    if (c == null) {
                        intent.setClass(mContext, RouteDetailActivity.class);//跳转行程详情界面
                    } else {
                        intent.setClass(mContext, c);
                    }
                } catch (ClassNotFoundException e) {
                    intent.setClass(mContext, RouteDetailActivity.class);//跳转行程详情界面
                }

                intent.setAction(dataBean.getNo());
            } else if (status.equals(Constants.PAYMENT_OVERDUE) ||
                    status.equals(Constants.DROPPED_OFF)) {
                try {
                    Class<?> c = (Class<?>) Class.forName("cn.lds.im.view.appview.AppPayActivity");
                    if (c == null) {
                        intent.setClass(mContext, PayActivity.class);//跳转支付页面
                    } else {
                        intent.setClass(mContext, c);
                    }
                } catch (ClassNotFoundException e) {
                    intent.setClass(mContext, PayActivity.class);//跳转支付页面
                }
                intent.putExtra("id", dataBean.getNo());
            } else if (status.equals(Constants.OPEN) ||
                    status.equals(Constants.ALLOCATED)) {
                try {
                    Class<?> c = (Class<?>) Class.forName("cn.lds.im.view.appview.AppTakeCarActivity");
                    if (c == null) {
                        intent.setClass(mContext, TakeCarActivity.class);//跳转支付页面
                    } else {
                        intent.setClass(mContext, c);
                    }
                } catch (ClassNotFoundException e) {
                    intent.setClass(mContext, TakeCarActivity.class);//跳转取车页面
                }
            } else if (status.equals(Constants.PICKED_UP) ||
                    status.equals(Constants.RETURN_OVERDUE)
                    ) {
                intent.setFlags(3);//跳转车钥匙
            }
        }

        holder.comments = (TextView) convertView.findViewById(R.id.item_routelistview_comments);
        holder.time = (TextView) convertView.findViewById(R.id.item_routelistview_time);
        holder.car = (TextView) convertView.findViewById(R.id.item_routelistview_car);
        holder.carcode = (TextView) convertView.findViewById(R.id.item_routelistview_carcode);
        holder.carplace = (TextView) convertView.findViewById(R.id.item_routelistview_carplace);
        holder.status = (TextView) convertView.findViewById(R.id.item_routelistview_status);
        holder.item_routelistview_lastitem = (ImageView) convertView.findViewById(R.id.item_routelistview_lastitem);
        holder.item_routelistview_firstitem = (ImageView) convertView.findViewById(R.id.item_routelistview_firstitem);

        holder.rlyt_routedetail_enter = (LinearLayout) convertView.findViewById(R.id.rlyt_routedetail_enter);
        convertView.setTag(holder);

//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        if (position == 0) {
            holder.item_routelistview_firstitem.setVisibility(View.VISIBLE);
        }
//        else {
//            holder.item_routelistview_firstitem.setVisibility(View.GONE);
//        }
        if (position == getCount() - 1) {
            holder.item_routelistview_lastitem.setVisibility(View.VISIBLE);
        }
//        else {
//            holder.item_routelistview_lastitem.setVisibility(View.GONE);
//        }


        holder.rlyt_routedetail_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals(Constants.CANCELLED)){
                    return;
                }
                if (3 == intent.getFlags()) {
                    MainActivity mainActivity = (MainActivity) MyApplication.getInstance().getMainActivity();
                    if (null != mainActivity)
                        mainActivity.switchContent(1);
                } else {
                    try {
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            }
        });


        holder.time.setText(TimeHelper.getDateStringString(dataBean.getReservationTime()));
        holder.car.setText(ToolsHelper.isNullString(dataBean.getVehicleBrandName()) + " " + dataBean.getVehicleModelName());
        holder.carcode.setText(dataBean.getPlateLicenseNo());
        holder.carplace.setText(dataBean.getReservationLocationName());
        if (status.equals(Constants.PAID)) {
            if (dataBean.isComment()) {
                holder.comments.setText("已评价");
                holder.comments.setTextColor(mContext.getResources().getColor(R.color.costsheetactivity_costsheet));
            } else {
                holder.comments.setText("待评价");
                holder.comments.setTextColor(mContext.getResources().getColor(R.color.textcolor_orange));
            }
        }
        if (status.equals(Constants.CANCELLED))
            holder.status.setText("已取消");
        else if (status.equals(Constants.PAID))
            holder.status.setText("已完成");
        else if (status.equals(Constants.PAYMENT_OVERDUE))
            holder.status.setText("逾期未支付");
        else if (status.equals(Constants.DROPPED_OFF))
            holder.status.setText("未支付");
        else if (status.equals(Constants.PICKED_UP))
            holder.status.setText("已取钥匙");
        else if (status.equals(Constants.RETURN_OVERDUE))
            holder.status.setText("逾期未还车");
        else if (status.equals(Constants.ALLOCATED) || status.equals(Constants.OPEN))
            holder.status.setText("未取钥匙");
        return convertView;
    }

    final protected class ViewHolder {
        protected TextView time;
        protected TextView car;
        protected TextView carcode;
        protected TextView carplace;
        protected TextView comments;
        protected TextView status;
        protected LinearLayout rlyt_routedetail_enter;
        protected ImageView item_routelistview_lastitem;
        protected ImageView item_routelistview_firstitem;
    }

}
