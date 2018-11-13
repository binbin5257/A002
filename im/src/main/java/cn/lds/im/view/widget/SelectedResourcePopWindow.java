package cn.lds.im.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.lds.chatcore.MyApplication;
import cn.lds.chat.R;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.im.data.PopSelectedModel;
import cn.lds.im.view.adapter.SelectedResourceAdapter;

/**
 * Created by pengwb on 15/12/25.
 * 资源选择器
 */
public class SelectedResourcePopWindow extends PopupWindow {

    @ViewInject(R.id.popwindow_selectedcommon_sexlistview)
    private ListView popwindow_selectedcommon_singlelistview;
    //模型
    private ArrayList<PopSelectedModel> singleModels;
    private SelectedResourceAdapter singleAdpter;
    private Context mContext;
    private View mPopupwindow;
    // 每个MAP 的显示值的KEY；
    public static final String mTitleKey = "title";
    //传入线程
    private Handler mHandler;
    //返回事件处理
    private int FEEDBACKCODE = 1000;
    //选中的map
    public Map<String, Object> selectedMap;
    //选中的position
    private int selectedPosition = 0;

    /**
     * 筛选框 处理传入集合
     *
     * @param context
     * @param mapList  筛选的集合
     * @param handler  线程
     * @param feedback 确定后的handler事件key
     */
    public SelectedResourcePopWindow(Context context, List<Map<String, Object>> mapList, Handler handler, int feedback) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupwindow = inflater.inflate(R.layout.popwindow_selectedcommon, null);
        this.mContext = context;
        this.mHandler = handler;
        this.FEEDBACKCODE = feedback;
        ViewUtils.inject(this, mPopupwindow);

//        mapList = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put(mTitleKey, "come on" + i);
//            mapList.add(map);
//        }
        if (null == mapList || mapList.isEmpty()) {
            ToolsHelper.showStatus(mContext,false, mContext.getString(R.string.selectedresourcepopwindow_list_isnull));
            return;
        }
        singleModels = new ArrayList<>();
        //初始化传入的数据
        for (Map<String, Object> map : mapList) {
            PopSelectedModel model = new PopSelectedModel(map, false);
            singleModels.add(model);
        }
        singleAdpter = new SelectedResourceAdapter(mContext, singleModels);
        popwindow_selectedcommon_singlelistview.setAdapter(singleAdpter);
        popwindow_selectedcommon_singlelistview.setVisibility(View.VISIBLE);
        setPopwindow();
    }


    private void setPopwindow() {

        // 设置SelectPicPopupWindow的View
        this.setContentView(mPopupwindow);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(MyApplication.getInstance().getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(MyApplication.getInstance().getHeight());
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    @OnClick({R.id.popwindow_selectedcommon_confirm
            , R.id.popwindow_selectedcommon_cancel
            , R.id.pop_bg
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popwindow_selectedcommon_confirm:
                selectedMap = singleModels.get(selectedPosition).getMapList();
                mHandler.sendEmptyMessage(FEEDBACKCODE);
                SelectedResourcePopWindow.this.dismiss();
                break;
            case R.id.popwindow_selectedcommon_cancel:
                SelectedResourcePopWindow.this.dismiss();
                break;
            case R.id.pop_bg:
                SelectedResourcePopWindow.this.dismiss();
                break;
        }
    }

    @OnItemClick({R.id.popwindow_selectedcommon_provincelistview
            , R.id.popwindow_selectedcommon_citylistview
            , R.id.popwindow_selectedcommon_sexlistview
    })
    public void OnItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.popwindow_selectedcommon_sexlistview:
                for (PopSelectedModel model : singleModels) {
                    model.setIsSelected(false);
                }
                PopSelectedModel model = singleModels.get(position);
                model.setIsSelected(true);
                selectedPosition = position;
                singleAdpter.notifyDataSetChanged();

                break;
        }
    }

}
