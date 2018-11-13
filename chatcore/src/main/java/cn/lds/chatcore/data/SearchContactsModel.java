package cn.lds.chatcore.data;

/**
 * Created by zhoup on 2015/12/11.
 */
public class SearchContactsModel {

    private String id;//好友的社群用户帐号ID
    private String no;//好友的社群业务帐号
    private String mobile;//手机号码
    private String name;//好友的用户昵称，公众号名称
    private String avatarUrl;//好友的头像URL 或 公众号标识url

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
