package com.wbja.stone.ydt.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.wbja.stone.ydt.ClinicRecordByDateActivity;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.ShowScheduleActivity;
import com.wbja.stone.ydt.adapter.ScheduleAdapter;
import com.wbja.stone.ydt.entity.Schedule;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.DateUtil;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wt.calendarcard.CalendarCard;
import com.wt.calendarcard.CardGridItem;
import com.wt.calendarcard.OnCellItemClick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentRc extends Fragment {

	private CalendarCard mCalendarCard;
	private TextView mTextView, textMonth;
	private ImageView last, next;
	Calendar dateDisplay;
	private ListView lvSch;
	private ScheduleAdapter adapterSch;
	private List<Schedule> schList;
	private DB db;
	private Intent intent;
	
	private Button btSearch;
	private Date timeTemp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_rc, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		db=new DB(getActivity());
		mCalendarCard = (CalendarCard) getView().findViewById(
				R.id.calendarCard1);
		dateDisplay = Calendar.getInstance();
		// dateDisplay.setTime(new Date());
		// dateDisplay.add(Calendar.MONTH, 1);
		timeTemp=new Date();
		
		mCalendarCard.setNoTitle(true);
		mCalendarCard.setOnCellItemClick(new OnCellItemClick() {
			@Override
			public void onCellClick(View v, CardGridItem item) {
				
				schList=db.querySchedule(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
				.format(item.getDate().getTime()),StringUtil.getID_User(getActivity()));
				timeTemp=item.getDate().getTime();
				if(schList==null||schList.size()==0){
					mTextView.setVisibility(View.VISIBLE);
					mTextView.setText("暂无日程");
				}else{
					mTextView.setVisibility(View.GONE);
				}
				adapterSch.updateListView(schList);
//				mTextView.setText(getResources().getString(
//						R.string.sel_date,
//						new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//								.format(item.getDate().getTime())));
			}
		});

		mTextView = (TextView) getView().findViewById(R.id.textView1);
		textMonth = (TextView) getView().findViewById(R.id.textMonth);
		last = (ImageView) getView().findViewById(R.id.last);
		next = (ImageView) getView().findViewById(R.id.next);
		btSearch = (Button) getView().findViewById(R.id.btSearch);
		
		lvSch=(ListView) getView().findViewById(R.id.lvSch);
		schList=db.querySchedule(DateUtil.dateToString(new Date(), "yyyy-MM-dd"),StringUtil.getID_User(getActivity()));
		if(schList==null||schList.size()==0){
			
			mTextView.setText("暂无日程");
		}else{
			mTextView.setVisibility(View.GONE);
		}
		adapterSch=new ScheduleAdapter(getActivity(), schList);
		lvSch.setAdapter(adapterSch);
		
		textMonth.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault())
				.format(dateDisplay.getTime()));

		btSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent=new Intent(getActivity(), ClinicRecordByDateActivity.class);
				intent.putExtra("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
				.format(timeTemp));
				getActivity().startActivity(intent);
			}
		});
		
		last.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dateDisplay.add(Calendar.MONTH, -1);
				mCalendarCard.setDateDisplay(dateDisplay);
				mCalendarCard.notifyChanges();
				textMonth.setText(new SimpleDateFormat("MMM yyyy", Locale
						.getDefault()).format(dateDisplay.getTime()));
			}
		});

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dateDisplay.add(Calendar.MONTH, 1);
				mCalendarCard.setDateDisplay(dateDisplay);
				mCalendarCard.notifyChanges();
				textMonth.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault())
						.format(dateDisplay.getTime()));
			}
		});
		
		lvSch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				intent=new Intent(getActivity(), ShowScheduleActivity.class);
				StringUtil.saveInfo(getActivity(), "shcedule", GsonUtil.getJsonValue(schList.get(position)));
				getActivity().startActivity(intent);
			}
		});
		
		lvSch.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new AlertDialog.Builder(getActivity())
				.setMessage("删除")
				.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								//delete from db
								db.deleteSch(schList.get(position).getId());
								//delete from ui
								schList.remove(position);
								//update ui
								adapterSch.updateListView(schList);
							}
						}).setNegativeButton("取消", null)
				.show();
				
				return false;
			}
		});
		
	}
	
	@Override
	public void onResume() {
//		schList=db.querySchedule(DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
//		if(schList==null||schList.size()==0){
//			
//			mTextView.setText("暂无日程");
//		}else{
//			mTextView.setVisibility(View.GONE);
//		}
//		adapterSch.updateListView(schList);
		super.onResume();
	}
	

}
