package com.aidancbrady.sandyspringsconservancy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aidancbrady.sandyspringsconservancy.core.DataHandler;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        new Thread(() -> new DataHandler(this).loadData()).start();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}