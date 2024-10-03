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
import com.example.mysql.databinding.ItemContainerEvaluationBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.Evaluatio>{

    private final List<Product> products;
    private final String currentUserId;

    public AdapterComment(List<Product> products, String currentUserId) {
        this.products = products;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public Evaluatio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerEvaluationBinding binding = ItemContainerEvaluationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Evaluatio(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Evaluatio holder, int position) {
        holder.setData(products.get(position));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Product productDle = products.get(position);

        holder.binding.imageDelete.setVisibility(currentUserId.equals(productDle.userId) ? View.VISIBLE : View.GONE);

        holder.binding.imageDelete.setOnClickListener(v -> {

            if (currentUserId.equals(productDle.userId)) {
                database.collection(Constant.KEY_COLLECTION_COMMENT)
                        .document(productDle.commentId)
                        .delete()
                        .addOnSuccessListener(command -> {
                            products.remove(position);
                            notifyItemRemoved(position);

                            notifyItemRangeChanged(position, products.size());

                            Toast.makeText(holder.binding.getRoot().getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(command -> {
                            Toast.makeText(holder.binding.getRoot().getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        });
            }
            else
            {
                Toast.makeText(holder.binding.getRoot().getContext(), "Bạn không thể xóa bình luận này", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public int getItemCount()
    {
        return products.size();
    }

    public class Evaluatio extends RecyclerView.ViewHolder {
        private final ItemContainerEvaluationBinding binding;

        public Evaluatio(ItemContainerEvaluationBinding item) {
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
