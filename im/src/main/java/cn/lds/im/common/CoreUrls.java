package cn.lds.im.common;


import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.ICoreUrls;

/**
 * Created by xuyongquan on 2015/11/21.
 */
public class CoreUrls implements ICoreUrls {

    /**
     * 登出信息
     */
    @Override
    public String getLogoutFilter() {
        return AppIndependentConfigure.getLogoutFilter;
    }

    /**
     * 登出信息
     */
    @Override
    public String getKickedFilter() {
        return AppIndependentConfigure.getKickedFilter;
    }

    /**
     * 获取IM服务器地址
     */
    @Override
    public String CONFIG_SERVER_URL() {
        return ModuleUrls.CONFIG_SERVER_URL();
    }

    /**
     * 注销
     */
    @Override
    public String UNREGISTER_CONFIG_SERVER_URL() {
        return ModuleUrls.UNREGISTER_CONFIG_SERVER_URL();
    }

    @Override
    public String H5_SERVER_HOST() {
        return null;
    }

    @Override
    public String H5_SERVER_DOMAIN() {
        return null;
    }


    // ----------------------------- 登录 sta -----------------------------

    /**
     * A001: 登录
     */
    @Override
    public String login() {
        return ModuleUrls.login();
    }
    /**
     * A001: 登录 （账号密码登录）
     */
    @Override
    public String loginCode() {
        return ModuleUrls.loginCode();
    }


    /**
     * A003: 退出
     */
    @Override
    public String logout() {
        return ModuleUrls.logout();
    }


    /**
     * A009: 微信号登录绑定手机号
     */
    @Override
    public String boundWeixinAndMobile() {
        return ModuleUrls.boundWeixinAndMobile();
    }


    @Override
    public String weixinLogin() {
        return ModuleUrls.weixinLogin();
    }

    @Override
    public String getUploadUrl() {
        return ModuleUrls.getUploadUrl();
    }

    @Override
    public String registerFile() {
        return ModuleUrls.registerFile();
    }

    @Override
    public String getDownloadUrl(String fileStorageId) {
        return ModuleUrls.getDownloadUrl(fileStorageId);
    }

    @Override
    public String getFileloadUrl(String fileStorageId) {
        return ModuleUrls.getFilesUrl(fileStorageId);
    }
    // ----------------------------- 登录 end -----------------------------

    // ----------------------------- 其他 sta -----------------------------

    /**
     * 保持session
     */
    @Override
    public String ping() {
        return ModuleUrls.ping();
    }

    @Override
    public String enrolleesGet() {
        return ModuleUrls.enrolleesGet();
    }

    @Override
    public String reservationOrdersWxPay() {
        return ModuleUrls.reservationOrdersWxPay();
    }

    @Override
    public String payment() {
        return ModuleUrls.payment();
    }

    @Override
    public String reservationOrdersAlipay() {
        return ModuleUrls.reservationOrdersAlipay();
    }

    @Override
    public String foregiftAccountsWxPay() {
        return ModuleUrls.foregiftAccountsWxPay();
    }
    @Override
    public String foregiftAccountsAlipay() {
        return ModuleUrls.foregiftAccountsAlipay();
    }
    @Override
    public String foregiftAccountsWalletpay() {
        return ModuleUrls.foregiftAccountsWalletpay();
    }

    @Override
    public String reservationOrdersWalletpay() {
        return ModuleUrls.reservationOrdersWalletpay();
    }
    @Override
    public String accountsAlipay() {
        return ModuleUrls.accountsAlipay();
    }
    @Override
    public String checkVersions() {
        return ModuleUrls.checkVersions();
    }
    @Override
    public String accountsWxPay() {
        return ModuleUrls.accountsWxPay();
    }

    @Override
    public String getSystemConfig() {
        return ModuleUrls.getSystemConfig();
    }
    // ----------------------------- 其他 end -----------------------------


}
