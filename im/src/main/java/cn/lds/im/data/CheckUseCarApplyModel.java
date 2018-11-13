package cn.lds.im.data;

/**
 * Created by E0608 on 2018/1/2.
 */

public class CheckUseCarApplyModel {

    /**
     * status : success
     * data : {"id":null,"createdBy":null,"createdDate":null,"lastModifiedBy":null,"lastModifiedDate":null,"status":"APPROVED"}
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
         * createdBy : null
         * createdDate : null
         * lastModifiedBy : null
         * lastModifiedDate : null
         * status : APPROVED
         */

        private Object id;
        private Object createdBy;
        private Object createdDate;
        private Object lastModifiedBy;
        private Object lastModifiedDate;
        private String status;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Object createdBy) {
            this.createdBy = createdBy;
        }

        public Object getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Object createdDate) {
            this.createdDate = createdDate;
        }

        public Object getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(Object lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public Object getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(Object lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
