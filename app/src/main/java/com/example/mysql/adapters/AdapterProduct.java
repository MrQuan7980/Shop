package com.example.mysql.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.OOP.Product;
import com.example.mysql.databinding.ItemContaierProductBinding;
import com.example.mysql.interfaces.ProductInterface;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

import android.content.Context;
public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.Products>{

    private final List<Product> products;
    private final ProductInterface productInterface;

    public AdapterProduct(List<Product> products, ProductInterface productInterface) {
        this.products = products;
        this.productInterface = productInterface;
    }

    @NonNull
    @Override
    public Products onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContaierProductBinding binding = ItemContaierProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new Products(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Products holder, int position) {
        holder.setData(products.get(position));
        
    }

    @Override
    public int getItemCount()
    {
        return products.size();
    }


    public class Products extends RecyclerView.ViewHolder
    {
        private final ItemContaierProductBinding binding;

        public Products(ItemContaierProductBinding item)
        {
            super(item.getRoot());

            binding = item;
        }

        void setData(Product product)
        {
            binding.title.setText(product.title);

            String formattedMoney = String.format(Locale.getDefault(), "%.2f", product.money);
            binding.monney.setText(formattedMoney);

            binding.image.setImageBitmap(getImage(product.image));

            binding.getRoot().setOnClickListener(v -> {
                productInterface.onClickProduct(product);
            });
        }

        private Bitmap getImage(String image)
        {
            if (image != null && !image.isEmpty())
            {
                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
            else
            {
                return null;
            }
        }
    }
}
