package ca.realsense.realsensegtadev.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.realsense.realsensegtadev.R;
import ca.realsense.realsensegtadev.util.CommonUtils;

public class WatchlistActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // for crash report
    private static final String TAG = "WatchlistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_watchlist);
        setTitle("Watchlist");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Map<String, String> watchListMap = new HashMap<String, String>();
        final ArrayList<String> selectedKeys = new ArrayList<String>();

        // get saved watchlist from preference
        SharedPreferences savedWatchlist = getSharedPreferences(CommonUtils.WATCH_LIST_PREFS_NAME, 0);

        final TextView message = (TextView) findViewById(R.id.tv_watchlist_message);

        int listSize = savedWatchlist.getAll().size();

        if (listSize == 0) { // user has not added any item to the wathclist
            message.setText("Please add watch item");
            //Toast.makeText(WatchlistActivity.this, "Please add watch item", Toast.LENGTH_LONG).show();

        } else { // user has items in the watchlist
            String tapToViewReport = "Tap to view report.";
            String selectToCompare = " Select checkboxes to compare.";

            if(listSize == 1){
                message.setText(tapToViewReport);
            }else{
                message.setText(tapToViewReport + selectToCompare);
            }

            for (Map.Entry entry : savedWatchlist.getAll().entrySet()) {
                watchListMap.put(entry.getKey().toString(), entry.getValue().toString());
            }

            final List<String> reportKeyList = new ArrayList();

            reportKeyList.addAll(watchListMap.keySet());

            final ListView watchlistView = (ListView) findViewById(R.id.lv_report_list);

            WatchListAdapter adapter = new WatchListAdapter(WatchlistActivity.this, reportKeyList, watchListMap, selectedKeys);

            watchlistView.setAdapter(adapter);

        }


        // buttons
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchlistActivity.this, RegionListActivity.class);
                startActivity(intent);
            }

        });


        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != selectedKeys && selectedKeys.size() > 0) {

                    SharedPreferences settings = getSharedPreferences(CommonUtils.WATCH_LIST_PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();

                    for (String reportKey : selectedKeys) {
                        editor.remove(reportKey);
                    }

                    editor.commit();
                    finish();
                    Intent intent = new Intent(WatchlistActivity.this, WatchlistActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);


                } else {
                    Toast.makeText(WatchlistActivity.this, "Please select at least 1 watch item to remove", Toast.LENGTH_LONG).show();
                }
            }

        });


        Button btnComp = (Button) findViewById(R.id.btn_go_comp);
        btnComp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (null != selectedKeys && selectedKeys.size() > 1) {
                    if (selectedKeys.size() > 3) {
                        Toast.makeText(WatchlistActivity.this, "Please select up to 3 watch items to compare", Toast.LENGTH_LONG).show();

                    } else {
                        Intent intent = new Intent(WatchlistActivity.this, ComparisonActivity.class);
                        intent.putStringArrayListExtra(CommonUtils.INTENT_EXTRA_KEY_REPORT_KEYS, selectedKeys);

                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(WatchlistActivity.this, "Please select at least 2 watch items to compare", Toast.LENGTH_LONG).show();
                }


            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_big_pic) {
            Intent intent = new Intent(WatchlistActivity.this, BigPictureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_watchlist) {

        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(WatchlistActivity.this, NewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_service_terms) {
            Intent intent = new Intent(WatchlistActivity.this, ServiceTermsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        finish();

        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();

            Intent intent = new Intent(WatchlistActivity.this, BigPictureActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(intent);

        }
    }


    public class WatchListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private Map<String, String> watchListMap;
        private List<String> reportKeyList;
        private ArrayList<String> selectedKeys;

        public WatchListAdapter(Activity context, List<String> reportKeyList, Map<String, String> watchListMap, ArrayList<String> selectedKeys) {

            super(context, R.layout.watch_list_row, reportKeyList);
            this.context = context;
            this.reportKeyList = reportKeyList;

            this.watchListMap = watchListMap;
            this.selectedKeys = selectedKeys;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.watch_list_row, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
            final String reportKey = (String) reportKeyList.get(position);
            String reportName = watchListMap.get(reportKey);

            txtTitle.setText(reportName);

            txtTitle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(WatchlistActivity.this, SingleReportActivity.class);
                    intent.putExtra(CommonUtils.INTENT_EXTRA_KEY_ACTION, CommonUtils.INTENT_EXTRA_VALUE_SINGLE);
                    intent.putExtra(CommonUtils.INTENT_EXTRA_KEY_SINGLE_REPORT_KEY, reportKey);
                    startActivity(intent);
                }

            });


            Button btnViewReport = (Button) rowView.findViewById(R.id.btn_view_report);
            btnViewReport.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(WatchlistActivity.this, SingleReportActivity.class);
                    intent.putExtra(CommonUtils.INTENT_EXTRA_KEY_ACTION, CommonUtils.INTENT_EXTRA_VALUE_SINGLE);
                    intent.putExtra(CommonUtils.INTENT_EXTRA_KEY_SINGLE_REPORT_KEY, reportKey);
                    startActivity(intent);
                }

            });


            CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.row_checkbox);
            checkBox.setChecked(false);
            //checkBoxes.add(checkBox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedKeys.add(reportKey);
                    } else {
                        selectedKeys.remove(reportKey);
                    }

                }
            });


            return rowView;

        }


    }
}

