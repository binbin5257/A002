package cn.lds.chatcore.imtp.message.tcloud;

import cn.lds.chatcore.imtp.message.Message;

public class TcloudMessage extends Message {


    /**
     * fromClientId : NOTICE
     * message : {"title":"有活动啦","content":"<p>&nbsp;&nbsp;&nbsp;&nbsp;有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦有活动啦</p>","objectId":1}
     * messageType : 302
     * time : 1466840106351
     * toClientId : C1N7HZUSFBPKP
     */

    private String fromClientId;
    private String content;
    private String objectId;
    private String title;
    private String messageType;
    private long time;
    private String toClientId;
    private String objectNo;

    public String getObjectNo() {
        return objectNo;
    }

    public void setObjectNo(String objectNo) {
        this.objectNo = objectNo;
    }

    public String getFromClientId() {
        return fromClientId;
    }

    public void setFromClientId(String fromClientId) {
        this.fromClientId = fromClientId;
    }


    public String getMessageType() {
        return messageType;
    }


    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getToClientId() {
        return toClientId;
    }

    public void setToClientId(String toClientId) {
        this.toClientId = toClientId;
    }

    @Override
    public String createContentJsonStr() {
        return null;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
