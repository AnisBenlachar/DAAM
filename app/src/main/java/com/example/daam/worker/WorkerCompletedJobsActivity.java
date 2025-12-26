package com.example.daam.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class WorkerCompletedJobsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayout jobsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_completed_jobs);

        setupNavigationDrawer();

        jobsContainer = findViewById(R.id.completedJobsContainer);
        loadDummyJobs();
    }

    private void loadDummyJobs() {
        // Add some dummy completed jobs
        addJobCard("Alice Cooper", "Solar Water Heater 200L", "12 Sunset Blvd", "20/12/2024");
        addJobCard("Bob Marley", "PV System 5kW", "56 Reggae Road", "18/12/2024");
        addJobCard("Charlie Puth", "Inverter 5000W", "99 Attention Street", "15/12/2024");
        addJobCard("David Guetta", "Battery Bank 48V", "1 Titanium Way", "10/12/2024");
    }

    private void addJobCard(String name, String product, String address, String date) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_worker_completed_job, jobsContainer, false);

        TextView tvName = cardView.findViewById(R.id.tvClientName);
        TextView tvProduct = cardView.findViewById(R.id.tvProduct);
        TextView tvAddress = cardView.findViewById(R.id.tvAddress);
        TextView tvDate = cardView.findViewById(R.id.tvDate);

        tvName.setText(name);
        tvProduct.setText(product);
        tvAddress.setText(address);
        tvDate.setText("Completed on: " + date);

        jobsContainer.addView(cardView);
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.worker_drawer_layout);
        navigationView = findViewById(R.id.worker_nav_view);
        ImageView menuIcon = findViewById(R.id.ivWorkerMenuIcon);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_worker_dashboard) {
                Intent intent = new Intent(this, WorkerHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (id == R.id.nav_worker_requests) {
                Intent intent = new Intent(this, WorkerRequestsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_worker_profile) {
                Intent intent = new Intent(this, WorkerProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_worker_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}
