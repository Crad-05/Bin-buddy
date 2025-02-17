package com.example.binbuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SCAN = 1;

    private Button mapsBtn;
    private Button settingsBtn;
    private Button gamesBtn;
    private Button scanButton;
    private TextView scannedText;

    private DatePicker blackBinDatePicker;
    private DatePicker blueBinDatePicker;
    private DatePicker brownBinDatePicker;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link UI components
        mapsBtn = findViewById(R.id.btn_maps);
        gamesBtn = findViewById(R.id.btn_Games);
        scanButton = findViewById(R.id.btn_scanner);
        settingsBtn = findViewById(R.id.btn_Settings);
        scannedText = findViewById(R.id.scannedText);
        blackBinDatePicker = findViewById(R.id.cal_black);
        blueBinDatePicker = findViewById(R.id.cal_blue);
        brownBinDatePicker = findViewById(R.id.cal_brown);

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(Dashboard.this, LoginActivity.class));
            finish();
            return;
        }

        // Set up games button click listener
        gamesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Quiz_array.class);
                startActivity(intent);
            }
        });

        // Set up scan button click listener
        scanButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                startScan();
            }
        });

        // Set up maps button click listener
        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        // Set up settings button click listener
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Settings.class);
                startActivity(intent);
            }
        });

        // Display bin collection dates on DatePicker
        displayBinCollectionDates();
    }

    private void displayBinCollectionDates() {
        BinCollectionSchedule schedule = new BinCollectionSchedule();
        LocalDate currentDate = LocalDate.now();
        Map<String, String> nextDates = schedule.getNextBinCollectionDates(currentDate);

        // Set the dates on the DatePicker components
        setDatePickerDate(blackBinDatePicker, nextDates.get("Black Bin"));
        setDatePickerDate(blueBinDatePicker, nextDates.get("Blue Bin"));
        setDatePickerDate(brownBinDatePicker, nextDates.get("Brown Bin"));
    }

    private void setDatePickerDate(DatePicker datePicker, String dateString) {
        LocalDate date = BinCollectionSchedule.parseDate(dateString);
        if (date != null) {
            datePicker.updateDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        }
    }

    private void startScan() {
        Intent intent = new Intent(Dashboard.this, CustomCaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            String scannedResult = data != null ? data.getStringExtra("SCAN_RESULT") : null;
            if (scannedResult != null) {
                scannedText.setText("Scanned QR code: " + scannedResult);
            } else {
                Toast.makeText(this, "Scan failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScan();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
