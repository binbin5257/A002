package cn.lds.chatcore.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by xuqm on 2016/6/24.
 */
public class UploadModel implements Parcelable {
    /**
     * status : success
     * data : [{"id":13,"no":"be22580e-5c23-4d0b-b5bb-dde6ef06fe77","objectType":"picture","filePath":"C201606200_14667396639248586.png","fileName":"Screenshot_2016-05-27-17-36-29.png","fileSize":76581,"thumbnailFilePath":"C201606200_14667396639245382.png","originalFilePath":"C201606200_14667396634606720.png","duration":0}]
     */

    private String status;
    /**
     * id : 13
     * no : be22580e-5c23-4d0b-b5bb-dde6ef06fe77
     * objectType : picture
     * filePath : C201606200_14667396639248586.png
     * fileName : Screenshot_2016-05-27-17-36-29.png
     * fileSize : 76581
     * thumbnailFilePath : C201606200_14667396639245382.png
     * originalFilePath : C201606200_14667396634606720.png
     * duration : 0
     */

    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        private int id;
        private String no;
        private String objectType;
        private String filePath;
        private String fileName;
        private int fileSize;
        private String thumbnailFilePath;
        private String originalFilePath;
        private int duration;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }

        public String getThumbnailFilePath() {
            return thumbnailFilePath;
        }

        public void setThumbnailFilePath(String thumbnailFilePath) {
            this.thumbnailFilePath = thumbnailFilePath;
        }

        public String getOriginalFilePath() {
            return originalFilePath;
        }

        public void setOriginalFilePath(String originalFilePath) {
            this.originalFilePath = originalFilePath;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.no);
            dest.writeString(this.objectType);
            dest.writeString(this.filePath);
            dest.writeString(this.fileName);
            dest.writeInt(this.fileSize);
            dest.writeString(this.thumbnailFilePath);
            dest.writeString(this.originalFilePath);
            dest.writeInt(this.duration);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.no = in.readString();
            this.objectType = in.readString();
            this.filePath = in.readString();
            this.fileName = in.readString();
            this.fileSize = in.readInt();
            this.thumbnailFilePath = in.readString();
            this.originalFilePath = in.readString();
            this.duration = in.readInt();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeTypedList(this.data);
    }

    public UploadModel() {
    }

    protected UploadModel(Parcel in) {
        this.status = in.readString();
        this.data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Parcelable.Creator<UploadModel> CREATOR = new Parcelable.Creator<UploadModel>() {
        @Override
        public UploadModel createFromParcel(Parcel source) {
            return new UploadModel(source);
        }

        @Override
        public UploadModel[] newArray(int size) {
            return new UploadModel[size];
        }
    };
}
