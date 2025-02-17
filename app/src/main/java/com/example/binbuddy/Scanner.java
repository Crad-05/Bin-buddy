package com.example.binbuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Scanner extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SCAN = 1;

    private Button scanButton;
    private TextView scannedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        scanButton = findViewById(R.id.btn_scanner);
        scannedText = findViewById(R.id.scannedText);

        // Set up the button click listener
        scanButton.setOnClickListener(v -> {
            // Check if camera permission is granted
            if (ContextCompat.checkSelfPermission(Scanner.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Request camera permission if not granted
                ActivityCompat.requestPermissions(Scanner.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                // Permission granted, start scanning activity
                startScan();
            }
        });
    }

    // Start the QR code scanning activity
    private void startScan() {
        Intent intent = new Intent(Scanner.this, CustomCaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    // Handle the result of the QR code scan
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

    // Handle runtime permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start scanning
                startScan();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}