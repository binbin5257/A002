package cn.lds.im.view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.DimensHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.im.data.HomeActiveModel;
import cn.lds.im.data.LandLockListModel;
import cn.lds.im.view.adapter.CallChargingAndServiceAdapter;
import cn.lds.im.view.adapter.ChoicePayWayAdapter;
import cn.lds.im.view.adapter.ChoiceReceiptTypeAdapter;
import cn.lds.im.view.adapter.CityListAdapter;
import cn.lds.im.view.adapter.PayWayAdapter;
import cn.lds.im.view.adapter.SearchRangeAdapter;


/**
 * 自定义popuwindow类
 */

public class CustomPopuwindow {

    private OnChoicePayWayListener onChoicePayWayListener;
    private PopupWindow popupWindow;
    private static CustomPopuwindow instance;
    private OnChoiceCityListener onChoiceCityListener;

    private OnHomeActiveListener onHomeActiveListener;
    private RelativeLayout checkedRlyt;
    private TextView textView;
    private ImageView chargingIv;
    private ImageView accIv;
    private ImageView doorIv;
    private ImageView lightIv;

    public interface OnHomeActiveListener{
        void onCloseActiveEvent();
    }

    public void setOnChoiceCityListener(OnChoiceCityListener onChoiceCityListener) {
        this.onChoiceCityListener = onChoiceCityListener;
    }

    public static CustomPopuwindow getInstance(){
        if(instance == null){
            instance = new CustomPopuwindow();
        }
        return instance;
    }

    public void setOnChoicePayWayListener(OnChoicePayWayListener onChoicePayWayListener) {
        this.onChoicePayWayListener = onChoicePayWayListener;
    }

    public interface OnChoiceCityListener{
        void selectedCity(Object data,int index);
        void dismiss();
    }
    public interface OnChoicePayWayListener{
        void selectedPayWay(int pay);
    }

    private OnSelectReceiptTypeListener mOnSelectReceiptTypeListener;

    public void setOnSelectReceiptTypeListener(OnSelectReceiptTypeListener onSelectReceiptTypeListener){
        this.mOnSelectReceiptTypeListener = onSelectReceiptTypeListener;
    }
    public interface OnSelectReceiptTypeListener{
        void selectedReceiptType(int index);
    }

