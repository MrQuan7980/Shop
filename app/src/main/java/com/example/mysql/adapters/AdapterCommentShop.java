package com.example.mysql.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.OOP.Product;
import com.example.mysql.databinding.ItemContainerCommentShopBinding;
import com.example.mysql.interfaces.ClickComment;

import java.util.List;

public class AdapterCommentShop extends RecyclerView.Adapter<AdapterCommentShop.CommentShop>{
    private final List<Product> products;
    private final ClickComment clickComment;

    public AdapterCommentShop(List<Product> products, ClickComment clickComment) {
        this.products = products;
        this.clickComment = clickComment;
    }

    @NonNull
    @Override
    public CommentShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContainerCommentShopBinding binding = ItemContainerCommentShopBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CommentShop(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentShop holder, int position) {
        holder.getData(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CommentShop extends RecyclerView.ViewHolder{

        private final ItemContainerCommentShopBinding binding;

        public CommentShop(ItemContainerCommentShopBinding item)
        {
            super(item.getRoot());

            binding = item;
        }

        void getData(Product product)
        {
            binding.imageProfile.setImageBitmap(getImage(product.imageUser));
            binding.star.setText(product.star);
            binding.textName.setText(product.nameUser);
            binding.titleProduct.setText(product.title);

            binding.getRoot().setOnClickListener(v -> {
                clickComment.onClick(product);
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
