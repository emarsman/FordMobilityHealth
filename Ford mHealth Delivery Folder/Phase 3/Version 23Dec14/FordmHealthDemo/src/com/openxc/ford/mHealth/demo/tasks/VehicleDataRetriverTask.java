package com.openxc.ford.mHealth.demo.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.web.WebService;

public class VehicleDataRetriverTask extends
		AsyncTask<Vehicle, Vehicle, ArrayList<Vehicle>> {
	private final String TAG = AppLog.getClassName();

	public static final String TAG_ID = "vehicleId";
	public static final String TAG_LATITUDE = "latitude";
	public static final String TAG_LONGITUDE = "longitude";
	public static final String TAG_SPEED = "vehSpeed";
	public static final String TAG_TIMESTAMP = "timeStamp";

	private ArrayList<Vehicle> vehicleDataList = null;

	@Override
	protected void onPreExecute() {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onPreExecute();
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected ArrayList<Vehicle> doInBackground(Vehicle... params) {
		AppLog.enter(TAG, AppLog.getMethodName());

		Vehicle currentVehicle = FordDemoUtil.getInstance().getVehicle();
		String vehicleId = currentVehicle.getVehicleId();

		AppLog.info(TAG, "Vehicle Id : " + vehicleId);

		String responseString = WebService.getInstance().request(
				Constants.URL_VEHICLE_ROUTE + vehicleId);

		AppLog.info(TAG, "Response String : " + responseString);

		try {
			if (responseString.contains("No record found")) {
				AppLog.info(TAG, "No record found.");
			} else {
				JSONArray jsonArray = new JSONArray(responseString);

				vehicleDataList = new ArrayList<Vehicle>();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					AppLog.info(TAG, "JSON Object : " + jsonObj);

					Vehicle vehicle = new Vehicle();

					try {
						vehicle.setVehicleId(jsonObj.getString(TAG_ID));
						vehicle.setLatitude(jsonObj.getString(TAG_LATITUDE));
						vehicle.setLongitude(jsonObj.getString(TAG_LONGITUDE));
						vehicle.setSpeed(jsonObj
								.getString(TAG_SPEED));
						vehicle.setTimeStamp(jsonObj.getString(TAG_TIMESTAMP));

					} catch (JSONException e) {
						AppLog.error(TAG, "JSONException : " + e.toString());
						continue;
					}

					AppLog.info(TAG, "Vehicle : " + vehicle);

					vehicleDataList.add(vehicle);
				}
			}

		} catch (NumberFormatException e) {
			AppLog.error(TAG, "NumberFormatException : " + e.toString());
		} catch (NullPointerException e) {
			AppLog.error(TAG, "NullPointerException : " + e.toString());
		} catch (Exception e) {
			AppLog.error(TAG, "Exception : " + e.toString());
		}

		AppLog.info(TAG, "Vehicle List : " + vehicleDataList);

		AppLog.exit(TAG, AppLog.getMethodName());
		return vehicleDataList;
	}

	@Override
	protected void onPostExecute(ArrayList<Vehicle> result) {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onPostExecute(result);
		AppLog.exit(TAG, AppLog.getMethodName());
	}

}
