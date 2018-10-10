package com.example.hung.myapplication.views.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.hung.myapplication.views.TrackingPlanView;
import com.example.hung.myapplication.R;
import java.util.List;

/**
 * @author Nguyen My
 */

public class EventAdapter extends ArrayAdapter<String> {
    private int resource;
    private Context context;
    private int trackableId;
    private Dialog popup;
    private String date;
    private Activity activity;
    public EventAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, int trackableId, Activity activity) {
        super(context, resource, objects);
        this.context = context;
        this.resource= resource;
        this.trackableId = trackableId;
        this.activity = activity;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,@NonNull ViewGroup parent) {

        LayoutInflater Inflater = LayoutInflater.from(context);
        popup = new Dialog(context);
        convertView = Inflater.inflate(resource,parent,false);
        date = getItem(position);
        TextView txtDate = (TextView)convertView.findViewById(R.id.txtdate);
        ImageButton seelist = (ImageButton)convertView.findViewById(R.id.imageadd);
        txtDate.setText(date);
        seelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TrackingPlanView.class);
                intent.putExtra("date",date);
                intent.putExtra("trackableID",trackableId);
                context.startActivity(intent);
            }
        });
        convertView.setTag(date);
        return convertView;
    }

}
