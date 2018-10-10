package com.example.hung.myapplication.httphandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.hung.myapplication.models.DurationAndDistance;
import com.example.hung.myapplication.models.TrackingService.TrackingInfo;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nguyen My
 */

public class GetJsonFile extends AsyncTask<List<String>, Void, List<String>> {

    private final String KEY = "AIzaSyAqxzDieVS-x1H3QxaOUJuBfH02YSmmgYo";
    private TrackingInfo trackingInfo;
    private String lat,lon;
    private DurationAndDistance durationAndDistance;
    private Context context;

    public GetJsonFile(TrackingInfo trackingInfoList, Context context,String lat,String lon) {
        this.trackingInfo = trackingInfoList;
        this.context = context;
        this.durationAndDistance = DurationAndDistance.getSingletonInstance(this.context);
        this.lat = lat;
        this.lon = lon;
    }

    public GetJsonFile() {

    }

    @Override
    protected List<String> doInBackground(List<String> ... s) {
        HttpURLConnection con=null;
        String duration;
        String distance;
        List<String> list = new ArrayList<>();

        try {
                //read distance matric by http request to get json data from googleapi
                URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+this.lat+","+this.lon+"&destinations="
                        + String.valueOf(trackingInfo.getLatitude()) + "," + String.valueOf(trackingInfo.getLongitude())
                        + "&key="+KEY);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statuscode = con.getResponseCode();
                if (statuscode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = br.readLine();
                    }
                    JSONObject obj = null;
                    obj = new JSONObject(sb.toString());
                    Log.d("ahai", obj.toString());
                    duration = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").get("text").toString();
                    distance = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").get("text").toString();
                    //store json data to the object
                    durationAndDistance.addToDurationList(trackingInfo,duration,distance);
                    list.add(duration);
                    list.add(distance);
                }
            }
             catch (MalformedURLException e) {
                Log.d("error", "error1");
            } catch (IOException e) {
                Log.d("error", "error2");
            }catch (JSONException e) {
                e.printStackTrace();
            }finally {
                con.disconnect();
            }

        return list;
    }

    @Override
    protected void onPostExecute(List<String> text) {
        super.onPostExecute(text);
    }

}

