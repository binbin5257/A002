package cn.lds.im.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.data.MapSignParkModel;
import cn.lds.im.view.widget.MarqueeTextView;
import cn.lds.im.view.widget.ProgressLayout;

/**
 * 首页- 用车适配器
 */
public class UseCarPagerAdapter extends PagerAdapter {

    protected LayoutInflater mInflater;
    protected List<MapSignParkModel.DataBean> mapHlistModels = null;
    protected Context mContext;
    public UseCarPagerAdapter(Context context, List<MapSignParkModel.DataBean> mapHlistModels) {
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
        final MapSignParkModel.DataBean mapHlistModel = mapHlistModels.get(position);
        EventItemHolder itemHolder = null;
        View view = container.getFocusedChild();
        if (null == view) {
            // 按当前所需的样式，确定new的布局
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_usercar, null);
            itemHolder = new EventItemHolder();
            itemHolder.item_usercar_seriesName = (TextView) view.findViewById(R.id.item_usercar_seriesName);
            itemHolder.item_usercar_platelicenseno = (TextView) view.findViewById(R.id.item_usercar_platelicenseno);
            itemHolder.item_usercar_minprice = (MarqueeTextView) view.findViewById(R.id.item_usercar_minprice);
            itemHolder.item_usercar_totalcost = (TextView) view.findViewById(R.id.item_usercar_totalcost);
            itemHolder.item_usercar_sustainedMileage = (TextView) view.findViewById(R.id.item_usercar_sustainedMileage);
            itemHolder.item_usercar_iv = (ImageView) view.findViewById(R.id.item_usercar_iv);
            itemHolder.item_usercar_number = (TextView) view.findViewById(R.id.item_usercar_number);
            itemHolder.progressLayout = (ProgressLayout) view.findViewById(R.id.progresslayout);
            itemHolder.item_usercar_chargingrule = (LinearLayout) view.findViewById(R.id.item_usercar_chargingrule);
            view.setTag(itemHolder);
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            itemHolder = (EventItemHolder) view.getTag();
        }
        itemHolder.item_usercar_seriesName.setText(mapHlistModel.getBrandName() + " " + mapHlistModel.getSeriesName());
        int number = position + 1;
        if (mapHlistModels.size() == 1) {
            itemHolder.item_usercar_number.setVisibility(View.GONE);
        } else {
            itemHolder.item_usercar_number.setVisibility(View.VISIBLE);
            itemHolder.item_usercar_number.setText(number + "/" + mapHlistModels.size());
        }
        itemHolder.item_usercar_platelicenseno.setText(mapHlistModel.getPlateLicenseNo());
        itemHolder.item_usercar_minprice.setText(mapHlistModel.getChargingRule());
        itemHolder.item_usercar_minprice.setFocusable(true);
        if (!ToolsHelper.isNull(mapHlistModel.getSustainedMileage())) {
            itemHolder.item_usercar_sustainedMileage.setText(String.format(mContext.getString(R.string.sustained_mileage), mapHlistModel.getSustainedMileage()));
            itemHolder.progressLayout.initParams(0, mapHlistModel.getMaxSustainedMileage(), Integer.valueOf(mapHlistModel.getSustainedMileage()));
        } else {
            itemHolder.progressLayout.initParams(0, mapHlistModel.getMaxSustainedMileage(), mapHlistModel.getMaxSustainedMileage());
            itemHolder.item_usercar_sustainedMileage.setText("");
        }
        ImageLoaderManager.getInstance().displayImageForCar(mContext, mapHlistModel.getPictureId(), itemHolder.item_usercar_iv);
        itemHolder.item_usercar_chargingrule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mapHlistModel.getModelId())){
                    IntentHelper.intentChargingRule(mContext, Integer.valueOf(mapHlistModel.getId()), null);
                }else{
                    IntentHelper.intentChargingRule(mContext, Integer.valueOf(mapHlistModel.getModelId()), null);
                }

            }
        });
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
        protected TextView item_usercar_seriesName;
        protected TextView item_usercar_platelicenseno;
        protected MarqueeTextView item_usercar_minprice;
        protected TextView item_usercar_totalcost;
        protected TextView item_usercar_sustainedMileage;
        protected ImageView item_usercar_iv;
        protected TextView item_usercar_number;
        protected ProgressLayout progressLayout;
        protected LinearLayout item_usercar_chargingrule;
    }
}
