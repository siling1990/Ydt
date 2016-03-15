package com.wbja.stone.ydt.adapter;

import java.util.List;

import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.entity.DeptInfo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeptAdapter extends BaseAdapter{
	
	private Context context;
	private List<DeptInfo> list;
	public DeptAdapter(Context context,List<DeptInfo> list){
		this.context=context;
		this.list=list;
	}

	
	public void updateListView(List<DeptInfo> list){
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

	
	class ViewHolder {
		TextView text1;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		DeptInfo dept=list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_spinner, null);
			viewHolder.text1 = (TextView) view.findViewById(R.id.text1);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.text1.setText(dept.getName());
			
		return view;
	}

}