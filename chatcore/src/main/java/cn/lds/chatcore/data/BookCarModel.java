package cn.lds.chatcore.data;

import java.io.Serializable;

/**
 * Created by pengwb on 2016/6/24.
 */
public class BookCarModel implements Serializable {


    /**
     * vehicleModelNo : StackA_vehicleModel1
     * scheduledPickedUpTime : 1474356684167
     * scheduledDroppedOffTime : 1474374684167
     * locationNo : StackA_Location1N7ZYN3SC14M
     */

    private String vehicleModelNo;
    private long scheduledPickedUpTime;
    private long scheduledDroppedOffTime;
    private String locationNo;

    public String getVehicleModelNo() {
        return vehicleModelNo;
    }

    public void setVehicleModelNo(String vehicleModelNo) {
        this.vehicleModelNo = vehicleModelNo;
    }

    public long getScheduledPickedUpTime() {
        return scheduledPickedUpTime;
    }

    public void setScheduledPickedUpTime(long scheduledPickedUpTime) {
        this.scheduledPickedUpTime = scheduledPickedUpTime;
    }

    public long getScheduledDroppedOffTime() {
        return scheduledDroppedOffTime;
    }

    public void setScheduledDroppedOffTime(long scheduledDroppedOffTime) {
        this.scheduledDroppedOffTime = scheduledDroppedOffTime;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    /**
     * 重置属性
     */
    public void resetModel() {
        setLocationNo("");
        setVehicleModelNo("");
        setScheduledDroppedOffTime(0);
        setScheduledPickedUpTime(0);
    }
}
