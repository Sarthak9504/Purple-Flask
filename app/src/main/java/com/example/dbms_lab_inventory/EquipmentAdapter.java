package com.example.dbms_lab_inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentViewHolder> {
    Context context;
    List<EquipmentItem> list;
    private ItemClickListener2 listener;

    public EquipmentAdapter(Context context, List<EquipmentItem> list,ItemClickListener2 listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EquipmentViewHolder(LayoutInflater.from(context).inflate(R.layout.equipments_rc_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.qty.setText(list.get(position).getQuantity());

        holder.card_button.setOnClickListener(view -> listener.onClick(list.get(position)));

        holder.card_button.setOnLongClickListener(view -> {
            listener.onLongPress(list.get(position));
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
