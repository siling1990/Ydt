package com.wbja.stone.ydt.adapter;

import java.util.List;

import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.entity.Registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RegAdapter extends BaseAdapter{
	
	private Context context;
	private List<Registration> list;
	public RegAdapter(Context context,List<Registration> list){
		this.context=context;
		this.list=list;
	}

	
	public void updateListView(List<Registration> list){
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
		TextView txtContent;
		TextView txtType;
		TextView txtTime;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		Registration reg=list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_schedule, null);
			viewHolder.txtContent = (TextView) view.findViewById(R.id.txtContent);
			viewHolder.txtType = (TextView) view.findViewById(R.id.txtType);
			viewHolder.txtTime = (TextView) view.findViewById(R.id.txtTime);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.txtContent.setText(reg.getClinic_time());
			
		viewHolder.txtType.setText(reg.getName_patient());
		
		if(reg.getIs_pass()==0){
			viewHolder.txtTime.setTextColor(context.getResources().getColor(R.color.orange));
			viewHolder.txtTime.setText("等待审核");
		}else
		if(reg.getIs_pass()==1){
			viewHolder.txtTime.setText("预约成功");
		}else{
			viewHolder.txtTime.setText("预约失败");
		}
		return view;
	}

}