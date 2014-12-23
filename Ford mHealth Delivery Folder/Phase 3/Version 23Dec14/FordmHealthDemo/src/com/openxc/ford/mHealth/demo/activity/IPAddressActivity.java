package com.openxc.ford.mHealth.demo.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.VehiclePreferences;

public class IPAddressActivity extends Activity implements OnClickListener {
	private final String TAG = AppLog.getClassName();

	private final Pattern IP_ADDRESS_PATTERN = Pattern
			.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
					+ "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
					+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
					+ "|[1-9][0-9]|[0-9]))");

	private EditText mEdTxIpAddress = null;
	private Button mBtnSubmit = null;
	private EditText mEdTxPort = null;

	private VehiclePreferences mVehiclePreferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_ip);

		mEdTxIpAddress = (EditText) findViewById(R.id.et_ip);
		mEdTxPort = (EditText) findViewById(R.id.et_port);
		mBtnSubmit = (Button) findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(this);

		mVehiclePreferences = new VehiclePreferences(this);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

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

		mEdTxIpAddress = null;
		mBtnSubmit = null;
		mEdTxPort = null;

		AppLog.exit(TAG, AppLog.getMethodName());

	}

	@Override
	public void onClick(View arg0) {
		AppLog.enter(TAG, AppLog.getMethodName());

		String ipAddress = mEdTxIpAddress.getText().toString().trim();
		String port = mEdTxPort.getText().toString().trim();
		Matcher matcher = IP_ADDRESS_PATTERN.matcher(ipAddress);

		if (matcher.matches()) {
			Intent intent = new Intent(IPAddressActivity.this,
					RegistrationActivity.class);

			mVehiclePreferences.saveIpAddress(ipAddress);
			mVehiclePreferences.savePort(port);
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(IPAddressActivity.this,
					"Please enter valid IP Address", Toast.LENGTH_LONG).show();
		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}
