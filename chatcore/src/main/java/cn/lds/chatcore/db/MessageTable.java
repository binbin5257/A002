package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by quwei on 2015/12/24.
 */
@Table(name = "tcloudMessage")
public class MessageTable extends EntityBase {
    /**
     * 默认的字段ID
     */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 消息编号
     */
    @Column(column = "messageId")
    private String messageId;
    /* 发送人 */
    @Column(column = "fromClientId")
    private String fromClientId;
    /*接收人*/
    @Column(column = "toClientId")
    private String toClientId;
    /**
     * 消息类型（301=订单，302=活动）
     */
    @Column(column = "messageType")
    private String messageType;

    /**
     * 消息标题
     */
    @Column(column = "title")
    private String title;
    /**
     * 消息内容
     */
    @Column(column = "content")
    private String content;
    /**
     * 消息objectId
     */
    @Column(column = "objectId")
    private String objectId;

    @Column(column = "isNew")
    private boolean isNew;
    @Column(column = "time")
    private long time;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFromClientId() {
        return fromClientId;
    }

    public void setFromClientId(String fromClientId) {
        this.fromClientId = fromClientId;
    }

    public String getToClientId() {
        return toClientId;
    }


    public void setToClientId(String toClientId) {
        this.toClientId = toClientId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
