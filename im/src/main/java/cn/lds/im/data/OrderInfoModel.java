package cn.lds.im.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by xuqm on 2016/6/17.
 */
public class OrderInfoModel implements Parcelable, Serializable {


	/**
	 * status : success
	 * data : {"amount":0,"chargingRule":"string","configuration":"string","departmentId":0,"departmentName":"string","droppedOffTime":"2017-07-20T02:30:17.856Z","endEnergyPercent":0,"endOdometer":0,"enrolleeId":0,"enrolleeName":"string","enrolleePhone":"string","id":0,"limitTime":"2017-07-20T02:30:17.856Z","maxSustainedMileage":0,"no":"string","odometer":0,"pickedUpTime":"2017-07-20T02:30:17.856Z","plateLicenseNo":"string","reservationLocationAddress":"string","reservationLocationId":0,"reservationLocationLatitude":0,"reservationLocationLongitude":0,"reservationLocationName":"string","reservationTime":"2017-07-20T02:30:17.856Z","returnLocationAddress":"string","returnLocationId":0,"returnLocationLatitude":0,"returnLocationLongitude":0,"returnLocationName":"string","scheduledDroppedOffTime":"2017-07-20T02:30:17.856Z","scheduledPickedUpTime":"2017-07-20T02:30:17.856Z","startEnergyPercent":0,"startOdometer":0,"status":"OPEN","sustainedMileage":0,"time":0,"type":"INSTANT","vehicleBrandName":"string","vehicleId":0,"vehiclePicId":"string","vehicleSeriesName":"string","year":0}
	 */

