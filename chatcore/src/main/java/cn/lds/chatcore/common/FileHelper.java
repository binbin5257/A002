package cn.lds.chatcore.common;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.lds.chatcore.R;
import cn.lds.chatcore.data.DataMenuModel;
import cn.lds.chatcore.data.DataPhotoModel;
import cn.lds.chatcore.data.FileModel;
import cn.lds.chatcore.enums.FileType;

public class FileHelper {

    public static final String PICTURE_FILE_SUFFIX = ".eucppic";
    public static final String RECORD_FILE_SUFFIX = ".amr";// 语音文件后缀
    public static final String RTF_IMAGE_JPG = ".jpg";// 图片文件后缀
    public static final String RTF_IMAGE_PNG = ".png";// 图片文件后缀
    public static final String RTF_VIDEO = ".mp4";// 视频文件后缀

    public static final String rootPathName = AppIndependentConfigure.SYS_CONFIG_APP_PACKAGE;// 根目录
    public static final String avatarPathName = "avatar";// 头像文件存放本地目录
    public static final String carPathName = "car";// 车辆文件存放本地目录
    public static final String chatPathName = "chat";// 聊天产生文件存放根目录
    public static final String downloadPathName = "Download";// 下载文件存放根目录
    public static final String recieveFilesPathName = "RecieveFiles";// 聊天文件接收文件存放目录
    public static final String sendFilesPathName = "SendFiles";// 聊天文件发送文件目录
    public static final String imagePathName = "image";// 图片文件目录
    public static final String voicePathName = "voice";// 音频文件
    public static final String videoPathName = "video";// 视频文件
    public static final String apkPathName = "apk";// apk文件
    public static final String filePathName = "files";// apk文件
    public static final String otherPathName = "other";// 不可知因素产生特殊文件存放目录！

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    /**
     * 获取文件根目录
     *
     * @param
     * @return
     */
    public static String getRootFilePath() {
        StringBuffer stbPath = new StringBuffer();
        stbPath.append(Environment.getExternalStorageDirectory().getPath());
        stbPath.append(File.separator);
        stbPath.append(rootPathName);
        stbPath.append(File.separator);
        return stbPath.toString();
    }

    /**
     * 聊天中接收到文件 保存路径 根据filetype获取 其中apk为更新应用时apk文件保存地址
     *
     * @return
     */
    public static String getSaveFilePath(FileType fileType) {
        StringBuffer stbPath = new StringBuffer();
        if (existSDCard()) {

            stbPath.append(Environment.getExternalStorageDirectory().getPath());
            stbPath.append(File.separator);
        }
        stbPath.append(rootPathName);
        stbPath.append(File.separator);
        if (FileType.IMAGE.equals(fileType)) {
            stbPath.append(chatPathName);
            stbPath.append(File.separator);
            stbPath.append(CacheHelper.getAccount());
            stbPath.append(File.separator);
            stbPath.append(recieveFilesPathName);
            stbPath.append(File.separator);
            stbPath.append(imagePathName);
            stbPath.append(File.separator);
        } else if (FileType.VOICE.equals(fileType)) {
            stbPath.append(chatPathName);
            stbPath.append(File.separator);
            stbPath.append(CacheHelper.getAccount());
            stbPath.append(File.separator);
            stbPath.append(recieveFilesPathName);
            stbPath.append(File.separator);
            stbPath.append(voicePathName);
            stbPath.append(File.separator);
        } else if (FileType.VEDIO.equals(fileType)) {
            stbPath.append(chatPathName);
            stbPath.append(File.separator);
            stbPath.append(CacheHelper.getAccount());
            stbPath.append(File.separator);
            stbPath.append(recieveFilesPathName);
            stbPath.append(File.separator);
            stbPath.append(videoPathName);
            stbPath.append(File.separator);
        } else if (FileType.APK.equals(fileType)) {
            stbPath.append(downloadPathName);
            stbPath.append(File.separator);
            stbPath.append(apkPathName);
            stbPath.append(File.separator);
        } else if (FileType.FILE.equals(fileType)) {
            stbPath.append(downloadPathName);
            stbPath.append(File.separator);
            stbPath.append(filePathName);
            stbPath.append(File.separator);
            stbPath.append(UUID.randomUUID());
            stbPath.append(File.separator);
        } else {
            stbPath.append(downloadPathName);
            stbPath.append(File.separator);
            stbPath.append(otherPathName);
            stbPath.append(File.separator);
        }

        return stbPath.toString();
    }

