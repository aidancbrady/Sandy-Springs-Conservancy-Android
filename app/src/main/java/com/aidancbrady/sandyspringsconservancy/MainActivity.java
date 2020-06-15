package com.aidancbrady.sandyspringsconservancy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aidancbrady.sandyspringsconservancy.core.DataHandler;
import com.aidancbrady.sandyspringsconservancy.core.DataLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // use handler to run callback on main thread
        Handler handler = new Handler(getMainLooper());
        new Thread(() -> {
            boolean ret = new DataLoader(this).loadData();
            handler.post(() -> callback(ret));
        }).start();
    }

    private void callback(boolean success) {
        ProgressBar progress = findViewById(R.id.progressBar);
        TextView view = findViewById(R.id.textView);
        progress.setVisibility(View.GONE);
        if (success) {
            DataHandler.loadFavorites(this);
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            view.setText(R.string.park_list_download_fail);
        }
    }
}