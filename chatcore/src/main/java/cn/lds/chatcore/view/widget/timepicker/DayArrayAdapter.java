package cn.lds.chatcore.view.widget.timepicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.lds.chatcore.R;

/**
 * Created by xuqm on 2016/6/16.
 */
public class DayArrayAdapter extends AbstractWheelTextAdapter {
    // Count of days to be shown
    private int daysCount;
    private int startCount;
    private String str;
    private int select;

    // Calendar

    /**
     *
     * @param context
     * @param startCount  开始标志 (2016====自2016开始)
     * @param daysCount  数量  (10=====一共十个)
     * @param select   选择的内容(2017==选择的是2017)
     * @param lab
     */
    public DayArrayAdapter(Context context, int startCount, int daysCount, int select, String lab) {
        super(context, R.layout.time2_day, NO_RESOURCE);
        this.startCount = startCount;
        this.daysCount = daysCount;
        this.str = lab;
        this.select = select;
        setItemTextResource(R.id.time2_monthday);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        // int day = -daysCount / 2 + index;
        int day = index + startCount;

        View view = super.getItem(index, cachedView, parent);

        TextView monthday = (TextView) view
                .findViewById(R.id.time2_monthday);
        if (day == select) {
            monthday.setTextColor(0xFF5d5d5d);
        } else {
            monthday.setTextColor(0xFFe5e5e5);
        }
//        DateFormat format = new SimpleDateFormat("dd日");
        monthday.setText(day + str);

        return view;
    }

    @Override
    public int getItemsCount() {
        return daysCount;
    }

    @Override
    protected CharSequence getItemText(int index) {
        return "";
    }
}