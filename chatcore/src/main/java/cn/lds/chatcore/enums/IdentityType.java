package cn.lds.chatcore.enums;

/**
 * 身份认证状态
 * Created by quwei on 2016/6/21.
 */
public enum IdentityType {
    /** 未认证 **/
    UNIDENTITY,
    /** 认证中 **/
    IDENTITYING,
    /** 认证成功 **/
    SUCCESS,
    /** 认证失败 **/
    FAILURE
}
