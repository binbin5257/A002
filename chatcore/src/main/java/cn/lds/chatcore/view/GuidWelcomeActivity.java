package cn.lds.chatcore.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by E0608 on 2017/11/14.
 */

public class GuidWelcomeActivity extends BaseActivity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
