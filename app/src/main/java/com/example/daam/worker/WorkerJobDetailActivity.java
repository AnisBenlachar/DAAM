package com.example.daam.worker;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daam.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class WorkerJobDetailActivity extends AppCompatActivity {

    private MapView mapView;
    private Button btnStartJob, btnCompleteJob;
    private boolean isJobStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize OSMDroid
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_worker_job_detail);

        initializeViews();
        setupMap();

        // Populate data from Intent
        String clientName = getIntent().getStringExtra("CLIENT_NAME");
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        String address = getIntent().getStringExtra("CLIENT_ADDRESS");

        if (clientName != null)
            ((TextView) findViewById(R.id.tvDetailClientName)).setText(clientName);
        if (productName != null)
            ((TextView) findViewById(R.id.tvDetailProduct)).setText(productName);
        if (address != null)
            ((TextView) findViewById(R.id.tvDetailAddress)).setText(address);
    }

    private void initializeViews() {
        mapView = findViewById(R.id.mapView);
        btnStartJob = findViewById(R.id.btnStartJob);
        btnCompleteJob = findViewById(R.id.btnCompleteJob);
        ImageView ivBack = findViewById(R.id.ivBack);

        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        btnStartJob.setOnClickListener(v -> {
            isJobStarted = true;
            btnStartJob.setVisibility(View.GONE);
            btnCompleteJob.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Job Started", Toast.LENGTH_SHORT).show();
        });

        btnCompleteJob.setOnClickListener(v -> {
            Toast.makeText(this, "Job Completed Successfully!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        // Dummy location for "123 Solar Blvd"
        GeoPoint startPoint = new GeoPoint(36.7525, 3.0420); // Algiers coordinates as placeholder
        mapView.getController().setCenter(startPoint);

        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Client Location");
        mapView.getOverlays().add(marker);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }
}
