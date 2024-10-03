package com.example.mysql.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.databinding.IconButtonBinding;

import java.util.List;

public class AdapterTitle extends RecyclerView.Adapter<AdapterTitle.MyViewHolder>{
    private List<String> dataList;
    private int selectedPosition = -1;

    public AdapterTitle(List<String> dataList)
    {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IconButtonBinding binding = IconButtonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String data = dataList.get(position);
        holder.setDate(data);

        if (selectedPosition == -1 && position == 0)
        {
            holder.binding.textView2.setVisibility(View.VISIBLE);
            holder.binding.textView.setVisibility(View.GONE);
        }
        else if (position == selectedPosition)
        {
            holder.binding.textView2.setVisibility(View.VISIBLE);
            holder.binding.textView.setVisibility(View.GONE);
        }
        else
        {
            holder.binding.textView2.setVisibility(View.GONE);
            holder.binding.textView.setVisibility(View.VISIBLE);
        }

        holder.binding.textView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private final IconButtonBinding binding;
        public boolean isSelected = false;
        public MyViewHolder(IconButtonBinding iconButtonBinding)
        {
            super(iconButtonBinding.getRoot());
            binding = iconButtonBinding;

        }
        private void setDate(String data)
        {
            binding.textView.setText(data);
            binding.textView2.setText(data);
        }
    }
}
