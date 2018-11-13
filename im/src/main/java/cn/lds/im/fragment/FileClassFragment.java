//package cn.lds.im.fragment;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.support.v4.app.Fragment;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import cn.lds.chat.R;
//import cn.lds.chatcore.common.FileHelper;
//import cn.lds.chatcore.common.ToolsHelper;
//import cn.lds.chatcore.data.FileModel;
//import cn.lds.im.view.adapter.FileListAdapter;
//
///**
// * Created by xuqm on 2016/5/17.
// */
//
//
//public class FileClassFragment extends Fragment {
//    protected View view;
//    protected Context mContext;
//
//    protected OnItemClickListener mOnItemClickListener;
//
//    @ViewInject(R.id.file_class_gridview)
//    protected GridView file_class_gridview;
//    @ViewInject(R.id.file_class_listview)
//    protected ListView file_class_listview;
//
//
//    protected MySimpleAdapter saImageItems;
//    List<Map<String, Object>> lstImageItem2 = new ArrayList<Map<String, Object>>();
//
//
//    protected FileListAdapter fileListAdapter;
//    protected List<FileModel> listFiles = new ArrayList<>();//当前文件夹下所有数据
//
//    protected int mScreenWidth;
//
//    protected List<FileModel> list_apk = new ArrayList<>();
//    protected List<FileModel> list_zip = new ArrayList<>();
//    protected List<FileModel> list_video = new ArrayList<>();
//    protected List<FileModel> list_audio = new ArrayList<>();
//    protected List<FileModel> list_image = new ArrayList<>();
//    protected List<FileModel> list_txt = new ArrayList<>();
//    protected List<FileModel> list_download = new ArrayList<>();
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_file_class, null);
//        ViewUtils.inject(FileClassFragment.class,this, view);
//        init();
//        return view;
//    }
//
//    protected void init() {
//        ViewUtils.inject(this, view);
//        mContext = getActivity();
//        getData();
//        mScreenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
//        saImageItems = new MySimpleAdapter(mContext, lstImageItem2);
//        // 添加并且显示
//        file_class_gridview.setAdapter(saImageItems);
//        // 添加消息处理
//        file_class_gridview.setOnItemClickListener(new ItemClickListener());
//        initMenu(0, 0, 0, 0, 0, 0, 0);
//        initAdapter();
//        file_class_listview.setAdapter(fileListAdapter);
//        file_class_listview.setOnItemClickListener(onItemClickListener);
//        file_class_listview.setVisibility(View.GONE);
//        file_class_gridview.setVisibility(View.VISIBLE);
//    }
//
//    protected void initAdapter() {
//        fileListAdapter = new FileListAdapter(mContext, listFiles);
//    }
//
//    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            FileModel seFileModel = fileListAdapter.getItem(position);
//            if (0 == position && "上级文件夹".equals(seFileModel.getName())) {
//                file_class_listview.setVisibility(View.GONE);
//                file_class_gridview.setVisibility(View.VISIBLE);
//                return;
//            }
//            if (null != mOnItemClickListener)
//                mOnItemClickListener.onItemClick(seFileModel.getName(), seFileModel.getPath());
//        }
//    };
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
//    protected void initMenu(int s1, int s2, int s3, int s4, int s5, int s6, int s7) {
//        lstImageItem2.clear();
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("name", "图片");
//        map1.put("size", s1);
//        lstImageItem2.add(map1);
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("name", "视频");
//        map2.put("size", s2);
//        lstImageItem2.add(map2);
//        Map<String, Object> map3 = new HashMap<>();
//        map3.put("name", "音频");
//        map3.put("size", s3);
//        lstImageItem2.add(map3);
//        Map<String, Object> map4 = new HashMap<>();
//        map4.put("name", "文档");
//        map4.put("size", s4);
//        lstImageItem2.add(map4);
//        Map<String, Object> map5 = new HashMap<>();
//        map5.put("name", "APK");
//        map5.put("size", s5);
//        lstImageItem2.add(map5);
//        Map<String, Object> map6 = new HashMap<>();
//        map6.put("name", "压缩包");
//        map6.put("size", s6);
//        lstImageItem2.add(map6);
//        Map<String, Object> map7 = new HashMap<>();
//        map7.put("name", "下载");
//        map7.put("size", s7);
//        lstImageItem2.add(map7);
//
//        saImageItems.notifyDataSetChanged();
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    initMenu(list_image.size(), list_video.size(), list_audio.size(), list_txt.size(), list_apk.size(), list_zip.size(), list_download.size());
//                    break;
//                case 1:
//                    ToolsHelper.showStatus(mContext,false,mContext.getString(R.string.fileclassfragment_one));
//                    break;
//            }
//        }
//    };
//
//    public void getData() {
//        new Thread(new Runnable() {//检索音频
//            @Override
//            public void run() {
//                list_audio.addAll(FileHelper.getAudiosFromMedia(mContext));
//                handler.sendEmptyMessage(0);
//            }
//        }
//        ).start();
//        new Thread(new Runnable() {//检索下载
//            @Override
//            public void run() {
//                list_download.addAll(FileHelper.getDownloads());
//                handler.sendEmptyMessage(0);
//            }
//        }
//        ).start();
//        new Thread(new Runnable() {//检索图片
//            @Override
//            public void run() {
//                list_image.addAll(FileHelper.getImagesFromMedia(mContext));
//                handler.sendEmptyMessage(0);
//            }
//        }
//        ).start();
//        new Thread(new Runnable() {//检索视频
//            @Override
//            public void run() {
//                list_video.addAll(FileHelper.getVideosFromMedia(mContext));
//
//                handler.sendEmptyMessage(0);
//            }
//        }
//        ).start();
//        new Thread(new Runnable() {//检索 apk,zip,tex
//            @Override
//            public void run() {
//                // 扫描files文件库
//                Cursor c = null;
//                ContentResolver resolver = mContext.getContentResolver();
//                try {
//                    c = resolver.query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data"}, null, null, null);
//                    int dataindex = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
//                    list_apk.clear();
//                    list_zip.clear();
//                    list_txt.clear();
//
//                    while (c.moveToNext()) {
//                        String path = c.getString(dataindex);
//
//                        if (FileHelper.isDocument(path)) {
//                            if (!FileHelper.isExists(path)) {
//                                continue;
//                            }
//                            list_txt.add(new FileModel(path, false));
//                        } else if (FileHelper.isZip(path)) {
//                            if (!FileHelper.isExists(path)) {
//                                continue;
//                            }
//                            list_zip.add(new FileModel(path, false));
//                        } else if (FileHelper.isApk(path)) {
//                            if (!FileHelper.isExists(path)) {
//                                continue;
//                            }
//                            list_apk.add(new FileModel(path, false));
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//
//                    if (c != null) {
//                        c.close();
//                    }
//                }
//                handler.sendEmptyMessage(0);
//            }
//        }
//        ).start();
//    }
//
//    // 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
//    class ItemClickListener implements AdapterView.OnItemClickListener {
//        @SuppressWarnings("unchecked")
//        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            // 在本例中arg2=arg3
//            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
//            if (0 == (int) item.get("size")) {
//                ToolsHelper.showStatus(mContext, false, mContext.getString(R.string.fileclassfragment_directory_nodata));
//                return;
//            }
//
//            listFiles.clear();
//            listFiles.add(new FileModel("up", "上级文件夹", 0, 0));
//            if ("图片".equals(ToolsHelper.isNullString((String) item.get("name")))) {
//                listFiles.addAll(list_image);
//            } else if ("视频".equals(ToolsHelper.isNullString((String) item.get("name")))) {
//                listFiles.addAll(list_video);
//            } else if ("音频".equals(ToolsHelper.isNullString((String) item.get("name")))) {
//                listFiles.addAll(list_audio);
//            } else if ("文档".equals(ToolsHelper.isNullString((String) item.get("name")))) {
//                listFiles.addAll(list_txt);
//            } else if ("APK".equals(ToolsHelper.isNullString((String) item.get("name")))) {
//                listFiles.addAll(list_apk);
//            } else if ("压缩包".equals(ToolsHelper.isNullString((String) item.get("name")))) {
//                listFiles.addAll(list_zip);
//            } else if ("下载".equals(ToolsHelper.isNullString((String) item.get("name")))) {
//                listFiles.addAll(list_download);
//            }
//            fileListAdapter.notifyDataSetChanged();
//            file_class_listview.setVisibility(View.VISIBLE);
//            file_class_gridview.setVisibility(View.GONE);
//        }
//    }
//
//    class MySimpleAdapter extends BaseAdapter {
//
//        protected ArrayList<HashMap<String, Object>> listdata;
//
//        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data) {
//            this.listdata = (ArrayList<HashMap<String, Object>>) data;
//        }
//
//        @Override
//        public int getCount() {
//            return listdata.size();
//        }
//
//        @Override
//        public HashMap<String, Object> getItem(int position) {
//            return listdata.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = LayoutInflater.from(mContext).inflate(
//                        R.layout.fragment_file_class_item, parent, false);
//            }
//            ImageView imageView = (ImageView) convertView.findViewById(R.id.file_class_ItemImage);
//            TextView textView = (TextView) convertView.findViewById(R.id.file_class_ItemText);
//            ViewGroup.LayoutParams para = convertView.getLayoutParams();
//
//
//            para.width = (mScreenWidth) / 3;// 一屏显示两列
//            para.height = para.width;
//            convertView.setLayoutParams(para);
//
//            textView.setText(ToolsHelper.isNullString((String) listdata.get(position).get("name")) + "(" + String.valueOf((int) listdata.get(position).get("size")).toString() + ")");
//
//
//            if ("图片".equals(ToolsHelper.isNullString((String) listdata.get(position).get("name")))) {
//                imageView.setImageResource(R.drawable.file_image);
//            } else if ("视频".equals(ToolsHelper.isNullString((String) listdata.get(position).get("name")))) {
//                imageView.setImageResource(R.drawable.file_video);
//            } else if ("音频".equals(ToolsHelper.isNullString((String) listdata.get(position).get("name")))) {
//                imageView.setImageResource(R.drawable.file_voice);
//            } else if ("文档".equals(ToolsHelper.isNullString((String) listdata.get(position).get("name")))) {
//                imageView.setImageResource(R.drawable.file_txt);
//            } else if ("APK".equals(ToolsHelper.isNullString((String) listdata.get(position).get("name")))) {
//                imageView.setImageResource(R.drawable.file_apk);
//            } else if ("压缩包".equals(ToolsHelper.isNullString((String) listdata.get(position).get("name")))) {
//                imageView.setImageResource(R.drawable.file_cutdown);
//            } else if ("下载".equals(ToolsHelper.isNullString((String) listdata.get(position).get("name")))) {
//                imageView.setImageResource(R.drawable.file_download);
//            }
//
//
//            return convertView;
//        }
//
//    }
//
//    // 返回按钮
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (View.VISIBLE == file_class_listview.getVisibility()) {
//            file_class_listview.setVisibility(View.GONE);
//            file_class_gridview.setVisibility(View.VISIBLE);
//            return true;
//        }
//        return false;
//    }
//
//}
