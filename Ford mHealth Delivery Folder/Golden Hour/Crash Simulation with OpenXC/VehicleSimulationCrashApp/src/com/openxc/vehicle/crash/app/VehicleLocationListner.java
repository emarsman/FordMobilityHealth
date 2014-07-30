package com.openxc.vehicle.crash.app;

import com.openxc.vehicle.crash.app.common.VehicleCrashUtil;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class VehicleLocationListner implements LocationListener {

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.v("Onlocation changed", "+++++++");
		VehicleCrashUtil.getInstance().getCurrentAddress(location);
		
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
