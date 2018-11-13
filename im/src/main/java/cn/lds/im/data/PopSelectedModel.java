package cn.lds.im.data;

import java.util.Map;

import cn.lds.chatcore.db.MasterTable;

/**
 * Created by colin on 15/12/25.
 */
public class PopSelectedModel {
    //码表
    private MasterTable masterTable;
    private boolean isSelected;
    //传入的集合
    private Map<String, Object> mapList;

    /**
     * 用于码表
     */
    public PopSelectedModel(boolean isSelected, MasterTable masterTable) {
        this.isSelected = isSelected;
        this.masterTable = masterTable;
    }

    /**
     * 用于传入集合选择后返回
     */
    public PopSelectedModel(Map<String, Object> mapList, boolean isSelected) {
        this.mapList = mapList;
        this.isSelected = isSelected;
    }

    public MasterTable getMasterTable() {
        return masterTable;
    }

    public void setMasterTable(MasterTable masterTable) {
        this.masterTable = masterTable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Map<String, Object> getMapList() {
        return mapList;
    }

    public void setMapList(Map<String, Object> mapList) {
        this.mapList = mapList;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
