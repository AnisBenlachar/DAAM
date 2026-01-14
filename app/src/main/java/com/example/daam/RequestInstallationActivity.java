package com.example.daam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.daam.api.RetrofitClient;
import com.example.daam.model.CreateTaskRequest;
import com.example.daam.model.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.SharedPreferences;

public class RequestInstallationActivity extends AppCompatActivity {

    private TextView tvProductName, tvProductPrice;
    private TextInputEditText etFullName, etPhone, etAddress;
    private Button btnSubmitRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_installation);

        initializeViews();

        // Get data from intent
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        String productPrice = getIntent().getStringExtra("PRODUCT_PRICE");

        // Set product data
        if (productName != null)
            tvProductName.setText(productName);
        if (productPrice != null)
            tvProductPrice.setText(productPrice);

        setupListeners();
    }

    private void initializeViews() {
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
    }

    private void setupListeners() {
        btnSubmitRequest.setOnClickListener(v -> submitRequest());
    }

    private void submitRequest() {
        // Simple validation
        String name = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty()) {
            etFullName.setError("Name is required");
            etFullName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone is required");
            etPhone.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        String clientEmail = prefs.getString("email", null);

        if (token == null || clientEmail == null) {
            Toast.makeText(this, "Session expired, please login again", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = "Installation for: " + tvProductName.getText().toString() +
                "\nClient: " + name +
                "\nPhone: " + phone +
                "\nAddress: " + address;

        CreateTaskRequest request = new CreateTaskRequest(description, clientEmail, "PENDING", null, null);

        btnSubmitRequest.setEnabled(false);
        RetrofitClient.getInstance().getApi().createTask(request, "Bearer " + token)
                .enqueue(new Callback<Task>() {
                    @Override
                    public void onResponse(Call<Task> call, Response<Task> response) {
                        btnSubmitRequest.setEnabled(true);
                        if (response.isSuccessful()) {
                            Toast.makeText(RequestInstallationActivity.this, "Installation request sent successfully!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(RequestInstallationActivity.this,
                                    "Failed to send request: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Task> call, Throwable t) {
                        btnSubmitRequest.setEnabled(true);
                        Toast.makeText(RequestInstallationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
