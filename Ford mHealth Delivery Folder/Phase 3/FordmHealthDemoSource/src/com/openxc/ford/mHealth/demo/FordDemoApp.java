package com.openxc.ford.mHealth.demo;

import android.app.Application;

public class FordDemoApp extends Application {
	private final String TAG = AppLog.getClassName();

	private static FordDemoApp sFordDemoApp = null;

	public static FordDemoApp getApplication() {
		return sFordDemoApp;
	}

	@Override
	public void onCreate() {
		AppLog.enter(TAG, AppLog.getMethodName());

		super.onCreate();
		sFordDemoApp = this;

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}
