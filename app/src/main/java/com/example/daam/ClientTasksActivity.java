package com.example.daam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daam.adapter.TaskAdapter;
import com.example.daam.api.RetrofitClient;
import com.example.daam.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientTasksActivity extends AppCompatActivity {

    private RecyclerView rvClientTasks;
    private TaskAdapter taskAdapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tasks);

        rvClientTasks = findViewById(R.id.rvClientTasks);
        rvClientTasks.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        fetchClientTasks();
    }

    private void fetchClientTasks() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        String email = prefs.getString("email", null);

        if (token != null && email != null) {
            RetrofitClient.getInstance().getApi().getTasksByClient(email, "Bearer " + token)
                    .enqueue(new Callback<List<Task>>() {
                        @Override
                        public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                taskAdapter = new TaskAdapter(response.body());
                                rvClientTasks.setAdapter(taskAdapter);
                            } else {
                                Toast.makeText(ClientTasksActivity.this, "Failed to load activity log",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Task>> call, Throwable t) {
                            Toast.makeText(ClientTasksActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }
}
