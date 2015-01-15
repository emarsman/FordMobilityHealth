package com.openxc.ford.mHealth.demo.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Ford.db";
	private static final String DB_PATH = "/data/data/com.openxc.ford.mHealth.demo/databases/";
	private SQLiteDatabase mDataBase;

	private final Context mContext;

	public DatabaseHelper(Context context) {

		super(context, DATABASE_NAME, null, 1);
		this.mContext = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	public void createDataBase() throws IOException {

		boolean dbExist = databaseExist();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			} finally {
				this.close();
			}
		}

	}

	public boolean databaseExist() {
		File dbFile = new File(DB_PATH + DATABASE_NAME);
		return dbFile.exists();
	}

	private void copyDataBase() throws IOException {

		InputStream inputStream = mContext.getAssets().open(DATABASE_NAME);
		String outFileName = DB_PATH + DATABASE_NAME;
		OutputStream outputStream = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}

		outputStream.flush();
		outputStream.close();
		inputStream.close();

	}

	public void openDataBase() {
		String myPath = DB_PATH + DATABASE_NAME;
		mDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (mDataBase != null)
			mDataBase.close();
		super.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
