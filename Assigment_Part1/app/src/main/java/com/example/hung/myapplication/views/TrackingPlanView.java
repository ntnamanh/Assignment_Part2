package com.example.hung.myapplication.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.hung.myapplication.controllers.TrackingPlanController;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.models.TrackingPlan;
import com.example.hung.myapplication.R;

/**
 * @author Nguyen My
 */

public class TrackingPlanView extends AppCompatActivity {

    private  DataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_plan_activity);
        dataSource = new DataSource(this);
        TrackingPlan tracking_plan = TrackingPlan.getSingletonInstance(getApplicationContext());
        ListView list = (ListView) findViewById(R.id.listviewtrackplan);
        //set on item click listener for listview
        list.setOnItemClickListener(new TrackingPlanController(this,this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
