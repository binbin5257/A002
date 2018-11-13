package cn.lds.chatcore.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;
import cn.lds.chatcore.common.pickerview.OptionsPickerView;
import cn.lds.chatcore.common.pickerview.ProvinceBean;
import cn.lds.chatcore.common.pickerview.TimeDataModel;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.data.RevertCarModel;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.adapter.PopMapAdapter;
import cn.lds.chatcore.view.adapter.PopMenuAdapter;
import cn.lds.chatcore.view.widget.ListView.MyListView;
import cn.lds.chatcore.view.widget.timepicker.DayArrayAdapter;
import cn.lds.chatcore.view.widget.timepicker.NumericMinWheelAdapter;
import cn.lds.chatcore.view.widget.timepicker.NumericWheelAdapter;
import cn.lds.chatcore.view.widget.timepicker.OnWheelScrollListener;
import cn.lds.chatcore.view.widget.timepicker.WheelView;

/**
 * Created by pengwb on 2016/2/24.
 * 弹窗助手 选择是或否
 */
public class PopWindowHelper {
    /**
     * 标题
     */
//    private TextView titleTx;
    /**
     * 内容
     */
    private TextView contentTx;
    /**
     * 备注
     */
    private TextView lableTx;
    /**
     * 确定按钮
     */
    private TextView confirmTx;

    /**
     * 取消按钮
     */
    private TextView cancelTx;
    /**
     * 图片
     */
    private ImageView imageView;
    private EditText mEditText;

    private ListView listView;

    private boolean confirm = true;


    private PopupWindow popupWindow = null;
    private View contentView = null;

    public static PopWindowHelper instance;

    static {
        instance = new PopWindowHelper();
    }

    private static ArrayList<ProvinceBean> options1Items;
    private static ArrayList<ArrayList<String>> options2Items;
    private static ArrayList<ArrayList<ArrayList<String>>> options3Items;

    public static PopWindowHelper getInstance() {
        options1Items = new ArrayList<ProvinceBean>();
        options2Items = new ArrayList<ArrayList<String>>();
        options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
        return instance;
    }

    public boolean isShow() {
        if (null != popupWindow) {
            return true;
        }
        return false;
    }

    public void cancle() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    public PopWindowHelper imageView(Context mContext, final PopWindowListener listener) {
        popupWindow = null;

        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindowhelper_imageview, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);
        //绑定控件
        cancelTx = (TextView) contentView.findViewById(R.id.pop_cancel);
        confirmTx = (TextView) contentView.findViewById(R.id.eventselected_popsend);
//        titleTx = (TextView) contentView.findViewById(R.id.eventselected_poptitle);
        contentTx = (TextView) contentView.findViewById(R.id.eventselected_popcontent);
        imageView = (ImageView) contentView.findViewById(R.id.eventselected_popiv);

        confirmTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm();
                popupWindow.dismiss();
                popupWindow = null;
            }
        });


        // 取消，隐藏弹出框
        cancelTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancel();
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        return instance;
    }


    public PopWindowHelper confirmTips(Context mContext, final PopWindowListener listener) {
        popupWindow = null;

        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindowhelper_confirm_tips, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);
        //绑定控件
        cancelTx = (TextView) contentView.findViewById(R.id.pop_cancel);
        confirmTx = (TextView) contentView.findViewById(R.id.eventselected_popsend);
//        titleTx = (TextView) contentView.findViewById(R.id.eventselected_poptitle);
        contentTx = (TextView) contentView.findViewById(R.id.eventselected_popcontent);
        lableTx = (TextView) contentView.findViewById(R.id.eventselected_poplabel);

        setBackKey(true);

        confirmTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm();
                popupWindow.dismiss();
                popupWindow = null;
            }
        });


        // 取消，隐藏弹出框
        cancelTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancel();
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        return instance;
    }

    public PopWindowHelper reback(Context mContext, final PopWindowListener listener) {
        popupWindow = null;

        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindowhelper_reback, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);
        //绑定控件
        cancelTx = (TextView) contentView.findViewById(R.id.pop_cancel);
        confirmTx = (TextView) contentView.findViewById(R.id.eventselected_popsend);
