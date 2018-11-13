package cn.lds.im.data;

import java.util.List;

/**
 * Created by colin on 15/12/25.
 */
public class MapSignParkModel {

    /**
     * status : success
     * data : [{"brandName":"string","chargingRule":"string","configuration":"string","id":0,"maxSustainedMileage":0,"pictureId":"string","plateLicenseNo":"string","seriesName":"string","sustainedMileage":0,"year":0}]
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-13T07:57:55.535Z
     */

    private String status;
    private String timestamp;
    private List<DataBean> data;
    private List<ErrorsBean> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsBean> errors) {
        this.errors = errors;
    }

    public static class DataBean {
        /**
         * brandName : string
         * chargingRule : string
         * configuration : string
         * id : 0
         * maxSustainedMileage : 0
         * pictureId : string
         * plateLicenseNo : string
         * seriesName : string
         * sustainedMileage : 0
         * year : 0
         */

        private String brandName;
        private String chargingRule;
        private String configuration;
        private String id;
        private int maxSustainedMileage;
        private String pictureId;
        private String plateLicenseNo;
        private String seriesName;
        private String sustainedMileage;
        private int year;
        private String modelId;
        private String locationName;
        private String locationAddress;
        private int locationId;
        private double locationLongitude;
        private double locationLatitude;

        public double getLocationLongitude() {
            return locationLongitude;
        }

        public void setLocationLongitude(double locationLongitude) {
            this.locationLongitude = locationLongitude;
        }

        public double getLocationLatitude() {
            return locationLatitude;
        }

        public void setLocationLatitude(double locationLatitude) {
            this.locationLatitude = locationLatitude;
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

        public String getLocationAddress() {
            return locationAddress;
        }

        public void setLocationAddress(String locationAddress) {
            this.locationAddress = locationAddress;
        }

        public String getModelId() {
            return modelId;
        }

        public void setModelId(String modelId) {
            this.modelId = modelId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getChargingRule() {
            return chargingRule;
        }

        public void setChargingRule(String chargingRule) {
            this.chargingRule = chargingRule;
        }

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getMaxSustainedMileage() {
            return maxSustainedMileage;
        }

        public void setMaxSustainedMileage(int maxSustainedMileage) {
            this.maxSustainedMileage = maxSustainedMileage;
        }

        public String getPictureId() {
            return pictureId;
        }

        public void setPictureId(String pictureId) {
            this.pictureId = pictureId;
        }

        public String getPlateLicenseNo() {
            return plateLicenseNo;
        }

        public void setPlateLicenseNo(String plateLicenseNo) {
            this.plateLicenseNo = plateLicenseNo;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        public String getSustainedMileage() {
            return sustainedMileage;
        }

        public void setSustainedMileage(String sustainedMileage) {
            this.sustainedMileage = sustainedMileage;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }

    public static class ErrorsBean {
        /**
         * field : string
         * errmsg : string
         * errcode : string
         */

        private String field;
        private String errmsg;
        private String errcode;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }
    }
}
