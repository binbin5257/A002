package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "setting")
public class SettingsTable extends EntityBase{

	public SettingsTable(){}
	
	public SettingsTable(String account) {
		this.account = account;
		this.user = "";
	}
	public SettingsTable(String account, String user) {
		this.account = account;
		this.user = user;
	}

	// TODO 这个表是否有存在的必要呢？现在的聊天回话已经有管理表了。修改的话，有多大影响不好说啊

	/** 默认的字段ID */
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/** 所属账户 */
	@Column(column = "account")
	private String account;
	/** 好友/群组 */
	@Column(column = "user")
	private String user;
	/** 置顶 */
	@Column(column = "top")
	private boolean top = false;
	/** 免打扰 */
	@Column(column = "quiet")
	private boolean quiet = false;
	/** 聊天背景 */
	@Column(column = "backgroundurl")
	private String backgroundurl;
	/** 回话状态：临时，正式等，除群主创建群聊外，其他都是正式，群主发送第一条消息后变为正式 */
	@Column(column = "enabaledraft")
	private boolean enabaledraft;
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean isTop() {
		return top;
	}

	public void setTop(boolean top) {
		this.top = top;
	}

	public boolean isQuiet() {
		return quiet;
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	public String getBackgroundurl() {
		return backgroundurl;
	}

	public void setBackgroundurl(String backgroundurl) {
		this.backgroundurl = backgroundurl;
	}

	public boolean isEnabaledraft() {
		return enabaledraft;
	}

	public void setEnabaledraft(boolean enabaledraft) {
		this.enabaledraft = enabaledraft;
	}
}