//        titleTx = (TextView) contentView.findViewById(R.id.eventselected_poptitle);
        contentTx = (TextView) contentView.findViewById(R.id.eventselected_popcontent);


        setBackKey(true);

        confirmTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm();
                popupWindow.dismiss();
                popupWindow = null;
            }
        });


        // 取消，隐藏弹出框
        cancelTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancel();
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        return instance;
    }


    public PopWindowHelper remind(Context mContext, final PopWindowListener listener) {

        if(popupWindow == null){
            contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindowhelper_remind, null);
            // 声明一个弹出框
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            // 声明一个弹出框
            popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                    .getDefaultDisplay().getHeight());
            popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));

            // 为弹出框设定自定义的布局
            popupWindow.setContentView(contentView);
            //若不设置，键盘不弹出
            popupWindow.setFocusable(true);
            //绑定控件
            cancelTx = (TextView) contentView.findViewById(R.id.pop_cancel);
            confirmTx = (TextView) contentView.findViewById(R.id.eventselected_popsend);
//        titleTx = (TextView) contentView.findViewById(R.id.eventselected_poptitle);
            contentTx = (TextView) contentView.findViewById(R.id.eventselected_popcontent);


            setBackKey(true);

            confirmTx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.confirm();
                    popupWindow.dismiss();
                    popupWindow = null;

                }
            });

            // 取消，隐藏弹出框
            cancelTx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.cancel();
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            });
        }else{
            popupWindow.dismiss();
        }



        return instance;
    }


    public PopWindowHelper confirm(Context mContext, final PopWindowListener listener) {
        if(popupWindow == null){
            contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindowhelper_confirm, null);
            // 声明一个弹出框
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            // 声明一个弹出框
            popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                    .getDefaultDisplay().getHeight());
            popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));

            // 为弹出框设定自定义的布局
            popupWindow.setContentView(contentView);
            //若不设置，键盘不弹出
            popupWindow.setFocusable(true);
            //绑定控件
            cancelTx = (TextView) contentView.findViewById(R.id.pop_cancel);
            confirmTx = (TextView) contentView.findViewById(R.id.eventselected_popsend);
//        titleTx = (TextView) contentView.findViewById(R.id.eventselected_poptitle);
            contentTx = (TextView) contentView.findViewById(R.id.eventselected_popcontent);


            setBackKey(true);

            confirmTx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm = true;
                    popupWindow.dismiss();

                }
            });
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;
                    if(confirm){
                        listener.confirm();
                    }

                }
            });


            // 取消，隐藏弹出框
            cancelTx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm = false;
                    popupWindow.dismiss();
                    listener.cancel();
                }
            });
        }else{
            popupWindow.dismiss();
        }
        return instance;
    }


    public PopWindowHelper alert(Context mContext, final PopWindowListener listener) {
        if(popupWindow == null){
            contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_alert_m, null);
            // 声明一个弹出框
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            // 声明一个弹出框
            popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                    .getDefaultDisplay().getHeight());
            popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));


            // 为弹出框设定自定义的布局
            popupWindow.setContentView(contentView);

            //绑定控件
            confirmTx = (TextView) contentView.findViewById(R.id.eventselected_popsend);
            contentTx = (TextView) contentView.findViewById(R.id.eventselected_popcontent);
            setBackKey(true);
            RelativeLayout confirm = (RelativeLayout) contentView.findViewById(R.id.pop_confirm_botton_ryt);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.confirm();
                        popupWindow.dismiss();
                        popupWindow = null;
                    } catch (Exception ex) {
                        LogHelper.e("", ex);
                    }
                }
            });
        }else {
            popupWindow.dismiss();
        }

        return instance;
    }


