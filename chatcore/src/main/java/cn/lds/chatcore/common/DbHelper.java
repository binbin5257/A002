package cn.lds.chatcore.common;

import android.database.Cursor;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.table.Column;
import com.lidroid.xutils.db.table.Finder;
import com.lidroid.xutils.db.table.Table;
import com.lidroid.xutils.db.table.TableUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.db.ContactsRequestTable;
import cn.lds.chatcore.db.FilesTable;
import cn.lds.chatcore.db.MasterTable;
import cn.lds.chatcore.db.SettingsTable;
import cn.lds.chatcore.db.TimestampTable;
import cn.lds.chatcore.db.VersionTable;

/**
 * Author: wyouflf Date: 13-11-12 Time: 上午10:24
 */
public class DbHelper {

    private static String TAG = DbHelper.class.getSimpleName();

    private DbHelper() {
    }


    private static String lastAccount = "";

    public static boolean initDbUtils(String dbName) {

        LogHelper.d("数据库共通(" + dbName + ")：创建数据库");
        lastAccount = dbName;
        MyApplication.dbUtils = DbUtils.create(MyApplication.getInstance().getApplicationContext(), dbName
                , 3, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
                LogHelper.d("数据库共通(" + lastAccount + ")：当前数据库版本" + oldVersion + ",最新数据库版本" + newVersion);
                if (newVersion > oldVersion) {
                    // TODO:实例名称在变成动态获取的就ok
                    String packageName = "cn.lds.chatcore.db.";
                    updateDb(db, packageName + "AccountsTable");
                    updateDb(db, packageName + "ChatSessionTable");
                    updateDb(db, packageName + "ContactsRequestTable");
                    updateDb(db, packageName + "ContactsTable");
                    updateDb(db, packageName + "FilesTable");
                    updateDb(db, packageName + "MasterTable");
                    updateDb(db, packageName + "MessagesTable");
                    updateDb(db, packageName + "MucMembersTable");
                    updateDb(db, packageName + "MucTable");
                    updateDb(db, packageName + "PublicAccountsTable");
                    updateDb(db, packageName + "SettingsTable");
                    updateDb(db, packageName + "TagMembersTable");
                    updateDb(db, packageName + "TagTable");
                    updateDb(db, packageName + "TimestampTable");
                    updateDb(db, packageName + "VcardsTable");
                    updateDb(db, packageName + "VersionTable");
                    updateDb(db, packageName + "OrganizationTable");
                    updateDb(db, packageName + "OrganizationMemberTable");
                    updateDb(db, packageName + "ThirdAppClassTable");
                    updateDb(db, packageName + "PublicWebTable");
                    updateDb(db, packageName + "TodoTaskTable");
                }
            }
        });
        // 创建表
        try {
            LogHelper.d("数据库共通(" + dbName + ")：创建表 sta");
            MyApplication.dbUtils.createTableIfNotExist(AccountsTable.class);
            MyApplication.dbUtils.createTableIfNotExist(ContactsRequestTable.class);
            MyApplication.dbUtils.createTableIfNotExist(FilesTable.class);
            MyApplication.dbUtils.createTableIfNotExist(MasterTable.class);
            MyApplication.dbUtils.createTableIfNotExist(SettingsTable.class);
            MyApplication.dbUtils.createTableIfNotExist(TimestampTable.class);
            MyApplication.dbUtils.createTableIfNotExist(VersionTable.class);
            LogHelper.d("数据库共通(" + dbName + ")：创建表 end");
        } catch (Exception ex) {
            LogHelper.e("创建表", ex);
        }
        LogHelper.d("数据库共通(" + dbName + ")：配置数据库 sta");
        // 开启数据库事务
        MyApplication.dbUtils.configAllowTransaction(true);
        // debug，输出sql语句
        //MyApplication.dbUtils.configDebug(true);
        LogHelper.d("数据库共通(" + dbName + ")：配置数据库 end");
        return true;
    }

    /**
     * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
     *
     * @return
     */
    public static DbUtils getDbUtils() {
        if (MyApplication.dbUtils == null) {
            LogHelper.d("数据库共通(" + lastAccount + ")：获取数据库对象失败、重新初始化dbUtils");
            initDbUtils(CacheHelper.getAccount());
        }
        return MyApplication.dbUtils;
    }


    /**
     * 升级表
     *
     * @param db
     */
    private static void updateDb(DbUtils db, String clazzName) {
        try {
            Class<?> c = (Class<?>) Class.forName(clazzName);
            if (db.tableIsExist(c)) {
                Table table = Table.get(db, c);


                List<String> dbFildsList = new ArrayList<String>();
                String str = "select * from " + table.tableName;
                Cursor cursor = db.execQuery(str);
                int count = cursor.getColumnCount();
                for (int i = 0; i < count; i++) {
                    dbFildsList.add(cursor.getColumnName(i));
                }
                cursor.close();

                Collection columns = table.columnMap.values();
                Iterator var7 = columns.iterator();
                while (var7.hasNext()) {
                    Column column = (Column) var7.next();
                    if (!(column instanceof Finder)) {
                        String fildName = column.getColumnName();
                        if (!isExist(dbFildsList, fildName)) {
                            LogHelper.d("数据库共通(" + lastAccount + ")：升级数据库,在" + table.tableName + "表中增加字段" + fildName);
                            try {
                                db.execNonQuery("alter table " + table.tableName + " add " + fildName + " " + column.getColumnDbType());
                            } catch (Exception e) {
                                LogHelper.e("数据库共通(" + lastAccount + ")：", e);
                            }
                        }
                    }
                }
                // 处理索引
                String execAfterTableCreated = TableUtils.getExecAfterTableCreated(c);
                if (!ToolsHelper.isNull(execAfterTableCreated)) {
                    LogHelper.d("数据库共通(" + lastAccount + ")：升级数据库,在" + table.tableName + "表中增加索引" + execAfterTableCreated);
                    try {
                        db.execNonQuery(execAfterTableCreated);
                    } catch (Exception e) {
                        LogHelper.e("数据库共通(" + lastAccount + ")：", e);
                    }
                }

            }
        } catch (Exception e) {
            LogHelper.e("数据库共通(" + lastAccount + ")：", e);
        }
    }

    /**
     * 判断字段是否纯在
     *
     * @param dbFildsList
     * @param fildName
     * @return
     */
    private static boolean isExist(List<String> dbFildsList, String fildName) {
        if (dbFildsList == null) {
            return false;
        }
        if (ToolsHelper.isNull(fildName)) {
            return false;
        }
        for (String s : dbFildsList) {
            if (fildName.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
