package com.openxc.ford.mHealth.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.adapter.VehicleDataAdapter;
import com.openxc.ford.mHealth.demo.tasks.VehicleDataRetriverTask;

public class VehicleDataActivity extends Activity {
	private final String TAG = AppLog.getClassName();

	private TextView mTxVwConnectionStatus = null;
	private View mVwListHeader = null;
	private ListView mLtVwVehicleData = null;
	private VehicleDataRetriverTask mVehicleDataRetriverTask = null;
	private VehicleDataAdapter mVehicleDataAdapter = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_data_layout);
		mVwListHeader = (View) findViewById(R.id.list_header);
		mTxVwConnectionStatus = (TextView) findViewById(R.id.tvInternetconnection);
		mLtVwVehicleData = (ListView) findViewById(R.id.ltVw_UploadedData);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onResume() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onResume();
		if (FordDemoUtil.getInstance().isConnectedToInternet(this)) {
			mTxVwConnectionStatus.setVisibility(View.GONE);
			mVwListHeader.setVisibility(View.VISIBLE);
			mLtVwVehicleData.setVisibility(View.VISIBLE);
			retriveVehicleRoute();

		} else {
			mLtVwVehicleData.setVisibility(View.INVISIBLE);
			mVwListHeader.setVisibility(View.INVISIBLE);
			mTxVwConnectionStatus.setVisibility(View.VISIBLE);
		}
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

	private void retriveVehicleRoute() {
		AppLog.enter(TAG, AppLog.getMethodName());

		mVehicleDataAdapter = new VehicleDataAdapter();
		mLtVwVehicleData.setAdapter(mVehicleDataAdapter);

		mVehicleDataRetriverTask = new VehicleDataRetriverTask(
				mVehicleDataAdapter);
		mVehicleDataRetriverTask.execute();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private void deInitialize() {
		AppLog.enter(TAG, AppLog.getMethodName());

		if(null != mVehicleDataRetriverTask){
			mVehicleDataRetriverTask.cancel(true);
			mVehicleDataRetriverTask = null;
		}
			
		mTxVwConnectionStatus = null;
		mLtVwVehicleData = null;
		mVwListHeader = null;
		mVehicleDataAdapter = null;

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}