package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener2;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.BookCarModel;
import cn.lds.chatcore.data.ChoiceBookTimeModel;
import cn.lds.chatcore.view.BaseActivity;

public class ChoiceBookTimeActivity extends BaseActivity {
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    @ViewInject(R.id.choicebooktime_taketime)
    TextView choicebooktime_taketime;
    @ViewInject(R.id.choicebooktime_taketime_hint)
    TextView choicebooktime_taketime_hint;
    @ViewInject(R.id.choicebooktime_returntime_hint)
    TextView choicebooktime_returntime_hint;
    @ViewInject(R.id.choicebooktime_returntime)
    TextView choicebooktime_returntime;
    BookCarModel bookCarModel;

    protected ChoiceBookTimeActivity activity;
    protected int layoutID = R.layout.activity_choice_book_time;

    public void setActivity(ChoiceBookTimeActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(ChoiceBookTimeActivity.class,this);
        if(null != activity){
            ViewUtils.inject(activity);
        }

        initConfig();
    }
    protected void initConfig() {
        if (null != getIntent())
            bookCarModel = (BookCarModel) getIntent().getExtras().get("data");
        init();
    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.bookcar_choicetime_title));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);
        if (0 != bookCarModel.getScheduledDroppedOffTime()) {
            String takeTime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, bookCarModel.getScheduledPickedUpTime());
            choicebooktime_taketime.setText(takeTime);
            choicebooktime_taketime_hint.setText("取车时间");
            String returnTime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, bookCarModel.getScheduledDroppedOffTime());
            choicebooktime_returntime.setText(returnTime);
            choicebooktime_returntime_hint.setText("还车时间");
        }
    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del,
            R.id.choicebooktime_taketimerllt,
            R.id.choicebooktime_returntimerllt
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.choicebooktime_taketimerllt:
                long t = System.currentTimeMillis();
                String taketime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, t + 5460000);
                if (!ToolsHelper.isNull(choicebooktime_taketime.getText().toString())
                        && TimeHelper.getTime(TimeHelper.FORMAT7, taketime) - System.currentTimeMillis() < 5400000)
                    taketime = choicebooktime_taketime.getText().toString();
                final String tempTime = taketime;
                PopWindowHelper.getInstance().timePicker(mContext, taketime, new PopWindowListener2() {
                    @Override
                    public void confirm(String text) {
                        if (TimeHelper.getTime(TimeHelper.FORMAT7, text) - System.currentTimeMillis() < 5400000){
                            ToolsHelper.showStatus(mContext, false, "取车时间必须在当前时间90分钟之后");
                            choicebooktime_taketime.setText(tempTime);
                        }else{
                            choicebooktime_taketime.setText(text);
                        }
                        choicebooktime_taketime_hint.setText("取车时间");
                    }

                    @Override
                    public void cancel() {

                    }
                }).show(findViewById(R.id.top__iv));
                break;
            case R.id.choicebooktime_returntimerllt:
//                long r = System.currentTimeMillis();
                //获得取车时间
                long r = TimeHelper.getTime(TimeHelper.FORMAT7, choicebooktime_taketime.getText().toString());
                String returntime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, r + 60000);//还车时间要在取车时间的1分钟之后
//                if (!ToolsHelper.isNull(choicebooktime_returntime.getText().toString()))
//                    returntime = choicebooktime_returntime.getText().toString();
//                else
                if (ToolsHelper.isNull(choicebooktime_taketime.getText().toString()))
                    ToolsHelper.showStatus(mContext, false, "请选择取车时间");
//                    returntime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, TimeHelper.getTime(TimeHelper.FORMAT7, choicebooktime_taketime.getText().toString()) + 3600000);


                PopWindowHelper.getInstance().timePicker(mContext, returntime, new PopWindowListener2() {
                    @Override
                    public void confirm(String text) {
                        choicebooktime_returntime.setText(text);
                        choicebooktime_returntime_hint.setText("还车时间");


                        String taketimetext = choicebooktime_taketime.getText().toString();

                        long takeTime = TimeHelper.getTime(TimeHelper.FORMAT7, taketimetext);
                        long returnTime = TimeHelper.getTime(TimeHelper.FORMAT7, text);

                        if (returnTime - takeTime < 60000) {
                            ToolsHelper.showStatus(mContext, false, "还车时间必须在取车时间1分钟之后");
                            return;
                        }

                        ChoiceBookTimeModel choiceBookTimeModel = new ChoiceBookTimeModel();
                        choiceBookTimeModel.setAppointmentPickedUpTime(takeTime);
                        choiceBookTimeModel.setAppointmentDroppedOffTime(returnTime);
                        choiceBookTimeModel.setTaketimetext(taketimetext);
                        choiceBookTimeModel.setReturntimetext(text);
                        mIntent.putExtra("choiceBookTimeModel", choiceBookTimeModel);
                        setResult(RESULT_OK, mIntent);
                        finish();
                    }

                    @Override
                    public void cancel() {

                    }
                }).show(findViewById(R.id.top__iv));
                break;
        }
    }

    @Override
    protected void onPause() {
        setResult(RESULT_CANCELED, mIntent);
        super.onPause();
    }
}
