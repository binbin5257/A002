package cn.lds.chatcore.data;

/**
 * 应用的必要数据，只有在必要数据加载完成后，才能进入应用
 * 必要数据采取时间戳机制
 * Created by user on 2016/1/5.
 */
public class Essential {
    /* 联系人同步完成 */
    private boolean contactsAvailable = false;
    /* 群聊同步完成 */
    private boolean muclistAvailable = false;
    /* 公众号同步完成 */
    private boolean publicAccountListAvailable = true;
    /* 个人详情同步完成 */
    private boolean accountDetailsAvailable = false;
    /* 码表同步完成 */
    private boolean masterAvailable = false;


    /* 单聊会话同步完成 */
    private boolean clientUserChatsAvailable = true;
    /* 群聊会话同步完成 */
    private boolean groupChatsAvailable = true;

    public static Essential instance = null;

    public static Essential getInstance() {
        if (null == instance) {
            instance = new Essential();
        }
        return instance;
    }

    /**
     * 必要数据是否准备OK
     *
     * @return
     */
    public boolean isOK() {
//        return contactsAvailable && muclistAvailable && publicAccountListAvailable && accountDetailsAvailable &&
//                clientUserChatsAvailable && groupChatsAvailable && masterAvailable;
        return masterAvailable && accountDetailsAvailable;
    }

    /**
     * 重置
     *
     * @return
     */
    public void resetStatus() {
        /* 联系人同步完成 */
        contactsAvailable = false;
        /* 群聊同步完成 */
        muclistAvailable = false;
        /* 公众号同步完成 */
        publicAccountListAvailable = true;
        /* 个人详情同步完成 */
        accountDetailsAvailable = false;
        /* 码表同步完成 */
        masterAvailable = false;

        /* 单聊会话同步完成 */
        clientUserChatsAvailable = true;
        /* 群聊会话同步完成 */
        groupChatsAvailable = true;
    }

    public boolean isContactsAvailable() {
        return contactsAvailable;
    }

    public void setContactsAvailable(boolean contactsAvailable) {
        this.contactsAvailable = contactsAvailable;
    }

    public boolean isMuclistAvailable() {
        return muclistAvailable;
    }

    public void setMuclistAvailable(boolean muclistAvailable) {
        this.muclistAvailable = muclistAvailable;
    }

    public boolean isPublicAccountListAvailable() {
        return publicAccountListAvailable;
    }

    public void setPublicAccountListAvailable(boolean publicAccountListAvailable) {
        this.publicAccountListAvailable = publicAccountListAvailable;
    }

    public boolean isAccountDetailsAvailable() {
        return accountDetailsAvailable;
    }

    public void setAccountDetailsAvailable(boolean accountDetailsAvailable) {
        this.accountDetailsAvailable = accountDetailsAvailable;
    }

    public boolean isClientUserChatsAvailable() {
        return clientUserChatsAvailable;
    }

    public void setClientUserChatsAvailable(boolean clientUserChatsAvailable) {
        this.clientUserChatsAvailable = clientUserChatsAvailable;
    }

    public boolean isGroupChatsAvailable() {
        return groupChatsAvailable;
    }

    public void setGroupChatsAvailable(boolean groupChatsAvailable) {
        this.groupChatsAvailable = groupChatsAvailable;
    }

    public boolean isMasterAvailable() {
        return masterAvailable;
    }

    public void setMasterAvailable(boolean masterAvailable) {
        this.masterAvailable = masterAvailable;
    }
}
