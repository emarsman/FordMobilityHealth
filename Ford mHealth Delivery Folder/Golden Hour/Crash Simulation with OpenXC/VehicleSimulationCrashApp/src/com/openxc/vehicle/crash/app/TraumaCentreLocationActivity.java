package com.openxc.vehicle.crash.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.openxc.vehicle.crash.app.common.Constants;

public class TraumaCentreLocationActivity extends Activity {

	private TextView txtVwTraumaCentreName = null;
	private Button btnHome = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trauma_centre_location);

		btnHome = (Button) findViewById(R.id.btn_home);

		btnHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				setResult(Constants.HOME_RESULT_CODE);
				finish();
			}
		});

		txtVwTraumaCentreName = (TextView) findViewById(R.id.trauma_centre_name);

		Intent intent = getIntent();
		String selectedTraumaCenter = intent
				.getStringExtra(Constants.TRAUMA_CARE_CENTRE);
		txtVwTraumaCentreName.setText("Trauma Centre Location : "
				+ selectedTraumaCenter);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
