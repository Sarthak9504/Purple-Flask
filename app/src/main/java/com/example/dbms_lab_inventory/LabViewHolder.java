package com.example.dbms_lab_inventory;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class LabViewHolder extends RecyclerView.ViewHolder {
    TextView lab_room;
    TextView lab_type;
    CardView button;
    public LabViewHolder(@NonNull View itemView) {
        super(itemView);
        lab_room = itemView.findViewById(R.id.lab_room);
        lab_type = itemView.findViewById(R.id.lab_type);
        button = itemView.findViewById(R.id.card_button);
    }
}
