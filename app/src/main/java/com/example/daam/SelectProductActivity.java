package com.example.daam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daam.adapter.ProductAdapter;
import com.example.daam.api.RetrofitClient;
import com.example.daam.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectProductActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private ImageView btnBack;
    private String workerEmail, workerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);

        workerEmail = getIntent().getStringExtra("WORKER_EMAIL");
        workerName = getIntent().getStringExtra("WORKER_NAME");

        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        fetchProducts();
    }

    private void fetchProducts() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token != null) {
            RetrofitClient.getInstance().getApi().getProducts("Bearer " + token)
                    .enqueue(new Callback<List<Product>>() {
                        @Override
                        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                productAdapter = new ProductAdapter(response.body(), product -> {
                                    Intent intent = new Intent(SelectProductActivity.this,
                                            HireSubmissionActivity.class);
                                    intent.putExtra("WORKER_EMAIL", workerEmail);
                                    intent.putExtra("WORKER_NAME", workerName);
                                    intent.putExtra("PRODUCT_ID", product.getId());
                                    intent.putExtra("PRODUCT_NAME", product.getName());
                                    startActivity(intent);
                                });
                                rvProducts.setAdapter(productAdapter);
                            } else {
                                Toast.makeText(SelectProductActivity.this, "Failed to load products",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Product>> call, Throwable t) {
                            Toast.makeText(SelectProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }
}
