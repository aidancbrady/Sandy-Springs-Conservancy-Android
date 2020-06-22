package com.aidancbrady.sandyspringsconservancy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aidancbrady.sandyspringsconservancy.core.Constants;
import com.aidancbrady.sandyspringsconservancy.core.DataHandler;
import com.aidancbrady.sandyspringsconservancy.core.Park;
import com.aidancbrady.sandyspringsconservancy.core.Utilities;
import com.aidancbrady.sandyspringsconservancy.ui.ParkFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;

public class MenuActivity extends AppCompatActivity {

    public static final int LOCATION_PERMISSION_CODE = 99;
    private static final String LOG_TAG = "MenuActivity";

    private LocationManager locationManager;
    private GPSListener gpsListener = new GPSListener();

    private DrawerLayout drawerLayout;
    private boolean showMenu;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // set location manager & request permission
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkLocationPermission();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBarTitleMarquee(toolbar);
        drawerLayout = findViewById(R.id.activity_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_park_list)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Menu menu = navigationView.getMenu();
        SubMenu subMenu = menu.addSubMenu(R.string.menu_parks);
        for (int i = 0; i < DataHandler.parkList.size(); i++) {
            Park park = DataHandler.parkList.get(i);
            MenuItem menuItem = subMenu.add(park.getName());
            menuItem.setOnMenuItemClickListener(item -> {
                Bundle bundle = new Bundle();
                bundle.putString(ParkFragment.PARK_BUNDLE_TAG, park.getName());
                navController.navigate(R.id.nav_park, bundle);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                return true;
            });
        }
        navigationView.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!showMenu) {
                onBackPressed();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setMenuState(boolean showMenu) {
        this.showMenu = showMenu;
        if(!showMenu) {
            // Enables back button icon
            // passing null or 0 brings back the <- icon
            getSupportActionBar().setHomeAsUpIndicator(null);
        }
    }

    private void setupActionBarTitleMarquee(Toolbar toolbar) {
        try {
            Field field = toolbar.getClass().getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            TextView titleTextView = (TextView) field.get(toolbar);

            titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            titleTextView.setFocusable(true);
            titleTextView.setFocusableInTouchMode(true);
            titleTextView.requestFocus();
            titleTextView.setSingleLine(true);
            titleTextView.setSelected(true);
            titleTextView.setMarqueeRepeatLimit(-1);
        } catch (Exception e) {
            Utilities.error(LOG_TAG, "Couldn't reflectively set up title marquee effect.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onContactClicked(MenuItem item) {
        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + Constants.DEV_EMAIL));
        startActivity(intent);
    }

    public void onDonateClicked(MenuItem item) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.DONATE_SITE));
        startActivity(intent);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.gps_perm_title)
                        .setMessage(R.string.gps_perm_text)
                        .setPositiveButton(R.string.gps_perm_confirm, (dialogInterface, i) ->
                                ActivityCompat.requestPermissions(MenuActivity.this,
                                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_PERMISSION_CODE))
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,  Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(gpsListener);
        }
    }

    private static class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            DataHandler.lastLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    }
}