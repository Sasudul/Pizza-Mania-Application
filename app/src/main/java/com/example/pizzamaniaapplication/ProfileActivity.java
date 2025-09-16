package com.example.pizzamaniaapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 101;
    private static final int CAMERA_PERMISSION = 200;

    private ImageView profilePic, backIcon, callIcon;


    private EditText nameEt, usernameEt, phoneEt, emailEt, addressEt, passwordEt;
    private Button updateBtn;

    private PizzaManiaDBOpenHelper dbHelper;
    private int userId = -1;
    private byte[] profileImageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new PizzaManiaDBOpenHelper(this);

        // UI refs
        profilePic = findViewById(R.id.profile_picture_placeholder);
        backIcon = findViewById(R.id.back_arrow_icon);
        callIcon = findViewById(R.id.call_icon);
        nameEt = findViewById(R.id.name_edit_text);
        usernameEt = findViewById(R.id.username_edit_text);
        phoneEt = findViewById(R.id.phone_edit_text);
        emailEt = findViewById(R.id.email_edit_text);
        addressEt = findViewById(R.id.address_edit_text);
        passwordEt = findViewById(R.id.password_edit_text);
        updateBtn = findViewById(R.id.edit_details_button);

        // ✅ Get logged in userId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("PizzaManiaPrefs", MODE_PRIVATE);
        userId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        if (userId != -1) {
            loadUserData(userId);
        } else {
            Toast.makeText(this, "No user logged in!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Update details
        updateBtn.setOnClickListener(v -> updateUserData());

        // Profile picture click → choose Camera or Gallery
        profilePic.setOnClickListener(v -> showImagePickerDialog());

        callIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });

        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LocationActivity.class);
            startActivity(intent);
        });
    }

    // 🔹 Dialog with Camera/Gallery options
    private void showImagePickerDialog() {
        String[] options = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Profile Picture");
        builder.setItems(options, (DialogInterface dialog, int which) -> {
            if (which == 0) { // Camera
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                } else {
                    openCamera();
                }
            } else if (which == 1) { // Gallery
                openGallery();
            }
        });
        builder.show();
    }

    // Load user data from SQLite
    private void loadUserData(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE userId=?",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(PizzaManiaDBOpenHelper.COLUMN_USER_NAME));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(PizzaManiaDBOpenHelper.COLUMN_USER_USERNAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(PizzaManiaDBOpenHelper.COLUMN_USER_PHONE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(PizzaManiaDBOpenHelper.COLUMN_USER_EMAIL));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(PizzaManiaDBOpenHelper.COLUMN_USER_ADDRESS));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(PizzaManiaDBOpenHelper.COLUMN_USER_PASSWORD));
            profileImageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(PizzaManiaDBOpenHelper.COLUMN_USER_PHOTO));

            // Set values
            nameEt.setText(name);
            usernameEt.setText(username);
            phoneEt.setText(phone);
            emailEt.setText(email);
            addressEt.setText(address);
            passwordEt.setText(password);

            if (profileImageBytes != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(profileImageBytes, 0, profileImageBytes.length);
                profilePic.setImageBitmap(bmp);
            } else {
                profilePic.setImageResource(R.drawable.profile); // default image
            }
        }
        cursor.close();
        db.close();
    }

    // Update user data in SQLite
    private void updateUserData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PizzaManiaDBOpenHelper.COLUMN_USER_NAME, nameEt.getText().toString());
        values.put(PizzaManiaDBOpenHelper.COLUMN_USER_USERNAME, usernameEt.getText().toString());
        values.put(PizzaManiaDBOpenHelper.COLUMN_USER_PHONE, phoneEt.getText().toString());
        values.put(PizzaManiaDBOpenHelper.COLUMN_USER_EMAIL, emailEt.getText().toString());
        values.put(PizzaManiaDBOpenHelper.COLUMN_USER_ADDRESS, addressEt.getText().toString());
        values.put(PizzaManiaDBOpenHelper.COLUMN_USER_PASSWORD, passwordEt.getText().toString());

        if (profileImageBytes != null) {
            values.put(PizzaManiaDBOpenHelper.COLUMN_USER_PHOTO, profileImageBytes);
        }

        int rows = db.update(PizzaManiaDBOpenHelper.TABLE_USERS, values,
                "userId=?", new String[]{String.valueOf(userId)});

        db.close();

        if (rows > 0) {
            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Open Gallery
    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_GALLERY);
    }

    // Open Camera
    private void openCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_GALLERY) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    profilePic.setImageBitmap(bitmap);
                    profileImageBytes = bitmapToByteArray(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    profilePic.setImageBitmap(bitmap);
                    profileImageBytes = bitmapToByteArray(bitmap);
                }
            }
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
