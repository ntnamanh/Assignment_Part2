package com.example.hung.myapplication.controllers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.models.TrackingPlan;
import com.example.hung.myapplication.views.adapters.plan_adapter;

/**
 * @author Nguyen My
 */


public class TrackingPlanController implements OnItemClickListener {
    private Context context;
    private Dialog dialog;
    private Activity activity;

    public TrackingPlanController(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        this.dialog = new Dialog(context);
        DataSource dataSource = new DataSource(context);
        dataSource.open();
        int trackableID = this.activity.getIntent().getIntExtra("trackableID", -1);
        String date = this.activity.getIntent().getStringExtra("date");
        //initial plan_adapter
        plan_adapter planAdapter = new plan_adapter(activity,R.layout.item_plan_layout, dataSource.getListTrackingById(trackableID, date));

        ListView list = (ListView) activity.findViewById(R.id.listviewtrackplan);
        list.setAdapter(planAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TrackingPlan.planInformation trackingItem = (TrackingPlan.planInformation) view.getTag();
        String planId = trackingItem.planId;
        Functions.popUpUpdateDelete(context,dialog,activity,planId);
    }


}