    /**
     * 保存图片到本地
     *
     * @return
     */
    public static String getSaveBitmapPath() {
        StringBuffer stbPath = new StringBuffer();
        if (existSDCard()) {

            stbPath.append(Environment.getExternalStorageDirectory().getPath());
            stbPath.append(File.separator);
        }
        stbPath.append(rootPathName);
        stbPath.append(File.separator);
        stbPath.append(chatPathName);
        stbPath.append(File.separator);
        stbPath.append(imagePathName);
        stbPath.append(File.separator);

        return stbPath.toString() + getpicname();
    }

    /**
     * 聊天中发送文件路径 语音 视频 图片
     *
     * @param
     * @return
     */
    public static String getSendFilePath(FileType fileType) {
        StringBuffer stbPath = new StringBuffer();
        if (existSDCard()) {
            stbPath.append(Environment.getExternalStorageDirectory().getPath());
            stbPath.append(File.separator);
        }
        stbPath.append(rootPathName);
        stbPath.append(File.separator);

        if (FileType.IMAGE.equals(fileType)) {
            stbPath.append(chatPathName);
            stbPath.append(File.separator);
            stbPath.append(CacheHelper.getAccount());
            stbPath.append(File.separator);
            stbPath.append(sendFilesPathName);
            stbPath.append(File.separator);
            stbPath.append(imagePathName);
            stbPath.append(File.separator);
        } else if (FileType.VOICE.equals(fileType)) {
            stbPath.append(chatPathName);
            stbPath.append(File.separator);
            stbPath.append(CacheHelper.getAccount());
            stbPath.append(File.separator);
            stbPath.append(sendFilesPathName);
            stbPath.append(File.separator);
            stbPath.append(voicePathName);
            stbPath.append(File.separator);
        } else if (FileType.VEDIO.equals(fileType)) {
            stbPath.append(chatPathName);
            stbPath.append(File.separator);
            stbPath.append(CacheHelper.getAccount());
            stbPath.append(File.separator);
            stbPath.append(sendFilesPathName);
            stbPath.append(File.separator);
            stbPath.append(videoPathName);
            stbPath.append(File.separator);
        } else {
            stbPath.append(chatPathName);
            stbPath.append(File.separator);
            stbPath.append(CacheHelper.getAccount());
            stbPath.append(File.separator);
            stbPath.append(sendFilesPathName);
            stbPath.append(File.separator);
            stbPath.append(otherPathName);
            stbPath.append(File.separator);
        }
        return stbPath.toString();
    }

