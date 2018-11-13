package cn.lds.im.data;

import java.util.List;

/**
 * Created by colin on 15/12/25.
 */
public class ReceiptInfoModel {


    /**
     * status : success
     * data : {"amount":0,"enrolleeId":0,"enrolleeName":"string","expressCode":"string","expressNo":"string","id":0,"invoiceNo":"string","invoiceType":"INDIVIDUAL","no":"string","recipientAddress":"string","recipientName":"string","recipientPhone":"string","recipientZipCode":"string","status":"REQUESTED","title":"string","createdBy":"string","createdDate":"2017-07-27T04:57:44.441Z","lastModifiedBy":"string","lastModifiedDate":"2017-07-27T04:57:44.441Z"}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-27T04:57:44.441Z
     */

    private String status;
    private DataBean data;
    private String timestamp;
    private List<ErrorsBean> errors;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsBean> errors) {
        this.errors = errors;
    }

    public static class DataBean {
        /**
         * amount : 0
         * enrolleeId : 0
         * enrolleeName : string
         * expressCode : string
         * expressNo : string
         * id : 0
         * invoiceNo : string
         * invoiceType : INDIVIDUAL
         * no : string
         * recipientAddress : string
         * recipientName : string
         * recipientPhone : string
         * recipientZipCode : string
         * status : REQUESTED
         * title : string
         * createdBy : string
         * createdDate : 2017-07-27T04:57:44.441Z
         * lastModifiedBy : string
         * lastModifiedDate : 2017-07-27T04:57:44.441Z
         */

        private double amount;
        private int enrolleeId;
        private String enrolleeName;
        private String expressCode;
        private String expressNo;
        private int id;
        private String invoiceNo;
        private String invoiceType;
        private String no;
        private String recipientAddress;
        private String recipientName;
        private String recipientPhone;
        private String recipientZipCode;
        private String status;
        private String title;
        private String createdBy;
        private String createdDate;
        private String lastModifiedBy;
        private String lastModifiedDate;
        private String taxId;

        public String getTaxId() {
            return taxId;
        }

        public void setTaxId(String taxId) {
            this.taxId = taxId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getEnrolleeId() {
            return enrolleeId;
        }

        public void setEnrolleeId(int enrolleeId) {
            this.enrolleeId = enrolleeId;
        }

        public String getEnrolleeName() {
            return enrolleeName;
        }

        public void setEnrolleeName(String enrolleeName) {
            this.enrolleeName = enrolleeName;
        }

        public String getExpressCode() {
            return expressCode;
        }

        public void setExpressCode(String expressCode) {
            this.expressCode = expressCode;
        }

        public String getExpressNo() {
            return expressNo;
        }

        public void setExpressNo(String expressNo) {
            this.expressNo = expressNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public String getInvoiceType() {
            return invoiceType;
        }

        public void setInvoiceType(String invoiceType) {
            this.invoiceType = invoiceType;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getRecipientAddress() {
            return recipientAddress;
        }

        public void setRecipientAddress(String recipientAddress) {
            this.recipientAddress = recipientAddress;
        }

        public String getRecipientName() {
            return recipientName;
        }

        public void setRecipientName(String recipientName) {
            this.recipientName = recipientName;
        }

        public String getRecipientPhone() {
            return recipientPhone;
        }

        public void setRecipientPhone(String recipientPhone) {
            this.recipientPhone = recipientPhone;
        }

        public String getRecipientZipCode() {
            return recipientZipCode;
        }

        public void setRecipientZipCode(String recipientZipCode) {
            this.recipientZipCode = recipientZipCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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