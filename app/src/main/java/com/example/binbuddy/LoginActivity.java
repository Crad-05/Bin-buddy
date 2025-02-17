package com.example.binbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, registerButton;
    private ProgressBar progressBar; // ProgressBar for loading indication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link UI components
        emailInput = findViewById(R.id.edit_log_user);
        passwordInput = findViewById(R.id.edit_log_password);
        loginButton = findViewById(R.id.btn_login);
        registerButton = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress_bar);

        // Initially hide the progress bar
        progressBar.setVisibility(View.GONE);

        // Login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Email is required!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password is required!");
                    return;
                }

                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Firebase login
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            // Hide progress bar
                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                // Navigate to MainActivity (or your home screen)
                                startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                finish(); // Close LoginActivity
                            } else {
                                // Show a generic error message
                                Toast.makeText(LoginActivity.this, "Login Failed. Please check your credentials.", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                startActivity(new Intent(LoginActivity.this, Register.class));
            }
        });
    }
}