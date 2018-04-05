package ca.realsense.realsensegtadev.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.realsense.realsensegtadev.R;
import ca.realsense.realsensegtadev.entity.DataEntry;
import ca.realsense.realsensegtadev.entity.DataEntryCollection;
import ca.realsense.realsensegtadev.util.CommonUtils;
import ca.realsense.realsensegtadev.util.MyXAxisValueFormatter;


public abstract class ReportActivity extends AppCompatActivity {

    protected abstract String getReportTitle(List<DataEntryCollection> dataEntryCollectionList);

    // the table layout and data content would be different between single and comparison reports
    protected abstract void drawTable(int dataType, List<DataEntryCollection> dataEntryCollectionList, List<String> rowNameLabels);

    // the month text on the x-axis and table rows would be different between single and comparison report
    protected abstract List<String> getXAxisLabels(DataEntryCollection dataEntryCollection);

    protected abstract String getLegendText(DataEntryCollection dataEntryCollection);

    // generate the report content: line charts and data tables
    protected void generateReport(List<DataEntryCollection> dataEntryCollectionList) {

        TextView tvReportAreaName = (TextView) findViewById(R.id.tv_report_title);
        tvReportAreaName.setText(getReportTitle(dataEntryCollectionList));

        List<String> xAxisLabels = getXAxisLabels(dataEntryCollectionList.get(0));

        // scroll view to the top
        ((ScrollView) findViewById(R.id.report_scroll)).fullScroll(ScrollView.FOCUS_UP);

        // draw line charts and tables
        drawLineChart(CommonUtils.DATA_TYPE_AVERAGE_PRICE, dataEntryCollectionList, xAxisLabels);
        drawTable(CommonUtils.DATA_TYPE_AVERAGE_PRICE, dataEntryCollectionList, xAxisLabels);

        drawLineChart(CommonUtils.DATA_TYPE_MEDIAN_PRICE, dataEntryCollectionList, xAxisLabels);
        drawTable(CommonUtils.DATA_TYPE_MEDIAN_PRICE, dataEntryCollectionList, xAxisLabels);

        drawLineChart(CommonUtils.DATA_TYPE_SALES, dataEntryCollectionList, xAxisLabels);
        drawTable(CommonUtils.DATA_TYPE_SALES, dataEntryCollectionList, xAxisLabels);

        drawLineChart(CommonUtils.DATA_TYPE_NEW_LISTINGS, dataEntryCollectionList, xAxisLabels);
        drawTable(CommonUtils.DATA_TYPE_NEW_LISTINGS, dataEntryCollectionList, xAxisLabels);

        drawLineChart(CommonUtils.DATA_TYPE_ACTIVE_LISTINGS, dataEntryCollectionList, xAxisLabels);
        drawTable(CommonUtils.DATA_TYPE_ACTIVE_LISTINGS, dataEntryCollectionList, xAxisLabels);

        drawLineChart(CommonUtils.DATA_TYPE_SPLP, dataEntryCollectionList, xAxisLabels);
        drawTable(CommonUtils.DATA_TYPE_SPLP, dataEntryCollectionList, xAxisLabels);

        drawLineChart(CommonUtils.DATA_TYPE_DOM, dataEntryCollectionList, xAxisLabels);
        drawTable(CommonUtils.DATA_TYPE_DOM, dataEntryCollectionList, xAxisLabels);

    }

