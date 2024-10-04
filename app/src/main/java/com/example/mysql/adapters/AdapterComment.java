package com.example.mysql.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.OOP.Product;
import com.example.mysql.database.Constant;
import com.example.mysql.databinding.ItemContainerAnswerBinding;
import com.example.mysql.databinding.ItemContainerEvaluationBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Product> products;
    private final String senderId;
    private static final int VIEW_SENT = 1;
    private static final int VIEW_REP = 2;

    public AdapterComment(List<Product> products, String senderId) {
        this.products = products;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_SENT)
        {
            ItemContainerEvaluationBinding binding = ItemContainerEvaluationBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new SentComment(binding);
        }
        else if (viewType == VIEW_REP)
        {
            ItemContainerAnswerBinding binding = ItemContainerAnswerBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new RepComment(binding);
        }
        else
        {
            return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_SENT)
        {
            ((SentComment) holder).setData(products.get(position));
        }
        else if (getItemViewType(position) == VIEW_REP)
        {
            ((RepComment) holder).setData(products.get(position));
        }
        else
        {
            return;
        }
    }

    @Override
    public int getItemCount()
    {
        return products.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        Product product = products.get(position);

        if(product.comment != null && !product.comment.isEmpty())
        {
            return VIEW_SENT;
        }
        else
        {
            return VIEW_REP;
        }

    }
    public class SentComment extends RecyclerView.ViewHolder {
        private final ItemContainerEvaluationBinding binding;

        public SentComment(ItemContainerEvaluationBinding item) {
            super(item.getRoot());

            binding = item;
        }

        void setData(Product product)
        {
            binding.textName.setText(product.nameUser);
            binding.imageProfile.setImageBitmap(getImage(product.imageUser));
            binding.ratingSao.setRating(Float.parseFloat(product.star));
            binding.textComment.setText(product.comment);
        }
    }

    public class RepComment extends RecyclerView.ViewHolder{
        private final ItemContainerAnswerBinding binding;
        public RepComment(ItemContainerAnswerBinding item)
        {
            super(item.getRoot());

            binding = item;
        }
        void setData(Product product)
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
