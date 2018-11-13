package cn.lds.chatcore.ViewPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPage extends ViewPager {
	private boolean isScroll = false;

	public MyViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyViewPage(Context context) {
		super(context);
	}

	public void setNoScroll(boolean isScroll) {
		this.isScroll = isScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (isScroll)
			return super.onTouchEvent(arg0);
		else
			return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (isScroll)
			return super.onInterceptTouchEvent(arg0);
		else
			return false;
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}
}
