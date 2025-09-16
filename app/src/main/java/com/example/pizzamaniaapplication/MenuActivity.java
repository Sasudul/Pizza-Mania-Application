package com.example.pizzamaniaapplication;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzamaniaapplication.adapters.PizzaAdapter;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.Pizza;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView pizzaRecyclerView;
    private PizzaAdapter adapter;
    private List<Pizza> pizzaList;
    private PizzaManiaDBOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_menu);

        pizzaRecyclerView = findViewById(R.id.pizzaRecyclerView);
        pizzaRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new PizzaManiaDBOpenHelper(this);

        // 🔹 Get branchId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("PizzaManiaPrefs", MODE_PRIVATE);
        int branchId = prefs.getInt("BRANCH_ID", 1);

        // 🔹 Get branch name for header
        String branchName;
        switch (branchId) {
            case 2: branchName = "Galle"; break;
            case 3: branchName = "Matara"; break;
            default: branchName = "Colombo"; break;
        }

        pizzaList = loadMenuFromDB(branchId);

        adapter = new PizzaAdapter(this, pizzaList, branchName); // pass branch name
        pizzaRecyclerView.setAdapter(adapter);
    }

    private List<Pizza> loadMenuFromDB(int branchId) {
        List<Pizza> pizzas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Menu WHERE branchId = ?", new String[]{String.valueOf(branchId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("pizzaId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                pizzas.add(new Pizza(id, name, description, price, imageUrl));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pizzas;
    }

}
