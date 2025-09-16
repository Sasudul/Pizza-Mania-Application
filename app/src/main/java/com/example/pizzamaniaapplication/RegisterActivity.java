package com.example.pizzamaniaapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzamaniaapplication.models.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etUsername, etPhone, etEmail, etAddress, etPassword, etConfirmPassword;
    private Button btnRegister;
    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bind views with new XML IDs
        etName = findViewById(R.id.name_edit_text);
        etUsername = findViewById(R.id.username_edit_text);
        etPhone = findViewById(R.id.phone_edit_text);
        etEmail = findViewById(R.id.email_edit_text);
        etAddress = findViewById(R.id.address_edit_text);
        etPassword = findViewById(R.id.password_edit_text);
        etConfirmPassword = findViewById(R.id.confirm_password_edit_text);
        btnRegister = findViewById(R.id.register_now_button);

        authHelper = new AuthHelper(this);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Basic validation
            if (name.isEmpty() || username.isEmpty() || phone.isEmpty() ||
                    email.isEmpty() || address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create user object
            User user = new User(
                    0,
                    username,
                    name,
                    email,
                    phone,
                    address
            );

            boolean success = authHelper.registerUser(user, password);

            if (success) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                finish(); // Go back to login
            } else {
                Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
