package com.openxc.ford.mHealth.demo.activity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.WindowManager;
import android.widget.TextView;


import com.openxc.VehicleManager;
import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoApp;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.VehiclePreferences;
import com.openxc.ford.mHealth.demo.database.DatabaseAdapter;
import com.openxc.ford.mHealth.demo.model.Vehicle;
import com.openxc.ford.mHealth.demo.tasks.OffLineThread;
import com.openxc.ford.mHealth.demo.tasks.UploadThread;
import com.openxc.ford.mHealth.demo.web.WebService;
import com.openxc.interfaces.bluetooth.BluetoothVehicleInterface;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.FuelConsumed;
import com.openxc.measurements.FuelLevel;
import com.openxc.measurements.HeadlampStatus;
import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.Latitude;
import com.openxc.measurements.Longitude;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.Odometer;
import com.openxc.measurements.UnrecognizedMeasurementTypeException;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.measurements.WindshieldWiperStatus;
import com.openxc.remote.VehicleServiceException;
import com.openxc.sources.DataSourceException;
import com.openxc.sources.trace.TraceVehicleDataSource;

public class DashboardActivity extends FragmentActivity {

	private final String TAG = AppLog.getClassName();

	public final int BLUETOOTH_DELAY = 2000;
	public final int LOCATION_DELAY = 10000;
	public final int LOCATION_INTERVAL = 30000;

	private int TYPE_SELECTION = 0;
	private VehicleManager mVehicleManager = null;
	private VehiclePreferences mVehiclePreferences = null;
	private Vehicle mVehicle = null;
	private LocationManager mLocationManager = null;
	private boolean isRealDataFeeds = false;

	private TextView mTxVwOdometer = null;
	private TextView mTxVwFuelLevel = null;
	private TextView mTxVwFuelConsumed = null;
	private TextView mTxVwRegistration = null;
	private TextView mTxVwVehicleSpeed = null;
	private TextView mTxVwTime = null;
	private TextView mTxVwEngineSpeed = null;
	private TextView mTxVwLocation = null;
	private TextView mTxVwLongitude = null;
	private TextView mTxVwLatitude = null;
	private TextView mTxVwUploadCounter = null;
	private TextView mTxVwOffLineCounter = null;
	private TextView mTxVwCloudConnect = null;
	private TextView mTxVwIgnitionStatus = null;
	private TextView mTxVwHeadLampStatus = null;
	private TextView mTxVwWiperStatus = null;

	private final Handler mOpenXCHandler = new Handler();
	private OffLineCacheTextHandler mOffLineCacheTextHandler = new OffLineCacheTextHandler();

