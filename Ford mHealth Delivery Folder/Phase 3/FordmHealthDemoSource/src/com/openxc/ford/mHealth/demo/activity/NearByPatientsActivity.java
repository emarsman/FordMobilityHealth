package com.openxc.ford.mHealth.demo.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.model.Patient;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.tasks.NearByPatientsRetriverTask;

public class NearByPatientsActivity extends FragmentActivity {

	private final String TAG = AppLog.getClassName();

	private GoogleMap mGoogleMap = null;
	private SupportMapFragment mMapFragment = null;
	private Marker currentVehicleMarker = null;
	private NearByPatientsRetriverTask mNearByPatientsRetriverTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onCreate(savedInstanceState);

		mMapFragment = new SupportMapFragment() {
			@Override
			public void onActivityCreated(Bundle savedInstanceState) {
				super.onActivityCreated(savedInstanceState);
				mGoogleMap = mMapFragment.getMap();
				if (mGoogleMap != null) {
					setUpMap();
				}
			}
		};

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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		try {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.remove(mMapFragment);
			fragmentTransaction.commitAllowingStateLoss();

		} catch (Exception e) {
			AppLog.error(TAG, "Error : " + e.toString());
		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onResumeFragments() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onResumeFragments();

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.remove(mMapFragment);
		fragmentTransaction.add(android.R.id.content, mMapFragment);
		fragmentTransaction.commitAllowingStateLoss();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private void deInitialize() {
		AppLog.enter(TAG, AppLog.getMethodName());

		currentVehicleMarker = null;
		mMapFragment = null;
		mGoogleMap = null;

		AppLog.exit(TAG, AppLog.getMethodName());

	}

	private void setUpMap() {
		AppLog.enter(TAG, AppLog.getMethodName());
		Vehicle currentVehicle = FordDemoUtil.getInstance().getVehicle();

		double srclat = Double.parseDouble(currentVehicle.getLatitude());
		double srclng = Double.parseDouble(currentVehicle.getLongitude());
		LatLng origin = new LatLng(srclat, srclng);

		currentVehicleMarker = mGoogleMap.addMarker(new MarkerOptions()
				.position(origin).icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.current_vehicle)));

		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 15));
		moveCarLocation();

		mNearByPatientsRetriverTask = new NearByPatientsRetriverTask(this);
		mNearByPatientsRetriverTask.execute();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public void drawMarker(Patient patient) {
		AppLog.enter(TAG, AppLog.getMethodName());

		double patientLat = Double.parseDouble(patient.getLatitude());
		double patientLong = Double.parseDouble(patient.getLongitude());
		LatLng position = new LatLng(patientLat, patientLong);
		if (null != mGoogleMap) {
			mGoogleMap.addMarker(new MarkerOptions().position(position).icon(
					BitmapDescriptorFactory
							.fromResource(R.drawable.map_patient_icon)));
		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private void moveCarLocation() {
		AppLog.enter(TAG, AppLog.getMethodName());

		Timer mTime = new Timer();
		mTime.scheduleAtFixedRate(new TimerTask() {

			double latitude = 0.0;
			double longitude = 0.0;
			LatLng latlong = null;
			Vehicle currentVehicle = null;

			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {

						AppLog.enter(TAG, AppLog.getMethodName());
						currentVehicle = FordDemoUtil.getInstance()
								.getVehicle();
						latitude = Double.parseDouble(currentVehicle
								.getLatitude());
						longitude = Double.parseDouble(currentVehicle
								.getLongitude());
						latlong = new LatLng(latitude, longitude);
						AppLog.info(TAG, "" + latlong);
						if (null != currentVehicleMarker) {
							currentVehicleMarker.setPosition(latlong);
							AppLog.info(TAG, "Marker's Position : "
									+ currentVehicleMarker.getPosition());
						} else {
							cancel();
							return;
						}
						AppLog.exit(TAG, AppLog.getMethodName());
					}
				});

			}
		}, 0, 100);
		AppLog.exit(TAG, AppLog.getMethodName());
	}
}