//    public PopWindowHelper setTitleTx(String text) {
////        titleTx.setVisibility(View.VISIBLE);
////        titleTx.setText(text);
//        return instance;
//    }

    public PopWindowHelper setContentTx(String text) {
        contentTx.setVisibility(View.VISIBLE);
        contentTx.setText(text);
        return instance;
    }
    public PopWindowHelper setContentTx(Spanned text) {
        contentTx.setVisibility(View.VISIBLE);
        contentTx.setText(text);
        return instance;
    }

    public PopWindowHelper setContentTxColor(int color) {
        contentTx.setTextColor(color);
        return instance;
    }

    public PopWindowHelper setContentTxSize(float color) {
        contentTx.setTextSize(color);
        return instance;
    }

    public PopWindowHelper setLableTx(String text) {
        lableTx.setText(text);
        return instance;
    }

    public PopWindowHelper setLableTxColor(int color) {
        lableTx.setTextColor(color);
        return instance;
    }

    public PopWindowHelper setLableTxSize(float color) {
        lableTx.setTextSize(TypedValue.COMPLEX_UNIT_PX, color);
        return instance;
    }

    public PopWindowHelper setConfirmTx(String text) {
        confirmTx.setText(text);
        return instance;
    }

    public PopWindowHelper setConfirmTxColor(int color) {
        confirmTx.setTextColor(color);
        return instance;
    }

    public PopWindowHelper setCancelTx(String text) {
        cancelTx.setText(text);
        return instance;
    }

    public PopWindowHelper setCancelTxColor(int color) {
        cancelTx.setTextColor(color);
        return instance;
    }

    public PopWindowHelper setImageView(String imageid) {
        ImageLoaderManager.getInstance().displayAvatar(imageid, imageView);
        return instance;
    }

    /**
     * 设置 手机返回键是否起作用
     *
     * @param flag
     * @return
     */
    public PopWindowHelper setBackKey(boolean flag) {
        popupWindow.setFocusable(flag);
        return instance;
    }

    public void show(View view) {
        popupWindow.showAsDropDown(view);
    }
    public void show(final View view,final View mainView) {
         final Handler mHandler = new Handler();
        Runnable showPopWindowRunnable = new Runnable() {

            @Override
            public void run() {
                // 如何根元素的width和height大于0说明activity已经初始化完毕
                if( mainView != null && mainView.getWidth() > 0 && mainView.getHeight() > 0) {
                    // 显示popwindow
                    popupWindow.showAsDropDown(view);
                    // 停止检测
                    mHandler.removeCallbacks(this);
                } else {
                    // 如果activity没有初始化完毕则等待5毫秒再次检测
                    mHandler.postDelayed(this, 5);
                }
            }
        };
        showPopWindowRunnable.run();

    }

    public void showCenter(View mainView) {
        popupWindow.showAtLocation(mainView, Gravity.CENTER, 0, 0);
    }

    /**
     * 弹出菜单的popWindow
     *
     * @param mContext
     * @param data
     * @param isShowIcon 是否显示图标和选择标识  如果不显示则内容居中显示 否则左对齐显示
     * @param listener   监听onItemListener(int position)方法
     * @return
     */
    public PopWindowHelper menu(Context mContext, List<PopData> data, boolean isShowIcon, final PopWindowListener listener) {
        popupWindow = null;


        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_menu, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));


        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
//        popupWindow.setFocusable(true);

        //绑定控件
        MyListView myListView = (MyListView) contentView.findViewById(R.id.pop_menu_lists_lv);
        TextView titleTx = (TextView) contentView.findViewById(R.id.pop_cancel);

        PopMenuAdapter adapter = new PopMenuAdapter(mContext, data);
        adapter.setShowIcon(isShowIcon);
        myListView.setAdapter(adapter);

        ViewHeightCalculator.setListViewHeightBasedOnChildren(myListView);
        adapter.setOnItemClickListener(new PopMenuAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                try {
                    listener.onItemListener(position);
                    popupWindow.dismiss();
                    popupWindow = null;
                } catch (Exception ex) {
                    LogHelper.e("", ex);
                }
            }
        });

        titleTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listener.cancel();
                    popupWindow.dismiss();
                    popupWindow = null;
                } catch (Exception ex) {
                    LogHelper.e("", ex);
                }
            }
        });
        return instance;
    }

    /**
     * 菜单栏，右上角 客服
     *
     * @param mContext
     * @return
     */
    public PopWindowHelper openCustomerservice(final Context mContext) {
        return openTel(mContext, CacheHelper.getServiceTel());
    }

    /**
     * 咨询客服
     *
     * @param mContext
     * @return
     */
    public PopWindowHelper openCustomerConsult(final Context mContext) {
        return openTel(mContext, CacheHelper.getServiceTel());
    }

    /**
     * 打电话
     *
     * @param mContext
     * @return
     */
    public PopWindowHelper openTel(final Context mContext, final String phone) {
        confirm(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.PHONE_CONSULT));//直接拨打
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));//跳转到拨打界面
                mContext.startActivity(intent);
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setConfirmTx(mContext.getString(R.string.customer_confirm)).setContentTx(phone);
        return instance;
    }


    /**
     * 弹出时间选择的popWindow
     *
     * @param mContext
     * @param listener 监听onItemListener(int position)方法
     * @return
     */
    private WheelView month;
    private WheelView year;
    private WheelView day;
    private TextView week;
    private WheelView hours;
    private WheelView mins;
    private long passTime;
    private int yearItem;
    private int monthItem;
    private int dayItem;
    private int hourItem;
    private int minItem;

    public PopWindowHelper timePicker(Context mContext, String time, final PopWindowListener2 listener) {
        popupWindow = null;
        this.mContext = mContext;

        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_timepicker, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));


        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);

        TextView time_picker_cancel_tv = (TextView) contentView.findViewById(R.id.time_picker_cancel_tv);
        TextView time_picker_confirm_tv = (TextView) contentView.findViewById(R.id.time_picker_confirm_tv);
        time_picker_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancel();
                popupWindow.dismiss();
                popupWindow = null;

            }
        });
        time_picker_confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String h = hours.getCurrentItem() < 10 ? ("0" + hours.getCurrentItem()) : ("" + hours.getCurrentItem());
                String m = mins.getCurrentItem() < 10 ? ("0" + mins.getCurrentItem()) : ("" + mins.getCurrentItem());
                listener.confirm((year.getCurrentItem() + curYear - 5) + "/" + (month.getCurrentItem() + 1) + "/" + (day.getCurrentItem() + 1) + "/" + h + ":" + m);
