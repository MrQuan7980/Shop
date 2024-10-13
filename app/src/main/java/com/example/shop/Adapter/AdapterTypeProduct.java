package com.example.shop.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.Object.Product;
import com.example.shop.databinding.ItemContainerTypeBinding;

import java.util.List;

public class AdapterTypeProduct extends RecyclerView.Adapter<AdapterTypeProduct.Type>{
    public List<Product>   productList;

    public AdapterTypeProduct(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public Type onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerTypeBinding binding = ItemContainerTypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Type(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Type holder, int position) {
        holder.getType(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class Type extends RecyclerView.ViewHolder{
        private final ItemContainerTypeBinding binding;

        public Type(ItemContainerTypeBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
        void getType(Product product)
        {
            binding.btnTypeProduct.setText(product.category);
        }
    }
}
