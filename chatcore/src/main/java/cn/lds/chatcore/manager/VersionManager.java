package cn.lds.chatcore.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.http.HttpHandler;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;
import cn.lds.chatcore.common.AppHelper;
import cn.lds.chatcore.common.ConnectivityHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.MD5checkUtil;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.VersionModel;
import cn.lds.chatcore.db.FilesTable;
import cn.lds.chatcore.db.VersionTable;
import cn.lds.chatcore.enums.FileType;
import cn.lds.chatcore.event.FileAvailableEvent;
import cn.lds.chatcore.event.FileDownloadErrorEvent;
import cn.lds.chatcore.event.FileDownloadingEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import de.greenrobot.event.EventBus;

/**
 * 版本管理
 *
 * @author user
 */
public class VersionManager {

    private final int cellularMobileNetwork = 1000;         //移动网络
    private final int isExit = 1001;                        //退出程序
    private final int downloadApi = 1002;                   //下载API请求
    public static final int sendCheckVersionApi = 1003;    //检测版本

    public boolean isCheckVersion = true;

    private int latestVersion = -1;
    private static VersionManager instance;
    private Context mContext;
    private boolean forceFlag = false;//是否强制升级 true 强制 false 不强制
    private View view;//显示popwindow的视图

    private String fileStorageId;//文件存储ID
    private String description;//描述
    private double fileSize;//文件大小
    private String versionName;//版本编号

    private Dialog dialog;
    private View processView;
    private HttpHandler<File> httpHandler;

    private boolean backKeyable = true; //返回按钮事件触发
    private boolean showTip = false;    //弹出提示（下载中,取消下载提示）
    private boolean sendApiFlag = true; //默认初始化 true
    private String oldMd5 = "";
    private OnUpdateVersionListener mOnUpdateVersionListener;

    public void setOnUpdateVersionListener(OnUpdateVersionListener onUpdateVersionListener){
        mOnUpdateVersionListener = onUpdateVersionListener;
    }
    public interface OnUpdateVersionListener{
        void forceUpdateVersionEvent(String url);
        void normalUpdateVersionEvent(String url);
        void NoUpdateVersionEvent();
    }
//	static {
//		instance = new VersionManager();
//		MyApplication.getInstance().addManager(instance);
//		EventBus.getDefault().register(instance);
//	}


