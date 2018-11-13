package cn.lds.im.data;

import java.io.Serializable;
import java.util.List;

/**
 * 请求 failure model
 * Created by sibinbin on 2017/7/25.
 */

public class FaIlureModel implements Serializable{

    private String status;

    private List<Errors> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Errors> getErrors() {
        return errors;
    }

    public void setErrors(List<Errors> errors) {
        this.errors = errors;
    }

    public class Errors implements Serializable{
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
