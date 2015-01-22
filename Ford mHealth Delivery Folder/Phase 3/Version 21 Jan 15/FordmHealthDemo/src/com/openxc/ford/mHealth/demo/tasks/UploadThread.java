package com.openxc.ford.mHealth.demo.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoApp;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.VehiclePreferences;
import com.openxc.ford.mHealth.demo.activity.FordDemoActivity;
import com.openxc.ford.mHealth.demo.database.DatabaseAdapter;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.web.WebService;

/**
 * UploadThread used to send data when in internet connection is available and
 * if internet connection is not vailable it stores data in local application
 * database
 */

public class UploadThread extends Thread {
	private final String TAG = AppLog.getClassName();

	private Handler handler;
	private int counter = 0;
	private static UploadThread sUploadThread;

	final String LONGITUDE = "longitude";
	final String LATITUDE = "latitude";
	final String TIMESTAMP = "timeStamp";
	final String VEH_SPEED = "vehSpeed";
	final String VEHICLE_ID = "vehicleId";
	private static final String IS_OFFLINE = "isOffline";
	private static final String RID = "rId";
	private VehiclePreferences vehiclePref = new VehiclePreferences(
			FordDemoApp.getApplication());

	final String RESPONSE_MSG = "responseMsg";

	public static UploadThread getInstance() {
		if (null == sUploadThread) {
			sUploadThread = new UploadThread();

		}

		return sUploadThread;
	}

