package cn.lds.chatcore.manager;

import com.lidroid.xutils.db.sqlite.Selector;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.db.TimestampTable;
import cn.lds.chatcore.enums.TimestampType;

/**
 * 时间戳管理
 */
public class TimestampManager {

	public static String TAG = TimestampManager.class.getSimpleName();
	protected static TimestampManager instance;
	//private DbUtils dbUtils = DbHelper.getDbUtils();

//	static {
//		instance = new TimestampManager();
//		MyApplication.getInstance().addManager(instance);
//	}

	public static TimestampManager getInstance() {
		if(instance == null){
			try {
				instance = new TimestampManager();
				MyApplication.getInstance().addManager(instance);
			}catch (Exception ex){
				LogHelper.e("初始化Manager", ex);
			}
		}
		return instance;
	}

	/**
	 * 根据类型查找
	 * @param type 时间戳类型
	 * @return 
	 */
	 public TimestampTable findByTimestampType(TimestampType type) {
		TimestampTable table = null;
		try {
			table = DbHelper.getDbUtils().findFirst(Selector.from(TimestampTable.class).where("account", "=", CacheHelper
					.getAccount())
					.and("type", "=", type.value()));
			
			if(null == table){
				table = new TimestampTable();
				table.setAccount(CacheHelper.getAccount());
				table.setType(type.value());
				table.setTimestamp(0);
				DbHelper.getDbUtils().save(table);
			}
		} catch (Exception e) {
			LogHelper.e(TAG,e);
		}
		return table;
	}

	/**
	 * 获取时间戳
	 * @param type
	 * @return
	 */
	public long getTimestamp(TimestampType type) {
		TimestampTable table = findByTimestampType(type);
		return table.getTimestamp();
	}
	
	/**
	 * 更新时间戳
	 * @param type
	 */
	public void update(TimestampType type, long timestamp) {
		try {
			TimestampTable table = this.findByTimestampType(type);
			table.setTimestamp(timestamp);
			DbHelper.getDbUtils().update(table, "timestamp");
		} catch (Exception e) {
			LogHelper.e(TAG, e);
		}
	}
}
