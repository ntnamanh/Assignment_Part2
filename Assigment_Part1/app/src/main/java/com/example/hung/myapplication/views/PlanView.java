package com.example.hung.myapplication.views;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.example.hung.myapplication.controllers.InsertTrackingPlanController;
import com.example.hung.myapplication.controllers.EditPlanController;
import com.example.hung.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * @author Nguyen My
 */

public class PlanView extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_view_activity);
        String status;
        status = getIntent().getStringExtra("status");
        Button btnAdd = findViewById(R.id.btnadd_plan);
        TextView txtTimePlan = (TextView) findViewById(R.id.txttimeplan);
        if(status.equals("insert")) {
            txtTimePlan.setOnClickListener(new InsertTrackingPlanController(this,this));
            btnAdd.setOnClickListener(new InsertTrackingPlanController(this,this));

        } else {
            txtTimePlan.setOnClickListener(new EditPlanController(this,this));
            btnAdd.setOnClickListener(new EditPlanController(this,this));
        }
    }
}
