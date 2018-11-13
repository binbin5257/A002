package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "version")
public class VersionTable extends EntityBase{
	/** 默认的字段ID */
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VersionTable() {
	}

	public VersionTable(int version, int file_id) {
		this.version = version;
		this.fileId = file_id;
	}

	public VersionTable(int version, int fileId, boolean forceUpdate) {
		this.version = version;
		this.fileId = fileId;
		this.forceUpdate = forceUpdate;
	}

	/** 所属账户 */
	@Column(column = "version")
	private int version;

	/** 所属账户 */
	@Column(column = "file_id")
	private int fileId = -1;

	/** 强制更新 */
	@Column(column = "force_update")
	private boolean forceUpdate;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
}