package cn.lds.chatcore.common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.lds.chatcore.R;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.data.RevertCarModel;

/**
 * @author xuqm xuqm@leadingsoft.cn
 * @ClassName:ToolsUtil
 * @Description:判断填入信息不能为空
 */
public class ToolsHelper {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNull(String str) {
        if (null == str || "".equals(str) || " ".equals(str)
                || "null".equals(str)) {
            return true;
        }
        return false;
    }


    public static List<PopData> getPayType(boolean isWallet) {
        List<PopData> lists = new ArrayList<>();
        lists.add(new PopData(R.drawable.order_wechat, "微信支付", true));
        lists.add(new PopData(R.drawable.order_alipay, "支付宝支付", false));
        if (!isWallet)
            lists.add(new PopData(R.drawable.order_wallet, "余额支付", false));
        return lists;
    }


    /**
     * 把单个英文字母或者字符串转换成数字ASCII码
     *
     * @param input
     * @return
     */
    public static int character2ASCII(String input) {
        char[] temp = input.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char each : temp) {
            builder.append((int) each);
        }
        String result = builder.toString();
        return Integer.parseInt(result);
    }

    public static String returnNull(String str) {

        if (isNull(str)) {
            return "";
        }
        return str;
    }

    /**
     * 判断密码是否全是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断密码是否全是由26个英文字母组成的字符串
     *
     * @param str
     * @return
     */
    public static boolean isAlphabet(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String isNullString(String str) {
        if (null == str || "".equals(str) || " ".equals(str)
                || "null".equals(str)) {
            return "";
        }
        return str;
    }

    public static String isNulltoInt(String str) {
        if (null == str || "".equals(str) || " ".equals(str)
                || "null".equals(str)) {
            return "0";
        }
        return str;
    }

    public static void showInfo(Context context, String info) {

        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    public static void showStatus(Context context, boolean isTrue, String info) {

        View layout = LayoutInflater.from(context).inflate(R.layout.toast_status, null);

        ImageView toast_icon_iv = (ImageView) layout.findViewById(R.id.toast_icon_iv);
        TextView toast_title_tv = (TextView) layout.findViewById(R.id.toast_title_tv);
        if (isTrue) toast_icon_iv.setImageResource(R.drawable.toast_success);
        else toast_icon_iv.setImageResource(R.drawable.toast_fail);

        toast_title_tv.setText(info);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    public static void showCountDownTimer() {

    }

    /**
     * 保留两位小数
     *
     * @param text
     * @return
     */
    public static String getDouble(double text) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(text);
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static void showInfoForShort(Context context, String info) {

        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 将String转int
     *
     * @param msg
     * @return String为空返回0
     */
    public static int stringTOint(String msg) {

        return isNull(msg) ? 0 : Integer.parseInt(msg);

    }

    /**
     * 将String转double
     *
     * @param msg
     * @return String为空返回0
     */
    public static double stringTOdouble(String msg) {

        return isNull(msg) ? 0 : Double.parseDouble(msg);

    }

    /**
     * 将String转float
     *
     * @param msg
     * @return String为空返回0
     */
    public static float stringTOfloat(String msg) {

        return isNull(msg) ? 0 : Float.parseFloat(msg);

    }

    /**
     * 将String转int
     *
     * @param msg
     * @return String为空返回0
     */
    public static int stringTOInteger(String msg) {

        return isNull(msg) ? 0 : Integer.parseInt(msg);

    }

    /**
     * 将String转long
     *
     * @param msg
     * @return String为空返回0
     */
    public static long stringTOlong(String msg) {

        return isNull(msg) ? 0 : Long.parseLong(msg);

    }

    /**
     * 根据给定字符串输出boolean
     *
     * @param str 给定字符串
     * @return 如果输入"true" 返回ture否则返回false
     */
    public static boolean isTrue(String str) {

        boolean isTrue = false;

        if (isNull(str) || str.equals("true")) {
            isTrue = true;
        }

        return isTrue;
    }

    /**
     * 根据给定字符串输出boolean
     *
     * @param str 给定字符串
     * @return 如果输入"true" 返回ture否则返回false
     */
    public static boolean isFalse(String str) {

        boolean isTrue = true;

        if (!isNull(str) && str.equals("false")) {
            isTrue = false;
        }

        return isTrue;
    }

    public static boolean isboolIP(String ipAddress) {
        String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static boolean isboolPort(String port) {
        // 端口号验�? 1 ~ 65535
        String regex = "^([1-9]|[1-9]\\d{1,3}|[1-6][0-5][0-5][0-3][0-5])$";
        return Pattern.matches(regex, port);
    }

    /**
     * 加密
     */
    public static String encrypt(String data, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     */
    public static String desEncrypt(String data, String key) {
        try {
            byte[] encrypted1 = Base64.decode(data.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * text 全文 sentence 关键字
     */
    public static boolean isContain(String text, String sentence) {

        char[] c1 = text.toCharArray();
        char[] c2 = sentence.toCharArray();

        if (ToolsHelper.isNull(sentence)) {
            return true;
        }

        if (text.equals(sentence)) {
            return true;
        }

        if (c1.length <= c2.length) {
            return false;
        }

        for (int i = 0; i < c1.length - c2.length + 1; i++) {
            if (c1[i] == c2[0]) {
                if (text.substring(i, i + c2.length).equals(sentence)) {
                    return true;
                }

            }

        }

        return false;
    }

    /**
     * 验证电话号码
     */
    public static boolean validatePhone(String phone) {
//        Pattern p = Pattern
//                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//
//        Matcher m = p.matcher(phone);
        return isMobileNO(null, phone);
    }

    /**
     * 验证邮箱
     */
    public static boolean validateEmail(String email) {
//        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(email);
//        if (matcher.matches()) {
//            return true;
//        } else {
//            return false;
//        }
        return isEmail(null, email);
    }

    /**
     * 验证手机号码
     *
     * @param context,mobiles
     * @return true 手机格式正确 false 不正确
     */

    public static boolean isMobileNO(Context context, String mobiles) {
        boolean flag = false;

        if (ToolsHelper.isNull(mobiles)) {
            if (context != null) {
                showStatus(context, false, context.getString(R.string.toolshelper_phone_nonull));
            }
            return flag;
        }

        try {
//            Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^(4,\\D)])|(18[0,2-9])|(17[(0,6-8)]))\\d{8}$");
            Pattern p = Pattern.compile("^1\\d{10}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }

        if (!flag) {
            if (context != null) {
                showStatus(context, false, context.getString(R.string.toolshelper_phone_wrong));
            }
        }

        return flag;
    }

    /**
     * 验证邮箱地址是否正确
     *
     * @param context,email
     * @return true 邮箱格式正确 false 不正确
     */
    public static boolean isEmail(Context context, String email) {
        boolean flag = false;

        if (ToolsHelper.isNull(email)) {
//            if (context != null)
//                ToolsHelper.showStatus(context, false, context.getString(R.string.toolshelper_email_nonull));
            return true;
        }

        try {
//            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
//            Pattern regex = Pattern.compile(check);
//            Matcher matcher = regex.matcher(email);
//            flag = matcher.matches();
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+([\\_|\\-|\\.]?[a-zA-Z0-9])*\\@[a-zA-Z0-9]+([\\_|\\-|\\.]?[a-zA-Z0-9])*\\.[a-zA-Z]{2,3}$");
            Matcher matcher = pattern.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        if (!flag) {
            if (context != null)
                ToolsHelper.showStatus(context, false, context.getString(R.string.toolshelper_email_wrong));
        }
        return flag;
    }

    /**
     * 验证 英文和数字
     */
    public static boolean isEngOrNum(String value) {
        boolean flag;
        try {

            Pattern pattern = Pattern.compile("^[0-9a-zA-Z]+$");
            Matcher matcher = pattern.matcher(value);
            flag = matcher.matches();
        } catch (Exception Ex) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证字符串是否为数字
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 将对象转换成String
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        return obj == null ? "" : String.valueOf(obj);
    }

    /**
     * 将对象转换成boolean
     *
     * @param str
     * @return
     */
    public static boolean toBoolean(String str) {
        return str == null ? false : Boolean.valueOf(str);
    }

    /**
     * 将对象转换成Int
     *
     * @param obj
     * @return
     */
    public static int toInt(Object obj) {
        return obj == null ? null : Integer.valueOf(toString(obj));
    }

    public static String getHttpRequestErrorMsg(Context context, HttpResult httpResult) {


        try {
            LoadingDialog.dismissDialog();
            if (httpResult == null) {
                return context.getString(R.string.toolshelper_disconecting);
            }
            if (401 == httpResult.getErrorCode() || 403 == httpResult.getErrorCode()) {
                return "登录信息保存超时,请重新登录";
            }

            JSONObject jsonObject = httpResult.getJsonResult();
            if (jsonObject == null) {
                return context.getString(R.string.toolshelper_disconecting);
            }
            JSONArray jsonArray = httpResult.getJsonResult().optJSONArray("errors");
            if (jsonArray == null || jsonArray.length() <= 0) {
                return context.getString(R.string.toolshelper_disconecting);
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    String strErrcode = JsonHelper.getString(json, "errcode");
                    if ("401".equals(strErrcode)) {
                        return "登录信息保存超时,请重新登录";
                    }
                    String strErrmsg = JsonHelper.getString(json, "errmsg");
                    if (ToolsHelper.isNull(strErrmsg))
                        return "获取数据失败";
                    else
                        return strErrmsg;
                }
            }
        } catch (Exception ex) {
            LogHelper.e(JsonHelper.class.getSimpleName(), ex);
        }
        return "获取数据错误";
    }

    /**
     * 显示Http请求业务错误消息
     *
     * @param context
     * @param httpResult
     */
    public static void showHttpRequestErrorMsg(Context context, HttpResult httpResult) {
        try {
            LoadingDialog.dismissDialog();
            if (httpResult == null) {
//                ToolsHelper.showInfo(context, context.getString(R.string.toolshelper_disconecting));
                ToolsHelper.showStatus(context, false, context.getString(R.string.toolshelper_disconecting));
                return;
            }
            if (401 == httpResult.getErrorCode() || 403 == httpResult.getErrorCode()) {
//                ToolsHelper.showInfo(context, context.getString(R.string.toolshelper_err));
//                ToolsHelper.showStatus(context, false, context.getString(R.string.toolshelper_err));
                return;
            }

            JSONObject jsonObject = httpResult.getJsonResult();
            if (jsonObject == null) {
//                ToolsHelper.showInfo(context, context.getString(R.string.toolshelper_disconecting));
                ToolsHelper.showStatus(context, false, context.getString(R.string.toolshelper_disconecting));
                return;
            }
            JSONArray jsonArray = httpResult.getJsonResult().optJSONArray("errors");
            if (jsonArray == null || jsonArray.length() <= 0) {
//                ToolsHelper.showInfo(context, context.getString(R.string.toolshelper_disconecting));
                ToolsHelper.showStatus(context, false, context.getString(R.string.toolshelper_disconecting));
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    String strErrcode = JsonHelper.getString(json, "errcode");
                    if ("401".equals(strErrcode)) {
//                        ToolsHelper.showInfo(context, "用户未登录,请登录后再试");
//                        ToolsHelper.showStatus(context, false, "用户未登录,请登录后再试");
//                        ToolsHelper.showStatus(context, false, "用户未登录,正在尝试自动登录");
//                        PopWindowHelper.getInstance().alert(MyApplication.getInstance().getApplicationContext(), new PopWindowListener() {
//                            @Override
//                            public void confirm() {
//
//                            }
//
//                            @Override
//                            public void cancel() {
//
//                            }
//
//                            @Override
//                            public void onItemListener(int position) {
//
//                            }
//                        }).setContentTx("用户未登录,正在尝试自动登录").show(MyApplication.getInstance().getIv__top());
//                        break;
                    }
                    String strErrmsg = JsonHelper.getString(json, "errmsg");
                    if (ToolsHelper.isNull(strErrmsg)){
                        ToolsHelper.showStatus(context, false, "获取数据失败");
                    }else{
                        ToolsHelper.showStatus(context, false, strErrmsg);

                    }
                }
            }
        } catch (Exception ex) {
            LogHelper.e(JsonHelper.class.getSimpleName(), ex);
        }
    }


    public static double getmax(int i) {
        return Math.pow(10, String.valueOf(i).toString().trim().length());
    }

    /**
     * 改变文字颜色(用于搜索)
     *
     * @param text     显示的内容
     * @param textView 显示的控件
     * @param keyword  查询的关键字
     */
    public static void changeColor(String text, TextView textView, String keyword) {
        int changeTextColor;
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#0c9932"));//墨绿色

        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (null == keyword) {
            textView.setText(text);
            return;
        }

        changeTextColor = text.indexOf(keyword);
        if (changeTextColor != -1) {
            builder.setSpan(redSpan, changeTextColor, changeTextColor + keyword.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        } else
            textView.setText(text);
    }


    public static RevertCarModel bdToGgFromLon(double bd_lon, double bd_lat) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        RevertCarModel revertCarModel = new RevertCarModel();
        revertCarModel.setLongitude(z * Math.cos(theta));
        revertCarModel.setLatitude(z * Math.sin(theta));
        return revertCarModel;
    }

    private static Uri SMS_INBOX = Uri.parse("content://sms/");

    /**
     * 自动获取验证码短信
     */
    public static String getSmsFromPhone(Context mContext) {
        ContentResolver cr = mContext.getContentResolver();
        String[] projection = new String[]{"body", "date"};//"_id", "address", "person",, "date", "type
        String date = String.valueOf(System.currentTimeMillis() - 3 * 60 * 1000).toString();
        Cursor cur = cr.query(SMS_INBOX, projection, "body like ? and date > ?", new String[]{"%分时租赁%", date}, "date asc");
        if (null == cur)
            return null;
        if (cur.moveToNext()) {
            String body = cur.getString(cur.getColumnIndex("body"));
            //这里我是要获取自己短信服务号码中的验证码~~
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(body);
            while (m.find()) {
                String find = m.group(1).toString();
                return find;
            }
        }
        return null;
    }

}
