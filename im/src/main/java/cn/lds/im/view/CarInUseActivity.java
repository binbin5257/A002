package cn.lds.im.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.inuker.bluetooth.library.BluetoothClient;

import com.bluebox.BlueBoxControllerProvider;
import com.bluebox.DefaultConfig;
import com.bluebox.DefaultConnectRule;
import com.bluebox.DeviceNameConnectRule;
import com.bluebox.IBlueBoxContoller;
import com.ddtc.tobsdk.DdtcToBScanOperModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.HttpHelper;
import cn.lds.chatcore.common.bluetooth.BleConnectionCallback;
import cn.lds.chatcore.common.bluetooth.BleScanResultCallback;
import cn.lds.chatcore.common.bluetooth.BluetoothHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.PopWindowListener2;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.RevertCarModel;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.event.ConfirmReceiveEvent;
import cn.lds.chatcore.event.CustomerServiceReturnCarEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.PickUpAcceptEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.DdtcHelper;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.common.ModuleUrls;
import cn.lds.im.data.FaIlureModel;
import cn.lds.im.data.GetReturnCarLocationInfo;
import cn.lds.im.data.LandLockListModel;
import cn.lds.im.data.OrderInfoModel;
import cn.lds.im.data.PickUpModel;
import cn.lds.im.data.ReservationOrdersLocationsModel;
import cn.lds.im.data.TokenModel;
import cn.lds.im.view.widget.CustomPopuwindow;
import cn.lds.im.view.widget.ProgressLayout;
import de.greenrobot.event.EventBus;

/**
 * 车辆使用中页面
 */
public class CarInUseActivity extends BaseActivity implements View.OnClickListener {

    private final int REQUEST_ENABLE_BT = 1000;
    private final int REQUEST_ENABLE_BT_DROP_CLOSE = 2000;
    /**
     * 外勤view
     */
    private TextView workerTv;
    /**
     * 还车区域
     */
    private RelativeLayout returnCarRlyt;
    /**
     * 上门取车信息区域
     */
    private LinearLayout returnCarInfoLlyt;
    /**
     * 选择取车方式
     */
    private ImageView switchGetCar;

    /**
     * 还车方式
     */
    private int GET_CAR_WAY = 1;

    /**
     * 自己还车
     */
    private int OWNER_RETURN_CAR = 1;
    /**
     * 上门取车
     */
    private int COME_GET_CAR = 2;
    /**
     * 还车按钮
     */
    private Button returnCarBtn;
    /**
     * 上门取车text
     */
    private TextView comeDoorGetCarTv;
    /**
     * 预约上门取车成功
     */
    private TextView bookGetCarSucTv;

    /**
     * 是否预约取车成功
     */
    private boolean isBookGetCarSuc = false;
    /**
     * 预约提示文字
     */
    private TextView waringTv;
    /**
     * 订单对象
     */
    protected OrderInfoModel orderInfoModel;
    /**
     * 常用联系方式
     */
    private EditText contactWay;
    /**
     * 预约上门取车还车地址
     */
    private TextView retrunAddressTv;
    /**
     * 预约上门取车时间
     */
    private TextView retrunTimeTv;
    /**
     * 还车地点位置信息
     */
    private GetReturnCarLocationInfo info; //还车地点位置信息
    /**
     * 当前消费金额
     */
    private TextView currentAmountTv;
    /**
     * 续航控件
     */
    private ProgressLayout progressLayout;
    /**
     * 车俩型号
     */
    private TextView carModel;
    /**
     * 续航
     */
    private TextView carLifeTv;
    /**
     * 车牌号
     */
    private TextView carNo;
    /**
     * 还车场站名称
     */
    private TextView retrunYardName;
    /**
     * 还车场站地址
     */
    private TextView retrunYardAddressName;
    /**
     * 行程
     */
    private TextView odometerTv;
    /**
     * 使用时间
     */
    private TextView useTimeTv;
    /**
     * 修改还车场站
     */
    private TextView prompt_return_location;

    private ImageView iconCarIv;
    private Runnable runnable;
    private Handler handler;
    private LinearLayout mainView;
    private CustomPopuwindow popuwindow;
    private TextView pay_get_car;
    private DecimalFormat df;
    private long taketime = 0;
    /**
     * 自还车布局
     */
    private RelativeLayout retrunCarOwner;
    private TextView yardOwnerTv;
    private TextView addressOwnerTv;
    private String sendtaketime;
    private AccountsTable mAccountsTable;
    private String contactStr;
    public static CarInUseActivity instance;

