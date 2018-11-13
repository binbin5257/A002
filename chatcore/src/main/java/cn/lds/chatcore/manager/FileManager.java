package cn.lds.chatcore.manager;

import android.widget.ImageView;

import com.lidroid.xutils.db.sqlite.Selector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.GraphicHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.HttpHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.UploadModel;
import cn.lds.chatcore.db.FilesTable;
import cn.lds.chatcore.enums.FileType;
import cn.lds.chatcore.event.FileAvailableEvent;
import cn.lds.chatcore.event.FileDownloadErrorEvent;
import cn.lds.chatcore.event.FileDownloadedEvent;
import cn.lds.chatcore.event.FileUploadedEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import de.greenrobot.event.EventBus;

/**
 * 文件管理
 */
public class FileManager extends AbstractManager {

    protected static FileManager instance;

    //private DbUtils dbUtils = DbHelper.getDbUtils();
    /* 异步下载图片时，等待图片下载完成后，在相应的ImageView中显示 */
    Map<String, ImageView> waitDownloadingAvatar = new HashMap<>();

//    static {
//        instance = new FileManager();
//        MyApplication.getInstance().addManager(instance);
//        EventBus.getDefault().register(instance);
//    }

    public static FileManager getInstance() {
        if (instance == null) {
            try {
                instance = new FileManager();
                MyApplication.getInstance().addManager(instance);
                EventBus.getDefault().register(instance);
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    /**
     * 根据表ID获取记录
     *
     * @param id 表id
     * @return
     */
    public FilesTable findById(int id) {
        FilesTable table = null;

        try {
            table = DbHelper.getDbUtils().findById(FilesTable.class, id);
        } catch (Exception e) {
            LogHelper.e("", e);
        }
        return table;
    }

    public FilesTable findByOwner(String owner) {
        FilesTable table = null;
        try {
            table = DbHelper.getDbUtils().findFirst(Selector.from(FilesTable.class).where("owner", "=", owner));
        } catch (Exception e) {
            LogHelper.e("", e);
        }
        return table;
    }

    /**
     * 删除文件
     *
     * @return
     */
    public void delete(String file_storage_id) {
        try {
            DbHelper.getDbUtils().execNonQuery("DELETE FROM files WHERE file_storage_id='" + file_storage_id + "' ");
        } catch (Exception e) {
            LogHelper.e(this.getClass().getName(), e);
        }
    }

    /**
     * 删除文件
     *
     * @return
     */
    public void deleteByFileTypeAndOwner(String file_type, String owner) {
        try {
            DbHelper.getDbUtils().execNonQuery("DELETE FROM files WHERE file_type='" + file_type + "' and owner='" + owner + "'");
        } catch (Exception e) {
            LogHelper.e(this.getClass().getName(), e);
        }
    }

    public FilesTable findByOwnerAndFileType(String owner, FileType fileType) {
        FilesTable table = null;
        try {
            table = DbHelper.getDbUtils().findFirst(Selector.from(FilesTable.class).where("owner", "=", owner).and("file_type",
                    "=", fileType.name()));
        } catch (Exception e) {
            LogHelper.e("", e);
        }
        return table;
    }

    /**
     * 插入表
     *
     * @param fileStorageId
     * @param path
     * @param fileType
     * @param size
     * @param duration
     * @param owner
     * @return
     */
    public FilesTable insert(String fileStorageId, String path, FileType fileType, long size,
                             long duration, String owner) {

        FilesTable table = new FilesTable();
        table.setFileStorageId(fileStorageId);
        table.setPath(path);
        table.setFileType(fileType.name());
        table.setSize(size);
        table.setDuration(duration);
        table.setOwner(owner);

        try {
//            if (FileType.APK.equals(fileType) && "APP".equals(owner)) {
//                this.deleteByFileTypeAndOwner(fileType.name(), owner);
//            } else {
//                this.delete(fileStorageId);
//            }

            DbHelper.getDbUtils().saveBindingId(table);
        } catch (Exception e) {
            LogHelper.e("", e);
        }
        return table;
    }

    public FilesTable clone(String from, String to) {
        FilesTable old = findByOwner(from);

        FilesTable table = new FilesTable();
        table.setFileStorageId(old.getFileStorageId());
        table.setPath(old.getPath());
        table.setFileType(old.getFileType());
        table.setSize(old.getSize());
        table.setDuration(old.getDuration());
        table.setOwner(to);

        try {
            DbHelper.getDbUtils().saveBindingId(table);
        } catch (Exception e) {
            LogHelper.e("", e);
        }
        return table;
    }


    /**
     * 订阅 ：
     * 1、上传文件第一步，获取上传路径完成
     * 2、”注册存储记录并执行内容标准化处理“完成
     * <p/>
     * 获取上传路径完成后，紧接着上传文件
     *
     * @param event
     */
    public void onEventBackgroundThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();

        if (!(CoreHttpApiKey.getUploadUrl.equals(apiNo) || CoreHttpApiKey.registerFile.equals(apiNo)))
            return;

        LogHelper.d("upload>>>> get upload url is completed, and start to upload file");

        switch (apiNo) {
            case CoreHttpApiKey.getUploadUrl:
                getUploadUrlHandler(httpResult);
                break;
            case CoreHttpApiKey.registerFile:
                //registerFileHandler(httpResult);
                break;
        }

    }

    public void uploadFile(String key, String path) {
    }

    /**
     * 获取上传路径完成后，准备上传文件
     *
     * @param httpResult
     */
    private void getUploadUrlHandler(HttpResult httpResult) {
        // 获取跟踪参数
        Map<String, String> extras = httpResult.getExtras();
        //本地文件路径，通过“文件上传”第一步传递过来的
        String filePath = extras.get("filePath");
        try {
            JSONObject data = httpResult.getJsonResult().getJSONObject("data");
            String uploadUrl = data.getString("url");

            //第二步：上传文件
            CoreHttpApi.fileUpload(uploadUrl, filePath, extras);
        } catch (JSONException e) {
            LogHelper.e(this.getClass().getName(), e);
        }
    }


    /**
     * 文件上传完成事件
     * <p/>
     * 文件上传成功后，紧接着发起”注册存储记录并执行内容标准化处理“请求
     *
     * @param event
     */
    public void onEventBackgroundThread(FileUploadedEvent event) {
        LogHelper.d("upload>>>> file upload is completed, and start to registerFile");

//        UploadModel uploadModel = GsonImplHelp.get().toObject(event.getResult(), UploadModel.class);
//        JSONObject result = event.getJsonResult();
//        try {
//            JSONObject data = result.getJSONObject("data");
//
//            //服务器返回的占位文件路径
//            String filePath = data.getString("filePath");
//
//            //注册存储记录并执行内容标准化处理的JSON参数
//            JSONObject jsonParams = new JSONObject();
//            jsonParams.put("fileName", event.getFilePath());
//            jsonParams.put("filePath", filePath);
//            jsonParams.put("normalization", "sync");
//
//            Map<String, String> extras = new HashMap<>();
//            extras.put("owner", event.getOwner());
//            extras.put("filePath", event.getFilePath());
//
//            //第三步：注册存储记录并执行内容标准化处理
//            HttpHelper.post(Constants.getCoreUrls().registerFile(), CoreHttpApiKey.registerFile, jsonParams.toString
//                    (), extras);
//        } catch (JSONException e) {
//            LogHelper.e(this.getClass().getName(), e);
//        }
    }

    public void uploadChatImage(String url, String key) {
        LogHelper.d("upload>>>> start to upload chat image");
        Map<String, String> extras = new HashMap<>();
        extras.put("filePath", url);
        extras.put("owner", key);
        //第一步：获取上传地址
//        CoreHttpApi.getUploadUrl(extras);
        CoreHttpApi.fileUpload(url, extras);

    }

    /**
     * 下载文件
     * 语音消息文件、视频消息文件、二维码
     *
     * @param fileStorageId
     * @param fileType
     * @param owner
     */
    public void download(String fileStorageId, FileType fileType, String owner) {
        String suffix = ".jpg";
        switch (fileType) {
            case IMAGE:
                suffix = ".jpg";
                break;
            case VEDIO:
                suffix = ".mp4";
                break;
            case VOICE:
                suffix = ".amr";
                break;
            case APK:
                suffix = ".apk";
                break;
            default:
                break;
        }

        CoreHttpApi.fileDownload(fileStorageId, Constants.getCoreUrls().getDownloadUrl(fileStorageId) +
                "?fileSaveName=" + UUID.randomUUID() + suffix, fileType, owner);
    }

    /**
     * 下载文件
     * 语音消息文件、视频消息文件、二维码
     *
     * @param fileStorageId
     * @param owner
     */
    public void download(String fileStorageId, String fileName, String owner) {

        CoreHttpApi.fileDownload(fileStorageId, Constants.getCoreUrls().getDownloadUrl(fileStorageId) +
                "?fileSaveName=" + fileName, FileType.FILE, owner);
    }

    /**
     * 文件下载完成后处理
     * <p/>
     * 当文件下载完成后，在File表中记录，并通过FileAvailableEvent广播出去
     * <p/>
     * 普通文件（头像，背景图，二维码等）和消息相关文件流程相同
     *
     * @param event
     */
    public void onEventBackgroundThread(FileDownloadedEvent event) {
        final FilesTable filesTable = insert(event.getFileStorageId(), event.getFilePath(), event.getFileType(), 0, 0,
                event.getOwner());
        MyApplication.getInstance().handlerDownload.remove(event.getOwner());
        if (filesTable != null) {
            for (Map.Entry<String, ImageView> entry : waitDownloadingAvatar.entrySet()) {
                if (entry != null && entry.getKey() != null) {
                    if (entry.getKey().equals(filesTable.getFileStorageId())) {
                        final ImageView container = entry.getValue();
                        MyApplication.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                container.setImageBitmap(GraphicHelper.getSmallBitmap(filesTable.getPath()));
                            }
                        });
                        waitDownloadingAvatar.remove(filesTable.getFileStorageId());
                    }
                }
            }
        }

        EventBus.getDefault().post(new FileAvailableEvent(filesTable));
    }

    /**
     * 文件下载失败
     *
     * @param event
     */
    public void onEventBackgroundThread(FileDownloadErrorEvent event) {
        MyApplication.getInstance().handlerDownload.remove(event.getOwner());
        FileType type = event.getFileType();
        if (null != type) {
        } else {
            ToolsHelper.showStatus(MyApplication.getInstance().getApplicationContext(),false, mApplicationContext.getString(R.string.filemanager_download_failed));
        }
    }

}
