package com.openxc.ford.mHealth.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.FordDemoApp;
import com.openxc.ford.mHealth.demo.FordDemoUtil;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.model.Vehicle;

public class VehicleDataAdapter extends BaseAdapter {
	private final String TAG = AppLog.getClassName();

	private List<Vehicle> mVehicleRoute = null;
	public LayoutInflater mInflater = null;

	public static class ViewHolder {
		TextView txVwSerialNo;
		TextView txVwLatitude;
		TextView txVwLongitude;
		TextView txVwSpeed;
		TextView txVwTimeStamp;
	}

	public VehicleDataAdapter() {
		this.mVehicleRoute = FordDemoUtil.getInstance().getVehicleRoute();
		this.mInflater = (LayoutInflater) FordDemoApp.getApplication()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		AppLog.enter(TAG, AppLog.getMethodName());

		int count = 0;

		if (null != mVehicleRoute) {
			AppLog.info(TAG, "Vehicle List is not null and not zero.");
			count = mVehicleRoute.size();
		}

		AppLog.exit(TAG, AppLog.getMethodName());
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppLog.enter(TAG, AppLog.getMethodName());

		AppLog.info(TAG, "Vehicle List not null.");
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.vehicle_data_list_row,
					null);

			holder.txVwSerialNo = (TextView) convertView
					.findViewById(R.id.tv_serial_no);
			holder.txVwLatitude = (TextView) convertView
					.findViewById(R.id.tv_latitude);
			holder.txVwLongitude = (TextView) convertView
					.findViewById(R.id.tv_longitude);
			holder.txVwSpeed = (TextView) convertView
					.findViewById(R.id.tv_speed);
			holder.txVwTimeStamp = (TextView) convertView
					.findViewById(R.id.tv_timestamp);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Vehicle vehicle = null;

		if (null != mVehicleRoute && mVehicleRoute.size() > position) {

			vehicle = mVehicleRoute.get(position);

			holder.txVwSerialNo.setText("" + (position + 1));
			holder.txVwLatitude.setText(vehicle.getLatitude());
			holder.txVwLongitude.setText(vehicle.getLongitude());
			holder.txVwSpeed.setText(vehicle.getSpeed());
			holder.txVwTimeStamp.setText(vehicle.getTimeStamp());
		}

		AppLog.exit(TAG, AppLog.getMethodName());
		return convertView;
	}
}