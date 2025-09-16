package com.example.pizzamaniaapplication.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzamaniaapplication.R;
import com.example.pizzamaniaapplication.PizzaDetailsActivity; // New Activity
import com.example.pizzamaniaapplication.SessionManager;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.Pizza;
import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private Context context;
    private List<Pizza> pizzaList;
    private String branchName;

    public PizzaAdapter(Context context, List<Pizza> pizzaList, String branchName) {
        this.context = context;
        this.pizzaList = pizzaList;
        this.branchName = branchName;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pizza, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        holder.name.setText(pizza.getName());
        holder.description.setText(pizza.getDescription());
        holder.price.setText("Rs. " + pizza.getPrice());
        holder.pizzaBranch.setText("Available at: " + branchName);

        try {
            int imageResId = Integer.parseInt(pizza.getImageUrl());
            holder.image.setImageResource(imageResId);
        } catch (Exception e) {
            holder.image.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // Handle item click to open PizzaDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PizzaDetailsActivity.class);
            intent.putExtra("pizza_id", pizza.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    public static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price, pizzaBranch;
        ImageView image;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pizzaName);
            description = itemView.findViewById(R.id.pizzaDescription);
            price = itemView.findViewById(R.id.pizzaPrice);
            image = itemView.findViewById(R.id.pizzaImage);
            pizzaBranch = itemView.findViewById(R.id.pizzaBranch);
        }
    }
}