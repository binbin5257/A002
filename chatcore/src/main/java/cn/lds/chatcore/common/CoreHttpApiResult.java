package cn.lds.chatcore.common;

import java.io.InputStream;

import cn.lds.chatcore.MyApplication;

public class CoreHttpApiResult {
    public static String tag = CoreHttpApiResult.class.getSimpleName();

    /**
     * 读取JSON文件
     */
    public static String readJSON(String strFileName) {
        String strResult = "";
        try {
            InputStream is = MyApplication.getInstance().getAssets().open("static_api_json/" + strFileName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            strResult = new String(buffer, "UTF-8");
            LogHelper.d(String.format("%s:%s:%s", CoreHttpApiResult.class.getName(), "strFileName", strResult));
            //strResult = "{\"status\":\"success\",\"data\":"+strResult+"}";
        } catch (Exception ex) {
            LogHelper.e(tag, ex);
            //strResult = "{\"status\":\"failure\",\"data\":null}";
        }
        return strResult;
    }
    // ----------------------------- 登录 sta -----------------------------

    /**
     * A001: 登录
     */
    public static String login() {
        return readJSON("login");
    }

    /**
     * A002: 校验access token
     */
    public static String verifyCredentials() {
        return readJSON("verifyCredentials");
    }

    /**
     * A003: 退出
     */
    public static String logout() {
        return readJSON("logout");
    }

    /**
     * A004: 获取客户端信息
     */
    public static String getClientInformation() {
        return readJSON("getClientInformation");
    }

    /**
     * A005: 手机注册
     */
    public static String registerByMobile() {
        return readJSON("registerByMobile");
    }

    /**
     * A006: 用户名注册
     */
    public static String register() {
        return readJSON("register");
    }

    /**
     * A007: 手机找回密码
     */
    public static String findPasswordByMobile() {
        return readJSON("findPasswordByMobile");
    }

    /**
     * A008: 邮件找回密码
     */
    public static String findPasswordByMail() {
        return readJSON("findPasswordByMail");
    }

    /**
     * A009: 微信号登录绑定手机号
     */
    public static String boundWeixinAndMobile() {
        return readJSON("boundWeixinAndMobile");
    }

    /**
     * A010: 发送验证码
     */
    public static String sendCAPTCHA() {
        return readJSON("sendCAPTCHA");
    }
    // ----------------------------- 登录 end -----------------------------

    // ----------------------------- 消息 sta -----------------------------

    /**
     * B001: 获取默认消息列表
     */
    public static String getFixMessageList() {
        return readJSON("getFixMessageList");
    }

    /**
     * B002: 荣联账号取得
     */
    public static String getUserDetail() {
        return readJSON("getUserDetail");
    }

    /**
     * B002: B002_1: 获取用户详细信息（和是否为好友无关）
     */
    public static String getClientUserDetail() {
        return readJSON("getClientUserDetail");
    }

    /**
     * B003: 通信好友荣联VOIP账号取得
     */
    public static String getFriendVoipAccount() {
        return readJSON("getFriendVoipAccount");
    }

    /**
     * B004: 用户头像取得
     */
    public static String getUserPortraitByVoipAccount() {
        return readJSON("getUserPortraitByVoipAccount");
    }

    /**
     * B011: 获取新消息列表
     */
    public static String getNewMessages() {
        return readJSON("getNewMessages");
    }

    /**
     * B012: 查找公众号
     */
    public static String findOfficialAccount() {
        return readJSON("findOfficialAccount");
    }

    /**
     * B013: 获得活动列表
     */
    public static String getCampaignList() {
        return readJSON("getCampaignList");
    }

    /**
     * B014: 获得活动详情
     */
    public static String getCampaignDetail() {
        return readJSON("getCampaignDetail");
    }

    /**
     * B015: 取消关注公众号
     */
    public static String unfollowOfficialAccount() {
        return readJSON("unfollowOfficialAccount");
    }

    /**
     * B016: 获取群二维码
     */
    public static String getGroupCode() {
        return readJSON("getGroupCode");
    }

    // ----------------------------- 消息 end -----------------------------

    // ----------------------------- 通讯录 sta -----------------------------

    /**
     * C001: 获取好友列表
     */
    public static String friends() {
        return readJSON("friends");
    }

    /**
     * C002.1: 获取好友详细
     */
    public static String getFriend() {
        return readJSON("getFriend");
    }

    /**
     * C002.2: 获陌生人详细
     */
    public static String getStranger() {
        return readJSON("getStranger");
    }

    /**
     * C003: 设置好友标签
     */
    public static String changeTags() {
        return readJSON("changeTags");
    }

    /**
     * C003: 设置好友备注名
     */
    public static String changeAlias() {
        return readJSON("changeAlias");
    }

    /**
     * C004: 标记为星标朋友
     */
    public static String markFavorite() {
        return readJSON("markFavorite");
    }

    /**
     * C005: 查找好友
     */
    public static String findNewFrind() {
        return readJSON("findNewFrind");
    }

    /**
     * C005_1: 查找通讯录好友
     */
    public static String searchContacts() {
        return readJSON("searchContacts");
    }

    /**
     * C006: 添加新朋友
     */
    public static String addFriend() {
        return readJSON("addFriend");
    }

    /**
     * C007: 删除朋友
     */
    public static String deleteFriend() {
        return readJSON("deleteFriend");
    }

    /**
     * C008.1: 发起朋友身份验证请求
     */
    public static String requestFriend() {
        return readJSON("requestFriend");
    }

    /**
     * C008: 接受新朋友请求
     */
    public static String acceptFriend() {
        return readJSON("acceptFriend");
    }

    /**
     * C009: 获取单聊会话列表
     */
    public static String clientUserChats() {
        return readJSON("clientUserChats");
    }

    /**
     * C010: 设置单聊置顶
     */
    public static String setTopmost() {
        return readJSON("setTopmost");
    }

    /**
     * C011: 设置单聊背景图片
     */
    public static String updateBackground() {
        return readJSON("updateBackground");
    }

    /**
     * C012: 设置单聊免打扰
     */
    public static String enableNoDisturbFriend() {
        return readJSON("enableNoDisturbFriend");
    }

    /**
     * C013: 获取群会话列表
     */
    public static String groupChats() {
        return readJSON("groupChats");
    }

    /**
     * C013: 获取通讯录中群列表
     */
    public static String groups() {
        return readJSON("groups");
    }

    /**
     * C014: 获取群详细信息
     */
    public static String getGroup() {
        return readJSON("getGroup");
    }

    /**
     * C014.2: 获取群成员详细信息
     */
    public static String getGroupMember() {
        return readJSON("getGroupMember");
    }

    /**
     * C015: 创建群聊
     */
    public static String createGroup() {
        return readJSON("createGroup");
    }

    /**
     * C016: 群保存到通讯录
     */
    public static String addGroupToAddressList() {
        return readJSON("addGroupToAddressList");
    }

    /**
     * C017: 更新群聊名称
     */
    public static String updateGroupName() {
        return readJSON("updateGroupName");
    }

    /**
     * C018: 删除群聊
     */
    public static String deleteGroup() {
        return readJSON("deleteGroup");
    }

    /**
     * C019: 增加群聊成员
     */
    public static String addGroupMember() {
        return readJSON("addGroupMember");
    }

    /**
     * C020: 移除群聊成员
     */
    public static String deleteGroupMember() {
        return readJSON("deleteGroupMember");
    }

    /**
     * C021: 设置群消息免打扰
     */
    public static String enableGroupNoDisturb() {
        return readJSON("enableGroupNoDisturb");
    }

    /**
     * C022: 设置群置顶聊天
     */
    public static String setGroupTopmost() {
        return readJSON("setGroupTopmost");
    }

    /**
     * C023: 设置我在群中的昵称
     */
    public static String updateNicknameInGroup() {
        return readJSON("updateNicknameInGroup");
    }

    /**
     * C024: 设置群聊天背景
     */
    public static String updateBackgroundInGroup() {
        return readJSON("updateBackgroundInGroup");
    }

    /**
     * C025: 更改群的状态
     */
    public static String updateGroupChatStatus() {
        return readJSON("updateGroupChatStatus");
    }

    /**
     * C026: 获取标签列表和标签详细
     */
    public static String tags() {
        return readJSON("tags");
    }

    /**
     * C027: 新建标签
     */
    public static String createTag() {
        return readJSON("createTag");
    }

    /**
     * C028: 修改标签
     */
    public static String updateTag() {
        return readJSON("updateTag");
    }

    /**
     * C029: 删除标签
     */
    public static String deleteTag() {
        return readJSON("deleteTag");
    }

    /**
     * C032: 获取组织
     */
    public static String getOrganization() {
        return readJSON("getOrganization");
    }

    /**
     * C033: 获取人员信息（画像）
     */
    public static String getFriendPicture() {
        return readJSON("getFriendPicture");
    }

    /**
     * C034: 通过手机号获取联系人
     */
    public static String getFriendByMobile() {
        return readJSON("getFriendByMobile");
    }

    /**
     * C036: 扫描群成员的二维码加入群聊组
     */
    public static String joinGroupByNoAndMember() {
        return readJSON("joinGroupByNoAndMember");
    }

    /**
     * C037: 查找附近的人
     */
    public static String searchNearbyUsers() {
        return readJSON("searchNearbyUsers");
    }
    // ----------------------------- 通讯录 end -----------------------------

    // ----------------------------- 我 sta -----------------------------

    /**
     * E001: 获取我的个人详细信息
     */
    public static String getMyDetail() {
        return readJSON("getMyDetail");
    }

    /**
     * E002: 修改我的头像
     */
    public static String changeAvatar() {
        return readJSON("changeAvatar");
    }

    /**
     * E003: 设置免打扰
     */
    public static String enableNoDisturbMe() {
        return readJSON("enableNoDisturbMe");
    }

    /**
     * E004: 设置昵称
     */
    public static String changeNickname() {
        return readJSON("changeNickname");
    }

    /**
     * E005: 设置地区
     */
    public static String changeCity() {
        return readJSON("changeCity");
    }

    /**
     * E006: 设置性别
     */
    public static String chanGegender() {
        return readJSON("chanGegender");
    }

    /**
     * E007: 设置个人签名
     */
    public static String changeMoodMessage() {
        return readJSON("changeMoodMessage");
    }

    /**
     * E008: 加我为朋友时是否需要验证
     */
    public static String changeNeedFriendConfirmation() {
        return readJSON("changeNeedFriendConfirmation");
    }

    /**
     * E009: 是否允许向我推荐通讯录朋友
     */
    public static String changeAllowFindMobileContacts() {
        return readJSON("changeAllowFindMobileContacts");
    }

    /**
     * E010: 是否允许通过登录名称找到我
     */
    public static String changeAllowFindMeByLoginId() {
        return readJSON("changeAllowFindMeByLoginId");
    }

    /**
     * E011: 是否允许通过手机号码找到我
     */
    public static String changeAllowFindMeByMobileNumber() {
        return readJSON("changeAllowFindMeByMobileNumber");
    }

    /**
     * E012: 获取我的二维码
     */
    public static String getMyCode() {
        return readJSON("getMyCode");
    }

    /**
     * E014: 获取最新版本
     */
    public static String getLastestVersion() {
        return readJSON("getLastestVersion");
    }

    /**
     * E015: 更新手机号
     */
    public static String updateMoblie() {
        return readJSON("updateMoblie");
    }

    /**
     * E016: 更新邮件
     */
    public static String updateEmail() {
        return readJSON("updateEmail");
    }

    /**
     * E018: 修改密码
     */
    public static String resetPassword() {
        return readJSON("resetPassword");
    }

    /**
     * E018_1: 修改密码（通过手机）
     */
    public static String resetPasswordByMobile() {
        return readJSON("resetPasswordByMobile");
    }

    /**
     * E019: 记录个人的轨迹
     */
    public static String track() {
        return readJSON("track");
    }

    /**
     * E020: 用户反馈使用当中的意见、建议、需求
     */
    public static String feedback() {
        return readJSON("feedback");
    }

    /**
     * E033: 设置地址
     */
    public static String changeAddress() {
        return readJSON("changeAddress");
    }

    /**
     * E034: 设置地理位置
     */
    public static String updateLocation() {
        return readJSON("updateLocation");
    }
    // ----------------------------- 我 end -----------------------------

    // ----------------------------- 其他 sta -----------------------------

    /**
     * F001: 获取省份列表
     */
    public static String getProvides() {
        return readJSON("getProvides");
    }

    /**
     * F002: 获取城市列表
     */
    public static String getCities() {
        return readJSON("getCities");
    }

    /**
     * F004: 发送文本消息
     */
    public static String sendTxtMessage() {
        return readJSON("sendTxtMessage");
    }

    /**
     * F005: 发送语音消息
     */
    public static String sendAudioMessage() {
        return readJSON("sendAudioMessage");
    }

    /**
     * F006: 发送图片消息
     */
    public static String sendPictureMessage() {
        return readJSON("sendPictureMessage");
    }

    /**
     * F007: 发送地理位置消息
     */
    public static String sendLocationMessage() {
        return readJSON("sendLocationMessage");
    }

    /**
     * F008: 发送视频消息
     */
    public static String sendVideoMessage() {
        return readJSON("sendVideoMessage");
    }

    /**
     * F009: 发送名片消息
     */
    public static String sendCardMessage() {
        return readJSON("sendCardMessage");
    }

    /**
     * F010: 发送单图文消息
     */
    public static String sendSingleImageTxtMessage() {
        return readJSON("sendSingleImageTxtMessage");
    }

    /**
     * F011: 发送多图文消息
     */
    public static String sendMultiImageTxtMessage() {
        return readJSON("sendMultiImageTxtMessage");
    }

    /**
     * F012: 发送通知消息
     */
    public static String sendInformMessage() {
        return readJSON("sendInformMessage");
    }

    /**
     * F013: 发送文件消息
     */
    public static String sendFileMessage() {
        return readJSON("sendFileMessage");
    }

    /**
     * F014: 发送业务消息
     */
    public static String sendBusinessMessage() {
        return readJSON("sendBusinessMessage");
    }

    /**
     * F015: 发送用户禁用消息
     */
    public static String sendDisableUserMessage() {
        return readJSON("sendDisableUserMessage");
    }

    /**
     * F016: 发送用户删除消息
     */
    public static String sendDeleteUserMessage() {
        return readJSON("sendDeleteUserMessage");
    }

    /**
     * F017: 发送用户注销消息
     */
    public static String sendCancelUserMessage() {
        return readJSON("sendCancelUserMessage");
    }

    /**
     * F018: 发送用户打开第三方APP
     */
    public static String sendOpen3rdApp() {
        return readJSON("sendOpen3rdApp");
    }

    /**
     * F019: 发送不明消息
     */
    public static String sendUnknownMessage() {
        return readJSON("sendUnknownMessage");
    }

    /**
     * F020: 发送语音聊天请求
     */
    public static String sendAudioChatRequest() {
        return readJSON("sendAudioChatRequest");
    }

    /**
     * F021: 同意语音聊天请求
     */
    public static String agreeAudioChatRequest() {
        return readJSON("agreeAudioChatRequest");
    }

    /**
     * F022: 拒绝语音聊天请示
     */
    public static String refuseAudioChatRequest() {
        return readJSON("refuseAudioChatRequest");
    }

    /**
     * F023: 语音聊天
     */
    public static String keepAudioChat() {
        return readJSON("keepAudioChat");
    }

    /**
     * F024: 发送视频聊天请求
     */
    public static String sendVideoChatRequest() {
        return readJSON("sendVideoChatRequest");
    }

    /**
     * F025: 同意视频聊天请求
     */
    public static String agreeVideoChatRequest() {
        return readJSON("agreeVideoChatRequest");
    }

    /**
     * F026: 拒绝视频聊天请示
     */
    public static String refuseVideoChatRequest() {
        return readJSON("refuseVideoChatRequest");
    }

    /**
     * F027: 视频聊天
     */
    public static String keepVideoChat() {
        return readJSON("keepVideoChat");
    }
    // ----------------------------- 其他 end -----------------------------

    /**
     * 获取上传路径
     */
    public static String getUploadUrl() {
        return readJSON("getUploadUrl");
    }

    /**
     * 上传文件
     */
    public static String uploadFile() {
        return readJSON("uploadFile");
    }

    /**
     * 上传文件
     */
    public static String registerFile() {
        return readJSON("registerFile");
    }

    // ----------------------------- 二维码 start -----------------------------

    /**
     * 001: 群二维码
     */
    public static String getGroupQRcode() {
        return readJSON("getGroupQRcode");
    }

    /**
     * 002: 我的二维码
     */
    public static String getMyQRcode() {
        return readJSON("getMyQRcode");
    }

    /**
     * 003: 校验二维码
     */
    public static String checkQRcode() {
        return readJSON("checkQRcode");
    }

    /**
     * 004: 下载二维码
     */
    public static String downloadQRcode() {
        return readJSON("downloadQRcode");
    }
    // ----------------------------- 二维码 end -----------------------------

    // ----------------------------- 公众号 start -----------------------------

    /**
     * 001: 查找开放平台服务帐号 (根据名称模糊匹配)
     */
    public static String searchServiceAccounts() {
        return readJSON("searchServiceAccounts");
    }

    /**
     * 002: 查看开放平台服务帐号详情
     */
    public static String getServiceAccountDetails() {
        return readJSON("getServiceAccountDetails");
    }

    /**
     * 003: 获取开放平台服务帐号包含的公众帐号
     */
    public static String getServiceAccountSubscription() {
        return readJSON("getServiceAccountSubscription");
    }

    /**
     * 004: 获取开放平台服务帐号包含的网站应用
     */
    public static String getServiceAccountWebsite() {
        return readJSON("getServiceAccountWebsite");
    }

    /**
     * 005: 获取个人使用的公众帐号服务列表
     */
    public static String listMySubscriptions() {
        return readJSON("listMySubscriptions");
    }

    /**
     * 006: 获取我关注的第三方web应用 个人用户列表
     */
    public static String listMyWebsites() {
        return readJSON("listMyWebsites");
    }

    /**
     * 006-1: 获取我关注的第三方web应用 个人用户列表 2016.5.6切换新接口
     */
    public static String apps() {
        return readJSON("apps");
    }

    /**
     * 007: 关注开放平台服务帐号包含的公众帐号
     */
    public static String followSubscription() {
        return readJSON("followSubscription");
    }

    /**
     * 008: 取消关注开放平台服务帐号包含的公众帐号
     */
    public static String unfollowSubscription() {
        return readJSON("unfollowSubscription");
    }

    /**
     * 009: 关注并授权开放平台帐号的第三方web应用 个人用户
     */
    public static String authorizeWebsite() {
        return readJSON("authorizeWebsite");
    }

    /**
     * 010: 取消关注并取消授权开放平台帐号的第三方web应用 个人用户
     */
    public static String unauthorizeWebsite() {
        return readJSON("unauthorizeWebsite");
    }

    /**
     * 011: 点击开放平台服务包含的公众帐号的自定义菜单项
     */
    public static String clickSubscriptionMenu() {
        return readJSON("clickSubscriptionMenu");
    }

    /**
     * 012: 是否启用置顶聊天
     */
    public static String enableSubscriptionTopmost() {
        return readJSON("enableSubscriptionTopmost");
    }

    /**
     * 013: 是否允许接收消息
     */
    public static String allowReceiveSubscriptionMessages() {
        return readJSON("clickSubscriptionMenu");
    }

    /**
     * 014: 查找开放平台帐号的第三方web应用 个人用户
     */
    public static String searchWebsites() {
        return readJSON("searchWebsites");
    }

    /**
     * 015: 查找全部分类（mobile）
     */
    public static String getMAppClassifications() {
        return readJSON("getMAppClassifications");
    }


    /**
     * 015-1: 查找全部分类（mobile） 2016.5.7 14:08 接口替换
     */
    public static String appClassificationGroups() {
        return readJSON("appClassificationGroups");
    }

    /**
     * XXX: 通知后台进入了公众号
     */
    public static String publicNoticeEnterServie() {
        return readJSON("publicNoticeEnterServie");
    }

    /**
     * XXX: 根据后台公众号的设置上传位置信息。
     */
    public static String publicUploadLocation() {
        return readJSON("publicUploadLocation");
    }

    /**
     * 001: 查找近期的（最早60天前）的待办事项
     */
    public static String getRecentTodoTasks() {
        return readJSON("getRecentTodoTasks");
    }

    /**
     * 002: 查找待办事项历史记录
     */
    public static String getHistoryTodoTasks() {
        return readJSON("getHistoryTodoTasks");
    }

    /**
     * 003: 处理待办事项（标记已处理）
     */
    public static String processTodoTasks() {
        return readJSON("processTodoTasks");
    }


    // ----------------------------- 公众号 end -----------------------------

    // ----------------------------- 码表 sta -----------------------------

    /**
     * 同步码表
     */
    public static String syncMasterCode() {
        return readJSON("syncMasterCode");
    }
    // ----------------------------- 码表 end -----------------------------

    // ----------------------------- 组织管理 sta -----------------------------

    /**
     * P001: 获取所有组织机构（团队）列表(MOBILE)
     */
    public static final String getMOrganizations() {
        return readJSON("getMOrganizations");
    }

    /**
     * P002: 获取所有组织用户列表(MOBILE)
     */
    public static final String getMOrganizationUsers() {
        return readJSON("getMOrganizationUsers");
    }
    // ----------------------------- 组织管理 end -----------------------------

    // ----------------------------- 其他 sta -----------------------------

    /**
     * 设为常用网站应用接口
     */
    public static final String setCommonWebApp() {
        return readJSON("setCommonWebApp");
    }

    /**
     * 取消设为常用网站应用
     */
    public static final String unsetCommonWebApp() {
        return readJSON("unsetCommonWebApp");
    }

    /**
     * 取消设为常用网站应用
     */
    public static final String authCode() {
        return readJSON("authCode");
    }

    /**
     * 获取个人信息（GET）
     * URL:http://localhost:8888/m/enrollees
     */
    public static final String enrolleesGet() {
        return readJSON("enrolleesGet");
    }

    /**
     * 男女本地码表
     */
    public static final String getMaster() {
        return readJSON("Master");
    }

    // ----------------------------- 其他 end -----------------------------

    /**
     * C013 订单支付(POST)
     * URL:http://localhost:8888/m/reservationOrders/{id}/pay
     */
    public static final String reservationOrdersPay() {
        return readJSON("reservationOrdersPay");
    }

    /**
     * 27.定金支付（POST）
     * URL：/m/foregiftAccounts
     * Respond:
     * {
     * "status": "success",
     * "data": {
     * "id": null,
     * "mchId": "***************",
     * "sign": "**************",
     * "nonceStr": "*************",
     * "prePayId": “***************”，
     * “timestamp”:”1400000000”
     * }
     * }
     */
    public static final String foregiftAccounts() {
        return readJSON("foregiftAccounts");
    }

    /**
     * 29.充值（POST）
     * URL：/m/accounts
     * Respond:
     * {
     * "status": "success",
     * "data": {
     * "id": null,
     * "mchId": "***************",
     * "sign": "**************",
     * "nonceStr": "*************",
     * "prePayId": “***************”，
     * “timestamp”:”1400000000”
     * }
     * }
     */
    public static final String accounts() {
        return readJSON("foregiftAccounts");
    }
}
