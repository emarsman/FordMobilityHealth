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
import com.openxc.ford.mHealth.demo.adapter.PatientsListAdapter;
import com.openxc.ford.mHealth.demo.model.Patient;
import com.openxc.ford.mHealth.demo.web.WebService;

public class PatientsRetriverTask extends AsyncTask<String, Patient, String> {

	private final String TAG = AppLog.getClassName();

	private final String TAG_ID = "uUid";
	private final String TAG_NAME = "name";
	private final String TAG_ADDRESS = "address1";
	private final String TAG_VILLAGE = "village";
	private final String TAG_POSTAL_CODE = "postalCode";
	private final String TAG_CONTACT_NO = "phone";

	private int mTag = -1;
	private String mValue = null;
	private PatientsListAdapter mAdapter = null;
	private List<Patient> mPatientsList = null;

	public PatientsRetriverTask(int tag, String value,
			PatientsListAdapter adapter) {
		this.mTag = tag;
		this.mValue = value;
		this.mAdapter = adapter;
		this.mPatientsList = FordDemoUtil.getInstance().getPatientsList();
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
	protected String doInBackground(String... params) {

		AppLog.enter(TAG, AppLog.getMethodName());

		String result = null;

		AppLog.info(TAG, "Tag : " + mTag);
		AppLog.info(TAG, "Value : " + mValue);

		String requestURL = null;
		switch (mTag) {
		case Constants.TAG_PATIENT_BY_NAME:
			requestURL = Constants.URL_PATIENT_BY_PATIENT_NAME + mValue;
			break;

		case Constants.TAG_PATIENT_BY_POSTAL_CODE:
			requestURL = Constants.URL_PATIENT_BY_POSTAL_CODE + mValue;
			break;
		case Constants.TAG_PATIENT_BY_VILLAGE:
			requestURL = Constants.URL_PATIENT_BY_VILLAGE + mValue;
			break;

		default:
			break;
		}

		AppLog.info(TAG, "Request URL : " + requestURL);

		String responseString = WebService.getInstance().request(requestURL);

		AppLog.info(TAG, "Response String : " + responseString);

		try {
			if (responseString.contains("object not found")
					|| responseString.equalsIgnoreCase("[]")) {
				result = Constants.RESULT_NO_RECORD;
			} else {

				if (null != mPatientsList) {
					mPatientsList.clear();
				}

				JSONArray jsonArray = new JSONArray(responseString);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					AppLog.info(TAG, "JSON Object : " + jsonObj);

					Patient patient = new Patient();

					if (Constants.TAG_PATIENT_BY_NAME == mTag) {
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
					mPatientsList.add(patient);
					result = Constants.RESULT_UPDATED;
				}
			}

		} catch (Exception e) {
			AppLog.error(TAG, "Error : " + e.toString());
			result = Constants.RESULT_ERROR;
			return result;
		}

		AppLog.info(TAG, "Patient List : " + mPatientsList);

		AppLog.exit(TAG, AppLog.getMethodName());
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onPostExecute(result);
		AppLog.info(TAG, "Patients Retriver Task result is : " + result);

		if (result.equalsIgnoreCase(Constants.RESULT_NO_RECORD)
				|| mPatientsList.size() <= 0) {
			mPatientsList.clear();
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
		
		mTag = -1;
		mValue = null;
		mAdapter = null;
		super.onCancelled();
		
		AppLog.exit(TAG, AppLog.getMethodName());
	}
	
}