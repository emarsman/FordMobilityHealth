package com.openxc.ford.mHealth.demo.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.model.Patient;
import com.openxc.ford.mHealth.demo.web.WebService;

public class PatientsRetriverTask extends
		AsyncTask<String, Patient, ArrayList<Patient>> {

	private final String TAG = AppLog.getClassName();

	private final String TAG_ID = "uUid";
	private final String TAG_NAME = "name";
	private final String TAG_ADDRESS = "address1";
	private final String TAG_VILLAGE = "village";
	private final String TAG_POSTAL_CODE = "postalCode";
	private final String TAG_CONTACT_NO = "phone";

	private int tag = -1;
	private String value = null;

	public PatientsRetriverTask(int tag, String value) {
		this.tag = tag;
		this.value = value;
	}

	@Override
	protected void onPreExecute() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onPreExecute();
		try {
			AppLog.info(TAG, "Waitting... for updating records.");
			Thread.sleep(1000);
			AppLog.info(TAG, "Records updated.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected ArrayList<Patient> doInBackground(String... params) {

		AppLog.enter(TAG, AppLog.getMethodName());

		AppLog.info(TAG, "Tag : " + tag);
		AppLog.info(TAG, "Value : " + value);

		String requestURL = null;
		switch (tag) {
		case Constants.TAG_PATIENT_BY_NAME:
			requestURL = Constants.URL_PATIENT_BY_PATIENT_NAME + value;
			break;

		case Constants.TAG_PATIENT_BY_POSTAL_CODE:
			requestURL = Constants.URL_PATIENT_BY_POSTAL_CODE + value;
			break;
		case Constants.TAG_PATIENT_BY_VILLAGE:
			requestURL = Constants.URL_PATIENT_BY_VILLAGE + value;
			break;

		default:
			break;
		}

		AppLog.info(TAG, "Request URL : " + requestURL);

		String responseString = WebService.getInstance().request(requestURL);

		AppLog.info(TAG, "Response String : " + responseString);

		ArrayList<Patient> patientsList = null;

		try {
			if (responseString.contains("object not found")
					|| responseString.equalsIgnoreCase("[]")) {

			} else {

				patientsList = new ArrayList<Patient>();

				JSONArray jsonArray = new JSONArray(responseString);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					AppLog.info(TAG, "JSON Object : " + jsonObj);

					Patient patient = new Patient();

					if (Constants.TAG_PATIENT_BY_NAME == tag) {
						try {

							String uuid = jsonObj.getString("uuid");
							JSONObject jsonPerson = jsonObj
									.getJSONObject("person");
							String name = jsonPerson.getString("display");

							JSONObject jsonAddress = jsonPerson
									.getJSONObject("preferredAddress");

							String address = jsonAddress.getString("address1");
							String postal = jsonAddress.getString("postalCode");
							String village = jsonAddress
									.getString("cityVillage");

							JSONArray jsonContactAttribute = jsonPerson
									.getJSONArray("attributes");
							JSONObject jsonContact = jsonContactAttribute
									.getJSONObject(0);

							String contact = jsonContact.getString("value");

							patient.setId(uuid);
							patient.setName(name);
							patient.setAddress(address);
							patient.setVillage(village);
							patient.setPostalCode(postal);
							patient.setContactNumber(contact);

						} catch (JSONException e) {
							AppLog.error(TAG, "JSONException : " + e.toString());
							continue;
						}

					} else {
						try {
							patient.setId(jsonObj.getString(TAG_ID));
							patient.setName(jsonObj.getString(TAG_NAME));
							patient.setAddress(jsonObj.getString(TAG_ADDRESS));
							patient.setVillage(jsonObj.getString(TAG_VILLAGE));
							patient.setPostalCode(jsonObj
									.getString(TAG_POSTAL_CODE));
							patient.setContactNumber(jsonObj
									.getString(TAG_CONTACT_NO));
						} catch (JSONException e) {
							AppLog.error(TAG, "JSONException : " + e.toString());
							continue;
						}
					}
					AppLog.info(TAG, "Patient : " + patient);
					patientsList.add(patient);
				}
			}

		} catch (Exception e) {
			AppLog.error(TAG, "Error : " + e.toString());
			return null;
		}

		AppLog.info(TAG, "Patient List : " + patientsList);

		AppLog.exit(TAG, AppLog.getMethodName());
		return patientsList;
	}

	@Override
	protected void onPostExecute(ArrayList<Patient> patientsList) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onPostExecute(patientsList);

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}
