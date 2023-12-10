package com.example.contactsharingqr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRGenerationActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String NAME_KEY = "name";
    private static final String PHONE_KEY = "phone";

    private EditText editTextName;
    private EditText editTextPhone;
    private ImageView imageViewQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generation);

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        imageViewQR = findViewById(R.id.imageViewQR);
        Button btnGenerateQR = findViewById(R.id.btnGenerateQR);

        btnGenerateQR.setOnClickListener(view -> {
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                // Save name and phone in SharedPreferences for future use
                saveData(name, phone);

                // Generate and display QR code
                generateAndDisplayQR(name, phone);
            }
        });

        // Check if data is already stored in SharedPreferences
        String storedName = getData(NAME_KEY);
        String storedPhone = getData(PHONE_KEY);

        if (storedName != null && storedPhone != null) {
            // Data is available, populate EditTexts and display QR code
            editTextName.setText(storedName);
            editTextPhone.setText(storedPhone);
            generateAndDisplayQR(storedName, storedPhone);
        }
    }

    private void generateAndDisplayQR(String name, String phone) {
        // Generate QR code using ZXing library
        QRCodeWriter writer = new QRCodeWriter();
        try {
            String qrContent = "BEGIN:VCARD\n" +
                    "VERSION:3.0\n" +
                    "FN:" + name + "\n" +
                    "TEL:" + phone + "\n" +
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

    private void saveData(String name, String phone) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(NAME_KEY, name);
        editor.putString(PHONE_KEY, phone);
        editor.apply();
    }

    private String getData(String key) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, null);
    }
}
