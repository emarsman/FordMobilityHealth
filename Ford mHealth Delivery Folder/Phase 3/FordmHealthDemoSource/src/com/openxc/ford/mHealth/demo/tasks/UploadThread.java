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
import com.openxc.ford.mHealth.demo.database.DatabaseAdapter;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.web.WebService;

public class UploadThread extends Thread {
	private final String TAG = AppLog.getClassName();

	Handler handler;
	private int counter = 0;
	static UploadThread uploadThread;

	final String LONGITUDE = "longitude";
	final String LATITUDE = "latitude";
	final String TIMESTAMP = "timeStamp";
	final String VEH_SPEED = "vehSpeed";
	final String VEHICLE_ID = "vehicleId";
	private static final String IS_OFFLINE = "isOffline";
	private static final String RID = "rId";

	final String RESPONSE_MSG = "responseMsg";

	public static UploadThread getInstance(Handler handler) {
		if (null == uploadThread) {
			uploadThread = new UploadThread(handler);
			uploadThread.start();
		}

		return uploadThread;
	}

	private UploadThread(Handler handler) {
		AppLog.enter(TAG, AppLog.getMethodName());
		this.handler = handler;
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void run() {
		while (true) {

			if (FordDemoUtil.getInstance().isConnectedToInternet(
					FordDemoApp.getApplication())) {
				AppLog.info(TAG, "Uploading data to Web Service.");
				String response = WebService.getInstance().request(
						Constants.URL_UPLOAD_PARAMETER, getUploadJson());
				AppLog.info(TAG, "Response : " + response);

				if (!(response.equals(""))) {
					counter = counter + 1;
					Message msg = handler.obtainMessage();
					msg.what = Constants.UPDATE_COUNTER;

					msg.arg1 = counter;

					handler.sendMessage(msg);
				} else {
					insertData();
				}
			} else {
				insertData();
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertData() {
		AppLog.info(TAG, "Inserting data to Database.");

		if (Constants.OFFLINE_COUNTER >= Constants.MAX_ROW_COUNT) {
			Constants.OFFLINE_COUNTER = Constants.MAX_ROW_COUNT;
		} else {
			Constants.OFFLINE_COUNTER = Constants.OFFLINE_COUNTER + 1;
		}

		if (Constants.DATABASE_LENGTH <= Constants.MAX_ROW_COUNT) {
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

		Message msg = handler.obtainMessage();
		msg.what = Constants.UPDATE_COUNTER_DATABASE;

		msg.arg1 = Constants.OFFLINE_COUNTER;

		handler.sendMessage(msg);
	}

	private JSONObject getUploadJson() {
		AppLog.enter(TAG, AppLog.getMethodName());

		Vehicle params = FordDemoUtil.getInstance().getVehicle();
		VehiclePreferences pref = new VehiclePreferences(
				FordDemoApp.getApplication());

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(VEHICLE_ID, pref.getToken());

			if (null != params) {
				jsonObject.put(VEH_SPEED, params.getVehicleSpeed());
				jsonObject.put(LONGITUDE, params.getLongitude());
				jsonObject.put(LATITUDE, params.getLatitude());

				jsonObject.put(TIMESTAMP, params.getTimeStamp());

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

	private JSONObject getUploadJson(int offlineFlag, int rId) {
		AppLog.enter(TAG, AppLog.getMethodName());

		Vehicle params = FordDemoUtil.getInstance().getVehicle();
		VehiclePreferences pref = new VehiclePreferences(
				FordDemoApp.getApplication());

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(VEHICLE_ID, pref.getToken());

			if (null != params) {
				jsonObject.put(VEH_SPEED, params.getVehicleSpeed());
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
