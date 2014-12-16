package com.openxc.ford.mHealth.demo.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;

@SuppressWarnings("deprecation")
public class FordDemoActivity extends TabActivity {

	private final String TAG = AppLog.getClassName();

	private final String STR_DASHBOARD = "Dashboard";
	private final String STR_NEAR_BY_PATIENT = "NearBy mHealth Patients";
	private final String STR_NEAR_BY_VEHICLE = "mHealth vehicles Location";
	private final String STR_PATIENTS_LIST = "Notify Patients";

	private TabHost tabHost = null;
	private TabSpec dashboardTabSpec = null;
	private TabSpec nearByPatientTabSpec = null;
	private TabSpec nearByVehicleTabSpec = null;
	private TabSpec patientsListTabSpec = null;
	private Intent dashboardIntent = null;
	private Intent nearByPatientIntent = null;
	private Intent nearByVehicleIntent = null;
	private Intent patientsListIntent = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);

		setContentView(R.layout.ford_demo_activity);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		tabHost = getTabHost();

		dashboardTabSpec = tabHost.newTabSpec(STR_DASHBOARD);
		dashboardTabSpec.setIndicator(STR_DASHBOARD, getResources()
				.getDrawable(R.drawable.ic_launcher));
		dashboardIntent = new Intent(this, DashboardActivity.class);
		dashboardTabSpec.setContent(dashboardIntent);

		nearByPatientTabSpec = tabHost.newTabSpec(STR_NEAR_BY_PATIENT);
		nearByPatientTabSpec.setIndicator(STR_NEAR_BY_PATIENT, getResources()
				.getDrawable(R.drawable.ic_launcher));
		nearByPatientIntent = new Intent(this, NearByPatientsActivity.class);
		nearByPatientTabSpec.setContent(nearByPatientIntent);

		nearByVehicleTabSpec = tabHost.newTabSpec(STR_NEAR_BY_VEHICLE);
		nearByVehicleTabSpec.setIndicator(STR_NEAR_BY_VEHICLE, getResources()
				.getDrawable(R.drawable.ic_launcher));
		nearByVehicleIntent = new Intent(this, NearByVehicleActivity.class);
		nearByVehicleTabSpec.setContent(nearByVehicleIntent);

		patientsListTabSpec = tabHost.newTabSpec(STR_PATIENTS_LIST);
		patientsListTabSpec.setIndicator(STR_PATIENTS_LIST, getResources()
				.getDrawable(R.drawable.ic_launcher));
		patientsListIntent = new Intent(this, PatientsListActivity.class);

		patientsListTabSpec.setContent(patientsListIntent);

		tabHost.setBackgroundColor(Color.LTGRAY);
		tabHost.addTab(dashboardTabSpec);
		tabHost.addTab(nearByPatientTabSpec);
		tabHost.addTab(nearByVehicleTabSpec);
		tabHost.addTab(patientsListTabSpec);

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

		AppLog.info(TAG, "Unbinding from vehicle service");
		ServiceConnection serviceConnection = FordDemoUtil.getInstance()
				.getServiceConnection();
		if (null != serviceConnection) {
			AppLog.info(TAG,
					"Service Connection is not null Vehicle Service Unbinding...");

			try {
				unbindService(serviceConnection);
			} catch (Exception e) {
				AppLog.error(TAG, "Error : " + e.toString());
			} finally {
				FordDemoUtil.getInstance().setServiceConnection(null);
			}

		} else {
			AppLog.info(TAG, "Service Connection is null.");
		}

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

		dashboardIntent = null;
		nearByPatientIntent = null;
		nearByVehicleIntent = null;
		patientsListIntent = null;
		dashboardTabSpec = null;
		nearByPatientTabSpec = null;
		nearByVehicleTabSpec = null;
		patientsListTabSpec = null;
		tabHost = null;

		AppLog.exit(TAG, AppLog.getMethodName());

	}
}