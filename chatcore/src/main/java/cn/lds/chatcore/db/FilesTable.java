package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "files")
public class FilesTable extends EntityBase{
	/** 默认的字段ID */
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/** 服务器存储ID */
	@Column(column = "file_storage_id")
	private String fileStorageId;
	/** 标准图下载完成后，本地存储路径 */
	@Column(column = "path")
	private String path;
	/** 文件类型(枚举FileType) */
	@Column(column = "file_type")
	private String fileType;
	/** 文件大小 */
	@Column(column = "size")
	private long size;
	/** 音频、视频播放时长 */
	@Column(column = "duration")
	private long duration;
	/** 消息ID或其它 */
	@Column(column = "owner")
	private String owner;

	public String getFileStorageId() {
		return fileStorageId;
	}

	public void setFileStorageId(String fileStorageId) {
		this.fileStorageId = fileStorageId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
