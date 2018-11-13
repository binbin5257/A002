package cn.lds.chatcore.enums;

/**
 * 时间戳类型
 */
public enum TimestampType {
    UNKNOWN("UNKNOWN")    /* 未知 */,
    CONTACT("CONTACT")    /* 联系人 */,
    CHAT("CHAT")       /* 单聊 */,
    MUC("MUC")        /* 群聊 */,
    PUBLIC("PUBLIC")     /* 公众号 */,
    PUBLICWEB("PUBLICWEB")     /* 第三方应用 */,
    TAG("TAG")        /* 标签 */
    // 码表 开始
    , Region("Region")                          /** 地区 */
    , CustomerGrade("CustomerGrade")            /** 客户等级 */
    , BrokerGrade("BrokerGrade")                /**经纪人等级*/
    , Sexuality("Sexuality")                    /** 性别 */
    , ThirdMerchant("ThirdMerchant")            /** 服务机构 */
    , OpportunityStage("OpportunityStage")      /**商机阶段*/
    , PurchaseTime("PurchaseTime")              /**购车时间*/
    , PurchaseBudget("PurchaseBudget")          /**购车预算*/
    , FailedCause("FailedCause")                /**战败原因*/
    , LeadsStatus("LeadsStatus")                /**线索状态*/
    , TaskThemes("TaskThemes")                  /**任务主题*/
    , TaskRelatedMatters("TaskRelatedMatters")  /**相关事项(任务)*/
    , TaskPriority("TaskPriority")              /**优先级(任务)*/
    , TaskStatus("TaskStatus")                  /**任务状态*/
    , OppoTypes("OppoTypes")                    /**商机类型*/
    , IntentVehicleType("IntentVehicleType")    /** 购车类型*/
    , PurchaseNumber("PurchaseNumber")          /** 购车数量*/
    , ClientIdentityType("ClientIdentityType")  /** 会员身份、角色*/
    , CustomerSource("CustomerSource")          /** 来源*/
    , BrokerRecommend ("BrokerRecommend")       /** 经纪人推荐状态*/
    , SalesTalk ("SalesTalk")                   /** 销售话术*/
    ,VoucherType("VoucherType")                /**代金券类型*/
    // 码表 结束
    // 组织 开始
    ,Organization("Organization")                /**组织*/
    ,OrganizationMember("OrganizationMember")    /**组织成员*/
    ,post("post")                               /**职务*/
    // 组织 结束
    // 其他 开始
    ,ThirdAppClass("ThirdAppClass")             /**第三方应用*/
    ,TodoTask("TodoTask")                       /**待办*/
    // 其他 结束
    ;

    private String value = "UNKNOWN";

    TimestampType(final String value) {
        this.value = value;
    }

    public static TimestampType getValue(final String value) {
        for (final TimestampType type : TimestampType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String value() {
        return this.value;
    }
}
