package cn.lds.chatcore.manager;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.CoreHttpApiResult;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.JsonHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.db.MasterTable;
import cn.lds.chatcore.enums.MasterType;
import cn.lds.chatcore.event.HttpRequestEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by quwei on 2015/12/24.
 */
public class MasterManager extends AbstractManager {
    public static String _TAG = MasterManager.class.getSimpleName();
    protected static MasterManager instance;

    //private DbUtils dbUtils = DbHelper.getDbUtils();

//    static {
//        instance = new MasterManager();
//        MyApplication.getInstance().addManager(instance);
//        EventBus.getDefault().register(instance);
//    }

    public static MasterManager getInstance() {
        if (instance == null) {
            try {
                instance = new MasterManager();
                MyApplication.getInstance().addManager(instance);
                EventBus.getDefault().register(instance);
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    /**
     * 写入数据库
     *
     * @param table
     * @return
     */
    private int write(MasterTable table) {
        try {
            DbHelper.getDbUtils().saveBindingId(table);
        } catch (Exception e) {
            LogHelper.e(_TAG, e);
        }
        return table.getId();
    }

    /**
     * 从本地删除（通过类型）
     *
     * @param masterType
     */
    public void deleteByType(MasterType masterType) throws Exception {
        // 从本地删除
        DbHelper.getDbUtils().delete(MasterTable.class, WhereBuilder.b("mastertype", "=", masterType.name()));
    }

    /**
     * 根据码表类型、父键查找码表列表
     *
     * @param masterType   码表类型
     * @param strParentKey 父键
     * @return
     */
    public List<MasterTable> getListByTypeAndParentKey(MasterType masterType, String strParentKey, String strTreeLevel) {
        List<MasterTable> list = null;
        try {
            // 构建查询条件
            Selector selector = Selector.from(MasterTable.class)
                    .where("mastertype", "=", masterType.name());
            if (!ToolsHelper.isNull(strParentKey)) {
                selector.and(WhereBuilder.b("parentkey", "=", ToolsHelper.toString(strParentKey)));
            }
            if (!ToolsHelper.isNull(strTreeLevel)) {
                selector.and(WhereBuilder.b("treelevel", "=", ToolsHelper.toString(strTreeLevel)));
            }
            selector.orderBy("sort_order", false);
            list = DbHelper.getDbUtils().findAll(selector);
        } catch (Exception e) {
            LogHelper.e(_TAG, e);
        }
        return list;
    }

    /**
     * 根据码表类型和键查找码表
     *
     * @param masterType 码表类型
     * @param strKey     键
     * @return
     */
    public MasterTable getTableByTypeAndKey(MasterType masterType, String strKey) {
        MasterTable talble = null;
        try {
            talble = DbHelper.getDbUtils().findFirst(Selector.from(MasterTable.class)
                    .where("mastertype", "=", masterType.name())
                    .and(WhereBuilder.b("key", "=", ToolsHelper.toString(strKey))));
        } catch (Exception e) {
            LogHelper.e(_TAG, e);
        }
        return talble;
    }

    /**
     * 根据码表类型和键查找显示的文本
     *
     * @param masterType 码表类型
     * @param strKey     键
     * @return
     */
    public String getTextByTypeAndKey(MasterType masterType, String strKey) {
        MasterTable talble = this.getTableByTypeAndKey(masterType, strKey);
        if (talble == null) {
            return "";
        } else {
            return ToolsHelper.toString(talble.getText());
        }

    }

    /**
     * 根据码表类型统计数据条数
     *
     * @param masterType
     * @return
     */
    public long countByType(MasterType masterType) {
        long count = 0;
        try {
            // 构建查询条件
            Selector selector = Selector.from(MasterTable.class)
                    .where("mastertype", "=", masterType.name());
            count = DbHelper.getDbUtils().count(selector);
        } catch (Exception e) {
            LogHelper.e(_TAG, e);
        }
        return count;
    }

    /**
     * 同步码表
     */
    public void request() {
//        CoreHttpApi.syncMasterCode();
    }

    /**
     * API请求处理
     *
     * @param event
     */
    public void onEventBackgroundThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();

        if (!(CoreHttpApiKey.syncMasterCode.equals(apiNo) || CoreHttpApiKey.login.equals(apiNo)))
            return;

        switch (apiNo) {
            case CoreHttpApiKey.syncMasterCode:
                syncMasterCode(httpResult);
                break;
            case CoreHttpApiKey.login:
                A001();
                break;
            default:
                break;
        }
    }

    /**
     * 手动发起码表
     * */
    private void A001() {
        //手动添加本地码表。男女
        String strResult = CoreHttpApiResult.getMaster();
        HttpResult httpResult = new HttpResult(CoreHttpApiKey.syncMasterCode, null, strResult,
                null);
        httpResult.setExtras(null);
        EventBus.getDefault().post(new HttpRequestEvent(httpResult));
    }

    /**
     * 同步码表数据
     *
     * @param httpResult
     */
    private void syncMasterCode(HttpResult httpResult) {
        try {
            JSONObject result = httpResult.getJsonResult();
            if (null == result) {
                result = new JSONObject(httpResult.getResult());
            }
            JSONArray data = result.optJSONArray("data");
            if (data != null) {
                for (int j = 0; j < data.length(); j++) {
                    JSONObject jsonObject = (JSONObject) data.get(j);
                    // 获取码表类型
                    String strType = JsonHelper.getString(jsonObject, "categoryKey");
                    MasterType type = MasterType.valueOf(strType);
                    // 根据类型删除码表 TODO
                    this.deleteByType(type);
                    // 插入码表
                    JSONArray jsonData = jsonObject.optJSONArray("dictionarys");
                    int len = jsonData.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonItem = (JSONObject) jsonData.get(i);
                        // 获取1级
                        MasterTable table = this.getMasterTableByJSONObject(type, jsonItem);
                        // 设置层级
                        table.setTreelevel("1");
                        this.write(table);
                        // 获取子集合
                        JSONArray children = jsonItem.optJSONArray("children");
                        if (children != null) {
                            List<MasterTable> list = this.getListMasterTableByJSONArray(type, children);
                            for (MasterTable tableChild : list) {
                                // 设置层级
                                tableChild.setTreelevel("2");
                                this.write(tableChild);
                            }
                        }
                    }
                    // 更新时间戳
//                    long timestemp = JsonHelper.getLong(jsonObject, "updatedTime");
//                    TimestampManager.getInstance().update(TimestampType.getValue(strType), new Date().getTime());
                }
            }
        } catch (Exception e) {
            LogHelper.e(this.getClass().getName(), e);
        }

        MyApplication.essential.setMasterAvailable(true);
    }

    /**
     * 获取 List<MasterTable>
     *
     * @param type
     * @param jsonArray
     * @return
     */
    private List<MasterTable> getListMasterTableByJSONArray(MasterType type, JSONArray jsonArray) {
        List<MasterTable> list = new ArrayList<MasterTable>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            MasterTable table = this.getMasterTableByJSONObject(type, jsonObject);
            list.add(table);
        }
        return list;
    }

    /**
     * 获取 MasterTable
     *
     * @param type
     * @param jsonObject
     * @return
     */
    private MasterTable getMasterTableByJSONObject(MasterType type, JSONObject jsonObject) {
        // 获取值
        String key = JsonHelper.getString(jsonObject, "key");
        String value = JsonHelper.getString(jsonObject, "value");
        String text = JsonHelper.getString(jsonObject, "text");
        String parentKey = JsonHelper.getString(jsonObject, "parentKey");
        int order = JsonHelper.getInt(jsonObject, "order");
        String description = JsonHelper.getString(jsonObject, "description");
        // 构建table
        MasterTable table = new MasterTable();
        table.setMastertype(type.name());
        table.setKey(key);
        table.setValue(value);
        table.setText(text);
        table.setParentKey(parentKey);
        table.setOrder(order);
        table.setDescription(description);
        return table;
    }
}
