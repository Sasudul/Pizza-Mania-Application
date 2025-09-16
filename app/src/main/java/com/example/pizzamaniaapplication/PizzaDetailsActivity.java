package com.example.pizzamaniaapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.Pizza;

public class PizzaDetailsActivity extends AppCompatActivity {

    private PizzaManiaDBOpenHelper dbHelper;
    private SessionManager sessionManager;
    private Pizza currentPizza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_details);

        ImageView pizzaImage = findViewById(R.id.pizzaImage);
        TextView titleText = findViewById(R.id.titleText);
        TextView priceText = findViewById(R.id.priceText);
        TextView ingredientsText = findViewById(R.id.ingredientsText);
        Button addBtn = findViewById(R.id.addBtn);

        dbHelper = new PizzaManiaDBOpenHelper(this);
        sessionManager = new SessionManager(this);

        int pizzaId = getIntent().getIntExtra("pizza_id", -1);
        if (pizzaId != -1) {
            currentPizza = dbHelper.getPizzaById(pizzaId);
            if (currentPizza != null) {
                titleText.setText(currentPizza.getName());
                priceText.setText("Rs. " + String.format("%.2f", currentPizza.getPrice()));
                ingredientsText.setText(currentPizza.getDescription());
                try {
                    int imageResId = Integer.parseInt(currentPizza.getImageUrl());
                    pizzaImage.setImageResource(imageResId);
                } catch (Exception e) {
                    pizzaImage.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        }

        addBtn.setOnClickListener(v -> {
            int userId = sessionManager.getUserId();
            if (userId != -1 && currentPizza != null) {
                dbHelper.addToCart(userId, currentPizza.getId(), 1, "");
                Toast.makeText(this, currentPizza.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}