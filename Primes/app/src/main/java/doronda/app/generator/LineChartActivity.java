package doronda.app.generator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by doronda on 16.12.2015.
 */
public class LineChartActivity extends AppCompatActivity {

    private static int range;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        Intent i = getIntent();
        i.getExtras();
        range = i.getExtras().getInt("RANGE");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ChartFragment()).commit();
        }
        DBAdapter.getInstance(getApplicationContext()).open();
    }

    public static class ChartFragment extends Fragment {

        private LineChartView chart;
        private LineChartData data;

        public ChartFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());
            generateData();
            chart.setViewportCalculationEnabled(false);
            resetViewport();

            return rootView;
        }

        private void resetViewport() {
            // Reset viewport height range to (0,100)
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = (DBAdapter.getInstance(getActivity()).getMaxDuration(range))*(float)1.1;
            v.left = 0;
            v.right = (DBAdapter.getInstance(getActivity()).getMaxNumberOfThreads(range)) < 10 ? 10 : (DBAdapter.getInstance(getActivity()).getMaxNumberOfThreads(range));
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        private void generateData() {
            List<Line> lines = new ArrayList<Line>();
            Line line = new Line(DBAdapter.getInstance(getActivity()).getDataForChart(range)).setColor(Color.BLUE).setCubic(true);
            lines.add(line);

            data = new LineChartData(lines);

            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            axisX.setName("Thread numbers");
            axisY.setName("Time, ms");

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);

            data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);
        }

        private class ValueTouchListener implements LineChartOnValueSelectListener {

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("act", "onDestroy");
    }
}