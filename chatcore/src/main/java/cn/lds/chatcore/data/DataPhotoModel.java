package cn.lds.chatcore.data;

public class DataPhotoModel {
    private int id;
    private String fileName;
    private String filePath;
    private String parentName;
    private int filetype;
    private boolean isSelect;

    public DataPhotoModel() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id         文件id
     * @param fileName   文件名
     * @param filePath   文件地址
     * @param parentName 文件所在目录
     * @param filetype   文件的类型 用来区分图片和视频文件的 FileHelper.TYPE_VIDEO  FileHelper.TYPE_IMAGE
     * @param isSelect   是否被选中 用来显示界面右上角选中按钮的
     */
    public DataPhotoModel(int id, String fileName, String filePath, String parentName, int filetype,
                          boolean isSelect) {
        super();
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.parentName = parentName;
        this.filetype = filetype;
        this.isSelect = isSelect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getFiletype() {
        return filetype;
    }

    public void setFiletype(int filetype) {
        this.filetype = filetype;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
