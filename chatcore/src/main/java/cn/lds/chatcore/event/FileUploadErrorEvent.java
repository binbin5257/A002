package cn.lds.chatcore.event;

/**
 * 文件上传失败事件
 */
public class FileUploadErrorEvent {
    String owner, filePath, errorMsg;

    public FileUploadErrorEvent(String owner, String filePath, String errorMsg) {
        this.owner = owner;
        this.filePath = filePath;
        this.errorMsg = errorMsg;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
