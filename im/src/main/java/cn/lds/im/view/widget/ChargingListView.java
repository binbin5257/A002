package cn.lds.im.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import cn.lds.chatcore.view.widget.ListView.Pullable;

/**
 * Created by E0608 on 2017/10/27.
 */

public class ChargingListView extends ListView implements Pullable{

    private float downY;

    private float yDist = -1;

    private float yMove;


    public ChargingListView(Context context) {
        super(context);
    }

    public ChargingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChargingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                yMove = ev.getY();
                yDist = Math.abs(yMove - downY);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean canPullDown() {

        if (getCount() == 0) {
            return true;
        } else if (getFirstVisiblePosition() == 0 && getChildAt(0)!=null &&getChildAt(0).getTop() >= 0 && yDist > 200) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {

        if (getCount() == 0) {
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1) && yDist > 200) {
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }
}
