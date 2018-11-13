package cn.lds.chatcore.data;

/**
 * Created by E0608 on 2017/12/25.
 */

public class RegistModel {

    private String password;
    private String confirmPassword;
    private String noncePassword;
    private String phone;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNoncePassword() {
        return noncePassword;
    }

    public void setNoncePassword(String noncePassword) {
        this.noncePassword = noncePassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
