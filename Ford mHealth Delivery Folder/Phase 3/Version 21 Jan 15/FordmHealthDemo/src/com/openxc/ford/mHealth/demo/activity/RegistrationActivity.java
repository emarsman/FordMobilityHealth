package com.openxc.ford.mHealth.demo.activity;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.VehiclePreferences;
import com.openxc.ford.mHealth.demo.web.WebService;

public class RegistrationActivity extends Activity implements OnClickListener {

	private final String TAG = AppLog.getClassName();

	private final String TAG_VEHICLE_ID = "vehicleId";
	private final String TAG_VEHICLE_REG_NO = "vehRegnNo";
	private final String TAG_VEHICLE_MAKE = "vehMake";
	private final String TAG_VEHICLE_MODEL = "vehModel";
	private final String TAG_VEHICLE_CHASIS_NO = "vehChasisNo";
	private final String TAG_OWNER_CONTACT_NO = "contactNo";
	private final String TAG_OWNER_EMAIL_ID = "emailId";
	private final String TAG_RESPONSE_MSG = "responseMsg";

	private final String MSG_REG_NO_CANT_BLANK = "Registration Number can't be left blank";
	private final String MSG_MODEL_CANT_BLANK = "Model can't be left blank";
	private final String MSG_MAKE_CANT_BLANK = "Make can't be left blank";
	private final String MSG_CHASIS_NO_CANT_BLANK = "Chasis Number can't be left blank";
	private final String MSG_CONTACT_NO_INVALID = "Contact Number is not valid";
	private final String MSG_EMAIL_ID_CANT_BLANK = "Owner Email can't be left blank";
	private final String MSG_EMAIL_ID_INVALID = "Email is not Valid";
	private final String MSG_NO_INTERNET_CONNECTION = "Not Connected to Internet.  Please check your internet connection";
	private final String MSG_CONNECTION_ERROR = "Could not connect to server.";
	private final String MSG_REGISTER = "Registering...";

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public final Pattern NO_SPECIAL_PATTERN = Pattern
			.compile("^[a-zA-Z0-9][-/]{4,20}$");

	private EditText mEdTxRegistratioNumbar = null;
	private EditText mEdTxMake = null;
	private EditText mEdTxModel = null;
	private EditText mEdTxChasisNumber = null;
	private EditText mEdTxContactNumber = null;
	private EditText mEdTxEmailId = null;
	private Button mBtnRegister = null;

