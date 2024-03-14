package com.example.dbms_lab_inventory;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EquipmentViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView qty;
    ImageView image;
    CardView card_button;
    public EquipmentViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.equip_name);
        qty = itemView.findViewById(R.id.qty);
        image = itemView.findViewById(R.id.image);
        card_button = itemView.findViewById(R.id.card_button);
    }
}
