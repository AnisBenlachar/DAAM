package com.example.daam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daam.adapter.WorkerAdapter;
import com.example.daam.api.RetrofitClient;
import com.example.daam.model.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkersActivity extends AppCompatActivity {

    private RecyclerView rvWorkers;
    private WorkerAdapter workerAdapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        rvWorkers = findViewById(R.id.rvWorkers);
        rvWorkers.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        fetchWorkers();
    }

    private void fetchWorkers() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token != null) {
            RetrofitClient.getInstance().getApi().getWorkers("Bearer " + token)
                    .enqueue(new Callback<List<UserDTO>>() {
                        @Override
                        public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                workerAdapter = new WorkerAdapter(response.body(), worker -> {
                                    Intent intent = new Intent(WorkersActivity.this, SelectProductActivity.class);
                                    intent.putExtra("WORKER_EMAIL", worker.getEmail());
                                    intent.putExtra("WORKER_NAME", worker.getFirstName() + " " + worker.getLastName());
                                    startActivity(intent);
                                });
                                rvWorkers.setAdapter(workerAdapter);
                            } else {
                                Toast.makeText(WorkersActivity.this, "Failed to load workers", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                            Toast.makeText(WorkersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
