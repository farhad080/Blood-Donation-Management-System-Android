package com.roktim.blooddonation.ui.admin.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private List<Inventory> inventoryList = new ArrayList<>();
    private final OnInventoryClickListener listener;

    private final String[] bloodGroupColors = {
            "#E53935", "#C62828", "#D81B60", "#AD1457",
            "#8E24AA", "#6A1B9A", "#3949AB", "#283593"
    };

    public interface OnInventoryClickListener {
        void onInventoryClick(Inventory inventory);
        void onAddUnits(Inventory inventory);
        void onReduceUnits(Inventory inventory);
    }

    public InventoryAdapter(OnInventoryClickListener listener) {
        this.listener = listener;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList != null ? inventoryList :  new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        Inventory inventory = inventoryList.get(position);
        holder.bind(inventory, position);
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    class InventoryViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final TextView tvBloodGroup, tvUnits, tvStatus;
        private final ImageButton btnAdd, btnReduce;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_inventory);
            tvBloodGroup = itemView.findViewById(R. id.tv_blood_group);
            tvUnits = itemView.findViewById(R.id.tv_units);
            tvStatus = itemView. findViewById(R.id.tv_status);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnReduce = itemView.findViewById(R.id.btn_reduce);
        }

        public void bind(Inventory inventory, int position) {
            tvBloodGroup.setText(inventory.getBloodGroup());
            tvUnits.setText(String.valueOf(inventory. getUnits()));

            if (inventory. getUnits() <= 5) {
                tvStatus.setText("Critical");
                tvStatus.setTextColor(Color.parseColor("#F44336"));
            } else if (inventory.getUnits() <= 15) {
                tvStatus.setText("Low");
                tvStatus.setTextColor(Color.parseColor("#FF9800"));
            } else {
                tvStatus. setText("Available");
                tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            }

            if (position < bloodGroupColors.length) {
                cardView.setCardBackgroundColor(Color.parseColor(bloodGroupColors[position]));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onInventoryClick(inventory);
            });

            btnAdd. setOnClickListener(v -> {
                if (listener != null) listener.onAddUnits(inventory);
            });

            btnReduce.setOnClickListener(v -> {
                if (listener != null) listener.onReduceUnits(inventory);
            });
        }
    }
}