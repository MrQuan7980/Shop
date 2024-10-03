package com.example.mysql.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.OOP.Product;
import com.example.mysql.databinding.IconButtonBinding;
import com.example.mysql.databinding.ItemContainerLikeBinding;

import java.util.List;
import java.util.Locale;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.MyViewHolder>{
    private final List<Product> productList;
    private Context context;

    public AdapterFavorite(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerLikeBinding binding = ItemContainerLikeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.setData(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemContainerLikeBinding binding;
        public MyViewHolder(ItemContainerLikeBinding iconButtonBinding)
        {
            super(iconButtonBinding.getRoot());
            binding = iconButtonBinding;

        }
        void setData(Product product)
        {

        }
    }
}
