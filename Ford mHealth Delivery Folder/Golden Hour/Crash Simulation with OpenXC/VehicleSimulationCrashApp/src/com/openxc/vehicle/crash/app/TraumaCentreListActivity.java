package com.openxc.vehicle.crash.app;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.openxc.vehicle.crash.app.common.Constants;
import com.openxc.vehicle.crash.app.common.VehicleCrashUtil;

public class TraumaCentreListActivity extends Activity {

	private TextView mTxtVwVehicleLocation = null;
	private ListView lstVwTraumaCenterList = null;
	private Button btnHome = null;
	
	private int HOME_REQUEST_CODE=10;
	protected LocationManager mLocManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trauma_centre_list);

		btnHome = (Button) findViewById(R.id.btn_home);

		btnHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		mTxtVwVehicleLocation = (TextView) findViewById(R.id.vehicle_location);
		
		VehicleCrashUtil.getInstance().setLocationText(mTxtVwVehicleLocation);
		mLocManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		String provider=VehicleCrashUtil.getInstance().getLocProvider(this);
		VehicleCrashUtil.getInstance().getCurrentAddress(mLocManager.getLastKnownLocation(provider));
		/*String location = VehicleCrashUtil.getInstance().fetchLocation(this);
		if (location.equals("")) {
			location = "HCL Technologies Ltd. Plot No 1& 2, Sector-125, Maple Towers  Noida. UP 201301";
		}

		mTxtVwVehicleLocation.setText("Vehicle Current Location is : "
				+ location);
*/
		lstVwTraumaCenterList = (ListView) findViewById(R.id.list_trauma_centre);

		ListAdapter adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, R.id.item, Constants.TRAUMA_CARE_CENTRES);
		lstVwTraumaCenterList.setAdapter(adapter);
		lstVwTraumaCenterList.setOnItemClickListener(listener);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if(Constants.HOME_RESULT_CODE == resultCode){
			finish();
		}
		
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View item, int position,
				long id) {
			Intent intent = new Intent(TraumaCentreListActivity.this,
					TraumaCentreLocationActivity.class);
			intent.putExtra(Constants.TRAUMA_CARE_CENTRE,
					Constants.TRAUMA_CARE_CENTRES[position]);
			startActivityForResult(intent, HOME_REQUEST_CODE);
		}
	};
	
}
