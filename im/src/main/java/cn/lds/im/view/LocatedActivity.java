package cn.lds.im.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BaiduMapHelper;
import cn.lds.chatcore.common.DimensHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.common.permission.PermissionHelper;
import cn.lds.im.data.CityBean;
import cn.lds.im.data.CityModel;
import cn.lds.im.data.LocationCtiyModel;
import cn.lds.im.data.NearStationModel;
import cn.lds.im.data.ReservationOrdersLocationsModel;
import cn.lds.im.view.adapter.LocationParkAdpter;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;


/**
 * 定位页面
 *
 * @author pengwb
 */
public class LocatedActivity extends BaseActivity implements OnClickListener {

	protected final String TAG = LocatedActivity.class.getSimpleName();
	protected boolean isFirstLoc = true;// 是否首次定位
	protected LocationParkAdpter adpter = null;
	protected Intent intent;
	boolean useDefaultIcon = false;
	protected String no;

	protected MapView mMapView;
	protected BaiduMap mBaiduMap;

	protected BaiduMapHelper baiduMapHelper;
	protected ArrayList<ReservationOrdersLocationsModel.DataBean> allParks;
	protected ReservationOrdersLocationsModel.DataBean selectPark;
	protected int parkInfowindowOffset;//车场infowindow偏移量

	@ViewInject(R.id.top_back_btn)
	protected TextView top_back;
	@ViewInject(R.id.top_title_tv)
	protected TextView top_title;
	@ViewInject(R.id.top_menu_btn_del)
	protected ImageView topbar_menu_service;

	@ViewInject(R.id.location_listview)
	protected ListView location_listview;
	@ViewInject(R.id.input_keyword)
	protected EditText input_keyword;
	@ViewInject(R.id.location_clear)
	protected ImageButton btnSearchClear;
	@ViewInject(R.id.setlocation)
	protected Button setlocations;
	@ViewInject(R.id.loacitonmapview)
	protected FrameLayout locationmapview;
	@ViewInject(R.id.input_whichcity)
	protected TextView input_whichcity;
	@ViewInject(R.id.locationview)
	protected RelativeLayout locationview;
	@ViewInject(R.id.first_refresh_btn)
	protected Button first_refresh_btn;

	@ViewInject(R.id.triangle_arrow)
	protected ImageView triangle_arrow;  //城市左边向下三角标


	protected ArrayList<ReservationOrdersLocationsModel.DataBean> cityParks = new  ArrayList<>();
	protected List<LocationCtiyModel.DataBean> mCityList = new  ArrayList<>();

	protected View infoWindowView;//气泡的view

	protected int layoutID = R.layout.activity_result_loc;
	protected LocatedActivity activity;
	private String citCode;
	//	private List<CityBean> mList;

	protected void setActivity(LocatedActivity activity) {
		this.activity = activity;
	}

	protected void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layoutID);
//		mList = new CityModel().initCityData();
		ViewUtils.inject(LocatedActivity.class, this);
		if (null != activity) {
			ViewUtils.inject(activity);
		}
		initConfig();
	}

	protected void initConfig() {
		intent = getIntent();
		baiduMapHelper = new BaiduMapHelper(mContext);

		LoadingDialog.showDialog(mContext, "");
		initview();
		addlisener();
	}

	protected void initview() {
		top_back.setVisibility(View.VISIBLE);
		top_title.setVisibility(View.VISIBLE);

		allParks = new ArrayList<>();
		initAdapter();
		location_listview.setAdapter(adpter);

		//初始化视图
		mMapView = baiduMapHelper.getMapView();
		locationmapview.addView(mMapView, 0);
		mBaiduMap = mMapView.getMap();
		baiduMapHelper.initLocation(bdLocationListener);

		topbar_menu_service.setVisibility(View.VISIBLE);
		topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);
		input_keyword.setVisibility(View.VISIBLE);
		setlocations.setVisibility(View.VISIBLE);

		top_title.setText(getString(R.string.locatedactivity_title));


		parkInfowindowOffset = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.location_mark).getHeight();

