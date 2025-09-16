package com.example.pizzamaniaapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzamaniaapplication.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private TextView btnRegister;
    private Button btnLogin;
    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.loginButton);
        btnRegister = findViewById(R.id.registerText);

        authHelper = new AuthHelper(this);

        btnLogin.setOnClickListener(v -> {
            String email = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if(email.equals("admin") && password.equals("admin1234")) {
                startActivity(new Intent(this, AdminDashboard.class));
                finish();
            }
            else {
                {
                    User user = authHelper.loginUser(email, password);
                    if (user != null) {
                        SharedPreferences prefs = getSharedPreferences("PizzaManiaPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", user.getUsername());
                        editor.putInt("LOGGED_IN_USER_ID", user.getUserId());
                        editor.apply();

                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, LocationActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