	// Listeners to handle the parameters provided by OpenXC //
	VehicleSpeed.Listener mSpeedListener = new VehicleSpeed.Listener() {

		@Override
		public void receive(final Measurement measurement) {
			final VehicleSpeed speed = (VehicleSpeed) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					double vehicleSpeed = speed.getValue().doubleValue();
					mVehicle.setSpeed(vehicleSpeed + "");

					mVehicle.setTimeStamp((measurement.getBirthtime() / 1000)
							+ "");
					String vehSpeed = null;
					try {
						vehSpeed = speed.toString().substring(0, 2)
								.replace(".", "");
					} catch (Exception e) {
						return;
					}

					if (null != mTxVwVehicleSpeed) {
						mTxVwVehicleSpeed.setText("" + vehSpeed + " Km/hr");
					} else {
						return;
					}

					AppLog.info(TAG, " Vehicle Speed : " + vehSpeed);
				}
			});
		}
	};

	EngineSpeed.Listener mEngineSpeed = new EngineSpeed.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final EngineSpeed speed = (EngineSpeed) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					double engineSpeed = speed.getValue().doubleValue();
					if (null != mTxVwEngineSpeed) {
						mTxVwEngineSpeed.setText("" + engineSpeed + " rpm");
					} else {
						return;
					}

					AppLog.info(TAG, " Engine Speed : " + engineSpeed);
				}
			});
		}
	};

	IgnitionStatus.Listener mIgnitionStatus = new IgnitionStatus.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final IgnitionStatus status = (IgnitionStatus) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					String ignitionStatus = status.getValue().toString();
					mVehicle.setIgnitionStatus(ignitionStatus);
					if (null != mTxVwIgnitionStatus) {
						mTxVwIgnitionStatus.setText("" + ignitionStatus);
					} else {
						return;
					}
				}
			});
		}
	};

	WindshieldWiperStatus.Listener mWiperStatus = new WindshieldWiperStatus.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final WindshieldWiperStatus status = (WindshieldWiperStatus) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					String wiperStatus = status.getValue().toString();
					mVehicle.setViperStatus(wiperStatus);
					if (null != mTxVwWiperStatus) {
						mTxVwWiperStatus.setText("" + wiperStatus);
					} else {
						return;
					}
				}
			});
		}
	};

	HeadlampStatus.Listener mHeadLampStatus = new HeadlampStatus.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final HeadlampStatus status = (HeadlampStatus) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					String headLampStatus = status.getValue().toString();
					mVehicle.setHeadLampStatus(headLampStatus);
					if (null != mTxVwHeadLampStatus) {
						mTxVwHeadLampStatus.setText("" + headLampStatus);
					} else {
						return;
					}
				}
			});
		}
	};

	Latitude.Listener mLatitudeListener = new Latitude.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final Latitude latitude = (Latitude) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					if (null != latitude) {

						if (!isRealDataFeeds) {
							double latValue = latitude.getValue().doubleValue();
							mVehicle.setLatitude(latValue + "");
						}
						String lat = null;
						try {
							lat = latitude.toString();
							if (lat.length() <= 10) {
								AppLog.info(TAG,
										"Lat length is less than or equal to ten : "
												+ lat.length());
								lat = lat.substring(0, lat.length() - 1);
							} else {
								AppLog.info(TAG,
										"Lat length is grater than ten : "
												+ lat.length());
								lat = lat.substring(0, 10);
							}
						} catch (Exception e) {
							return;
						}

						if (null != mTxVwLatitude && !isRealDataFeeds) {
							mTxVwLatitude.setText("" + lat);
						} else {
							return;
						}
					}
				}
			});
		}
	};

	Longitude.Listener mLongitudeListener = new Longitude.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final Longitude longitude = (Longitude) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					if (!isRealDataFeeds) {
						double longValue = longitude.getValue().doubleValue();
						mVehicle.setLongitude(longValue + "");
					}
					String longi = null;
					try {
						longi = longitude.toString();
						if (longi.length() <= 10) {
							longi = longi.substring(0, longi.length() - 1);
						} else {
							longi = longi.substring(0, 10);
						}
					} catch (Exception e) {
						return;
					}

					if (null != mTxVwLongitude && !isRealDataFeeds) {
						mTxVwLongitude.setText("" + longi);
					} else {
						return;
					}
				}
			});
		}
	};

	LocationListener mAndroidLocationListener = new LocationListener() {
		public void onLocationChanged(final Location location) {
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					if (isRealDataFeeds) {
						String latitude = "" + location.getLatitude();
						String longitude = "" + location.getLongitude();
						mVehicle.setLatitude(latitude);
						mVehicle.setLongitude(longitude);

						try {
							if (latitude.length() <= 10) {
								latitude = latitude.substring(0,
										latitude.length() - 1);
							} else {
								latitude = latitude.substring(0, 10);
							}
						} catch (Exception e) {
							return;
						}

						try {
							if (longitude.length() <= 10) {
								longitude = longitude.substring(0,
										longitude.length() - 1);
							} else {
								longitude = longitude.substring(0, 10);
							}
						} catch (Exception e) {
							return;
						}

						mTxVwLatitude.setText(latitude);
						mTxVwLongitude.setText(longitude);
					}
				}
			});
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	FuelConsumed.Listener mFuelConsumedListener = new FuelConsumed.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final FuelConsumed fuelConsumed = (FuelConsumed) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					double fuelConsumedValue = fuelConsumed.getValue()
							.doubleValue();
					mVehicle.setFuelConsumedSinceRestart(fuelConsumedValue + "");
					String fuelStr = null;
					try {
						fuelStr = fuelConsumed.toString();
						if (fuelStr.length() <= 4) {
							fuelStr = fuelStr.substring(0, fuelStr.length() - 1);
						} else {
							fuelStr = fuelStr.substring(0, 4);
						}

					} catch (Exception e) {
						return;
					}
					if (null != mTxVwFuelConsumed) {
						mTxVwFuelConsumed.setText(fuelStr + " Lt");
					} else {
						return;
					}
				}
			});
		}
	};

	FuelLevel.Listener mFuelLevelListener = new FuelLevel.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final FuelLevel fuelLevel = (FuelLevel) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					double fuelValue = fuelLevel.getValue().doubleValue();
					mVehicle.setFuelLevel(fuelValue + "");
					String fuelStr = null;
					try {
						fuelStr = fuelLevel.toString();
						if (fuelStr.length() <= 4) {
							fuelStr = fuelStr.substring(0, fuelStr.length() - 1);
						} else {
							fuelStr = fuelStr.substring(0, 4);
						}
					} catch (Exception e) {
						return;
					}

					if (null != mTxVwFuelLevel) {
						mTxVwFuelLevel.setText(fuelStr + " %");
					} else {
						return;
					}
				}
			});
		}
	};

	Odometer.Listener mOdometerListener = new Odometer.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final Odometer odometer = (Odometer) measurement;
			mOpenXCHandler.post(new Runnable() {
				public void run() {
					String odo = null;
					try {
						odo = odometer.toString();
						if (odo.length() <= 4) {
							odo = odo.substring(0, odo.length() - 1);
						} else {
							odo = odo.substring(0, 4);
						}
					} catch (Exception e) {
						return;
					}

					mVehicle.setOdometer(odo);
					if (null != mTxVwOdometer) {
						mTxVwOdometer.setText(odo + " Km");
					} else {
						return;
					}
				}
			});
		}
	};

	// Service to add Trace file and add the listeners to the service

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			AppLog.enter(TAG, AppLog.getMethodName());

			mVehicleManager = ((VehicleManager.VehicleBinder) service)
					.getService();
			try {

				try {
					String driveTraceFile = null;

					if (TYPE_SELECTION == R.id.ll_browse_file) {

						driveTraceFile = FordDemoUtil.getInstance()
								.getDriveTraceFilePath();

						AppLog.info(TAG, "User Select drive Trace File : "
								+ driveTraceFile);

						mVehicleManager.addSource(new TraceVehicleDataSource(
								DashboardActivity.this.getApplicationContext(),
								new URI(driveTraceFile)));
					}

					else if (TYPE_SELECTION == R.id.ll_default_file) {

						driveTraceFile = "resource://" + R.raw.fortis_to_aiims;

						AppLog.info(TAG,
								"User Select default drive Trace File : "
										+ driveTraceFile);

						mVehicleManager.addSource(new TraceVehicleDataSource(
								DashboardActivity.this.getApplicationContext(),
								new URI(driveTraceFile)));

					} else if (TYPE_SELECTION == R.id.ll_real_data_feeds) {
						isRealDataFeeds = true;
						AppLog.info(TAG, "User Select real data feeds.");

						mVehicleManager.addVehicleInterface(
								BluetoothVehicleInterface.class, null);

						mVehicleManager.setBluetoothPollingStatus(true);
					}

				} catch (DataSourceException e) {
					AppLog.error(TAG,
							"Couldn't add Source to Trace Vehicle : ", e);
				} catch (URISyntaxException e) {
					AppLog.error(TAG, "URI Syntax is no correct : ", e);
				}

				mVehicleManager.addListener(VehicleSpeed.class, mSpeedListener);
				mVehicleManager.addListener(EngineSpeed.class, mEngineSpeed);
				mVehicleManager.addListener(IgnitionStatus.class,
						mIgnitionStatus);
				mVehicleManager.addListener(HeadlampStatus.class,
						mHeadLampStatus);
				mVehicleManager.addListener(WindshieldWiperStatus.class,
						mWiperStatus);
				mVehicleManager.addListener(Odometer.class, mOdometerListener);
				mVehicleManager.addListener(FuelConsumed.class,
						mFuelConsumedListener);
				mVehicleManager
						.addListener(FuelLevel.class, mFuelLevelListener);
				mVehicleManager.addListener(Latitude.class, mLatitudeListener);
				mVehicleManager
						.addListener(Longitude.class, mLongitudeListener);

			} catch (VehicleServiceException e) {
				AppLog.error(TAG, "Couldn't add listeners for measurements", e);
			} catch (UnrecognizedMeasurementTypeException e) {
				AppLog.error(TAG, "Couldn't add listeners for measurements", e);
			}

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					AppLog.enter(TAG, AppLog.getMethodName());

					if (null != mVehicleManager && isRealDataFeeds) {

						mVehicleManager.addVehicleInterface(
								BluetoothVehicleInterface.class, null);
						AppLog.info(TAG, "Vehicle Manager is not null and Real Data Feed is selected");
						
					} else {
						AppLog.info(TAG, "Vehicle Manager is null OR Real Data Feed is not selected.");
						
					}

					AppLog.exit(TAG, AppLog.getMethodName());
				}
			}, 2000);

			AppLog.exit(TAG, AppLog.getMethodName());
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			AppLog.enter(TAG, AppLog.getMethodName());

			AppLog.info(TAG, "VehicleService disconnected unexpectedly");
			mVehicleManager = null;

			AppLog.exit(TAG, AppLog.getMethodName());
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_activity);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		startService(new Intent(this, VehicleManager.class));

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		TYPE_SELECTION = getParent().getIntent().getExtras()
				.getInt(FilePickerActivity.TYPE_SELECTION);

		if (TYPE_SELECTION == R.id.ll_real_data_feeds) {
			isRealDataFeeds = true;
		}

		mTxVwWiperStatus = (TextView) findViewById(R.id.tv_wiper_status);
		mTxVwHeadLampStatus = (TextView) findViewById(R.id.tv_head_lamp_status);
		mTxVwIgnitionStatus = (TextView) findViewById(R.id.tv_ignition_status);

		mTxVwVehicleSpeed = (TextView) findViewById(R.id.tv_vehicle_speed);
		mTxVwEngineSpeed = (TextView) findViewById(R.id.tv_engine_speed);
		mTxVwLocation = (TextView) findViewById(R.id.tv_location);
		mTxVwLongitude = (TextView) findViewById(R.id.tv_longitude);
		mTxVwLatitude = (TextView) findViewById(R.id.tv_latitude);
		mTxVwTime = (TextView) findViewById(R.id.tv_time);
		mTxVwRegistration = (TextView) findViewById(R.id.tv_registration);
		mTxVwFuelConsumed = (TextView) findViewById(R.id.tv_fuel_consumed);
		mTxVwFuelLevel = (TextView) findViewById(R.id.tv_fuel_level);
		mTxVwOdometer = (TextView) findViewById(R.id.tv_odometer);
		mTxVwUploadCounter = (TextView) findViewById(R.id.tv_upload_counter);
		mTxVwOffLineCounter = (TextView) findViewById(R.id.tv_offline_counter);

		Constants.DATABASE_LENGTH = DatabaseAdapter.getInstance()
				.getDataBaseLength(FordDemoApp.getApplication());
		Constants.OFFLINE_COUNTER = DatabaseAdapter.getInstance()
				.getDataBaseLength(FordDemoApp.getApplication(), 1);

		mTxVwOffLineCounter.setText("Data Uploaded to cache "
				+ Constants.OFFLINE_COUNTER + " Times");

		mVehicle = new Vehicle();
		mVehiclePreferences = new VehiclePreferences(this);

		mVehicle.setVehicleId(mVehiclePreferences.getToken());

		mTxVwRegistration.setText("Your Registration Id is "
				+ mVehiclePreferences.getToken());
		mTxVwCloudConnect = (TextView) findViewById(R.id.tv_cloud_connect);
		setTimeOnUI();

		// Web Service call to get location and show the current location
		// Timer call for every 30 seconds to update the location.

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Timer mTime = new Timer();
				mTime.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						if (null != mVehiclePreferences) {

							JSONObject jsonReg = new JSONObject();
							try {
								jsonReg.put("vehicleId",
										mVehiclePreferences.getToken());
							} catch (JSONException e) {
								e.printStackTrace();
							}
							new LocationBackgroundProcess(mVehiclePreferences)
									.execute();
						}
					}
				}, 0, LOCATION_INTERVAL);
			}
		}, LOCATION_DELAY);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onResumeFragments() {
		AppLog.enter(TAG, AppLog.getMethodName());

		AppLog.info(TAG, "Setting.... Vehicle : ");
		FordDemoUtil.getInstance().setVehicle(mVehicle);

		AppLog.info(TAG, "Binding Service.... Vehicle Manager ");
		getParent().bindService(
				new Intent(getApplicationContext(), VehicleManager.class),
				mConnection, Context.BIND_AUTO_CREATE);

		if (null != mVehicleManager && isRealDataFeeds) {
			mVehicleManager.addVehicleInterface(
					BluetoothVehicleInterface.class, null);
		}

		AppLog.info(TAG, "Setting Service Connection on Util.... ");
		FordDemoUtil.getInstance().setServiceConnection(mConnection);

		try {
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0,
					mAndroidLocationListener);
		} catch (IllegalArgumentException e) {
			AppLog.error(TAG, "Vehicle location provider is unavailable");
		}

		FordDemoUtil.getInstance().setHandler(mOffLineCacheTextHandler);

		// Initialization of uploading thread
		UploadThread.getInstance();

		// Initialization of offline thread
		OffLineThread.getInstance();

		AppLog.exit(TAG, AppLog.getMethodName());
		super.onResumeFragments();
	}

	@Override
	public void onPause() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onPause();
		mLocationManager.removeUpdates(mAndroidLocationListener);
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

		mTxVwOdometer = null;
		mTxVwFuelLevel = null;
		mTxVwFuelConsumed = null;
		mTxVwRegistration = null;
		mTxVwVehicleSpeed = null;
		mTxVwTime = null;
		mTxVwEngineSpeed = null;
		mTxVwHeadLampStatus = null;
		mTxVwWiperStatus = null;
		mTxVwLocation = null;
		mTxVwLongitude = null;
		mTxVwLatitude = null;
		mTxVwUploadCounter = null;
		mTxVwOffLineCounter = null;
		mTxVwCloudConnect = null;

		mVehiclePreferences = null;

		isRealDataFeeds = false;

		if (null != mOffLineCacheTextHandler) {
			mOffLineCacheTextHandler.removeCallbacksAndMessages(null);
			mOffLineCacheTextHandler = null;
		}

		FordDemoUtil.getInstance().setHandler(null);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	// Set Current time on UI//
	// Timer calls for every 2 seconds to show the exact current time//

	private void setTimeOnUI() {
		AppLog.enter(TAG, AppLog.getMethodName());

		Timer mTime = new Timer();
		mTime.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (null != mTxVwTime) {
							mTxVwTime.setText(DateFormat.format(
									"E  d  MMM    hh:mm A",
									System.currentTimeMillis()));
						}
					}
				});

			}
		}, 0, 2000);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	class LocationBackgroundProcess extends AsyncTask<String, String, String> {

		String limit;
		String name;
		JSONObject json;
		VehiclePreferences sp;

		LocationBackgroundProcess(VehiclePreferences sp) {

			this.sp = sp;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... uri) {

			String responseString = WebService.getInstance().requestGet(
					Constants.URL_LOCATION + "" + sp.getToken());

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != result && null != mTxVwLocation) {

				if (!result.equals("")) {

					Vehicle params = FordDemoUtil.getInstance().getVehicle();

					if (params.getLatitude().equals("")
							|| params.getLongitude().equals("")) {

						mTxVwLocation.setText("Not Available");
					} else {
						JSONObject jsonLocation;
						try {
							jsonLocation = new JSONObject(result);
							mTxVwLocation.setText(jsonLocation
									.getString("address"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {
					mTxVwLocation.setText("Not Available");
				}
			}
		}
	}

	/**
	 * Handler to update the UI Text views from the Non Ui threads Data Coming
	 * from Upload Thread and offline Thread updates in this handler
	 * 
	 */
	public class OffLineCacheTextHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			AppLog.enter(TAG, AppLog.getMethodName());
			if (null != mTxVwOffLineCounter && null != mTxVwCloudConnect
					&& null != mTxVwUploadCounter) {
				if (msg.what == Constants.TAG_UPDATE_COUNTER_CACHE) {

					mTxVwCloudConnect.setText("Connected to Cloud Portal");

					mTxVwCloudConnect.setTextColor(Color.parseColor("#ffffff"));
					if (msg.arg1 == 0) {
						mTxVwOffLineCounter.setText(" Cache is empty now");
						Constants.OFFLINE_COUNTER = 0;
					} else {
						mTxVwOffLineCounter.setText(" Cache has " + msg.arg1
								+ " data packets remaining");
					}
				}
				if (msg.what == Constants.TAG_UPDATE_COUNTER_DATABASE) {
					mTxVwOffLineCounter.setText("Data Uploaded to cache "
							+ msg.arg1 + " Times");
					mTxVwCloudConnect.setTextColor(Color.parseColor("#ff2222"));
					mTxVwCloudConnect.setText("Not Connected to Cloud.");
				}
				if (msg.what == Constants.TAG_UPDATE_COUNTER) {

					mTxVwUploadCounter.setText("Data Uploaded " + msg.arg1
							+ " Times");

					mTxVwCloudConnect.setText("Connected to Cloud Portal");
					mTxVwCloudConnect.setTextColor(Color.parseColor("#ffffff"));
				}
				if (msg.what == Constants.TAG_BLANK) {

					mTxVwLocation.setText("Not Available");
					if (FordDemoUtil.getInstance().isConnectedToInternet(
							FordDemoApp.getApplication())) {
						mTxVwUploadCounter.setText("No Vehicle Data Available");

						mTxVwCloudConnect.setText("Connected to Cloud Portal");
						mTxVwCloudConnect.setTextColor(Color
								.parseColor("#ffffff"));
					} else {
						mTxVwOffLineCounter
								.setText("No Vehicle Data Available");
						mTxVwCloudConnect.setTextColor(Color
								.parseColor("#ff2222"));
						mTxVwCloudConnect.setText("Not Connected to Cloud.");
					}
				}
			} else {
				AppLog.info(TAG, "Views are null, returning...");
				return;
			}

			AppLog.exit(TAG, AppLog.getMethodName());
			super.handleMessage(msg);
		}
	};

	@Override
	public void onBackPressed() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onBackPressed();
		DashboardActivity.this.finish();

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}