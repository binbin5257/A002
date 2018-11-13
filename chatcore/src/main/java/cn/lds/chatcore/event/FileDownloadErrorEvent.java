package cn.lds.chatcore.event;

import cn.lds.chatcore.enums.FileType;

public class FileDownloadErrorEvent {

	/* 文件服务器地址 */
	String fileStorageId;
	/* 文件类型 */
	FileType fileType;
	/* 文件所属 */
	String owner;
	
	String errorMsg;

	public FileDownloadErrorEvent(String fileStorageId, String owner, FileType fileType, String errorMsg) {
		this.fileStorageId = fileStorageId;
		this.errorMsg = errorMsg;
		this.owner = owner;
		this.fileType = fileType;
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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
