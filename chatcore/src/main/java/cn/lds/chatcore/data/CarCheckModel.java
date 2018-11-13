package cn.lds.chatcore.data;

/**
 * Created by E0608 on 2018/1/3.
 */

public class CarCheckModel {

    /**
     * status : success
     * data : {"id":null,"accSuccess":true,"chargeSuccess":true}
     */

    private String status;
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
        /**
         * id : null
         * accSuccess : true
         * chargeSuccess : true
         */

        private Object id;
        private boolean accSuccess;
        private boolean chargeSuccess;
        private boolean doorLockedSuccess;
        private boolean lightOffSuccess;

        public boolean isDoorLockedSuccess() {
            return doorLockedSuccess;
        }

        public void setDoorLockedSuccess(boolean doorLockedSuccess) {
            this.doorLockedSuccess = doorLockedSuccess;
        }

        public boolean isLightOffSuccess() {
            return lightOffSuccess;
        }

        public void setLightOffSuccess(boolean lightOffSuccess) {
            this.lightOffSuccess = lightOffSuccess;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public boolean isAccSuccess() {
            return accSuccess;
        }

        public void setAccSuccess(boolean accSuccess) {
            this.accSuccess = accSuccess;
        }

        public boolean isChargeSuccess() {
            return chargeSuccess;
        }

        public void setChargeSuccess(boolean chargeSuccess) {
            this.chargeSuccess = chargeSuccess;
        }
    }
}
