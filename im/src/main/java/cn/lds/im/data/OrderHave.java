package cn.lds.im.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuqm on 2016/6/20.
 */
public class OrderHave implements Parcelable {
    /**
     * status : success
     * data : true
     */

    private String status;
    private boolean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OrderHave{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeByte(this.data ? (byte) 1 : (byte) 0);
    }

    public OrderHave() {
    }

    protected OrderHave(Parcel in) {
        this.status = in.readString();
        this.data = in.readByte() != 0;
    }

    public static final Parcelable.Creator<OrderHave> CREATOR = new Parcelable.Creator<OrderHave>() {
        @Override
        public OrderHave createFromParcel(Parcel source) {
            return new OrderHave(source);
        }

        @Override
        public OrderHave[] newArray(int size) {
            return new OrderHave[size];
        }
    };
}
