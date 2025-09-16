package com.example.pizzamaniaapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzamaniaapplication.R;
import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.Staff;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private Context context;
    private List<Staff> staffList;
    private PizzaManiaDBOpenHelper dbHelper;

    public StaffAdapter(Context context, List<Staff> staffList) {
        this.context = context;
        this.staffList = staffList;
        this.dbHelper = new PizzaManiaDBOpenHelper(context);
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        holder.tvName.setText(staff.getName());
        holder.tvEmail.setText(staff.getEmail());
        holder.tvPhone.setText(staff.getPhone());

        holder.btnDelete.setOnClickListener(v -> {
            boolean isDeleted = dbHelper.deleteStaff(staff.getStaffId());
            if (isDeleted) {
                // Remove the item from the list and notify the adapter
                staffList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, staffList.size());
                Toast.makeText(context, "Staff member deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete staff member", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle update button click (for later implementation)
        holder.btnUpdate.setOnClickListener(v -> {
            Toast.makeText(context, "Update function coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone;
        Button btnUpdate, btnDelete;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}