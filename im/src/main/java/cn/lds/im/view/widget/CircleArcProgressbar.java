package cn.lds.im.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.lds.chatcore.common.TimeHelper;

/**
 * Created on 2017/7/22.
 */
public class CircleArcProgressbar extends TextView {

	/**
	 * 中心圆的颜色。
	 */
	private int circleColor;

	/**
	 * 进度条的颜色。
	 */
	private int progressLineColor = Color.BLUE;
	/**
	 * 进度条的颜色。
	 */
	private int currentLineColor = Color.RED;

	/**
	 * 进度条的宽度。
	 */
	private int progressLineWidth = 8;

	/**
	 * 画笔。
	 */
	private Paint mPaint = new Paint();

	/**
	 * 进度条的矩形区域。
	 */
	private RectF mArcRect = new RectF();

	/**
	 * 进度。
	 */
	private long progress = 1800;
	/**
	 * 进度条类型。
	 */
	private ProgressType mProgressType = ProgressType.COUNT_BACK;
	/**
	 * 进度倒计时时间。
	 */
	private long timeMillis = 1800;
	/**
	 * 进度倒计进度时时间
	 */
	private long progressTimeMillis = 1800;
	/**
	 * 进度倒计时时间。
	 */
	private long currentMillis = 300;

	/**
	 * View的显示区域。
	 */
	final Rect bounds = new Rect();
	/**
	 * 进度条通知。
	 */
	private OnCountdownProgressListener mCountdownProgressListener;
	/**
	 * Listener what。
	 */
	private int listenerWhat = 0;

	private boolean isShowText = true;

	public CircleArcProgressbar(Context context) {
		this(context, null);
	}

