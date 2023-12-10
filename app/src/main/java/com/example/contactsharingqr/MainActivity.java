package com.example.contactsharingqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShowMyQR = findViewById(R.id.btnShowMyQR);
        Button btnScanQR = findViewById(R.id.btnScanQR);

        btnShowMyQR.setOnClickListener(view -> {
            // Redirect to the QR generation page
            Intent intent = new Intent(MainActivity.this, QRGenerationActivity.class);
            startActivity(intent);
        });

        btnScanQR.setOnClickListener(view -> {
            // Redirect to the QR scanning page
            Intent intent = new Intent(MainActivity.this, QRScanningActivity.class);
            startActivity(intent);
        });
    }
}