//                listener.confirm((month.getCurrentItem() + 1) + "/" + (day.getCurrentItem() + 1) + "/" + h + ":" + m);
                popupWindow.dismiss();
                popupWindow = null;

            }
        });
        int curMonth = 1;
        int curDay = 1;
        int curHour = 1;
        int curMin = 1;
        if (!ToolsHelper.isNull(time)) {
            String[] times = time.split("/");
            if (null == times || times.length < 4) {
                ToolsHelper.showStatus(mContext, false, "传入时间格式错误");
            } else {
                String[] h = times[3].split(":");
                if (null == h || h.length < 2) {
                    ToolsHelper.showStatus(mContext, false, "传入时间格式错误");
                } else {
                    curYear = Integer.valueOf(times[0]).intValue();
                    curMonth = Integer.valueOf(times[1]).intValue();
                    curDay = Integer.valueOf(times[2]).intValue();
                    curHour = Integer.valueOf(h[0]).intValue();
                    curMin = Integer.valueOf(h[1]).intValue();
                }
            }
        }
        if (-1 == curYear) {

            Calendar calendar = Calendar.getInstance(Locale.US);
            curYear = calendar.get(Calendar.YEAR);
            curMonth = calendar.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
            curDay = calendar.get(Calendar.DATE);
            curHour = calendar.get(Calendar.HOUR_OF_DAY);
            curMin = calendar.get(Calendar.MINUTE);
        }
        //绑定控件
        week = (TextView) contentView.findViewById(R.id.time_picker_week_tv);
        month = (WheelView) contentView.findViewById(R.id.month);
        year = (WheelView) contentView.findViewById(R.id.year);
        day = (WheelView) contentView.findViewById(R.id.day);

        year.setViewAdapter(new DayArrayAdapter(mContext, curYear - 5, 11, curYear, "年"));
        year.setCurrentItem(5);
        year.addScrollingListener(monthscrollListener);
        year.setCyclic(true);

        month.setViewAdapter(new DayArrayAdapter(mContext, 1, 12, curMonth, "月"));
        month.setCurrentItem(curMonth - 1);
        month.addScrollingListener(monthscrollListener);
        month.setCyclic(true);

        initDay(curYear, curMonth, curDay);
        day.setCurrentItem(curDay - 1);
        day.addScrollingListener(monthscrollListener);
        day.setCyclic(true);


        //
        // 小时
        hours = (WheelView) contentView.findViewById(R.id.h);
        // （min ,max）
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(mContext, 0, 23,
                "%02d", curHour);
//        hourAdapter.setItemResource(R.layout.wheel_text_item);
//        hourAdapter.setItemTextResource(R.id.text);
        hours.setViewAdapter(hourAdapter);
        hours.addScrollingListener(monthscrollListener);
        hours.setCyclic(true);

        // 分钟
        mins = (WheelView) contentView.findViewById(R.id.m);
        // （min ,max ,format）
        NumericWheelAdapter minAdapter = new NumericWheelAdapter(mContext, 0, 59,
                "%02d", curMin);
