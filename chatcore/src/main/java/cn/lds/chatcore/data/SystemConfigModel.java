package cn.lds.chatcore.data;

/**
 * Created by pengwb on 2016/6/24.
 */
public class SystemConfigModel {

    /**
     * status : success
     * data : {"minMinuteScheduledInterval":60,"scheduledAutoCancelMinute":20,instantAutoCancelMinute : 20}
     */

    private String status;
    /**
     * minMinuteScheduledInterval : 60
     * scheduledAutoCancelMinute : 20
     * instantAutoCancelMinute : 20
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

    public static class DataBean {
        private int minMinuteScheduledInterval;
        private int scheduledAutoCancelMinute;
        private int instantAutoCancelMinute;
        private int instantPickedUpTimeLimit;
        private String serviceTel;

        public String getServiceTel() {
            return serviceTel;
        }

        public void setServiceTel(String serviceTel) {
            this.serviceTel = serviceTel;
        }

        public int getInstantPickedUpTimeLimit() {
            return instantPickedUpTimeLimit;
        }

        public void setInstantPickedUpTimeLimit(int instantPickedUpTimeLimit) {
            this.instantPickedUpTimeLimit = instantPickedUpTimeLimit;
        }

        public int getInstantAutoCancelMinute() {
            return instantAutoCancelMinute;
        }

        public void setInstantAutoCancelMinute(int instantAutoCancelMinute) {
            this.instantAutoCancelMinute = instantAutoCancelMinute;
        }

        public int getMinMinuteScheduledInterval() {
            return minMinuteScheduledInterval;
        }

        public void setMinMinuteScheduledInterval(int minMinuteScheduledInterval) {
            this.minMinuteScheduledInterval = minMinuteScheduledInterval;
        }

        public int getScheduledAutoCancelMinute() {
            return scheduledAutoCancelMinute;
        }

        public void setScheduledAutoCancelMinute(int scheduledAutoCancelMinute) {
            this.scheduledAutoCancelMinute = scheduledAutoCancelMinute;
        }
    }
}
