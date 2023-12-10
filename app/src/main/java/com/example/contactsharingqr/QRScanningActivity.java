package com.example.contactsharingqr;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Directly initiate the QR code scan
        initiateQRScan();
    }

    private void initiateQRScan() {
        // Initialize the QR code scanner
        IntentIntegrator integrator = new IntentIntegrator(QRScanningActivity.this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Receive the scan result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Handle the scanned data (vCard QR code)
                String vCardData = result.getContents();

                // Redirect to the contact save page
                redirectToContactSavePage(vCardData);
            } else {
                // Handle case when the scan was canceled
            }
        }
    }

    private void redirectToContactSavePage(String vCardData) {
        // Parse vCard data and create a new contact
        ContactUtils contactUtils = new ContactUtils();
        Contact contact = contactUtils.parseVCardData(vCardData);

        // Create an implicit intent to add a new contact
        Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName()); // Set the contact name
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhoneNumber()); // Set the contact phone number

        // You can set additional contact information here

        startActivity(intent);
        finish(); // Close this activity after initiating the contact save page
    }
}