//        minAdapter.setItemResource(R.layout.wheel_text_item);
//        minAdapter.setItemTextResource(R.id.text);
        mins.setViewAdapter(minAdapter);
        mins.addScrollingListener(monthscrollListener);
        mins.setCyclic(true);
        // 当前时间
        // set current time
        hours.setCurrentItem(curHour);
        mins.setCurrentItem(curMin);

        yearItem = year.getCurrentItem();
        monthItem = month.getCurrentItem();
        dayItem = day.getCurrentItem();
        hourItem = hours.getCurrentItem();
        minItem = mins.getCurrentItem();
        passTime = TimeHelper.getTime(TimeHelper.FORMAT7, time);


        return instance;
    }

    public PopWindowHelper timeIntervalPicker(Context mContext, String time, final PopWindowListener2 listener) {
        popupWindow = null;
        this.mContext = mContext;

        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_timepicker, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));


        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);

        TextView time_picker_cancel_tv = (TextView) contentView.findViewById(R.id.time_picker_cancel_tv);
        TextView time_picker_confirm_tv = (TextView) contentView.findViewById(R.id.time_picker_confirm_tv);
        time_picker_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancel();
                popupWindow.dismiss();
                popupWindow = null;

            }
        });
        time_picker_confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String h = hours.getCurrentItem() < 10 ? ("0" + hours.getCurrentItem()) : ("" + hours.getCurrentItem());
                String m = String.format("%02d", mins.getCurrentItem() * 10);
                listener.confirm((year.getCurrentItem() + curYear - 5) + "/" + (month.getCurrentItem() + 1) + "/" + (day.getCurrentItem() + 1) + "/" + h + ":" + m);
//                listener.confirm((month.getCurrentItem() + 1) + "/" + (day.getCurrentItem() + 1) + "/" + h + ":" + m);
                popupWindow.dismiss();
                popupWindow = null;

            }
        });
        int curMonth = 1;
        int curDay = 1;
        int curHour = 1;
        int curMin = 1;
        if (!ToolsHelper.isNull(time)) {
            String[] times = time.split("/");
            if (null == times || times.length < 4) {
                ToolsHelper.showStatus(mContext, false, "传入时间格式错误");
            } else {
                String[] h = times[3].split(":");
                if (null == h || h.length < 2) {
                    ToolsHelper.showStatus(mContext, false, "传入时间格式错误");
                } else {
                    curYear = Integer.valueOf(times[0]).intValue();
                    curMonth = Integer.valueOf(times[1]).intValue();
                    curDay = Integer.valueOf(times[2]).intValue();
                    curHour = Integer.valueOf(h[0]).intValue();
                    curMin = Integer.valueOf(h[1]).intValue();
                }
            }
        }
        if (-1 == curYear) {

            Calendar calendar = Calendar.getInstance(Locale.US);
            curYear = calendar.get(Calendar.YEAR);
            curMonth = calendar.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
            curDay = calendar.get(Calendar.DATE);
            curHour = calendar.get(Calendar.HOUR_OF_DAY);
            curMin = calendar.get(Calendar.MINUTE);
        }
        //绑定控件
        week = (TextView) contentView.findViewById(R.id.time_picker_week_tv);
        month = (WheelView) contentView.findViewById(R.id.month);
        year = (WheelView) contentView.findViewById(R.id.year);
        day = (WheelView) contentView.findViewById(R.id.day);

        year.setViewAdapter(new DayArrayAdapter(mContext, curYear - 5, 11, curYear, "年"));
        year.setCurrentItem(5);
        year.addScrollingListener(intervalscrollListener);
        year.setCyclic(true);

        month.setViewAdapter(new DayArrayAdapter(mContext, 1, 12, curMonth, "月"));
        month.setCurrentItem(curMonth - 1);
        month.addScrollingListener(intervalscrollListener);
        month.setCyclic(true);

        initDay(curYear, curMonth, curDay);
        day.setCurrentItem(curDay - 1);
        day.addScrollingListener(intervalscrollListener);
        day.setCyclic(true);


        //
        // 小时
        hours = (WheelView) contentView.findViewById(R.id.h);
        // （min ,max）
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(mContext, 0, 23,
                "%02d", curHour);
