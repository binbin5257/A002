package cn.lds.chatcore.event;

import cn.lds.chatcore.enums.FileType;

/**
 * 文件下载进度
 */
public class FileDownloadingEvent {


    /* 文件所属 */
    String owner;

    String fileStorageId;

    int progress;
    FileType fileType;

    public FileDownloadingEvent(String fileStorageId, FileType fileType, String owner, int progress) {

        this.owner = owner;
        this.fileStorageId = fileStorageId;
        this.progress = progress;
        this.fileType = fileType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFilePath() {
        return fileStorageId;
    }

    public void setFilePath(String fileStorageId) {
        this.fileStorageId = fileStorageId;
    }
}
