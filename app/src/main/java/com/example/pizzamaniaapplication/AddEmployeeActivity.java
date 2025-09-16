package com.example.pizzamaniaapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AddEmployeeActivity extends AppCompatActivity {

    private ImageView profileAvatar;
    private Button btnUploadProfile, btnSaveEmployee;
    private AutoCompleteTextView etGender, etPosition;
    private EditText etName, etPhone, etEmail, etPassword, etAddress, etDob;

    private Bitmap selectedBitmap;
    private PizzaManiaDBOpenHelper dbHelper;

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        selectedBitmap = bitmap;
                        profileAvatar.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        dbHelper = new PizzaManiaDBOpenHelper(this);

        profileAvatar = findViewById(R.id.iv_profile_avatar);
        btnUploadProfile = findViewById(R.id.btn_upload_profile);
        btnSaveEmployee = findViewById(R.id.btn_save_employee);

        etName = findViewById(R.id.et_employee_name);
        etPhone = findViewById(R.id.et_employee_phone);
        etEmail = findViewById(R.id.et_employee_email);
        etPassword = findViewById(R.id.et_employee_password);
        etAddress = findViewById(R.id.et_employee_address);
        etDob = findViewById(R.id.et_employee_dob);
        etGender = findViewById(R.id.et_gender);
        etPosition = findViewById(R.id.et_position);

        btnUploadProfile.setOnClickListener(v -> openGallery());

        etDob.setOnClickListener(v -> showDatePicker());

        setupDropdowns();

        btnSaveEmployee.setOnClickListener(v -> saveEmployee());
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhoto);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> etDob.setText(year + "-" + (month + 1) + "-" + dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setupDropdowns() {
        String[] genders = {"Male", "Female", "Other"};
        String[] positions = {"Manager", "Chef", "Cashier", "Delivery", "Staff"};

        etGender.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genders));
        etPosition.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, positions));
    }

    private void saveEmployee() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String position = etPosition.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()
                || address.isEmpty() || dob.isEmpty() || gender.isEmpty() || position.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] photoBytes = null;
        if (selectedBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            photoBytes = stream.toByteArray();
        }

        try {
            dbHelper.insertStaff(dbHelper.getWritableDatabase(),
                    name, email, phone, address, password, position, gender, dob, photoBytes);
            Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving employee", Toast.LENGTH_SHORT).show();
        }
    }
}
