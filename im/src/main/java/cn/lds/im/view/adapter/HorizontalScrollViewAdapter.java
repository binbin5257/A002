package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;

public class HorizontalScrollViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> horizontalList;

    public HorizontalScrollViewAdapter(Context context, List<String> horizontalList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.horizontalList = horizontalList;
    }

    public int getCount() {
        return horizontalList.size();
    }

    public Object getItem(int position) {
        return horizontalList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
//        OrganizationTable mDatas = (OrganizationTable) getItem(position);
        if (horizontalList != null && horizontalList.size() > 0) {
            String data = horizontalList.get(position);
            int size = horizontalList.size();
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.activity_organization_horizontalscrollview_item, parent, false);
                viewHolder.mName = (TextView) convertView.findViewById(R.id.id_index_gallery_item_image);
                viewHolder.mText = (TextView) convertView.findViewById(R.id.id_index_gallery_item_text);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (convertView != null) {
                viewHolder.mName.setText(data);
                if (position == size - 1) {
                    viewHolder.mName.setTextColor(mContext.getResources().getColor(R.color.textcolor_blue));
                } else {
                    viewHolder.mText.setText(">");
//            viewHolder.mText.setTextColor(mContext.getResources().getColor(R.color.textcolor_blue));
                }
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView mName;
        TextView mText;
    }

}
