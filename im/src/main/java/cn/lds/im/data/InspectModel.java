package cn.lds.im.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqm on 2016/6/21.
 */
public class InspectModel implements Parcelable {
    /**
     * scratch : 是否有划痕 true
     * sanitaryState : 整洁程度 CLEAN干净，GARBAGE有杂物，DIRTY很脏
     * wheelOk : 轮胎情况 false
     * picList : [1,2]
     */

    private boolean scratch;
    private boolean sanitaryState;
    private boolean wheelOk;
    private List<String> picList;
    private String description;


    public boolean isScratch() {
        return scratch;
    }

    public void setScratch(boolean scratch) {
        this.scratch = scratch;
    }

    public boolean isSanitaryState() {
        return sanitaryState;
    }

    public void setSanitaryState(boolean sanitaryState) {
        this.sanitaryState = sanitaryState;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.scratch ? (byte) 1 : (byte) 0);
        dest.writeByte(this.sanitaryState ? (byte) 1 : (byte) 0);
        dest.writeByte(this.wheelOk ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.picList);
        dest.writeString(this.description);
    }

    public InspectModel() {
    }

    protected InspectModel(Parcel in) {
        this.scratch = in.readByte() != 0;
        this.sanitaryState = in.readByte() != 0;
        this.wheelOk = in.readByte() != 0;
        this.picList = in.createStringArrayList();
        this.description = in.readString();
    }

    public static final Creator<InspectModel> CREATOR = new Creator<InspectModel>() {
        @Override
        public InspectModel createFromParcel(Parcel source) {
            return new InspectModel(source);
        }

        @Override
        public InspectModel[] newArray(int size) {
            return new InspectModel[size];
        }
    };
}