    public void checkVersions(Context mContext,View view) {
        isCheckVersion = false;
        this.mContext = mContext;
        this.view = view;
        CoreHttpApi.checkVersions();

    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String fullUrl = String.valueOf(msg.obj);
            switch (msg.what) {
                case cellularMobileNetwork:
                    //流量
                    popDownload(fullUrl);
                    break;
                case isExit://退出
                    if (forceFlag) {
                        android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
                        System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
                    }
                    break;
                case downloadApi://下载API
                    if (ToolsHelper.isNull(fullUrl)) {
                        ToolsHelper.showStatus(mContext, false, mContext.getString(R.string.versionmanager_downloadurl_nonull));
                        break;
                    }
                    CoreHttpApi.fileDownload(versionName, fileStorageId, fullUrl, FileType.APK, mContext.getString(R.string.wxentryactivity_install_app));
                    break;
                case 9999://返回按钮监听（LoadingDialog）
                    if (showTip) {//再次点击提示，防止误操作中断下载
                        dialog.dismiss();
                        if (null != httpHandler)
                            httpHandler.cancel();
                        ToolsHelper.showStatus(mContext, true, mContext.getString(R.string.versionmanager_download_cancel));
                        backKeyable = true;
                        handler.obtainMessage(isExit).sendToTarget();
                    } else {
                        if (forceFlag) {
                            ToolsHelper.showInfo(mContext, mContext.getString(R.string.versionmanager_download_cancel_logout));
                        } else {
                            ToolsHelper.showInfo(mContext, mContext.getString(R.string.versionmanager_cancel_download));
                        }
                        showTip = true;
                    }
                    break;
                case sendCheckVersionApi:
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    view = (View) map.get("view");
                    mContext = (Context) map.get("activity");
                    checkVersion();
                    break;
            }
        }

    };

    public static VersionManager getInstance() {
        if (instance == null) {
            try {
                instance = new VersionManager();
                MyApplication.getInstance().addManager(instance);
                EventBus.getDefault().register(instance);
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    /**
     * 写入
     *
     * @param version
     * @param fileId
     * @param forceFlag
     */
    public void write(int version, int fileId, boolean forceFlag) {
        VersionTable table = new VersionTable(version, fileId, forceFlag);
        try {
            DbHelper.getDbUtils().deleteAll(VersionTable.class);
            DbHelper.getDbUtils().save(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地库中版本信息
     *
     * @return
     */
    public VersionTable get() {
        VersionTable versionTable = null;
        try {
            Selector selector = Selector.from(VersionTable.class);
            versionTable = DbHelper.getDbUtils().findFirst(selector);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionTable;
    }

    /**
     * 检查本地数据库是否存在新版本
     */
    public void checkVersion() {
//        this.mContext = MyApplication.getInstance();
//        this.view = view;
        this.forceFlag = false;

//        LoadingDialog.showDialog(this.mContext, "检查版本更新..");
        httpHandler = MyApplication.getInstance().handlerDownload.get("APP");
        if (null != httpHandler) {// httpHandler若不为空则表示已存在下载线程，检查更新到此返回。
            VersionManager.getInstance().setSendApiFlag(false);//调用一次，立即置为false，等待ping之后重置（大概俩分钟重置）
            return;
        }
//        CoreHttpApi.getLastestVersion(null);
    }

    /**
     * 下载
     */
    public void download() {
        //url设置
        String fullUrl = Constants.getCoreUrls().getDownloadUrl(fileStorageId);
        fullUrl = fullUrl + "?type=original";//设置下载type original-原图  standard-标准图  thumbnail-缩略图

        //清除本地包
        String sdcardPath = FileHelper.getSaveFilePath(FileType.APK);
        File file = new File(sdcardPath);
        deleteAllFile(file);//删除本地APK包以及文件夹

        //设置文件名称
        if (ToolsHelper.isNull(versionName)) {
            versionName = "lschat2.0.apk";
        } else {
            versionName = "lschat2.0-eimap_" + versionName + ".apk";
        }
        //检测网络
        if (!ConnectivityHelper.isConnected(MyApplication.getInstance())) {
            //无网络
            ToolsHelper.showStatus(mContext, false, mContext.getString(R.string.versionmanager_wifi_shutdown));
        } else if (ConnectivityHelper.isWifi(MyApplication.getInstance())) {
            //wifi
            downloading();
            handler.obtainMessage(downloadApi, fullUrl).sendToTarget();//异步操作
        } else {
            //移动网络
            handler.obtainMessage(cellularMobileNetwork, fullUrl).sendToTarget();//异步操作
        }
    }

    /**
     * 删除本地文件
     *
     * @param file
     * @return
     */
    private boolean deleteAllFile(File file) {
        if (file.isDirectory()) {//不是文件夹直接删除
            String[] children = file.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteAllFile(new File(file, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    /**
     * 新版本提示
     */
    public void popDialog(final String url) {
        String desc = mContext.getString(R.string.versionmanager_version_desc);
        PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                startWeb(url);
            }

            @Override
            public void cancel() {
                handler.obtainMessage(isExit).sendToTarget();
            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(String.format(desc)).show(view);
    }

    /**
     * 新版本强制提示
     */
    public void popForceDialog(final String url) {
//        String desc = "发现新版本:%s %n所占内存大小: %.0f %n点击确取升级,点击[取消]退出系统！";
        String desc = mContext.getString(R.string.versionmanager_version_desc_force);
        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                startWeb(url);
            }

            @Override
            public void cancel() {
                handler.obtainMessage(isExit).sendToTarget();
            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(String.format(desc)).setBackKey(false).show(view);
    }

    private void startWeb(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    /**
     * 下载流量提示
     */
    public void popDownload(final String url) {
        PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                downloading();//加载等待框
                handler.obtainMessage(downloadApi, url).sendToTarget();
            }

            @Override
            public void cancel() {
                handler.obtainMessage(isExit).sendToTarget();
            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(mContext.getString(R.string.versionmanager_gprs))
                .show(view);
    }

    /**
     * E014: 获取最新版本 getLastestVersion 请求成功处理
     *
     * @param event
     */
    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();
        if (!CoreHttpApiKey.checkVersions.equals(apiNo)) {
            return;
        }
        isCheckVersion = true;
        VersionModel versionModel = GsonImplHelp.get().toObject(httpResult.getResult(), VersionModel.class);
        if (versionModel.getData() != null && versionModel.getData().getVersionNo() > AppHelper.getVersionCode()) {
            forceFlag = versionModel.getData().isForceUpdate();
            if (forceFlag) {
                mOnUpdateVersionListener.forceUpdateVersionEvent(versionModel.getData().getDownloadUrl());
            } else {
                mOnUpdateVersionListener.normalUpdateVersionEvent(versionModel.getData().getDownloadUrl());
            }
        }else{
            mOnUpdateVersionListener.NoUpdateVersionEvent();
        }

        LoadingDialog.dismissDialog();
    }

    /**
     * E014: 获取最新版本
     *
     * @param httpResult
     */
    private void E014(HttpResult httpResult) {
        JSONObject jsonObject = httpResult.getJsonResult();

        JSONObject jsonData = jsonObject.optJSONObject("data");
        if (jsonData == null) {
            return;
        }
        String fileStorageId = jsonData.optString("fileId");
        String description = jsonData.optString("description");
        double fileSize = jsonData.optDouble("fileSize", 0);
        String forceUpdate = jsonData.optString("forceUpdate");
        String versionName = jsonData.optString("versionName");
        oldMd5 = jsonData.optString("md5");
        int versionNo = jsonData.optInt("versionNo");
        int localVersion = AppHelper.getVersionCode();//注意 版本号至少1，没有0
//        int localVersion = 0;

        //服务器版本比对
        if (versionNo > localVersion) {
            VersionTable table = this.get();
            FilesTable filesTable = FileManager.getInstance().findByOwnerAndFileType("APP", FileType.APK);
            //本地数据库版本比对
            if (null != table && table.getVersion() > localVersion) {
                forceFlag = table.isForceUpdate();
                installPop(filesTable);//直接弹出安装提示
            } else {
                this.fileStorageId = fileStorageId;
                this.description = description;
                this.fileSize = fileSize;
                this.latestVersion = versionNo;
                this.versionName = versionName;

                if ("0".equals(forceUpdate)) {//不强制更新
//                    popDialog();
                    forceFlag = false;
                } else if ("1".equals(forceUpdate)) {//强制更新
//                    popForceDialog();
                    backKeyable = false;//即将弹出提示时
                    forceFlag = true;
                }
            }
        }
    }


    /**
     * E014: 获取最新版本 getLastestVersion 失败处理
     *
     * @param event
     */

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();
        if (!CoreHttpApiKey.getLastestVersion.equals(apiNo)) {
            return;
        }
        setSendApiFlag(true);
        ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
        LoadingDialog.dismissDialog();
    }

    /**
     * 文件下载成功，FileManager处理后
     *
     * @param event
     */
    public void onEventMainThread(FileAvailableEvent event) {

        FilesTable table = event.getFilesTable();
        if (null != table && !FileType.APK.name().equals(table.getFileType()))
            return;
        dialog.dismiss();
        //重置下载线程
        MyApplication.getInstance().handlerDownload.remove("APP");
        httpHandler = null;

        backKeyable = true;

        installPop(table);
    }

    /**
     * 下载中进程监听
     *
     * @param event
     */
    public void onEventMainThread(FileDownloadingEvent event) {

        if (!FileType.APK.equals(event.getFileType()))
            return;

        int percent = event.getProgress();
        if (null == httpHandler)
            httpHandler = MyApplication.getInstance().handlerDownload.get("APP");
        LoadingDialog.updateCircleProgressDialog(percent, dialog, processView);
    }

    /**
     * 下载中，等待框加载
     */
    private void downloading() {
        dialog = LoadingDialog.getCircleProgressDialog(mContext);
        processView = LoadingDialog.getProgressViewByTextView(mContext);
        LoadingDialog.showCircleProgressDialog(mContext, dialog, processView, handler, mContext.getString(R.string.versionmanager_downloading));
    }

    /**
     * 安装前校验MD5
     *
     * @param table
     */
    private void installPop(final FilesTable table) {
        checkMd5(table);
    }

    /**
     * 校验MD5
     *
     * @param table
     */
    private void checkMd5(FilesTable table) {
        String newMd5 = null;
        File file = new File(table.getPath());
        try {
            newMd5 = MD5checkUtil.getInstance().getFileMD5String(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!ToolsHelper.isNull(newMd5) && newMd5.equals(oldMd5)) {
            int fileId = Integer.parseInt(table.getFileStorageId());
            // 存入File表
            this.write(latestVersion, fileId, forceFlag);
            //安装App
            installApp(table.getPath());
            handler.obtainMessage(isExit).sendToTarget();
        } else {
            //删除损坏文件，
            FileHelper.deleteFile(table.getPath());
            PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
                @Override
                public void confirm() {
                    download();
                }

                @Override
                public void cancel() {
                    handler.obtainMessage(isExit).sendToTarget();
                }

                @Override
                public void onItemListener(int position) {

                }
            }).setContentTx(mContext.getString(R.string.versionmanager_filesbroken_redownload)).setBackKey(false).show(view);
        }
    }

    /**
     * 安装应用
     *
     * @param path
     */
    private void installApp(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }


    /**
     * 文件下载失败处理
     *
     * @param event
     */
    public void onEventBackgroundThread(FileDownloadErrorEvent event) {

        if (null != event && !FileType.APK.name().equals(event.getFileType()))
            return;

        dialog.dismiss();
        ToolsHelper.showStatus(mContext, false, event.getErrorMsg());
        handler.sendEmptyMessageDelayed(isExit, 500);
    }

    /**
     * 获取手机返回键的可触发事件状态
     *
     * @return
     */
    public boolean getBackKeyable() {
        return this.backKeyable;
    }

    /**
     * 获取属性 sendApiFlag
     */
    public boolean getSendApiFlag() {
        return this.sendApiFlag;
    }

    public void setSendApiFlag(boolean sendApiFlag) {
        this.sendApiFlag = sendApiFlag;
    }


}
