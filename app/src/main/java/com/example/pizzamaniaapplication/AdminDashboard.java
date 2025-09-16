package com.example.pizzamaniaapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzamaniaapplication.LoginActivity;
import com.example.pizzamaniaapplication.R;
import com.example.pizzamaniaapplication.ViewStaffActivity;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Make sure this matches your XML file name

        // Find the buttons by their IDs
        Button btnViewOrders = findViewById(R.id.btnViewOrders);
        Button btnAddItems = findViewById(R.id.btnAddItems);
        Button btnViewEmployees = findViewById(R.id.btnViewEmployees);
        Button btnAddEmployees = findViewById(R.id.btnAddEmployees);

        // Set up click listeners for each button
        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the ViewOrdersActivity
                Intent intent = new Intent(AdminDashboard.this, ViewOrdersActivity.class);
                startActivity(intent);
            }
        });

        btnAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the AddItemsActivity
                Intent intent = new Intent(AdminDashboard.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        btnViewEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the ViewEmployeesActivity
                Intent intent = new Intent(AdminDashboard.this, ViewStaffActivity.class);
                startActivity(intent);
            }
        });

        btnAddEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the AddEmployeesActivity
                Intent intent = new Intent(AdminDashboard.this, AddEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }
}