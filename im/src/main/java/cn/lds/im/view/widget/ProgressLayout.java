package cn.lds.im.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.lds.chat.R;
import cn.lds.chatcore.common.DimensHelper;


/**
 * 续航自定view
 * Created by sibinbin on 17-7-14.
 */

public class ProgressLayout extends LinearLayout {

    int progress = 3;

    int min = 0;

    int max = 160;

    public void initParams(int min, int max, int endurance) {
        float i = endurance * 10 / max;
        if (i < 1) {
            this.progress = 1;
        } else if (i > 10) {
            this.progress = 10;
        } else {
            this.progress = (int) Math.rint(i) + 1;
        }
        this.min = min;
        this.max = max;
        init();
    }

    public ProgressLayout(Context context) {
        super(context);
    }

    public ProgressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public ProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    private void init() {
        this.removeAllViews();
        this.setOrientation(HORIZONTAL);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DimensHelper.dip2px(getContext(),8), DimensHelper.dip2px(getContext(),4));
        RelativeLayout.LayoutParams icPaddingPamas = new RelativeLayout.LayoutParams(DimensHelper.dip2px(getContext(),4), DimensHelper.dip2px(getContext(),3));
        RelativeLayout.LayoutParams textPaddingParams = new RelativeLayout.LayoutParams(DimensHelper.dip2px(getContext(),6), DimensHelper.dip2px(getContext(),3));
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(DimensHelper.dip2px(getContext(),4), 0, DimensHelper.dip2px(getContext(),4), 0);
        TextView minTv = new TextView(getContext());
        minTv.setText(min + "");
        minTv.setTextSize(14);
        minTv.setTextColor(getContext().getResources().getColor(R.color.white));

        ProgressLayout.this.addView(minTv, textParams);
        View textPaddingView = new View(getContext());
        ProgressLayout.this.addView(textPaddingView, textPaddingParams);
        for (int i = 0; i < 10; i++) {
             View view = new View(getContext());
             View view2 = new View(getContext());

            if (i < progress) {
                view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_radius_rect_blue));

            } else {
                view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_radius_rect));

            }
            view.setLayoutParams(params);

            ProgressLayout.this.addView(view);
            if(i != 9){
                ProgressLayout.this.addView(view2,icPaddingPamas);

            }
        }
        TextView maxTv = new TextView(getContext());
        maxTv.setText(max + "");
        maxTv.setTextSize(14);
        maxTv.setTextColor(getContext().getResources().getColor(R.color.white));
        View textPaddingView2 = new View(getContext());
        ProgressLayout.this.addView(textPaddingView2, textPaddingParams);
        ProgressLayout.this.addView(maxTv, textParams);

    }






}
