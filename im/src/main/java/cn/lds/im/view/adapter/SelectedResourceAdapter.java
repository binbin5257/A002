package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chat.R;
import cn.lds.im.data.PopSelectedModel;
import cn.lds.im.view.widget.SelectedResourcePopWindow;

/**
 * Created by pengwb on 15/12/25.
 * 资源选择器适配器
 */
public class SelectedResourceAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private ArrayList<PopSelectedModel> popSelectedModels = null;
    private Context mContext;

    public SelectedResourceAdapter(Context context, ArrayList<PopSelectedModel> popSelectedModels) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.popSelectedModels = popSelectedModels;
    }

    @Override
    public int getCount() {
        return popSelectedModels.size();
    }

    @Override
    public Object getItem(int position) {
        return popSelectedModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PopSelectedModel popSelectedModel = (PopSelectedModel) getItem(position);
        EventItemHolder itemHolder = null;
        if (convertView == null) {
            // 按当前所需的样式，确定new的布局
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_selectedcommon, null);
            itemHolder = new EventItemHolder();
            itemHolder.nameTx = (TextView) convertView.findViewById(R.id.item_selectedcommon_tx);
            itemHolder.item_selectedcommon_bg = (RelativeLayout) convertView.findViewById(R.id.item_selectedcommon_bg);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (EventItemHolder) convertView.getTag();
        }
        //设置标题值
        Map<String, Object> map = popSelectedModel.getMapList();
        itemHolder.nameTx.setText(ToolsHelper.toString(map.get(SelectedResourcePopWindow.mTitleKey)));

        if (popSelectedModel.isSelected()) {
            itemHolder.item_selectedcommon_bg.setBackgroundColor(mContext.getResources().getColor(R.color.businessoptunityd9d9d9));
            itemHolder.nameTx.setTextColor(mContext.getResources().getColor(R.color.seletcedcommon_seletced));
        } else {
            itemHolder.item_selectedcommon_bg.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            itemHolder.nameTx.setTextColor(mContext.getResources().getColor(R.color.seletcedcommon_unseletced));
        }
        return convertView;
    }


    public final class EventItemHolder {
        private TextView nameTx;
        private RelativeLayout item_selectedcommon_bg;
    }
}
