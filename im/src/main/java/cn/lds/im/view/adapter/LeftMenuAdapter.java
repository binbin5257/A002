package cn.lds.im.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.im.data.LeftMenuBean;

/**
 * 左侧滑菜单列表适配器
 * Created by E0608 on 2017/8/14.
 */

public class LeftMenuAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<LeftMenuBean> menuBeanList;

    public LeftMenuAdapter(Context context,List<LeftMenuBean> list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.menuBeanList = list;

    }

    @Override
    public int getCount() {
        return menuBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_left_menu,null);
            holder = new ViewHolder();
            holder.menuIconIv = (ImageView) convertView.findViewById(R.id.menu_icon);
            holder.menuNameTv = (TextView) convertView.findViewById(R.id.menu_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        LeftMenuBean leftMenuBean = menuBeanList.get(position);
        holder.menuNameTv.setText(leftMenuBean.getName());
        holder.menuIconIv.setImageResource(leftMenuBean.getImageRes());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeftMenuBean menuBean = menuBeanList.get(position);
                onItemOnClickListener.OnItemClickEvent(menuBean);
            }
        });
        return convertView;
    }
    class ViewHolder{
        private TextView menuNameTv;
        private ImageView menuIconIv;
    }

    private OnItemOnClickListener onItemOnClickListener;

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }

    public interface OnItemOnClickListener{
        void OnItemClickEvent(LeftMenuBean bean);
    }
}
