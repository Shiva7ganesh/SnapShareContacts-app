package com.example.contactsharingqr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrgenFragment extends Fragment {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String NAME_KEY = "name";
    private static final String PHONE_KEY = "phone";
    private static final String EMAIL_KEY = "email";

    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private ImageView imageViewQR;

    public QrgenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrgen, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        imageViewQR = view.findViewById(R.id.imageViewQR);
        Button btnGenerateQR = view.findViewById(R.id.btnGenerateQR);

        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                if (!isValidContactName(name)) {
                    editTextName.setError("Name cannot contain numbers");
                } else if (!isValidPhoneNumber(phone)) {
                    editTextPhone.setError("Invalid phone number. Please enter a valid 10 or 12-digit number.");
                } else if (!email.isEmpty() && !isValidEmail(email)) {
                    editTextEmail.setError("Invalid email address");
                } else {
                    // Save name, phone, and email in SharedPreferences for future use
                    saveData(name, phone, email);

                    // Generate and display QR code
                    generateAndDisplayQR(name, phone, email);
                    hideKeyboard(v);

                    // Show toast message for QR code generated
                    showToast("QR Code Generated Successfully");
                }
            }
        });

        // Check if data is already stored in SharedPreferences
        String storedName = getData(NAME_KEY);
        String storedPhone = getData(PHONE_KEY);
        String storedEmail = getData(EMAIL_KEY);

        if (storedName != null && storedPhone != null && storedEmail != null) {
            // Data is available, populate EditTexts and display QR code
            editTextName.setText(storedName);
            editTextPhone.setText(storedPhone);
            editTextEmail.setText(storedEmail);
            generateAndDisplayQR(storedName, storedPhone, storedEmail);
        }

        return view;
    }

    private void generateAndDisplayQR(String name, String phone, String email) {
        // Generate QR code using ZXing library
        QRCodeWriter writer = new QRCodeWriter();
        try {
            String qrContent = "BEGIN:VCARD\n" +
                    "VERSION:3.0\n" +
                    "FN:" + name + "\n" +
                    "TEL:" + phone + "\n" +
                    (email.isEmpty() ? "" : "EMAIL:" + email + "\n") +
                    "END:VCARD";
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(qrContent, com.google.zxing.BarcodeFormat.QR_CODE, 512, 512);

            // Convert BitMatrix to Bitmap
            android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(512, 512, android.graphics.Bitmap.Config.RGB_565);
            for (int x = 0; x < 512; x++) {
                for (int y = 0; y < 512; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }

            // Display the generated QR code
            imageViewQR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void saveData(String name, String phone, String email) {
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences(PREFS_NAME, requireActivity().MODE_PRIVATE).edit();
        editor.putString(NAME_KEY, name);
        editor.putString(PHONE_KEY, phone);
        editor.putString(EMAIL_KEY, email);
        editor.apply();
    }

    private String getData(String key) {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, requireActivity().MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Function to validate the contact name
    private boolean isValidContactName(String contactName) {
        // Check if the contact name contains any numeric characters
        return !contactName.matches(".*\\d.*");
    }

    // Function to validate the email address
    private boolean isValidEmail(String email) {
        // Implement email address validation using regular expressions
        // Return true if the email is valid, false otherwise
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Modify the isValidPhoneNumber method to handle plus sign and 12-digit numbers
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Check if the phone number matches the required pattern
        return phoneNumber.matches("\\+?\\d{10,12}");
    }

    // Function to show a toast message
    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}