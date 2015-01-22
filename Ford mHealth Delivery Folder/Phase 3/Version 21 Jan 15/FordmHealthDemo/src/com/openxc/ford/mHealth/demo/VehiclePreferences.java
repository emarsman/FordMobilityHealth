package com.openxc.ford.mHealth.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class VehiclePreferences {
	private final String TAG = AppLog.getClassName();

	public static final String PREF_NAME = "ConfigData";
	public static final String KEY_TOKEN = "Token";
	public static final String KEY_NAME = "Name";
	public static final String KEY_IP_ADDRESS = "ip_address";
	public static final String KEY_PORT = "port";
	public static final String KEY_INTERVAL = "interval";

	private SharedPreferences mPref;
	private Editor mEditor;
	private Context mContext;

	public VehiclePreferences(Context context) {
		AppLog.enter(TAG, AppLog.getMethodName());

		this.mContext = context;
		mPref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		mEditor = mPref.edit();

		AppLog.exit(TAG, AppLog.getMethodName());
	}
	
	
	public void saveInterval(int interval) {
		AppLog.enter(TAG, AppLog.getMethodName());

		mEditor.putInt(KEY_INTERVAL, interval);
		mEditor.commit();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public int getInterval() {
		AppLog.enter(TAG, AppLog.getMethodName());

		int interval = mPref.getInt(KEY_INTERVAL, 30000);

		AppLog.exit(TAG, AppLog.getMethodName());
		return interval;
	}
	
	

	public void saveToken(String token) {
		AppLog.enter(TAG, AppLog.getMethodName());

		mEditor.putString(KEY_TOKEN, token);
		mEditor.commit();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public void saveName(String name) {
		AppLog.enter(TAG, AppLog.getMethodName());

		mEditor.putString(KEY_NAME, name);
		mEditor.commit();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public String getToken() {
		AppLog.enter(TAG, AppLog.getMethodName());

		String token = mPref.getString(KEY_TOKEN, null);

		AppLog.exit(TAG, AppLog.getMethodName());
		return token;
	}

	public String getName() {
		AppLog.enter(TAG, AppLog.getMethodName());

		String name = mPref.getString(KEY_NAME, "");

		AppLog.exit(TAG, AppLog.getMethodName());
		return name;
	}

	public void saveIpAddress(String ipAddress) {
		AppLog.enter(TAG, AppLog.getMethodName());

		mEditor.putString(KEY_IP_ADDRESS, ipAddress);
		mEditor.commit();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public String getIpAddress() {
		AppLog.enter(TAG, AppLog.getMethodName());

		String ipAddress = mPref.getString(KEY_IP_ADDRESS, null);

		AppLog.exit(TAG, AppLog.getMethodName());
		return ipAddress;
	}

	public void savePort(String port) {
		AppLog.enter(TAG, AppLog.getMethodName());

		mEditor.putString(KEY_PORT, port);
		mEditor.commit();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public String getPort() {
		AppLog.enter(TAG, AppLog.getMethodName());

		String port = mPref.getString(KEY_PORT, null);

		AppLog.exit(TAG, AppLog.getMethodName());
		return port;
	}

}