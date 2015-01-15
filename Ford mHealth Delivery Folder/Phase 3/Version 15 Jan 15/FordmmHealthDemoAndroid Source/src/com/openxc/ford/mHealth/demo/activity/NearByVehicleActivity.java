package com.openxc.ford.mHealth.demo.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.tasks.NearByVehiclesRetriverTask;

public class NearByVehicleActivity extends FragmentActivity {
	private final String TAG = AppLog.getClassName();

	private GoogleMap mGoogleMap = null;
	private SupportMapFragment mMapFragment = null;
	private Marker mCurrentVehicleMarker = null;
	private NearByVehiclesRetriverTask mNearByVehiclesRetriverTask = null;

	private Projection mProjection = null;
	private Point mMapedPoint = null;
	private Display mDisplay = null;
	private Point mDisplayPoint = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		mDisplay = getWindowManager().getDefaultDisplay();
		mDisplayPoint = new Point();
		mDisplay.getSize(mDisplayPoint);
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

		if (null != mNearByVehiclesRetriverTask) {
			mNearByVehiclesRetriverTask.cancel(true);
			mNearByVehiclesRetriverTask = null;
		}

		mCurrentVehicleMarker = null;
		mMapFragment = null;
		mGoogleMap = null;
		mProjection = null;
		mMapedPoint = null;
		mDisplay = null;
		mDisplayPoint = null;

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

	private void setUpMap() {
		AppLog.enter(TAG, AppLog.getMethodName());

		Vehicle currentVehicle = FordDemoUtil.getInstance().getVehicle();

		double srclat = 0.0;
		double srclng = 0.0;

		try {
			srclat = Double.parseDouble(currentVehicle.getLatitude());
			srclng = Double.parseDouble(currentVehicle.getLongitude());
		} catch (NumberFormatException ex) {
			AppLog.error(TAG, "Error : " + ex.toString());
			return;

		}

		LatLng origin = new LatLng(srclat, srclng);

		mCurrentVehicleMarker = mGoogleMap.addMarker(new MarkerOptions()
				.position(origin).icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.current_vehicle)));

		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 15));
		moveCarLocation();
		mNearByVehiclesRetriverTask = new NearByVehiclesRetriverTask(this);
		mNearByVehiclesRetriverTask.execute();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public void drawMarker(Vehicle vehicle) {
		AppLog.enter(TAG, AppLog.getMethodName());

		double vehicleLat = Double.parseDouble(vehicle.getLatitude());
		double vehicleLong = Double.parseDouble(vehicle.getLongitude());
		LatLng position = new LatLng(vehicleLat, vehicleLong);

		MarkerOptions vehicleMarker = new MarkerOptions();
		vehicleMarker.position(position);
		if (null != mGoogleMap) {
			mGoogleMap.addMarker(vehicleMarker).setIcon(
					BitmapDescriptorFactory
							.fromResource(R.drawable.map_car_icon));
		}
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public void moveCarLocation() {
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
						if (null != mCurrentVehicleMarker) {
							mCurrentVehicleMarker.setPosition(latlong);
							AppLog.info(TAG, "Marker's Position : "
									+ mCurrentVehicleMarker.getPosition());

							mProjection = mGoogleMap.getProjection();
							mMapedPoint = mProjection.toScreenLocation(latlong);

							if (mMapedPoint.x <= 50
									|| mMapedPoint.x >= (mDisplayPoint.x - 50)
									|| mMapedPoint.y <= 50
									|| mMapedPoint.y >= (mDisplayPoint.y - 50)) {
								AppLog.info(TAG, "screen postion ++ : "
										+ mMapedPoint.x + " and "
										+ mMapedPoint.y);
								mGoogleMap.animateCamera(
										CameraUpdateFactory.newLatLng(latlong),
										200, null);
							}

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
