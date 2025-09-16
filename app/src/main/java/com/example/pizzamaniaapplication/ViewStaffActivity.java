package com.example.pizzamaniaapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzamaniaapplication.adapters.StaffAdapter;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.Staff;

import java.util.List;

public class ViewStaffActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffAdapter adapter;
    private PizzaManiaDBOpenHelper dbHelper;

    private ImageView BackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employees_list); // Create this XML file

        dbHelper = new PizzaManiaDBOpenHelper(this);
        recyclerView = findViewById(R.id.recyclerViewStaff); // Add this RecyclerView to your activity_view_employees.xml
        BackArrow = findViewById(R.id.back_arrow_icon);
        // Get the list of staff from the database
        List<Staff> staffList = dbHelper.getAllStaff();

        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StaffAdapter(this, staffList);
        recyclerView.setAdapter(adapter);

        BackArrow.setOnClickListener(v -> {
            Intent intent = new Intent(ViewStaffActivity.this, AdminDashboard.class);
            startActivity(intent);
        });
    }
}