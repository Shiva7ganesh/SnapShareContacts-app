package com.example.contactsharingqr;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.person);
    }

    QrgenFragment firstFragment = new QrgenFragment();
    QrscanFragment secondFragment = new QrscanFragment();

    // Function to check and request permission
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.person) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, firstFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.home) {
            // Checking camera permission
            checkPermissionAndOpenScanner();
            return true;
        }
        return false;
    }

    private void checkPermissionAndOpenScanner() {
        // Checking if camera permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            // Request camera permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, 100);
        } else {
            // Camera permission is already granted, open scanner
            openScannerFragment();
        }
    }

    // Callback for permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Camera permission granted, open scanner
            openScannerFragment();
        }
    }

    private void openScannerFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, secondFragment)
                .commit();
    }
}


