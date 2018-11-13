package cn.lds.chatcore.data;

/**
 * Created by xuqm on 2016/6/8.
 */
public class PopData {
    private int imageID;
    private String text;
    private boolean isSelect;

    public PopData() {
    }

    public PopData(int imageID, String text, boolean isSelect) {
        this.imageID = imageID;
        this.text = text;
        this.isSelect = isSelect;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "PopData{" +
                "imageID=" + imageID +
                ", text='" + text + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
