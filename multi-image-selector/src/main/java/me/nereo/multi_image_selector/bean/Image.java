package me.nereo.multi_image_selector.bean;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class Image {
    public String path;
    public String name;
    public long time;
    public boolean isImage;
    public int id;

    public Image(int id,String path, String name, long time, boolean isImage) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.time = time;
        this.isImage = isImage;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
