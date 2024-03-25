package com.example.dbms_lab_inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DepAdapter extends RecyclerView.Adapter<DepViewHolder> {
    Context context;
    List<DepItem> list;
    ItemClickListener3 listener3;

    public DepAdapter(Context context, List<DepItem> list, ItemClickListener3 listener3) {
        this.context = context;
        this.list = list;
        this.listener3 = listener3;
    }

    @NonNull
    @Override
    public DepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DepViewHolder(LayoutInflater.from(context).inflate(R.layout.department_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DepViewHolder holder, int position) {
        holder.dep_name.setText(list.get(position).getDep_name());

        holder.btn.setOnClickListener(view -> listener3.onClick(list.get(position)));

        holder.btn.setOnLongClickListener(view -> {
            listener3.onLongPress(list.get(position));
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
