//package cn.lds.im.view;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.lidroid.xutils.view.annotation.event.OnClick;
//import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import cn.lds.chat.R;
//import cn.lds.chatcore.common.BitmapHelper;
//import cn.lds.chatcore.common.CoreHttpApiKey;
//import cn.lds.chatcore.common.GsonImplHelp;
//import cn.lds.chatcore.common.LoadingDialog;
//import cn.lds.chatcore.common.LogHelper;
//import cn.lds.chatcore.common.PopWindowHelper;
//import cn.lds.chatcore.common.ToolsHelper;
//import cn.lds.chatcore.common.ViewHeightCalculator;
//import cn.lds.chatcore.data.HttpResult;
//import cn.lds.chatcore.data.UploadModel;
//import cn.lds.chatcore.event.FileUploadErrorEvent;
//import cn.lds.chatcore.event.FileUploadedEvent;
//import cn.lds.chatcore.event.FileUploadingEvent;
//import cn.lds.chatcore.event.HttpRequestErrorEvent;
//import cn.lds.chatcore.event.HttpRequestEvent;
//import cn.lds.chatcore.manager.FileManager;
//import cn.lds.chatcore.view.BaseActivity;
//import cn.lds.im.common.ModuleHttpApi;
//import cn.lds.im.common.ModuleHttpApiKey;
//import cn.lds.im.data.InspectModel;
//import de.greenrobot.event.EventBus;
//import me.nereo.multi_image_selector.MultiImageSelectorActivity;
//
///**
// * 车况检查
// */
//public class InspectActivity extends BaseActivity {
//
//	protected final int PHOTO_REQUEST_SELECT = 5;// 选择图片
//	protected int PHOTO_NUMBER = 6;// 选择图片最大数
//	protected List<Map<String, Object>> picList = null;
//	protected int mScreenWidth;
//	protected MySimpleAdapter saImageItems;
//	protected List<String> picArray;
//	protected String messageId;//messageId 前台识别图片的唯一标识
//
//	protected InspectModel inspectModel;
//	protected int orderInfoId;
//	/**
//	 * 将选好的数据保存到临时对象中，进行后续操作
//	 * 目的，不改变全局变量中的值
//	 */
//	List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
//	protected int indext = 0;
//
//	//topbar
//	@ViewInject(R.id.top_back_btn)
//	protected Button top_back_btn;
//	@ViewInject(R.id.top_title_tv)
//	protected TextView top_title_tv;
//	//控件绑定
//	@ViewInject(R.id.inspect_exterior_rg)
//	protected RadioGroup inspect_exterior_rg;
//	@ViewInject(R.id.inspect_inside_rg)
//	protected RadioGroup inspect_inside_rg;
//	@ViewInject(R.id.inspect_tyre_rg)
//	protected RadioGroup inspect_tyre_rg;
//	@ViewInject(R.id.inspect_process_rg)
//	protected RadioGroup inspect_process_rg;
//	@ViewInject(R.id.inspect_gv)
//	protected GridView gridview;
//	@ViewInject(R.id.inspect_confirm_tv)
//	protected TextView inspect_confirm_tv;
//	@ViewInject(R.id.tv_word_num)
//	protected TextView tv_word_num;
//	@ViewInject(R.id.tv_pic_num)
//	protected TextView tv_pic_num;
//	@ViewInject(R.id.top_right_tv)
//	protected TextView top_right_tv;
//	@ViewInject(R.id.et_add_remarks)
//	protected EditText et_add_remarks;
//
//	protected int layoutID = R.layout.activity_inspect;
//	protected InspectActivity activity;
//
//	protected void setActivity(InspectActivity activity) {
//		this.activity = activity;
//	}
//
//	protected void setLayoutID(int layoutID) {
//		this.layoutID = layoutID;
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(layoutID);
//		ViewUtils.inject(InspectActivity.class, this);
//		if (null != activity) {
//			ViewUtils.inject(activity);
//		}
//		initConfig();
//	}
//
//	protected void initConfig() {
//		init();
//		initData();
//		refreshView();//刷新画面
//	}
//
//	protected void init() {
//		top_back_btn.setVisibility(View.VISIBLE);
//		top_title_tv.setVisibility(View.VISIBLE);
//		top_title_tv.setText(getString(R.string.inspect_title));
//
//		mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
//		//设置默认值
//		inspectModel = new InspectModel();
//		inspectModel.setPicList(null);
//		inspectModel.setSanitaryState(true);
//		inspectModel.setScratch(true);
//		inspectModel.setWheelOk(true);
//		//设置默认选择
//		inspect_exterior_rg.check(R.id.inspect_exterior_yes_rb);
//		inspect_inside_rg.check(R.id.inspect_inside_clean_rb);
//		inspect_tyre_rg.check(R.id.inspect_tyre_yes_rb);
//		inspect_process_rg.check(R.id.inspect_process_yes_rb);
//		top_right_tv.setVisibility(View.VISIBLE);
//		top_right_tv.setText(getString(R.string.inspect_skip));
//
//		et_add_remarks.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//				if (charSequence != null) {
//					tv_word_num.setText("(" + charSequence.length() + "/50)");
//				} else {
//					tv_word_num.setText("(50/50)");
//				}
//			}
//
//			@Override
//			public void afterTextChanged(Editable editable) {
//
//			}
//		});
//
//	}
//
//	protected void refreshView() {
//		defaultPicData();
//	}
//
//	protected void initData() {
//		orderInfoId = getIntent().getIntExtra("id", 0);
//		if (null == picArray)
//			picArray = new ArrayList<String>();//上传图片的ID存储类
//	}
//
//	@OnRadioGroupCheckedChange({R.id.inspect_exterior_rg, R.id.inspect_inside_rg, R.id.inspect_tyre_rg})
//	public void OnCheckedChangeListener(RadioGroup arg0, int arg1) {
//		switch (arg0.getId()) {
//			case R.id.inspect_exterior_rg:
//				switch (arg1) {
//					case R.id.inspect_exterior_yes_rb:
//						inspectModel.setScratch(true);
//						break;
//					case R.id.inspect_exterior_no_rb:
//						inspectModel.setScratch(false);
//						break;
//				}
//				break;
//			case R.id.inspect_inside_rg:
//				switch (arg1) {
//					case R.id.inspect_inside_clean_rb:
//						inspectModel.setSanitaryState(true);
//						break;
//					case R.id.inspect_inside_dirty_rb:
//						inspectModel.setSanitaryState(false);
//						break;
//				}
//				break;
//			case R.id.inspect_tyre_rg:
//				switch (arg1) {
//					case R.id.inspect_tyre_yes_rb:
//						inspectModel.setWheelOk(true);
//						break;
//					case R.id.inspect_tyre_no_rb:
//						inspectModel.setWheelOk(false);
//						break;
//				}
//				break;
//		}
//	}
//
//	@OnClick({R.id.top_back_btn, R.id.inspect_confirm_tv, R.id.top_right_tv, R.id.take_car_customer_lyt})
//	public void onClick(View view) {
//		switch (view.getId()) {
//			case R.id.top_back_btn:
//				finish();
//				break;
//			case R.id.inspect_confirm_tv:
//				LogHelper.e(inspectModel.toString());
//
//				if (picArray.size() != picList.size()) {
//					LoadingDialog.showDialog(mContext, getString(R.string.common_dialog_wait));
//					uploadPicture();
//				} else {
//					checkUploadPic(null);
//				}
//
//				break;
//			case R.id.top_right_tv:
//				startActivity(new Intent(mContext, CarInUseActivity.class));
//				finish();
//				break;
//			case R.id.take_car_customer_lyt:
//				PopWindowHelper.getInstance().openCustomerConsult(mContext).show(findViewById(R.id.top__iv));
//				break;
//		}
//	}
//
//	protected void uploadPicture() {
//		for (int i = 0; i < picList.size(); i++) {
//			Map<String, Object> map = picList.get(i);
//			messageId = "inspect_up" + System.currentTimeMillis();
//			//上传图片
//			FileManager.getInstance().uploadChatImage(map.get("pic_path").toString(), messageId);
//			map.put(messageId, null);
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == PHOTO_REQUEST_SELECT) {
//			if (resultCode == RESULT_OK) {
//				final List<String> d = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//				if (null != d) {
//					LoadingDialog.showDialog(mContext, "处理中");
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							picArray.clear();
//							for (int i = 0; i < d.size(); i++) {
//								Map<String, Object> map = new HashMap<String, Object>();
//								map.put("pic_type", "normal");
//								String mPath = "";
//								try {
//									mPath = BitmapHelper.compressPic(d.get(i), 100);
////							GraphicHelper.saveBitmapToFileFromPath(bmp, mPath);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								if (new File(mPath).exists())
//									map.put("pic_path", mPath);
//								else
//									map.put("pic_path", d.get(i));
//								picList.add(map);
//							}
//							handler.sendEmptyMessage(0);
//						}
//					}).start();
//				}
//			}
//
//		}
//	}
//
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//				case 0:
//					defaultPicData();
//					LoadingDialog.dismissDialog();
//					break;
//			}
//
//		}
//	};
//
//	protected void defaultPicData() {
//
//		if (picList == null) {
//			picList = new ArrayList<Map<String, Object>>();
//		}
//
//
//		tempList.clear();
//		tempList.addAll(picList);
//		if (picList.size() < PHOTO_NUMBER) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("pic_type", "add");
//			map.put("pic_path", R.drawable.icon_camera);
//
//			tempList.add(map);
//		}
//		tv_pic_num.setText("(" + picList.size() + "/" + PHOTO_NUMBER + ")");
//		if (null == saImageItems) {
//
//			saImageItems = new MySimpleAdapter(mContext, tempList);
//			// 添加并且显示
//			gridview.setAdapter(saImageItems);
//			// 添加消息处理
//			gridview.setOnItemClickListener(new ItemClickListener());
//		} else {
//			saImageItems.notifyDataSetChanged();
//		}
//
//		ViewHeightCalculator.setGrideViewHeightBasedOnChildren(mContext, gridview, 6, 0);
//	}
//
//	class ItemClickListener implements AdapterView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//			HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
//			if (!"add".equals(item.get("pic_type").toString())) {
//				return;
//			}
//			Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
//			// whether show camera
//			intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//			// max select image amount
//			intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, PHOTO_NUMBER - picList.size());
//			// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
//			intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
//			// default select images (support array list)
//			ArrayList<String> defaultDataArray = new ArrayList<String>();
//			intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
//			startActivityForResult(intent, PHOTO_REQUEST_SELECT);
//		}
//	}
//
//
//	class MySimpleAdapter extends BaseAdapter {
//
//		protected List<Map<String, Object>> listdata;
//
//		public MySimpleAdapter(Context context, List<Map<String, Object>> data) {
//			this.listdata = data;
//		}
//
//		@Override
//		public int getCount() {
//			return listdata.size();
//		}
//
//		@Override
//		public Map<String, Object> getItem(int position) {
//			return listdata.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			final Map<String, Object> map = getItem(position);
//			if (convertView == null) {
//				convertView = LayoutInflater.from(mContext).inflate(
//						R.layout.item_feedback_pic, parent, false);
//			}
//			if (position < picList.size())
//				picList.get(position).put("view", convertView);
//			ImageView icon = (ImageView) convertView
//					.findViewById(R.id.feedback_picture_iv);// 拿个这行的icon 就可以设置图片
//			ImageView delIcon = (ImageView) convertView
//					.findViewById(R.id.del_picture_iv);     //删除icon
//			ViewGroup.LayoutParams para = convertView.getLayoutParams();
//
//
//			para.width = (mScreenWidth) / 6;// 一屏显示两列
//			para.height = para.width;
//			convertView.setLayoutParams(para);
//
//			//选择的图片设置
//			if ("normal".equals(map.get("pic_type").toString())) {
//				final Map<String, Object> picMap = picList.get(position);
//				delIcon.setVisibility(View.VISIBLE);
//				BitmapHelper.displayPic(icon, map.get("pic_path").toString());
//
//				delIcon.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						picList.remove(picMap);//移除数据
//						defaultPicData();
//					}
//				});
//
//			} else {
//				//默认图片设置
//				delIcon.setVisibility(View.GONE);
//				icon.setImageResource(Integer.parseInt(map.get("pic_path").toString()));
//
//			}
//
//			return convertView;
//		}
//
//	}
//
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//		try {
//			EventBus.getDefault().register(this);
//		} catch (Exception e) {
//			LogHelper.e(getString(R.string.default_bus_register), e);
//		}
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		try {
//			EventBus.getDefault().unregister(this);
//		} catch (Exception e) {
//			LogHelper.e(getString(R.string.default_bus_unregister), e);
//		}
//	}
//
//	public void onEventMainThread(HttpRequestEvent event) {
//		HttpResult httpResult = event.getResult();
//		String apiNo = httpResult.getApiNo();
//		if (!(CoreHttpApiKey.registerFile.equals(apiNo) || ModuleHttpApiKey.condition.equals(apiNo))) {
//			return;
//		}
//		switch (httpResult.getApiNo()) {
//			case CoreHttpApiKey.registerFile:
////                checkUploadPic(httpResult);
//				break;
//			case ModuleHttpApiKey.condition:
//				LoadingDialog.dismissDialog();
//				ToolsHelper.showStatus(mContext, true, getString(R.string.inspect_up_success));
//				startActivity(new Intent(mContext, CarInUseActivity.class));
//				finish();
//				break;
//		}
//
//
//	}
//
//	public void onEventMainThread(FileUploadedEvent event) {
//		indext++;
//		checkUploadPic(event);
//
//	}
//
//	public void onEventMainThread(FileUploadErrorEvent event) {
//		indext++;
//		if (indext == picList.size()) {
//			ToolsHelper.showStatus(mContext, false, "上传图片失败");
//			LoadingDialog.dismissDialog();
//			indext = 0;
//			defaultPicData();
//		}
//
//	}
//
//	public void onEventMainThread(HttpRequestErrorEvent event) {
//		HttpResult httpResult = event.getResult();
//		String apiNo = httpResult.getApiNo();
//		if (!(CoreHttpApiKey.registerFile.equals(apiNo) || ModuleHttpApiKey.condition.equals(apiNo))) {
//			return;
//		}
//		LoadingDialog.dismissDialog();
//		ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
//	}
//
//	/**
//	 * 下载中进程监听
//	 *
//	 * @param event
//	 */
//	public void onEventMainThread(FileUploadingEvent event) {
//
//		String messageId = event.getOwner();
//
//		int percent = event.getProgress();
//
//		for (int i = 0; i < picList.size(); i++) {
//			Map<String, Object> map = picList.get(i);
//			if (map.containsKey(messageId)) {
//				View view = (View) map.get("view");
//				ImageView delIcon = (ImageView) view
//						.findViewById(R.id.del_picture_iv);     //删除icon
//				delIcon.setVisibility(View.GONE);
//				FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.progress_bar_frame);
//				frameLayout.setVisibility(View.GONE);
//				updateCircleProgressDialog(percent, view);
//			}
//
//		}
//	}
//
//	/**
//	 * 显示带进度条的dialog
//	 */
//	public static void updateCircleProgressDialog(int percent, View view) {
////        TextView tvPercent = (TextView) view.findViewById(cn.lds.chatcore.R.id.tvPercent);
////        ColorfulRingProgressView isDownload = (ColorfulRingProgressView) view.findViewById(cn.lds.chatcore.R.id.crpv);
////        isDownload.setPercent(percent);
////        tvPercent.setText("" + percent);
//	}
//
//	/**
//	 * 检测是否上传完成（无图片提交反馈，带图片提交反馈）
//	 * <p/>
//	 *
//	 * @param event
//	 */
//	protected synchronized void checkUploadPic(FileUploadedEvent event) {
//
//		String fileStorageId = "";//上传文件后，返回的服务器文件ID
//		String messageId = "";//本地唯一标识ID
//		UploadModel uploadModel = null;
//		if (null != event)
//			uploadModel = GsonImplHelp.get().toObject(event.getResult(), UploadModel.class);
//		if (null != uploadModel) {
//			messageId = event.getOwner();
//			fileStorageId = uploadModel.getData().get(0).getNo();
//		}
//
//		//无图片不走循环 上传成功，将fileStorageId保存到piclist中
//		for (int i = 0; i < picList.size(); i++) {
//			Map<String, Object> map = picList.get(i);
//			if (!map.containsKey(messageId)) {
//				continue;
//			}
//			map.put(messageId, fileStorageId);
//			picArray.add(fileStorageId);
//		}
//
//		if (picArray.size() == picList.size()) {//判断将要上传的图片ID是否与已经上传的图片数量相等
////                ToolsHelper.showInfo(mContext, "上传完成开始提交");
//			LoadingDialog.dismissDialog();
//			LoadingDialog.showDialog(mContext, getString(R.string.inspect_loading));
//			inspectModel.setPicList(picArray);
//			indext = 0;
//			ModuleHttpApi.condition(inspectModel, orderInfoId);
//		} else {
//			return;
//		}
//
//	}
//}
