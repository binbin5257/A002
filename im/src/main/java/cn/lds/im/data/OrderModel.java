package cn.lds.im.data;

import java.io.Serializable;

import cn.lds.chatcore.data.BookCarModel;

/**
 * Created by xuqm on 2016/6/12.
 */
public class OrderModel implements Serializable {

	private int type;//0==预约用车：：1==立即用车

	private String id;//场站id

	private String url;

	private String name;
	private String number;

	private String mileage;
	private double time;// xx/分钟
	private double range;// xx/公里

	private String depot_name;
	private String depot_address;
	private String depot_number;

	private String chargingRule;

	private String time_interval;//用车时间段
	private String reservationLocationNo;//立即用车  取车场站No
	private String returnLocationNo;//立即用车  还车车场站No
	private int maxSustainedMileage;

	private double lat;
	private double lng;

	private String modelId;

	private BookCarModel bookCarModel;//预约用车信息

	public OrderModel(int type, String id,String modelId) {
		this.type = type;
		this.id = id;
		this.modelId = modelId;
	}

	public int getMaxSustainedMileage() {
		return maxSustainedMileage;
	}

	public void setMaxSustainedMileage(int maxSustainedMileage) {
		this.maxSustainedMileage = maxSustainedMileage;
	}

	public String getChargingRule() {
		return chargingRule;
	}

	public void setChargingRule(String chargingRule) {
		this.chargingRule = chargingRule;
	}



	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public String getDepot_name() {
		return depot_name;
	}

	public void setDepot_name(String depot_name) {
		this.depot_name = depot_name;
	}

	public String getDepot_address() {
		return depot_address;
	}

	public void setDepot_address(String depot_address) {
		this.depot_address = depot_address;
	}

	public String getDepot_number() {
		return depot_number;
	}

	public void setDepot_number(String depot_number) {
		this.depot_number = depot_number;
	}

	public String getTime_interval() {
		return time_interval;
	}

	public void setTime_interval(String time_interval) {
		this.time_interval = time_interval;
	}

	public BookCarModel getBookCarModel() {
		return bookCarModel;
	}

	public void setBookCarModel(BookCarModel bookCarModel) {
		this.bookCarModel = bookCarModel;
	}

	public String getReservationLocationNo() {
		return reservationLocationNo;
	}

	public void setReservationLocationNo(String reservationLocationNo) {
		this.reservationLocationNo = reservationLocationNo;
	}

	public String getReturnLocationNo() {
		return returnLocationNo;
	}

	public void setReturnLocationNo(String returnLocationNo) {
		this.returnLocationNo = returnLocationNo;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "OrderModel{" +
				"type=" + type +
				", id='" + id + '\'' +
				", url='" + url + '\'' +
				", name='" + name + '\'' +
				", number='" + number + '\'' +
				", mileage='" + mileage + '\'' +
				", time='" + time + '\'' +
				", range='" + range + '\'' +
				", depot_name='" + depot_name + '\'' +
				", depot_address='" + depot_address + '\'' +
				", depot_number='" + depot_number + '\'' +
				", time_interval='" + time_interval + '\'' +
				", bookCarModel=" + bookCarModel +
				'}';
	}
}
