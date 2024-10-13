package com.example.shop.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.Object.Comment;
import com.example.shop.databinding.ItemContainerCommentBinding;
import com.example.shop.databinding.ItemContainerCommentShopBinding;

import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ListComment> {

    private List<Comment> commentList;

    public AdapterComment(List<Comment> commentList)
    {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ListComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerCommentBinding binding = ItemContainerCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListComment(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListComment holder, int position) {
        holder.getData(commentList.get(position));

        holder.binding.textTime.setOnClickListener(v -> {
            holder.binding.rating.setVisibility(holder.binding.rating.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ListComment extends RecyclerView.ViewHolder{
        private final ItemContainerCommentBinding binding;

        public ListComment(ItemContainerCommentBinding item)
        {
            super(item.getRoot());

            binding = item;
        }
        void getData(Comment comment)
        {
            binding.imageProfile.setImageBitmap(getImage(comment.user.image));
            binding.textName.setText(comment.user.name);
            binding.textTime.setText(comment.date);
            binding.rating.setRating(Float.parseFloat(String.valueOf(comment.rating)));
            binding.textComment.setText(comment.comment);
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
