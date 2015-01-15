package com.openxc.ford.mHealth.demo.tasks;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoApp;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.adapter.VehicleDataAdapter;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.web.WebService;

public class VehicleDataRetriverTask extends
		AsyncTask<Vehicle, Vehicle, String> {
	private final String TAG = AppLog.getClassName();

	public static final String TAG_ID = "vehicleId";
	public static final String TAG_LATITUDE = "latitude";
	public static final String TAG_LONGITUDE = "longitude";
	public static final String TAG_SPEED = "vehSpeed";
	public static final String TAG_TIMESTAMP = "timeStamp";
	private List<Vehicle> mVehicleRoute = null;
	private VehicleDataAdapter mAdapter = null;

	public VehicleDataRetriverTask(VehicleDataAdapter adapter) {
		this.mAdapter = adapter;
		this.mVehicleRoute = FordDemoUtil.getInstance().getVehicleRoute();
	}

	@Override
	protected void onPreExecute() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onPreExecute();
		Toast.makeText(FordDemoApp.getApplication(),
				"Retrieving data from cloud...", Toast.LENGTH_LONG).show();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected String doInBackground(Vehicle... params) {
		AppLog.enter(TAG, AppLog.getMethodName());

		String result = null;

		Vehicle currentVehicle = FordDemoUtil.getInstance().getVehicle();
		String vehicleId = currentVehicle.getVehicleId();

		AppLog.info(TAG, "Vehicle Id : " + vehicleId);

		String responseString = null;
		try {
			responseString = WebService.getInstance().request(
					Constants.URL_VEHICLE_ROUTE + vehicleId);

			AppLog.info(TAG, "Response String : " + responseString);

			if (responseString.contains("No record found")) {
				AppLog.info(TAG, "No record found.");
				result = Constants.RESULT_NO_RECORD;
			} else {

				mVehicleRoute.clear();

				JSONArray jsonArray = new JSONArray(responseString);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					AppLog.info(TAG, "JSON Object : " + jsonObj);

					Vehicle vehicle = new Vehicle();

					try {
						vehicle.setVehicleId(jsonObj.getString(TAG_ID));
						vehicle.setLatitude(jsonObj.getString(TAG_LATITUDE));
						vehicle.setLongitude(jsonObj.getString(TAG_LONGITUDE));
						vehicle.setSpeed(jsonObj.getString(TAG_SPEED));
						vehicle.setTimeStamp(jsonObj.getString(TAG_TIMESTAMP));

					} catch (JSONException e) {
						AppLog.error(TAG, "JSONException : " + e.toString());
						continue;
					}

					AppLog.info(TAG, "Vehicle : " + vehicle);

					mVehicleRoute.add(vehicle);
					result = Constants.RESULT_UPDATED;
				}
			}

		} catch (NumberFormatException e) {
			AppLog.error(TAG, "NumberFormatException : " + e.toString());
			result = Constants.RESULT_ERROR;
		} catch (NullPointerException e) {
			AppLog.error(TAG, "NullPointerException : " + e.toString());
			result = Constants.RESULT_ERROR;
		} catch (Exception e) {
			AppLog.error(TAG, "Exception : " + e.toString());
			result = Constants.RESULT_ERROR;
		}

		AppLog.info(TAG, "Vehicle List : " + mVehicleRoute);

		AppLog.exit(TAG, AppLog.getMethodName());
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onPostExecute(result);
		AppLog.info(TAG, "Vehicle Route Retriver Task result is : " + result);

		if (result.equalsIgnoreCase(Constants.RESULT_NO_RECORD)
				|| mVehicleRoute.size() <= 0) {
			mVehicleRoute.clear();
			
			Toast.makeText(FordDemoApp.getApplication(),
					"No record found.", Toast.LENGTH_LONG).show();
		}

		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}
	
	@Override
	protected void onCancelled() {
		AppLog.enter(TAG, AppLog.getMethodName());
		
		mAdapter = null;
		super.onCancelled();
		
		AppLog.exit(TAG, AppLog.getMethodName());
	}
}
