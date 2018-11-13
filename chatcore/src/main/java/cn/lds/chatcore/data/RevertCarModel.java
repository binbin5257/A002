package cn.lds.chatcore.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuqm on 2016/6/24.
 */
public class RevertCarModel implements Parcelable {
    private String id;//hub版，存储NO
    private String time;
    private double longitude;
    private double latitude;
    private String name;

    public RevertCarModel() {
    }

    public RevertCarModel(String id, String time, double longitude, double latitude, String name) {
        this.id = id;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RevertCarModel{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.time);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.name);
    }

    protected RevertCarModel(Parcel in) {
        this.id = in.readString();
        this.time = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<RevertCarModel> CREATOR = new Parcelable.Creator<RevertCarModel>() {
        @Override
        public RevertCarModel createFromParcel(Parcel source) {
            return new RevertCarModel(source);
        }

        @Override
        public RevertCarModel[] newArray(int size) {
            return new RevertCarModel[size];
        }
    };
}
