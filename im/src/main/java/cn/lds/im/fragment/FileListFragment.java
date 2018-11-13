//package cn.lds.im.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.ScrollView;
//
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.lds.chat.R;
//import cn.lds.chatcore.common.FileHelper;
//import cn.lds.chatcore.common.ToolsHelper;
//import cn.lds.chatcore.data.FileModel;
//import cn.lds.im.view.adapter.FileHorizontalScrollViewAdapter;
//import cn.lds.im.view.adapter.FileListAdapter;
//import cn.lds.im.view.widget.FileMyHorizontalScrollView;
//
///**
// * Created by xuqm on 2016/5/17.
// */
//
//
//public class FileListFragment extends Fragment {
//    protected View view;
//    protected Context mContext;
//
//    protected OnItemClickListener mOnItemClickListener;
//
//    @ViewInject(R.id.select_file_horizontalScrollView)
//    protected FileMyHorizontalScrollView horizontalScrollView;
//    protected FileHorizontalScrollViewAdapter horizontalScrollViewAdapter;
//
//    //horizontalScrollView 数据
//    protected List<FileModel> horizontalList = new ArrayList<>();
//    protected FileModel mFileModel;//当前文件夹
//
//    @ViewInject(R.id.select_file_listview)
//    protected ListView listview;
//
//    protected List<FileModel> listFiles = new ArrayList<>();//当前文件夹下所有数据
//    protected FileListAdapter fileListAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_file_list, null);
//        ViewUtils.inject(FileListFragment.class,this, view);
//        init();
//        return view;
//    }
//
//    protected void init() {
//        ViewUtils.inject(this, view);
//        mContext = getActivity();
//
//        horizontalScrollView.setOnItemClickListener(new FileMyHorizontalScrollView.OnItemClickListener() {
//
//                                                        @Override
//                                                        public void onClick(View view, int position) {
//                                                            mFileModel = horizontalList.get(position);
//                                                            setHorizontalScrollView();
//                                                        }
//                                                    }
//
//        );
//        // 设置适配器
//        initAdapter();
//        listview.setAdapter(fileListAdapter);
//        listview.setOnItemClickListener(onItemClickListener);
//
//        mFileModel = new FileModel(Environment.getExternalStorageDirectory().getPath(), true);
//        setHorizontalScrollView();
//
//    }
//
//    protected void initAdapter() {
//        horizontalScrollViewAdapter = new FileHorizontalScrollViewAdapter(mContext, horizontalList);
//        fileListAdapter = new FileListAdapter(mContext, listFiles);
//    }
//
//
//    public void setOnItemClickListener(OnItemClickListener l) {
//        mOnItemClickListener = l;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(String name, String path);
//    }
//
//    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            FileModel seFileModel = fileListAdapter.getItem(position);
//            if (seFileModel.isDirectory()) {
//                mFileModel = seFileModel;
//                setHorizontalScrollView();
//            } else {
//                if (null != mOnItemClickListener)
//                    mOnItemClickListener.onItemClick(seFileModel.getName(), seFileModel.getPath());
//            }
//        }
//    };
//
//    protected void setHorizontalList(FileModel fileModel) {
//        if (Environment.getExternalStorageDirectory().getPath().equals(fileModel.getPath())) {
//            fileModel.setName("根目录");
//            horizontalList.add(0, fileModel);
//        } else {
//            horizontalList.add(0, fileModel);
//            FileModel f = new FileModel(fileModel.getParentPath(), true);
//            setHorizontalList(f);
//        }
//    }
//
//    /**
//     * 设置水平滚动控件
//     */
//    protected void setHorizontalScrollView() {
//        horizontalList.clear();
//        setHorizontalList(mFileModel);
//        horizontalScrollViewAdapter.notifyDataSetChanged();
//        horizontalScrollView.initDatas(horizontalScrollViewAdapter);
//        handler.sendEmptyMessage(0);
//
//        File[] files = new File(mFileModel.getPath()).listFiles();
//
//        FileModel fileInfo = null;
//        listFiles.clear();
//        if (null != files && files.length > 0) {
//            // for
////            if (files.length > PROGRESS_SHOW_MIN) {
////                mHandler.sendEmptyMessage(MSG_SHOW_PROGRESS);
////            }
//            for (int i = 0; i < files.length; i++) {
//                try {
//                    if (files[i].getName().startsWith(".")) {
//                        continue;
//                    }
//                    fileInfo = new FileModel(files[i].getCanonicalPath(), files[i].isDirectory());
//                    fileInfo.setCreateTime(files[i].lastModified());
//                    fileInfo.setFileSize(FileHelper.getChildCount(files[i]));
//                    fileInfo.setName(files[i].getName());
//                    fileInfo.setParentPath(files[i].getParent());
//                    listFiles.add(fileInfo);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }// end for
//        }
//
//        handler.sendEmptyMessage(1);
//    }
//
//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    //滑动到最右侧
//                    horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
//                    break;
//                case 1:
//                    fileListAdapter.notifyDataSetChanged();
//                    //滑动到最上
//                    if (listFiles.size() > 0)
//                        listview.setSelection(0);
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    // 返回按钮
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (horizontalList.size() > 1) {
//            mFileModel = horizontalList.get(horizontalList.size() - 2);
//            setHorizontalScrollView();
//            return true;
//        }
//        return false;
//    }
//
//}
