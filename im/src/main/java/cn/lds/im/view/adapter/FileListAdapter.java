//package cn.lds.im.view.adapter;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.io.File;
//import java.util.List;
//
//import cn.lds.chat.R;
//import cn.lds.chatcore.common.FileHelper;
//import cn.lds.chatcore.common.TimeHelper;
//import cn.lds.chatcore.data.FileModel;
//import cn.lds.im.data.TypeFile;
//
///**
// * Created by xuqm on 2016/5/17.
// */
//public class FileListAdapter extends BaseAdapter {
//
//    private Context mContext;
//    private List<FileModel> mListFiles;
//    private LayoutInflater mInflater;
//
//    public FileListAdapter(Context mContext, List<FileModel> listFiles) {
//        this.mContext = mContext;
//        this.mListFiles = listFiles;
//        mInflater = LayoutInflater.from(mContext);
//    }
//
//    @Override
//    public int getCount() {
//        return mListFiles.size();
//    }
//
//    @Override
//    public FileModel getItem(int position) {
//        return mListFiles.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        FileModel fileModel = getItem(position);
//        ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.fragment_file_list_item, null);
//            holder.file_item_image = (ImageView) convertView.findViewById(R.id.file_item_image);
//            holder.file_item_name = (TextView) convertView.findViewById(R.id.file_item_name);
//            holder.file_item_size = (TextView) convertView.findViewById(R.id.file_item_size);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        if ("上级文件夹".equals(fileModel.getName())) {
//            holder.file_item_name.setText("...");
//            holder.file_item_image.setImageResource(R.drawable.file_directory);
//            holder.file_item_size.setText(mContext.getString(R.string.filelistadapter_superior_files));
//
//            return convertView;
//        }
//        holder.file_item_name.setText(fileModel.getName());
//        if (fileModel.isDirectory()) {
//            holder.file_item_image.setImageResource(R.drawable.file_directory);
//            holder.file_item_size.setText(String.format(mContext.getString(R.string.filelistadapter_file_item_size), fileModel.getFileSize(), TimeHelper.getDateStringString(fileModel.getCreateTime())));
//        } else {
//            holder.file_item_size.setText(FileHelper.formetFileSize(fileModel.getFileSize()) + "  " + TimeHelper.getDateStringString(fileModel.getCreateTime()));
//            switch (fileModel.getType()) {
//                case TypeFile.type_unknow:
//                    holder.file_item_image.setImageResource(R.drawable.file_unknow);
//                    break;
//                case TypeFile.type_zip:
//                    holder.file_item_image.setImageResource(R.drawable.file_cutdown);
//                    break;
//                case TypeFile.type_mp3:
//                    holder.file_item_image.setImageResource(R.drawable.file_voice);
//                    break;
//                case TypeFile.type_doc:
//                    holder.file_item_image.setImageResource(R.drawable.file_word);
//                    break;
//                case TypeFile.type_jpg:
////                    BitmapHelper.getBitmapUtils(mContext.getResources().getDrawable(R.drawable.file_image)).display(holder.file_item_image, fileModel.getPath());
//
//                    Picasso.with(mContext)
//                            .load(new File(fileModel.getPath()))
//                            .placeholder(R.drawable.file_image)
//                            .tag("FileListAdapter")
//                            .resize(144, 144)
//                            .centerCrop()
//                            .into(holder.file_item_image);
//
//
//// holder.file_item_image.setImageResource(R.drawable.file_image);
//                    break;
//                case TypeFile.type_pdf:
//                    holder.file_item_image.setImageResource(R.drawable.file_pdf);
//                    break;
//                case TypeFile.type_ppt:
//                    holder.file_item_image.setImageResource(R.drawable.file_ppt);
//                    break;
//                case TypeFile.type_pst:
//                    holder.file_item_image.setImageResource(R.drawable.file_unknow);
//                    break;
//                case TypeFile.type_class:
//                    holder.file_item_image.setImageResource(R.drawable.file_java);
//                    break;
//                case TypeFile.type_txt:
//                    holder.file_item_image.setImageResource(R.drawable.file_txt);
//                    break;
//                case TypeFile.type_mp4:
//                    holder.file_item_image.setImageResource(R.drawable.file_video);
//                    break;
//                case TypeFile.type_html:
//                    holder.file_item_image.setImageResource(R.drawable.file_html);
//                    break;
//                case TypeFile.type_xls:
//                    holder.file_item_image.setImageResource(R.drawable.file_excel);
//                    break;
//                case TypeFile.type_apk:
//                    Drawable drawable = FileHelper.getApkIcon(mContext, fileModel.getPath());
//                    if (null != drawable)
//                        holder.file_item_image.setImageDrawable(drawable);
//                    else
//                        holder.file_item_image.setImageResource(R.drawable.file_apk);
//                    break;
//
//            }
//        }
//
//        return convertView;
//    }
//
//    private class ViewHolder {
//        public ImageView file_item_image;
//        public TextView file_item_name;
//        public TextView file_item_size;
//    }
//}
