package com.java.eventregistrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class FourthActivity extends AppCompatActivity {
    private ImageView qrCodeImageView;
    private EditText username;
    private EditText email;
    private EditText number;
    private EditText college;
    private Button submit;
    private CheckBox checkboxPrice;

    private Button Home;

    DBHelper DB;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        username = findViewById(R.id.editTextName);
        email = findViewById(R.id.userEmail);
        number = findViewById(R.id.editTextNumber);
        college = findViewById(R.id.editTextCollege);
        checkboxPrice = findViewById(R.id.checkboxPrice);
        submit = findViewById(R.id.buttonSubmit);
        Home = findViewById(R.id.button4);
        DB = new DBHelper(this);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourthActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                String name = username.getText().toString().trim();
                String em = email.getText().toString().trim();
                String num = number.getText().toString().trim();
                String col = college.getText().toString().trim();
                boolean priceAccepted = checkboxPrice.isChecked();

                if (priceAccepted && !name.isEmpty() && !em.isEmpty() && !num.isEmpty() && !col.isEmpty()) {
                    try {
                        String registrationData = "Name: " + name + "\n" +
                                "Email: " + em + "\n" +
                                "Number: " + num + "\n" +
                                "College: " + col;

                        Bitmap qrCode = generateQRCode(registrationData);
                        qrCodeImageView.setImageBitmap(qrCode);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }


                Boolean insert = DB.insertData(name, em, num, col);
                if (!name.isEmpty()) {
                    try {
                        // Generate the QR code
                        Bitmap qrCode = generateQRCode(name);

                        qrCodeImageView.setImageBitmap(qrCode);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                if (!em.isEmpty()) {
                    try {
                        // Generate the QR code
                        Bitmap qrCode = generateQRCode(em);

                        qrCodeImageView.setImageBitmap(qrCode);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

                if (insert == true) {
                    Toast.makeText(FourthActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                }
                // Check if required fields are empty
                if (name.isEmpty() || em.isEmpty() || num.isEmpty() || col.isEmpty()) {
                    // Show an error dialog or message indicating that required fields are missing
                    showErrorMessage("Please fill in all required fields.");
                } else {
                    Boolean checkuser = DB.checkusername(name);
                    // All required fields are filled; you can proceed with registration
                    // Your registration logic goes here
                }
            }
            });
        }

    private Bitmap generateQRCode(String data) throws WriterException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 400, 400);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return bitmap;
    }


    private void showErrorMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }


}

