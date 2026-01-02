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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daam.LoginActivity;
import com.example.daam.R;
import com.example.daam.adapter.TaskAdapter;
import com.example.daam.model.Task;
import com.example.daam.api.RetrofitClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerHomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private RecyclerView rvWorkerTasks;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_home);

        setupNavigationDrawer();
        setupDashboardClicks();
        setupRecyclerView();
        fetchTasks();
    }

    private void setupRecyclerView() {
        rvWorkerTasks = findViewById(R.id.rvWorkerTasks);
        rvWorkerTasks.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchTasks() {
        android.content.SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        String email = prefs.getString("email", null);

        if (token != null && email != null) {
            RetrofitClient.getInstance().getApi().getWorkerTasks(email, "Bearer " + token)
                    .enqueue(new Callback<List<Task>>() {
                        @Override
                        public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Task> tasks = response.body();
                                taskAdapter = new TaskAdapter(tasks);
                                rvWorkerTasks.setAdapter(taskAdapter);
                            } else {
                                Toast.makeText(WorkerHomeActivity.this, "Failed to load tasks: " + response.code(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Task>> call, Throwable t) {
                            Toast.makeText(WorkerHomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } else {
            Toast.makeText(this, "Session expired, please login again", Toast.LENGTH_SHORT).show();
            // Redirect to login
            Intent intent = new Intent(WorkerHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
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
                // Clear prefs
                getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply();
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
        // Button btnViewRequests removed from layout or handled differently, strictly
        // updating logic here as per layout change logic
        // But since I didn't remove the ID from the java code in previous steps (only
        // layout), I should keep it conditional or remove if not found.
        // Actually I removed it from layout in step 88. So I should remove it here too
        // to avoid NPE if I access it.
        // I will just keep the parts that are still relevant. `btnViewRequests` was
        // removed from layout (or replaced).
        // Wait, did I remove it?
        // Step 88 diff shows I replaced the block containing `btnViewRequests` with
        // RecyclerView block.
        // So `findViewById(R.id.btnViewRequests)` will return null.
        // The existing code has null checks: `if (btnViewRequests != null)`. So it's
        // safe.

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
    }
}
