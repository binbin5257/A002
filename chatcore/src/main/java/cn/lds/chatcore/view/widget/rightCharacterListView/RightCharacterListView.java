package cn.lds.chatcore.view.widget.rightCharacterListView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.lds.chatcore.R;

/**
 * 右侧字母表，快速定位
 */
public class RightCharacterListView extends View {

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    static String[] b = new String[]{};

    public static void setB(String[] bar) {
        //清空数组
        if (b.length != 0) {
            b = new String[]{};
        }
        RightCharacterListView.b = bar;
    }

    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;

    public RightCharacterListView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    public RightCharacterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RightCharacterListView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }
        int width = getWidth();
        int height = getHeight();
//        int singleHeight = 42;
        int singleHeight = 0;

        try {
            singleHeight = height / b.length;
        } catch (ArithmeticException e) {
        }

        if (b.length == 0)
            return;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.parseColor("#888888"));
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);
            paint.setTextSize(getContext().getResources().getDimension(
                    R.dimen.friend_right_character_size));
            if (i == choose) {
                paint.setColor(Color.parseColor("#da0101"));
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;

            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        // 如果第一个字母是#，无效点击的话，条件变为c>0
                        listener.onTouchingLetterChanged(b[c]);
                        listener.showLetter(b[c]);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        // 如果第一个字母是#，无效点击的话，条件变为c>0
                        listener.onTouchingLetterChanged(b[c]);
                        listener.showLetter(b[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                listener.ehdTextView();
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);

        public void showLetter(String s);

        public void ehdTextView();

    }
}