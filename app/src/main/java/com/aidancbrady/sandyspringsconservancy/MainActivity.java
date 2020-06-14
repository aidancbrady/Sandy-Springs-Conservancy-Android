package com.aidancbrady.sandyspringsconservancy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aidancbrady.sandyspringsconservancy.core.DataCache;
import com.aidancbrady.sandyspringsconservancy.core.DataHandler;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // use handler to run callback on main thread
        Handler handler = new Handler(getMainLooper());
        new Thread(() -> {
            boolean ret = new DataHandler(this).loadData();
            handler.post(() -> callback(ret));
        }).start();
    }

    private void callback(boolean success) {
        ProgressBar progress = findViewById(R.id.progressBar);
        TextView view = findViewById(R.id.textView);
        progress.setVisibility(View.GONE);
        if (success) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            view.setText(R.string.park_list_download_fail);
        }
    }

    public void onClick(View view) {
        new Thread(() -> {
            new DataHandler(this).loadData();
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }).start();
    }
}