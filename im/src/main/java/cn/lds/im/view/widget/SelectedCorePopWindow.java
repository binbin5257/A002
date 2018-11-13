package cn.lds.im.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chatcore.MyApplication;

import cn.lds.chat.R;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.db.MasterTable;
import cn.lds.chatcore.enums.MasterType;
import cn.lds.chatcore.manager.MasterManager;
import cn.lds.im.data.PopSelectedModel;
import cn.lds.im.view.adapter.SelectedCoreAdapter;

/**
 * Created by pengwb on 15/12/25.
 * 码表选择器
 */
public class SelectedCorePopWindow extends PopupWindow {

    private View mPopupwindow;

    @ViewInject(R.id.popwindow_selectedcommon_sexlistview)
    private ListView popwindow_selectedcommon_singlelistview;
    @ViewInject(R.id.popwindow_selectedcommon_provincelistview)
    private ListView popwindow_selectedcommon_provincelistview;
    @ViewInject(R.id.popwindow_selectedcommon_citylistview)
    private ListView popwindow_selectedcommon_citylistview;


    private Context mContext;
    //模型
    private ArrayList<PopSelectedModel> singleModels, proModels, cityModels;
    //性别TEXTVIEW, 所在省城TEXTVIEW
    private TextView singleTextview, proTextview;
    private SelectedCoreAdapter singleAdpter, proAdpter, cityAdpter;
    private List<MasterTable> provincTables, cityTables, singleTables;
    //用于item点击纪录,点击位置使用;
    private int cityPosition = 0, proPossition = 0;
    //省
    private String provinceName;
    //市区
    private String cityName;
    //省代码
    public String provinceCode;
    //城市代码;
    public String cityCode;
    //性别
    private String genderName;
    //性别代码
    public String genderCode;


    //传入线程
    private Handler mHandler;
    //返回事件处理
    private int FEEDBACKCODE = 1000;


    /**
     * 筛选框 处理单个 选项
     */
    public SelectedCorePopWindow(Context context, TextView tx1, Handler mhadler, int feedback, String key, MasterType masterType) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupwindow = inflater.inflate(R.layout.popwindow_selectedcommon, null);
        mContext = context;
        ViewUtils.inject(this, mPopupwindow);

        singleTextview = tx1;
        this.mHandler = mhadler;
        this.FEEDBACKCODE = feedback;

        singleModels = new ArrayList<>();
        singleTables = MasterManager.getInstance().getListByTypeAndParentKey(masterType, null, null);
        //初始原本选中的效果
        for (int i = 0; i < singleTables.size(); i++) {
            MasterTable table = singleTables.get(i);
            if (!ToolsHelper.isNull(key)) {
                if (key.equals(table.getKey()))
                    cityPosition = i;
            }
            singleModels.add(new PopSelectedModel(false, table));
        }
        singleModels.get(cityPosition).setIsSelected(true);