	private String status;
	private DataBean data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class BlueToothData implements Parcelable{

		private String name;
		private String openDoorCommand;
		private String closeDoorCommand;
		private String findCarCommand;
		private String macAddress;

		public String getMacAddress() {
			return macAddress;
		}

		public void setMacAddress(String macAddress) {
			this.macAddress = macAddress;
		}

		public static Creator<BlueToothData> getCREATOR() {
			return CREATOR;
		}

		protected BlueToothData(Parcel in) {
			name = in.readString();
			openDoorCommand = in.readString();
			closeDoorCommand = in.readString();
			findCarCommand = in.readString();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(name);
			dest.writeString(openDoorCommand);
			dest.writeString(closeDoorCommand);
			dest.writeString(findCarCommand);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public static final Creator<BlueToothData> CREATOR = new Creator<BlueToothData>() {
			@Override
			public BlueToothData createFromParcel(Parcel in) {
				return new BlueToothData(in);
			}

			@Override
			public BlueToothData[] newArray(int size) {
				return new BlueToothData[size];
			}
		};

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOpenDoorCommand() {
			return openDoorCommand;
		}

		public void setOpenDoorCommand(String openDoorCommand) {
			this.openDoorCommand = openDoorCommand;
		}

		public String getCloseDoorCommand() {
			return closeDoorCommand;
		}

		public void setCloseDoorCommand(String closeDoorCommand) {
			this.closeDoorCommand = closeDoorCommand;
		}

		public String getFindCarCommand() {
			return findCarCommand;
		}

		public void setFindCarCommand(String findCarCommand) {
			this.findCarCommand = findCarCommand;
		}
	}

	public static class DataBean implements Parcelable {
		/**
		 * amount : 0
		 * chargingRule : string
		 * configuration : string
		 * departmentId : 0
		 * departmentName : string
		 * droppedOffTime : 2017-07-20T02:30:17.856Z
		 * endEnergyPercent : 0
		 * endOdometer : 0
		 * enrolleeId : 0
		 * enrolleeName : string
		 * enrolleePhone : string
		 * id : 0
		 * limitTime : 2017-07-20T02:30:17.856Z
		 * maxSustainedMileage : 0
		 * no : string
		 * odometer : 0
		 * pickedUpTime : 2017-07-20T02:30:17.856Z
		 * plateLicenseNo : string
		 * reservationLocationAddress : string
		 * reservationLocationId : 0
		 * reservationLocationLatitude : 0
		 * reservationLocationLongitude : 0
		 * reservationLocationName : string
		 * reservationTime : 2017-07-20T02:30:17.856Z
		 * returnLocationAddress : string
		 * returnLocationId : 0
		 * returnLocationLatitude : 0
		 * returnLocationLongitude : 0
		 * returnLocationName : string
		 * scheduledDroppedOffTime : 2017-07-20T02:30:17.856Z
		 * scheduledPickedUpTime : 2017-07-20T02:30:17.856Z
		 * startEnergyPercent : 0
		 * startOdometer : 0
		 * status : OPEN
		 * sustainedMileage : 0
		 * time : 0
		 * type : INSTANT
		 * vehicleBrandName : string
		 * vehicleId : 0
		 * vehiclePicId : string
		 * vehicleSeriesName : string
		 * year : 0
		 */

		private float amount;
		private boolean deliverCompleted;//送车上门是否已送达(false: 未送达, true: 已送达) ,
		private boolean delivered;//是否需要送车上门(false: 不需要送车上门, true: 需要送车上门) ,
		private boolean scheduledTimeUp;//预约租车，是否到达预约取车时间 ,
		private String deliverFfsPhone;//送车上门外勤联系方式 ,
		private String deliverName;//送车上门地点（名称）
		private String deliverAddress;//送车上门地点（地址）
		private double deliverLatitude;//送车上门地点（纬度）
		private double deliverLongitude;//送车上门地点（经度）
		private String chargingRule;
		private String configuration;
		private int departmentId;
		private String departmentName;
		private long droppedOffTime;
		private int endEnergyPercent;
		private int endOdometer;
		private int enrolleeId;
		private String enrolleeName;
		private String enrolleePhone;
		private int id;
		private long limitTime;
		private int maxSustainedMileage;
		private String no;
		private int odometer;
		private long pickedUpTime;
		private String plateLicenseNo;
		private String reservationLocationAddress;
		private int reservationLocationId;
		private double reservationLocationLatitude;
		private double reservationLocationLongitude;
		private String reservationLocationName;
		private long reservationTime;
		private String returnLocationAddress;
		private int returnLocationId;
		private double returnLocationLatitude;
		private double returnLocationLongitude;
		private String returnLocationName;
		private long scheduledDroppedOffTime;
		private long scheduledPickedUpTime;
		private int startEnergyPercent;
		private int startOdometer;
		private String status;
		private int sustainedMileage;
		private String time;
		private String type;
		private String vehicleBrandName;
		private int vehicleId;
		private String vehiclePicId;
		private String vehicleSeriesName;
		private int year;

		private double pickupLatitude;
		private double pickupLongitude;
		private boolean pickuped;
		private String pickupName;
		private String pickupAddress;
		private String pickupFfsPhone;
		private double pickupAmount;
		private double deliverAmount;
		private double dispatchAmount;
		private double distanceAmount;
		private double timeAmount;
		private double distanceCost;
		private double timeCost;
		private double cappedPricePerDay;
		private double minPrice;
		private boolean pickupFfsReceived;
		private double latitude;
		private double longitude;
		private String modelId;
		private String chargingRuleType;
		private BlueToothData blueToothMDTO;

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public BlueToothData getBlueToothMDTO() {
			return blueToothMDTO;
		}

		public void setBlueToothMDTO(BlueToothData blueToothMDTO) {
			this.blueToothMDTO = blueToothMDTO;
		}

		public static Creator<DataBean> getCREATOR() {
			return CREATOR;
		}

		public String getChargingRuleType() {
			return chargingRuleType;
		}

		public void setChargingRuleType(String chargingRuleType) {
			this.chargingRuleType = chargingRuleType;
		}

		public String getModelId() {
			return modelId;
		}

		public void setModelId(String modelId) {
			this.modelId = modelId;
		}

		public double getCappedPricePerDay() {
			return cappedPricePerDay;
		}

		public void setCappedPricePerDay(double cappedPricePerDay) {
			this.cappedPricePerDay = cappedPricePerDay;
		}

		public double getMinPrice() {
			return minPrice;
		}

		public void setMinPrice(double minPrice) {
			this.minPrice = minPrice;
		}

		public double getDistanceCost() {
			return distanceCost;
		}

		public void setDistanceCost(double distanceCost) {
			this.distanceCost = distanceCost;
		}

		public double getTimeCost() {
			return timeCost;
		}

		public void setTimeCost(double timeCost) {
			this.timeCost = timeCost;
		}

		public boolean isPickupFfsReceived() {
			return pickupFfsReceived;
		}

		public void setPickupFfsReceived(boolean pickupFfsReceived) {
			this.pickupFfsReceived = pickupFfsReceived;
		}

		public double getPickupLatitude() {
			return pickupLatitude;
		}

		public void setPickupLatitude(double pickupLatitude) {
			this.pickupLatitude = pickupLatitude;
		}

		public double getPickupLongitude() {
			return pickupLongitude;
		}

		public void setPickupLongitude(double pickupLongitude) {
			this.pickupLongitude = pickupLongitude;
		}

		public void setAmount(float amount) {
			this.amount = amount;
		}

		public void setPickupAmount(double pickupAmount) {
			this.pickupAmount = pickupAmount;
		}

		public void setDeliverAmount(double deliverAmount) {
			this.deliverAmount = deliverAmount;
		}

		public void setDistanceAmount(double distanceAmount) {
			this.distanceAmount = distanceAmount;
		}

		public double getTimeAmount() {
			return timeAmount;
		}

		public void setTimeAmount(double timeAmount) {
			this.timeAmount = timeAmount;
		}

		public double getDispatchAmount() {
			return dispatchAmount;
		}

		public void setDispatchAmount(double dispatchAmount) {
			this.dispatchAmount = dispatchAmount;
		}

		public double getDistanceAmount() {
			return distanceAmount;
		}

		public void setDistanceAmount(int distanceAmount) {
			this.distanceAmount = distanceAmount;
		}

		public double getDeliverAmount() {
			return deliverAmount;
		}

		public void setDeliverAmount(int deliverAmount) {
			this.deliverAmount = deliverAmount;
		}

		public double getPickupAmount() {
			return pickupAmount;
		}

		public void setPickupAmount(int pickupAmount) {
			this.pickupAmount = pickupAmount;
		}

		public String getPickupFfsPhone() {
			return pickupFfsPhone;
		}

		public void setPickupFfsPhone(String pickupFfsPhone) {
			this.pickupFfsPhone = pickupFfsPhone;
		}

		public String getPickupAddress() {
			return pickupAddress;
		}

		public void setPickupAddress(String pickupAddress) {
			this.pickupAddress = pickupAddress;
		}

		public String getPickupName() {
			return pickupName;
		}

		public void setPickupName(String pickupName) {
			this.pickupName = pickupName;
		}

		public void setReservationLocationLongitude(double reservationLocationLongitude) {
			this.reservationLocationLongitude = reservationLocationLongitude;
		}

		public boolean isPickuped() {
			return pickuped;
		}

		public void setPickuped(boolean pickuped) {
			this.pickuped = pickuped;
		}

		public float getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public boolean isDeliverCompleted() {
			return deliverCompleted;
		}

		public void setDeliverCompleted(boolean deliverCompleted) {
			this.deliverCompleted = deliverCompleted;
		}

		public boolean isDelivered() {
			return delivered;
		}

		public void setDelivered(boolean delivered) {
			this.delivered = delivered;
		}

		public boolean isScheduledTimeUp() {
			return scheduledTimeUp;
		}

		public void setScheduledTimeUp(boolean scheduledTimeUp) {
			this.scheduledTimeUp = scheduledTimeUp;
		}

		public String getDeliverFfsPhone() {
			return deliverFfsPhone;
		}

		public void setDeliverFfsPhone(String deliverFfsPhone) {
			this.deliverFfsPhone = deliverFfsPhone;
		}

		public String getDeliverName() {
			return deliverName;
		}

		public void setDeliverName(String deliverName) {
			this.deliverName = deliverName;
		}

		public String getDeliverAddress() {
			return deliverAddress;
		}

		public void setDeliverAddress(String deliverAddress) {
			this.deliverAddress = deliverAddress;
		}

		public double getDeliverLatitude() {
			return deliverLatitude;
		}

		public void setDeliverLatitude(double deliverLatitude) {
			this.deliverLatitude = deliverLatitude;
		}

		public double getDeliverLongitude() {
			return deliverLongitude;
		}

		public void setDeliverLongitude(double deliverLongitude) {
			this.deliverLongitude = deliverLongitude;
		}

		public String getChargingRule() {
			return chargingRule;
		}

		public void setChargingRule(String chargingRule) {
			this.chargingRule = chargingRule;
		}

		public String getConfiguration() {
			return configuration;
		}

		public void setConfiguration(String configuration) {
			this.configuration = configuration;
		}

		public int getDepartmentId() {
			return departmentId;
		}

		public void setDepartmentId(int departmentId) {
			this.departmentId = departmentId;
		}

		public String getDepartmentName() {
			return departmentName;
		}

		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
		}


		public int getEndEnergyPercent() {
			return endEnergyPercent;
		}

		public void setEndEnergyPercent(int endEnergyPercent) {
			this.endEnergyPercent = endEnergyPercent;
		}

		public int getEndOdometer() {
			return endOdometer;
		}

		public void setEndOdometer(int endOdometer) {
			this.endOdometer = endOdometer;
		}

		public int getEnrolleeId() {
			return enrolleeId;
		}

		public void setEnrolleeId(int enrolleeId) {
			this.enrolleeId = enrolleeId;
		}

		public String getEnrolleeName() {
			return enrolleeName;
		}

		public void setEnrolleeName(String enrolleeName) {
			this.enrolleeName = enrolleeName;
		}

		public String getEnrolleePhone() {
			return enrolleePhone;
		}

		public void setEnrolleePhone(String enrolleePhone) {
			this.enrolleePhone = enrolleePhone;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public long getLimitTime() {
			return limitTime;
		}

		public void setLimitTime(long limitTime) {
			this.limitTime = limitTime;
		}

		public int getMaxSustainedMileage() {
			return maxSustainedMileage;
		}

		public void setMaxSustainedMileage(int maxSustainedMileage) {
			this.maxSustainedMileage = maxSustainedMileage;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public int getOdometer() {
			return odometer;
		}

		public void setOdometer(int odometer) {
			this.odometer = odometer;
		}

		public long getDroppedOffTime() {
			return droppedOffTime;
		}

		public void setDroppedOffTime(long droppedOffTime) {
			this.droppedOffTime = droppedOffTime;
		}

		public long getPickedUpTime() {
			return pickedUpTime;
		}

		public void setPickedUpTime(long pickedUpTime) {
			this.pickedUpTime = pickedUpTime;
		}

		public long getReservationTime() {
			return reservationTime;
		}

		public void setReservationTime(long reservationTime) {
			this.reservationTime = reservationTime;
		}

		public String getPlateLicenseNo() {
			return plateLicenseNo;
		}

		public void setPlateLicenseNo(String plateLicenseNo) {
			this.plateLicenseNo = plateLicenseNo;
		}

		public String getReservationLocationAddress() {
			return reservationLocationAddress;
		}

		public void setReservationLocationAddress(String reservationLocationAddress) {
			this.reservationLocationAddress = reservationLocationAddress;
		}

		public int getReservationLocationId() {
			return reservationLocationId;
		}

		public void setReservationLocationId(int reservationLocationId) {
			this.reservationLocationId = reservationLocationId;
		}

		public double getReservationLocationLatitude() {
			return reservationLocationLatitude;
		}

		public void setReservationLocationLatitude(double reservationLocationLatitude) {
			this.reservationLocationLatitude = reservationLocationLatitude;
		}

		public double getReservationLocationLongitude() {
			return reservationLocationLongitude;
		}

		public void setReservationLocationLongitude(int reservationLocationLongitude) {
			this.reservationLocationLongitude = reservationLocationLongitude;
		}

		public String getReservationLocationName() {
			return reservationLocationName;
		}

		public void setReservationLocationName(String reservationLocationName) {
			this.reservationLocationName = reservationLocationName;
		}



		public String getReturnLocationAddress() {
			return returnLocationAddress;
		}

		public void setReturnLocationAddress(String returnLocationAddress) {
			this.returnLocationAddress = returnLocationAddress;
		}

		public int getReturnLocationId() {
			return returnLocationId;
		}

		public void setReturnLocationId(int returnLocationId) {
			this.returnLocationId = returnLocationId;
		}

		public double getReturnLocationLatitude() {
			return returnLocationLatitude;
		}

		public void setReturnLocationLatitude(double returnLocationLatitude) {
			this.returnLocationLatitude = returnLocationLatitude;
		}

		public double getReturnLocationLongitude() {
			return returnLocationLongitude;
		}

		public void setReturnLocationLongitude(double returnLocationLongitude) {
			this.returnLocationLongitude = returnLocationLongitude;
		}

		public String getReturnLocationName() {
			return returnLocationName;
		}

		public void setReturnLocationName(String returnLocationName) {
			this.returnLocationName = returnLocationName;
		}

		public long getScheduledDroppedOffTime() {
			return scheduledDroppedOffTime;
		}

		public void setScheduledDroppedOffTime(long scheduledDroppedOffTime) {
			this.scheduledDroppedOffTime = scheduledDroppedOffTime;
		}

		public long getScheduledPickedUpTime() {
			return scheduledPickedUpTime;
		}

		public void setScheduledPickedUpTime(long scheduledPickedUpTime) {
			this.scheduledPickedUpTime = scheduledPickedUpTime;
		}

		public int getStartEnergyPercent() {
			return startEnergyPercent;
		}

		public void setStartEnergyPercent(int startEnergyPercent) {
			this.startEnergyPercent = startEnergyPercent;
		}

		public int getStartOdometer() {
			return startOdometer;
		}

		public void setStartOdometer(int startOdometer) {
			this.startOdometer = startOdometer;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public int getSustainedMileage() {
			return sustainedMileage;
		}

		public void setSustainedMileage(int sustainedMileage) {
			this.sustainedMileage = sustainedMileage;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getVehicleBrandName() {
			return vehicleBrandName;
		}

		public void setVehicleBrandName(String vehicleBrandName) {
			this.vehicleBrandName = vehicleBrandName;
		}

		public int getVehicleId() {
			return vehicleId;
		}

		public void setVehicleId(int vehicleId) {
			this.vehicleId = vehicleId;
		}

		public String getVehiclePicId() {
			return vehiclePicId;
		}

		public void setVehiclePicId(String vehiclePicId) {
			this.vehiclePicId = vehiclePicId;
		}

		public String getVehicleSeriesName() {
			return vehicleSeriesName;
		}

		public void setVehicleSeriesName(String vehicleSeriesName) {
			this.vehicleSeriesName = vehicleSeriesName;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeDouble(this.amount);
			dest.writeByte(this.deliverCompleted ? (byte) 1 : (byte) 0);
			dest.writeByte(this.delivered ? (byte) 1 : (byte) 0);
			dest.writeByte(this.scheduledTimeUp ? (byte) 1 : (byte) 0);
			dest.writeString(this.deliverFfsPhone);
			dest.writeString(this.deliverName);
			dest.writeString(this.deliverAddress);
			dest.writeDouble(this.deliverLatitude);
			dest.writeDouble(this.deliverLongitude);
			dest.writeString(this.chargingRule);
			dest.writeString(this.configuration);
			dest.writeInt(this.departmentId);
			dest.writeString(this.departmentName);
			dest.writeLong(this.droppedOffTime);
			dest.writeInt(this.endEnergyPercent);
			dest.writeInt(this.endOdometer);
			dest.writeInt(this.enrolleeId);
			dest.writeString(this.enrolleeName);
			dest.writeString(this.enrolleePhone);
			dest.writeInt(this.id);
			dest.writeLong(this.limitTime);
			dest.writeInt(this.maxSustainedMileage);
			dest.writeString(this.no);
			dest.writeInt(this.odometer);
			dest.writeLong(this.pickedUpTime);
			dest.writeString(this.plateLicenseNo);
			dest.writeString(this.reservationLocationAddress);
			dest.writeInt(this.reservationLocationId);
			dest.writeDouble(this.reservationLocationLatitude);
			dest.writeDouble(this.reservationLocationLongitude);
			dest.writeString(this.reservationLocationName);
			dest.writeLong(this.reservationTime);
			dest.writeString(this.returnLocationAddress);
			dest.writeInt(this.returnLocationId);
			dest.writeDouble(this.returnLocationLatitude);
			dest.writeDouble(this.returnLocationLongitude);
			dest.writeString(this.returnLocationName);
			dest.writeLong(this.scheduledDroppedOffTime);
			dest.writeLong(this.scheduledPickedUpTime);
			dest.writeInt(this.startEnergyPercent);
			dest.writeInt(this.startOdometer);
			dest.writeString(this.status);
			dest.writeInt(this.sustainedMileage);
			dest.writeString(this.time);
			dest.writeString(this.type);
			dest.writeString(this.vehicleBrandName);
			dest.writeInt(this.vehicleId);
			dest.writeString(this.vehiclePicId);
			dest.writeString(this.vehicleSeriesName);
			dest.writeInt(this.year);
		}

		public DataBean() {
		}

		protected DataBean(Parcel in) {
			this.amount = in.readInt();
			this.deliverCompleted = in.readByte() != 0;
			this.delivered = in.readByte() != 0;
			this.scheduledTimeUp = in.readByte() != 0;
			this.deliverFfsPhone = in.readString();
			this.deliverName = in.readString();
			this.deliverAddress = in.readString();
			this.deliverLatitude = in.readDouble();
			this.deliverLongitude = in.readDouble();

			this.chargingRule = in.readString();
			this.configuration = in.readString();
			this.departmentId = in.readInt();
			this.departmentName = in.readString();
			this.droppedOffTime = in.readLong();
			this.endEnergyPercent = in.readInt();
			this.endOdometer = in.readInt();
			this.enrolleeId = in.readInt();
			this.enrolleeName = in.readString();
			this.enrolleePhone = in.readString();
			this.id = in.readInt();
			this.limitTime = in.readLong();
			this.maxSustainedMileage = in.readInt();
			this.no = in.readString();
			this.odometer = in.readInt();
			this.pickedUpTime = in.readLong();
			this.plateLicenseNo = in.readString();
			this.reservationLocationAddress = in.readString();
			this.reservationLocationId = in.readInt();
			this.reservationLocationLatitude = in.readDouble();
			this.reservationLocationLongitude = in.readDouble();
			this.reservationLocationName = in.readString();
			this.reservationTime = in.readLong();
			this.returnLocationAddress = in.readString();
			this.returnLocationId = in.readInt();
			this.returnLocationLatitude = in.readDouble();
			this.returnLocationLongitude = in.readDouble();
			this.returnLocationName = in.readString();
			this.scheduledDroppedOffTime = in.readLong();
			this.scheduledPickedUpTime = in.readLong();
			this.startEnergyPercent = in.readInt();
			this.startOdometer = in.readInt();
			this.status = in.readString();
			this.sustainedMileage = in.readInt();
			this.time = in.readString();
			this.type = in.readString();
			this.vehicleBrandName = in.readString();
			this.vehicleId = in.readInt();
			this.vehiclePicId = in.readString();
			this.vehicleSeriesName = in.readString();
			this.year = in.readInt();
		}

		public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
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
		dest.writeParcelable(this.data, flags);
	}

	public OrderInfoModel() {
	}

	protected OrderInfoModel(Parcel in) {
		this.status = in.readString();
		this.data = in.readParcelable(DataBean.class.getClassLoader());
	}

	public static final Creator<OrderInfoModel> CREATOR = new Creator<OrderInfoModel>() {
		@Override
		public OrderInfoModel createFromParcel(Parcel source) {
			return new OrderInfoModel(source);
		}

		@Override
		public OrderInfoModel[] newArray(int size) {
			return new OrderInfoModel[size];
		}
	};
}