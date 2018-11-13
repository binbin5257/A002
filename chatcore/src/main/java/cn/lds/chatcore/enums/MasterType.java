package cn.lds.chatcore.enums;

/**
 * 码表类型类型
 * 注意：如果新增加了枚举，必须在TimestampType.java中也添加对应的枚举用于同步数据。
 */
public enum MasterType {
    Region             /**地区*/
    , CustomerGrade      /**客户等级*/
    , BrokerGrade        /**经纪人等级*/
    , Sexuality          /**性别*/
    , ThirdMerchant      /** 服务机构 */
    , OpportunityStage   /**商机阶段*/
    , PurchaseTime       /**购车时间*/
    , PurchaseBudget     /**购车预算*/
    , FailedCause        /**战败原因*/
    , LeadsStatus        /**线索状态*/
    , TaskThemes          /**任务主题*/
    , TaskRelatedMatters /**相关事项(任务)*/
    , TaskPriority       /**优先级(任务)*/
    , TaskStatus         /**任务状态*/
    , OppoTypes          /**商机类型*/
    , IntentVehicleType  /** 购车类型*/
    , PurchaseNumber     /** 购车数量*/
    , ClientIdentityType  /** 会员身份、角色*/
    , CustomerSource      /** 来源*/
    , BrokerRecommend     /** 经纪人推荐状态*/
    , SalesTalk           /** 销售话术*/
    , VoucherType        /**代金券类型*/
    , post               /**职务*/


    , OrderStatus               /**订单状态*/
}