    private int NET_COTROLLER_CAR = 0;
    private int BLUETOOTH_CONTROLLER_CAR = 1;
    private int CONTROLLER_CAR_TYPE = NET_COTROLLER_CAR;
    private BluetoothHelper mBluetoothHelper;
    private String blueToothName;
    private IBlueBoxContoller cto;
    private String closeDoorCommand;
    private String openDoorCommand;
    private String findCarCommand;
    private ImageView bluettoothIv;
    private String macAddress;
    private String appId = "ddtc";
    private String appSecret = "86568edc1e56e1fe6655b81272cb3558";
    private String mDropCloseAddress = "86568edc1e56e1fe6655b81272cb3558";
    private String mLockId = "86568edc1e56e1fe6655b81272cb3558";
    private String mDdtcAccessToken;
    private DdtcToBScanOperModel mDdtcToBScanOperModel;
    private DdtcToBScanOperModel.OperType mCurrentType = DdtcToBScanOperModel.OperType.drop;
    private List<LandLockListModel.DataBean> mLandLockList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_using);
        instance = this;
        EventBus.getDefault().register(this);
        initView();
        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
        initData();
        startHandler();
        initDdtcBle();

    }

    /**
     * 初始化丁丁停车sdk
     */
    private void initDdtcBle() {
        mDdtcToBScanOperModel = new DdtcToBScanOperModel(new WeakReference<Activity>(this));
        if(mDdtcToBScanOperModel == null){
            Toast.makeText(mContext, "丁丁停车sdk初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }

        mDdtcToBScanOperModel.setDdtcBleScanListener(new DdtcToBScanOperModel.DdtcBleScanListener() {
            @Override
            public void onSpecDeviceFind( final String address,int rssi ) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "设备已经连接........", Toast.LENGTH_SHORT).show();

                        if(!TextUtils.isEmpty(mDdtcAccessToken)){
                            //
                            mDdtcToBScanOperModel.oper(mDdtcAccessToken, address, mCurrentType);
                            if (popuwindow != null) {
                                popuwindow.showControllerCar(CarInUseActivity.this, 4, mainView);
                            }
                        }else{
                            Toast.makeText(mContext, "未获取到请求token", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

        });

        mDdtcToBScanOperModel.setDdtcBleOperModelListener(new DdtcToBScanOperModel.DdtcBleOperModelListener() {
            @Override
            public void onOperFailed( final String reason, final String rssi) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert("操作失败---reason=" + reason);
//                        ToolsHelper.showStatus(mContext, true, "操作失败++======" + reason);

                        if (popuwindow != null) {
                            popuwindow.dismissControlCarPopuWindow();
                        }
                    }
                });

            }

            @Override
            public void onOperSuccess(String battery, String type) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (popuwindow != null) {
                            popuwindow.dismissControlCarPopuWindow();
                        }
                        ToolsHelper.showStatus(mContext, true, "操作成功");
                    }
                });

            }

        });


    }

    private void startHandler() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000 * 30);
                initData();
            }
        };
        handler.postDelayed(runnable, 1000 * 30);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
        if(!TextUtils.isEmpty(macAddress)){
            mBluetoothHelper.disconnect(macAddress);
        }
        if(cto != null){

            cto.onDestory();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                if (requestCode == 1) {
                    Bundle b = data.getExtras(); //data为B中回传的Intent
                    //str即为回传的值
                    info = (GetReturnCarLocationInfo) b.getSerializable("CAR_LOCATION_INFO");
                    if (info != null) {
                        retrunAddressTv.setText(info.getSpecificLocation());
                    }

                    if (info.getLatitude() > 0 && info.getLongitude() > 0) {
                        if (orderInfoModel.getData().isDeliverCompleted()) {
                            ModuleHttpApi.getPickupCost(orderInfoModel.getData().getReservationLocationLongitude(),orderInfoModel.getData().getReservationLocationLatitude(), info.getLongitude(), info.getLatitude());
                        } else {
                            ModuleHttpApi.getPickupCost(orderInfoModel.getData().getReservationLocationLongitude(),orderInfoModel.getData().getReservationLocationLatitude(), info.getLongitude(), info.getLatitude());

                        }

                    }
                }else if(requestCode == REQUEST_ENABLE_BT){
                    scanAndConnectBle(macAddress);
                }else if(requestCode == REQUEST_ENABLE_BT_DROP_CLOSE){
                    mDdtcToBScanOperModel.startScanDevice(mDropCloseAddress,mLockId);
                }
                else{
                    ReservationOrdersLocationsModel.DataBean selectPark = (ReservationOrdersLocationsModel.DataBean) data.getSerializableExtra("selectPark");
                    retrunYardName.setText(selectPark.getName());
                    retrunYardAddressName.setText(selectPark.getAddress());
                    ModuleHttpApi.changeReturnLocation(selectPark.getId(),orderInfoModel.getData().getId());
                }

                break;
            case 0:
                if(requestCode == REQUEST_ENABLE_BT){
                    ToolsHelper.showStatus(this,false,"你拒绝开启蓝牙，无法通过蓝牙通道控车");
                }
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drop_closed:

                //请求服务器获取地锁列表
                getLandLockList();



                //1.判断设备是否支持蓝牙ble
                //2.检查蓝牙是否打开，如果没有打开提示打开蓝牙
                if(mBluetoothHelper.adapterEnabled(this)){
                    mDdtcToBScanOperModel.startScanDevice(mDropCloseAddress,mLockId);
                }else{
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT_DROP_CLOSE);
                }
                break;
            case R.id.tv_navgation:
                EnterNavigation();
                break;
            case R.id.tv_return_car:
                String pickupFfsPhone = orderInfoModel.getData().getPickupFfsPhone();
                if (!TextUtils.isEmpty(pickupFfsPhone)) {
                    PopWindowHelper.getInstance().openTel(this, orderInfoModel.getData().getPickupFfsPhone()).show(findViewById(R.id.top__iv));
                } else {
                    ToolsHelper.showStatus(mContext, true, "外勤还未接单");
                }
                break;
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.iv_switch:
                choiceGetCarWay();
                break;
            case R.id.top_menu_btn_del:
                callWork();
                break;
            case R.id.btn_return_car:
                retrunCarEvent();
                break;
            case R.id.rl_open_door: // 开锁
                unlock();

                break;
            case R.id.rl_close_door: // 关锁
                lock();

                break;
            case R.id.rl_whistle: //鸣笛
                whistle();
                break;
