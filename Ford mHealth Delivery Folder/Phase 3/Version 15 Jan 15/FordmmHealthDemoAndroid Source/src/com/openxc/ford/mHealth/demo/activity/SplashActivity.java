package com.openxc.ford.mHealth.demo.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.VehiclePreferences;
import com.openxc.ford.mHealth.demo.database.DatabaseHelper;

public class SplashActivity extends Activity {
	private final String TAG = AppLog.getClassName();

	private int SPLASH_TIME_OUT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		try {
			new DatabaseHelper(this).createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		transitToNextActivity();
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
		super.onDestroy();
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public void transitToNextActivity() {
		AppLog.enter(TAG, AppLog.getMethodName());

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				VehiclePreferences storePref = new VehiclePreferences(
						SplashActivity.this);
				Intent intent = null;
				if (storePref.getIpAddress() != null) {
					if (storePref.getToken() != null) {
						if (!storePref.getToken().equals("")) {
							intent = new Intent(SplashActivity.this,
									FilePickerActivity.class);
						}

					} else {
						intent = new Intent(SplashActivity.this,
								RegistrationActivity.class);
					}

				} else {
					intent = new Intent(SplashActivity.this,
							IPAddressActivity.class);
				}
				startActivity(intent);
				finish();
			}
		}, SPLASH_TIME_OUT);

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}