package com.example.shop.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.Object.Product;
import com.example.shop.databinding.ItemContainerFavoriteBinding;

import java.util.List;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.Favorite>{
    private final List<Product> productList;

    public AdapterFavorite(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public Favorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerFavoriteBinding binding = ItemContainerFavoriteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Favorite(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Favorite holder, int position) {
        holder.getData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class Favorite extends RecyclerView.ViewHolder{
        private final ItemContainerFavoriteBinding binding;

        public Favorite(ItemContainerFavoriteBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
        void getData(Product product)
        {
            binding.imageProduct.setImageBitmap(getImage(product.image_two));
            binding.textTitle.setText(product.title);
            binding.imageShop.setImageBitmap(getImage(product.shop.image));
            binding.nameShop.setText(product.shop.name);
            binding.textCity.setText(product.shop.city);
            binding.money.setText(String.valueOf(product.money));

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
