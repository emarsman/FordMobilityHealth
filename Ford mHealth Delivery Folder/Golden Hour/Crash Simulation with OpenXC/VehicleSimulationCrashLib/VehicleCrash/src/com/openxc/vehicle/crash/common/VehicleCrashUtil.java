package com.openxc.vehicle.crash.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.openxc.sources.DataSourceException;
import com.openxc.sources.trace.TraceVehicleDataSource;
import com.openxc.vehicle.crash.R;

public class VehicleCrashUtil {
	private final String TAG = AppLog.getClassName();
	private static VehicleCrashUtil mVehicleCrashUtil = null;
	private OnVehicleCrashedListener mOnVehicleCrashedListener = null;
	private double mPreviousVehicleSpeed = 0;
	private double mPreviousEngineSpeed = 0;
	private String mSourceFile = null;
	private TraceVehicleDataSource mSource = null;
	private String provider = "";
	private Context mContext = null;

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

	public String getSourceFile() {
		return mSourceFile;
	}

	public void setSourceFile(Context context, String sourceFile) {
		this.mContext = context;
		this.mSourceFile = sourceFile;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public TraceVehicleDataSource getSource() {

		try {

			if (null == mSource) {
				AppLog.info(TAG,
						"Trace Vehicle Data Source is null, Seting Using defined Drive Trace file.");
				if (null != mContext && null != mSourceFile) {
					AppLog.info(TAG, "Drive Trace file is defined by Client.");
					mSource = new TraceVehicleDataSource(mContext, new URI(
							getSourceFile()));
				} else {
					AppLog.info(TAG,
							"Client is not defining any Drive Trace file. Using defalut drive trace.");
					mSource = new TraceVehicleDataSource(mContext, new URI(
							"resource://" + R.raw.driving));
				}
			} else {
				AppLog.info(TAG,
						"Trace Vehicle Data Source is not null. Already set by Client App.");
			}

		} catch (DataSourceException e) {
			AppLog.error(TAG, "Couldn't add Source to Trace Vehicle : ", e);
		} catch (URISyntaxException e) {
			AppLog.error(TAG, "URI Syntax is no correct : ", e);
		}
		return mSource;
	}

	public void setSource(TraceVehicleDataSource source) {
		this.mSource = source;
	}

	public boolean checkVehicleCrash(double speed) {
		AppLog.enter(TAG, AppLog.getMethodName());

		boolean mCrashStatus = false;
		if (mPreviousEngineSpeed > 1000 && speed <= 0.0
				|| mPreviousVehicleSpeed > 60 && speed <= 0.0) {

			AppLog.info(TAG, "Car Crashed !!!");

			mCrashStatus = true;
			if (null != mOnVehicleCrashedListener) {
				mOnVehicleCrashedListener.onVehicleCrashed();
			}
		}

		AppLog.exit(TAG, AppLog.getMethodName());
		return mCrashStatus;
	}

	public String fetchLocation(Context ctx) {
		LocationManager locationManager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		String address = "";

		// get the provider.
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
			return address;
		}

		Location location = locationManager.getLastKnownLocation(provider);

		Log.i("LOC", "Last Known Location is : " + location);

		if (location != null) {
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
			Log.i("LOC", "Last Known Location address is : " + address);
			return address;

		} else {
			Log.i("LOC", "Last Known Location address is : " + address);
			return address;
		}

	}

}
