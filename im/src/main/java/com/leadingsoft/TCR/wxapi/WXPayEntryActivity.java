package com.leadingsoft.TCR.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.lds.chat.R;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.event.RequestPay;
import cn.lds.chatcore.view.BaseActivity;
import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
        api = WXAPIFactory.createWXAPI(this, AppIndependentConfigure.appId);
        api.handleIntent(getIntent(), this);
        TextView tv = (TextView) findViewById(R.id.top_title_tv);
        tv.setText("支付完成");
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Message message = new Message();
            switch (resp.errCode) {
                case 0:
                    message.what = 0;
                    break;
                case -1:
                    ToolsHelper.showStatus(mContext, true, "支付失败");
                    message.what = -1;
                    break;
                case -2:
                    ToolsHelper.showStatus(mContext, true, "用户取消支付");
                    message.what = -2;
                    break;
                default:
                    break;

            }
            handler.sendMessageDelayed(message, 2000);
            finish();
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    EventBus.getDefault().post(new RequestPay(0, -1));
                    break;
                case -2:
                    EventBus.getDefault().post(new RequestPay(0, -2));
                    break;
                case 0:
                    EventBus.getDefault().post(new RequestPay(0, 0));
                    break;
            }
        }
    };
}