//            case R.id.rl_lamp: //闪灯寻车
//                if(CONTROLLER_CAR_TYPE == BLUETOOTH_CONTROLLER_CAR){
//                    cto.writeCmd(findCarCommand);
//                }else{
//                    lamp();
//                }
//
//                break;
            case R.id.rl_return_address: //上门取车
                EnterGetCarLocatedActivity();
                break;
            case R.id.rl_return_time: //还车时间
                choiceReturnCarTime();
                break;
            case R.id.rl_bluetooth: //蓝牙开启按钮
                switchBluetoothToggle();
                break;
            case R.id.prompt_return_location:
                Intent intent = new Intent();
                intent.setClass(mContext, LocatedActivity.class);
                intent.setAction(String.valueOf(orderInfoModel.getData().getReturnLocationId()));
                startActivityForResult(intent, 1111);
                break;
        }

    }

    /**
     * 获取地锁列表
     */
    private void getLandLockList() {
        String url = ModuleUrls.getLandLockList().replace("{id}",String.valueOf(orderInfoModel.getData().getId()));
        HttpHelper.get(url,ModuleHttpApiKey.getLandLockList);
    }

    /**
     *  切换蓝牙状态
     *
     *
     * 控车方式：
     * （1）通过手机蓝牙向车辆发送控车指令，实现控车
     *    1.判断设备是否支持蓝牙
     *    2.检查蓝牙是否打开，如果没有打开提示打开蓝牙
     *    3.扫描周围蓝牙设备，根据订单中蓝牙名称过滤汽车TBOX设备
     *    4.连接TBOX设备
     *    5.改变控车方式，Ui蓝牙图片切换，文字显示蓝牙通道已经打开
     * （2）通过请求服务器接口，后台发送指令进行控车
     */
    private void switchBluetoothToggle() {
        if(CONTROLLER_CAR_TYPE == NET_COTROLLER_CAR){
            if(orderInfoModel!=null && orderInfoModel.getData()!=null && orderInfoModel.getData().getBlueToothMDTO()!=null){
                blueToothName = orderInfoModel.getData().getBlueToothMDTO().getName();
                closeDoorCommand = orderInfoModel.getData().getBlueToothMDTO().getCloseDoorCommand();
                openDoorCommand = orderInfoModel.getData().getBlueToothMDTO().getOpenDoorCommand();
                findCarCommand = orderInfoModel.getData().getBlueToothMDTO().getFindCarCommand();
                macAddress = orderInfoModel.getData().getBlueToothMDTO().getMacAddress();
                if(TextUtils.isEmpty(macAddress)){
                    ToolsHelper.showStatus(this,false,"车辆没有连接TBOX");
                    return;
                }
                macAddress = macAddress.replace("-",":");
                //1.判断设备是否支持蓝牙ble
                //2.检查蓝牙是否打开，如果没有打开提示打开蓝牙
                if(mBluetoothHelper.adapterEnabled(this)){
                    scanAndConnectBle(macAddress);
                }else{
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }else{
                ToolsHelper.showStatus(mContext,false,"车辆没有绑定蓝牙设备");
            }


        }else{
            //切换为网络控车
            CONTROLLER_CAR_TYPE = NET_COTROLLER_CAR;
            bluettoothIv.setImageResource(R.drawable.bg_bluetooth_close);
            if(!TextUtils.isEmpty(macAddress)){
                mBluetoothHelper.disconnect(macAddress);
                ToolsHelper.showStatus(mContext,false,"断开连接");
            }

            if(cto != null){
                cto.onDestory();
                cto.stopScan();
                cto.disConnect();
            }
        }


    }

    private void scanAndConnectBle(final String address) {
//        ToolsHelper.showStatus(mContext,true,"正在连接蓝牙名称：" + blueToothName +"地址：" + address);
        cto = BlueBoxControllerProvider.getInstance(address, 0,
                new DefaultConnectRule(), new DefaultConfig(), mContext,
                new IBlueBoxContoller.BlueBoxStateListener<String>() {
                    @Override
                    public void onConnectFailure() {
                        if (popuwindow != null) {
                            popuwindow.dismissControlCarPopuWindow();
                        }
                        bluettoothIv.setImageResource(R.drawable.bg_bluetooth_close);
                        Toast.makeText(mContext, blueToothName + "连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectSuccess() {
                        //切换为蓝牙控车
                        CONTROLLER_CAR_TYPE = BLUETOOTH_CONTROLLER_CAR;
                        bluettoothIv.setImageResource(R.drawable.bg_bluetooth_open);
                        Toast.makeText(mContext, blueToothName + "连接成功", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onReadResult(String data) {
                        if (popuwindow != null) {
                            popuwindow.dismissControlCarPopuWindow();
                        }
//                        alert("盒子返回了结果" + data);
                    }

                    @Override
                    public void onDisConnected() {

                        Toast.makeText(mContext, "连接断开", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    /**
     * 账号变更
     */
    private void alert(String content) {
        PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
            @Override
            public void confirm() {
            }

            @Override
            public void cancel() {
            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(content).show(findViewById(R.id.top__iv),mainView);
    }

    /**
     * 获取正在进行订单数据
     */
    private void initData() {
        mAccountsTable = AccountManager.getInstance().findByNo();
        contactWay.setHint(mAccountsTable.getMobile());
        mBluetoothHelper = new BluetoothHelper();
        mBluetoothHelper.init(this);
        ModuleHttpApi.getExistOrder();
//        DdtcHelper.getInstance().getAccessToken(appId,appSecret);
    }

    /**
     * 进入上门取车界面
     */
    private void EnterGetCarLocatedActivity() {
        Intent intent = new Intent(this, GetCarLocatedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USER_WAY", "RETURN_CAR");
        if (info != null) {
            bundle.putSerializable("CAR_LOCATION_INFO", info);
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    /**
     * 选择还车时间
     */
    private void choiceReturnCarTime() {
        if(taketime == 0){
            long t = System.currentTimeMillis() + 1000 * 60 * 10;
            sendtaketime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, t);
        }else{
            sendtaketime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, taketime);
        }

        PopWindowHelper.getInstance().showOptions(this,"选择上门取车时间",new PopWindowListener2() {
            @Override
            public void confirm(String text) {
                retrunTimeTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));

            }

            @Override
            public void cancel() {

            }
        });

    }


    /**
     * 闪灯寻车
     */
    private void lamp() {
        if (popuwindow != null) {
            popuwindow.showControllerCar(this, 3, mainView);
        }
        if (orderInfoModel != null && orderInfoModel.getData() != null) {
            if(CONTROLLER_CAR_TYPE == BLUETOOTH_CONTROLLER_CAR){
                cto.writeCmd(findCarCommand);
            }else{
                ModuleHttpApi.reservationOrdersFlash(String.valueOf(orderInfoModel.getData().getId()));
            }
        }
    }

    /**
     * 鸣笛
     */
    private void whistle() {
        if (popuwindow != null) {
            popuwindow.showControllerCar(this, 2, mainView);
        }
        if (orderInfoModel != null && orderInfoModel.getData() != null) {
            if(CONTROLLER_CAR_TYPE == BLUETOOTH_CONTROLLER_CAR){
                cto.writeCmd(findCarCommand);
            }else{
                ModuleHttpApi.reservationOrdersBlast(String.valueOf(orderInfoModel.getData().getId()));
            }
        }
    }

    /**
     * 关锁
     */
    private void lock() {
        if (popuwindow != null) {
            popuwindow.showControllerCar(this, 1, mainView);
        }
        if (orderInfoModel != null && orderInfoModel.getData() != null) {
            if(CONTROLLER_CAR_TYPE == BLUETOOTH_CONTROLLER_CAR){
                cto.writeCmd(closeDoorCommand);
            }else{
                ModuleHttpApi.reservationOrdersLock(String.valueOf(orderInfoModel.getData().getId()));
            }
        }
    }

    /**
     * 开锁
     */
    private void unlock() {
        if (popuwindow != null) {
            popuwindow.showControllerCar(this, 0, mainView);
        }
        if (orderInfoModel != null && orderInfoModel.getData() != null) {
            if(CONTROLLER_CAR_TYPE == BLUETOOTH_CONTROLLER_CAR){
                cto.writeCmd(openDoorCommand);
            }else{
                ModuleHttpApi.reservationOrdersOpen(String.valueOf(orderInfoModel.getData().getId()));
            }

        }
    }

    public void onEventMainThread(HttpRequestEvent event) {

        HttpResult httpResult = event.httpResult;
        LoadingDialog.dismissDialog();
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo)
                || ModuleHttpApiKey.getPickupCost.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersReturn.equals(apiNo)
                || ModuleHttpApiKey.pickUpCar.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersFlash.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersBlast.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersLock.equals(apiNo)
                || ModuleHttpApiKey.changeReturnLocation.equals(apiNo)
                || ModuleHttpApiKey.getAccessToken.equals(apiNo)
                || ModuleHttpApiKey.getLandLockList.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersOpen.equals(apiNo))) {
            return;
        }

        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.reservationOrdersFlash:
                case ModuleHttpApiKey.reservationOrdersBlast:
                case ModuleHttpApiKey.reservationOrdersLock:
                case ModuleHttpApiKey.reservationOrdersOpen:
                    if (popuwindow != null) {
                        popuwindow.dismissControlCarPopuWindow();
                    }
//
                    break;
                case ModuleHttpApiKey.reservationOrdersReturn:
//                    ToolsHelper.showStatus(mContext, true, "成功还车");
//                    Intent intent = new Intent(CarInUseActivity.this, OrderDetailActivity.class);
//                    startActivity(intent);
//                    finish();
                    break;
                case ModuleHttpApiKey.getExistOrder:
                    orderInfoModel = GsonImplHelp.get().toObject(result, OrderInfoModel.class);
                    if (orderInfoModel != null && orderInfoModel.getData() != null) {
                        refrushView();
                    }
                    break;
                case ModuleHttpApiKey.pickUpCar:
                    isBookGetCarSuc = true;
                    returnCarBtn.setText("确认外勤到达进行还车");
                    comeDoorGetCarTv.setVisibility(View.INVISIBLE);
                    bookGetCarSucTv.setVisibility(View.VISIBLE);
                    switchGetCar.setVisibility(View.GONE);
                    workerTv.setVisibility(View.VISIBLE);
                    prompt_return_location.setVisibility(View.GONE);
                    returnCarRlyt.setVisibility(View.VISIBLE);
                    returnCarInfoLlyt.setVisibility(View.GONE);
                    waringTv.setVisibility(View.GONE);
                    pay_get_car.setVisibility(View.GONE);
                    ModuleHttpApi.getExistOrder();
                    break;
                case ModuleHttpApiKey.getPickupCost:
                    try {
                        pay_get_car.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject(result);
                        String money = jsonObject.optString("data");
                        String format = df.format(Double.parseDouble(money) / 100);
                        if (!ToolsHelper.isNull(format)) {
                            getCarMoney(format);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ModuleHttpApiKey.changeReturnLocation:
                    ToolsHelper.showStatus(this,true,"成功修改还车场站");
                    break;
                case ModuleHttpApiKey.getLandLockList:
                    LandLockListModel landLockListModel = GsonImplHelp.get().toObject(result, LandLockListModel.class);
                    if(landLockListModel != null && landLockListModel.getData().size() > 0){
                        final List<LandLockListModel.DataBean> datas = landLockListModel.getData();
                        mDdtcAccessToken = datas.get(0).getAccessToken();
                        CustomPopuwindow.getInstance().showLandLockListPopuwindow(this, mainView, datas, new CustomPopuwindow.OnListItemClickListener() {
                            @Override
                            public void clickEvent( int position ) {
                                LandLockListModel.DataBean dataBean = datas.get(position);
                                mDdtcToBScanOperModel.startScanDevice(dataBean.getBluetoothAddress(),dataBean.getDeviceId());
                            }
                        });
                    }else{
                        ToolsHelper.showStatus(this,true,"场站没有找到地锁");

                    }

                    break;
                case ModuleHttpApiKey.getAccessToken:
                    TokenModel tokenModel = GsonImplHelp.get().toObject(result, TokenModel.class);
                    if(tokenModel != null && !TextUtils.isEmpty(tokenModel.getAccessToken())){
                        mDdtcAccessToken = tokenModel.getAccessToken();
                    }
                    break;
            }
        }
    }


    private void getCarMoney(String money) {
        String content = String.format(getString(R.string.order_get_car_money), money);
        int start = content.indexOf(money);
        int end = start + money.length();
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.normal_text_color_red)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pay_get_car.setText(style);
    }
    /**
     * 外勤确认接车
     *
     * @param event
     */
    public void onEventMainThread(CustomerServiceReturnCarEvent event) {
        String objectNo = event.getMessage().getObjectNo();
        if(objectNo.equals("1")){
            Intent intent = new Intent(this,TripInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("objectNo",objectNo);
            bundle.putInt("id",orderInfoModel.getData().getId());
            bundle.putString("event",event.getMessage().getContent());
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,OrderDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("objectNo",objectNo);
            bundle.putString("event",event.getMessage().getContent());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        finish();

    }
    /**
     * 外勤确认接车
     *
     * @param event
     */
    public void onEventMainThread(ConfirmReceiveEvent event) {
        initData();
        showConfirmDialog(event.getMessage().getContent());
    }

    /**
     * 外勤接受取车工单
     *
     * @param event
     */
    public void onEventMainThread(PickUpAcceptEvent event) {
        initData();
        Toast.makeText(this,event.getMessage().getContent(),Toast.LENGTH_SHORT).show();
//        showConfirmDialog(event.getMessage().getContent());
    }

    private void showConfirmDialog(String content) {
        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
            @Override
            public void confirm() {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(content).setConfirmTx("确认").show(findViewById(R.id.top__iv));
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String result = httpResult.getResult();
        FaIlureModel model = GsonImplHelp.get().toObject(result, FaIlureModel.class);
        if (model != null
                && model.getStatus().equals("failure")
                && model.getErrors() != null
                && model.getErrors().size() > 0) {

        }
        LoadingDialog.dismissDialog();
        String apiNo = httpResult.getApiNo();
        switch (apiNo) {
            case ModuleHttpApiKey.reservationOrdersFlash:
                ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
                break;
            case ModuleHttpApiKey.reservationOrdersBlast:
                ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
                break;
            case ModuleHttpApiKey.reservationOrdersLock:
                ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
                break;
            case ModuleHttpApiKey.reservationOrdersOpen:
                ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
                break;
            case ModuleHttpApiKey.getExistOrder:
                ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
                break;
            case ModuleHttpApiKey.reservationOrdersReturn:
//                ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
                break;
            case ModuleHttpApiKey.pickUpCar:
                ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
                break;
        }
    }

    /**
     * 刷新界面
     */

    private void refrushView() {
        df = new DecimalFormat("#,##0.00");
        OrderInfoModel.DataBean dataBean = orderInfoModel.getData();
        ImageLoaderManager.getInstance().displayImageForCar(mContext, dataBean.getVehiclePicId(), iconCarIv);
        if (dataBean.getAmount() == 0.0) {
            currentAmountTv.setText("当前消费：0.00 元");
        } else {
            currentAmountTv.setText("当前消费：" + df.format(dataBean.getAmount() / 100) + " 元");
        }
        progressLayout.initParams(0, dataBean.getMaxSustainedMileage(), dataBean.getSustainedMileage());
        carModel.setText(dataBean.getVehicleBrandName() + dataBean.getVehicleSeriesName());
        carLifeTv.setText("续航" + dataBean.getSustainedMileage() + "km");
        carNo.setText(dataBean.getPlateLicenseNo());
        odometerTv.setText("已行驶：" + dataBean.getOdometer() + "km");
        retrunYardName.setText(dataBean.getReturnLocationName());
        retrunYardAddressName.setText(dataBean.getReturnLocationAddress());
        //如果预约用车，选择上门取车业务，将预约还车时间带入，默认为上门取车时间，可修改
        if (orderInfoModel.getData().getType().equals("SCHEDULED")) {
            taketime = orderInfoModel.getData().getScheduledDroppedOffTime();
            retrunTimeTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, taketime));

        }
        useTimeTv.setText("使用时长：" + dataBean.getTime());
//        if (dataBean.getTime() < 60) {
//            useTimeTv.setText("使用时长：" + dataBean.getTime() + "分钟");
//        } else {
//            useTimeTv.setText("使用时长：" + dataBean.getTime() / 60 + "小时" + dataBean.getTime() % 60 + "分钟");
//        }
        if (orderInfoModel.getData().isPickuped()) {
            GET_CAR_WAY = COME_GET_CAR;
            prompt_return_location.setVisibility(View.GONE);
            isBookGetCarSuc = true;
            retrunCarOwner.setVisibility(View.VISIBLE);
            yardOwnerTv.setText(dataBean.getPickupName());
            addressOwnerTv.setText(dataBean.getPickupAddress());
            returnCarBtn.setText("确认外勤到达进行还车");
            comeDoorGetCarTv.setVisibility(View.INVISIBLE);
            bookGetCarSucTv.setVisibility(View.VISIBLE);
            switchGetCar.setVisibility(View.GONE);
            workerTv.setVisibility(View.VISIBLE);
//            workerTv.setVisibility(View.GONE);
            returnCarRlyt.setVisibility(View.VISIBLE);
            returnCarInfoLlyt.setVisibility(View.GONE);
            waringTv.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化界面view
     */
    private void initView() {
        bluettoothIv = (ImageView) findViewById(R.id.iv_bluetooth);
        Button backBtn = (Button) findViewById(R.id.top_back_btn);
        TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
        workerTv = (TextView) findViewById(R.id.tv_return_car);
        comeDoorGetCarTv = (TextView) findViewById(R.id.tv_come_door_car);
        bookGetCarSucTv = (TextView) findViewById(R.id.tv_come_door_car_suc);
        switchGetCar = (ImageView) findViewById(R.id.iv_switch);
        iconCarIv = (ImageView) findViewById(R.id.iv_car);
        ImageView topMenuRight = (ImageView) findViewById(R.id.top_menu_btn_del);
        returnCarRlyt = (RelativeLayout) findViewById(R.id.rl_retrun_car);
        returnCarInfoLlyt = (LinearLayout) findViewById(R.id.ll_return_info);
        returnCarBtn = (Button) findViewById(R.id.btn_return_car);
        waringTv = (TextView) findViewById(R.id.tv_waring);
        TextView navigationTv = (TextView) findViewById(R.id.tv_navgation);
        mainView = (LinearLayout) findViewById(R.id.main_view);

        retrunCarOwner = (RelativeLayout) findViewById(R.id.rl_retrun_car_owner);
        yardOwnerTv = (TextView) findViewById(R.id.tv_yard_owner);
        addressOwnerTv = (TextView) findViewById(R.id.tv_address_owner);

        odometerTv = (TextView) findViewById(R.id.tv_distance);
        useTimeTv = (TextView) findViewById(R.id.use_time);
        carLifeTv = (TextView) findViewById(R.id.tv_life);
        carNo = (TextView) findViewById(R.id.car_no);
        carModel = (TextView) findViewById(R.id.car_model);
        currentAmountTv = (TextView) findViewById(R.id.current_amount);
        progressLayout = (ProgressLayout) findViewById(R.id.progresslayout);
        retrunYardName = (TextView) findViewById(R.id.tv_yard);
        retrunYardAddressName = (TextView) findViewById(R.id.tv_address);
        prompt_return_location = (TextView) findViewById(R.id.prompt_return_location);
        prompt_return_location.setOnClickListener(this);

        /*以下是车辆控制区域view,开锁，关锁，鸣笛，指示灯*/
        RelativeLayout openDoorRlyt = (RelativeLayout) findViewById(R.id.rl_open_door);
        RelativeLayout closeDoorRlyt = (RelativeLayout) findViewById(R.id.rl_close_door);
        RelativeLayout whistleRlyt = (RelativeLayout) findViewById(R.id.rl_whistle);
        RelativeLayout lampRlyt = (RelativeLayout) findViewById(R.id.rl_lamp);
        RelativeLayout bluetoothRlyt = (RelativeLayout) findViewById(R.id.rl_bluetooth);
        RelativeLayout dropCloseRlyt = (RelativeLayout) findViewById(R.id.drop_closed);
        openDoorRlyt.setOnClickListener(this);
        closeDoorRlyt.setOnClickListener(this);
        whistleRlyt.setOnClickListener(this);
        lampRlyt.setOnClickListener(this);
        bluetoothRlyt.setOnClickListener(this);
        dropCloseRlyt.setOnClickListener(this);
        /*以上是车辆控制区域view*/

        /*以下是上门取车相关view初始化*/
        RelativeLayout returnCarAddressRlyt = (RelativeLayout) findViewById(R.id.rl_return_address);
        RelativeLayout returnCarTimeRlyt = (RelativeLayout) findViewById(R.id.rl_return_time);
        contactWay = (EditText) findViewById(R.id.et_phone);
        retrunAddressTv = (TextView) findViewById(R.id.tv_return_car_address);
        retrunTimeTv = (TextView) findViewById(R.id.tv_return_car_time);
        pay_get_car = (TextView) findViewById(R.id.pay_get_car);
        returnCarAddressRlyt.setOnClickListener(this);
        returnCarTimeRlyt.setOnClickListener(this);

        /*以上是上门取车相关view初始化*/
        popuwindow = new CustomPopuwindow();

        titleTv.setText("车辆使用中");
        returnCarBtn.setText("还车拍照");
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        workerTv.setOnClickListener(this);
        switchGetCar.setOnClickListener(this);
        returnCarBtn.setOnClickListener(this);
        topMenuRight.setImageResource(R.drawable.ic_phone);
        topMenuRight.setOnClickListener(this);
        navigationTv.setOnClickListener(this);
    }

    /**
     * 还车
     */
    private void retrunCarEvent() {
        if (isBookGetCarSuc) { //预约用车
            if (orderInfoModel != null && orderInfoModel.getData() != null) {
                if (orderInfoModel.getData().isPickupFfsReceived()) {
                    showReturnCarPrompt();
                } else {
                    showConfirmDialog("请等待外勤人员上门取车");
                }

            }

            return;
        }
        if (GET_CAR_WAY == COME_GET_CAR) {
            contactStr = contactWay.getText().toString().trim();
            String addressStr = retrunAddressTv.getText().toString().trim();
            final String returnCarTimeStr = retrunTimeTv.getText().toString().trim();

            if (TextUtils.isEmpty(addressStr) || addressStr.equals("还车地点")) {
                ToolsHelper.showStatus(mContext, false, "请选择上门取车地点");
                return;

            }
            if (TextUtils.isEmpty(returnCarTimeStr) || returnCarTimeStr.equals("还车时间")) {
                ToolsHelper.showStatus(mContext, false, "请选择上门取车时间");
                return;
            }
            String phoneHint = contactWay.getHint().toString();
            if(TextUtils.isEmpty(contactStr) && !TextUtils.isEmpty(phoneHint) && phoneHint.equals(mAccountsTable.getMobile())){
                contactStr = phoneHint;
            }
            if (TextUtils.isEmpty(contactStr)) {
                ToolsHelper.showStatus(mContext, false, "手机号不能为空");
                return;
            }
            String telRegex = "[1][358]\\d{9}";
            if (!contactStr.matches(telRegex)) {
                ToolsHelper.showStatus(mContext, false, "请输入正确的手机号");
                return;
            }
            PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
                @Override
                public void confirm() {

                    PickUpModel model = new PickUpModel();
                    model.setId(orderInfoModel.getData().getId());
                    model.setPickuped(true);
                    model.setAddress(info.getAdministrativePosition());
                    model.setEstimatedTime(TimeHelper.getTime(TimeHelper.FORMAT4, returnCarTimeStr));
                    model.setLatitude(info.getLatitude());
                    model.setLongitude(info.getLongitude());
                    model.setName(info.getSpecificLocation());
                    model.setPhone(contactStr);
                    model.setReturnLocationId(orderInfoModel.getData().getReturnLocationId());
                    ModuleHttpApi.pickUpCar(model);
                }

                @Override
                public void cancel() {

                }

                @Override
                public void onItemListener(int position) {

                }
            }).setContentTx("确认预约上门取车服务？").show(findViewById(R.id.top__iv));


        } else {
//            if (orderInfoModel != null && orderInfoModel.getData() != null) {
//                showReturnCarPrompt();
//            }
            //TODO 进入车况上报界面
            PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
                @Override
                public void confirm() {

                    Intent intent = new Intent(mContext,CarReportConditionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("FLAG","RETURN_CAR");
                    bundle.putSerializable("RETURNMODEL",orderInfoModel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                public void cancel() {

                }

                @Override
                public void onItemListener(int position) {

                }
            }).setContentTx("请确认车灯与车门已经关闭，车辆已经断电，并且已经插上充电枪").show(findViewById(R.id.top__iv));


        }
    }

    /**
     * 显示还车提示
     */
    private void showReturnCarPrompt() {
        PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
            @Override
            public void confirm() {

//                LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//                ModuleHttpApi.reservationOrdersReturn(orderInfoModel.getData().getId(), orderInfoModel.getData().getReturnLocationId());
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx("还车后您将无法继续使用此车辆，确认还车吗？").show(findViewById(R.id.top__iv));
    }

    /**
     * 导航
     */
    private void EnterNavigation() {
        CustomPopuwindow.getInstance().navigationPop(this, mainView, new CustomPopuwindow.OnListViewItemClickListener() {
            @Override
            public void onItemClickEvent(int index) {
                if(index == 0){
                    //导航到场站
                    navigationToStation();
                }else if(index == 1){
                    //导航到车
                    navigationToCar();
                }
            }

        });
    }
    /**
     * 导航到车
     */
    private void navigationToCar() {
        RevertCarModel revertCarModel = new RevertCarModel();
        revertCarModel.setLongitude(orderInfoModel.getData().getLongitude());
        revertCarModel.setLatitude(orderInfoModel.getData().getLatitude());
        IntentHelper.intentMap(mContext, revertCarModel.getLongitude(), revertCarModel.getLatitude(),false);

    }
    /**
     * 导航到场站
     */
    private void navigationToStation() {
        RevertCarModel revertCarModel = new RevertCarModel();
        if (orderInfoModel.getData().isPickuped()) {
            revertCarModel.setLongitude(orderInfoModel.getData().getPickupLongitude());
            revertCarModel.setLatitude(orderInfoModel.getData().getPickupLatitude());
            revertCarModel.setName(orderInfoModel.getData().getPickupAddress());
        } else {
            revertCarModel.setLongitude(orderInfoModel.getData().getReservationLocationLongitude());
            revertCarModel.setLatitude(orderInfoModel.getData().getReservationLocationLatitude());
            revertCarModel.setName(orderInfoModel.getData().getReservationLocationAddress());
        }
        IntentHelper.intentMap(mContext, revertCarModel.getLongitude(), revertCarModel.getLatitude(),true);


    }


    /**
     * 给客服打电话
     */
    private void callWork() {
        PopWindowHelper.getInstance().openCustomerConsult(mContext).show(findViewById(R.id.top__iv));

    }

    /**
     * 选择取车方式
     */
    private void choiceGetCarWay() {
        if (GET_CAR_WAY == OWNER_RETURN_CAR) {
            GET_CAR_WAY = COME_GET_CAR;
            returnCarInfoLlyt.setVisibility(View.VISIBLE);
            switchGetCar.setImageResource(R.drawable.ic_switch_on);
            pay_get_car.setVisibility(View.VISIBLE);
            returnCarBtn.setText("下一步");
        } else if (GET_CAR_WAY == COME_GET_CAR) {
            GET_CAR_WAY = OWNER_RETURN_CAR;
            returnCarInfoLlyt.setVisibility(View.GONE);
            switchGetCar.setImageResource(R.drawable.ic_switch_off);
            pay_get_car.setVisibility(View.GONE);
            returnCarBtn.setText("下一步");
        }
    }
}