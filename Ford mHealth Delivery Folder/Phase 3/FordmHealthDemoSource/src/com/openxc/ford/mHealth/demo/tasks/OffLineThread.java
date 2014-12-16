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
import com.openxc.ford.mHealth.demo.database.DatabaseAdapter;
import com.openxc.ford.mHealth.demo.web.WebService;

public class OffLineThread extends Thread {
	private final String TAG = AppLog.getClassName();

	private FordDemoUtil fordDemoUtil = null;
	private JSONArray listJson;
	Handler handler;
	static OffLineThread offLineThread;
	final String RESPONSE_MSG = "responseMsg";

	public static OffLineThread getInstance(Handler handler) {
		if (null == offLineThread) {
			offLineThread = new OffLineThread(handler);
			offLineThread.start();
		}

		return offLineThread;
	}

	private OffLineThread(Handler handler) {
		AppLog.enter(TAG, AppLog.getMethodName());

		fordDemoUtil = FordDemoUtil.getInstance();
		this.handler = handler;
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	@Override
	public void run() {
		while (true) {

			listJson = DatabaseAdapter.getInstance().getDataJsonArrayByFlag(
					FordDemoApp.getApplication(), 1);

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
							msg.what = Constants.UPDATE_COUNTER_CACHE;
							if (i < listJson.length() - 2) {
								msg.arg1 = listJson.length() - i;
							} else {
								msg.arg1 = 0;
							}
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
			// Check for connection after one second.
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
