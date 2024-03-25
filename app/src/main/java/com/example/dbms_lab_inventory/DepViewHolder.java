package com.example.dbms_lab_inventory;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DepViewHolder extends RecyclerView.ViewHolder {
    TextView dep_name;
    CardView btn;
    public DepViewHolder(@NonNull View itemView) {
        super(itemView);
        dep_name = itemView.findViewById(R.id.dep);
        btn = itemView.findViewById(R.id.card_button);
    }
}
