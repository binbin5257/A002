package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Calendar;
import java.util.Locale;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener2;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.RevertCarModel;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.data.ReservationOrdersLocationsModel;

/**
 * 0921变更中，弃用该页面
 */
public class RevertCarInfoActivity extends BaseActivity {
    //topbar
    @ViewInject(R.id.top_back_btn)
    protected Button top_back_btn;
    @ViewInject(R.id.top_title_tv)
    protected TextView top_title_tv;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView top_menu_btn_del;
    //控件绑定
    @ViewInject(R.id.revert_time_ryt)
    protected RelativeLayout revert_time_ryt;
    @ViewInject(R.id.revert_address_ryt)
    protected RelativeLayout revert_address_ryt;
    @ViewInject(R.id.revert_time_tv)
    protected TextView revert_time_tv;
    @ViewInject(R.id.revert_address_tv)
    protected TextView revert_address_tv;
    @ViewInject(R.id.revert_address_select_tv)
    protected TextView revert_address_select_tv;

    public static final int requestCodeAddress = 1;

    protected RevertCarModel revertCarModel;
    protected Intent intent;
    protected String orderNo;

    protected RevertCarInfoActivity activity;
    protected int layoutID = R.layout.activity_revert_car_info;

    public void setActivity(RevertCarInfoActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(RevertCarInfoActivity.class,this);
        if(null != activity){
            ViewUtils.inject(activity);
        }

        initConfig();
    }
    protected void initConfig() {
        init();
    }

    protected void init() {
        intent = getIntent();
        if (null != intent) {
            revertCarModel = intent.getParcelableExtra("data");
            orderNo = intent.getStringExtra("orderNo");
        }
        top_title_tv.setText(R.string.revert_title);
        if (null != revertCarModel) {
            revert_address_select_tv.setText(revertCarModel.getName());
            revert_time_tv.setText(revertCarModel.getTime());
        }
        top_back_btn.setVisibility(View.VISIBLE);
        top_title_tv.setVisibility(View.VISIBLE);
        top_menu_btn_del.setImageResource(R.drawable.topbar_menu_customerservice);
        top_menu_btn_del.setVisibility(View.VISIBLE);

        revert_address_tv.setText(R.string.revert_city);
        String city = MyApplication.lastCity;
        if (!ToolsHelper.isNull(city)) {
//            revert_address_tv.setText(city);
        }
    }

    @OnClick({R.id.revert_time_ryt, R.id.revert_address_ryt, R.id.key_have_confirm_tv, R.id.top_back_btn, R.id.top_menu_btn_del})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.revert_time_ryt:
                PopWindowHelper.getInstance().timePicker(mContext,revert_time_tv.getText().toString(), new PopWindowListener2() {
                    @Override
                    public void confirm(String text) {
                        revert_time_tv.setText(text);
                        if (null == revertCarModel)
                            revertCarModel = new RevertCarModel();
                        revertCarModel.setTime(text);
                    }

                    @Override
                    public void cancel() {

                    }
                }).show(findViewById(R.id.top__iv));
                break;
            case R.id.revert_address_ryt:
                if (null == orderNo)
                    return;
                mIntent.setClass(mContext, LocatedActivity.class);
                mIntent.setAction(orderNo);
                startActivityForResult(mIntent, requestCodeAddress);

                break;
            case R.id.key_have_confirm_tv:


                if (null == revertCarModel) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.revert_show_no_time_and_address));
                    return;
                }
                if (ToolsHelper.isNull(revertCarModel.getTime())) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.revert_show_no_time));
                    return;
                }
                if (ToolsHelper.isNull(revertCarModel.getName())) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.revert_show_no_address));
                    return;
                }
                Calendar calendar = Calendar.getInstance(Locale.US);
                long time = TimeHelper.getTime(TimeHelper.FORMAT5, String.format(getString(R.string.revert_set_date), calendar.get(Calendar.YEAR), revertCarModel.getTime()));
                long time2 = TimeHelper.getTime(TimeHelper.FORMAT5, TimeHelper.getTime(TimeHelper.FORMAT5));
                if (time < time2) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.revert_show_must_time));
                    return;
                }

                intent.putExtra("data", revertCarModel);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data)
            return;
        if (RESULT_OK != resultCode)
            return;
        switch (requestCode) {
            case requestCodeAddress:
                ReservationOrdersLocationsModel.DataBean selectPark = (ReservationOrdersLocationsModel.DataBean) data.getSerializableExtra("selectPark");
//                ToolsHelper.showInfo(mContext, selectPark.getAddress() + "-----");
                if (!ToolsHelper.isNull(selectPark.getAddress()))
                    revert_address_select_tv.setText(selectPark.getName());
                if (null == revertCarModel)
                    revertCarModel = new RevertCarModel();
                revertCarModel.setLatitude(selectPark.getLatitude());
                revertCarModel.setLongitude(selectPark.getLongitude());
                revertCarModel.setName(selectPark.getName());
                revertCarModel.setId(String.valueOf(selectPark.getId()));
                break;
        }
    }
}
