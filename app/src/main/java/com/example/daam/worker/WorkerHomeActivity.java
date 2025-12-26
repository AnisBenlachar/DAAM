package com.example.daam.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.daam.LoginActivity;
import com.example.daam.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class WorkerHomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_home);

        setupNavigationDrawer();
        setupDashboardClicks();
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.worker_drawer_layout);
        navigationView = findViewById(R.id.worker_nav_view);
        ImageView menuIcon = findViewById(R.id.ivWorkerMenuIcon);

        // Setup custom menu icon click
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Setup navigation item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_worker_dashboard) {
                // Already here
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_worker_requests) {
                Intent intent = new Intent(WorkerHomeActivity.this, WorkerRequestsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_worker_profile) {
                Intent intent = new Intent(WorkerHomeActivity.this, WorkerProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_worker_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(WorkerHomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WorkerHomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupDashboardClicks() {
        CardView cardPending = findViewById(R.id.cardPendingRequests);
        CardView cardCompleted = findViewById(R.id.cardCompletedJobs);
        Button btnViewRequests = findViewById(R.id.btnViewRequests);

        if (cardPending != null) {
            cardPending.setOnClickListener(v -> {
                Intent intent = new Intent(WorkerHomeActivity.this, WorkerRequestsActivity.class);
                startActivity(intent);
            });
        }

        if (cardCompleted != null) {
            cardCompleted.setOnClickListener(v -> {
                Intent intent = new Intent(WorkerHomeActivity.this, WorkerCompletedJobsActivity.class);
                startActivity(intent);
            });
        }

        if (btnViewRequests != null) {
            btnViewRequests.setOnClickListener(v -> {
                Intent intent = new Intent(WorkerHomeActivity.this, WorkerRequestsActivity.class);
                startActivity(intent);
            });
        }
    }
}
