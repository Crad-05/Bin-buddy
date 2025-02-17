package com.example.binbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput;
    private Button registerButton;
    private ProgressBar progressBar; // ProgressBar for loading indication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link UI components
        usernameInput = findViewById(R.id.edit_reg_user);
        emailInput = findViewById(R.id.edit_reg_email);
        passwordInput = findViewById(R.id.edit_reg_password);
        registerButton = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress_bar);

        // Register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Validate inputs
                if (TextUtils.isEmpty(username)) {
                    usernameInput.setError("Username is required!");
                    usernameInput.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Email is required!");
                    emailInput.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Enter a valid email address!");
                    emailInput.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password is required!");
                    passwordInput.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    passwordInput.setError("Password must be at least 6 characters long!");
                    passwordInput.requestFocus();
                    return;
                }

                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Firebase registration
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    user.updateProfile(new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build());
                                }

                                // Navigate to LoginActivity
                                startActivity(new Intent(Register.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Register.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}