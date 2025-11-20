package com.example.daam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_product_detail extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, productDescription, productDetails;
    private Button btnRequestInstallation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔹 Initialize views
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productDetails = findViewById(R.id.productDetails);
        btnRequestInstallation = findViewById(R.id.btnRequestInstallation);

        // 🔹 Example content (you can replace this with intent extras later)
        productName.setText("Solar Panel 500W");
        productPrice.setText("Price: 80,000 DZD");
        productDescription.setText("High-efficiency solar panel for residential and commercial use.");
        productDetails.setText("• Power Output: 500W\n• Efficiency: 22%\n• Warranty: 10 years\n• Size: 2m x 1m");

        // 🔹 Button click
        btnRequestInstallation.setOnClickListener(v ->
                Toast.makeText(this, "Request sent to a worker!", Toast.LENGTH_SHORT).show()
        );
    }
}
