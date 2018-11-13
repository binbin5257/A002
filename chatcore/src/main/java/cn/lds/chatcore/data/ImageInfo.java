package cn.lds.chatcore.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ImageInfo implements Serializable {
    private String thumbnail;
    private String path;
    private String imageStorageId;

    public ImageInfo() {
    }

    public ImageInfo(String thumbnail, String path, String imageStorageId) {
        this.thumbnail = thumbnail;
        this.path = path;
        this.imageStorageId = imageStorageId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImageStorageId() {
        return imageStorageId;
    }

    public void setImageStorageId(String imageStorageId) {
        this.imageStorageId = imageStorageId;
    }
}
