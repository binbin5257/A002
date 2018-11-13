package cn.lds.im.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.im.fragment.CanUseCouponFragment;
import cn.lds.im.fragment.ExpireCouponFragment;
import cn.lds.im.fragment.UsedCouponFragment;

/**
 * 优惠卷-Tab 界面切换
 * Created by E0608 on 2017/9/4.
 */

public class CouponListActivityNew extends FragmentActivity{
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    //客服
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    //工单滑动viewPager
    @ViewInject(R.id.view_pager)
    private ViewPager viewPager;

    //可使用tab
    @ViewInject(R.id.tab_can_use)
    private TextView canUseTab;

    //已使用tab
    @ViewInject(R.id.tab_used)
    private TextView usedTab;

    //已过期tab
    @ViewInject(R.id.tab_expire)
    private TextView expireTab;


    //tab下滑线
    @ViewInject(R.id.indicate_line)
    private View indicate_line;

    private List<Fragment> fragments = new ArrayList<>();
    private int indicateLineWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list_new);
        ViewUtils.inject(this);
        init();
        addFragmentToViewpager();
        //计算下滑线的宽度
        calculateIndicateLineWidth();

    }
    /**
     * 添加待接受，处理中，已完成，已取消页面到viewPager
     */
    private void addFragmentToViewpager() {
        CanUseCouponFragment canUseCouponFragment = new CanUseCouponFragment();
        UsedCouponFragment usedCouponFragment = new UsedCouponFragment();
        ExpireCouponFragment expireCouponFragment = new ExpireCouponFragment();
        fragments.add(canUseCouponFragment);
        fragments.add(usedCouponFragment);
        fragments.add(expireCouponFragment);
        //计算下滑线的宽度
        calculateIndicateLineWidth();
        viewPager.setAdapter(new CouponPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int translatePosition = position * indicateLineWidth + positionOffsetPixels / fragments.size();
                ViewHelper.setTranslationX(indicate_line,translatePosition);
            }

            @Override
            public void onPageSelected(int position) {
                //设置页面字体颜色和缩放
                lightAndScaleTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

    }
    public class CouponPagerAdapter extends FragmentPagerAdapter {

        public CouponPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }
    /**
     * 设置页面标签字体颜色和缩放
     */
    private void lightAndScaleTitle() {
        int currentPage = viewPager.getCurrentItem();
        canUseTab.setTextColor(currentPage == 0 ? Color.parseColor("#FF10BFAF") : getResources().getColor(R.color.black));
        usedTab.setTextColor(currentPage == 1 ? Color.parseColor("#FF10BFAF") : getResources().getColor(R.color.black));
        expireTab.setTextColor(currentPage == 2 ? Color.parseColor("#FF10BFAF") : getResources().getColor(R.color.black));
    }
    /**
     * 计算下划线的宽度
     */
    private void calculateIndicateLineWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        indicateLineWidth = screenWidth / fragments.size();
        indicate_line.getLayoutParams().width = indicateLineWidth;
        indicate_line.requestLayout();
    }

    private void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.coupon_title));
        btnBack.setVisibility(View.VISIBLE);

    }
    @OnClick({R.id.top_back_btn
            , R.id.top_menu_btn_del
            ,R.id.tab_can_use
            ,R.id.tab_used
            ,R.id.tab_expire
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_menu_btn_del:
                break;
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.tab_can_use:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab_used:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab_expire:
                viewPager.setCurrentItem(2);
                break;

        }

    }


}
