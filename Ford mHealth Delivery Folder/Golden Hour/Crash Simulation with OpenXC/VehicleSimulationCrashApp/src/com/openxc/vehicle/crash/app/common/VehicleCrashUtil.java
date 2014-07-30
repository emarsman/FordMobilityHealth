package com.openxc.vehicle.crash.app.common;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;

public class VehicleCrashUtil {

	private static VehicleCrashUtil mVehicleCrashUtil = null;
	private OnVehicleCrashedListener mOnVehicleCrashedListener = null;
	private double mPreviousVehicleSpeed = 0;
	private double mPreviousEngineSpeed = 0;
	Context ctx;
	private String provider = "";
	TextView mTxtViewLocation;

	private VehicleCrashUtil() {
	}

	public static VehicleCrashUtil getInstance() {
		if (null == mVehicleCrashUtil) {
			mVehicleCrashUtil = new VehicleCrashUtil();
		}

		return mVehicleCrashUtil;
	}

	public void setOnVehicleCrashedListener(OnVehicleCrashedListener listener) {
		mOnVehicleCrashedListener = listener;
	}

	public double getPreviousVehicleSpeed() {
		return mPreviousVehicleSpeed;
	}

	public void setPreviousVehicleSpeed(double previousVehicleSpeed) {
		this.mPreviousVehicleSpeed = previousVehicleSpeed;
	}

	public double getPreviousEngineSpeed() {
		return mPreviousEngineSpeed;
	}

	public void setPreviousEngineSpeed(double previousEngineSpeed) {
		this.mPreviousEngineSpeed = previousEngineSpeed;
	}

	public boolean checkVehicleCrash(double speed) {
		boolean mCrashStatus = false;
		if (mPreviousEngineSpeed > 1000 && speed <= 0.0
				|| mPreviousVehicleSpeed > 60 && speed <= 0.0) {
			mCrashStatus = true;
			if (null != mOnVehicleCrashedListener) {
				mOnVehicleCrashedListener.onVehicleCrashed();
			}
		}

		return mCrashStatus;
	}

	public String getLocProvider(Context ctx){
		this.ctx=ctx;
		LocationManager locationManager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
			
		
		for (int _i = 0; _i < 10; _i++) {
			provider = locationManager.getBestProvider(criteria, false);

			if (provider != "")
				break;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		// return a blank string if provider is not found.
		if (provider == "") {
			Log.i("LOC", "Provider not found.");
			
		}
		return provider;
		
		
	}
	
	

	public void  getCurrentAddress(Location location) {
		String address = null;

		Log.i("LOC", "current Location is : " + location);

		if (location != null) {
			// address = " at ";
			Geocoder geoCoder = new Geocoder(ctx, Locale.getDefault());
			try {
				List<Address> addresses = geoCoder.getFromLocation(
						location.getLatitude(), location.getLongitude(), 1);

				if (addresses.size() > 0) {
					for (int i = 0; i < addresses.get(0)
							.getMaxAddressLineIndex(); i++)
						address += addresses.get(0).getAddressLine(i) + "\n";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.i("LOC", "current Location address is : " + address);
			

		} else {
			address="HCL Technologies Ltd. Plot No 1& 2,\n Sector-125, Maple Towers \n Noida. UP 201301 \n";
			
			Log.i("LOC", "current Known Location address is : " + address);
			
		}
		
		mTxtViewLocation.setText(address);
	}

	public void setLocationText(TextView mCrashLocation) {
		// TODO Auto-generated method stub
		this.mTxtViewLocation=mCrashLocation;
	}

}
