package com.wbja.stone.ydt.adapter;

import java.util.List;

import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.entity.MRClinic;
import com.wbja.stone.ydt.entity.Patient;
import com.wbja.stone.ydt.entity.PatientUpload;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MRCAdapter extends BaseAdapter{
	
	private Context context;
	private List<MRClinic> list;
	public MRCAdapter(Context context,List<MRClinic> list){
		this.context=context;
		this.list=list;
	}

	
	public void updateListView(List<MRClinic> list){
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
		TextView txtContent;
		TextView txtType;
		TextView txtTime;
		TextView txtUpload;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		DB db=new DB(context);
		MRClinic sch=(MRClinic)getItem(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_schedule, null);
			viewHolder.txtContent = (TextView) view.findViewById(R.id.txtContent);
			viewHolder.txtType = (TextView) view.findViewById(R.id.txtType);
			viewHolder.txtTime = (TextView) view.findViewById(R.id.txtTime);
			viewHolder.txtUpload = (TextView) view.findViewById(R.id.txtUpload);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if(StringUtil.isEmpty(sch.getTitle())){
			viewHolder.txtContent.setText("暂无名称");
		}else{
			viewHolder.txtContent.setText("病历名称："+sch.getTitle());
		}
		
		viewHolder.txtType.setText("图像："+FileUtil.getFilesCount(sch.getSrc())[0]+"音频："+FileUtil.getFilesCount(sch.getSrc())[1]+"文本："+FileUtil.getFilesCount(sch.getSrc())[2]);
		if(sch.getId_patient()==0){
			viewHolder.txtTime.setText("无关联患者");
		}else{
			Patient p=db.queryPatientById(sch.getId_patient());
			String json = GsonUtil.getJsonValue((Patient) p);
			Log.d("****PatientActivityInfo****", json);
			StringUtil.saveInfo(context, "uploadRP",json);
			if(p!=null){
				viewHolder.txtTime.setText("患者姓名："+p.getName_doctor());
			}
			if(sch.getId()>0&&sch.getIsEdit()!=1){//已上传并且未修改
				viewHolder.txtUpload.setVisibility(View.VISIBLE);
				viewHolder.txtUpload.setText("已同步云端");
			}
		}
		return view;
	}

}