        singleAdpter = new SelectedCoreAdapter(mContext, singleModels);
        popwindow_selectedcommon_singlelistview.setAdapter(singleAdpter);
        popwindow_selectedcommon_singlelistview.setVisibility(View.VISIBLE);
        popwindow_selectedcommon_singlelistview.setSelection(cityPosition);
        setPopwindow();
    }

    /**
     * 筛选框  选择地区
     */
    public SelectedCorePopWindow(Context context, TextView tx2, String province, String city, Handler mhadler, int feedback) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupwindow = inflater.inflate(R.layout.popwindow_selectedcommon, null);
        mContext = context;
        ViewUtils.inject(this, mPopupwindow);


        popwindow_selectedcommon_provincelistview.setVisibility(View.VISIBLE);
        popwindow_selectedcommon_citylistview.setVisibility(View.VISIBLE);
        proTextview = tx2;
        this.mHandler = mhadler;
        this.FEEDBACKCODE = feedback;


        proModels = new ArrayList<>();
        cityModels = new ArrayList<>();


        //获取省份列表;
        provincTables = MasterManager.getInstance().getListByTypeAndParentKey(MasterType.Region, null, "1");
        if (ToolsHelper.isNull(province)) {
            for (MasterTable table : provincTables) {
                proModels.add(new PopSelectedModel(false, table));
            }
        } else {
            for (int i = 0; i < provincTables.size(); i++) {
                MasterTable table = provincTables.get(i);
                if (province.equals(table.getKey())) {
                    proModels.add(new PopSelectedModel(true, table));
                    proPossition = i;
                } else
                    proModels.add(new PopSelectedModel(false, table));
            }
        }


        if (!ToolsHelper.isNull(province)) {
            //初始化城市列表;
            cityTables = MasterManager.getInstance().getListByTypeAndParentKey(MasterType.Region, province, null);
            if (ToolsHelper.isNull(city)) {
                for (MasterTable table : cityTables) {
                    cityModels.add(new PopSelectedModel(false, table));
                }
            } else {
                for (int i = 0; i < cityTables.size(); i++) {
                    MasterTable table = cityTables.get(i);
                    if (city.equals(table.getKey())) {
                        cityModels.add(new PopSelectedModel(true, table));
                        cityPosition = i;
                    } else
                        cityModels.add(new PopSelectedModel(false, table));
                }
            }
        }

        proAdpter = new SelectedCoreAdapter(mContext, proModels);
        cityAdpter = new SelectedCoreAdapter(mContext, cityModels);
        popwindow_selectedcommon_provincelistview.setAdapter(proAdpter);
        popwindow_selectedcommon_citylistview.setAdapter(cityAdpter);

        popwindow_selectedcommon_provincelistview.setSelection(proPossition);
        popwindow_selectedcommon_citylistview.setSelection(cityPosition);


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
                //确定选中的值;
                if (null == proTextview) {
                    MasterTable table = singleModels.get(cityPosition).getMasterTable();
                    //设置 性别
                    genderName = table.getText();
//                    singleTextview.setText(genderName);
                    //设置 性别代码
                    genderCode = table.getKey();
                } else {
                    MasterTable pro = proModels.get(proPossition).getMasterTable();
                    //设置省
                    provinceName = pro.getText();
                    //设置省代码
                    provinceCode = pro.getKey();
                    if (cityModels.isEmpty()) {
                        proTextview.setText(provinceName);
                    } else {
                        MasterTable table = cityModels.get(cityPosition).getMasterTable();
                        //设置市区
                        cityName = table.getText();
                        proTextview.setText(provinceName + cityName);
                        //设置市区代码
                        cityCode = table.getKey();
                    }
                }
                //返回发送事件;
                mHandler.sendEmptyMessage(FEEDBACKCODE);
                SelectedCorePopWindow.this.dismiss();
                break;
            case R.id.popwindow_selectedcommon_cancel:
                SelectedCorePopWindow.this.dismiss();
                break;
            case R.id.pop_bg:
                SelectedCorePopWindow.this.dismiss();
                break;
        }
    }

    @OnItemClick({R.id.popwindow_selectedcommon_provincelistview
            , R.id.popwindow_selectedcommon_citylistview
            , R.id.popwindow_selectedcommon_sexlistview})
    public void OnItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.popwindow_selectedcommon_provincelistview:
                //更新省份UI
                for (PopSelectedModel model : proModels) {
                    model.setIsSelected(false);
                }
                proModels.get(position).setIsSelected(true);
                proAdpter.notifyDataSetChanged();

                cityModels.clear();
                //获取点击的值
                proPossition = position;
                cityPosition = 0;
                cityTables = MasterManager.getInstance().getListByTypeAndParentKey(MasterType.Region, proModels.get(position).getMasterTable().getKey(), null);
                //重新获取城市列表
                for (MasterTable table :
                        cityTables) {
                    cityModels.add(new PopSelectedModel(false, table));
                }
                cityAdpter.notifyDataSetChanged();
                break;
            case R.id.popwindow_selectedcommon_citylistview:
                //更新城市UI
                for (PopSelectedModel model : cityModels) {
                    model.setIsSelected(false);
                }
                cityModels.get(position).setIsSelected(true);
                cityAdpter.notifyDataSetChanged();
                //获取点击的值
                cityPosition = position;
                break;
            case R.id.popwindow_selectedcommon_sexlistview:

                for (PopSelectedModel model : singleModels) {
                    model.setIsSelected(false);
                }
                singleModels.get(position).setIsSelected(true);
                singleAdpter.notifyDataSetChanged();
                cityPosition = position;
                break;
        }
    }

}
