package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.TripListModel;
import cn.lds.im.enums.TripStatus;
import cn.lds.im.enums.TripStatusEnum;
import cn.lds.im.view.adapter.TripListAdapter;
import de.greenrobot.event.EventBus;

/**
 * 行程列表页面
 * Created by sibinbin on 2017/7/19.
 */

public class TripListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
	/**
	 * 行程列表View
	 */
	private ListView tripLv;
	/**
	 * 行程信息的集合
	 */
	private List<TripListModel.DataBean> mList = new ArrayList<>();
	/**
	 * 返回按钮
	 */
	private Button backBtn;
	/**
	 * 页面标题
	 */
	private TextView titleTv;
	private TripListAdapter mAdapter;
	//无数据
	private LinearLayout mNoData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip_list);
		initView();
	}

	/**
	 * 初始化界面UI
	 */
	private void initView() {
		backBtn = (Button) findViewById(R.id.top_back_btn);
		titleTv = (TextView) findViewById(R.id.top_title_tv);
		tripLv = (ListView) findViewById(R.id.list_trip);
		mNoData = (LinearLayout)findViewById(R.id.no_data);

		titleTv.setText("行程");
		backBtn.setVisibility(View.VISIBLE);

		backBtn.setOnClickListener(this);
		tripLv.setOnItemClickListener(this);

		mAdapter = new TripListAdapter(this, mList,"OWNER");
		tripLv.setAdapter(mAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LoadingDialog.showDialog(mContext, "获取可开发票总额，请稍候");
		ModuleHttpApi.getTripList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.top_back_btn: //返回上一页
				finish();
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TripListModel.DataBean dataBean = mList.get(position);
		if (null != dataBean) {
			IntentHelper.intentTripList(mContext, mIntent, dataBean.getId(), dataBean.getStatus(), dataBean.getType(), dataBean.isDelivered(), dataBean.isScheduledTimeUp());
		}
	}

	protected void F001(String result) {
		TripListModel mTripModel = GsonImplHelp.get().toObject(result, TripListModel.class);
		if (null == mTripModel)
			return;
		if (mTripModel.getData() == null || mTripModel.getData().size() == 0) {
			mNoData.setVisibility(View.VISIBLE);
			tripLv.setVisibility(View.GONE);
		} else {
			mNoData.setVisibility(View.GONE);
			tripLv.setVisibility(View.VISIBLE);
			mList.clear();
			mList.addAll(mTripModel.getData());
		}
		mAdapter.notifyDataSetChanged();
	}

	public void onEventMainThread(HttpRequestEvent event) {
		HttpResult httpResult = event.httpResult;
		String apiNo = httpResult.getApiNo();
		if (!(ModuleHttpApiKey.getTripList.equals(apiNo))) {
			return;
		}
		LoadingDialog.dismissDialog();
		String result = httpResult.getResult();
		if (!ToolsHelper.isNull(result)) {
			switch (apiNo) {
				case ModuleHttpApiKey.getTripList:
					F001(httpResult.getResult());
					break;
			}
		}

	}

	public void onEventMainThread(HttpRequestErrorEvent event) {
		HttpResult httpResult = event.httpResult;
		String apiNo = httpResult.getApiNo();
		if (!(ModuleHttpApiKey.getTripList.equals(apiNo))) {
			return;
		}
		LoadingDialog.dismissDialog();
		ToolsHelper.showStatus(mContext, false, "获取行程失败");
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			EventBus.getDefault().register(this);
		} catch (Exception e) {
			LogHelper.e(ReceiptActivity.class.getName(), e);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
			LogHelper.e(ReceiptActivity.class.getName(), e);
		}
	}

}
