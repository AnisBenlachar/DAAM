package com.example.daam;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.daam.adapter.ProductAdapter;
import com.example.daam.api.RetrofitClient;
import com.example.daam.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    // ============================================
    // Navigation Drawer Components
    // ============================================
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    // Search and Buttons
    private EditText etSearch;
    private CardView cardSystemStatus, cardFindWorkers;

    private RecyclerView rvHomeProducts;
    private ProductAdapter productAdapter;
    private List<Product> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ============================================
        // STEP 3A: Initialize Navigation Drawer
        // ============================================
        setupNavigationDrawer();

        // ============================================
        // STEP 3B: Initialize product cards
        // ============================================
        rvHomeProducts = findViewById(R.id.rvHomeProducts);
        rvHomeProducts.setLayoutManager(new GridLayoutManager(this, 2));

        setupMenuIconListener();

        fetchProducts();

        // Setup Search and Buttons
        setupSearch();
        setupHomeButtons();
    }

    private void setupSearch() {
        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupHomeButtons() {
        cardSystemStatus = findViewById(R.id.cardSystemStatus);
        cardFindWorkers = findViewById(R.id.cardFindWorkers);

        if (cardSystemStatus != null) {
            cardSystemStatus.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, activity_stores.class);
                intent.putExtra("INITIAL_VIEW", "PRODUCTS");
                startActivity(intent);
            });
        }

        if (cardFindWorkers != null) {
            cardFindWorkers.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, activity_stores.class);
                intent.putExtra("INITIAL_VIEW", "MAP");
                startActivity(intent);
            });
        }
    }

    private void filterProducts(String query) {
        if (allProducts == null)
            return;

        String lowerQuery = query.toLowerCase();
        java.util.ArrayList<Product> filteredList = new java.util.ArrayList<>();

        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(lowerQuery) ||
                    product.getDescription().toLowerCase().contains(lowerQuery)) {
                filteredList.add(product);
            }
        }

        productAdapter = new ProductAdapter(filteredList, product -> showProductDialog(product));
        rvHomeProducts.setAdapter(productAdapter);
    }

    private void fetchProducts() {
        android.content.SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token != null) {
            RetrofitClient.getInstance().getApi().getProducts("Bearer " + token)
                    .enqueue(new Callback<List<Product>>() {
                        @Override
                        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                allProducts = response.body();
                                productAdapter = new ProductAdapter(allProducts, product -> showProductDialog(product));
                                rvHomeProducts.setAdapter(productAdapter);
                            } else {
                                Toast.makeText(HomeActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Product>> call, Throwable t) {
                            Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // ============================================
    // STEP 3A: Setup Navigation Drawer Method
    // ============================================
    private void setupNavigationDrawer() {
        // Find drawer layout and navigation view
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Create the ActionBarDrawerToggle (hamburger menu icon)
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Enable the hamburger menu icon in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Set up navigation item click listeners
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Toast.makeText(HomeActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                // Handle home click - already on home, so just close drawer

            } else if (id == R.id.nav_activity_log) {
                Intent intent = new Intent(HomeActivity.this, ClientTasksActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_settings) {
                Toast.makeText(HomeActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to Settings activity/fragment
            } else if (id == R.id.nav_about) {
                Toast.makeText(HomeActivity.this, "About Us selected", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to About activity/fragment
            } else if (id == R.id.nav_stores) {
                Toast.makeText(HomeActivity.this, "Stores selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, activity_stores.class);
                startActivity(intent);
            } else if (id == R.id.nav_workers) {
                Toast.makeText(HomeActivity.this, "Workers selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, WorkersActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                // Logout functionality
                com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            // Close the drawer
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupMenuIconListener() {
        ImageView menuIcon = findViewById(R.id.ivMenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                drawerLayout.openDrawer(GravityCompat.START);
            });
        }
    }

    // ============================================
    // STEP 4: Setup Product Cards Method
    // ============================================
    private void setupProductCards() {
        // Obsolete
    }

    // ============================================
    // STEP 5: Show Product Dialog Method
    // ============================================
    private void showProductDialog(Product product) {
        try {
            // Create and configure the dialog
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove title bar
            dialog.setContentView(R.layout.activity_dialog_product_details); // Set our custom layout

            // Make dialog full width and transparent background
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            // Find all views in the dialog
            ImageView ivDialogImage = dialog.findViewById(R.id.ivDialogProductImage);
            TextView tvDialogName = dialog.findViewById(R.id.tvDialogProductName);
            TextView tvDialogPrice = dialog.findViewById(R.id.tvDialogProductPrice);
            TextView tvDialogDesc = dialog.findViewById(R.id.tvDialogProductDesc);
            ImageView btnClose = dialog.findViewById(R.id.btnCloseDialog);
            Button btnAddToCart = dialog.findViewById(R.id.btnAddToCart);

            // Set product data to dialog views
            if (ivDialogImage != null) {
                ivDialogImage.setImageResource(R.drawable.solar_roof);
            }

            if (tvDialogName != null) {
                tvDialogName.setText(product.getName());
            }

            if (tvDialogPrice != null) {
                tvDialogPrice.setText("$" + product.getBasePrice());
            }

            if (tvDialogDesc != null) {
                tvDialogDesc.setText(product.getDescription());
            }

            // Close button - dismisses the dialog
            if (btnClose != null) {
                btnClose.setOnClickListener(v -> dialog.dismiss());
            }

            // Request Installation button
            if (btnAddToCart != null) {
                btnAddToCart.setOnClickListener(v -> {
                    // Start Request Installation Activity
                    Intent intent = new Intent(HomeActivity.this, RequestInstallationActivity.class);
                    intent.putExtra("PRODUCT_NAME", product.getName());
                    intent.putExtra("PRODUCT_PRICE", "$" + product.getBasePrice());
                    startActivity(intent);

                    // Close the dialog
                    dialog.dismiss();
                });
            }

            // Show the dialog!
            dialog.show();

        } catch (Exception e) {
            // Handle any errors gracefully
            e.printStackTrace();
            Toast.makeText(
                    this,
                    "Error showing product details: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // ============================================
    // Handle Menu Item Clicks (Hamburger Menu)
    // ============================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ============================================
    // Handle Back Button with Drawer
    // ============================================

}