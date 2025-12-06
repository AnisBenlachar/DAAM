package com.example.daam;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
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

    // ============================================
    // STEP 2: Product Data Class
    // ============================================
    // This class holds information about each product
    public static class Product {
        public int imageResource; // Image drawable ID
        public String name; // Product name
        public String price; // Product price
        public String desc; // Product description
        public String badge; // Badge text (NEW, HOT, etc.) or null

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
        setupProductCards();

        setupMenuIconListener();
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

            } else if (id == R.id.nav_messages) {
                Toast.makeText(HomeActivity.this, "Messages selected", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to Messages activity/fragment
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
            } else if (id == R.id.nav_map) {
                Toast.makeText(HomeActivity.this, "Map selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                startActivity(intent);
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
        // Create product objects with data
        Product product1 = new Product(
                R.drawable.solar_roof,
                "Solar Panel Kit",
                "$2,499",
                "High efficiency solar panel kit with advanced monocrystalline technology. Perfect for residential installations with maximum energy production capacity. Includes mounting hardware and 25-year warranty.",
                "NEW");

        Product product2 = new Product(
                R.drawable.solar_roof,
                "Inverter System",
                "$1,299",
                "Professional grade 3000W inverter system with smart monitoring capabilities. Converts DC to AC power efficiently with built-in surge protection and real-time performance tracking.",
                "-20%");

        Product product3 = new Product(
                R.drawable.solar_roof,
                "Battery Storage",
                "$3,999",
                "Advanced 10kWh lithium-ion battery storage system. Store excess solar energy for nighttime use. Includes smart management system and 10-year warranty.",
                null // No badge
        );

        Product product4 = new Product(
                R.drawable.solar_roof,
                "Monitoring Kit",
                "$599",
                "Smart monitoring system with mobile app integration. Track your energy production, consumption, and savings in real-time. Easy installation with wireless connectivity.",
                "HOT");

        // Find product card views by their IDs
        CardView cardProduct1 = findViewById(R.id.cardProduct1);
        CardView cardProduct2 = findViewById(R.id.cardProduct2);
        CardView cardProduct3 = findViewById(R.id.cardProduct3);
        CardView cardProduct4 = findViewById(R.id.cardProduct4);

        // Set click listeners - when clicked, show dialog with product data
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

            // Close button - dismisses the dialog
            if (btnClose != null) {
                btnClose.setOnClickListener(v -> dialog.dismiss());
            }

            // Request Installation button
            if (btnAddToCart != null) {
                btnAddToCart.setOnClickListener(v -> {
                    // Show confirmation message
                    Toast.makeText(
                            this,
                            "Installation request sent for " + product.name + " ",
                            Toast.LENGTH_SHORT).show();

                    // Close the dialog
                    dialog.dismiss();

                    // TODO: Add your custom logic here:
                    // - Navigate to installation form
                    // - Send request to server
                    // - Save to database, etc.
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