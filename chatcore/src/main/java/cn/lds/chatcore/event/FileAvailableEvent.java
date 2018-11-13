package cn.lds.chatcore.event;

import cn.lds.chatcore.db.FilesTable;

/**
 * 文件准备完成事件
 * <p>
 * 相关manager处理完@see FileDownloadedEvent事件后，会广播出该事件。
 * </p>
 * <p>
 * UI可以注册接收该事件
 * </p>
 * 
 * 
 * @author suncf
 * 
 */
public class FileAvailableEvent {
	FilesTable mFilesTable;

	public FileAvailableEvent(){
	}
	
	public FileAvailableEvent(FilesTable filesTable){
		this.mFilesTable =filesTable;
	}

	public FilesTable getFilesTable() {
		return mFilesTable;
	}

	public void setFilesTable(FilesTable filesTable) {
		mFilesTable = filesTable;
	}
}
