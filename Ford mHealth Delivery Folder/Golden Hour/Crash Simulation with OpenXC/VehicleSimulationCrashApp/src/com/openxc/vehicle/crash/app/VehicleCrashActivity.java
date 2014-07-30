package com.openxc.vehicle.crash.app;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.openxc.VehicleManager;
import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.BrakePedalStatus;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.ParkingBrakeStatus;
import com.openxc.measurements.UnrecognizedMeasurementTypeException;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.remote.VehicleServiceException;
import com.openxc.sources.DataSourceException;
import com.openxc.sources.trace.TraceVehicleDataSource;
import com.openxc.vehicle.crash.app.common.VehicleCrashUtil;

public class VehicleCrashActivity extends Activity implements OnClickListener {

	private static String TAG = "VehicleDashboard";
	Animation animBling;
	private VehicleManager mVehicleManager;
	private boolean mIsBound;
	private final Handler mHandler = new Handler();
	private TextView mVehicleSpeedView;
	private TextView mVehicleBrakeStatusView;
	private TextView mParkingBrakeStatusView;
	private TextView mVehicleEngineSpeedView;
	private TextView mAcceleratorPedalPositionView;
	private TextView mIgnitionStatusView;
	private TextView mCrashStatusView,mCrashLocation;
	private Button mBtnPlay;
	private Button mBtnTraumaCenter;
	private Button mBtnHealthDetails;
	private boolean mCrashDetected = false;
	private Intent intent = null;
	private VehicleLocationListner mVehicleLocListner;
	private VehicleCrashUtil mVehicleCrashUtil = null; 
	
	protected LocationManager mLocationManager;
	VehicleSpeed.Listener mSpeedListener = new VehicleSpeed.Listener() {
		public void receive(Measurement measurement) {
			if (mCrashDetected == true) {
				return;
			}
			final VehicleSpeed speed = (VehicleSpeed) measurement;
			mHandler.post(new Runnable() {
				public void run() {
					double vehicleSpeed = speed.getValue().doubleValue();
					mVehicleSpeedView.setText("" + vehicleSpeed);
					if (VehicleCrashUtil.getInstance().checkVehicleCrash(
							vehicleSpeed)) {
						updateViewWhenVehicleCrashDetected();
						stopVehicleService();
						mCrashDetected = true;
					} else {
						VehicleCrashUtil.getInstance().setPreviousVehicleSpeed(
								vehicleSpeed);
					}
				}
			});
		}
	};

	BrakePedalStatus.Listener mBrakePedalStatus = new BrakePedalStatus.Listener() {
		public void receive(Measurement measurement) {
			final BrakePedalStatus status = (BrakePedalStatus) measurement;
			mHandler.post(new Runnable() {
				public void run() {
					mVehicleBrakeStatusView.setText(""
							+ status.getValue().booleanValue());
				}
			});
		}
	};

	ParkingBrakeStatus.Listener mParkingBrakeStatus = new ParkingBrakeStatus.Listener() {
		public void receive(Measurement measurement) {
			final ParkingBrakeStatus status = (ParkingBrakeStatus) measurement;
			mHandler.post(new Runnable() {
				public void run() {
					mParkingBrakeStatusView.setText(""
							+ status.getValue().booleanValue());
				}
			});
		}
	};

	EngineSpeed.Listener mEngineSpeed = new EngineSpeed.Listener() {
		public void receive(Measurement measurement) {

			if (mCrashDetected == true) {
				return;
			}

			final EngineSpeed speed = (EngineSpeed) measurement;
			mHandler.post(new Runnable() {
				public void run() {
					double engineSpeed = speed.getValue().doubleValue();
					mVehicleEngineSpeedView.setText("" + engineSpeed);
					if (VehicleCrashUtil.getInstance().checkVehicleCrash(
							engineSpeed)) {
						updateViewWhenVehicleCrashDetected();
						stopVehicleService();
						mCrashDetected = true;
					} else {
						VehicleCrashUtil.getInstance().setPreviousEngineSpeed(
								engineSpeed);
					}
				}
			});
		}
	};

