package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.view.BaseActivity;

/**
 * 引导页面
 * Created by E0608 on 2017/11/15.
 */

public class GuideActivity extends BaseActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        setViewPager();
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.guide_vp);
    }


    //实现基本滑动切换界面
    private void setViewPager(){
        final List<View> vList = new ArrayList<>();
        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.bg_guide_first);
        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.bg_guide_second);
        ImageView iv3 = new ImageView(this);
        iv3.setImageResource(R.drawable.bg_guide_first);
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
            }
        });
        vList.add(iv1);
        vList.add(iv2);
        vList.add(iv3);

        PagerAdapter myAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return vList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view==o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(vList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(vList.get(position));
                return vList.get(position);
            }
        };

        pager.setAdapter(myAdapter);
    }
}
