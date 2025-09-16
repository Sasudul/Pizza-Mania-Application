package com.example.pizzamaniaapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzamaniaapplication.R;
import com.example.pizzamaniaapplication.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.txtOrderId.setText("Order #" + order.getOrderId());
        holder.txtItems.setText(order.getItems());
        holder.txtPrice.setText("Rs. " + order.getTotalPrice());
        holder.txtStatus.setText("Status: " + order.getCurrentStatus());
        holder.txtETA.setText("ETA: " + order.getEstimatedDelivery());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtItems, txtPrice, txtStatus, txtETA;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtItems = itemView.findViewById(R.id.txtItems);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtETA = itemView.findViewById(R.id.txtETA);
        }
    }
}