	private UploadThread() {
		AppLog.enter(TAG, AppLog.getMethodName());

		this.handler = FordDemoUtil.getInstance().getHandler();
		start();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void run() {
		AppLog.enter(TAG, AppLog.getMethodName());

		while (FordDemoActivity.isUploadingRequired) {

			AppLog.info(TAG, "Executing While...");
			try {
				Thread.sleep(vehiclePref.getInterval());
			} catch (InterruptedException e) {
				AppLog.info(TAG, "Upload Thread intrupted, returning...");
				AppLog.error(TAG, "Error : " + e.toString());
				return;

			}

			// Condition to check internet connection presense

			if (getUploadJson() != null) {
				if (FordDemoUtil.getInstance().isConnectedToInternet(
						FordDemoApp.getApplication())) {
					AppLog.info(TAG, "Uploading data to Web Service.");

					String response = WebService.getInstance().request(
							Constants.URL_UPLOAD_PARAMETER, getUploadJson());
					AppLog.info(TAG, "Response : " + response);

					if (!(response.equals(""))) {
						counter = counter + 1;
						Message msg = handler.obtainMessage();
						msg.what = Constants.TAG_UPDATE_COUNTER;

						msg.arg1 = counter;
						AppLog.info(TAG, "Sending Message...");
						handler.sendMessageDelayed(msg, 2000);
					} else {
						insertData();
					}

				} else {
					insertData();
				}
			} else {

				Message msg = handler.obtainMessage();
				msg.what = Constants.TAG_BLANK;

				handler.sendMessageDelayed(msg, 100);

			}

		}
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public boolean cancelUploadThread() {
		AppLog.enter(TAG, AppLog.getMethodName());

		boolean bResult = false;
		if (isAlive()) {
			interrupt();
			bResult = true;
		}

		handler = null;
		sUploadThread = null;

		AppLog.exit(TAG, AppLog.getMethodName());
		return bResult;
	}

	// Method for insert data to data base when Internet connection is not
	// available
	// OR when you are connected to a restricted network
	public void insertData() {
		AppLog.info(TAG, "Inserting data to Database...");

		if (Constants.OFFLINE_COUNTER >= Constants.MAX_ROW_COUNT) {
			Constants.OFFLINE_COUNTER = Constants.MAX_ROW_COUNT;
		} else {
			Constants.OFFLINE_COUNTER = Constants.OFFLINE_COUNTER + 1;
		}

		if (Constants.DATABASE_LENGTH < Constants.MAX_ROW_COUNT) {
			Constants.DATABASE_LENGTH = Constants.DATABASE_LENGTH + 1;

			DatabaseAdapter.getInstance().insertData(
					FordDemoApp.getApplication(),
					getUploadJson(1, Constants.DATABASE_LENGTH));

			AppLog.info(TAG, "DATABASE_LENGTH : " + Constants.DATABASE_LENGTH);
			AppLog.info(TAG, "NEXT_ROW_COUNTER : " + Constants.NEXT_ROW_COUNTER);

		} else {

			if (Constants.NEXT_ROW_COUNTER < 0) {
				Constants.NEXT_ROW_COUNTER = DatabaseAdapter.getInstance()
						.getLastUpdatedRecordId(FordDemoApp.getApplication()) + 1;
			}

			if (Constants.NEXT_ROW_COUNTER <= Constants.MAX_ROW_COUNT) {
				DatabaseAdapter.getInstance().UpdateData(
						FordDemoApp.getApplication(),
						getUploadJson(1, Constants.DATABASE_LENGTH),
						Constants.NEXT_ROW_COUNTER);

			} else {
				Constants.NEXT_ROW_COUNTER = 1;
				DatabaseAdapter.getInstance().UpdateData(
						FordDemoApp.getApplication(),
						getUploadJson(1, Constants.DATABASE_LENGTH),
						Constants.NEXT_ROW_COUNTER);

			}
			AppLog.info(TAG, "DATABASE_LENGTH else : "
					+ Constants.DATABASE_LENGTH);
			AppLog.info(TAG, "NEXT_ROW_COUNTER else : "
					+ Constants.NEXT_ROW_COUNTER);

			Constants.NEXT_ROW_COUNTER = Constants.NEXT_ROW_COUNTER + 1;

		}
		// Code block to send data using handler to update the UI Thread from
		// this Non UI Thread
		Message msg = handler.obtainMessage();
		msg.what = Constants.TAG_UPDATE_COUNTER_DATABASE;

		msg.arg1 = Constants.OFFLINE_COUNTER;
		AppLog.info(TAG, "Sending Message...");
		handler.sendMessage(msg);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private JSONObject getUploadJson() {
		AppLog.enter(TAG, AppLog.getMethodName());

		Vehicle params = FordDemoUtil.getInstance().getVehicle();

		if (params.getSpeed().equals("") || params.getLatitude().equals("")
				|| params.getLongitude().equals("")
				|| params.getTimeStamp().equals("")) {
			return null;
		} else {
			VehiclePreferences pref = new VehiclePreferences(
					FordDemoApp.getApplication());

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put(VEHICLE_ID, pref.getToken());

				if (null != params) {
					jsonObject.put(VEH_SPEED, params.getSpeed());
					jsonObject.put(LONGITUDE, params.getLongitude());
					jsonObject.put(LATITUDE, params.getLatitude());

					jsonObject.put(TIMESTAMP, params.getTimeStamp());

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			AppLog.info(TAG, "Vehicle data to upload : " + jsonObject);
			AppLog.exit(TAG, AppLog.getMethodName());

			return jsonObject;
		}

	}

	private JSONObject getUploadJson(int offlineFlag, int rId) {
		AppLog.enter(TAG, AppLog.getMethodName());

		Vehicle params = FordDemoUtil.getInstance().getVehicle();
		VehiclePreferences pref = new VehiclePreferences(
				FordDemoApp.getApplication());

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(VEHICLE_ID, pref.getToken());

			if (null != params) {
				jsonObject.put(VEH_SPEED, params.getSpeed());
				jsonObject.put(LONGITUDE, params.getLongitude());
				jsonObject.put(LATITUDE, params.getLatitude());

				jsonObject.put(TIMESTAMP, params.getTimeStamp());
				jsonObject.put(IS_OFFLINE, offlineFlag);
				jsonObject.put(RID, rId);
			} else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AppLog.info(TAG, "Vehicle data to upload : " + jsonObject);
		AppLog.exit(TAG, AppLog.getMethodName());
		return jsonObject;
	}

}
