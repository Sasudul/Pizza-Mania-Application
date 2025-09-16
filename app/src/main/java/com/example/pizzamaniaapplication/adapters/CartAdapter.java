package com.example.pizzamaniaapplication.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzamaniaapplication.R;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartList;
    private PizzaManiaDBOpenHelper dbHelper;
    private Runnable onCartUpdated; // ✅ callback to CartActivity

    // Constructor
    public CartAdapter(Context context, List<CartItem> cartList, Runnable onCartUpdated) {
        this.context = context;
        this.cartList = cartList;
        this.onCartUpdated = onCartUpdated;
        dbHelper = new PizzaManiaDBOpenHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        holder.tvItemName.setText(item.getPizzaName());
        holder.tvItemPrice.setText("Rs. " + item.getPrice());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        try {
            int resId = Integer.parseInt(item.getImageUrl());
            holder.ivPizzaImage.setImageResource(resId);
        } catch (Exception e) {
            holder.ivPizzaImage.setImageResource(R.drawable.italian_chicken_pizza);
        }

        // ✅ Increment quantity
        holder.btnIncrement.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            updateCartItem(item);
            notifyItemChanged(holder.getAdapterPosition());
            if (onCartUpdated != null) onCartUpdated.run(); // update total
        });

        // ✅ Decrement quantity
        holder.btnDecrement.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                updateCartItem(item);
                notifyItemChanged(holder.getAdapterPosition());
                if (onCartUpdated != null) onCartUpdated.run();
            } else {
                Toast.makeText(context, "Minimum 1 item required!", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Delete item
        holder.btnDelete.setOnClickListener(v -> {
            deleteCartItem(item.getCartId());
            int pos = holder.getAdapterPosition();
            cartList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, cartList.size());
            if (onCartUpdated != null) onCartUpdated.run(); // update total
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // 🔹 Update quantity in DB
    private void updateCartItem(CartItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE Cart SET quantity = ? WHERE cartId = ?",
                new Object[]{item.getQuantity(), item.getCartId()});
        db.close();
    }

    // 🔹 Delete from DB
    private void deleteCartItem(int cartId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Cart WHERE cartId = ?", new Object[]{cartId});
        db.close();
        Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemPrice, tvQuantity;
        ImageView ivPizzaImage;
        ImageButton btnDelete, btnIncrement, btnDecrement;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPizzaImage = itemView.findViewById(R.id.ivPizzaImage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnIncrement = itemView.findViewById(R.id.btnIncrement);
            btnDecrement = itemView.findViewById(R.id.btnDecrement);
        }
    }
}
