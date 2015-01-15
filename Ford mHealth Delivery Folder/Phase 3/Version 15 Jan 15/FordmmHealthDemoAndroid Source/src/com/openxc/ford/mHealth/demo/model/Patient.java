package com.openxc.ford.mHealth.demo.model;

public class Patient {

	private String id = null;
	private String name = null;
	private String address = null;
	private String village = null;
	private String postalCode = null;
	private String contactNumber = null;
	private String latitude = null;
	private String longitude = null;

	public Patient() {
		super();
	}

	public Patient(String id, String name, String address, String village,
			String postalCode, String contactNumber) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.village = village;
		this.postalCode = postalCode;
		this.contactNumber = contactNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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

	@Override
	public String toString() {
		String toString = "id : " + id + " name : " + name + " address : "
				+ address + " village : " + village + " postalCode : "
				+ postalCode + " contactNumber : " + contactNumber
				+ " latitude : " + latitude + " longitude : " + longitude;
		return toString;
	}
}
