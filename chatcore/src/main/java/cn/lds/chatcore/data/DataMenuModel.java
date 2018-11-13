package cn.lds.chatcore.data;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class DataMenuModel {
	private int id;
	private String name;
	private boolean isSelect;
	private ArrayList<DataPhotoModel> datas;
	private Bitmap thumbnail;

	// public DataMenuModel() {
	// super();
	// // TODO Auto-generated constructor stub
	// }
	/**
	 * 
	 * @param id
	 *            菜单id 现阶段没用上
	 * @param name
	 *            菜单名称
	 * @param isSelect
	 *            是否选中
	 * @param datas
	 *            菜单下对应数据
	 * @param thumbnail
	 *            菜单缩略图 通常设置菜单下第一张图片或者视频的缩略图
	 */
	public DataMenuModel(int id, String name, boolean isSelect, ArrayList<DataPhotoModel> datas, Bitmap thumbnail) {
		super();
		this.id = id;
		this.name = name;
		this.isSelect = isSelect;
		this.datas = datas;
		this.thumbnail = thumbnail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public ArrayList<DataPhotoModel> getDatas() {
		return datas;
	}

	public void setDatas(ArrayList<DataPhotoModel> datas) {
		this.datas = datas;
	}

	public Bitmap getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Bitmap thumbnail) {
		this.thumbnail = thumbnail;
	}

}