//        hourAdapter.setItemResource(R.layout.wheel_text_item);
//        hourAdapter.setItemTextResource(R.id.text);
        hours.setViewAdapter(hourAdapter);
        hours.addScrollingListener(intervalscrollListener);
        hours.setCyclic(true);

        // 分钟
        mins = (WheelView) contentView.findViewById(R.id.m);
        // （min ,max ,format）

        int interval = 10;
        curMin = curMin / 10;

        NumericMinWheelAdapter minAdapter = new NumericMinWheelAdapter(mContext, 0, 50, interval,
                "%02d", curMin);
//        minAdapter.setItemResource(R.layout.wheel_text_item);
//        minAdapter.setItemTextResource(R.id.text);
        mins.setViewAdapter(minAdapter);
        mins.addScrollingListener(intervalscrollListener);
        mins.setCyclic(true);
        // 当前时间
        // set current time
        hours.setCurrentItem(curHour);
        mins.setCurrentItem(curMin);

        yearItem = year.getCurrentItem();
        monthItem = month.getCurrentItem();
        dayItem = day.getCurrentItem();
        hourItem = hours.getCurrentItem();
        minItem = mins.getCurrentItem();
        passTime = TimeHelper.getTime(TimeHelper.FORMAT7, time);


        return instance;
    }

    public static void main(String[] args) {
        int c = 9;
        int a = c / 10;
        System.out.println(a);
    }

    OnWheelScrollListener dayscrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
            initDay(curYear, month.getCurrentItem() + 1, day.getCurrentItem() + 1);
        }
    };
    OnWheelScrollListener monthscrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
//			if (yearItem > year.getCurrentItem()) {
//				year.setCurrentItem(yearItem);
//			} else if (yearItem == year.getCurrentItem()) {
//				if (monthItem > month.getCurrentItem()) {
//					month.setCurrentItem(monthItem);
//				} else if (monthItem == month.getCurrentItem()) {
//					if (dayItem > day.getCurrentItem()) {
//						day.setCurrentItem(dayItem);
//					} else if (dayItem == day.getCurrentItem()) {
//						if (hourItem > hours.getCurrentItem()) {
//							hours.setCurrentItem(hourItem);
//						} else if (hourItem == hours.getCurrentItem()) {
//							if (minItem > mins.getCurrentItem()) {
//								mins.setCurrentItem(minItem);
//							}
//						}
//					}
//				}
//			}
            int sYear = year.getCurrentItem() + curYear - 5;
            int sMonth = month.getCurrentItem() + 1;
            int arg3 = day.getCurrentItem() + 1;
            int sDay = arg3 > getDay(curYear, sMonth) ? getDay(curYear, sMonth) : arg3;
            int sHours = hours.getCurrentItem();
            int sMin = mins.getCurrentItem();
            long sTime = TimeHelper.getTime(TimeHelper.FORMAT7, sYear + "/" + sMonth + "/" + sDay + "/" + sHours + ":" + sMin);
            if (sTime < passTime) {
                year.setCurrentItem(yearItem);
                month.setCurrentItem(monthItem);
                day.setCurrentItem(dayItem);
                hours.setCurrentItem(hourItem);
                mins.setCurrentItem(minItem);
            }
            initMonth();
            hours.setViewAdapter(new NumericWheelAdapter(mContext, 0, 23,
                    "%02d", hours.getCurrentItem()));
            mins.setViewAdapter(new NumericWheelAdapter(mContext, 0, 59,
                    "%02d", mins.getCurrentItem()));
        }
    };

    OnWheelScrollListener intervalscrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
