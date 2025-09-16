package com.example.pizzamaniaapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;

public class CheckOutActivity extends AppCompatActivity {
    private PizzaManiaDBOpenHelper dbHelper;
    private SessionManager sessionManager;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        dbHelper = new PizzaManiaDBOpenHelper(this);
        sessionManager = new SessionManager(this);

        TextView totalAmountTextView = findViewById(R.id.totalAmount);
        Button payNowButton = findViewById(R.id.payNowButton);

        // Get the total amount from the Intent
        totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);
        totalAmountTextView.setText("Rs. " + String.format("%.2f", totalAmount));

        // Set up the Pay Now button click listener
        payNowButton.setOnClickListener(v -> {
            int loggedInUserId = sessionManager.getUserId();
            if (loggedInUserId != -1) {
                // Determine the nearest branch (you'll need to implement this logic)
                int nearestBranchId = 1; // Placeholder: you'll need to find the real branch ID

                dbHelper.placeOrder(loggedInUserId, totalAmount, nearestBranchId);
                Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show();
                // Navigate to a confirmation screen or home page
                Intent intent = new Intent(this, LocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }
}