package com.example.daam.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.daam.LoginActivity;
import com.example.daam.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class WorkerRequestsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayout requestsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_requests);

        setupNavigationDrawer();

        requestsContainer = findViewById(R.id.requestsContainer);
        loadDummyWrapper();
    }

    private void loadDummyWrapper() {
        // Add some dummy requests
        addRequestCard("John Doe", "Solar Panel Kit - Premium", "123 Solar Blvd, Sun City");
        addRequestCard("Jane Smith", "Inverter System 3000W", "45 Energy Lane, Power Town");
        addRequestCard("Robert Brown", "Battery Storage 10kWh", "789 Green St, Eco Village");
    }

    private void addRequestCard(String name, String product, String address) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_worker_request, requestsContainer, false);

        TextView tvName = cardView.findViewById(R.id.tvClientName);
        TextView tvProduct = cardView.findViewById(R.id.tvProduct);
        TextView tvAddress = cardView.findViewById(R.id.tvAddress);
        Button btnAccept = cardView.findViewById(R.id.btnAccept);
        Button btnReject = cardView.findViewById(R.id.btnReject);

        tvName.setText(name);
        tvProduct.setText(product);
        tvAddress.setText(address);

        btnAccept.setOnClickListener(v -> {
            // Navigate to Job Detail
            Intent intent = new Intent(WorkerRequestsActivity.this, WorkerJobDetailActivity.class);
            intent.putExtra("CLIENT_NAME", name);
            intent.putExtra("PRODUCT_NAME", product);
            intent.putExtra("CLIENT_ADDRESS", address);
            startActivity(intent);
        });

        btnReject.setOnClickListener(v -> {
            requestsContainer.removeView(cardView);
            Toast.makeText(this, "Request Rejected", Toast.LENGTH_SHORT).show();
        });

        requestsContainer.addView(cardView);
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.worker_drawer_layout);
        navigationView = findViewById(R.id.worker_nav_view);
        ImageView menuIcon = findViewById(R.id.ivWorkerMenuIcon);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_worker_dashboard) {
                Intent intent = new Intent(WorkerRequestsActivity.this, WorkerHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (id == R.id.nav_worker_requests) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_worker_profile) {
                Intent intent = new Intent(WorkerRequestsActivity.this, WorkerProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_worker_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(WorkerRequestsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}
