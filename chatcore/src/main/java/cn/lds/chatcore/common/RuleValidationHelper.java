package cn.lds.chatcore.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 规则验证帮助类
 * Created by sibinbin on 18-1-23.
 */

public class RuleValidationHelper {

    /**
     * 校验邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        Pattern p = Pattern
                .compile("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
