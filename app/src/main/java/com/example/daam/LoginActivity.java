package com.example.daam;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private TextView textSignUp;
    private TextView textForgotPassword;
    private Button buttonLogin;
    private Button buttonGoogle;
    private EditText editTextEmail, editTextPassword;
    private TextView btnRoleClient, btnRoleWorker;
    private boolean isWorker = false;

    // Firebase Auth
    private FirebaseAuth mAuth;

    // Google Sign-In
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        textSignUp = findViewById(R.id.textSignUp);
        textForgotPassword = findViewById(R.id.textForgotPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnRoleClient = findViewById(R.id.btnRoleClient);
        btnRoleWorker = findViewById(R.id.btnRoleWorker);

        setupRoleSelection();

        // Configure Google Sign-In
        configureGoogleSignIn();

        // ✅ Login button -> validate input first
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Password is required");
                    return;
                }

                // Sign in with Firebase Email/Password
                signInWithEmailPassword(email, password);
            }
        });

        // Google Sign-In button
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        // Sign Up text -> go to SignUpActivity
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Forgot Password text -> go to ForgotPasswordActivity
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupRoleSelection() {
        btnRoleClient.setOnClickListener(v -> {
            isWorker = false;
            updateRoleUI();
        });

        btnRoleWorker.setOnClickListener(v -> {
            isWorker = true;
            updateRoleUI();
        });
    }

    private void updateRoleUI() {
        if (isWorker) {
            btnRoleWorker.setBackgroundResource(R.drawable.toggle_button_selected);
            btnRoleWorker.setTextColor(getResources().getColor(android.R.color.white));

            btnRoleClient.setBackgroundResource(android.R.color.transparent);
            btnRoleClient.setTextColor(getResources().getColor(R.color.gray_text)); // Assuming a gray color logic or
                                                                                    // hardcoded
        } else {
            btnRoleClient.setBackgroundResource(R.drawable.toggle_button_selected);
            btnRoleClient.setTextColor(getResources().getColor(android.R.color.white));

            btnRoleWorker.setBackgroundResource(android.R.color.transparent);
            btnRoleWorker.setTextColor(getResources().getColor(R.color.gray_text));
        }

        // Use hardcoded colors if resource not found to be safe, but ideally defining
        // them in colors.xml
        if (!isWorker)
            btnRoleWorker.setTextColor(0xFF757575);
        else
            btnRoleClient.setTextColor(0xFF757575);
    }

    private void configureGoogleSignIn() {
        // Configure Google Sign-In to request the user's ID, email, and basic profile
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        // Sign out first to force account picker to show
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Now start the sign-in intent
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from
        // GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign-In failed
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT)
                                    .show();
                            navigateToHome();
                        } else {
                            // Sign-in failed
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInWithEmailPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                            navigateToHome();
                        } else {
                            // Sign-in failed
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToHome() {
        Intent intent;
        if (isWorker) {
            // Navigate to Worker Home (Fully qualified name to avoid import issues yet)
            intent = new Intent(LoginActivity.this, com.example.daam.worker.WorkerHomeActivity.class);
        } else {
            // Navigate to Client Home
            intent = new Intent(LoginActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Auto-login disabled - user must explicitly log in
        // This prevents bypassing the login screen
        /*
         * FirebaseUser currentUser = mAuth.getCurrentUser();
         * if (currentUser != null) {
         * navigateToHome();
         * }
         */
    }
}
