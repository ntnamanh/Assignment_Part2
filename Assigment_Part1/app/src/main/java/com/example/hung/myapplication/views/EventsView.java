package com.example.hung.myapplication.views;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;
import com.example.hung.myapplication.controllers.EventsController;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.models.TrackingService;
import com.example.hung.myapplication.R;

/**
 * @author: Nguyen My
 */

public class EventsView extends AppCompatActivity {
    private DataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_view_activity);
        dataSource = new DataSource(this);
        TrackingService trackingService = TrackingService.getSingletonInstance(getApplicationContext());
        ListView listEvents = (ListView) findViewById(R.id.trackinglist);
        Spinner dropDownList = (Spinner) findViewById(R.id.datespinner);
        dropDownList.setOnItemSelectedListener(new EventsController(this,this));
        listEvents.setOnItemClickListener(new EventsController(this,this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();

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
    protected void onStop() {
        super.onStop();
        dataSource.close();
    }
}
