package com.example.pizzamaniaapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzamaniaapplication.adapters.CartAdapter;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.CartItem;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private PizzaManiaDBOpenHelper dbHelper;
    private RecyclerView cartListView;
    private CartAdapter cartAdapter;
    private ArrayList<CartItem> cartItems;
    private int loggedInUserId;

    private TextView totalAmountText;
    private Button btnCheckout;
    private TextView emptyCartMessage; // New TextView for empty cart message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new PizzaManiaDBOpenHelper(this);
        cartListView = findViewById(R.id.cartListView);
        totalAmountText = findViewById(R.id.totalAmount);
        btnCheckout = findViewById(R.id.btnCheckout);
        emptyCartMessage = findViewById(R.id.emptyCartMessage); // Initialize the new TextView

        SessionManager sessionManager = new SessionManager(this);
        loggedInUserId = sessionManager.getUserId();

        if (loggedInUserId == -1) {
            Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        cartListView.setLayoutManager(new LinearLayoutManager(this));

        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                double total = 0.0;
                for (CartItem item : cartItems) {
                    total += item.getPrice() * item.getQuantity();
                }
                Intent intent = new Intent(this, CheckOutActivity.class);
                intent.putExtra("TOTAL_AMOUNT", total);
                startActivity(intent);
            }
        });
    }

    private void loadCartItems() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cartItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT c.cartId, m.name, m.price, c.quantity, m.imageUrl " +
                        "FROM Cart c JOIN Menu m ON c.pizzaId = m.pizzaId " +
                        "WHERE c.userId = ?",
                new String[]{String.valueOf(loggedInUserId)}
        );

        if (cursor.moveToFirst()) {
            do {
                int cartId = cursor.getInt(0);
                String pizzaName = cursor.getString(1);
                double price = cursor.getDouble(2);
                int qty = cursor.getInt(3);
                String imageUrl = cursor.getString(4);
                cartItems.add(new CartItem(cartId, pizzaName, price, qty, imageUrl));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        if (cartItems.isEmpty()) {
            // Show the empty cart message and hide the other UI elements
            emptyCartMessage.setVisibility(View.VISIBLE);
            cartListView.setVisibility(View.GONE);
            totalAmountText.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
            findViewById(R.id.totalLabel).setVisibility(View.GONE); // Hide the "Total" label
        } else {
            // Show the cart and other UI elements
            emptyCartMessage.setVisibility(View.GONE);
            cartListView.setVisibility(View.VISIBLE);
            totalAmountText.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);
            findViewById(R.id.totalLabel).setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapter(this, cartItems, this::updateTotal);
            cartListView.setAdapter(cartAdapter);
            updateTotal();
        }
    }

    private void updateTotal() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        totalAmountText.setText("Rs. " + String.format("%.2f", total));
    }
}