//        no = intent.getAction();


	}
	protected void initAdapter() {
		adpter = new LocationParkAdpter(mContext, cityParks);
	}

	protected void addlisener() {
		try {
			top_back.setOnClickListener(this);
			btnSearchClear.setOnClickListener(this);
			setlocations.setOnClickListener(this);

			input_keyword.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int start, int before, int count) {
					if (cs.length() <= 0) {
						btnSearchClear.setVisibility(View.GONE);
						return;
					} else {
						btnSearchClear.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
			input_keyword.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
							|| actionId == EditorInfo.IME_ACTION_NEXT) {
						hideInputService();

						String keyword = input_keyword.getText().toString().trim();
						if (ToolsHelper.isNull(keyword))
							return true;
						setListPostion(keyword);
						isSearchResult = false;
						return true;
					}
					return false;

				}

			});

			location_listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					try {
						if (null != mBaiduMap) {
							mBaiduMap.hideInfoWindow();
						}
						ReservationOrdersLocationsModel.DataBean item = cityParks.get(position);
						LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
						LatLngBounds b = new LatLngBounds.Builder().include(latLng).build();
						List<Marker> markers = mBaiduMap.getMarkersInBounds(b);
						Marker marker = markers.get(0);
						parkClick(item, marker);
					} catch (Exception e) {
						LogHelper.d(TAG, e);
					}
				}
			});

			mBaiduMap.setOnMarkerClickListener(markerClickListener);


		} catch (Exception ex) {
			LogHelper.e(TAG, ex);
		}
	}
	boolean isSearchResult = false;
	/**
	 * 根据关键字获取list位置
	 */
	protected void setListPostion(String keyword) {
		for (int i = 0; i < cityParks.size(); i++) {
			String name = cityParks.get(i).getName();
			String address = cityParks.get(i).getAddress();
			if (name.contains(keyword)) {
				refreshUi(cityParks.get(i));
				selectPark = cityParks.get(i);
				location_listview.setSelection(i);
				isSearchResult = true;
				return;
			}

			if (address.contains(keyword)) {
				refreshUi(cityParks.get(i));
				location_listview.setSelection(i);
				isSearchResult = true;
				return;
			}

		}
		if(!isSearchResult){
			Toast.makeText(this,"没有搜索到场站信息",Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 場站點擊
	 *
	 * @param item
	 * @param marker
	 */
	protected void parkClick(ReservationOrdersLocationsModel.DataBean item, Marker marker) {
		selectPark = item;
		refreshUi(item);
	}

	/**
	 * 刷新listview及地图位置
	 */
	protected void refreshUi(ReservationOrdersLocationsModel.DataBean item) {
		// 定位
		LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
		baiduMapHelper.setCenter(latLng);
		for (ReservationOrdersLocationsModel.DataBean item2 : cityParks) {
			item2.setSelected(false);
		}
		item.setSelected(true);
		adpter.notifyDataSetChanged();
	}


	/**
	 * 气泡的弹窗
	 *
	 * @param latLng
	 */
	protected void showInfoWindow(LatLng latLng) {
		//显示infowindow
		InfoWindowViewHolder infoWindowViewHolder = null;
		if (infoWindowView == null) {
			infoWindowView = LayoutInflater.from(mContext).inflate(R.layout.item_firstfragment_infowindow, null);
			infoWindowViewHolder = new InfoWindowViewHolder();
			infoWindowViewHolder.name = (TextView) infoWindowView.findViewById(R.id.item_firstfragment_infowindow_text);
			infoWindowViewHolder.vehiclecount = (TextView) infoWindowView.findViewById(R.id.map_hlist_infowindow_car);
			infoWindowViewHolder.chargespotscount = (TextView) infoWindowView.findViewById(R.id.map_hlist_infowindow_power);
			infoWindowViewHolder.parkinglotcount = (TextView) infoWindowView.findViewById(R.id.map_hlist_infowindow_p);
			infoWindowView.setTag(infoWindowViewHolder);
		} else {
			infoWindowViewHolder = (InfoWindowViewHolder) infoWindowView.getTag();
		}
		if (selectPark != null) {
			infoWindowViewHolder.name.setText(selectPark.getName());
			infoWindowViewHolder.vehiclecount.setText(selectPark.getInstantAvailableVehicle());
			infoWindowViewHolder.chargespotscount.setText(selectPark.getChargeSpots());
			infoWindowViewHolder.parkinglotcount.setText(selectPark.getParkingLots());
		}
		if (latLng != null) {
			InfoWindow mInfoWindow = new InfoWindow(infoWindowView, latLng, -parkInfowindowOffset);
			mBaiduMap.showInfoWindow(mInfoWindow);
		}
	}

	public final class InfoWindowViewHolder {
		private TextView name;
		private TextView vehiclecount;
		private TextView chargespotscount;
		private TextView parkinglotcount;
	}

	/**
	 * 隐藏键盘
	 */
	protected void hideInputService() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(LocatedActivity.this
				.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

	}

	@OnClick({R.id.top_back_btn,
			R.id.top_menu_btn_del,
			R.id.setlocation,
			R.id.location_clear,
			R.id.locatedactivity_confirm,
			R.id.input_whichcity,
			R.id.first_refresh_btn,
	})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.input_whichcity:
				selectCity(); //城市切换
				break;
			case R.id.top_back_btn:
				finish();
				break;
			case R.id.location_clear:
				input_keyword.setText("");
				btnSearchClear.setVisibility(View.GONE);
				adpter.notifyDataSetChanged();
				break;
			case R.id.setlocation:
				if (isFirstLoc) {
					ToolsHelper.showStatus(mContext, false, getString(R.string.locatedactivity_input_after_aftersuccess));
					return;
				}
				baiduMapHelper.setCenter(MyApplication.myLocation);
				break;
			case R.id.top_menu_btn_del:
				PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
				break;
			case R.id.locatedactivity_confirm:
				Intent intent = new Intent();
				if (null == selectPark) {
					ToolsHelper.showStatus(mContext, false, getString(R.string.bookcar_choiceparkingstate));
					return;
				}
				intent.putExtra("selectPark", selectPark);
				setResult(RESULT_OK, intent);
				finish();
				break;
			case R.id.first_refresh_btn:
				baiduMapHelper.resumeMap();
				LoadingDialog.showDialog(mContext, "刷新");
				ModuleHttpApi.getNearStations("PUBLIC",ModuleHttpApiKey.reservationOrdersLocations,MyApplication.myLocation,0,10000,"",0,citCode);
//				ModuleHttpApi.reservationOrdersLocations(citCode);
				hideInfowindow();
				break;
		}
	}

	/**
	 * 城市切换
	 */
	private void selectCity() {
		CustomPopuwindow.getInstance().showList(Gravity.LEFT,this, mCityList , input_whichcity,triangle_arrow, new CustomPopuwindow.OnChoiceCityListener() {
            @Override
            public void selectedCity(Object o, int index) {
				LocationCtiyModel.DataBean data = (LocationCtiyModel.DataBean) o;
                input_whichcity.setText(data.getName());
                for(int i = 0; i < mCityList.size();i++){
                    LocationCtiyModel.DataBean cityBean = mCityList.get(i);
                    if(i == index){
                        cityBean.setSelected(true);
                        citCode = cityBean.getCode();
//                        ModuleHttpApi.reservationOrdersLocations(citCode);
						ModuleHttpApi.getNearStations("PUBLIC",ModuleHttpApiKey.reservationOrdersLocations,MyApplication.myLocation,0,10000,"",0,citCode);

					}else{
                        cityBean.setSelected(false);
                    }
                }
                LatLng lat = new LatLng(mCityList.get(index).getLatitude(), mCityList.get(index).getLongitude());
                baiduMapHelper.setCenter(lat);
                MyApplication.lastCity = mCityList.get(index).getName();

            }

            @Override
            public void dismiss() {

            }
        });
	}


	/**
	 * 隐藏列表，infowindow
	 */
	protected void hideInfowindow() {
		if (null != mBaiduMap) {
			mBaiduMap.hideInfoWindow();
		}
	}

	/**
	 * 定位SDK监听函数
	 */
	BDLocationListener bdLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder().accuracy(0)
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				MyApplication.myLocation = new LatLng(location.getLatitude(), location.getLongitude());
				isFirstLoc = false;
				String city = location.getCity();
				if (null != city) {
					MyApplication.lastCity = city.split(getString(R.string.locatedactivity_city))[0];
                    ModuleHttpApi.getLocationsCitys();
				}
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}

	};


	@Override
	protected void onPause() {
		setResult(RESULT_CANCELED, intent);
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			// 检查权限
			PermissionHelper.checkPermission(this,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION
			);
		}
	}

	@Override
	protected void onDestroy() {
		baiduMapHelper.dissmissMap();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}


	// 定制RouteOverly
	protected class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}


	protected BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
		@Override
		public boolean onMarkerClick(Marker marker) {
			marker.setToTop();
			int position = Integer.parseInt(marker.getTitle());
			ReservationOrdersLocationsModel.DataBean selectPark = allParks.get(position);
			parkClick(selectPark, marker);
			showInfoWindow(marker.getPosition());
			return false;
		}
	};

	/**
	 * 发送验证码
	 * 使用手机号注册
	 * 登录
	 *
	 * @param event http请求完成事件
	 */

	public void onEventMainThread(HttpRequestEvent event) {
		LoadingDialog.dismissDialog();
		try {
			// 获取API请求返回值
			HttpResult httpResult = event.getResult();
			// API区分
			String apiNo = httpResult.getApiNo();

            if (!(ModuleHttpApiKey.reservationOrdersLocations.equals(apiNo)
                    || ModuleHttpApiKey.getLocationsCitys.equals(apiNo))) {
                return;
            }
            switch (apiNo){
                case ModuleHttpApiKey.reservationOrdersLocations:
                    B001(event.getResult().getResult());// 全部停车场
                    break;
                case ModuleHttpApiKey.getLocationsCitys:
                   locationCityList(event.getResult().getResult());// 城市列表
                    break;
            }

		} catch (Exception ex) {
			LogHelper.e(this.getClass().getSimpleName(), ex);
		}
	}

    private void locationCityList(String result) {
        LocationCtiyModel locationCityModel = GsonImplHelp.get().toObject(result, LocationCtiyModel.class);
        List<LocationCtiyModel.DataBean> citys = locationCityModel.getData();
        if(citys != null && citys.size() > 0){
            mCityList.clear();
            mCityList.addAll(citys);
        }
        input_whichcity.setText(MyApplication.lastCity + "市");
        for(LocationCtiyModel.DataBean bean:mCityList){
            if(bean.getName().equals(MyApplication.lastCity + "市")){
                bean.setSelected(true);
				citCode = bean.getCode();
//				ModuleHttpApi.reservationOrdersLocations(citCode);
				ModuleHttpApi.getNearStations("PUBLIC",ModuleHttpApiKey.reservationOrdersLocations,MyApplication.myLocation,0,10000,"",0,citCode);

			}else{
                bean.setSelected(false);
            }
        }


    }


    /**
	 * 订阅：相关REST请求失败后
	 *
	 * @param event HttpRequestEvent
	 */
	public void onEventMainThread(HttpRequestErrorEvent event) {
		LoadingDialog.dismissDialog();
		// 只要是本画面的处理。都异常时都关闭等待
		HttpResult httpResult = event.getResult();
		// API区分
		String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.reservationOrdersLocations.equals(apiNo)
                || ModuleHttpApiKey.getLocationsCitys.equals(apiNo))) {
            return;
        }
        switch (apiNo){
            case ModuleHttpApiKey.reservationOrdersLocations:
                ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
                break;
            case ModuleHttpApiKey.getLocationsCitys:
                ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
                break;
        }

	}


	protected void B001(String httpResult) {
		ReservationOrdersLocationsModel ordersLocationsModel = GsonImplHelp.get().toObject(httpResult, ReservationOrdersLocationsModel.class);
		if (!allParks.isEmpty())
			allParks.clear();
		allParks.addAll(ordersLocationsModel.getData());
		cityParks.clear();
		cityParks.addAll(allParks);
		adpter.notifyDataSetChanged();
		for (int i = 0; i < allParks.size(); i++) { //设置附近地点，空闲车辆标示
			ReservationOrdersLocationsModel.DataBean dataBean = allParks.get(i);
			TextView textView = new TextView(this);
			textView.setGravity(Gravity.CENTER_HORIZONTAL);
			textView.setPadding(0, 15, 0, 0);
			textView.setBackgroundResource(cn.lds.chatcore.R.drawable.location_mark);
			textView.setTextColor(getResources().getColor(cn.lds.chatcore.R.color.textgray));
			textView.setTextSize(12);
			textView.setText(dataBean.getInstantAvailableVehicle() + "");

			LatLng latLng = new LatLng(dataBean.getLatitude(), dataBean.getLongitude());
			BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(textView);
			OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bdA).zIndex(12).title(i + "");
			mBaiduMap.addOverlay(ooA);
		}

		if (!cityParks.isEmpty() && cityParks.size() > 0) {
			selectPark = cityParks.get(0);
			selectPark.setSelected(true);
			LatLng latLng = new LatLng(selectPark.getLatitude(), selectPark.getLongitude());
			baiduMapHelper.setCenter(latLng);
			showInfoWindow(latLng);//初始化气泡弹窗
			locationview.setVisibility(View.VISIBLE);
			first_refresh_btn.setVisibility(View.VISIBLE);
			setlocations.setVisibility(View.VISIBLE);
		}else{
			locationview.setVisibility(View.GONE);
			first_refresh_btn.setVisibility(View.GONE);
			setlocations.setVisibility(View.GONE);
			Toast.makeText(LocatedActivity.this,MyApplication.lastCity + "还没有设置场站",Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		mMapView.onResume();

		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		LogHelper.e("onStart");
		try {
			EventBus.getDefault().register(this);
		} catch (Exception e) {
			LogHelper.e("注册EvenBus", e);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		LogHelper.e("onStop");
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
			LogHelper.e("注销EvenBus", e);
		}
	}

}
