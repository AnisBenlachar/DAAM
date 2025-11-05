package com.example.daam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // "Create Account" button
        Button createAccountBtn = findViewById(R.id.buttonCreateAccount);
        // "Back to Login" button
        Button backToLoginBtn = findViewById(R.id.buttonBackToLogin);

        // When user taps Create Account
        createAccountBtn.setOnClickListener(v -> {
            // TODO: add actual sign-up logic later (Firebase, API, etc.)
            // For now, just go back to LoginActivity
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // close SignUpActivity so user can't go back here by pressing back
        });

        // When user taps Back to Login
        backToLoginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
