package cn.lds.im.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuqm on 2016/7/22.
 */
public class OperationReturnModel implements Parcelable {

    /**
     * status : success
     * data : SUCCEED
     */

    private String status;
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.data);
    }

    public OperationReturnModel() {
    }

    protected OperationReturnModel(Parcel in) {
        this.status = in.readString();
        this.data = in.readString();
    }

    public static final Parcelable.Creator<OperationReturnModel> CREATOR = new Parcelable.Creator<OperationReturnModel>() {
        @Override
        public OperationReturnModel createFromParcel(Parcel source) {
            return new OperationReturnModel(source);
        }

        @Override
        public OperationReturnModel[] newArray(int size) {
            return new OperationReturnModel[size];
        }
    };
}