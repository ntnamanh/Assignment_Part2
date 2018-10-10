package com.example.hung.myapplication.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.views.adapters.EventAdapter;
import com.example.hung.myapplication.views.adapters.TrackingAdaper;
import com.example.hung.myapplication.views.PlanView;
import com.example.hung.myapplication.models.TrackingService;
import com.example.hung.myapplication.R;
import java.util.List;

/**
 * @author Nguyen My
 */


public class EventsController implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private int trackableId;
    private Context context;
    private Activity activity;
    private TrackingService trackingService;
    private DataSource dataSource;
    private ListView trackingList;
    private Spinner dropDownListDate;
    private TrackingAdaper adapter;
    private boolean check;
    private Thread thread;


    public EventsController(){

    }

    public EventsController(Context context, Activity activity){
        this.context = context;
        this.dataSource = new DataSource(context);
        this.trackingService = TrackingService.getSingletonInstance(context);
        this.activity = activity;
        this.trackableId = this.activity.getIntent().getIntExtra("TrackableID", -1);;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                R.layout.support_simple_spinner_dropdown_item,
                dataSource.retreiveListDateByTrackableId(this.trackableId));
        this.dropDownListDate = (Spinner)this.activity.findViewById(R.id.datespinner);
        this.dropDownListDate.setAdapter(arrayAdapter);
        this.trackingList = (ListView)this.activity.findViewById(R.id.trackinglist);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TrackingService.TrackingInfo trackingItem = (TrackingService.TrackingInfo) view.getTag();
        int trackingId = dataSource.getTrackingIdByObject(trackingItem);
        Intent intent = new Intent(context, PlanView.class);
        intent.putExtra("trackingId",trackingId);
        intent.putExtra("status","insert");
        context.startActivity(intent);

    }

    @Override
    public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
        String selection_spinner = dropDownListDate.getItemAtPosition(position).toString();

        if(selection_spinner.equals("All Events")){
            check = false;
            List<String> listDate = dataSource.retreiveListDateByTrackableId(this.trackableId);
            listDate.remove(0);
            if(listDate.isEmpty()){
                Toast.makeText(context, "No event for this food truck.", Toast.LENGTH_SHORT).show();
            }
            else {
                EventAdapter eventAdapter = new EventAdapter(context, R.layout.event_item_layout, listDate, trackableId, activity);
                trackingList.setAdapter(eventAdapter);
            }

        }
        else{
            Toast.makeText(context, "Select any schedule of the food truck to make your tracking plan.", Toast.LENGTH_SHORT).show();
            if(!Functions.check_date(selection_spinner)){
                check = false;
                adapter = new TrackingAdaper(context,R.layout.item_tracking_layout,dataSource.getEventById(parent.getItemAtPosition(position).toString(),trackableId));
                trackingList.setAdapter(adapter);
            }
            else{
                check = true;
                adapter = new TrackingAdaper(context,R.layout.item_tracking_layout,dataSource.getEventById(parent.getItemAtPosition(position).toString(),trackableId));
                trackingList.setAdapter(adapter);
                thread = new Thread(){
                    @Override
                    public void run(){
                        while(!isInterrupted()){
                            try {
                                Thread.sleep(1000);

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(check){
                                            adapter = new TrackingAdaper(context,R.layout.item_tracking_layout,dataSource.getEventById(parent.getItemAtPosition(position).toString(),trackableId));
                                            trackingList.setAdapter(adapter);
                                        }
                                        else{
                                            thread.interrupt();
                                        }
                                    }
                                });

                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