    /**
     * 检查内存卡是否可用
     *
     * @return
     */
    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 根据路径创建文件夹
     *
     * @param filePath
     * @return
     */
    public static boolean createDirectory(String filePath) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return file.mkdirs();

    }

    /**
     * 递归删除文件和文件夹
     *
     * @param path 要删除的根目录
     */
    public static void deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f.getAbsolutePath());
                }
                file.delete();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 调用系统相机图片存储位置 生成path
     *
     * @return
     */
    public static String getTakePhotoPath() {
        return getSendFilePath(FileType.IMAGE) + getpicname();
    }

    /**
     * 随机生成图片文件名 文件名是当前时间加六位随机数
     *
     * @return
     */
    public static String getpicname() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        int i = (int) (Math.random() * (999999 - 100000) + 100000);

        return str + String.valueOf(i) + RTF_IMAGE_JPG;
    }

    /**
     * 调用系统摄像时文件path
     *
     * @return
     */
    public static String getTakeVideoPath() {
        return getSendFilePath(FileType.VEDIO) + getfilename();
    }

    /**
     * 视频文件名获取 文件名是当前时间加六位随机数
     *
     * @return
     */
    public static String getfilename() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        int i = (int) (Math.random() * (999999 - 100000) + 100000);

        return str + String.valueOf(i) + RTF_VIDEO;
    }

    /**
     * 随机生成语音文件path
     *
     * @return
     */
    public static String getTakeVoicePath() {
        return getSendFilePath(FileType.VOICE) + getvoicename();
    }

    /**
     * 随机生成语音文件名 文件名是当前时间加六位随机数
     *
     * @return
     */
    public static String getvoicename() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        int i = (int) (Math.random() * (999999 - 100000) + 100000);

        return str + String.valueOf(i) + RECORD_FILE_SUFFIX;
    }

    /**
     * 头像存放地址
     *
     * @return
     */
    public static String getTakeAvatarPath() {
        StringBuffer stbPath = new StringBuffer();
        if (existSDCard()) {

            stbPath.append(Environment.getExternalStorageDirectory().getPath());
            stbPath.append(File.separator);
        }
        stbPath.append(rootPathName);
        stbPath.append(File.separator);
        stbPath.append(downloadPathName);
        stbPath.append(File.separator);
        stbPath.append(avatarPathName);
        stbPath.append(File.separator);
        stbPath.append(Constants.PHOTO_FILE_NAME);
        return stbPath.toString();
    }

    /**
     * 车辆图片存放地址
     *
     * @return
     */
    public static String getTakeCarPath() {
        StringBuffer stbPath = new StringBuffer();
        if (existSDCard()) {
            stbPath.append(Environment.getExternalStorageDirectory().getPath());
            stbPath.append(File.separator);
        }
        stbPath.append(rootPathName);
        stbPath.append(File.separator);
        stbPath.append(downloadPathName);
        stbPath.append(File.separator);
        stbPath.append(carPathName);
        stbPath.append(File.separator);
        stbPath.append(Constants.CAR_FILE_NAME);
        return stbPath.toString();
    }

    /**
     * 根据path获取文件名的方法
     *
     * @param path
     * @return
     */
    public static String getFileNameFromPath(String path) {
        // String name = url.lastIndexOf(File.separator) + 1
        int start = path.lastIndexOf(File.separator) + 1;
        return path.substring(start);
    }

    /**
     * 文件转货 Base64 的方法
     *
     * @param fileName
     * @return
     */
    public static byte[] getFilebyte(String fileName) {
        byte[] bytes = null;
        try {
            InputStream in = new FileInputStream(fileName);
            // in.available()返回文件的字节长度
            bytes = new byte[in.available()];
            // 将文件中的内容读入到数组中
            in.read(bytes);
            in.close();
        } catch (Exception e) {
        }
        return bytes;
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    public static long getFileSize(String path) throws Exception {
        File file = new File(path);
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 转换文件大小
     */
    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小
     *
     * @param fileSizeStr
     * @return
     */
    public static String formetFileSize(String fileSizeStr) {
        try {
            long fileSize = Long.valueOf(fileSizeStr);
            return formetFileSize(fileSize);
        } catch (NumberFormatException e) {
            return "";
        }
    }

    /**
     * 复制文件
     *
     * @param sourceFile 源文件
     * @param targetFile 复制目的文件
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    /**
     * 打开文件
     *
     * @param file
     */
    public static void openFile(Context activity, File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        // 获取文件file的MIME类型
        String type = getMIMEType(file);
        // 设置intent的data和Type属性。
        intent.setDataAndType(/* uri */Uri.fromFile(file), type);
        // 跳转
        activity.startActivity(intent);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    @SuppressLint("DefaultLocale")
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            return type;
        }
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
            }
        }
        return type;
    }

    public static final String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"},
            {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"},
            {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"}, {".eucppic", "image/jpeg"}, {".js", "application/x-javascript"},
            {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"}, {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"},
            {"", "*/*"}};

    /**
     * 根据文件后缀名获得对应的文件类型。
     *
     * @param
     */
    @SuppressLint("DefaultLocale")
    public static int getFileTypeIcon(String fName) {
        int iconLevel = 0;

        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            iconLevel = 0;
            return iconLevel;
        }

		/* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            iconLevel = 0;
            return iconLevel;
        }

        // 获取文件的Icon
        for (int i = 0; i < FILE_ICON_Table.length; i++) {
            for (int j = 0; j < FILE_ICON_Table[i].length; j++) {
                if (end.equals(FILE_ICON_Table[i][j])) {
                    iconLevel = i;
                }
            }
        }

        return iconLevel;
    }

    public static final String[][] FILE_ICON_Table = {
            // {后缀名，文件类型}
            {""}, // 0 -- file_default_icon
            {".7z", ".gtar", ".gz", ".jar", ".rar", ".tar", ".tgz", ".zip"}, // 1
            // --
            // file_compress_icon
            {".m3u", ".m4a", ".m4b", ".mp2", ".mp3", ".mpga", ".ogg", ".wav", ".wma", ".ra", ".mid", ".ape", ".flac", ".amr"}, // 2
            // --
            // file_audio_icon
            {".doc", ".docx"}, // 3 -- file_doc_icon
            {".bmp", ".gif", ".jpeg", ".jpg", ".png"}, // 4 -- file_image_icon
            {".pdf"}, // 5 -- file_pdf_icon
            {".pps", ".ppt", ".pptx"}, // 6 -- file_ppt_icon
            {".pst"}, // 7 -- file_pdf_icon
            {".bin", ".conf", ".class", ".exe", ".prop", "properties", ".mpc", ".sh", ".rc"}, // 8
            // --
            // file_setting_icon
            {".c", ".cpp", ".h", ".java", ".log", ".msg", ".rtf", ".txt", ".js", ".xml"}, // 9
            // --
            // file_txt_icon
            {".3gp", ".asf", ".avi", ".m4u", ".m4v", ".mov", ".mp4", ".mkv", ".mpe", ".mpeg", ".mpg", ".mpg4",
                    ".rmvb", ".rm", ".flv", ".wmv", ".vob"}, // 10 --
            // file_video_icon
            {".htm", ".html"}, // 11 -- file_web_icon
            {".xls", ".xlsx"}, // 12 -- file_xls_icon
            {".apk"}, // 13 -- apk
    };

    /*
         * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过
         * appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
         * http://code.google.com/p/android/issues/detail?id=9151
         */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                LogHelper.e("获取APK图标", e);
            }
        }
        return null;
    }

    /**
     * 遍历所有图片文件
     *
     * @param context
     * @return
     */
    public static ArrayList<DataPhotoModel> queryAllImage(final Context context) {
        if (context == null) { // 判断传入的参数的有效性
            return null;
        }
        ArrayList<DataPhotoModel> images = new ArrayList<DataPhotoModel>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            // 查询数据库，参数分别为（路径，要查询的列名，条件语句，条件参数，排序）
            cursor = resolver.query(Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    DataPhotoModel image = new DataPhotoModel();
                    image.setId(cursor.getInt(cursor.getColumnIndex(Images.Media._ID))); // 获取唯一id
                    image.setFilePath(cursor.getString(cursor.getColumnIndex(Images.Media.DATA))); // 文件路径
                    image.setFileName(cursor.getString(cursor.getColumnIndex(Images.Media.DISPLAY_NAME))); // 文件名
                    image.setParentName(cursor.getString(cursor.getColumnIndex(Images.Media.BUCKET_DISPLAY_NAME))); // 父文件夹名
                    image.setFiletype(FileHelper.TYPE_IMAGE);
                    image.setSelect(false);
                    // ... 还有很多属性可以设置
                    // 可以通过下一行查看属性名，然后在Images.Media.里寻找对应常量名

                    // 获取缩略图（如果数据量大的话，会很耗时——需要考虑如何开辟子线程加载）
                    /*
                     * 可以访问android.provider.MediaStore.Images.Thumbnails查询图片缩略图
					 * Thumbnails下的getThumbnail方法可以获得图片缩略图
					 * ，其中第三个参数类型还可以选择MINI_KIND
					 */
//					Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(resolver, image.getId(),
//							Images.Thumbnails.MICRO_KIND, null);
//					image.setThumbnail(thumbnail);

                    images.add(image);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return images;
    }

    /**
     * 遍历所有视频文件
     *
     * @param context
     * @return
     */
    public static ArrayList<DataPhotoModel> queryAllVideo(final Context context) {
        if (context == null) { // 判断传入的参数的有效性
            return null;
        }
        ArrayList<DataPhotoModel> videos = new ArrayList<DataPhotoModel>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            // 查询数据库，参数分别为（路径，要查询的列名，条件语句，条件参数，排序）
            cursor = resolver.query(Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    DataPhotoModel video = new DataPhotoModel();
                    video.setId(cursor.getInt(cursor.getColumnIndex(Video.Media._ID))); // 获取唯一id
                    video.setFilePath(cursor.getString(cursor.getColumnIndex(Video.Media.DATA))); // 文件路径
                    video.setFileName(cursor.getString(cursor.getColumnIndex(Video.Media.DISPLAY_NAME))); // 文件名
                    video.setParentName(cursor.getString(cursor.getColumnIndex(Video.Media.MIME_TYPE))); // 所在文件夹名字
                    video.setFiletype(FileHelper.TYPE_VIDEO);
                    video.setSelect(false);
                    // ... 还有很多属性可以设置
                    // 可以通过下一行查看属性名，然后在Video.Media.里寻找对应常量名

                    // 获取缩略图（如果数据量大的话，会很耗时——需要考虑如何开辟子线程加载）
                    /*
                     * 可以访问android.provider.MediaStore.Video.Thumbnails查询图片缩略图
					 * Thumbnails下的getThumbnail方法可以获得图片缩略图
					 * ，其中第三个参数类型还可以选择MINI_KIND
					 */
//					Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(resolver, video.getId(),
//							Video.Thumbnails.MICRO_KIND, null);

                    videos.add(video);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return videos;
    }

    /**
     * 创建选择图片菜单数据
     *
     * @param context
     * @return
     */
    public static ArrayList<DataMenuModel> getMenus(final Context context) {
        if (context == null) { // 判断传入的参数的有效性
            return null;
        }

        ArrayList<DataMenuModel> menus = new ArrayList<DataMenuModel>();
        HashMap<String, Integer> types = new HashMap<String, Integer>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            // 查询数据库，参数分别为（路径，要查询的列名，条件语句，条件参数，排序）
            cursor = resolver.query(Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Images.Media.DATE_ADDED + " DESC");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    DataPhotoModel image = new DataPhotoModel();
                    image.setId(cursor.getInt(cursor.getColumnIndex(Images.Media._ID))); // 获取唯一id
                    image.setFilePath(cursor.getString(cursor.getColumnIndex(Images.Media.DATA))); // 文件路径
                    image.setFileName(cursor.getString(cursor.getColumnIndex(Images.Media.DISPLAY_NAME))); // 文件名
                    image.setParentName(cursor.getString(cursor.getColumnIndex(Images.Media.BUCKET_DISPLAY_NAME))); // 父文件夹名
                    image.setFiletype(FileHelper.TYPE_IMAGE);
                    image.setSelect(false);
                    // ... 还有很多属性可以设置
                    // 可以通过下一行查看属性名，然后在Images.Media.里寻找对应常量名

                    // 获取缩略图（如果数据量大的话，会很耗时——需要考虑如何开辟子线程加载）
                    /*
                     * 可以访问android.provider.MediaStore.Images.Thumbnails查询图片缩略图
					 * Thumbnails下的getThumbnail方法可以获得图片缩略图
					 * ，其中第三个参数类型还可以选择MINI_KIND
					 */
//					Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(resolver, image.getId(),
//							Images.Thumbnails.MICRO_KIND, null);
//					image.setThumbnail(thumbnail);

                    if (menus.size() < 1) {
                        Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(resolver, image.getId(),
                                Images.Thumbnails.MICRO_KIND, null);
                        ArrayList<DataPhotoModel> data = new ArrayList<DataPhotoModel>();
                        data.add(image);
                        menus.add(new DataMenuModel(0, context.getString(R.string.photo_select_menu_default), true,
                                data, thumbnail));
                        ArrayList<DataPhotoModel> datas = new ArrayList<DataPhotoModel>();
                        datas.add(image);

                        menus.add(new DataMenuModel(1, image.getParentName(), false, datas, thumbnail));
                        types.put(image.getParentName(), menus.size() - 1);
                    } else {
                        DataMenuModel model = menus.get(0);
                        menus.remove(0);
                        ArrayList<DataPhotoModel> d = model.getDatas();
                        d.add(image);
                        model.setDatas(d);
                        menus.add(0, model);

                        if (types.containsKey(image.getParentName())) {
                            menus.get(types.get(image.getParentName())).getDatas().add(image);

                        } else {
                            ArrayList<DataPhotoModel> data = new ArrayList<DataPhotoModel>();
                            data.add(image);
                            Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(resolver, image.getId(),
                                    Images.Thumbnails.MICRO_KIND, null);
                            menus.add(new DataMenuModel(menus.size() - 1, image.getParentName(), false, data, thumbnail));
                            types.put(image.getParentName(), menus.size() - 1);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        ArrayList<DataPhotoModel> d = queryAllVideo(context);
        if (null != d && d.size() > 0) {
            menus.get(0).getDatas().addAll(d);

            Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(resolver, d.get(0).getId(),
                    Video.Thumbnails.MICRO_KIND, null);

            menus.add(new DataMenuModel(menus.size() - 1, context.getString(R.string.photo_select_menu_allvideo),
                    false, d, thumbnail));
            types.put(context.getString(R.string.photo_select_menu_allvideo), menus.size() - 1);
        }
        return menus;
    }

    /**
     *  * Try to return the absolute file path from the given Uri
     *  *
     *  * @param context
     *  * @param uri
     *  * @return the file path or null
     *  
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 将String数据存为文件
     */
    public static File getFileFromBytes(String name, String path) {
        byte[] b = name.getBytes();
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(path);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            LogHelper.e("字符串写入到文本文件中", e);
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            LogHelper.e("字符串写入到文本文件中", e);
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            LogHelper.e("字符串写入到文本文件中", e);
        }
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 获取文件大小  文件夹则返回子文件数量
     *
     * @param file
     * @return
     */
    public static long getChildCount(File file) {
        if (!file.exists()) {
            return 0;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return 0;
            } else {
                return files.length;
            }
        }
        return file.length();
    }

    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static List<FileModel> getAudiosFromMedia(Context context) {
        ContentResolver resolver = context.getContentResolver();
        List<FileModel> audios = new ArrayList<FileModel>();
        Cursor c = null;
        try {
            c = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径

                if (!FileHelper.isExists(path)) {
                    continue;
                }

                int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时间
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));// 大小
//                long date = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));

                audios.add(new FileModel(path, name, duration, size));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return audios;

    }

    public static List<FileModel> getVideosFromMedia(Context context) {

        List<FileModel> videos = new ArrayList<FileModel>();
        ContentResolver resolver = context.getContentResolver();

        Cursor c = null;
        try {
            // String[] mediaColumns = { "_id", "_data", "_display_name",
            // "_size", "date_modified", "duration", "resolution" };
            c = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));// 路径
                if (!isExists(path)) {
                    continue;
                }

//                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));// 大小
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 歌曲名
//                String resolution = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)); //
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));// 大小
                long duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));// 时间
