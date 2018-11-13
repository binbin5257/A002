package cn.lds.chatcore.common;

import android.content.Context;
import android.content.Intent;

import cn.lds.chatcore.view.BaseCordovaActivity;

public class WebViewActivityHelper extends BaseCordovaActivity {

    private static final String SPLITER = "&%%&%&";

    /**
     * Start LocationActivity
     */
    public static void startWebViewActivity(Context activity, String url,
                                            String title) {
        // Intent intent = new Intent(Intent.ACTION_VIEW,
        // Uri.parse(url.trim()));
        // activity.startActivity(intent);
        try {
            Class<?> c = (Class<?>) Class.forName("cn.lds.im.view.WebViewHelpActivity");
            if (c == null) {
                ToolsHelper.showStatus(activity,false, "没有找到HTML容器");
                return;
            }
            Intent intent = new Intent(activity, c);
            LogHelper.d("打开WEBVIEW：title=" + title + "url=" + url);
            intent.putExtra("title", title);
            intent.putExtra("url", url.trim());
            activity.startActivity(intent);
        } catch (ClassNotFoundException e) {
            LogHelper.e("startWebViewActivity", e);
        }
        return;
    }

    /**
     * Start LocationActivity
     */
//    public static void startWebViewActivity(Context activity, String content) {
//        // Intent intent = new Intent(Intent.ACTION_VIEW,
//        // Uri.parse(url.trim()));
//        // activity.startActivity(intent);
//        Intent intent = new Intent(activity, WebHostActivity.class);
//        LogHelper.d("打开WEBVIEW：content=" + content);
//        intent.putExtra("content", content);
//        intent.putExtra("showtitlebar", true);
//        intent.putExtra("enablezoom", false);
//        intent.putExtra("ReceivedTitle", false);
//        activity.startActivity(intent);
//        return;
//    }

    /**
     * 新版 增加分享按钮；
     */
//    public static void startNewWebViewActivity(Context activity, String url,
//                                               String title) {
//        Intent intent = new Intent(activity, WebviewActivity.class);
//        LogHelper.d("打开WEBVIEW：title=" + title + "url=" + url);
//        intent.putExtra("title", title);
//        intent.putExtra("url", url.trim());
//        intent.putExtra("showtitlebar", true);
//        intent.putExtra("enablezoom", false);
//        intent.putExtra("ReceivedTitle", true);
//        activity.startActivity(intent);
//        return;
//    }
    public static String getInfo(String title, String date, String image,
                                 String count, String url, String content) {
        String info = title + SPLITER + date + SPLITER + image + SPLITER
                + count + SPLITER + url + SPLITER + content;
        return info;
    }

    public static String getTitle(String info) {
        String title = "";
        if (info != null) {
            String[] infos = info.split(SPLITER);
            if (infos.length >= 1) {
                title = infos[0];
            }
        }
        return title;
    }

    public static String getCount(String info) {
        String title = "";
        if (info != null) {
            String[] infos = info.split(SPLITER);
            if (infos.length >= 4) {
                title = infos[3];
            }
        }
        return title;
    }

    public static String getDate(String info) {
        String date = "";
        if (info != null) {
            String[] infos = info.split(SPLITER);
            if (infos.length >= 2) {
                date = infos[1];
                // SimpleDateFormat df = new
                // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // Date newDate = null;
                // try {
                // newDate = df.parse(infos[1]);
                // } catch (ParseException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // date = TimeUtil.getTimeToShow(newDate);
            }

        }
        return date;
    }

    public static String getImage(String info) {
        String image = "";
        if (info != null) {
            String[] infos = info.split(SPLITER);
            if (infos.length >= 3) {
                image = infos[2];
            }
        }
        return image;
    }

    public static String getImageUrl(String info) {
        String image = "";
        if (info != null) {
            String[] infos = info.split(SPLITER);
            if (infos.length >= 5) {
                image = infos[4];
            }
        }
        return image;
    }

    public static String getContent(String info) {
        String content = "";
        if (info != null) {
            String[] infos = info.split(SPLITER);
            if (infos.length >= 6) {
                content = infos[5];
            }
        }
        return content;
    }

    public static String getUrl(String info) {
        String url = "";
        if (info != null) {
            String[] infos = info.split(SPLITER);
            if (infos.length >= 5) {
                url = infos[4];
            }
        }
        return url;
    }

}
