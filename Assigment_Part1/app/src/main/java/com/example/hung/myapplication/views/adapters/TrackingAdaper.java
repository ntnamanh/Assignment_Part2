package com.example.hung.myapplication.views.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hung.myapplication.controllers.Functions;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.models.TrackingService;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * @author Nguyen My
 */

public class TrackingAdaper extends ArrayAdapter<TrackingService.TrackingInfo> {
    private Context context;
    private int resource;


    public TrackingAdaper(@NonNull Context context, int resource, @NonNull List<TrackingService.TrackingInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        int ID = Objects.requireNonNull(getItem(position)).trackableId;
        Date date = Objects.requireNonNull(getItem(position)).date;
        String StringDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM).format(date);
        String latitude = String.valueOf(Objects.requireNonNull(getItem(position)).latitude);
        String longtitude = String.valueOf(Objects.requireNonNull(Objects.requireNonNull(getItem(position)).longitude));
        String StopTime =String.valueOf(Objects.requireNonNull(getItem(position)).stopTime);
        LayoutInflater inflater =  LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView txtDate = convertView.findViewById(R.id.txtxdate);
        TextView txtstoptime = convertView.findViewById(R.id.txtstoptime);
        TextView txttimestop = convertView.findViewById(R.id.txttimestop);
        ImageView image = convertView.findViewById(R.id.imagetruck);
        TextView txtaddress =  convertView.findViewById(R.id.txtaddress);
        long stoped_time = Functions.monitorFoodTruck(date,Integer.parseInt(StopTime));
        if(Functions.check_availabledate(date)){
            image.setImageResource(R.drawable.comingsoon);
        }
        if(stoped_time > 0){
            image.setImageResource(R.drawable.icon_food_truck);
            txttimestop.setText("Stoped for: "+stoped_time +" mins");
        }
        else{
            txttimestop.setText("Status: Left");
        }
        txtDate.setText("Date: "+StringDate);
        txtstoptime.setText("Stop Time: "+StopTime + " mins");
        txtaddress.setText("Latitude: " + latitude + "  " + "Longtitude: "+longtitude);
        convertView.setTag(getItem(position));
        return convertView;

    }
}