//                long date = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));


                videos.add(new FileModel(path, name, duration, size));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return videos;
    }

    /**
     * 获取图片地址列表
     *
     * @return list
     */
    public static List<FileModel> getImagesFromMedia(Context context) {

        List<FileModel> pictures = new ArrayList<FileModel>();
        ContentResolver resolver = context.getContentResolver();
        Cursor c = null;
        try {
            c = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "_size", "_display_name"}, null, null, null);
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                if (!isExists(path)) {
                    continue;
                }
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                String name = c.getString(c.getColumnIndex(Images.Media.DISPLAY_NAME));

                pictures.add(new FileModel(path, name, 0, size));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return pictures;
    }

    /**
     * 获取下载路径
     *
     * @return
     */
    public static String getDownloadPath() {
        String downloadPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Download";
        File dir = new File(downloadPath);

        if (!dir.exists()) {
            downloadPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "download";
            dir = new File(downloadPath);
        }
        dir.mkdirs();
        System.out.println("下载路径:" + dir.getAbsolutePath());
        return downloadPath;
    }

    public static List<FileModel> getDownloads() {
        List<FileModel> downloads = new ArrayList<FileModel>();
        String downloadPath = getDownloadPath();
        File dir = new File(downloadPath);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        downloads.add(new FileModel(file.getCanonicalPath(), file.getName(), file.lastModified(), file.length()));
                    } catch (IOException e) {
                        LogHelper.e("下载列表", e);
                    }
                }
            }
        }
        return downloads;
    }

    public static boolean isDocument(String path) {
        path = path.toLowerCase();
        boolean flag = false;
        if (path.endsWith(".txt") || path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".xls") || path.endsWith(".xlsx")
                || path.endsWith(".ppt") || path.endsWith(".pptx") || path.endsWith(".xml") || path.endsWith(".html") || path.endsWith(".htm")) {
            return true;
        }

        return flag;
    }

    public static boolean isApk(String path) {
        path = path.toLowerCase();
        boolean flag = false;
        if (path.endsWith(".apk")) {
            return true;
        }
        return flag;
    }

    public static boolean isZip(String path) {
        path = path.toLowerCase();
        boolean isZip = false;
        if (path.endsWith(".zip") || path.endsWith(".rar") || path.endsWith(".tar") || path.endsWith(".gz")) {
            return true;
        }

        return isZip;
    }
}
