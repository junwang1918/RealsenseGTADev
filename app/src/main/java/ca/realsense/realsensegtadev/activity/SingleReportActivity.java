package ca.realsense.realsensegtadev.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import ca.realsense.realsensegtadev.R;
import ca.realsense.realsensegtadev.data.DataService;
import ca.realsense.realsensegtadev.data.DatabaseManager;
import ca.realsense.realsensegtadev.entity.DataEntry;
import ca.realsense.realsensegtadev.entity.DataEntryCollection;
import ca.realsense.realsensegtadev.util.CalculationUtil;
import ca.realsense.realsensegtadev.util.CommonUtils;


public class SingleReportActivity extends ReportActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize the screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Report");

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get the intent from previous activity
        Intent intent = getIntent();

        // a combination of area and property type as part of the key
        final String areaPropertyType = intent.getStringExtra(CommonUtils.INTENT_EXTRA_KEY_SINGLE_REPORT_KEY);

        // get firebase database reference
        DatabaseReference firebaseDatabaseReference = DatabaseManager.getDatabaseReference();

        // firebase database listener
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get data for current and last year
                List<DataEntryCollection> dataEntryCollectionList = DataService.getInstance().retrieveChartData(dataSnapshot, areaPropertyType, CommonUtils.yearsToReport);

                // calculate rates for the current year
                int dataCount = dataEntryCollectionList.size();
                DataEntryCollection currentYearData = dataEntryCollectionList.get(dataCount - 1);
                DataEntryCollection lastYearData = dataEntryCollectionList.get(dataCount - 2);

                CalculationUtil.calculateRatesForCurrentYear(currentYearData, lastYearData);

                // generate report content
                generateReport(dataEntryCollectionList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // add listener to the database reference
        firebaseDatabaseReference.addValueEventListener(valueEventListener);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_big_pic) {
            Intent intent = new Intent(SingleReportActivity.this, BigPictureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_watchlist) {
            Intent intent = new Intent(SingleReportActivity.this, WatchlistActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(SingleReportActivity.this, NewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_service_terms) {
            Intent intent = new Intent(SingleReportActivity.this, ServiceTermsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected List<String> getXAxisLabels(DataEntryCollection dataEntryCollectionList) {

        List<String> monthNames = new ArrayList<String>();

        for (int i = 0; i < 12; i++) {
            if (i < 9) {
                monthNames.add("0" + (i + 1));
            } else {
                monthNames.add("" + (i + 1));
            }
        }
        return monthNames;

    }

    @Override
    protected String getLegendText(DataEntryCollection dataEntryCollection) {
        return dataEntryCollection.getYear().toString();
    }

    @Override
    protected String getReportTitle(List<DataEntryCollection> dataEntryCollectionList) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append(dataEntryCollectionList.get(0).getPropertyType());
        sb.append(" in ");
        sb.append(dataEntryCollectionList.get(0).getAreaName());
        sb.append("\n");
        return sb.toString();
    }

    // draw a table for a list of report data
    protected void drawTable(int dataType, List<DataEntryCollection> dataEntryCollectionList, List<String> rowNameLabels) {

        // find the table view
        TableLayout tableLayout = null;

        switch (dataType) {
            case CommonUtils.DATA_TYPE_AVERAGE_PRICE:
                tableLayout = (TableLayout) findViewById(R.id.tbl_report_average_price);
                break;
            case CommonUtils.DATA_TYPE_MEDIAN_PRICE:
                tableLayout = (TableLayout) findViewById(R.id.tbl_report_median_price);
                break;
            case CommonUtils.DATA_TYPE_SALES:
                tableLayout = (TableLayout) findViewById(R.id.tbl_report_sales);
                break;
            case CommonUtils.DATA_TYPE_NEW_LISTINGS:
                tableLayout = (TableLayout) findViewById(R.id.tbl_report_new_listing);
                break;
            case CommonUtils.DATA_TYPE_ACTIVE_LISTINGS:
                tableLayout = (TableLayout) findViewById(R.id.tbl_report_active_listing);
                break;
            case CommonUtils.DATA_TYPE_DOM:
                tableLayout = (TableLayout) findViewById(R.id.tbl_report_average_dom);
                break;
            case CommonUtils.DATA_TYPE_SPLP:
                tableLayout = (TableLayout) findViewById(R.id.tbl_report_splp);
                break;
        }
        // clean any existing table cell
        tableLayout.removeAllViews();

        // draw header
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundResource(R.drawable.backtext);
        // first cell
        TextView headerRowTv = new TextView(this);
        headerRowTv.setText("");
        headerRowTv.setGravity(Gravity.CENTER);
        headerRow.addView(headerRowTv);
        // loop thru years, cell 2 and 3
        ListIterator<DataEntryCollection> liter = dataEntryCollectionList.listIterator();

        while (liter.hasNext()) {
            DataEntryCollection reportdata = liter.next();

            TextView headerRowTv1 = new TextView(this);
            headerRowTv1.setText("" + reportdata.getYear().intValue());
            headerRowTv1.setGravity(Gravity.CENTER);
            headerRow.addView(headerRowTv1);
        }

        // cell 4, title of monthly change
        TextView headerRowTv4 = new TextView(this);
        headerRowTv4.setText("Monthly");
        headerRowTv4.setGravity(Gravity.CENTER);
        headerRow.addView(headerRowTv4);
        // cell 5, title of annual change
        TextView headerRowTv5 = new TextView(this);
        headerRowTv5.setText("Annual");
        headerRowTv5.setGravity(Gravity.CENTER);
        headerRow.addView(headerRowTv5);
        // add header
        tableLayout.addView(headerRow);

        // go thru report data, draw each cell of table content
        for (String rowName : rowNameLabels) {
            // a row
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundResource(R.drawable.backtext);
            // first cell. row name
            TextView tv = new TextView(this);
            tv.setText(rowName);
            tv.setGravity(Gravity.CENTER);
            tableRow.addView(tv);

            // same as line index in the line chart
            int columnIndex = 0;

            // go thru 2 years report data
            ListIterator<DataEntryCollection> li = dataEntryCollectionList.listIterator();

            while (li.hasNext()) {

                DataEntryCollection dataEntryCollection = li.next();
                Map reportEntryMap = dataEntryCollection.getDataEntryMap();

                // entry key is the report entry key in database
                Integer entryKey = Integer.parseInt(dataEntryCollection.getYear() + rowName);

                Object reportEntryObj = reportEntryMap.get(entryKey);

                // prepare cell
                TextView dataTv = new TextView(this);
                dataTv.setTextColor(CommonUtils.COMP_COLORS[columnIndex]);
                dataTv.setGravity(Gravity.CENTER);

                // prepare cell data
                int reportEntryValue = 0;

                if (null == reportEntryObj) { // no data available yet

                    dataTv.setText("n/a");
                    dataTv.setGravity(Gravity.CENTER);
                    dataTv.setTextColor(Color.GRAY);
                    tableRow.addView(dataTv);

                    if (dataEntryCollection.getYear().intValue() == Calendar.getInstance().get(Calendar.YEAR)) {
                        TextView tvPcRate = new TextView(this);
                        tvPcRate.setText("n/a");
                        tvPcRate.setGravity(Gravity.CENTER);
                        tvPcRate.setTextColor(Color.GRAY);
                        tableRow.addView(tvPcRate);

                        TextView tvAcRate = new TextView(this);
                        tvAcRate.setGravity(Gravity.CENTER);
                        tvAcRate.setText("n/a");
                        tvAcRate.setTextColor(Color.GRAY);

                        // add annual change to row
                        tableRow.addView(tvAcRate);
                    }


                } else { // this is data, get the data based on given data type

                    DataEntry dataEntry = (DataEntry) reportEntryObj;
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

                    if (dataType == CommonUtils.DATA_TYPE_AVERAGE_PRICE || dataType == CommonUtils.DATA_TYPE_MEDIAN_PRICE) {
                        dataTv.setText(CommonUtils.formatPrice(reportEntryValue));
                    } else if (dataType == CommonUtils.DATA_TYPE_SPLP) {
                        dataTv.setText(reportEntryValue + "%");
                    } else {
                        dataTv.setText("" + reportEntryValue);
                    }
                    // add cell to the row
                    tableRow.addView(dataTv);

                    // if it is current year, then keep going to draw column 4 and 5
                    if (dataEntry.getYear() == Calendar.getInstance().get(Calendar.YEAR)) {

                        // prepare numbers
                        float Pc = 0.0f;
                        float Ac = 0.0f;

                        switch (dataType) {
                            case CommonUtils.DATA_TYPE_AVERAGE_PRICE:
                                Pc = dataEntry.getAveragePricePc();
                                Ac = dataEntry.getAveragePriceAc();
                                break;
                            case CommonUtils.DATA_TYPE_MEDIAN_PRICE:
                                Pc = dataEntry.getMedianPricePc();
                                Ac = dataEntry.getMedianPriceAc();
                                break;
                            case CommonUtils.DATA_TYPE_SALES:
                                Pc = dataEntry.getSalesPc();
                                Ac = dataEntry.getSalesAc();
                                break;
                            case CommonUtils.DATA_TYPE_NEW_LISTINGS:
                                Pc = dataEntry.getNewListingsPc();
                                Ac = dataEntry.getNewListingsAc();
                                break;
                            case CommonUtils.DATA_TYPE_ACTIVE_LISTINGS:
                                Pc = dataEntry.getActiveListingsPc();
                                Ac = dataEntry.getActiveListingsAc();
                                break;
                            case CommonUtils.DATA_TYPE_DOM:
                                Pc = dataEntry.getDomPc();
                                Ac = dataEntry.getDomAc();
                                break;
                            case CommonUtils.DATA_TYPE_SPLP:
                                Pc = dataEntry.getSplpPc();
                                Ac = dataEntry.getSplpAc();
                                break;
                        }
                        // format numbers to text
                        DecimalFormat df = new DecimalFormat("#");
                        df.setMinimumFractionDigits(2);
                        df.setRoundingMode(RoundingMode.CEILING);

                        // prepare monthly change cell
                        TextView tvPcRate = new TextView(this);
                        tvPcRate.setGravity(Gravity.CENTER);
                        if (Pc > 0) {
                            tvPcRate.setText("\u2191" + (df.format(Pc)) + "%");
                            tvPcRate.setTextColor(Color.rgb(39, 174, 96));
                        } else if (Pc == 0) {
                            tvPcRate.setText("-");
                            tvPcRate.setTextColor(Color.rgb(0, 0, 0));
                        } else {
                            tvPcRate.setText("\u2193" + (df.format(-1 * Pc)) + "%");
                            tvPcRate.setTextColor(Color.rgb(231, 76, 60));
                        }
                        // add monthly change rate
                        tableRow.addView(tvPcRate);

                        // prepare annual change cell
                        TextView tvAcRate = new TextView(this);
                        tvAcRate.setGravity(Gravity.CENTER);

                        if (Ac > 0) {
                            tvAcRate.setText("\u2191" + (df.format(Ac)) + "%");
                            tvAcRate.setTextColor(Color.rgb(39, 174, 96));
                        } else if (Ac == 0) {
                            tvAcRate.setText("-");
                            tvAcRate.setTextColor(Color.rgb(0, 0, 0));
                        } else {
                            tvAcRate.setText("\u2193" + (df.format(-1 * Ac)) + "%");
                            tvAcRate.setTextColor(Color.rgb(231, 76, 60));
                        }
                        // add annual change to row
                        tableRow.addView(tvAcRate);

                    }
                }
                columnIndex = columnIndex + 1;

            }
            // add a row
            tableLayout.addView(tableRow);
        }
    }


}
