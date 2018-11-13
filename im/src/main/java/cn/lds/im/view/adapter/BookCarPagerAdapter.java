package cn.lds.im.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.im.data.AvailableVehicleModel;

/**
 * 首页- 用车适配器
 */
public class BookCarPagerAdapter extends PagerAdapter {

    private LayoutInflater mInflater;
    private List<AvailableVehicleModel.DataBean> mapHlistModels = null;
    private Context mContext;


    public BookCarPagerAdapter(Context context, List<AvailableVehicleModel.DataBean> mapHlistModels) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mapHlistModels = mapHlistModels;
    }

    @Override
    public int getCount() {
        return mapHlistModels.size();
    }

    @Override
    public View instantiateItem(final ViewGroup container, int position) {
        final AvailableVehicleModel.DataBean mapHlistModel = mapHlistModels.get(position);
        EventItemHolder itemHolder = null;
        View view = container.getFocusedChild();
        if (null == view) {
            // 按当前所需的样式，确定new的布局
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_bookcar, null);
            itemHolder = new EventItemHolder();

            itemHolder.item_usercar_totalcost = (TextView) view.findViewById(R.id.item_usercar_totalcost);
            itemHolder.item_usercar_seriesName = (TextView) view.findViewById(R.id.item_usercar_seriesName);
            itemHolder.item_usercar_iv = (ImageView) view.findViewById(R.id.item_usercar_iv);
            view.setTag(itemHolder);
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            itemHolder = (EventItemHolder) view.getTag();
        }


//        itemHolder.item_usercar_totalcost.setText(mapHlistModel.getTimeCost() / 100
//                + mContext.getString(R.string.order_unit_minute)
//                + mapHlistModel.getDistanceCost() / 100
//                + mContext.getString(R.string.order_unit_mileage));
//        itemHolder.item_usercar_seriesName.setText(mapHlistModel.getBrandName()+" "+mapHlistModel.getSeriesName());
        itemHolder.item_usercar_seriesName.setText(mapHlistModel.getBrandName()+" "+mapHlistModel.getSeriesName());
        ImageLoaderManager.getInstance().displayImageForCar(mContext, mapHlistModel.getPictureId(), itemHolder.item_usercar_iv);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public final class EventItemHolder {
        private TextView item_usercar_seriesName;
        private TextView item_usercar_totalcost;
        private ImageView item_usercar_iv;
    }

}
