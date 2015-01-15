package com.openxc.ford.mHealth.demo.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoApp;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.activity.NearByPatientsActivity;
import com.openxc.ford.mHealth.demo.model.Patient;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.web.WebService;

public class NearByPatientsRetriverTask extends
		AsyncTask<String, Patient, String> {

	private final String TAG = AppLog.getClassName();

	private final String TAG_ID = "uUid";
	private final String TAG_NAME = "name";
	private final String TAG_ADDRESS_1 = "address1";
	private final String TAG_ADDRESS_2 = "address2";
	private final String TAG_VILLAGE = "village";
	private final String TAG_STATE = "state";
	private final String TAG_POSTAL_CODE = "postalCode";
	private final String TAG_CONTACT_NO = "phone";

	private NearByPatientsActivity activity = null;

	public NearByPatientsRetriverTask(NearByPatientsActivity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onPreExecute();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected String doInBackground(String... params) {

		AppLog.enter(TAG, AppLog.getMethodName());

		Vehicle currentVehicle = FordDemoUtil.getInstance().getVehicle();

		AppLog.info(TAG, "Current Vehicle : " + currentVehicle);

		double latitude = Double.parseDouble(currentVehicle.getLatitude());
		double longitude = Double.parseDouble(currentVehicle.getLongitude());

		String village = getVillage(latitude, longitude);

		AppLog.info(TAG, "Village : " + village);
		String requestURL = Constants.URL_PATIENT_BY_VILLAGE + village;
		AppLog.info(TAG, "Request URL : " + requestURL);
		String responseString = WebService.getInstance().request(requestURL);
		AppLog.info(TAG, "Response String : " + responseString);

		try {
			if (responseString.contains("object not found")) {
			} else {

				JSONArray jsonArray = new JSONArray(responseString);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					AppLog.info(TAG, "JSON Object : " + jsonObj);

					Patient patient = new Patient();

					try {
						patient.setId(jsonObj.getString(TAG_ID));
						patient.setName(jsonObj.getString(TAG_NAME));

						String patientAddress = jsonObj
								.getString(TAG_ADDRESS_1)
								+ " "
								+ jsonObj.getString(TAG_ADDRESS_2)
								+ " "
								+ jsonObj.getString(TAG_VILLAGE)
								+ " "
								+ jsonObj.getString(TAG_STATE);

						patient.setAddress(patientAddress);
						patient.setVillage(jsonObj.getString(TAG_VILLAGE));
						patient.setPostalCode(jsonObj
								.getString(TAG_POSTAL_CODE));
						patient.setContactNumber(jsonObj
								.getString(TAG_CONTACT_NO));
					} catch (JSONException e) {
						AppLog.error(TAG, "JSONException : " + e.toString());
						continue;
					}

					AppLog.info(TAG, "Patient : " + patient);

					String add = patient.getAddress();

					AppLog.info(TAG, "Patient Address is : " + add);

					Geocoder coder = new Geocoder(FordDemoApp.getApplication());
					try {
						ArrayList<Address> adresses = (ArrayList<Address>) coder
								.getFromLocationName(add, 1);
						Address address = adresses.get(0);

						latitude = address.getLatitude();
						longitude = address.getLongitude();

						AppLog.info(TAG, "Latitude : " + latitude);
						AppLog.info(TAG, "Longitude : " + longitude);

						patient.setLatitude(String.valueOf(latitude));
						patient.setLongitude(String.valueOf(longitude));
					} catch (IOException e) {
						AppLog.error(TAG, "Error : " + e.toString());
						return null;
					}
					AppLog.info(TAG, "Adding... Patient : " + patient);
					AppLog.info(TAG, "Publish Progress...");
					publishProgress(patient);
				}
			}

		} catch (Exception e) {
			AppLog.error(TAG, "Error : " + e.toString());
			return null;
		}

		AppLog.exit(TAG, AppLog.getMethodName());
		return "DONE";
	}

	@Override
	protected void onProgressUpdate(Patient... values) {
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

	private String getVillage(double latitude, double longitude) {
		AppLog.enter(TAG, AppLog.getMethodName());

		AppLog.info(TAG, "Latitude : " + latitude);
		AppLog.info(TAG, "Longitude : " + longitude);

		String locality = "";
		Geocoder geocoder = new Geocoder(FordDemoApp.getApplication(),
				Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(latitude,
					longitude, 1);
			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				locality = returnedAddress.getLocality();
				locality = locality.replaceAll(" ", "%20");

				AppLog.info(TAG, "Current loction's locality is :" + locality);
			} else {
				AppLog.info(TAG, "No locality returned!");
			}
		} catch (Exception e) {
			AppLog.error(TAG, "Canont get locality!" + e.toString());
		}

		AppLog.exit(TAG, AppLog.getMethodName());
		return locality;
	}
}