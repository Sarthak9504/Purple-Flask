package com.example.dbms_lab_inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LabAdapter extends RecyclerView.Adapter<LabViewHolder> {

    Context context;
    List<LabItem> list;

    public LabAdapter(Context context, List<LabItem> list) {
        this.context = context;
        this.list = list;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
