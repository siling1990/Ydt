package com.wbja.stone.ydt.adapter;

import java.util.List;

import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.entity.HospitalInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HospitalSearchAdapter extends BaseAdapter{
	
	private Context context;
	private List<HospitalInfo> list;
	public HospitalSearchAdapter(Context context,List<HospitalInfo> list){
		this.context=context;
		this.list=list;
	}

	
	public void updateListView(List<HospitalInfo> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner, null);
			viewHolder = new ViewHolder();
			
			viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
			
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		HospitalInfo item=list.get(position);
		// holder.index.setText((String) items.get(position).getIndex());
		if(item.getName().length()>20){
			viewHolder.text1.setText(item.getName().substring(0, 20));
		}else
		viewHolder.text1.setText(item.getName());
		
		return convertView;
	}

	/* class ViewHolder */
	private class ViewHolder {
		TextView text1;
		
	}

}