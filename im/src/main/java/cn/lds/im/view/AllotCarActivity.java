package cn.lds.im.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CommonAdapter;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.ViewHolder;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.DepartmentCarsModel;
import de.greenrobot.event.EventBus;

public class AllotCarActivity extends BaseActivity {
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;

    // 车辆列表
    @ViewInject(R.id.car_list)
    protected ListView carLv;
    private List<DepartmentCarsModel.DataBean> departmentCars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allot_car);
        ViewUtils.inject(AllotCarActivity.class, this);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        LoadingDialog.showDialog(this,"请稍后...");
        ModuleHttpApi.getDepartmentCars();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void init(){
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText("指定用车");
        btnBack.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.top_back_btn//返回

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
        }
    }
    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();
            if (!(ModuleHttpApiKey.getDepartmentCars.equals(apiNo)
            ))
                return;
            DepartmentCarsModel model = GsonImplHelp.get().toObject(httpResult.getResult(), DepartmentCarsModel.class);
            departmentCars = model.getData();
            CommonAdapter<DepartmentCarsModel.DataBean> commonAdapter = new CommonAdapter<DepartmentCarsModel.DataBean>(this, departmentCars,R.layout.item_department_car) {
                @Override
                public void convert(ViewHolder helper, DepartmentCarsModel.DataBean item) {
                    helper.setText(R.id.car_no,item.getPlateLicenseNo());
                    helper.setText(R.id.car_model,item.getModelName());

                }


            };
            carLv.setAdapter(commonAdapter);
            carLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ALLOT_CAR_NO",departmentCars.get(position).getPlateLicenseNo());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });




        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
