package com.example.daam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

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

        // Simulate submission
        // Here you would typically send this data to a server or save to a database

        Toast.makeText(this, "Installation request sent successfully!", Toast.LENGTH_LONG).show();

        // Return to previous screen
        finish();
    }
}
