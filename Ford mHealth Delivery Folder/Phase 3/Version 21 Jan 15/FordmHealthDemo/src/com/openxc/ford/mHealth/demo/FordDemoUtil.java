package com.openxc.ford.mHealth.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.openxc.ford.mHealth.demo.model.Patient;
import com.openxc.ford.mHealth.demo.model.Vehicle;

public class FordDemoUtil {

	private final String TAG = AppLog.getClassName();
	private static FordDemoUtil sFordDemoUtil = null;
	private ServiceConnection serviceConnection = null;
	private String driveTraceFilePath = null;
	private Vehicle vehicle = null;
	private Handler handler = null;

	private List<Vehicle> mVehicleRoute = new ArrayList<Vehicle>();
	private List<Patient> mPatientsList = new ArrayList<Patient>();

	private FordDemoUtil() {
	}

	public static FordDemoUtil getInstance() {
		if (null == sFordDemoUtil) {
			sFordDemoUtil = new FordDemoUtil();
		}

		return sFordDemoUtil;
	}

	public ServiceConnection getServiceConnection() {
		return serviceConnection;
	}

	public void setServiceConnection(ServiceConnection serviceConnection) {
		this.serviceConnection = serviceConnection;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getDriveTraceFilePath() {
		return driveTraceFilePath;
	}

	public void setDriveTraceFilePath(String driveTraceFilePath) {
		this.driveTraceFilePath = driveTraceFilePath;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public List<Vehicle> getVehicleRoute() {
		return mVehicleRoute;
	}

	public void setVehicleRoute(List<Vehicle> vehicleRoute) {
		this.mVehicleRoute = vehicleRoute;
	}

	public List<Patient> getPatientsList() {
		return mPatientsList;
	}

	public void setPatientsList(List<Patient> patientsList) {
		this.mPatientsList = patientsList;
	}

	public boolean isConnectedToInternet(Context context) {
		AppLog.enter(TAG, AppLog.getMethodName());

		boolean result = false;

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			result = true;
		} else {
			result = false;
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return result;
	}

	public void showAlert(String warning, Context context) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle("Alert Dialog");

		// Setting Dialog Message
		alertDialog.setMessage(warning);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed

			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}