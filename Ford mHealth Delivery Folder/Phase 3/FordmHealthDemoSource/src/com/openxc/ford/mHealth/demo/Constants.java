package com.openxc.ford.mHealth.demo;

public class Constants {
	public static final int TAG_PATIENT_BY_NAME = 111;
	public static final int TAG_PATIENT_BY_POSTAL_CODE = 222;
	public static final int TAG_PATIENT_BY_VILLAGE = 333;

	public static final int UPDATE_COUNTER = 457;
	public static final int UPDATE_CLOUD_CONNECT = 452;
	

	public static final int UPDATE_COUNTER_CACHE = 458;
	public static final int UPDATE_COUNTER_DATABASE = 459;

	public static int OFFLINE_COUNTER = 0;
	public static int NEXT_ROW_COUNTER = -1;
	public static int DATABASE_LENGTH = 0;
	

	public static final int MAX_ROW_COUNT = 120;

	
	public static String BASE_URL = "http://" + getIPAddress() + ":"
			+ getPort() + "/motech-platform-server/module";

	public static final String URL_MOTECH_BASE = "/mHealthDataInterface";
	
	public static final String URL_OPENXC_BASE = "/OpenXCDataInterface";

	public static final String URL_REGISTRATION = BASE_URL + URL_OPENXC_BASE
			+ "/registermHealthVehicle";

	public static final String URL_UPLOAD_PARAMETER = BASE_URL
			+ URL_OPENXC_BASE + "/uploadmHealthVehicleData";

	public static final String URL_PATIENT_BY_POSTAL_CODE = BASE_URL
			+ URL_MOTECH_BASE + "/patientsByPostalCode/";

	public static final String URL_PATIENT_BY_PATIENT_NAME = BASE_URL
			+ URL_MOTECH_BASE + "/patientsDetailByName/";

	public static final String URL_PATIENT_BY_VILLAGE = BASE_URL
			+ URL_MOTECH_BASE + "/patientsByVillage/";

	public static final String URL_ALL_VEHICLE_LOCATION = BASE_URL
			+ URL_OPENXC_BASE + "/getAllmHealthVehiclesLocation";

	public static final String URL_LOCATION = BASE_URL + URL_OPENXC_BASE
			+ "/getmHealthVehicleLocation/";

	public static String getIPAddress() {
		String ipAddress = new VehiclePreferences(FordDemoApp.getApplication())
				.getIpAddress();
		return ipAddress;
	}

	public static String getPort() {
		String port = new VehiclePreferences(FordDemoApp.getApplication())
				.getPort();
		return port;
	}
}
