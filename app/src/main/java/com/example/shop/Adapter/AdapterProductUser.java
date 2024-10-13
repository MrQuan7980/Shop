package com.example.shop.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.Interface.OnClickProduct;
import com.example.shop.Object.Product;
import com.example.shop.databinding.ItemContainerProductUserBinding;

import java.util.List;

public class AdapterProductUser extends RecyclerView.Adapter<AdapterProductUser.ProductUser> {

    private final List<Product> products;
    private final OnClickProduct onClickProduct;

    public AdapterProductUser(List<Product> products, OnClickProduct onClickProduct) {
        this.products = products;
        this.onClickProduct = onClickProduct;
    }

    @NonNull
    @Override
    public ProductUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerProductUserBinding binding = ItemContainerProductUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductUser(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductUser holder, int position) {
        holder.setData(products.get(position));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductUser extends RecyclerView.ViewHolder {

        private final ItemContainerProductUserBinding binding;

        public ProductUser(ItemContainerProductUserBinding itemContainerProductUserBinding) {
            super(itemContainerProductUserBinding.getRoot());
            binding = itemContainerProductUserBinding;
        }

        void setData(Product product)
        {
            binding.titleProduct.setText(product.title);
            binding.imageProduct.setImageBitmap(getImage(product.image_two));
            binding.moneyProduct.setText(String.valueOf(product.money));

            binding.getRoot().setOnClickListener(v -> {
                onClickProduct.onClick(product);
            });
        }
        private Bitmap getImage(String image)
        {
            if (image != null && !image.isEmpty())
            {
                byte[] bytes = Base64.decode(image, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            else
            {
                return null;
            }
        }
    }
}
