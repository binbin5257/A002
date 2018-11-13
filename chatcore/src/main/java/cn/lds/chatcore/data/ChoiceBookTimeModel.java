package cn.lds.chatcore.data;

import java.io.Serializable;

/**
 * Created by pengwb on 2016/6/24.
 */
public class ChoiceBookTimeModel implements Serializable {
    /**
     * taketimetext :
     * appointmentPickedUpTime : 1470661032783
     * appointmentDroppedOffTime : 1470679032783
     * returntimetext :
     */

    private String taketimetext;
    private long appointmentPickedUpTime;
    private long appointmentDroppedOffTime;
    private String returntimetext;

    public String getTaketimetext() {
        return taketimetext;
    }

    public void setTaketimetext(String taketimetext) {
        this.taketimetext = taketimetext;
    }

    public long getAppointmentPickedUpTime() {
        return appointmentPickedUpTime;
    }

    public void setAppointmentPickedUpTime(long appointmentPickedUpTime) {
        this.appointmentPickedUpTime = appointmentPickedUpTime;
    }

    public long getAppointmentDroppedOffTime() {
        return appointmentDroppedOffTime;
    }

    public void setAppointmentDroppedOffTime(long appointmentDroppedOffTime) {
        this.appointmentDroppedOffTime = appointmentDroppedOffTime;
    }

    public String getReturntimetext() {
        return returntimetext;
    }

    public void setReturntimetext(String returntimetext) {
        this.returntimetext = returntimetext;
    }
}
