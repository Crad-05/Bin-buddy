package com.example.binbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Account extends AppCompatActivity {

    private Button logoutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Logout Button
        logoutBtn = findViewById(R.id.btn_logout); // Initialize scan button

        // Set up logout button click listener
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser ();
            }
        });
    }

    private void logoutUser () {
        try {
            mAuth.signOut(); // Sign out from Firebase
            Intent intent = new Intent(Account.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Finish Dashboard activity
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Logout failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}