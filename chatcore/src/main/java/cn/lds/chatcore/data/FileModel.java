package cn.lds.chatcore.data;

import android.os.Environment;

import java.io.File;

import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.ToolsHelper;

/**
 * Created by xuqm on 2016/5/17.
 */
public class FileModel {
    private String path;
    private int type = 0;
    private String name;
    private long createTime;
    private long fileSize = 0; // 字节
    private String parentPath;
    private boolean isDirectory;

    public FileModel() {
    }

    public FileModel(String path, String name, long createTime, long fileSize) {
        this.path = path;
        this.name = name;
        this.createTime = createTime;
        this.fileSize = fileSize;
    }

    public FileModel(String path, boolean isDirectory) {
        this.path = path;
        this.isDirectory = isDirectory;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return FileHelper.getFileTypeIcon(this.getName());
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {

        if (ToolsHelper.isNull(name)) {
            File file = new File(path);
            if (null != file)
                return file.getName();
            else
                return "...";
        }
        return ToolsHelper.isNullString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateTime() {
        if (0 == createTime) {
            {
                File file = new File(path);
                if (null != file)
                    return file.lastModified();
            }
        }
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getParentPath() {
        if (ToolsHelper.isNull(parentPath)) {
            File file = new File(path);
            if (null != file)
                return file.getParent();
            else
                return Environment.getExternalStorageDirectory().getPath();
        }
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public long getFileSize() {
        if (0 == fileSize) {
            File file = new File(path);
            if (null != file) {
                return FileHelper.getChildCount(file);
            }
        }
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "path='" + path + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", fileSize=" + fileSize +
                ", parentPath='" + parentPath + '\'' +
                ", isDirectory=" + isDirectory +
                '}';
    }
}
