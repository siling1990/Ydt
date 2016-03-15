package com.wbja.stone.ydt.fragment;

import java.util.Date;
import java.util.List;

import com.wbja.stone.ydt.ClinicRecordActivity;
import com.wbja.stone.ydt.ConversationListActivity;
import com.wbja.stone.ydt.FyfxActivity;
import com.wbja.stone.ydt.PatientActivity;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.ScheduleActivity;
import com.wbja.stone.ydt.TabZYZSKActivity;
import com.wbja.stone.ydt.adapter.ScheduleAdapter;
import com.wbja.stone.ydt.entity.Schedule;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.DateUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentIndex extends Fragment implements OnClickListener {

	private LinearLayout hzxx, mzbl, fyfx, rctx, dtyq, gdgn;
	private Intent intent;

	private ListView lvSch;
	private ScheduleAdapter adapterSch;
	private List<Schedule> schList;
	private TextView txtRc;
	private DB db;

	private RelativeLayout rlrc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_index, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		intent = new Intent();
		db = new DB(getActivity());


		hzxx = (LinearLayout) getView().findViewById(R.id.hzxx);
		mzbl = (LinearLayout) getView().findViewById(R.id.mzbl);
		fyfx = (LinearLayout) getView().findViewById(R.id.fyfx);
		rctx = (LinearLayout) getView().findViewById(R.id.rctx);
		dtyq = (LinearLayout) getView().findViewById(R.id.dtyq);
		gdgn = (LinearLayout) getView().findViewById(R.id.gdgn);
		txtRc = (TextView) getView().findViewById(R.id.txtRc);

		rlrc = (RelativeLayout) getView().findViewById(R.id.rlrc);

		hzxx.setOnClickListener(this);
		mzbl.setOnClickListener(this);
		fyfx.setOnClickListener(this);
		rctx.setOnClickListener(this);
		dtyq.setOnClickListener(this);
		gdgn.setOnClickListener(this);
		rlrc.setOnClickListener(this);

		lvSch = (ListView) getView().findViewById(R.id.lvSch);
		initUI();
	}
	private void initUI(){
		schList = db.querySchedule(DateUtil.dateToString(new Date(),
				"yyyy-MM-dd"),StringUtil.getID_User(getActivity()));
		if (schList == null || schList.size() == 0) {

			txtRc.setText("暂无日程");
		} else {
			txtRc.setVisibility(View.GONE);
		}
		adapterSch = new ScheduleAdapter(getActivity(), schList);
		lvSch.setAdapter(adapterSch);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
		initUI();
	}

	public void turnTo(Class<?> cls) {
		intent.setClass(getActivity(), cls);
		getActivity().startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.hzxx:
			turnTo(PatientActivity.class);
			break;
		case R.id.mzbl:
			turnTo(ClinicRecordActivity.class);
			break;
		case R.id.fyfx:
			//Toast.makeText(getActivity(), "暂不开放", Toast.LENGTH_SHORT).show();
			turnTo(FyfxActivity.class);
			break;
		case R.id.rctx:
			turnTo(ScheduleActivity.class);
			break;
		case R.id.dtyq:
			//Toast.makeText(getActivity(), "正在开发。。。", Toast.LENGTH_SHORT).show();
			turnTo(ConversationListActivity.class);
			break;
		case R.id.gdgn:
			//Toast.makeText(getActivity(), "正在开发。。。", Toast.LENGTH_SHORT).show();
			turnTo(TabZYZSKActivity.class);
			break;
		case R.id.rlrc:
			turnTo(ScheduleActivity.class);
			break;
		default:
			break;
		}
	}
}
