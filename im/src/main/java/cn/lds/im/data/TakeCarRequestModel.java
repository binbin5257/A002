package cn.lds.im.data;

import java.util.List;

/**
 * Created by E0608 on 2017/12/27.
 */

public class TakeCarRequestModel {

    /**
     * description : string
     * id : 0
     * locationId : 0
     * picList : ["string"]
     * sanitaryState : false
     * scratch : false
     * wheelOk : false
     */

    private String description;
    private int id;
    private int locationId;
    private boolean sanitaryState;
    private boolean scratch;
    private boolean wheelOk;
    private List<String> picList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public boolean isSanitaryState() {
        return sanitaryState;
    }

    public void setSanitaryState(boolean sanitaryState) {
        this.sanitaryState = sanitaryState;
    }

    public boolean isScratch() {
        return scratch;
    }

    public void setScratch(boolean scratch) {
        this.scratch = scratch;
    }

    public boolean isWheelOk() {
        return wheelOk;
    }

    public void setWheelOk(boolean wheelOk) {
        this.wheelOk = wheelOk;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}
