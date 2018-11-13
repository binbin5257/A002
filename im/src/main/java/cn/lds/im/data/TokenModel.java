package cn.lds.im.data;

/**
 * Created by sibinbin on 18-4-20.
 */

public class TokenModel {


    /**
     * errNo : 200
     * errMessage : OK
     * accessToken : ac13d641e2a17611a68a76d635b24f65
     * expiresIn : 3600
     */

    private String errNo;
    private String errMessage;
    private String accessToken;
    private String expiresIn;

    public String getErrNo() {
        return errNo;
    }

    public void setErrNo( String errNo ) {
        this.errNo = errNo;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage( String errMessage ) {
        this.errMessage = errMessage;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken( String accessToken ) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn( String expiresIn ) {
        this.expiresIn = expiresIn;
    }
}
