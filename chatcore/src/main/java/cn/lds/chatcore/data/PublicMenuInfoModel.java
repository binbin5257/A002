package cn.lds.chatcore.data;

import java.util.ArrayList;

public class PublicMenuInfoModel {
    private String code;
    private String name;
    private String type;
    private ArrayList<PublicMenuInfoModel> children;

    public PublicMenuInfoModel(String code, String name, String type, ArrayList<PublicMenuInfoModel> children) {
        super();
        this.code = code;
        this.name = name;
        this.type = type;
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<PublicMenuInfoModel> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<PublicMenuInfoModel> children) {
        this.children = children;
    }

}
