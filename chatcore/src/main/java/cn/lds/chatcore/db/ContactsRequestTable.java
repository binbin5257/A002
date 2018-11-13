package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by quwei on 2015/11/26.
 * 好友申请管理表
 */
@Table(name = "contacts_request")
public class ContactsRequestTable extends EntityBase{
    /** 默认的字段ID */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /**
     * 登录账号
     */
    @Column(column = "account")
    private String account;
    /**
     * 申请人编号
     */
    @Column(column = "requestuserid")
    private String requestuserid;

    /**
     * 申请人姓名
     */
    @Column(column = "friend_nickname")
    private String friendNickname;

    /**
     * 申请人头像
     */
    @Column(column = "friend_avatar_url")
    private String friendAvatarUrl;

    /**
     * 申请ID
     */
    @Column(column = "requestid")
    private String requestid;
    /**
     * 申请消息
     */
    @Column(column = "requestmessage")
    private String requestmessage;
    /**
     * 申请日期
     */
    @Column(column = "requestdate")
    private long requestdate;
    /**
     * 申请状态:awaiting, added, refused;等待确认，已添加，已拒绝
     */
    @Column(column = "status")
    private String status;
    /**
     * 消息接收标志--用于判断控制UI 接受 等待验证
     */
    @Column(column = "is_msg_receive")
    private Boolean isMsgReceive;

    /**
     * 消息已读/未读
     */
    @Column(column = "read")
    private boolean read;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRequestuserid() {
        return requestuserid;
    }

    public void setRequestuserid(String requestuserid) {
        this.requestuserid = requestuserid;
    }

    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
    }

    public long getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(long requestdate) {
        this.requestdate = requestdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsMsgReceive() {
        return isMsgReceive;
    }

    public void setIsMsgReceive(Boolean isMsgReceive) {
        this.isMsgReceive = isMsgReceive;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }

    public String getFriendAvatarUrl() {
        return friendAvatarUrl;
    }

    public void setFriendAvatarUrl(String friendAvatarUrl) {
        this.friendAvatarUrl = friendAvatarUrl;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
