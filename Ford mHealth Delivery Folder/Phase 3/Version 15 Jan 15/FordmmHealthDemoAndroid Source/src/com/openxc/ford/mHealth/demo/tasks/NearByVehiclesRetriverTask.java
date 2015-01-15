package com.openxc.ford.mHealth.demo.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.activity.NearByVehicleActivity;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.web.WebService;

public class NearByVehiclesRetriverTask extends
		AsyncTask<Vehicle, Vehicle, String> {

	private final String TAG = AppLog.getClassName();

	public static final String TAG_ID = "vehicleId";
	public static final String TAG_LATITUDE = "latitude";
	public static final String TAG_LONGITUDE = "longitude";
	public static final String TAG_ADDRESS = "address";

	private NearByVehicleActivity activity = null;

	public NearByVehiclesRetriverTask(NearByVehicleActivity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onPreExecute();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected String doInBackground(Vehicle... arg0) {
		AppLog.enter(TAG, AppLog.getMethodName());

		Vehicle currentVehicle = FordDemoUtil.getInstance().getVehicle();

		AppLog.info(TAG, "Current Vehicle : " + currentVehicle);

		String responseString = WebService.getInstance().request(
				Constants.URL_ALL_VEHICLE_LOCATION);

		AppLog.info(TAG, "Response String : " + responseString);

		try {
			if (responseString.contains("No record found")) {
				AppLog.info(TAG, "No record found.");
			} else {
				JSONArray jsonArray = new JSONArray(responseString);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					AppLog.info(TAG, "JSON Object : " + jsonObj);

					Vehicle vehicle = new Vehicle();

					try {
						vehicle.setVehicleId(jsonObj.getString(TAG_ID));
						vehicle.setLatitude(jsonObj.getString(TAG_LATITUDE));
						vehicle.setLongitude(jsonObj.getString(TAG_LONGITUDE));

					} catch (JSONException e) {
						AppLog.error(TAG, "JSONException : " + e.toString());
						continue;
					}

					AppLog.info(TAG, "Vehicle : " + vehicle);

					double startLatitude = Double.parseDouble(currentVehicle
							.getLatitude());
					double startLongitude = Double.parseDouble(currentVehicle
							.getLongitude());
					double endLatitude = Double.parseDouble(vehicle
							.getLatitude());
					double endLongitude = Double.parseDouble(vehicle
							.getLongitude());

					float[] results = new float[2];

					Location.distanceBetween(startLatitude, startLongitude,
							endLatitude, endLongitude, results);

					AppLog.info(TAG, "Results : " + results);

					if (null != results && results.length > 0) {
						float distance = results[0];
						AppLog.info(TAG, "Distance : " + distance);

						// TODO: Need to add condition for distance.
						if (distance > 0) {
							AppLog.info(TAG, "Adding to vehiclesList.");
							AppLog.info(TAG, "Publishing Progress...");
							publishProgress(vehicle);
						} else {
							AppLog.info(TAG, "Distance is less.");
						}
					} else {
						AppLog.info(TAG, "No Result found.");
					}
				}
			}

		} catch (NumberFormatException e) {
			AppLog.error(TAG, "NumberFormatException : " + e.toString());
		} catch (NullPointerException e) {
			AppLog.error(TAG, "NullPointerException : " + e.toString());
		} catch (Exception e) {
			AppLog.error(TAG, "Exception : " + e.toString());
		}

		AppLog.exit(TAG, AppLog.getMethodName());
		return "DONE";
	}

	@Override
	protected void onProgressUpdate(Vehicle... values) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onProgressUpdate(values);
		activity.drawMarker(values[0]);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onPostExecute(String result) {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onPostExecute(result);
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onCancelled() {
		AppLog.enter(TAG, AppLog.getMethodName());

		activity = null;
		super.onCancelled();

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}
