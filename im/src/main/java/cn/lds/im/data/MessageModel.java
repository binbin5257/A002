package cn.lds.im.data;

/**
 * Created by colin on 15/12/25.
 */
public class MessageModel {

    private String title;
    private String date;
    private String content;
    private boolean isnew;

    public boolean isnew() {
        return isnew;
    }

    public void setIsnew(boolean isnew) {
        this.isnew = isnew;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageModel(String title, String date, String content, boolean isnew) {

        this.title = title;
        this.date = date;
        this.content = content;
        this.isnew = isnew;
    }
}
