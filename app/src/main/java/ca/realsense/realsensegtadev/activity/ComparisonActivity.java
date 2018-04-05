package ca.realsense.realsensegtadev.activity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.realsense.realsensegtadev.R;
import ca.realsense.realsensegtadev.data.DataService;
import ca.realsense.realsensegtadev.data.DatabaseManager;
import ca.realsense.realsensegtadev.entity.DataEntry;
import ca.realsense.realsensegtadev.entity.DataEntryCollection;
import ca.realsense.realsensegtadev.util.CommonUtils;

/**
 * Created by joewang on 2017-08-31.
 */

public class ComparisonActivity extends ReportActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<String> reportKeyList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize the screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Comparison");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();

        String action = intent.getStringExtra(CommonUtils.INTENT_EXTRA_KEY_ACTION);

        if (CommonUtils.INTENT_EXTRA_VALUE_SINGLE.equalsIgnoreCase(action)) {
            reportKeyList = new ArrayList(1);
            reportKeyList.add(intent.getStringExtra(CommonUtils.INTENT_EXTRA_KEY_SINGLE_REPORT_KEY));

        } else {
            reportKeyList = intent.getStringArrayListExtra(CommonUtils.INTENT_EXTRA_KEY_REPORT_KEYS);
        }

        DatabaseReference fbdbRef = DatabaseManager.getDatabaseReference();

        ValueEventListener valueEventListener = new ValueEventListener() {

            // if data changes on remote real-time database, the allReportData would be changed as well
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //List<DataEntryCollection>
                List<DataEntryCollection> dataEntryCollectionList = DataService.getInstance().retrieveChartData(dataSnapshot, reportKeyList, CommonUtils.yearsToReport);

                // generate report
                generateReport(dataEntryCollectionList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // add listener to the database
        fbdbRef.addValueEventListener(valueEventListener);

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
            Intent intent = new Intent(ComparisonActivity.this, BigPictureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_watchlist) {
            Intent intent = new Intent(ComparisonActivity.this, WatchlistActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(ComparisonActivity.this, NewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_service_terms) {
            Intent intent = new Intent(ComparisonActivity.this, ServiceTermsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected List<String> getXAxisLabels(DataEntryCollection dataEntryCollectionList) {
        List<String> monthNameList = new ArrayList<String>();
        Set<Integer> dataEntryKeySet = dataEntryCollectionList.getDataEntryMap().keySet();
        for (Integer dataEntryKey : dataEntryKeySet) {
            monthNameList.add(dataEntryKey.toString());
        }
        return monthNameList;
    }

    @Override
    protected String getLegendText(DataEntryCollection dataEntryCollection) {
        return dataEntryCollection.getAreaName();
    }

    @Override
    protected String getReportTitle(List<DataEntryCollection> dataEntryCollectionList) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        for(DataEntryCollection dataEntryCollection : dataEntryCollectionList){
            sb.append(dataEntryCollection.getPropertyType());
            sb.append(" in ");
            sb.append(dataEntryCollection.getAreaName());
            sb.append("\n");
        }
        return sb.toString();
    }

    protected void drawTable(int dataType, List<DataEntryCollection> dataEntryCollectionList, List<String> rowNameList) {

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

        tableLayout.removeAllViews();

        // draw header
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundResource(R.drawable.backtext);
        // first cell
        TextView headerRowTv = new TextView(this);
        headerRowTv.setText("");
        headerRowTv.setGravity(Gravity.CENTER);
        headerRow.addView(headerRowTv);
        // loop through the collection

        for (DataEntryCollection dataEntryCollection : dataEntryCollectionList) {

            TextView headerRowTv1 = new TextView(this);
            headerRowTv1.setText(dataEntryCollection.getAreaName() + "\n" + dataEntryCollection.getPropertyType());
            headerRowTv1.setGravity(Gravity.CENTER);
            headerRow.addView(headerRowTv1);
        }

        // add header
        tableLayout.addView(headerRow);


        int timeWindowIndex = 0;

        for (String timeKey : rowNameList) {

            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundResource(R.drawable.backtext);

            TextView tv = new TextView(this);

            tv.setText(CommonUtils.getMonthYear(timeKey));
            tv.setPadding(0, 0, 0, 0);
            tv.setGravity(Gravity.CENTER);
            tableRow.addView(tv);

            timeWindowIndex = timeWindowIndex + 1;

            int index = 0;
            for (DataEntryCollection dataEntryCollection : dataEntryCollectionList) {

                Map reportEntryMap = dataEntryCollection.getDataEntryMap();
                DataEntry dataEntry = (DataEntry) reportEntryMap.get(Integer.valueOf(timeKey));

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
                }

                TextView tvSales = new TextView(this);

                if (dataType == CommonUtils.DATA_TYPE_AVERAGE_PRICE || dataType == CommonUtils.DATA_TYPE_MEDIAN_PRICE) {
                    tvSales.setText(CommonUtils.formatPrice(reportEntryValue));

                } else {
                    tvSales.setText("" + reportEntryValue);

                }

                tvSales.setTextColor(CommonUtils.COMP_COLORS[index]);
                tvSales.setPadding(0, 0, 0, 0);
                tvSales.setGravity(Gravity.CENTER);
                tableRow.addView(tvSales);

                index = index + 1;

            }
            tableLayout.addView(tableRow);


        }
    }


}
