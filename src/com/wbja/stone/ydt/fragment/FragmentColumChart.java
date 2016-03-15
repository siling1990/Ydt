package com.wbja.stone.ydt.fragment;

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
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import android.widget.RelativeLayout;

public class FragmentColumChart extends Fragment {
	
private RelativeLayout container;
	public FragmentColumChart() {

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
		
		ColumnChartView columnChartView = new ColumnChartView(getActivity());
		columnChartView.setColumnChartData(generateColumnChartData());
		columnChartView.setZoomType(ZoomType.HORIZONTAL);
		columnChartView.setValueSelectionEnabled(true);
		/**
		 * Note: Chart is within ViewPager so enable container scroll
		 * mode.
		 **/
		columnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
		Viewport v1 = new Viewport(columnChartView.getMaximumViewport());
		v1.bottom = 0;
		v1.top = 300;

		columnChartView.setMaximumViewport(v1);
		columnChartView.setCurrentViewport(v1);
		container.addView(columnChartView);
		
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
	private ColumnChartData generateColumnChartData() {
		int numSubcolumns = 1;
		int numColumns = 12;
		// Column can have many subcolumns, here by default I use 1
		// subcolumn in each of 8 columns.
		String[] months = new String[] { "¹ðÖ¦15g", "Éú½ª15g", "¸Ê²Ý10g", "ÉÖÒ©15g", "´óÔæ10g" };
		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		values = new ArrayList<SubcolumnValue>();
		values.add(new SubcolumnValue(283, ChartUtils.COLOR_BLUE));
		columns.add(new Column(values).setHasLabels(true));

		values = new ArrayList<SubcolumnValue>();
		values.add(new SubcolumnValue(283, ChartUtils.COLOR_GREEN));
		columns.add(new Column(values).setHasLabels(true));

		values = new ArrayList<SubcolumnValue>();
		values.add(new SubcolumnValue(117, ChartUtils.COLOR_ORANGE));
		columns.add(new Column(values).setHasLabels(true));

		values = new ArrayList<SubcolumnValue>();
		values.add(new SubcolumnValue(100, ChartUtils.COLOR_RED));
		columns.add(new Column(values).setHasLabels(true));

		values = new ArrayList<SubcolumnValue>();
		values.add(new SubcolumnValue(39, ChartUtils.COLOR_VIOLET));
		columns.add(new Column(values).setHasLabels(true));

		axisValues.add(new AxisValue(0).setLabel(months[0]));
		axisValues.add(new AxisValue(1).setLabel(months[1]));
		axisValues.add(new AxisValue(2).setLabel(months[2]));
		axisValues.add(new AxisValue(3).setLabel(months[3]));
		axisValues.add(new AxisValue(4).setLabel(months[4]));

		ColumnChartData data = new ColumnChartData(columns);

		data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
		data.setAxisYLeft(new Axis().setName("Ïà¶Ô Ò©Á¿").setHasLines(true));
		return data;

	}
}
