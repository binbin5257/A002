package cn.lds.im.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cn.lds.chat.R;
import cn.lds.chatcore.data.message.LocationMessageModel;

public class LocationAdpter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<LocationMessageModel> mlist;

	public LocationAdpter(Context context, ArrayList<LocationMessageModel> list) {
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
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_location, null);

			holder.textView = (TextView) convertView.findViewById(R.id.item_title);
			holder.button = (Button) convertView.findViewById(R.id.item_isselectbtn);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LocationMessageModel item = mlist.get(position);
		holder.textView.setText(item.getUri());
		if (item.isSlected()) {
			holder.button.setVisibility(View.VISIBLE);
		} else {
			holder.button.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	final private class ViewHolder {
		public Button button;
		public TextView textView;
	}

}
