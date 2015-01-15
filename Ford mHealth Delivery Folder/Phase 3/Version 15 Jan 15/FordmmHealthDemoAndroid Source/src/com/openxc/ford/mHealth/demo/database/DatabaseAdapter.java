package com.openxc.ford.mHealth.demo.database;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.Constants;
import com.openxc.ford.mHealth.demo.model.Vehicle;

public class DatabaseAdapter {

	private final String TAG = AppLog.getClassName();

	private String TABLE_UPLOAD = "upload_data";
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";
	private static final String TIMESTAMP = "timeStamp";
	private static final String VEH_SPEED = "vehSpeed";
	private static final String VEHICLE_ID = "vehicleId";
	private static final String IS_OFFLINE = "isOffline";
	private static final String RID = "rId";
	public static SQLiteDatabase database = null;
	private static DatabaseAdapter sDatabaseAdapter = null;

	private DatabaseAdapter() {
	}

	public static DatabaseAdapter getInstance() {
		if (null == sDatabaseAdapter) {
			sDatabaseAdapter = new DatabaseAdapter();
		}

		return sDatabaseAdapter;
	}

	// Insertion of data in the database using json of vehicle Object //

	public void insertData(Context context, ArrayList<Vehicle> dataList) {
		AppLog.enter(TAG, AppLog.getMethodName());

		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		try {
			for (Vehicle data : dataList) {
				ContentValues values = new ContentValues();
				values.put(LATITUDE, data.getLatitude());
				values.put(LONGITUDE, data.getLongitude());
				values.put(TIMESTAMP, data.getTimeStamp());
				values.put(VEH_SPEED, data.getSpeed());
				values.put(VEHICLE_ID, data.getVehicleId());
				values.put(IS_OFFLINE, data.getIsOffline());
				values.put(RID, data.getrId());
				database.insert(TABLE_UPLOAD, null, values);
			}

		} finally {
			database.close();
			dbHelper.close();
		}
		AppLog.exit(TAG, AppLog.getMethodName());
	}

	public void insertData(Context context, JSONObject json) {
		AppLog.enter(TAG, AppLog.getMethodName());

		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(LATITUDE, json.getString(LATITUDE));
			values.put(LONGITUDE, json.getString(LONGITUDE));
			values.put(TIMESTAMP, json.getString(TIMESTAMP));
			values.put(VEH_SPEED, json.getString(VEH_SPEED));
			values.put(VEHICLE_ID, json.getString(VEHICLE_ID));
			values.put(IS_OFFLINE, Integer.parseInt(json.getString(IS_OFFLINE)));

			values.put(RID, Integer.parseInt(json.getString(RID)));

			database.insert(TABLE_UPLOAD, null, values);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			database.close();
			dbHelper.close();
		}

		AppLog.exit(TAG, AppLog.getMethodName());
	}

	// Method and Query to get specific amount of data from database

	public JSONArray getDataJsonArrayByFlag(Context context, int flag) {
		AppLog.enter(TAG, AppLog.getMethodName());
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from " + TABLE_UPLOAD
				+ " where isOffline = " + "'" + flag + "'"
				+ " order by timeStamp  desc limit " + Constants.MAX_ROW_COUNT,
				null);

		JSONArray alData = new JSONArray();
		JSONObject data = null;

		try {
			cursor.moveToFirst();

			if (cursor.getCount() > 0) {

				for (int i = 0; i < cursor.getCount(); i++) {
					data = new JSONObject();

					data.put(LATITUDE,
							cursor.getString(cursor.getColumnIndex(LATITUDE)));
					data.put(LONGITUDE,
							cursor.getString(cursor.getColumnIndex(LONGITUDE)));
					data.put(TIMESTAMP,
							cursor.getString(cursor.getColumnIndex(TIMESTAMP)));
					data.put(VEHICLE_ID,
							cursor.getString(cursor.getColumnIndex(VEHICLE_ID)));
					data.put(VEH_SPEED,
							cursor.getString(cursor.getColumnIndex(VEH_SPEED)));

					data.put(RID, cursor.getString(cursor.getColumnIndex(RID)));

					alData.put(data);
					cursor.moveToNext();
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			dbHelper.close();
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return alData;
	}

	public int getDataBaseLength(Context context) {
		AppLog.enter(TAG, AppLog.getMethodName());
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select rId from " + TABLE_UPLOAD,
				null);

		int count;

		try {
			count = cursor.getCount();

			AppLog.info(TAG, "Count " + count);

		} finally {
			cursor.close();
			dbHelper.close();
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return count;
	}

	public int getDataBaseLength(Context context, int flag) {
		AppLog.enter(TAG, AppLog.getMethodName());
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select rId from " + TABLE_UPLOAD
				+ " where isOffline = " + "'" + flag + "'", null);

		int count;

		try {
			count = cursor.getCount();

			AppLog.info(TAG, "Count " + count);

		} finally {
			cursor.close();
			dbHelper.close();
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return count;
	}

	// Database Method and Query to get last record from the database
	// Query uses timestamp to put the data in decreasing order and choose first
	// one

	public int getLastUpdatedRecordId(Context context) {
		AppLog.enter(TAG, AppLog.getMethodName());
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select rId from " + TABLE_UPLOAD
				+ " order by timeStamp  desc limit 1", null);

		int count;

		try {
			cursor.moveToFirst();
			count = cursor.getInt(cursor.getColumnIndex(RID));

			AppLog.info(TAG, "Count " + count);

		} finally {
			cursor.close();
			dbHelper.close();
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return count;
	}

	public void removedata(Context context, String timestamp) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete(TABLE_UPLOAD, "timeStamp=?", new String[] { timestamp });
		dbHelper.close();
	}

	// Database query to update the already exist data //

	public boolean UpdateData(Context context, JSONObject json) {
		AppLog.enter(TAG, AppLog.getMethodName());
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		long index = 0;
		try {
			ContentValues values = new ContentValues();

			values.put(LATITUDE, json.getString(LATITUDE));
			values.put(LONGITUDE, json.getString(LONGITUDE));
			values.put(TIMESTAMP, json.getString(TIMESTAMP));
			values.put(VEH_SPEED, json.getString(VEH_SPEED));
			values.put(VEHICLE_ID, json.getString(VEHICLE_ID));
			values.put(IS_OFFLINE, 0);

			values.put(RID, Integer.parseInt(json.getString(RID)));

			index = database.update(TABLE_UPLOAD, values, "rId=?",
					new String[] { json.getString(RID) });

		} catch (Exception e) {

		} finally {
			dbHelper.close();
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return index > 0;
	}

	public boolean UpdateData(Context context, JSONObject json, int rId) {
		AppLog.enter(TAG, AppLog.getMethodName());
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		AppLog.info(TAG, " ID : " + rId);

		long index = 0;
		try {
			ContentValues values = new ContentValues();

			values.put(LATITUDE, json.getString(LATITUDE));
			values.put(LONGITUDE, json.getString(LONGITUDE));
			values.put(TIMESTAMP, json.getString(TIMESTAMP));
			values.put(VEH_SPEED, json.getString(VEH_SPEED));
			values.put(VEHICLE_ID, json.getString(VEHICLE_ID));
			values.put(IS_OFFLINE, 1);

			values.put(RID, rId);

			index = database.update(TABLE_UPLOAD, values, "rId=?",
					new String[] { "" + rId });

		} catch (Exception e) {

		} finally {
			dbHelper.close();
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return index > 0;
	}

}