    // draw a line chart based on given data type and data of last year and current year
    protected void drawLineChart(int dataType, List<DataEntryCollection> dataEntryCollectionList, List<String> xAxisLabels) {

        // a set of lines to feed one line chart
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        // line index to get line properties, e.g. line color
        int lineIndex = 0;

        // go thru each line
        for (DataEntryCollection dataEntryCollection : dataEntryCollectionList) {



            // get list of entry point keys from common utils
            //List<String> reportEntryKeyList = CommonUtils.getReportEntryKeyList(areaPropertyType, year);

            Map<Integer, DataEntry> reportEntryMap = dataEntryCollection.getDataEntryMap();
            Set<Integer> reportEntryKeySet = reportEntryMap.keySet();

            // a list contains dots on the line
            List<Entry> valueSet = new ArrayList<>();

            // index for each dot
            float chartDotIndex = 0f;

            // go thru each report entry to feed data for each dot on the line
            for (Integer reportEntryKey : reportEntryKeySet) {

                DataEntry dataEntry = reportEntryMap.get(reportEntryKey);

                // entry data is only available to current month. Keys are always 12 for the whole year,
                // but data for current year is always not fully 12 months data
                if (null != dataEntry) {

                    int reportEntryValue = 0;

                    switch (dataType) {
                        case CommonUtils.DATA_TYPE_AVERAGE_PRICE:
                            reportEntryValue = dataEntry.getAveragePrice();
                            break;
                        case CommonUtils.DATA_TYPE_MEDIAN_PRICE:
                            reportEntryValue = dataEntry.getMedianPrice();
                            break;
                        case CommonUtils.DATA_TYPE_SALES:
                            reportEntryValue = dataEntry.getSales();
                            break;
                        case CommonUtils.DATA_TYPE_NEW_LISTINGS:
                            reportEntryValue = dataEntry.getNewListings();
                            break;
                        case CommonUtils.DATA_TYPE_ACTIVE_LISTINGS:
                            reportEntryValue = dataEntry.getActiveListings();
                            break;
                        case CommonUtils.DATA_TYPE_DOM:
                            reportEntryValue = dataEntry.getDom();
                            break;
                        case CommonUtils.DATA_TYPE_SPLP:
                            reportEntryValue = dataEntry.getSplp();
                            break;
                    }

                    // add a dot in the line
                    valueSet.add(new Entry(chartDotIndex, Float.valueOf("" + reportEntryValue)));
                }

                // increase the index for next dot. even there is no entry data, the index still need to increase
                chartDotIndex = chartDotIndex + 1F;
            }

            // add dot value set to a line data set. It makes a line
            // the first parameter is the data of all dots
            // the second parameter is the text displayed under the chart

            LineDataSet lineDataSet = new LineDataSet(valueSet, getLegendText(dataEntryCollection));


            // decorate the line based on the line index
            lineDataSet.setColor(CommonUtils.COMP_COLORS[lineIndex]);
            lineDataSet.setLineWidth(CommonUtils.LINE_CHART_LINE_WIDTH);
            lineDataSet.setCircleRadius(CommonUtils.LINE_CHART_CIRCLE_SIZE);
            lineDataSet.setCircleColor(CommonUtils.COMP_COLORS[lineIndex]);
            lineDataSet.setDrawValues(false);

            // add a line into the chart data sets
            dataSets.add(lineDataSet);
            // move to next line
            lineIndex = lineIndex + 1;
        }

        // prepare the chart
        // get the line chart view
        LineChart lineChart = null;

        switch (dataType) {
            case CommonUtils.DATA_TYPE_AVERAGE_PRICE:
                lineChart = (LineChart) findViewById(R.id.lc_report_average_price);
                break;
            case CommonUtils.DATA_TYPE_MEDIAN_PRICE:
                lineChart = (LineChart) findViewById(R.id.lc_report_median_price);
                break;
            case CommonUtils.DATA_TYPE_SALES:
                lineChart = (LineChart) findViewById(R.id.lc_report_sales);
                break;
            case CommonUtils.DATA_TYPE_NEW_LISTINGS:
                lineChart = (LineChart) findViewById(R.id.lc_report_new_listings);
                break;
            case CommonUtils.DATA_TYPE_ACTIVE_LISTINGS:
                lineChart = (LineChart) findViewById(R.id.lc_report_active_listings);
                break;
            case CommonUtils.DATA_TYPE_DOM:
                lineChart = (LineChart) findViewById(R.id.lc_report_average_dom);
                break;
            case CommonUtils.DATA_TYPE_SPLP:
                lineChart = (LineChart) findViewById(R.id.lc_report_splp);
                break;

        }

        // prepare x and y axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(xAxisLabels));
        //xAxis.setLabelCount(xAxisLabels.size());

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        lineChart.setTouchEnabled(false);

        lineChart.getAxisLeft().setEnabled(false);

        lineChart.setDescription(null);
        lineChart.animateX(12 * 200);
        lineChart.setGridBackgroundColor(Color.WHITE);

        // draw the line chart
        lineChart.invalidate();

    }



}
