package com.example.dbms_lab_inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LabAdapter extends RecyclerView.Adapter<LabViewHolder> {

    Context context;
    List<LabItem> list;
    private ItemClickListener listener;

    public LabAdapter(Context context, List<LabItem> list,ItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LabViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LabViewHolder holder, int position) {
        holder.lab_room.setText(list.get(position).getRoom_num());
        holder.lab_type.setText(list.get(position).getLab_type());

        holder.button.setOnClickListener(view -> listener.onClick(list.get(position)));

        holder.button.setOnLongClickListener(view -> {
            listener.onLongPress(list.get(position));
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
