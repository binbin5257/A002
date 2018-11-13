package cn.lds.im.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuqm on 2016/7/22.
 */
public class ControlReturnModel implements Parcelable {
    /**
     * status : success
     * data : {"operationId":"fakeOperationId","secondOfDuration":3,"canStop":false}
     */

    private String status;
    /**
     * operationId : fakeOperationId
     * secondOfDuration : 3
     * canStop : false
     */

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        private String operationId;
        private int secondOfDuration;
        private boolean canStop;

        public String getOperationId() {
            return operationId;
        }

        public void setOperationId(String operationId) {
            this.operationId = operationId;
        }

        public int getSecondOfDuration() {
            return secondOfDuration;
        }

        public void setSecondOfDuration(int secondOfDuration) {
            this.secondOfDuration = secondOfDuration;
        }

        public boolean isCanStop() {
            return canStop;
        }

        public void setCanStop(boolean canStop) {
            this.canStop = canStop;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.operationId);
            dest.writeInt(this.secondOfDuration);
            dest.writeByte(this.canStop ? (byte) 1 : (byte) 0);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.operationId = in.readString();
            this.secondOfDuration = in.readInt();
            this.canStop = in.readByte() != 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeParcelable(this.data, flags);
    }

    public ControlReturnModel() {
    }

    protected ControlReturnModel(Parcel in) {
        this.status = in.readString();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ControlReturnModel> CREATOR = new Parcelable.Creator<ControlReturnModel>() {
        @Override
        public ControlReturnModel createFromParcel(Parcel source) {
            return new ControlReturnModel(source);
        }

        @Override
        public ControlReturnModel[] newArray(int size) {
            return new ControlReturnModel[size];
        }
    };
}