//			if (yearItem > year.getCurrentItem()) {
//				year.setCurrentItem(yearItem);
//			} else if (yearItem == year.getCurrentItem()) {
//				if (monthItem > month.getCurrentItem()) {
//					month.setCurrentItem(monthItem);
//				} else if (monthItem == month.getCurrentItem()) {
//					if (dayItem > day.getCurrentItem()) {
//						day.setCurrentItem(dayItem);
//					} else if (dayItem == day.getCurrentItem()) {
//						if (hourItem > hours.getCurrentItem()) {
//							hours.setCurrentItem(hourItem);
//						} else if (hourItem == hours.getCurrentItem()) {
//							if (minItem > mins.getCurrentItem()) {
//								mins.setCurrentItem(minItem);
//							}
//						}
//					}
//				}
//			}
            int sYear = year.getCurrentItem() + curYear - 5;
            int sMonth = month.getCurrentItem() + 1;
            int arg3 = day.getCurrentItem() + 1;
            int sDay = arg3 > getDay(curYear, sMonth) ? getDay(curYear, sMonth) : arg3;
            int sHours = hours.getCurrentItem();
            int sMin = mins.getCurrentItem() * 10;
            long sTime = TimeHelper.getTime(TimeHelper.FORMAT7, sYear + "/" + sMonth + "/" + sDay + "/" + sHours + ":" + sMin);
            long minPassTime = System.currentTimeMillis() + 10 *60 * 1000;
            if (sTime < minPassTime) {
                year.setCurrentItem(yearItem);
                month.setCurrentItem(monthItem);
                day.setCurrentItem(dayItem);
                hours.setCurrentItem(hourItem);
                mins.setCurrentItem(minItem);
            }else{
                passTime = minPassTime;
            }
            initMonth();
            hours.setViewAdapter(new NumericWheelAdapter(mContext, 0, 23,
                    "%02d", hours.getCurrentItem()));
            mins.setViewAdapter(new NumericMinWheelAdapter(mContext, 0, 50, 10,
                    "%02d", mins.getCurrentItem()));
        }
    };

    private void initMonth() {
        int sYear = year.getCurrentItem() + curYear - 5;
        year.setViewAdapter(new DayArrayAdapter(mContext, curYear - 5, 11, sYear, "年"));
        month.setViewAdapter(new DayArrayAdapter(mContext, 1, 12, month.getCurrentItem() + 1, "月"));
        initDay(sYear, month.getCurrentItem() + 1, day.getCurrentItem() + 1);
    }

    private Context mContext;
    private int curYear = -1;

    /**
     * @param curYear 年
     * @param arg2    月
     * @param arg3    日
     */
    private void initDay(int curYear, int arg2, int arg3) {
        day.setViewAdapter(new DayArrayAdapter(mContext, 1, getDay(curYear, arg2), arg3 > getDay(curYear, arg2) ? getDay(curYear, arg2) : arg3, "日"));
        day.setCurrentItem(arg3 > getDay(curYear, arg2) ? getDay(curYear, arg2) - 1 : arg3 - 1);

        initWeek(curYear);
    }

    private String w;

    private void initWeek(int curYear) {
        w = getWeek(curYear + "-" + (month.getCurrentItem() + 1) + "-" + (day.getCurrentItem() + 1));
        week.setText("周" + w);
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int days = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 2:
                days = flag ? 29 : 28;
                break;
            default:
                days = 30;
                break;
        }
        return days;
    }

    private String getWeek(String pTime) {


        String Week = "";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "六";
        }


        return Week;
    }


    /**
     * 弹出导航的popWindow
     *
     * @param mContext
     * @param listener 监听onItemListener(int position)方法
     * @return
     */
    public PopWindowHelper map(final Context mContext, final boolean isDriving, final RevertCarModel revertCarModel, final PopWindowListener3 listener) {
        popupWindow = null;


        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_menu, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));


        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