    public void choiceType(View view, Activity context, final OnChoiceTypeListener listener){
        View contentView = LayoutInflater.from(context).inflate(R.layout.popwindow_choicetype, null);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final PopupWindow popupWindow = new PopupWindow(contentView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());

        LinearLayout textView1 = (LinearLayout) contentView.findViewById(R.id.pop_choicetype_1);
        LinearLayout textView2 = (LinearLayout) contentView.findViewById(R.id.pop_choicetype_2);
        LinearLayout textView3 = (LinearLayout) contentView.findViewById(R.id.pop_choicetype_3);
        LinearLayout bg = (LinearLayout) contentView.findViewById(R.id.pop_bg);
        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInstantCarEvent();
                popupWindow.dismiss();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBookingCarEvent();
                popupWindow.dismiss();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChargingPileEvent();
                popupWindow.dismiss();
            }
        });
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        if (Build.VERSION.SDK_INT >= 24) {
            int[] mLocation = new int[2];
            view.getLocationInWindow(mLocation);
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, mLocation[1] + view.getHeight());
        } else {
            popupWindow.showAsDropDown(view);
        }
    }

    public interface OnChoiceTypeListener{
        void onInstantCarEvent();
        void onBookingCarEvent();
        void onChargingPileEvent();
    }

    /**
     * 选择支付方式
     */
    public void selectPayWay(final Activity context, View mainView, int PAY_WAY){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_choose_pay_way
                ,null);
        ListView lv = (ListView) view.findViewById(R.id.list_choose_pay);
        lv.setAdapter(new ChoicePayWayAdapter(context,PAY_WAY));

        final PopupWindow popupWindow = new PopupWindow(view,
                600, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundColor(context,1f);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onChoicePayWayListener.selectedPayWay(position);
                popupWindow.dismiss();

            }
        });
        setBackGroundColor(context,0.7f);

    }
    /**
     * 选择发票开票类型
     */
    public void selectReceiptType(final Activity context, View mainView, final List<PopData> list, final OnSelectReceiptTypeListener onSelectReceiptTypeListener){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_choose_pay_way
                ,null);
        ListView lv = (ListView) view.findViewById(R.id.list_choose_pay);
        lv.setAdapter(new ChoiceReceiptTypeAdapter(context,list));

        final PopupWindow popupWindow = new PopupWindow(view,
                DimensHelper.dip2px(context,160), RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundColor(context,1f);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                onSelectReceiptTypeListener.selectedReceiptType(position);


            }
        });
        setBackGroundColor(context,0.7f);

    }

    /**
     * 选择支付方式(只有微信和支付宝)
     */
    public void payWay(final Activity context, View mainView, int PAY_WAY){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_choose_pay_way
                ,null);
        ListView lv = (ListView) view.findViewById(R.id.list_choose_pay);
        lv.setAdapter(new PayWayAdapter(context,PAY_WAY));
        final PopupWindow popupWindow = initShowCenterPopuwindow(context, mainView, view);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_black_bg));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onChoicePayWayListener.selectedPayWay(position);
                popupWindow.dismiss();

            }
        });


    }

    /**
     * 初始化一个显示在屏幕中间的pop
     * @param context
     * @param mainView
     * @param view
     * @return
     */
    @NonNull
    private PopupWindow initShowCenterPopuwindow(final Activity context, View mainView, View view) {
        final PopupWindow popupWindow = new PopupWindow(view,
                600, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
        return popupWindow;
    }

    /**
     * 设置窗体透明度
     */
    public void setBackGroundColor(Activity context,float alpha){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);

    }
    /**
     * 控车popuwindow
     */
    public void showControllerCar(final Activity context, int controCarWay, View mainView){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_controller_car
                ,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_text);
        if(controCarWay == 0){
            textView.setText("开门执行中，请稍后...");
        }else if(controCarWay == 1){
            textView.setText("关门执行中，请稍后...");
        }else if(controCarWay == 2){
            textView.setText("鸣笛闪灯执行中，请稍后...");
        }else if(controCarWay == 3){
            textView.setText("闪灯执行中，请稍后...");
        }else if(controCarWay == 4){
            textView.setText("车位锁下落执行中，请稍后...");
        }

        popupWindow = new PopupWindow(view,
                DimensHelper.dip2px(context,220), RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundColor(context,1f);
                ToolsHelper.showStatus(context, true, "操作成功");
            }
        });
        setBackGroundColor(context,0.7f);
    }
    boolean notNow = false;
    /**
     * 控车popuwindow
     */
    public void checkCarStatus(final Activity context,View mainView){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_controller_car
                ,null);
        textView = (TextView) view.findViewById(R.id.tv_text);
        checkedRlyt = (RelativeLayout) view.findViewById(R.id.rl_car_check);
        chargingIv = (ImageView) view.findViewById(R.id.iv_charging);
        accIv = (ImageView) view.findViewById(R.id.iv_acc);
        doorIv = (ImageView) view.findViewById(R.id.iv_door);
        lightIv = (ImageView) view.findViewById(R.id.iv_light);
        textView.setText("获取车辆状态,请稍后...");
        popupWindow = new PopupWindow(view,
                DimensHelper.dip2px(context,220) , RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundColor(context,1f);
                if(!notNow){
                    dismissCarCheckPopuwindowListener.onDismissCarCheckEvent();
                }

            }
        });
        setBackGroundColor(context,0.7f);
    }
    public void getStatusFailure(){
        checkedRlyt.setVisibility(View.GONE);
        textView.setText("暂时无法获取车况信息，请稍后重试");
    }
    public void setChecked(boolean accSuccess, boolean chargeSuccess, boolean doorSuccess, boolean lightSuccess){
        /**
         * status : success
         * data : {"id":null,"accSuccess":true,"chargeSuccess":true}
         */
        checkedRlyt.setVisibility(View.VISIBLE);
        textView.setText("");
        if(accSuccess){
            accIv.setImageResource(R.drawable.ic_check_blue);
        }else{
            accIv.setImageResource(R.drawable.ic_red_close);
        }
        if(chargeSuccess){
            chargingIv.setImageResource(R.drawable.ic_check_blue);
        }else{
            chargingIv.setImageResource(R.drawable.ic_red_close);
        }
        if(doorSuccess){
            doorIv.setImageResource(R.drawable.ic_check_blue);
        }else{
            doorIv.setImageResource(R.drawable.ic_red_close);
        }
        if(lightSuccess){
            lightIv.setImageResource(R.drawable.ic_check_blue);
        }else{
            lightIv.setImageResource(R.drawable.ic_red_close);
        }

    }

    public void dismissCarCheckPopuWindow(){

        if(popupWindow != null){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //五秒后执行
                    notNow = false;
                    popupWindow.dismiss();
                }
            }, 4000);
        }

    }
    public void nowDismissCarCheckPopuWindow(){
        if(popupWindow != null){
            notNow = true;
            popupWindow.dismiss();
        }


    }
    private DismissCarCheckPopuwindowListener dismissCarCheckPopuwindowListener;



    public void setDismissCarCheckPopuwindowListener(DismissCarCheckPopuwindowListener dismissCarCheckPopuwindowListener) {
        this.dismissCarCheckPopuwindowListener = dismissCarCheckPopuwindowListener;
    }

    public interface DismissCarCheckPopuwindowListener{
        void onDismissCarCheckEvent();
    }


    public void dismissControlCarPopuWindow(){

        if(popupWindow != null){

            new Handler().postDelayed(new Runnable() {
                @Override
                 public void run() {
                     //两秒后执行
                    popupWindow.dismiss();
                }
            }, 1000);
        }
    }

    /**
     * 显示抖索范围
     */
    public void showSearchRange(int location,final Activity mContext, final List<?> list, View mainView, final ImageView triangle_arrow, final OnChoiceCityListener listener){
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_choose_pay_way, null);
        ListView lv = (ListView) contentView.findViewById(R.id.list_choose_pay);
        lv.setAdapter(new SearchRangeAdapter(mContext,list));
        choiceCityOrSearchRangePop(location, mContext, list, mainView, triangle_arrow, listener, contentView, lv);
    }

    /**
     * 显示城市选择列表
     * @param mContext
     */
    public void showList(int location,final Activity mContext, final List<?> list, View mainView, final ImageView triangle_arrow, final OnChoiceCityListener listener) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_choose_pay_way, null);
        ListView lv = (ListView) contentView.findViewById(R.id.list_choose_pay);
        lv.setAdapter(new CityListAdapter(mContext,list));
        choiceCityOrSearchRangePop(location, mContext, list, mainView, triangle_arrow, listener, contentView, lv);
    }

    /**
     * 选择城或选择搜索范围pop
     * @param location
     * @param mContext
     * @param list
     * @param mainView
     * @param triangle_arrow
     * @param listener
     * @param contentView
     * @param lv
     */
    private void choiceCityOrSearchRangePop(int location, final Activity mContext, final List<?> list, View mainView, final ImageView triangle_arrow, final OnChoiceCityListener listener, View contentView, ListView lv) {
        final PopupWindow popupWindow = new PopupWindow(contentView,
                200, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(location == Gravity.LEFT){
                popupWindow.showAsDropDown(mainView,-20,2,Gravity.LEFT);
            }else if(location == Gravity.RIGHT){
                popupWindow.showAsDropDown(mainView,20,2,Gravity.RIGHT);
            }

        }
        startRoateUp(mContext,triangle_arrow);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                startRoateDown(mContext,triangle_arrow);
                listener.dismiss();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                listener.selectedCity(list.get(position),position);

            }
        });
    }

    /**
     * 执行向上旋转的动画
     */
    private void startRoateUp(Activity act, ImageView iv) {

        Animation upAnimation = AnimationUtils.loadAnimation(act, R.anim.rotate_up);
        upAnimation.setFillAfter(true);
        iv.startAnimation(upAnimation);
    }
    /**
     * 执行向下旋转的动画
     */
    private void startRoateDown(Activity act, ImageView iv) {

        Animation downAnimation = AnimationUtils.loadAnimation(act, R.anim.rotate_down);
        downAnimation.setFillAfter(true);
        iv.startAnimation(downAnimation);
    }

    /**
     * 显示活动界面
     */
    public void showActiveView(final Activity context, final View mainView, List<HomeActiveModel.DataBean> mActiveList, final OnHomeActiveListener onHomeActiveListener){
       View contentView = LayoutInflater.from(context).inflate(R.layout.layout_home_active, null);
        ImageView closeActive = (ImageView) contentView.findViewById(R.id.close_active);
        RollPagerView rollpager = (RollPagerView) contentView.findViewById(R.id.active_rollpager);
        configRollpager(context, rollpager,mActiveList);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(contentView,
                wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight(), true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);
         final Handler mHandler = new Handler();
        Runnable showPopWindowRunnable = new Runnable() {

            @Override
            public void run() {
                // 如何根元素的width和height大于0说明activity已经初始化完毕
                if( mainView != null && mainView.getWidth() > 0 && mainView.getHeight() > 0) {
                    // 显示popwindow
                    popupWindow.showAtLocation(mainView,Gravity.CENTER,0,0);
                    // 停止检测
                    mHandler.removeCallbacks(this);
                } else {
                    // 如果activity没有初始化完毕则等待5毫秒再次检测
                    mHandler.postDelayed(this, 5);
                }
            }
        };
        showPopWindowRunnable.run();

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                onHomeActiveListener.onCloseActiveEvent();
            }
        });
        closeActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });


    }

    private void configRollpager(final Activity context, RollPagerView rollpager, final List<HomeActiveModel.DataBean> mActiveList) {
        rollpager.setHintView(new ColorPointHintView(context, context.getResources().getColor(R.color.important_color_blue),context.getResources().getColor(R.color.login_hint_color)));
        rollpager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebViewActivityHelper.startWebViewActivity(context, mActiveList.get(position).getDetailUrl(), "");

            }
        });
        rollpager.setAdapter(new RollpagerAdapter(mActiveList));
    }
    private class RollpagerAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.bg_active,
                R.drawable.bg_active,
                R.drawable.bg_active,
                R.drawable.bg_active,
        };
        private  List<HomeActiveModel.DataBean> mActiveList;

        public RollpagerAdapter(List<HomeActiveModel.DataBean> activeList) {
            this.mActiveList = activeList;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            View view = View.inflate(container.getContext(),R.layout.layout_activi_pager,null);
            ImageView iv = (ImageView) view.findViewById(R.id.icon_active);
            ImageLoaderManager.getInstance().displayHomeActive(mActiveList.get(position).getPictureId(), iv);
            return view;
        }

        @Override
        public int getCount() {
            return mActiveList.size();
        }
    }

    public interface onCallChargingAndServiceListener{
        void onCallCharging();
        void onCallService();
        void dismiss();
    }

    /**
     * 充电站详情页面拨打场站电话和运营商电话
     */
    public void callChargingAndService(final Activity context, View mainView, final onCallChargingAndServiceListener listener){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_charging_service,null);
        ListView lv = (ListView) view.findViewById(R.id.lv);
        List<String> itemNameList = new ArrayList<>();
        itemNameList.add("充电站");
        itemNameList.add("运营商");
        CallChargingAndServiceAdapter adapter = new CallChargingAndServiceAdapter(context,itemNameList);
        lv.setAdapter(adapter);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(view, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(cn.lds.chatcore.R.drawable.pop_black_bg));
        popupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                listener.dismiss();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    popupWindow.dismiss();
                    listener.onCallCharging();
                }else if(position == 1){
                    popupWindow.dismiss();
                    listener.onCallService();
                }
            }
        });
    }

    /**
     * 地锁列表pop
     */
    public void showLandLockListPopuwindow( final Activity context, View mainView, List<LandLockListModel.DataBean> datas, final OnListItemClickListener listener){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_charging_service,null);
        TextView titleTv = (TextView) view.findViewById(R.id.title);
        titleTv.setText("选择地锁下落");
        ListView lv = (ListView) view.findViewById(R.id.lv);
        ArrayList<String> list = new ArrayList<>();
        for(LandLockListModel.DataBean data:datas){
            list.add(data.getParkingNum());
        }
        CallChargingAndServiceAdapter adapter = new CallChargingAndServiceAdapter(context,list);
        lv.setAdapter(adapter);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(view, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(cn.lds.chatcore.R.drawable.pop_black_bg));
        popupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                listener.clickEvent(position);
            }
        });
    }

    public interface OnListItemClickListener{
        void clickEvent(int position);
    }

    /**
     * 导航列表
     */
    public void navigationPop(final Activity context, View mainView, final OnListViewItemClickListener onListViewItemClickListener){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_charging_service,null);
        TextView title = (TextView) view.findViewById(R.id.title);
        View line = (View) view.findViewById(R.id.line);
        line.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        ListView lv = (ListView) view.findViewById(R.id.lv);
        List<String> itemNameList = new ArrayList<>();
        itemNameList.add("导航到场站");
        itemNameList.add("导航到车辆");
        CallChargingAndServiceAdapter adapter = new CallChargingAndServiceAdapter(context,itemNameList);
        lv.setAdapter(adapter);
        // 声明一个弹出框
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(view,
                1000, 600, true);
        //若不设置，键盘不弹出
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_black_bg));
        popupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                listener.dismiss();
                setBackGroundColor(context,1.0f);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewItemClickListener.onItemClickEvent(position);
                popupWindow.dismiss();

            }
        });
        setBackGroundColor(context,0.7f);
    }

    public OnListViewItemClickListener onListViewItemClickListener;

    public void setOnListViewItemClickListener(OnListViewItemClickListener onListViewItemClickListener) {
        this.onListViewItemClickListener = onListViewItemClickListener;
    }

    public interface OnListViewItemClickListener{
        void onItemClickEvent(int index);
    }



    public interface OnChoicePictureListener{
        void formCameraEvent();
        void formMediaEvent();
    }
    /**
     * 弹出获取照片的pop
     */
    public void getPicture(Activity context,View mainView,View topView,final OnChoicePictureListener listener) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_head, null);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(mainView, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(topView);

        LinearLayout bg = (LinearLayout) contentView.findViewById(R.id.pop_bg);
        TextView cancel = (TextView) contentView.findViewById(R.id.pop_cancel);
        TextView formCamera = (TextView) contentView.findViewById(R.id.pop_camera);
        TextView formMedia = (TextView) contentView.findViewById(R.id.pop_meida);
        LinearLayout line = (LinearLayout) contentView.findViewById(R.id.ll_line);
        line.setVisibility(View.GONE);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        formCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                // 判断存储卡是否可以用，可用进行存储
//                if (hasSdcard()) {
//                    imageFilePath = FileHelper.getTakeAvatarPath();
//                    Intent caremaIntent = GraphicHelper.getPhotoIntent(imageFilePath);
//                    startActivityForResult(caremaIntent, PHOTO_REQUEST_CAMERA);
//                }
                listener.formCameraEvent();
                popupWindow.dismiss();
            }
        });
        formMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // 激活系统图库，选择一张图片
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                listener.formMediaEvent();
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


}
