package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by quwei on 2015/12/24.
 */
@Table(name = "master")
public class MasterTable extends EntityBase{
    /** 默认的字段ID */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /** 码表类型 */
    @Column(column = "mastertype")
    private String mastertype;
    /** 键 */
    @Column(column = "key")
    private String key;
    /** 键值 */
    @Column(column = "value")
    private String value;
    /** 键名 */
    @Column(column = "text")
    private String text;
    /** 父键 */
    @Column(column = "parentkey")
    private String parentKey;
    /** 排序 */
    @Column(column = "sort_order")
    private int order;
    /** 描述 */
    @Column(column = "description")
    private String description;
    /** 层级 */
    @Column(column = "treelevel")
    private String treelevel;

    public String getMastertype() {
        return mastertype;
    }

    public void setMastertype(String mastertype) {
        this.mastertype = mastertype;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTreelevel() {
        return treelevel;
    }

    public void setTreelevel(String treelevel) {
        this.treelevel = treelevel;
    }
}
