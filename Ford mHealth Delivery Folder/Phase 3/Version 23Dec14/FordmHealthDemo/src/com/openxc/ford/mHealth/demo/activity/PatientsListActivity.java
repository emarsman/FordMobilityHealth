package com.openxc.ford.mHealth.demo.activity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.adapter.PatientsListAdapter;
import com.openxc.ford.mHealth.demo.model.Patient;
import com.openxc.ford.mHealth.demo.tasks.PatientsRetriverTask;

public class PatientsListActivity extends Activity implements OnClickListener {
	private final String TAG = AppLog.getClassName();

	private final String BTN_MSG_OK = "OK";
	private final String BTN_MSG_CANCEL = "Cancel";
	private final String MSG_POSTAL_CODE = "Enter Postal Code";
	private final String MSG_VILLAGE = "Enter Village";
	private final String MSG_PATIENT_NAME = "Enter Patient Name";
	private final String MSG_NO_RECORD = "No record found.";

	private LinearLayout mLayoutTop = null;
	private ListView mLtVwPatient = null;
	private Button mBtnSearchbyPostalCode = null;
	private Button mBtnSearchbyPatientName = null;
	private Button mBtnSearchbyVillage = null;
	private TextView mTxVwInternetConnectionStatus = null;

	private PatientsListAdapter mPatientListAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.patients_list_activity);

		mLayoutTop = (LinearLayout) findViewById(R.id.layoutSearchbtn);
		mBtnSearchbyPostalCode = (Button) findViewById(R.id.btnSearchbypostalcode);
		mBtnSearchbyVillage = (Button) findViewById(R.id.btnSearchbyvillage);
		mBtnSearchbyPatientName = (Button) findViewById(R.id.btnSearchbyname);
		mLtVwPatient = (ListView) findViewById(R.id.listView_patient);
		mTxVwInternetConnectionStatus = (TextView) findViewById(R.id.tvInternetconnection);

		mBtnSearchbyPatientName.setOnClickListener(this);
		mBtnSearchbyPostalCode.setOnClickListener(this);
		mBtnSearchbyVillage.setOnClickListener(this);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onResume() {

		AppLog.enter(TAG, AppLog.getMethodName());
		super.onResume();

		if (FordDemoUtil.getInstance().isConnectedToInternet(this)) {
			mLayoutTop.setVisibility(View.VISIBLE);
			mTxVwInternetConnectionStatus.setVisibility(View.GONE);
			mLtVwPatient.setVisibility(View.VISIBLE);
		} else {
			mLayoutTop.setVisibility(View.INVISIBLE);
			mLtVwPatient.setVisibility(View.INVISIBLE);
			mTxVwInternetConnectionStatus.setVisibility(View.VISIBLE);
		}

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
		
		mBtnSearchbyPostalCode = null;
		mBtnSearchbyPatientName = null;
		mBtnSearchbyVillage = null;
		mTxVwInternetConnectionStatus = null;
		mLtVwPatient = null;
		mLayoutTop = null;
		
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSearchbypostalcode:
			showCustomDialog(Constants.TAG_PATIENT_BY_POSTAL_CODE,
					MSG_POSTAL_CODE);
			break;

		case R.id.btnSearchbyvillage:
			showCustomDialog(Constants.TAG_PATIENT_BY_VILLAGE, MSG_VILLAGE);
			break;

		case R.id.btnSearchbyname:
			showCustomDialog(Constants.TAG_PATIENT_BY_NAME, MSG_PATIENT_NAME);
			break;

		default:
			break;
		}
	}

	private void setAdapter(int tag, String value) {
		AppLog.enter(TAG, AppLog.getMethodName());

		PatientsRetriverTask task = new PatientsRetriverTask(tag, value);
		ArrayList<Patient> patientsList = null;
		try {
			patientsList = task.execute().get();
		} catch (InterruptedException e) {
			AppLog.error(TAG, "Exception : " + e.toString());
			Toast.makeText(getApplicationContext(), MSG_NO_RECORD,
					Toast.LENGTH_LONG).show();
			return;
		} catch (ExecutionException e) {
			AppLog.error(TAG, "Exception : " + e.toString());
			Toast.makeText(getApplicationContext(), MSG_NO_RECORD,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (null != patientsList) {

			mPatientListAdapter = new PatientsListAdapter(
					PatientsListActivity.this, patientsList);
			mLtVwPatient.setAdapter(mPatientListAdapter);

		} else {
			Toast.makeText(getApplicationContext(), MSG_NO_RECORD,
					Toast.LENGTH_LONG).show();
			mLtVwPatient.setAdapter(null);

		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private void showCustomDialog(final int type, String message) {
		AppLog.enter(TAG, AppLog.getMethodName());

		LayoutInflater inflater = LayoutInflater.from(this);
		View promptsView = inflater.inflate(R.layout.search_patient_dialog,
				null);
		TextView txVwTitle = (TextView) promptsView
				.findViewById(R.id.tx_search_criteria);
		final EditText edTxInput = (EditText) promptsView
				.findViewById(R.id.ed_SearchValue);

		if (type == Constants.TAG_PATIENT_BY_POSTAL_CODE) {
			edTxInput.setInputType(InputType.TYPE_CLASS_NUMBER);
			InputFilter[] inputFilter = new InputFilter[1];
			inputFilter[0] = new InputFilter.LengthFilter(6);
			edTxInput.setFilters(inputFilter);
		} else {
			edTxInput.setInputType(InputType.TYPE_CLASS_TEXT);

		}

		txVwTitle.setText(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(promptsView);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton(BTN_MSG_OK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								String searchCriteria = edTxInput.getText()
										.toString();
								if (null != searchCriteria) {
									searchCriteria=searchCriteria.replaceAll(" ", "%20");
									setAdapter(type, searchCriteria);
								}
							}
						})
				.setNegativeButton(BTN_MSG_CANCEL,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}
