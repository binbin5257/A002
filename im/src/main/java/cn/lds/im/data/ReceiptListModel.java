package cn.lds.im.data;

import java.util.List;

/**
 * Created by gaihd on 17/07/20.
 */
public class ReceiptListModel {

    /**
     * status : success
     * data : [{"amount":0,"enrolleeId":0,"enrolleeName":"string","expressCode":"string","expressNo":"string","id":0,"invoiceNo":"string","invoiceType":"INDIVIDUAL","no":"string","recipientAddress":"string","recipientName":"string","recipientPhone":"string","recipientZipCode":"string","status":"REQUESTED","title":"string","createdBy":"string","createdDate":"2017-07-20T01:51:48.918Z","lastModifiedBy":"string","lastModifiedDate":"2017-07-20T01:51:48.918Z"}]
     * pageable : {"totalElements":0,"numberOfElements":0,"totalPages":0,"number":0,"first":false,"last":false,"size":0,"fromNumber":0,"toNumber":0}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-20T01:51:48.918Z
     */

    private String status;
    private PageableBean pageable;
    private String timestamp;
    private List<DataBean> data;
    private List<ErrorsBean> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PageableBean getPageable() {
        return pageable;
    }

    public void setPageable(PageableBean pageable) {
        this.pageable = pageable;
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

    public static class PageableBean {
        /**
         * totalElements : 0
         * numberOfElements : 0
         * totalPages : 0
         * number : 0
         * first : false
         * last : false
         * size : 0
         * fromNumber : 0
         * toNumber : 0
         */

        private int totalElements;
        private int numberOfElements;
        private int totalPages;
        private int number;
        private boolean first;
        private boolean last;
        private int size;
        private int fromNumber;
        private int toNumber;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getFromNumber() {
            return fromNumber;
        }

        public void setFromNumber(int fromNumber) {
            this.fromNumber = fromNumber;
        }

        public int getToNumber() {
            return toNumber;
        }

        public void setToNumber(int toNumber) {
            this.toNumber = toNumber;
        }
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
         * createdDate : 2017-07-20T01:51:48.918Z
         * lastModifiedBy : string
         * lastModifiedDate : 2017-07-20T01:51:48.918Z
         */

        private String amount;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
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