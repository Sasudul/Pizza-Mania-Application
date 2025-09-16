package com.example.pizzamaniaapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzamaniaapplication.adapters.OrderAdapter;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.Order;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private TextView emptyOrderMessage;
    private PizzaManiaDBOpenHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        emptyOrderMessage = findViewById(R.id.emptyOrderMessage);
        dbHelper = new PizzaManiaDBOpenHelper(this);
        sessionManager = new SessionManager(this);

        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the logged-in user's ID
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Please log in to view your order history.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Load the orders from the database
        loadOrders(userId);
    }

    private void loadOrders(int userId) {
        List<Order> orders = dbHelper.getOrdersForUser(userId);

        if (orders.isEmpty()) {
            ordersRecyclerView.setVisibility(View.GONE);
            emptyOrderMessage.setVisibility(View.VISIBLE);
        } else {
            ordersRecyclerView.setVisibility(View.VISIBLE);
            emptyOrderMessage.setVisibility(View.GONE);
            orderAdapter = new OrderAdapter(this, orders);
            ordersRecyclerView.setAdapter(orderAdapter);
        }
    }
}