	AcceleratorPedalPosition.Listener mAcceleratorPedalPosition = new AcceleratorPedalPosition.Listener() {
		public void receive(Measurement measurement) {
			final AcceleratorPedalPosition status = (AcceleratorPedalPosition) measurement;
			mHandler.post(new Runnable() {
				public void run() {
					mAcceleratorPedalPositionView.setText(""
							+ status.getValue().doubleValue());
				}
			});
		}
	};

	IgnitionStatus.Listener mIgnitionStatus = new IgnitionStatus.Listener() {
		public void receive(Measurement measurement) {
			final IgnitionStatus status = (IgnitionStatus) measurement;
			mHandler.post(new Runnable() {
				public void run() {
					mIgnitionStatusView.setText(""
							+ status.getValue().enumValue());
				}
			});
		}
	};

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.i(TAG, "Bound to VehicleManager");
			mVehicleManager = ((VehicleManager.VehicleBinder) service)
					.getService();

			try {

				try {
					mVehicleManager.addSource(new TraceVehicleDataSource(
							VehicleCrashActivity.this.getApplicationContext(),
							new URI("resource://" + R.raw.driving)));
				} catch (DataSourceException e) {
					Log.w(TAG, "Couldn't add Source to Trace Vehicle : ", e);
				} catch (URISyntaxException e) {
					Log.w(TAG, "URI Syntax is no correct : ", e);
				}

				mVehicleManager.addListener(VehicleSpeed.class, mSpeedListener);
				mVehicleManager.addListener(BrakePedalStatus.class,
						mBrakePedalStatus);
				mVehicleManager.addListener(ParkingBrakeStatus.class,
						mParkingBrakeStatus);
				mVehicleManager.addListener(EngineSpeed.class, mEngineSpeed);
				mVehicleManager.addListener(AcceleratorPedalPosition.class,
						mAcceleratorPedalPosition);
				mVehicleManager.addListener(IgnitionStatus.class,
						mIgnitionStatus);
			} catch (VehicleServiceException e) {
				Log.w(TAG, "Couldn't add listeners for measurements", e);
			} catch (UnrecognizedMeasurementTypeException e) {
				Log.w(TAG, "Couldn't add listeners for measurements", e);
			}
			mIsBound = true;
		}

		public void onServiceDisconnected(ComponentName className) {
			Log.w(TAG, "VehicleService disconnected unexpectedly");
			mVehicleManager = null;
			mIsBound = false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_crash);
		Log.i(TAG, "Vehicle dashboard created");
		mCrashLocation=(TextView)findViewById(R.id.txtcrash_location);
		mVehicleSpeedView = (TextView) findViewById(R.id.vehicle_speed);
		mVehicleBrakeStatusView = (TextView) findViewById(R.id.brake_pedal_status);
		mParkingBrakeStatusView = (TextView) findViewById(R.id.parking_brake_status);
		mVehicleEngineSpeedView = (TextView) findViewById(R.id.engine_speed);
		mAcceleratorPedalPositionView = (TextView) findViewById(R.id.accelerator_pedal_position);
		mIgnitionStatusView = (TextView) findViewById(R.id.ignition);
		mCrashStatusView = (TextView) findViewById(R.id.crash_status);
		mBtnPlay = (Button) findViewById(R.id.play);
		mBtnTraumaCenter = (Button) findViewById(R.id.trauma_centre_location);
		mBtnHealthDetails = (Button) findViewById(R.id.health_details);
		mBtnPlay.setOnClickListener(this);
		mBtnTraumaCenter.setOnClickListener(this);
		mBtnHealthDetails.setOnClickListener(this);
		
		getUserLocation();
		
				
	}

	private void getUserLocation() {
		// TODO Auto-generated method stub
		mVehicleCrashUtil = VehicleCrashUtil.getInstance(); 
		mVehicleCrashUtil.setLocationText(mCrashLocation);
		
		mLocationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		mVehicleLocListner = new VehicleLocationListner();
		String provider= mVehicleCrashUtil.getLocProvider(this);
		
		mLocationManager.requestLocationUpdates(
				provider,
				1000,
				1,mVehicleLocListner );
		mVehicleCrashUtil.getCurrentAddress(mLocationManager.getLastKnownLocation(provider));

		
	}

	@Override
	public void onResume() {
		super.onResume();
		
		// Instantiating reference of OnVehicleCrashedListener
		TestVehicleCrashedListener listener = new TestVehicleCrashedListener();

		// Registering OnVehicleCrashedListener
		// To get Notification when Vehicle Crashed
		VehicleCrashUtil.getInstance().setOnVehicleCrashedListener(listener);

		mCrashDetected = false;
		bindService(new Intent(this, VehicleManager.class), mConnection,
				Context.BIND_AUTO_CREATE);
		if (null != mCrashStatusView) {
			mCrashStatusView.setTextColor(Color.GREEN);
			mCrashStatusView.setText("Drive Safely.");
		}
		mBtnHealthDetails.setBackgroundResource(R.drawable.health_detail);
		mBtnTraumaCenter.setVisibility(View.INVISIBLE);
		mBtnHealthDetails.setVisibility(View.INVISIBLE);
	
		
	}

	@Override
	public void onPause() {
		super.onPause();
		mCrashDetected = false;
		if (mIsBound) {
			Log.i(TAG, "Unbinding from vehicle service");
			unbindService(mConnection);
			mIsBound = false;
		}
		mLocationManager.removeUpdates(mVehicleLocListner);
	}

	@Override
	protected void onDestroy() {

		mVehicleSpeedView = null;
		mVehicleBrakeStatusView = null;
		mParkingBrakeStatusView = null;
		mVehicleEngineSpeedView = null;
		mAcceleratorPedalPositionView = null;
		mIgnitionStatusView = null;
		mCrashStatusView = null;
		mBtnPlay = null;
		mBtnTraumaCenter = null;
		mBtnHealthDetails = null;
		mCrashLocation=null;
		super.onDestroy();
	}

	private void updateViewWhenVehicleCrashDetected() {
		
		animBling = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.blink);
		mCrashStatusView.setText("Car Crashed !!! ");
		mCrashStatusView.setTextColor(Color.RED);
		mCrashStatusView.setAnimation(animBling);
		mBtnTraumaCenter.setVisibility(View.VISIBLE);
		mBtnHealthDetails.setVisibility(View.VISIBLE);
		
		
	}

	private void stopVehicleService() {
		if (mIsBound) {
			Log.i(TAG, "Unbinding from vehicle service");
			unbindService(mConnection);
			mIsBound = false;
		}
	}

	@Override
	public void onClick(View view) {
		int viewId = view.getId();
		if (R.id.play == viewId) {
			stopVehicleService();
			bindService(new Intent(this, VehicleManager.class), mConnection,
					Context.BIND_AUTO_CREATE);
			mCrashDetected = false;
			if (null != mCrashStatusView) {
				mCrashStatusView.setTextColor(Color.GREEN);
				mCrashStatusView.setText("Drive Safely.");
				mBtnTraumaCenter.setVisibility(View.INVISIBLE);
				mBtnHealthDetails.setVisibility(View.INVISIBLE);
			}
		} else if ((R.id.trauma_centre_location == viewId)) {
			stopVehicleService();
			intent = new Intent(this, TraumaCentreListActivity.class);
			startActivity(intent);
		} else if ((R.id.health_details == viewId)) {
			mBtnHealthDetails.setBackgroundResource(R.drawable.health_detail1);
			
			stopVehicleService();

			intent = new Intent(this, DisplayPatientInfo.class);
			startActivity(intent);
		} else {
		}

	}
}
