package cn.lds.chatcore.common;

/**
 * Created by quwei on 2016/6/1.
 */
public class SystemConfig {
    /**************************
     * 系统个性化配置 STA
     ************************************/

    // 10秒定位一次
    public final static int SYS_CONFIG_MAP_LOCATIONSCANSAPN = 1000*3;
    // 欢迎页>是否显示动画
    public final static boolean SYS_CONFIG_SHOW_WELCOME_CARTOON = false;
    // 消息>加号>添加好友
    public final static boolean SYS_CONFIG_SHOW_ADD_FRIEND = false;
    // 消息>加号>发起群聊
    public final static boolean SYS_CONFIG_SHOW_CREATE_MUC = true;
    // 消息>加号>扫一扫
    public final static boolean SYS_CONFIG_SHOW_DO_SCAN = true;
    // 消息>待办
    public final static boolean SYS_CONFIG_SHOW_TODO = true;
    // 通讯录>新的朋友
    public final static boolean SYS_CONFIG_SHOW_NEW_FRIEND = false;
    // 通讯录>组织机构
    public final static boolean SYS_CONFIG_SHOW_ORG = true;
    // 创建群聊、添加群聊成员（显示模式控制：①组织、好友方式②好友方式。）
    public final static boolean SYS_CONFIG_CREATE_MUC_BY_ORG = true;
    // 名片>修改备注
    public final static boolean SYS_CONFIG_SHOW_EDIT_REMARK = true;
    // 名片>发送该名片
    public final static boolean SYS_CONFIG_SHOW_SEND_CARD = true;
    // 名片>删除
    public final static boolean SYS_CONFIG_SHOW_DELETE_CARD = true;
    // 我>设置>隐私
    public final static boolean SYS_CONFIG_SHOW_SETTING_SECRET = false;
    // 登录>注册
    public final static boolean SYS_CONFIG_SHOW_REGIST = true;
    // 登录>忘记密码
    public final static boolean SYS_CONFIG_SHOW_FORGET_PASS = true;
    // 登录>微信登录
    public final static boolean SYS_CONFIG_SHOW_WEIXIN_LOGIN = false;

    // 重新登录>是否显示更多
    public final static boolean SYS_CONFIG_SHOW_RELOGIN_MORE = false;
    // 重新登录>切换账号
    public final static boolean SYS_CONFIG_SHOW_RELOGIN_CHANGE_USER = true;
    // 重新登录>注册
    public final static boolean SYS_CONFIG_SHOW_RELOGIN_REGIST = true;
    // 重新登录>微信登录
    public final static boolean SYS_CONFIG_SHOW_RELOGIN_WEIXIN_LOGIN = true;

    /************************** 系统个性化配置 END ************************************/
}
