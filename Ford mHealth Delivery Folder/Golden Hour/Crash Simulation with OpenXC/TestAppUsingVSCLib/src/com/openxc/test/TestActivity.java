package com.openxc.test;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.openxc.vehicle.crash.VehicleCrashNotificationService;
import com.openxc.vehicle.crash.common.AppLog;
import com.openxc.vehicle.crash.common.OnVehicleCrashedListener;
import com.openxc.vehicle.crash.common.VehicleCrashUtil;

public class TestActivity extends Activity implements OnVehicleCrashedListener, android.view.View.OnClickListener  {

	private final String TAG = AppLog.getClassName();
	private TextView mVehicleCrashStatusView;
	private Button btnRestart;
	private final String VEHICLE_NOT_CRASHED_MSG = "Vehicle is running safely !!!";
	private final String VEHICLE_CRASHED_MSG = "Vehicle Crashed !!!";
	boolean b1=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		mVehicleCrashStatusView = (TextView) findViewById(R.id.vehicle_crash);
		mVehicleCrashStatusView.setText(VEHICLE_NOT_CRASHED_MSG);
btnRestart=(Button)findViewById(R.id.btnRestart);
btnRestart.setOnClickListener(this);
btnRestart.setVisibility(View.INVISIBLE);
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onResume() {
			
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onResume();
		btnRestart.setVisibility(View.INVISIBLE);
		mVehicleCrashStatusView.setText(VEHICLE_NOT_CRASHED_MSG );

		// Registering OnVehicleCrashedListener
		// To get Notification when Vehicle Crashed
		VehicleCrashUtil.getInstance().setOnVehicleCrashedListener(this);

		// Providing reference of Drive Trace File
		String sourceFile = "resource://" + R.raw.carcrash;
		VehicleCrashUtil.getInstance().setSourceFile(this, sourceFile);

		AppLog.info(TAG, "Starting vehicle crash service");
		startService(new Intent(this, VehicleCrashNotificationService.class));
		
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onPause() {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onPause();
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onDestroy() {
		AppLog.enter(TAG, AppLog.getMethodName());
		stopService(new Intent(this, VehicleCrashNotificationService.class));
		super.onDestroy();
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onVehicleCrashed() {
		AppLog.enter(TAG, AppLog.getMethodName());

		AppLog.info(TAG, VEHICLE_CRASHED_MSG);
		mVehicleCrashStatusView.setText(VEHICLE_CRASHED_MSG);
		btnRestart.setVisibility(View.VISIBLE);
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onClick(View view) {
	
		if (R.id.btnRestart==view.getId()) {
			stopService(new Intent(this, VehicleCrashNotificationService.class));
			
			onResume();
		}
		
		
	}

	
	
}
