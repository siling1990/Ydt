package com.wbja.stone.ydt.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wbja.stone.ydt.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentLineColumChart extends Fragment {
	
private RelativeLayout container;
	public FragmentLineColumChart() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("********linecolum******", "");
		return inflater.inflate(R.layout.fragment_chart, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		Log.d("********linecolum******", "");

		super.onActivityCreated(savedInstanceState);
		container=(RelativeLayout) getView().findViewById(R.id.container);
		
		TextView text=(TextView) getView().findViewById(R.id.txt);;
		text.setVisibility(View.VISIBLE);
		
		ComboLineColumnChartView lineChartView = new ComboLineColumnChartView(getActivity());
		// lineChartView.setLineChartData(generateLineChartData());
		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		String[] days = new String[] { "未加权药性", "心", "肝", "脾", "肺", "肾", "膀胱", "胃" };
		axisValues.add(new AxisValue(1).setLabel(days[0]));
		axisValues.add(new AxisValue(2).setLabel(days[1]));
		axisValues.add(new AxisValue(3).setLabel(days[2]));
		axisValues.add(new AxisValue(4).setLabel(days[3]));
		axisValues.add(new AxisValue(5).setLabel(days[4]));
		axisValues.add(new AxisValue(6).setLabel(days[5]));
		axisValues.add(new AxisValue(7).setLabel(days[6]));
		axisValues.add(new AxisValue(8).setLabel(days[7]));
		
		ComboLineColumnChartData data = new ComboLineColumnChartData(getCom(), generateLineChartData());

		data.setAxisXBottom(new Axis(axisValues));
		data.setAxisYLeft(new Axis().setName("未加权属性").setHasLines(true));
		
		lineChartView.setComboLineColumnChartData(data);
		lineChartView.setZoomType(ZoomType.HORIZONTAL);
		/**
		 * Note: Chart is within ViewPager so enable container scroll
		 * mode.
		 **/
		lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
		lineChartView.setValueSelectionEnabled(false);
		Viewport v = new Viewport(lineChartView.getMaximumViewport());
		v.bottom = -3;
		v.top = 4;
//		v.left = 0;
//		v.right = 8;
		lineChartView.setMaximumViewport(v);
		lineChartView.setCurrentViewport(v);
		container.addView(lineChartView);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("********Bsoncreate******", "");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	}
	private LineChartData generateLineChartData() {
		int numValues = 20;

		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<PointValue> values = new ArrayList<PointValue>();
		// for (int i = 0; i < numValues; ++i) {
		// values.add(new PointValue(i, (float) Math.random() * 100f));
		// }

		values.add(new PointValue(1, 3));
		values.add(new PointValue(2, 2));
		values.add(new PointValue(3, -2));
		values.add(new PointValue(4, 1));
		values.add(new PointValue(5, 3));
		values.add(new PointValue(6, 0));
		values.add(new PointValue(7, 2));
		values.add(new PointValue(8, 3));

		Line line = new Line(values);
		line.setColor(ChartUtils.COLOR_GREEN);

		List<Line> lines = new ArrayList<Line>();
		line.setHasLabels(true);
		lines.add(line);

		// second
		values = new ArrayList<PointValue>();
		values.add(new PointValue(1, 2));
		values.add(new PointValue(2, 2));
		values.add(new PointValue(3, 0));
		values.add(new PointValue(4, 0));
		values.add(new PointValue(5, 2));
		values.add(new PointValue(6, 0));
		values.add(new PointValue(7, 2));
		values.add(new PointValue(8, 0));
		String[] days = new String[] { "未加权药性", "心", "肝", "脾", "肺", "肾", "膀胱", "胃" };
		axisValues.add(new AxisValue(1).setLabel(days[0]));
		axisValues.add(new AxisValue(2).setLabel(days[1]));
		axisValues.add(new AxisValue(3).setLabel(days[2]));
		axisValues.add(new AxisValue(4).setLabel(days[3]));
		axisValues.add(new AxisValue(5).setLabel(days[4]));
		axisValues.add(new AxisValue(6).setLabel(days[5]));
		axisValues.add(new AxisValue(7).setLabel(days[6]));
		axisValues.add(new AxisValue(8).setLabel(days[7]));

		line = new Line(values);
		line.setHasLabels(true);
		line.setColor(ChartUtils.COLOR_ORANGE);

		lines.add(line);

		// Line line = new Line(values);
		// line.setColor(ChartUtils.COLOR_ORANGE);
		// List<Line> lines = new ArrayList<Line>();
		// lines.add(line);
		LineChartData data = new LineChartData(lines);
		data.setAxisXBottom(new Axis().setName("方剂及药物属性对比"));
		data.setAxisXBottom(new Axis(axisValues).setName("方剂及药物属性对比"));
		data.setAxisYLeft(new Axis().setName("未加权属性").setHasLines(true));

		return data;

	}

	private ColumnChartData getCom() {
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> valuessub;
		
//		valuessub = new ArrayList<SubcolumnValue>();
//		valuessub.add(new SubcolumnValue(-3, ChartUtils.COLOR_GREEN).setLabel("桂枝汤"));
//		columns.add(new Column(valuessub).setHasLabels(true));
		
		valuessub = new ArrayList<SubcolumnValue>();
		valuessub.add(new SubcolumnValue(-1, ChartUtils.COLOR_GREEN).setLabel("桂枝汤"));
		columns.add(new Column(valuessub).setHasLabels(true));

		valuessub = new ArrayList<SubcolumnValue>();
		valuessub.add(new SubcolumnValue(-1, ChartUtils.COLOR_ORANGE).setLabel("桂枝"));
		columns.add(new Column(valuessub).setHasLabels(true));

		ColumnChartData data = new ColumnChartData(columns);

		return data;
	}
}
