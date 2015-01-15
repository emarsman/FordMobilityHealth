package com.openxc.ford.mHealth.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.R;

public class AddDeletePatientActivity extends Activity implements
		OnClickListener {

	private final String TAG = AppLog.getClassName();

	private Button mBtnAddDelete = null;
	private Intent intent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.adddeletepatient_activity);
		mBtnAddDelete = (Button) findViewById(R.id.btnAdddeletePatient);
		mBtnAddDelete.setOnClickListener(this);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onResume() {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onResume();
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

		mBtnAddDelete = null;
		super.onDestroy();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onClick(View view) {
		AppLog.enter(TAG, AppLog.getMethodName());

		intent = new Intent(Intent.ACTION_VIEW, Constants.URI_OPEN_MRS);
		startActivity(intent);

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}
