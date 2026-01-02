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
import com.example.daam.api.RetrofitClient;
import com.example.daam.model.Task;
import com.example.daam.model.UpdateTaskStatusRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

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
        fetchRequests();
    }

    private void fetchRequests() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        String email = prefs.getString("email", null);

        if (token != null && email != null) {
            RetrofitClient.getInstance().getApi().getWorkerTasks(email, "Bearer " + token)
                    .enqueue(new Callback<List<Task>>() {
                        @Override
                        public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                requestsContainer.removeAllViews();
                                for (Task task : response.body()) {
                                    if ("PENDING".equals(task.getStatus())) {
                                        addRequestCard(task);
                                    }
                                }
                            } else {
                                Toast.makeText(WorkerRequestsActivity.this, "Failed to load requests",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Task>> call, Throwable t) {
                            Toast.makeText(WorkerRequestsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    private void addRequestCard(Task task) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_worker_request, requestsContainer, false);

        TextView tvName = cardView.findViewById(R.id.tvClientName);
        TextView tvProduct = cardView.findViewById(R.id.tvProduct);
        TextView tvAddress = cardView.findViewById(R.id.tvAddress);
        Button btnAccept = cardView.findViewById(R.id.btnAccept);
        Button btnReject = cardView.findViewById(R.id.btnReject);

        tvName.setText(task.getClientName() != null ? task.getClientName() : task.getClientEmail());
        tvProduct.setText(task.getDescription());
        tvAddress.setText("Installation Request");

        btnAccept.setOnClickListener(v -> updateTaskStatus(task.getId(), "CONFIRMED", cardView));

        btnReject.setOnClickListener(v -> updateTaskStatus(task.getId(), "CANCELLED", cardView));

        requestsContainer.addView(cardView);
    }

    private void updateTaskStatus(Long taskId, String status, View cardView) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token != null) {
            UpdateTaskStatusRequest request = new UpdateTaskStatusRequest(status);
            RetrofitClient.getInstance().getApi().updateTaskStatus(taskId, request, "Bearer " + token)
                    .enqueue(new Callback<Task>() {
                        @Override
                        public void onResponse(Call<Task> call, Response<Task> response) {
                            if (response.isSuccessful()) {
                                requestsContainer.removeView(cardView);
                                Toast.makeText(WorkerRequestsActivity.this, "Task " + status, Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(WorkerRequestsActivity.this, "Failed to update status",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Task> call, Throwable t) {
                            Toast.makeText(WorkerRequestsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
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
                // Clear prefs
                getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply();
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
