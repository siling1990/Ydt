package com.wbja.stone.ydt.adapter;

import java.util.List;

import com.wbja.stone.ydt.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ZhengzhuangAdapter extends BaseAdapter{
	
	private Context context;
	private List<String> list;
	private ViewHolder viewHolder = null;
	public ZhengzhuangAdapter(Context context,List<String> list){
		this.context=context;
		this.list=list;
	}

	public void updateListView(List<String> list){
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

	
	final static class ViewHolder {
		TextView title;
	}
	
	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		String cas=list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_zz, null);
			viewHolder.title = (TextView) view.findViewById(R.id.title);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.title.setText(cas);
		
		return view;
	}

}