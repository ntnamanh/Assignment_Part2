package com.example.hung.myapplication.views;

import android.Manifest;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.hung.myapplication.ConnectionHandler.ConnectionReceiver;
import com.example.hung.myapplication.controllers.TrackableController;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.gpsHandller.gpsHandler;

/**
 * @author Nguyen My
 */

public class TrackableView extends AppCompatActivity {

    private ConnectionReceiver connectionReceiver = new ConnectionReceiver();
    private Spinner dropDownList;
    private String lat,lon;
    private gpsHandler gps;
    DataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackable_view_activity);
        connectionReceiver = new ConnectionReceiver();
        dataSource = new DataSource(this);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        gps = new gpsHandler(getApplicationContext());
        gps.getLocation();
        dropDownList = (Spinner) findViewById(R.id.category_spinner);
        dropDownList.setOnItemSelectedListener(new TrackableController(this,this));
        ImageButton btnSuggest = (ImageButton)findViewById(R.id.btnSuggest);
        btnSuggest.setOnClickListener(new TrackableController(this,this));
        ListView trackableList = (ListView) findViewById(R.id.listviewtrackable);
        trackableList.setOnItemClickListener(new TrackableController(this,this));
        Toast.makeText(this, "Please, choose the brand you want to track.", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataSource.close();
        unregisterReceiver(connectionReceiver);
    }
}
