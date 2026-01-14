package com.example.daam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daam.api.RetrofitClient;
import com.example.daam.model.CreateTaskRequest;
import com.example.daam.model.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HireSubmissionActivity extends AppCompatActivity {

    private TextView tvSummaryWorker, tvSummaryProduct;
    private EditText etDescription;
    private Button btnSubmitHire;
    private ImageView btnBack;
    private String workerEmail, workerName, productName;
    private Long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_submission);

        workerEmail = getIntent().getStringExtra("WORKER_EMAIL");
        workerName = getIntent().getStringExtra("WORKER_NAME");
        productId = getIntent().getLongExtra("PRODUCT_ID", -1);
        productName = getIntent().getStringExtra("PRODUCT_NAME");

        tvSummaryWorker = findViewById(R.id.tvSummaryWorker);
        tvSummaryProduct = findViewById(R.id.tvSummaryProduct);
        etDescription = findViewById(R.id.etDescription);
        btnSubmitHire = findViewById(R.id.btnSubmitHire);
        btnBack = findViewById(R.id.btnBack);

        tvSummaryWorker.setText("Worker: " + workerName);
        tvSummaryProduct.setText("Product: " + productName);

        btnBack.setOnClickListener(v -> finish());
        btnSubmitHire.setOnClickListener(v -> submitHireRequest());
    }

    private void submitHireRequest() {
        String description = etDescription.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            description = "Installation of " + productName;
        }

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String clientEmail = prefs.getString("email", null);
        String token = prefs.getString("token", null);

        if (clientEmail != null && token != null) {
            btnSubmitHire.setEnabled(false);
            CreateTaskRequest request = new CreateTaskRequest(description, clientEmail, "PENDING", workerEmail,
                    productId);

            RetrofitClient.getInstance().getApi().createTask(request, "Bearer " + token)
                    .enqueue(new Callback<Task>() {
                        @Override
                        public void onResponse(Call<Task> call, Response<Task> response) {
                            btnSubmitHire.setEnabled(true);
                            if (response.isSuccessful()) {
                                Toast.makeText(HireSubmissionActivity.this, "Hiring Request Sent!", Toast.LENGTH_LONG)
                                        .show();
                                // Redirect to home or activity log
                                finish();
                            } else {
                                Toast.makeText(HireSubmissionActivity.this, "Submission Failed: " + response.code(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Task> call, Throwable t) {
                            btnSubmitHire.setEnabled(true);
                            Toast.makeText(HireSubmissionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }
}
