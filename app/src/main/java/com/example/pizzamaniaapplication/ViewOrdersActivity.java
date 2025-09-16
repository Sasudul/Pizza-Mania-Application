package com.example.pizzamaniaapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzamaniaapplication.adapters.OrderAdapter;
import com.example.pizzamaniaapplication.adapters.OrderAdapter;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.Order;

import java.util.List;

public class ViewOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private OrderAdapter adapter;
    private PizzaManiaDBOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new PizzaManiaDBOpenHelper(this);
        List<Order> orders = dbHelper.getAllOrders();

        adapter = new OrderAdapter(this, orders);
        recyclerOrders.setAdapter(adapter);
    }
}
