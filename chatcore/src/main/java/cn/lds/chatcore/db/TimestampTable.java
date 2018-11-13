package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by quwei on 2015/11/26.
 * 时间戳管理表
 */
@Table(name = "timestamp")
public class TimestampTable extends EntityBase{

    /** 默认的字段ID */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /** 登录账号 */
    @Column(column = "account")
    private String account;
    /** 时间戳类型:见枚举 TimestampType */
    @Column(column = "type")
    private String type;
    /** 时间戳 */
    @Column(column = "timestamp")
    private long timestamp;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
