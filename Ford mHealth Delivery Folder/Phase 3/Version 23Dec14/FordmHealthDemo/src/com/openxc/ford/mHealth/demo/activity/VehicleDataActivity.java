package com.openxc.ford.mHealth.demo.activity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.adapter.VehicleDataAdapter;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.tasks.VehicleDataRetriverTask;

public class VehicleDataActivity extends Activity {
	private final String TAG = AppLog.getClassName();

	private TextView mTxVwConnectionStatus = null;
	private View mVwListHeader = null;
	private ListView mLtVwVehicleData = null;
	private ArrayList<Vehicle> mVehicleDataList = null;
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
		Vehicle currentVehicle = FordDemoUtil.getInstance().getVehicle();
		AppLog.info(TAG, "vehicle id :" + currentVehicle.getId());
		if (FordDemoUtil.getInstance().isConnectedToInternet(this)) {
			mTxVwConnectionStatus.setVisibility(View.GONE);
			mVwListHeader.setVisibility(View.VISIBLE);
			mLtVwVehicleData.setVisibility(View.VISIBLE);
			retriveVehicleData();

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

	private void retriveVehicleData() {
		AppLog.enter(TAG, AppLog.getMethodName());

		VehicleDataRetriverTask task = new VehicleDataRetriverTask();

		try {
			mVehicleDataList = task.execute().get();

			if (null != mVehicleDataList) {
				AppLog.info(TAG, "Vehicle Data List is not null.");
				mVehicleDataAdapter = new VehicleDataAdapter(this,
						mVehicleDataList);
				mLtVwVehicleData.setAdapter(mVehicleDataAdapter);
			} else {
				Toast.makeText(getApplicationContext(), "No record found.",
						Toast.LENGTH_LONG).show();
				AppLog.info(TAG, "Vehicle Data List is null.");
				return;
			}

		} catch (InterruptedException e) {
			AppLog.error(TAG, "Error : " + e.toString());
		} catch (ExecutionException e) {
			AppLog.error(TAG, "Error : " + e.toString());
		}
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private void deInitialize() {
		AppLog.enter(TAG, AppLog.getMethodName());

		mTxVwConnectionStatus = null;
		mLtVwVehicleData = null;
		mVwListHeader = null;

		if (null != mVehicleDataList) {
			mVehicleDataList.clear();
			mVehicleDataList = null;
		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}