//        popupWindow.setFocusable(true);

        //绑定控件
        MyListView myListView = (MyListView) contentView.findViewById(R.id.pop_menu_lists_lv);
        TextView titleTx = (TextView) contentView.findViewById(R.id.pop_cancel);

        final List<PopData> dataList = new ArrayList<>();
        if (isInstallByread("com.baidu.BaiduMap"))
            dataList.add(new PopData(R.drawable.map_baidu, "百度地图", true));
        else
            dataList.add(new PopData(R.drawable.map_baidu, "百度地图", false));
        if (isInstallByread("com.autonavi.minimap"))
            dataList.add(new PopData(R.drawable.map_gaode, "高德地图", true));
        else
            dataList.add(new PopData(R.drawable.map_gaode, "高德地图", false));

        PopMapAdapter adapter = new PopMapAdapter(mContext, dataList);
        adapter.setShowIcon(false);
        myListView.setAdapter(adapter);

        ViewHeightCalculator.setListViewHeightBasedOnChildren(myListView);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = "北京";
                if (!ToolsHelper.isNull(MyApplication.lastCity)) {
                    city = MyApplication.lastCity;
                }
                try {
                    if (dataList.get(position).isSelect()) {
                        String str = "";
                        if (0 == position) {
                            String mode = "driving";
                            if (isDriving) {
                                mode = "driving";
                            } else {
                                mode = "walking";
                            }
                            //调起百度地图客户端
                            str = "intent://map/direction?origin=" + MyApplication.myLocation.latitude + ","
                                    + MyApplication.myLocation.longitude
                                    + "&destination=" + revertCarModel.getLatitude() + ","
                                    + revertCarModel.getLongitude() + "&mode=" + mode + "&region="
                                    + city + "&src=" + mContext.getString(R.string.app_name) + "|" + AppIndependentConfigure.SYS_CONFIG_APP_PACKAGE + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";


                        } else {
                            String mode = "driving";
                            if (isDriving) {
                                mode = "2";
                            } else {
                                mode = "4";
                            }
                            RevertCarModel re = ToolsHelper.bdToGgFromLon(revertCarModel.getLongitude(), revertCarModel.getLatitude());
                            RevertCarModel startRe = ToolsHelper.bdToGgFromLon(MyApplication.myLocation.latitude, MyApplication.myLocation.longitude);
                            str = "androidamap://route?sourceApplication="
                                    + mContext.getString(R.string.app_name)
                                    + "&dlat=" + re.getLatitude() + "&dlon=" + re.getLongitude() + "&dname=" + revertCarModel.getName() + "&dev=0&t=" + mode;
//                            str = "androidamap://navi?sourceApplication=appname&poiname=&lat=" + re.getLatitude() + "&lon=" + re.getLongitude() + "&dev=0&style=2";
                        }
                        Intent intent = Intent.getIntent(str);
                        try {
                            mContext.startActivity(intent); //启动调用
                        } catch (Exception e) {
                            LogHelper.e("打开地图导航", e);
                        }
                        listener.onItemListener(position);
                    } else {
//                        ToolsHelper.showStatus(mContext, false, "请下载对应地图客户端");
                        listener.failed();
                    }
                    popupWindow.dismiss();
                    popupWindow = null;
                } catch (Exception ex) {
                    LogHelper.e("", ex);
                }
            }
        });

        titleTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listener.cancel();
                    popupWindow.dismiss();
                    popupWindow = null;
                } catch (Exception ex) {
                    LogHelper.e("", ex);
                }
            }
        });
        return instance;
    }

    /**
     * 弹出窗口，提供选择租车模式，立即用车、预约用车
     *
     * @param mContext
     * @param listener 监听onItemListener(int position)方法
     * @return
     */
    public PopWindowHelper choiceType(Context mContext, final TextView top_title_btn, final OnChoiceTypeListener listener) {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_choicetype, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());


        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
//        popupWindow.setFocusable(true);

        LinearLayout textView1 = (LinearLayout) popupWindow.getContentView().findViewById(R.id.pop_choicetype_1);
        LinearLayout textView2 = (LinearLayout) popupWindow.getContentView().findViewById(R.id.pop_choicetype_2);
        LinearLayout textView3 = (LinearLayout) popupWindow.getContentView().findViewById(R.id.pop_choicetype_3);
        LinearLayout bg = (LinearLayout) popupWindow.getContentView().findViewById(R.id.pop_bg);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInstantCarEvent();
//                top_title_btn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, top_title_btn_down);
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBookingCarEvent();
//                top_title_btn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, top_title_btn_down);
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChargingPileEvent();
//                top_title_btn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, top_title_btn_down);
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                top_title_btn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, top_title_btn_down);
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

        return instance;
    }

    public interface OnChoiceTypeListener{
        void onInstantCarEvent();
        void onBookingCarEvent();
        void onChargingPileEvent();
    }


    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public void showOptions(Context context,String title,final PopWindowListener2 listener) {
        //选项选择器
        final OptionsPickerView pvOptions = new OptionsPickerView(context);
        // 初始化三个列表数据
        TimeDataModel.initData(options1Items, options2Items, options3Items);
        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        pvOptions.setTitle(title);
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setTextSize(18);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String yearAndMonthAndDay = options1Items.get(options1).getDescription();
                String hours = options2Items.get(options1).get(option2).split("时")[0];
                String min = options3Items.get(options1).get(option2).get(options3).split("分")[0];
                listener.confirm(yearAndMonthAndDay + hours + ":" + min);


            }
        });
        pvOptions.show();
    }


}
