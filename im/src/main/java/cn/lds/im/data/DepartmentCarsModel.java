package cn.lds.im.data;

import java.io.Serializable;
import java.util.List;

public class DepartmentCarsModel implements Serializable {


    /**
     * status : success
     * data : [{"id":13,"plateLicenseNo":"皖B48739","modelName":"小蚂蚁2017款 智臻版","locationId":1,"locationName":"联通芜湖市分公司"},{"id":47,"plateLicenseNo":"皖BD02536","modelName":"艾瑞泽5e2017款 智享版","locationId":1,"locationName":"联通芜湖市分公司"},{"id":48,"plateLicenseNo":"皖BD00026","modelName":"艾瑞泽5e2017款 智享版","locationId":1,"locationName":"联通芜湖市分公司"}]
     */

    private String status;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 13
         * plateLicenseNo : 皖B48739
         * modelName : 小蚂蚁2017款 智臻版
         * locationId : 1
         * locationName : 联通芜湖市分公司
         */

        private int id;
        private String plateLicenseNo;
        private String modelName;
        private int locationId;
        private String locationName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlateLicenseNo() {
            return plateLicenseNo;
        }

        public void setPlateLicenseNo(String plateLicenseNo) {
            this.plateLicenseNo = plateLicenseNo;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public int getLocationId() {
            return locationId;
        }

        public void setLocationId(int locationId) {
            this.locationId = locationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }
    }
}
