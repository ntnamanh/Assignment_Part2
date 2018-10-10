package com.example.hung.myapplication.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.hung.myapplication.models.TrackingPlan;
import com.example.hung.myapplication.R;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Nguyen My
 */

public class plan_adapter extends ArrayAdapter<TrackingPlan.planInformation>
{
    private Context context;
    private List<TrackingPlan.planInformation> trackingitem;
    int resource;
    public plan_adapter(@NonNull Context context, int resource, @NonNull List<TrackingPlan.planInformation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        trackingitem = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);

        int id = Objects.requireNonNull(getItem(position)).trackableId;
        String title = Objects.requireNonNull(getItem(position)).title;
        Date targetStr = Objects.requireNonNull(getItem(position)).targetStr;
        Date targetEnd = Objects.requireNonNull(getItem(position)).targetEnd;
        Date meetTime =  Objects.requireNonNull(getItem(position)).meetTime;
        String currentLoca =  Objects.requireNonNull(getItem(position)).currentLoca;
        String meetLoca =  Objects.requireNonNull(getItem(position)).meetLoca;

        LayoutInflater inflater =  LayoutInflater.from(context);

        convertView = inflater.inflate(resource,parent,false);

        TextView txtTrackableId = (TextView) convertView.findViewById(R.id.txttrid);
        txtTrackableId.setText(String.valueOf(id));

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        txtTitle.setText(title);

        TextView txtTargetStr = (TextView) convertView.findViewById(R.id.txttargetstarttime);
        txtTargetStr.setText(dateFormat.format(targetStr));

        TextView txtTargetEnd = (TextView) convertView.findViewById(R.id.txttargetendtime);
        txtTargetEnd.setText(dateFormat.format(targetEnd));

        TextView txtMeetTime = (TextView) convertView.findViewById(R.id.txtmeettime);
        txtMeetTime.setText(dateFormat.format(meetTime));

        TextView txtCurrentLoca = (TextView) convertView.findViewById(R.id.txtcurrentl);
        txtCurrentLoca.setText(currentLoca);

        TextView txtMeetLoca = (TextView) convertView.findViewById(R.id.txtmeetl);
        txtMeetLoca.setText(meetLoca);

        convertView.setTag(getItem(position));
        return convertView;

    }
}
