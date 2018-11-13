package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;

/**
 * 位置搜索结果适配器
 * Created by sibinbin on 2017/7/24.
 */

public class SearchResultsAdapter extends BaseAdapter {

    private List<SuggestionResult.SuggestionInfo> mList;
    private LayoutInflater inflater;
    private Context mContext;
    public SearchResultsAdapter(Context context,List<SuggestionResult.SuggestionInfo> list) {
        this.mList = list;
        this.mContext = context;
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
        if(convertView == null){
            convertView = this.inflater.inflate(R.layout.item_search_location,null);
            holder = new ViewHolder();
            holder.searchIcon = (ImageView) convertView.findViewById(R.id.iv_location);
            holder.titleTv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.contentTv = (TextView) convertView.findViewById(R.id.tv_content);
            holder.distanceTv = (TextView) convertView.findViewById(R.id.tv_distance);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        SuggestionResult.SuggestionInfo info = mList.get(position);
        if(position == 0){
            holder.searchIcon.setImageResource(R.drawable.icon_choice_star);
        }else{
            holder.searchIcon.setImageResource(R.drawable.ic_location);

        }
        holder.titleTv.setText(info.key);
        holder.contentTv.setText(info.city + info.district + info.key);
        LatLng pt = info.pt;
        double distance = DistanceUtil.getDistance(MyApplication.myLocation, pt);
        int distanceInt = (int) distance;
        if(distanceInt >= 1000){
            holder.distanceTv.setText(distanceInt/1000 + "km");
        }else{
            holder.distanceTv.setText(distanceInt + "m");
        }

        return convertView;
    }

    class ViewHolder{
        private ImageView searchIcon;
        private TextView titleTv;
        private TextView contentTv;
        private TextView distanceTv;

    }


}
