package com.openxc.ford.mHealth.demo.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.FordDemoApp;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.activity.FordDemoActivity;
import com.openxc.ford.mHealth.demo.database.DatabaseAdapter;
import com.openxc.ford.mHealth.demo.web.WebService;

/**
 * OfflineThread used to send cache data , that is stored in local app's
 * database when app is running in offline mode
 */

public class OffLineThread extends Thread {
	private final String TAG = AppLog.getClassName();

	private FordDemoUtil fordDemoUtil = null;
	private JSONArray listJson;
	private Handler handler = null;
	private static OffLineThread sOffLineThread;
	final String RESPONSE_MSG = "responseMsg";

	public static OffLineThread getInstance() {
		if (null == sOffLineThread) {
			sOffLineThread = new OffLineThread();
		}

		return sOffLineThread;
	}

	private OffLineThread() {
		AppLog.enter(TAG, AppLog.getMethodName());

		fordDemoUtil = FordDemoUtil.getInstance();
		this.handler = FordDemoUtil.getInstance().getHandler();
		start();

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void run() {
		AppLog.enter(TAG, AppLog.getMethodName());

		while (FordDemoActivity.isUploadingRequired) {

			AppLog.info(TAG, "Executing While...");
			// Fetch the list of cache data from database
			listJson = DatabaseAdapter.getInstance().getDataJsonArrayByFlag(
					FordDemoApp.getApplication(), 1);

			// Condition to check the internet connectivity
			if (fordDemoUtil
					.isConnectedToInternet(FordDemoApp.getApplication())
					&& null != listJson && !(listJson.length() < 1)) {

				try {
					for (int i = 0; i < listJson.length(); i++) {

						String response = WebService.getInstance().request(
								Constants.URL_UPLOAD_PARAMETER,
								listJson.getJSONObject(i));
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.get(RESPONSE_MSG).equals("Y")) {
							DatabaseAdapter.getInstance().UpdateData(
									FordDemoApp.getApplication(),
									listJson.getJSONObject(i));

							Message msg = handler.obtainMessage();
							msg.what = Constants.TAG_UPDATE_COUNTER_CACHE;
							if (i < listJson.length() - 2) {
								msg.arg1 = listJson.length() - i;
							} else {
								msg.arg1 = 0;
							}
							AppLog.info(TAG, "Sending Message...");
							handler.sendMessage(msg);

						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				AppLog.info(TAG,
						"Connection is not avalable OR No Data is available to Upload");
			}
			// Check for connection after 10 seconds second.
			try {
				Thread.sleep(Constants.OFFLINE_INTERVAL);
			} catch (InterruptedException e) {
				AppLog.info(TAG, "Offline Thread intrupted, returning...");
				AppLog.error(TAG, "Error : " + e.toString());
				return;

			}
		}
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public boolean cancelOffLineThread() {
		AppLog.enter(TAG, AppLog.getMethodName());

		boolean bResult = false;
		if (isAlive()) {
			interrupt();
			bResult = true;
		}

		handler = null;
		sOffLineThread = null;

		AppLog.exit(TAG, AppLog.getMethodName());
		return bResult;
	}

}
