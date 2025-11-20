package com.example.daam;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.navigation.NavigationView;

public class activity_stores extends AppCompatActivity {
    // UI Elements
    private Button btnMap, btnProducts;
    private FrameLayout mapLayout;
    private ScrollView productsLayout;

    // Product data class (same as HomeActivity)
    public static class Product {
        public int imageResource;
        public String name;
        public String price;
        public String desc;
        public String badge;

        public Product(int imageResource, String name, String price, String desc, String badge) {
            this.imageResource = imageResource;
            this.name = name;
            this.price = price;
            this.desc = desc;
            this.badge = badge;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        // Initialize views
        initializeViews();

        // Setup toggle buttons
        setupToggleButtons();

        // Setup product cards
        setupProductCards();

        // Initialize drawer layout and navigation view
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set up navigation item click listeners
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(activity_stores.this, HomeActivity.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        ImageView menuIcon = findViewById(R.id.ivMenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }
    }

    private void initializeViews() {
        btnMap = findViewById(R.id.btnMap);
        btnProducts = findViewById(R.id.btnProducts);
        mapLayout = findViewById(R.id.mapLayout);
        productsLayout = findViewById(R.id.productsLayout);
    }

    private void setupToggleButtons() {
        // Map button click
        btnMap.setOnClickListener(v -> {
            showMapSection();
        });

        // Products button click
        btnProducts.setOnClickListener(v -> {
            showProductsSection();
        });
    }

    private void showMapSection() {
        // Show map, hide products
        mapLayout.setVisibility(View.VISIBLE);
        productsLayout.setVisibility(View.GONE);

        // Update button styles
        btnMap.setBackgroundResource(R.drawable.toggle_button_selected);
        btnMap.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        btnProducts.setBackgroundResource(R.drawable.toggle_button_unselected);
        btnProducts.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
    }

    private void showProductsSection() {
        // Show products, hide map
        mapLayout.setVisibility(View.GONE);
        productsLayout.setVisibility(View.VISIBLE);

        // Update button styles
        btnProducts.setBackgroundResource(R.drawable.toggle_button_selected);
        btnProducts.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        btnMap.setBackgroundResource(R.drawable.toggle_button_unselected);
        btnMap.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
    }

    private void setupProductCards() {
        // Create product data
        Product product1 = new Product(
                R.drawable.solar_roof,
                "Solar Panel Kit",
                "$2,499",
                "High efficiency solar panel kit with advanced monocrystalline technology. Perfect for residential installations with maximum energy production capacity. Includes mounting hardware and 25-year warranty.",
                "NEW"
        );

        Product product2 = new Product(
                R.drawable.solar_roof,
                "Inverter System",
                "$1,299",
                "Professional grade 3000W inverter system with smart monitoring capabilities. Converts DC to AC power efficiently with built-in surge protection and real-time performance tracking.",
                "-20%"
        );

        Product product3 = new Product(
                R.drawable.solar_roof,
                "Battery Storage",
                "$3,999",
                "Advanced 10kWh lithium-ion battery storage system. Store excess solar energy for nighttime use. Includes smart management system and 10-year warranty.",
                null
        );

        Product product4 = new Product(
                R.drawable.solar_roof,
                "Monitoring Kit",
                "$599",
                "Smart monitoring system with mobile app integration. Track your energy production, consumption, and savings in real-time. Easy installation with wireless connectivity.",
                "HOT"
        );

        // Find product cards
        CardView cardProduct1 = findViewById(R.id.cardStoreProduct1);
        CardView cardProduct2 = findViewById(R.id.cardStoreProduct2);
        CardView cardProduct3 = findViewById(R.id.cardStoreProduct3);
        CardView cardProduct4 = findViewById(R.id.cardStoreProduct4);

        // Set click listeners
        if (cardProduct1 != null) {
            cardProduct1.setOnClickListener(v -> showProductDialog(product1));
        }

        if (cardProduct2 != null) {
            cardProduct2.setOnClickListener(v -> showProductDialog(product2));
        }

        if (cardProduct3 != null) {
            cardProduct3.setOnClickListener(v -> showProductDialog(product3));
        }

        if (cardProduct4 != null) {
            cardProduct4.setOnClickListener(v -> showProductDialog(product4));
        }
    }

    private void showProductDialog(Product product) {
        try {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_dialog_product_details);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            // Initialize dialog views
            ImageView ivDialogImage = dialog.findViewById(R.id.ivDialogProductImage);
            TextView tvDialogName = dialog.findViewById(R.id.tvDialogProductName);
            TextView tvDialogPrice = dialog.findViewById(R.id.tvDialogProductPrice);
            TextView tvDialogDesc = dialog.findViewById(R.id.tvDialogProductDesc);
            ImageView btnClose = dialog.findViewById(R.id.btnCloseDialog);
            Button btnAddToCart = dialog.findViewById(R.id.btnAddToCart);

            // Set product data
            if (ivDialogImage != null) {
                ivDialogImage.setImageResource(product.imageResource);
            }

            if (tvDialogName != null) {
                tvDialogName.setText(product.name);
            }

            if (tvDialogPrice != null) {
                tvDialogPrice.setText(product.price);
            }

            if (tvDialogDesc != null) {
                tvDialogDesc.setText(product.desc);
            }

            // Close button
            if (btnClose != null) {
                btnClose.setOnClickListener(v -> dialog.dismiss());
            }

            // Request Installation button
            if (btnAddToCart != null) {
                btnAddToCart.setOnClickListener(v -> {
                    Toast.makeText(
                            this,
                            "Installation request sent for " + product.name + " ",
                            Toast.LENGTH_SHORT
                    ).show();
                    dialog.dismiss();
                });
            }

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(
                    this,
                    "Error showing product details: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}