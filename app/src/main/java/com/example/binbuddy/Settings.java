package com.example.binbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find Account Button by its ID
        Button accountButton = findViewById(R.id.btn_Account);

        // Set an onClick listener for the button
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Account activity
                Intent intent = new Intent(Settings.this, Account.class);
                startActivity(intent);
            }
        });

        /* Find Notifications Button by Its ID
        Button notificationsButton = findViewById(R.id.btn_Notifications);

        // Set an onClick listener for the button
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Notifications activity
                Intent intent = new Intent(Settings.this, Notifications.class);
                startActivity(intent);
            }
        });*/

        // Find Appearance Button by its ID
        Button appearanceButton = findViewById(R.id.btn_Appearance);

        // Set an onClick listener for the button
        appearanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Appearance activity
                Intent intent = new Intent(Settings.this, Appearance.class);
                startActivity(intent);
            }
        });

        // Find About Button by its ID
        Button aboutButton = findViewById(R.id.btn_AboutBinBuddy);

        // Set an onClick listener for the button
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the About activity
                Intent intent = new Intent(Settings.this, AboutBinBuddy.class);
                startActivity(intent);
            }
        });
    }
}