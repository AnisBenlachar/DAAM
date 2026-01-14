package com.example.daam;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.navigation.NavigationView;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.location.Location;
import androidx.annotation.NonNull;
import android.os.AsyncTask;
import android.graphics.Color;
import java.util.ArrayList;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.views.overlay.MapEventsOverlay;

public class activity_stores extends AppCompatActivity {
    // UI Elements
    private Button btnMap, btnProducts;
    // ✅ Location Card UI
    private CardView cardLocation;
    private TextView txtLatitude, txtLongitude;
    private ImageView btnCloseLocation;

    private FrameLayout mapLayout;
    private android.widget.LinearLayout productsLayout;
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;
    private static final int REQUEST_PERMISSIONS = 1;

    // Routing fields
    private RoadManager roadManager;
    private Polyline roadOverlay;
    private Marker destinationMarker;

    private androidx.recyclerview.widget.RecyclerView rvProducts;
    private com.example.daam.adapter.ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        // ✅ Initialize Location Card Views
        cardLocation = findViewById(R.id.cardLocation);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        btnCloseLocation = findViewById(R.id.btnCloseLocation);

        // ✅ Close button logic
        btnCloseLocation.setOnClickListener(v -> cardLocation.setVisibility(View.GONE));

        // Set user agent value, required for OSM
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());

        // Initialize views
        initializeViews();

        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));

        // Check intent for initial view
        String initialView = getIntent().getStringExtra("INITIAL_VIEW");
        if ("MAP".equals(initialView)) {
            showMapSection();
        } else {
            showProductsSection();
        }

        mapView = findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set default zoom
        mapView.getController().setZoom(15.0);

        // Add compass overlay
        CompassOverlay compassOverlay = new CompassOverlay(this, mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        // Add scale bar overlay
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        mapView.getOverlays().add(scaleBarOverlay);

        // Initialize routing manager
        roadManager = new OSRMRoadManager(this, "DAAM");

        // Setup map click listener for destination selection
        setupMapClickListener();

        // Request location permissions
        requestPermissionsIfNecessary(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        // Setup toggle buttons
        setupToggleButtons();

        // Fetch products from backend
        fetchProducts();

        // Initialize drawer layout and navigation view
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set up navigation item click listeners
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(activity_stores.this, HomeActivity.class));
            } else if (id == R.id.nav_logout) {
                // Clear prefs
                getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply();
                com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
                Toast.makeText(activity_stores.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_stores.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        ImageView menuIcon = findViewById(R.id.ivMenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }
    }

    private void fetchProducts() {
        android.content.SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token != null) {
            com.example.daam.api.RetrofitClient.getInstance().getApi().getProducts("Bearer " + token)
                    .enqueue(new retrofit2.Callback<java.util.List<com.example.daam.model.Product>>() {
                        @Override
                        public void onResponse(retrofit2.Call<java.util.List<com.example.daam.model.Product>> call,
                                retrofit2.Response<java.util.List<com.example.daam.model.Product>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                java.util.List<com.example.daam.model.Product> products = response.body();
                                productAdapter = new com.example.daam.adapter.ProductAdapter(products,
                                        product -> showProductDialog(product));
                                rvProducts.setAdapter(productAdapter);
                            } else {
                                Toast.makeText(activity_stores.this, "Failed to load products: " + response.code(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<java.util.List<com.example.daam.model.Product>> call,
                                Throwable t) {
                            Toast.makeText(activity_stores.this, "Error fetching products: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void setupProductCards() {
        // Not used anymore as we use RecyclerView
    }

    private void showProductDialog(com.example.daam.model.Product product) {
        try {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_dialog_product_details);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            // Initialize dialog views
            ImageView ivDialogImage = dialog.findViewById(R.id.ivDialogProductImage);
            TextView tvDialogName = dialog.findViewById(R.id.tvDialogProductName);
            TextView tvDialogPrice = dialog.findViewById(R.id.tvDialogProductPrice);
            TextView tvDialogDesc = dialog.findViewById(R.id.tvDialogProductDesc);
            ImageView btnClose = dialog.findViewById(R.id.btnCloseDialog);
            Button btnAddToCart = dialog.findViewById(R.id.btnAddToCart);

            // Set product data
            if (ivDialogImage != null) {
                ivDialogImage.setImageResource(R.drawable.solar_roof);
            }

            if (tvDialogName != null) {
                tvDialogName.setText(product.getName());
            }

            if (tvDialogPrice != null) {
                tvDialogPrice.setText("$" + product.getBasePrice());
            }

            if (tvDialogDesc != null) {
                tvDialogDesc.setText(product.getDescription());
            }

            // Close button
            if (btnClose != null) {
                btnClose.setOnClickListener(v -> dialog.dismiss());
            }

            // Request Installation button
            if (btnAddToCart != null) {
                btnAddToCart.setOnClickListener(v -> {
                    // Start Request Installation Activity
                    Intent intent = new Intent(activity_stores.this, RequestInstallationActivity.class);
                    intent.putExtra("PRODUCT_NAME", product.getName());
                    intent.putExtra("PRODUCT_PRICE", "$" + product.getBasePrice());
                    startActivity(intent);
                    dialog.dismiss();
                });
            }

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(
                    this,
                    "Error showing product details: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // ============================================
    // ROUTING METHODS
    // ============================================

    private void setupMapClickListener() {
        mapView.setOnClickListener(v -> {
            // This won't work, we need to use MapEventsOverlay
        });

        // Add a simple tap listener using existing overlays
        org.osmdroid.views.overlay.MapEventsOverlay mapEventsOverlay = new org.osmdroid.views.overlay.MapEventsOverlay(
                new org.osmdroid.events.MapEventsReceiver() {
                    @Override
                    public boolean singleTapConfirmedHelper(GeoPoint p) {
                        onMapTapped(p);
                        return true;
                    }

                    @Override
                    public boolean longPressHelper(GeoPoint p) {
                        return false;
                    }
                });
        mapView.getOverlays().add(0, mapEventsOverlay);
    }

    private void onMapTapped(GeoPoint destination) {
        // Check if we have user location
        if (locationOverlay == null || locationOverlay.getMyLocation() == null) {
            Toast.makeText(this, "Waiting for GPS location...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set destination marker
        if (destinationMarker != null) {
            mapView.getOverlays().remove(destinationMarker);
        }

        destinationMarker = new Marker(mapView);
        destinationMarker.setPosition(destination);
        destinationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        destinationMarker.setTitle("Destination");
        mapView.getOverlays().add(destinationMarker);
        mapView.invalidate();

        // Calculate route
        calculateRoute(destination);
    }

    private void calculateRoute(GeoPoint destination) {
        GeoPoint startPoint = locationOverlay.getMyLocation();
        if (startPoint == null) {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading message
        Toast.makeText(this, "Calculating route...", Toast.LENGTH_SHORT).show();

        // Calculate route in background thread
        new Thread(() -> {
            ArrayList<GeoPoint> waypoints = new ArrayList<>();
            waypoints.add(startPoint);
            waypoints.add(destination);

            Road road = roadManager.getRoad(waypoints);

            runOnUiThread(() -> {
                if (road.mStatus == Road.STATUS_OK) {
                    drawRoute(road);

                    // Show route info
                    double distance = road.mLength; // in km
                    double duration = road.mDuration / 60.0; // convert to minutes
                    String info = String.format("Route: %.1f km, ~%.0f min", distance, duration);
                    Toast.makeText(this, info, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Route calculation failed", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void drawRoute(Road road) {
        // Remove old route if exists
        if (roadOverlay != null) {
            mapView.getOverlays().remove(roadOverlay);
        }

        // Create new route overlay
        roadOverlay = new Polyline();
        roadOverlay.setPoints(road.mRouteHigh);
        roadOverlay.setColor(Color.parseColor("#4285F4")); // Google Maps blue
        roadOverlay.setWidth(10f);

        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate();
    }

    private void initializeViews() {
        btnMap = findViewById(R.id.btnMap);
        btnProducts = findViewById(R.id.btnProducts);
        mapLayout = findViewById(R.id.mapLayout);
        productsLayout = findViewById(R.id.productsLayout);
    }

    private void setupToggleButtons() {
        if (btnMap != null) {
            btnMap.setOnClickListener(v -> showMapSection());
        }
        if (btnProducts != null) {
            btnProducts.setOnClickListener(v -> showProductsSection());
        }
    }

    private void showMapSection() {
        if (mapLayout != null)
            mapLayout.setVisibility(View.VISIBLE);
        if (productsLayout != null)
            productsLayout.setVisibility(View.GONE);
        if (btnMap != null) {
            btnMap.setBackgroundResource(R.drawable.toggle_button_selected);
            btnMap.setTextColor(Color.WHITE);
        }
        if (btnProducts != null) {
            btnProducts.setBackgroundResource(R.drawable.toggle_button_unselected);
            btnProducts.setTextColor(Color.parseColor("#757575"));
        }
    }

    private void showProductsSection() {
        if (mapLayout != null)
            mapLayout.setVisibility(View.GONE);
        if (productsLayout != null)
            productsLayout.setVisibility(View.VISIBLE);
        if (btnMap != null) {
            btnMap.setBackgroundResource(R.drawable.toggle_button_unselected);
            btnMap.setTextColor(Color.parseColor("#757575"));
        }
        if (btnProducts != null) {
            btnProducts.setBackgroundResource(R.drawable.toggle_button_selected);
            btnProducts.setTextColor(Color.WHITE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS);
        } else {
            setupMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMyLocation();
            } else {
                Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupMyLocation() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        mapView.getOverlays().add(locationOverlay);

        locationOverlay.runOnFirstFix(() -> {
            GeoPoint myPoint = locationOverlay.getMyLocation();
            if (myPoint != null) {
                runOnUiThread(() -> {
                    mapView.getController().animateTo(myPoint);
                    if (txtLatitude != null)
                        txtLatitude.setText("Latitude: " + String.format("%.5f", myPoint.getLatitude()));
                    if (txtLongitude != null)
                        txtLongitude.setText("Longitude: " + String.format("%.5f", myPoint.getLongitude()));
                    if (cardLocation != null)
                        cardLocation.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    private void clearRoute() {
        if (roadOverlay != null) {
            mapView.getOverlays().remove(roadOverlay);
            roadOverlay = null;
        }
        if (destinationMarker != null) {
            mapView.getOverlays().remove(destinationMarker);
            destinationMarker = null;
        }
        mapView.invalidate();
    }
}