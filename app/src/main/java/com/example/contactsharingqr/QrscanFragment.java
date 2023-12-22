package com.example.contactsharingqr;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QrscanFragment extends Fragment {

    private CodeScanner mCodeScanner;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    public QrscanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrscan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check and request camera permission
        setupCodeScanner(getView());
    }
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    private void setupCodeScanner(View view) {
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(requireActivity(), scannerView);

        // Set decode callback
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Handle the result.getText() (scanned content)
                        String vCardData = result.getText();

                        // Redirect to the contact save page
                        redirectToContactSavePage(vCardData);
                    }
                });
            }
        });

        // Start preview when the view is created
        mCodeScanner.startPreview();
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
        requireActivity().finish(); // Close this activity after initiating the contact save page
    }
}