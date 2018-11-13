//package cn.lds.im.view;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.lidroid.xutils.view.annotation.event.OnClick;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.lds.chat.R;
//import cn.lds.chatcore.common.PopWindowHelper;
//import cn.lds.chatcore.common.PopWindowListener;
//import cn.lds.chatcore.view.BaseFragmentActivity;
//import cn.lds.im.fragment.FileClassFragment;
//import cn.lds.im.fragment.FileListFragment;
//import cn.lds.im.view.adapter.FragmentTabAdapter;
//
//public class SelectFileActivity extends BaseFragmentActivity {
//
//    @ViewInject(R.id.top_title_tv)
//    protected TextView top_title_tv;
//    @ViewInject(R.id.top_back_btn)
//    protected Button top_back_btn;
//
//    @ViewInject(R.id.id_select_file_radiogroup)
//    protected RadioGroup select_fileRadiogroup;
//    @ViewInject(R.id.id_select_file_iv1)
//    protected ImageView select_fileIv1;
//    @ViewInject(R.id.id_select_file_iv2)
//    protected ImageView select_fileIv2;
//
//    public List<Fragment> fragments = new ArrayList<Fragment>();
//
//    protected int number = 0;
//
//    protected Context mContext;
//
//    protected int layoutID = R.layout.activity_select_file;
//    protected SelectFileActivity activity;
//
//    protected void setActivity(SelectFileActivity activity) {
//        this.activity = activity;
//    }
//    protected void setLayoutID(int layoutID) {
//        this.layoutID = layoutID;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(layoutID);
//        ViewUtils.inject(SelectFileActivity.class,this);
//        if (null != activity) {
//            ViewUtils.inject(activity);
//        }
//        initConfig();
//    }
//
//    protected void initConfig() {
//        init();
//    }
//
//    FileListFragment fileListFragment;
//    FileClassFragment fileClassFragment;
//
//    public void init() {
//        mContext = SelectFileActivity.this;
//        top_title_tv.setText(getString(R.string.selectfileactivity_title));
//        top_back_btn.setVisibility(View.VISIBLE);
//        fileListFragment = new FileListFragment();
//        fileClassFragment = new FileClassFragment();
//        fileListFragment.setOnItemClickListener(new FileListFragment.OnItemClickListener() {
//            @Override
//            public void onItemClick(final String name, final String path) {
//                PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
//                    @Override
//                    public void confirm() {
//                        // 返回已选择的文件路径
//                        Intent data = new Intent();
//                        data.putExtra("path", path);
//                        setResult(RESULT_OK, data);
//                        finish();
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//
//                    @Override
//                    public void onItemListener(int position) {
//
//                    }
//                }).setContentTx(getString(R.string.selectfileactivity_pickup_currentfile)).show(findViewById(R.id.top__iv));
//            }
//        });
//        fileClassFragment.setOnItemClickListener(new FileClassFragment.OnItemClickListener() {
//            @Override
//            public void onItemClick(final String name, final String path) {
//                PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
//                    @Override
//                    public void confirm() {
//                        // 返回已选择的文件路径
//                        Intent data = new Intent();
//                        data.putExtra("path", path);
//                        setResult(RESULT_OK, data);
//                        finish();
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//
//                    @Override
//                    public void onItemListener(int position) {
//
//                    }
//                }).setContentTx(getString(R.string.selectfileactivity_pickup_currentfile)).show(findViewById(R.id.top__iv));
//
//            }
//        });
//
//        fragments.add(fileClassFragment);
//        fragments.add(fileListFragment);
//        initAdapter();
//    }
//
//    protected void initAdapter() {
//        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.fl_select_file_content, select_fileRadiogroup);
//        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
//            @Override
//            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
//                System.out.println("Extra---- " + index + " checked!!! ");
//                switch (radioGroup.getCheckedRadioButtonId()) {
//                    case R.id.id_select_file_radiobtn1:
//                        select_fileIv1.setBackgroundResource(R.color.eventselected45c01a);
//                        select_fileIv2.setBackgroundResource(0);
//                        number = 0;
//                        break;
//                    case R.id.id_select_file_radiobtn2:
//                        select_fileIv1.setBackgroundResource(0);
//                        select_fileIv2.setBackgroundResource(R.color.eventselected45c01a);
//                        number = 1;
//                        break;
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 返回
//     *
//     * @param v
//     */
//    @OnClick(R.id.top_back_btn)
//    public void onClick(View v) {
//        finish();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (1 == number) {
//            if (!fileListFragment.onKeyDown(keyCode, event))
//                finish();
//            else {
//            }
//        } else if (0 == number) {
//            if (!fileClassFragment.onKeyDown(keyCode, event))
//                finish();
//            else {
//            }
//        }
//        return true;
//    }
//}
