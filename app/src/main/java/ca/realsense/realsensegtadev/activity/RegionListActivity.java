package ca.realsense.realsensegtadev.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.realsense.realsensegtadev.R;
import ca.realsense.realsensegtadev.data.DatabaseManager;
import ca.realsense.realsensegtadev.util.CommonUtils;

/**
 * Created by joewang on 2017-08-31.
 */

public class RegionListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    // model of the lvWatchlist view
    List<String> regionKeyList = null;
    Map<String, String> regionKeyNameMap = null;

    // for crash report
    private static final String TAG = "RegionListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setTitle("Areas");

        setContentView(R.layout.activity_region_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // property type lvWatchlist view
        final ListView regionListView = (ListView) findViewById(R.id.lv_region);

        // selected region key from previous region step, it is used to retrieve sub regions
        final String selectedRegionKey = getIntent().getStringExtra(CommonUtils.INTENT_EXTRA_KEY_REGION_KEY);

        String currentRegionKey = null;
        if (null == selectedRegionKey) {
            currentRegionKey = CommonUtils.TOP_REGION;
        } else {
            currentRegionKey = selectedRegionKey;
        }

        final String mRegionKey = currentRegionKey;
        // Database
        DatabaseReference fbdbRef = DatabaseManager.getDatabaseReference();

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                regionKeyList = new ArrayList();
                regionKeyNameMap = new HashMap<String, String>();

                for (DataSnapshot subRegion : dataSnapshot.child(mRegionKey).getChildren()) {

                    final String regionKey = (String) subRegion.child("key").getValue();
                    String regionName = (String) subRegion.child("name").getValue();

                    regionKeyList.add(regionKey);
                    regionKeyNameMap.put(regionKey, regionName);

                }

                RegionListAdapter adapter = new RegionListAdapter(RegionListActivity.this, regionKeyList);

                regionListView.setAdapter(adapter);

                regionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        try {

                            String regionKey = (String) regionKeyList.get(position);
                            String regionName = (String) regionKeyNameMap.get(regionKey);

                            Intent intent;

                            if (dataSnapshot.child(regionKey).exists()) {
                                // if there is more sub region to go, go get more sub regions
                                intent = new Intent(RegionListActivity.this, RegionListActivity.class);

                            } else {
                                // if there is no more sub region, go to property type selection
                                intent = new Intent(RegionListActivity.this, PropertyTypeActivity.class);

                            }

                            // specify the selected region key
                            intent.putExtra(CommonUtils.INTENT_EXTRA_KEY_REGION_KEY, regionKey);
                            intent.putExtra(CommonUtils.INTENT_EXTRA_KEY_REGION_NAME, regionName);

                            startActivity(intent);

                        } catch (Exception ex) {

                            FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
                            throw ex;
                        }

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        fbdbRef.addValueEventListener(valueEventListener);


    }


    public class RegionListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final List<String> itemKeys;

        public RegionListAdapter(Activity context, List<String> itemKeys) {
            super(context, R.layout.common_list_row,  itemKeys);

            this.context = context;
            this.itemKeys = itemKeys;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.common_list_row, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.common_list_row_text);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.common_list_row_icon);

            String regionKey = (String) itemKeys.get(position);
            String propertyName = regionKeyNameMap.get(regionKey);
            txtTitle.setText(propertyName);

            imageView.setImageResource(R.drawable.realsenselogo);

            return rowView;

        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        //finish();
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
            super.onBackPressed();
        }
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
            Intent intent = new Intent(RegionListActivity.this, BigPictureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_watchlist) {
            Intent intent = new Intent(RegionListActivity.this, WatchlistActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(RegionListActivity.this, NewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_service_terms) {
            Intent intent = new Intent(RegionListActivity.this, ServiceTermsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startOver() {
        Intent intent = new Intent(this, RegionListActivity.class);
        intent.putExtra(CommonUtils.INTENT_EXTRA_KEY_REGION_KEY, CommonUtils.TOP_REGION);
        startActivity(intent);
    }


}