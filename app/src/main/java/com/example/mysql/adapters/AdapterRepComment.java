package com.example.mysql.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.OOP.Product;
import com.example.mysql.databinding.ItemContainerAnswerBinding;

import java.util.List;

public class AdapterRepComment extends RecyclerView.Adapter<AdapterRepComment.RepComment>{

    private final List<Product> productList;

    public AdapterRepComment(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public RepComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContainerAnswerBinding itemContainerAnswerBinding = ItemContainerAnswerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new RepComment(itemContainerAnswerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RepComment holder, int position) {
        holder.getData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class RepComment extends RecyclerView.ViewHolder{
        private final ItemContainerAnswerBinding binding;
        public RepComment(ItemContainerAnswerBinding item)
        {
            super(item.getRoot());

            binding = item;
        }
        void getData(Product product)
        {
            binding.textName.setText(product.nameUser);
            binding.imageProfile.setImageBitmap(getImage(product.imageUser));
            binding.textComment.setText(product.comment);
        }
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
