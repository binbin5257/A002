package cn.lds.im.data;

import java.util.List;

/**
 * 违章
 */

public class IllegalListModel {

    /**
     * status : success
     * data : [{"cleaned":true,"id":0,"no":"string","enrolleeId":0,"name":"string","vehicleId":0,"plateLicenseNo":"string","illegalLocation":"string","illegalTime":"2017-07-25T02:25:35.902Z","illegalDescription":"string","price":0,"mark":0,"orderNo":"string","brandName":"string","seriesName":"string","modelName":"string","picId":"string","receiptPics":["string"],"vehicleIllegalOrderType":"NOT_RELATED","createdBy":"string","createdDate":"2017-07-25T02:25:35.902Z","lastModifiedBy":"string","lastModifiedDate":"2017-07-25T02:25:35.902Z"}]
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-25T02:25:35.902Z
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
         * cleaned : true
         * id : 0
         * no : string
         * enrolleeId : 0
         * name : string
         * vehicleId : 0
         * plateLicenseNo : string
         * illegalLocation : string
         * illegalTime : 2017-07-25T02:25:35.902Z
         * illegalDescription : string
         * price : 0
         * mark : 0
         * orderNo : string
         * brandName : string
         * seriesName : string
         * modelName : string
         * picId : string
         * receiptPics : ["string"]
         * vehicleIllegalOrderType : NOT_RELATED
         * createdBy : string
         * createdDate : 2017-07-25T02:25:35.902Z
         * lastModifiedBy : string
         * lastModifiedDate : 2017-07-25T02:25:35.902Z
         */

        private boolean cleaned;
        private int id;
        private String no;
        private String enrolleeId;
        private String name;
        private String vehicleId;
        private String plateLicenseNo;
        private String illegalLocation;
        private String illegalTime;
        private String illegalDescription;
        private String price;
        private String mark;
        private String orderNo;
        private String brandName;
        private String seriesName;
        private String modelName;
        private String picId;
        private String vehicleIllegalOrderType;
        private String createdBy;
        private String createdDate;
        private String lastModifiedBy;
        private String lastModifiedDate;
        private List<String> receiptPics;

        public boolean isCleaned() {
            return cleaned;
        }

        public void setCleaned(boolean cleaned) {
            this.cleaned = cleaned;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getEnrolleeId() {
            return enrolleeId;
        }

        public void setEnrolleeId(String enrolleeId) {
            this.enrolleeId = enrolleeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getPlateLicenseNo() {
            return plateLicenseNo;
        }

        public void setPlateLicenseNo(String plateLicenseNo) {
            this.plateLicenseNo = plateLicenseNo;
        }

        public String getIllegalLocation() {
            return illegalLocation;
        }

        public void setIllegalLocation(String illegalLocation) {
            this.illegalLocation = illegalLocation;
        }

        public String getIllegalTime() {
            return illegalTime;
        }

        public void setIllegalTime(String illegalTime) {
            this.illegalTime = illegalTime;
        }

        public String getIllegalDescription() {
            return illegalDescription;
        }

        public void setIllegalDescription(String illegalDescription) {
            this.illegalDescription = illegalDescription;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getPicId() {
            return picId;
        }

        public void setPicId(String picId) {
            this.picId = picId;
        }

        public String getVehicleIllegalOrderType() {
            return vehicleIllegalOrderType;
        }

        public void setVehicleIllegalOrderType(String vehicleIllegalOrderType) {
            this.vehicleIllegalOrderType = vehicleIllegalOrderType;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public String getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(String lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        public List<String> getReceiptPics() {
            return receiptPics;
        }

        public void setReceiptPics(List<String> receiptPics) {
            this.receiptPics = receiptPics;
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
