package com.example.hung.myapplication.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.database.DataSource;

/**
 * @author Nguyen My
 */

public class MainActivity extends AppCompatActivity {

    DataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }
    @Override
    protected void onStart() {
        super.onStart();
        dataSource = new DataSource(this);
        dataSource.open();
        Toast.makeText(this, "Database acquired", Toast.LENGTH_SHORT).show();
        dataSource.seedTrackableData();
        dataSource.seedTrackingeData();
        dataSource.seedPlanData();
        this.finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        dataSource.close();
        Intent intent = new Intent(this,TrackableView.class);
        this.startActivity(intent);

    }

}
