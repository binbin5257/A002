package cn.lds.chatcore.data;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class LoginResponseModel {
    /**
     * appId :
     * authenticated : true
     * authorities :
     * black : false
     * confirmStatus : true
     * details : {"nickname":"","onceToken":"MjczMzM0NTYzMTQ3ODY1NzI0NA==","username":""}
     * organizations : [{"id":2,"name":"1"}]
     * principal : CEN1N869L04HVBH
     * reviewType : APPROVED
     */

    private DataBean data;
    /**
     * data : {"appId":"","authenticated":true,"authorities":"","black":false,"confirmStatus":true,"details":{"nickname":"","onceToken":"MjczMzM0NTYzMTQ3ODY1NzI0NA==","username":""},"organizations":[{"id":2,"name":"1"}],"principal":"CEN1N869L04HVBH","reviewType":"APPROVED"}
     * status : success
     */

    private String status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class AuthoritiesBean {
        /**
         * role : DRIVER
         */

        private String role;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
    public static class DataBean {
        private String appId;
        private boolean authenticated;
        private List<AuthoritiesBean> authorities;
        private boolean black;
        private boolean confirmStatus;
        /**
         * nickname :
         * onceToken : MjczMzM0NTYzMTQ3ODY1NzI0NA==
         * username :
         */

        private DetailsBean details;
        private String principal;
        private String reviewType;
        /**
         * id : 2
         * name : 1
         */

        private List<OrganizationsBean> organizations;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public boolean isAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(boolean authenticated) {
            this.authenticated = authenticated;
        }

        public List<AuthoritiesBean> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<AuthoritiesBean> authorities) {
            this.authorities = authorities;
        }

        public boolean isBlack() {
            return black;
        }

        public void setBlack(boolean black) {
            this.black = black;
        }

        public boolean isConfirmStatus() {
            return confirmStatus;
        }

        public void setConfirmStatus(boolean confirmStatus) {
            this.confirmStatus = confirmStatus;
        }

        public DetailsBean getDetails() {
            return details;
        }

        public void setDetails(DetailsBean details) {
            this.details = details;
        }

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public String getReviewType() {
            return reviewType;
        }

        public void setReviewType(String reviewType) {
            this.reviewType = reviewType;
        }

        public List<OrganizationsBean> getOrganizations() {
            return organizations;
        }

        public void setOrganizations(List<OrganizationsBean> organizations) {
            this.organizations = organizations;
        }

        public static class DetailsBean {
            private String nickname;
            private String onceToken;
            private String username;
            private String approverPhone;
            private String approverName;
            private String approverId;

            public String getApproverId() {
                return approverId;
            }

            public void setApproverId(String approverId) {
                this.approverId = approverId;
            }

            public String getApproverPhone() {
                return approverPhone;
            }

            public void setApproverPhone(String approverPhone) {
                this.approverPhone = approverPhone;
            }

            public String getApproverName() {
                return approverName;
            }

            public void setApproverName(String approverName) {
                this.approverName = approverName;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getOnceToken() {
                return onceToken;
            }

            public void setOnceToken(String onceToken) {
                this.onceToken = onceToken;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public static class OrganizationsBean {
            private String id;
            private String name;

            public OrganizationsBean(String id, String name) {
                this.id = id;
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
