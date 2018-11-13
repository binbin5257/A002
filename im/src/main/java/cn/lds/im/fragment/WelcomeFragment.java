package cn.lds.im.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.im.view.MainActivity;
import cn.lds.im.view.WelcomeActivity;

/**
 * Created by xuqm on 2016/5/17.
 */


public class WelcomeFragment extends Fragment {
    protected View view;
    protected int position;
    protected WelcomeActivity welcomeActivity;
//    @ViewInject(R.id.welcome_enter)
//    protected Button welcome_enter;
//    @ViewInject(R.id.welcome_jump)
//    protected Button welcome_jump;
    @ViewInject(R.id.welcome_iv)
    protected ImageView imageView;
    @ViewInject(R.id.rl_count_down)
    protected RelativeLayout countDownRl;
    @ViewInject(R.id.time)
    protected TextView time;
    private CountDownTimer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, null);
        ViewUtils.inject(WelcomeFragment.class,this, view);
        init();
        return view;
    }

    @SuppressLint("ValidFragment")
    public WelcomeFragment(int position, WelcomeActivity welcomeActivity) {
        this.position = position;
        this.welcomeActivity = welcomeActivity;
    }

    public WelcomeFragment() {
    }

    protected void init() {
        ViewUtils.inject(this, view);
        switch (position) {
            case 0:
                imageView.setBackgroundResource(R.drawable.bg_guide_first);
//                welcome_jump.setVisibility(View.VISIBLE);
//                welcome_enter.setVisibility(View.GONE);
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.bg_guide_second);
//                welcome_jump.setVisibility(View.VISIBLE);
//                welcome_enter.setVisibility(View.GONE);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.bg_guide_third);
//                welcome_jump.setVisibility(View.GONE);
//                welcome_enter.setVisibility(View.VISIBLE);
                break;

        }
    }

    @OnClick({ R.id.welcome_iv})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.welcome_enter:
//            case R.id.welcome_jump:
//                welcomeActivity.stopPlaying();
//                break;
            case R.id.welcome_iv:
                if(position == 2){
                    enterMainActivity();
                }
                break;
        }
    }

    public void enterMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void startCountDown() {
        countDownRl.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(millisUntilFinished / 1000 + "s");

            }

            @Override
            public void onFinish() {
                enterMainActivity();
            }
        }.start();

    }

    public void cancelCountDown() {
        if(timer != null){
            countDownRl.setVisibility(View.INVISIBLE);
            timer.cancel();
            timer = null;
        }
    }
}
