package com.openxc.vehicle.crash.app;

import android.util.Log;

import com.openxc.vehicle.crash.app.common.OnVehicleCrashedListener;

public class TestVehicleCrashedListener implements OnVehicleCrashedListener {

	private static final String TAG = "VehicleCrashedListener";
	
	@Override
	public void onVehicleCrashed() {
		Log.i(TAG, "Car Crashed !!!");
		System.out.println("Car Crashed !!!");
	}

}