	public CircleArcProgressbar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleArcProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context, attrs);
	}

	/**
	 * 初始化。
	 *
	 * @param context      上下文。
	 * @param attributeSet 属性。
	 */
	private void initialize(Context context, AttributeSet attributeSet) {
		mPaint.setAntiAlias(true);
	}

	/**
	 * 设置圆形的填充颜色。
	 *
	 * @param inCircleColor 颜色值。
	 */
	public void setInCircleColor(@ColorInt int inCircleColor) {
		this.circleColor = inCircleColor;
		invalidate();
	}

	/**
	 * 设置进度条颜色。
	 *
	 * @param progressLineColor 颜色值。
	 */
	public void setProgressColor(@ColorInt int progressLineColor) {
		this.progressLineColor = progressLineColor;
		invalidate();
	}

	/**
	 * 设置进度条颜色。
	 *
	 * @param currentLineColor 颜色值。
	 */
	public void setCurrentLineColor(@ColorInt int currentLineColor) {
		this.currentLineColor = currentLineColor;
		invalidate();
	}

	/**
	 * 设置进度条线的宽度。
	 *
	 * @param progressLineWidth 宽度值。
	 */
	public void setProgressLineWidth(int progressLineWidth) {
		this.progressLineWidth = progressLineWidth;
		invalidate();
	}

	/**
	 * 拿到此时的进度。
	 *
	 * @return 进度值，最大100，最小0。
	 */
	public long getProgress() {
		return progress;
	}

	/**
	 * 设置倒计时总时间。
	 *
	 * @param timeMillis 毫秒。
	 */
	public void setTimeMillis(long timeMillis, long progressTimeMillis) {
		this.timeMillis = timeMillis / 1000;
		this.progressTimeMillis = progressTimeMillis / 1000;
		this.progress = progressTimeMillis / 1000;
		invalidate();
	}


	/**
	 * 设置倒计时几分钟后进度条的颜色。
	 *
	 * @param currentMillis 毫秒。
	 */
	public void setCurrentMillis(long currentMillis) {
		this.currentMillis = currentMillis;
		invalidate();
	}


	/**
	 * 拿到进度条计时时间。
	 *
	 * @return 毫秒。
	 */
	public long getTimeMillis() {
		return this.timeMillis;
	}

	/**
	 * 设置进度条类型。
	 *
	 * @param progressType {@link ProgressType}.
	 */
	public void setProgressType(ProgressType progressType) {
		this.mProgressType = progressType;
		resetProgress();
		invalidate();
	}

	/**
	 * 是否显示倒计时
	 *
	 * @param isShowText
	 */
	public void setIsShowText(boolean isShowText) {
		this.isShowText = isShowText;
	}

	/**
	 * 重置进度。
	 */
	private void resetProgress() {
		switch (mProgressType) {
			case COUNT:
				progress = 0;
				break;
			case COUNT_BACK:
				progress = progressTimeMillis;
				break;
		}
	}

	/**
	 * 拿到进度条类型。
	 *
	 * @return
	 */
	public ProgressType getProgressType() {
		return mProgressType;
	}

	/**
	 * 设置进度监听。
	 *
	 * @param mCountdownProgressListener 监听器。
	 */
	public void setCountdownProgressListener(int what, OnCountdownProgressListener mCountdownProgressListener) {
		this.listenerWhat = what;
		this.mCountdownProgressListener = mCountdownProgressListener;
	}

	/**
	 * 开始。
	 */
	public void start() {
		stop();
		post(progressChangeTask);
	}

	/**
	 * 重新开始。
	 */
	public void reStart() {
		resetProgress();
		start();
	}

	/**
	 * 停止。
	 */
	public void stop() {
		removeCallbacks(progressChangeTask);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//获取view的边界
		getDrawingRect(bounds);

		int size = bounds.height() > bounds.width() ? bounds.width() : bounds.height();
		float outerRadius = size / 2;

		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(circleColor);
		canvas.drawCircle(bounds.centerX(), bounds.centerY(), outerRadius, mPaint);

		if (isShowText) {
			//画字
			Paint paint = getPaint();
			paint.setColor(getCurrentTextColor());
			paint.setAntiAlias(true);
			paint.setTextAlign(Paint.Align.CENTER);
			float textY = bounds.centerY() - (paint.descent() + paint.ascent()) / 2;
			canvas.drawText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT9, progress * 1000), bounds.centerX(), textY, paint);
		}

		int deleteWidth = progressLineWidth;

		//空心圆
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(progressLineWidth);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		canvas.drawCircle(bounds.centerX(), bounds.centerY(), outerRadius - deleteWidth / 2, mPaint);

		//画进度条
		if (progress > currentMillis) {
			mPaint.setColor(progressLineColor);
		} else {
			mPaint.setColor(currentLineColor);
		}
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(progressLineWidth);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mArcRect.set(bounds.left + deleteWidth / 2, bounds.top + deleteWidth / 2, bounds.right - deleteWidth / 2, bounds.bottom - deleteWidth / 2);

		canvas.drawArc(mArcRect, -90, -360 * progress / timeMillis, false, mPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int size = (width > height ? width : height);
		setMeasuredDimension(size, size);
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
	}

	/**
	 * 进度更新task。
	 */
	private Runnable progressChangeTask = new Runnable() {
		@Override
		public void run() {
			removeCallbacks(this);
			switch (mProgressType) {
				case COUNT:
					progress += 1;
					break;
				case COUNT_BACK:
					progress -= 1;
					break;
			}
			if (progress >= 0) {
				if (mCountdownProgressListener != null)
					mCountdownProgressListener.onProgress(listenerWhat, progress);
				invalidate();
				postDelayed(progressChangeTask, 1000);
			} else
				progress = validateProgress(progress);
		}
	};

	/**
	 * 验证进度。
	 *
	 * @param progress 你要验证的进度值。
	 * @return 返回真正的进度值。
	 */
	private long validateProgress(long progress) {
		if (progress > 100)
			progress = 100;
		else if (progress < 0)
			progress = 0;
		return progress;
	}

	/**
	 * 进度条类型。
	 */
	public enum ProgressType {
		/**
		 * 顺数进度条，从0-100；
		 */
		COUNT,

		/**
		 * 倒数进度条，从100-0；
		 */
		COUNT_BACK;
	}

	/**
	 * 进度监听。
	 */
	public interface OnCountdownProgressListener {

		/**
		 * 进度通知。
		 *
		 * @param progress 进度值。
		 */
		void onProgress(int what, long progress);
	}
}
