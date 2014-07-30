package com.openxc.vehicle.crash.app;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoAdapter extends BaseAdapter{
	ArrayList<String> groupNamelist, groupValuelist;
Context context;
public LayoutInflater inflater;
	public InfoAdapter(Context cnxt,
			ArrayList<String> nodeNamelist, ArrayList<String> nodeValelist) {
		// TODO Auto-generated constructor stub
		super();
		this.context=cnxt;
		this.groupNamelist=nodeNamelist;
		this.groupValuelist=nodeValelist;
		 this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupNamelist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	public static class ViewHolder
	{
	
		TextView txtViewGrpName;
		TextView txtViewGrptxt;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listitem_row, null);

		
			holder.txtViewGrpName = (TextView) convertView.findViewById(R.id.txtViewTitle);
			holder.txtViewGrptxt = (TextView) convertView.findViewById(R.id.txtViewDescription);

			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder)convertView.getTag();

		
		holder.txtViewGrpName.setText(groupNamelist.get(position));
		holder.txtViewGrptxt.setText(groupValuelist.get(position));

		return convertView;

		
		
	
	}

}
