package com.example.hung.myapplication.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.database.DataSource;
import com.example.hung.myapplication.gpsHandller.gpsHandler;
import com.example.hung.myapplication.models.TrackingService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.List;

public class TrackingMapView extends FragmentActivity implements OnMapReadyCallback {
    private int trackablId;
    private String trackableName;
    private DataSource dataSource;
    gpsHandler gpsHandler;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_map_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        dataSource = new DataSource(this);
        trackablId = getIntent().getIntExtra("TrackableID",-1);
        trackableName = getIntent().getStringExtra("TrackableName");
        gpsHandler = new gpsHandler(getApplicationContext());

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        dataSource.open();
        try {
            Location mLastLocation = gpsHandler.getLocation();
            if (mLastLocation != null) {
                double lat = mLastLocation.getLatitude();
                double lon = mLastLocation.getLongitude();
                LatLng mLatLon = new LatLng(lat, lon);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLon, 16));
                mMap.addMarker(new MarkerOptions()
                        .position(mLatLon)
                        .title("your location")
                );
                if(dataSource.checkDate(trackablId)) {
                    Toast.makeText(this, trackableName+" truck is being in service!!!", Toast.LENGTH_SHORT).show();
                    List<TrackingService.TrackingInfo> listlatlong = dataSource.locationOfTruck(trackablId);
                    for (TrackingService.TrackingInfo trackingInfo: listlatlong) {
                        LatLng newlatlng = new LatLng(trackingInfo.latitude,trackingInfo.longitude);
                        Marker truckMarker = mMap.addMarker(new MarkerOptions()
                                .position(newlatlng)
                                .title("Available at: " + DateFormat.getTimeInstance().format(trackingInfo.date)).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_food_truck))
                        );
                    }
                } else{
                    Toast.makeText(this, trackableName+" truck is out of service!!!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
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
}
