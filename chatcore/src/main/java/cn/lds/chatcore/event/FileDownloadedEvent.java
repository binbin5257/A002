package cn.lds.chatcore.event;

import cn.lds.chatcore.enums.FileType;

/**
 * 文件下载完成事件
 * <p>
 * 收到文件类型消息（图片、语音、视频、位置等）时，会先现在，并缓存在本地，然后广播出该事件
 * </p>
 * <p>
 * 该事件只在manager中使用，UI中不会注册接收该事件，参考@see FileAvailableEvent
 * </p>
 * 
 * @author suncf
 * 
 */
public class FileDownloadedEvent {

	/* 文件服务器地址 */
	String fileStorageId;
	/* 文件类型 */
	FileType fileType;
	/* 文件所属 */
	String owner;
	
	String filePath;

	public FileDownloadedEvent(String fileStorageId, FileType fileType, String owner, String filePath) {
		this.fileStorageId = fileStorageId;
		this.fileType = fileType;
		this.owner = owner;
		this.filePath = filePath;
	}

	public String getFileStorageId() {
		return fileStorageId;
	}

	public void setFileStorageId(String fileStorageId) {
		this.fileStorageId = fileStorageId;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
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
}
