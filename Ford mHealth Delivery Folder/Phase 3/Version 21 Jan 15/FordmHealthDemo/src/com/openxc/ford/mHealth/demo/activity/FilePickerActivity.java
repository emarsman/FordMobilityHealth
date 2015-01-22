package com.openxc.ford.mHealth.demo.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.FileUtils;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class FilePickerActivity extends Activity implements OnClickListener {
	private final String TAG = AppLog.getClassName();

	private final int FILE_PICKER_REQUEST_CODE = 11;

	private LinearLayout mLinearBrowsefile = null;
	private LinearLayout mLinearDefaultfile = null;
	private LinearLayout mLinearRealDataFeeds = null;

	public static final String TYPE_SELECTION = "TYPE_SELECTION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.filepicker_activity);
		mLinearBrowsefile = (LinearLayout) findViewById(R.id.ll_browse_file);
		mLinearDefaultfile = (LinearLayout) findViewById(R.id.ll_default_file);
		mLinearRealDataFeeds = (LinearLayout) findViewById(R.id.ll_real_data_feeds);
		mLinearBrowsefile.setOnClickListener(this);
		mLinearDefaultfile.setOnClickListener(this);
		mLinearRealDataFeeds.setOnClickListener(this);

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void onClick(View view) {
		AppLog.enter(TAG, AppLog.getMethodName());

		switch (view.getId()) {
		case R.id.ll_browse_file:
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("*/*");
			startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
			break;
		case R.id.ll_default_file:
			FordDemoUtil.getInstance().setDriveTraceFilePath(null);
			transitToFordDemoActivity(R.id.ll_default_file);

			break;

		case R.id.ll_real_data_feeds:

			FordDemoUtil.getInstance().setDriveTraceFilePath(null);
			transitToFordDemoActivity(R.id.ll_real_data_feeds);

			break;

		default:
			break;
		}
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onResume() {
		AppLog.enter(TAG, AppLog.getMethodName());
		super.onResume();
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent resultData) {

		AppLog.enter(TAG, AppLog.getMethodName());

		if (requestCode == FILE_PICKER_REQUEST_CODE) {

			if (resultData != null) {

				try {
					Uri returnUri = resultData.getData();
					final String filePath = FileUtils.getPath(
							FilePickerActivity.this, returnUri);

					if (filePath.contains(" ")) {
						Toast.makeText(
								FilePickerActivity.this,
								"Please select a file without white spaces in file name.",
								Toast.LENGTH_LONG).show();
					} else {
						String content = readFileContent(returnUri);
						if (isJSONValid(content)) {
							FordDemoUtil.getInstance().setDriveTraceFilePath(
									filePath);

							Toast.makeText(FilePickerActivity.this,
									"Drive trace file is loading...",
									Toast.LENGTH_LONG).show();

							transitToFordDemoActivity(R.id.ll_browse_file);
						} else {
							Toast.makeText(FilePickerActivity.this,
									"Seleted file is not valid.",
									Toast.LENGTH_LONG).show();
						}
					}

				} catch (Exception e) {

					AppLog.error(TAG, "Error : " + e.toString());
				}
			}
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

		mLinearBrowsefile = null;
		mLinearDefaultfile = null;

		AppLog.exit(TAG, AppLog.getMethodName());

	}

	private void transitToFordDemoActivity(int value) {
		AppLog.enter(TAG, AppLog.getMethodName());

		Intent intent = new Intent(FilePickerActivity.this,
				FordDemoActivity.class);
		intent.putExtra(TYPE_SELECTION, value);
		startActivity(intent);
		finish();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	private String readFileContent(Uri uri) throws IOException {
		AppLog.enter(TAG, AppLog.getMethodName());

		InputStream inputStream = getContentResolver().openInputStream(uri);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		String currentline = "";

		for (int i = 0; i < 100; i++) {
			if ((currentline = reader.readLine()) != null) {
				stringBuilder.append(currentline + "\n");
			} else {
				break;
			}
		}

		inputStream.close();

		AppLog.exit(TAG, AppLog.getMethodName());
		return stringBuilder.toString();
	}

	private boolean isJSONValid(String content) {
		AppLog.enter(TAG, AppLog.getMethodName());
		try {
			new JSONObject(content);
		} catch (JSONException ex) {
			AppLog.error(TAG, "Not a valid JSON, Error : " + ex.toString());
			return false;
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return true;
	}
}
