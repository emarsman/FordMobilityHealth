package com.openxc.ford.mHealth.demo.model;

public class Vehicle {

	private int isOffline = -1;
	private String vehicleId = "";
	private String odometer = "";
	private String fuelLevel = "";
	private String fuelConsumedSinceRestart = "";
	private String latitude = "";
	private String longitude = "";
	private String ignitionStatus = "";
	private String headLampStatus = "";
	private String viperStatus = "";
	private String gearLeverPosition = "ndsh";
	private String airBagStatus;
	private String batteryStatus;
	private String mainOdometer;
	private String tyrePressure;
	private String speed = "";
	private String timeStamp = "";
	private String address = "";
	private int rId;

	private int id;

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public int getIsOffline() {
		return isOffline;
	}

	public void setIsOffline(int isOffline) {
		this.isOffline = isOffline;
	}

	public String getAirBagStatus() {
		return airBagStatus;
	}

	public void setAirBagStatus(String airBagStatus) {
		this.airBagStatus = airBagStatus;
	}

	public String getBatteryStatus() {
		return batteryStatus;
	}

	public void setBatteryStatus(String batteryStatus) {
		this.batteryStatus = batteryStatus;
	}

	public String getMainOdometer() {
		return mainOdometer;
	}

	public void setMainOdometer(String mainOdometer) {
		this.mainOdometer = mainOdometer;
	}

	public String getTyrePressure() {
		return tyrePressure;
	}

	public void setTyrePressure(String tyrePressure) {
		this.tyrePressure = tyrePressure;
	}

	public String getOdometer() {
		return odometer;
	}

	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}

	public String getFuelLevel() {
		return fuelLevel;
	}

	public void setFuelLevel(String fuelLevel) {
		this.fuelLevel = fuelLevel;
	}

	public String getFuelConsumedSinceRestart() {
		return fuelConsumedSinceRestart;
	}

	public void setFuelConsumedSinceRestart(String fuelConsumedSinceRestart) {
		this.fuelConsumedSinceRestart = fuelConsumedSinceRestart;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getIgnitionStatus() {
		return ignitionStatus;
	}

	public void setIgnitionStatus(String ignitionStatus) {
		this.ignitionStatus = ignitionStatus;
	}

	public String getGearLeverPosition() {
		return gearLeverPosition;
	}

	public void setGearLeverPosition(String gearLeverPosition) {
		this.gearLeverPosition = gearLeverPosition;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {

		String toString = "isOffline : " + isOffline + " vehicleId : "
				+ vehicleId + " odometer : " + odometer + " fuelLevel : "
				+ fuelLevel + " fuelConsumedSinceRestart : "
				+ fuelConsumedSinceRestart + " latitude : " + latitude
				+ " longitude : " + longitude + " ignitionStatus : "
				+ ignitionStatus + " gearLeverPosition : " + gearLeverPosition
				+ " airBagStatus : " + airBagStatus + " batteryStatus : "
				+ batteryStatus + " mainOdometer : " + mainOdometer
				+ " tyrePressure : " + tyrePressure + " vehicleSpeed : "
				+ speed + " timeStamp : " + timeStamp + " address : " + address;
		return toString;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getrId() {
		return rId;
	}

	public void setrId(int rId) {
		this.rId = rId;
	}

	public String getHeadLampStatus() {
		return headLampStatus;
	}

	public void setHeadLampStatus(String headLampStatus) {
		this.headLampStatus = headLampStatus;
	}

	public String getViperStatus() {
		return viperStatus;
	}

	public void setViperStatus(String viperStatus) {
		this.viperStatus = viperStatus;
	}
}
