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

public class PropertyTypeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    // property type lvWatchlist view
    ListView list = null;
    // property type key lvWatchlist
    List propertyTypeKeyList = null;
    // property type name
    Map<String, String> propertyTypeKeyNameMap = null;

    // for crash report
    private static final String TAG = "PropertyTypeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        super.onCreate(savedInstanceState);

        setTitle("Property Types");

        setContentView(R.layout.activity_property_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final String selectedRegionKey = getIntent().getStringExtra(CommonUtils.INTENT_EXTRA_KEY_REGION_KEY);
        final String selectedRegionName = getIntent().getStringExtra(CommonUtils.INTENT_EXTRA_KEY_REGION_NAME);


        DatabaseReference fbdbRef = DatabaseManager.getDatabaseReference();
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                propertyTypeKeyList = new ArrayList();
                propertyTypeKeyNameMap = new HashMap<String, String>();

                for (DataSnapshot cityChild : dataSnapshot.child("property_type").getChildren()) {

                    String propertyTypeKey = (String) cityChild.child("key").getValue();
                    String propertyTypeName = (String) cityChild.child("name").getValue();

                    propertyTypeKeyList.add(propertyTypeKey);
                    propertyTypeKeyNameMap.put(propertyTypeKey, propertyTypeName);
                }

                CustomListAdapter adapter = new CustomListAdapter(PropertyTypeActivity.this, propertyTypeKeyList, propertyTypeKeyNameMap);
                list = (ListView) findViewById(R.id.list);

                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        fbdbRef.addValueEventListener(valueEventListener);

        list = (ListView) findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {

                    String propertyTypeKey = (String) propertyTypeKeyList.get(position);
                    String propertyTypeName = propertyTypeKeyNameMap.get(propertyTypeKey);

                    String reportKey = CommonUtils.getReportKey(selectedRegionKey, propertyTypeKey);
                    String reportTitle = CommonUtils.getRegionNamePropertyName(selectedRegionName, propertyTypeName);

                    SharedPreferences settings = getSharedPreferences(CommonUtils.WATCH_LIST_PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(reportKey, reportTitle);
                    editor.commit();

                    Intent intent = new Intent(PropertyTypeActivity.this, WatchlistActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(intent);

                } catch (Exception ex) {

                    FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
                    throw ex;
                }

            }
        });

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
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_share) {
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
            Intent intent = new Intent(PropertyTypeActivity.this, BigPictureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_watchlist) {
            Intent intent = new Intent(PropertyTypeActivity.this, WatchlistActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(PropertyTypeActivity.this, NewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_service_terms) {
            Intent intent = new Intent(PropertyTypeActivity.this, ServiceTermsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class CustomListAdapter extends ArrayAdapter<String> {

        private Activity context;
        private List<String> properTypeKeyList;
        private Map<String, String> propertyTypeKeyNameMap;

        public CustomListAdapter(Activity context, List<String> properTypeKeyList, Map<String, String> propertyTypeKeyNameMap) {
            super(context, R.layout.common_list_row, properTypeKeyList);

            this.context = context;
            this.properTypeKeyList = properTypeKeyList;
            this.propertyTypeKeyNameMap = propertyTypeKeyNameMap;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.common_list_row, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.common_list_row_text);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.common_list_row_icon);

            String propertyTypeKey = (String) properTypeKeyList.get(position);
            String propertyName = propertyTypeKeyNameMap.get(propertyTypeKey);

            txtTitle.setText(propertyName);

            imageView.setImageResource(CommonUtils.getPropertyTypeIcon(propertyTypeKey));

            return rowView;

        }
    }
}