	private VehiclePreferences mVehiclePreferences = null;
	private String mRegistrationNumbar = null;
	private FordDemoUtil mFordDemoUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_activity);

		mEdTxRegistratioNumbar = (EditText) findViewById(R.id.et_reg_no);
		mEdTxMake = (EditText) findViewById(R.id.et_make);
		mEdTxModel = (EditText) findViewById(R.id.et_model);
		mEdTxChasisNumber = (EditText) findViewById(R.id.et_chasis_number);
		mEdTxContactNumber = (EditText) findViewById(R.id.et_contact_number);
		mEdTxEmailId = (EditText) findViewById(R.id.et_owner_email);
		mBtnRegister = (Button) findViewById(R.id.btn_register);

		mBtnRegister.setOnClickListener(this);

		mVehiclePreferences = new VehiclePreferences(this);
		mFordDemoUtil = FordDemoUtil.getInstance();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onResume() {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onResume();
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onPause() {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onPause();
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onDestroy() {
		AppLog.enter(TAG, AppLog.getMethodName());
		deInitialize();
		super.onDestroy();
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private void deInitialize() {
		AppLog.enter(TAG, AppLog.getMethodName());

		mEdTxRegistratioNumbar = null;
		mEdTxMake = null;
		mEdTxModel = null;
		mEdTxChasisNumber = null;
		mEdTxContactNumber = null;
		mEdTxEmailId = null;
		mBtnRegister = null;

		mVehiclePreferences = null;
		mRegistrationNumbar = null;
		mFordDemoUtil = null;

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onClick(View arg0) {
		AppLog.enter(TAG, AppLog.getMethodName());

		JSONObject json = new JSONObject();

		mRegistrationNumbar = mEdTxRegistratioNumbar.getText().toString()
				.trim().replace(" ", "");
		try {
			json.put(TAG_VEHICLE_REG_NO, mRegistrationNumbar);
			json.put(TAG_VEHICLE_MAKE, mEdTxMake.getText().toString().trim());
			json.put(TAG_VEHICLE_MODEL, mEdTxModel.getText().toString().trim());

			json.put(TAG_OWNER_CONTACT_NO, mEdTxContactNumber.getText()
					.toString().trim());

			json.put(TAG_VEHICLE_CHASIS_NO, mEdTxChasisNumber.getText()
					.toString().trim());

			json.put(TAG_OWNER_EMAIL_ID, mEdTxEmailId.getText().toString()
					.trim());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (Validate(json)) {
			if (mFordDemoUtil.isConnectedToInternet(RegistrationActivity.this)) {
				new BackgroundProcess(json).execute();
			} else {
				mFordDemoUtil.showAlert(MSG_NO_INTERNET_CONNECTION,
						RegistrationActivity.this);
			}
		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	class BackgroundProcess extends AsyncTask<String, String, String> {

		String limit;
		String name;
		JSONObject json;

		BackgroundProcess(JSONObject json) {

			this.json = json;
		}

		ProgressDialog progressDialog = new ProgressDialog(
				RegistrationActivity.this);

		@Override
		protected void onPreExecute() {
			AppLog.enter(TAG, AppLog.getMethodName());

			super.onPreExecute();
			progressDialog.setMessage(MSG_REGISTER);
			progressDialog.show();

			AppLog.exit(TAG, AppLog.getMethodName());
		}

		@Override
		protected String doInBackground(String... uri) {
			AppLog.enter(TAG, AppLog.getMethodName());

			String responseString = WebService.getInstance().request(
					Constants.URL_REGISTRATION, json);

			AppLog.exit(TAG, AppLog.getMethodName());
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			AppLog.enter(TAG, AppLog.getMethodName());

			super.onPostExecute(result);

			try {
				if (result != null) {
					if (!result.equals("")) {

						JSONObject jsonId = null;

						jsonId = new JSONObject(result.toString());
						if (jsonId.get(TAG_RESPONSE_MSG).equals("Y")) {

							mVehiclePreferences.saveToken(jsonId
									.getString(TAG_VEHICLE_ID));
							mVehiclePreferences.saveName(mRegistrationNumbar);

							transitToFordDemoActivity();

							progressDialog.dismiss();

						}

						else if (jsonId.get(TAG_RESPONSE_MSG).equals("N")) {

							mFordDemoUtil.showAlert(MSG_CONNECTION_ERROR,
									RegistrationActivity.this);

						} else if (jsonId.get(TAG_RESPONSE_MSG).equals("E")) {

							mVehiclePreferences.saveToken(jsonId
									.getString(TAG_VEHICLE_ID));
							mVehiclePreferences.saveName(mRegistrationNumbar);

							transitToFordDemoActivity();

							progressDialog.dismiss();

						}
					} else {
						progressDialog.dismiss();
						mFordDemoUtil.showAlert(MSG_CONNECTION_ERROR,
								RegistrationActivity.this);

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			AppLog.exit(TAG, AppLog.getMethodName());
		}
	}

	private void transitToFordDemoActivity() {
		AppLog.enter(TAG, AppLog.getMethodName());

		Intent intent = new Intent(RegistrationActivity.this,
				FilePickerActivity.class);
		startActivity(intent);
		finish();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private boolean Validate(JSONObject json) {
		AppLog.enter(TAG, AppLog.getMethodName());

		try {
			if (mEdTxRegistratioNumbar.getText().toString().trim().length() < 1) {
				mFordDemoUtil.showAlert(MSG_REG_NO_CANT_BLANK, this);
				return false;
			}
			if (mEdTxModel.getText().toString().trim().length() < 1) {
				mFordDemoUtil.showAlert(MSG_MODEL_CANT_BLANK, this);
				return false;
			}
			if (mEdTxMake.getText().toString().trim().length() < 1) {
				mFordDemoUtil.showAlert(MSG_MAKE_CANT_BLANK, this);
				return false;
			}
			if (mEdTxChasisNumber.getText().toString().trim().length() < 1) {
				mFordDemoUtil.showAlert(MSG_CHASIS_NO_CANT_BLANK, this);
				return false;
			}
			if (mEdTxContactNumber.getText().toString().trim().length() < 7) {
				mFordDemoUtil.showAlert(MSG_CONTACT_NO_INVALID, this);
				return false;
			}
			if (!checkEmail(mEdTxEmailId.getText().toString().trim())) {
				mFordDemoUtil.showAlert(MSG_EMAIL_ID_INVALID, this);
				return false;
			}

			if (json.getString("ownerEmail").length() < 1) {
				mFordDemoUtil.showAlert(MSG_EMAIL_ID_CANT_BLANK, this);
				return false;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		AppLog.exit(TAG, AppLog.getMethodName());
		return true;
	}

	private boolean checkEmail(String email) {
		AppLog.enter(TAG, AppLog.getMethodName());

		AppLog.exit(TAG, AppLog.getMethodName());
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
}