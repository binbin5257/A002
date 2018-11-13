package cn.lds.chatcore.data;

/**
 * Created by Administrator on 2016/12/7.
 */

public class VersionModel {
    /**
     * status : success
     * data : {"id":8,"versionNo":11,"versionName":"3.1.5.8024","downloadUrl":"https://www.pgyer.com/bE26","forceUpdate":true,"description":"3.1.5.8024"}
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
         * id : 8
         * versionNo : 11
         * versionName : 3.1.5.8024
         * downloadUrl : https://www.pgyer.com/bE26
         * forceUpdate : true
         * description : 3.1.5.8024
         */

        private int id;
        private int versionNo;
        private String versionName;
        private String downloadUrl;
        private boolean forceUpdate;
        private String description;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(int versionNo) {
            this.versionNo = versionNo;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public boolean isForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
