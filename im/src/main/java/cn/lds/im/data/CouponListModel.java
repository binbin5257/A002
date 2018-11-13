package cn.lds.im.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaihd on 17/07/20.
 */
public class CouponListModel{


    /**
     * status : success
     * data : [{"description":"string","endTime":"2017-07-20T01:51:48.891Z","id":0,"limitedPrice":0,"no":"string","price":0,"startTime":"2017-07-20T01:51:48.891Z","status":"RELEASE","title":"string","useTime":"2017-07-20T01:51:48.891Z"}]
     * pageable : {"totalElements":0,"numberOfElements":0,"totalPages":0,"number":0,"first":false,"last":false,"size":0,"fromNumber":0,"toNumber":0}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-20T01:51:48.892Z
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

    public static class PageableBean implements Serializable{
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

    public static class DataBean implements Serializable{
        /**
         * description : string
         * endTime : 2017-07-20T01:51:48.891Z
         * id : 0
         * limitedPrice : 0
         * no : string
         * price : 0
         * startTime : 2017-07-20T01:51:48.891Z
         * status : RELEASE
         * title : string
         * useTime : 2017-07-20T01:51:48.891Z
         */

        private String description;
        private String endTime;
        private int id;
        private String limitedPrice;
        private String no;
        private int price;
        private String startTime;
        private String status;
        private String title;
        private String useTime;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLimitedPrice() {
            return limitedPrice;
        }

        public void setLimitedPrice(String limitedPrice) {
            this.limitedPrice = limitedPrice;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
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

        public String getUseTime() {
            return useTime;
        }

        public void setUseTime(String useTime) {
            this.useTime = useTime;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "description='" + description + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", id=" + id +
                    ", limitedPrice='" + limitedPrice + '\'' +
                    ", no='" + no + '\'' +
                    ", price=" + price +
                    ", startTime='" + startTime + '\'' +
                    ", status='" + status + '\'' +
                    ", title='" + title + '\'' +
                    ", useTime='" + useTime + '\'' +
                    '}';
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