package com.example.mysql.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.OOP.Product;
import com.example.mysql.databinding.ItemContainerProductShopBinding;
import com.example.mysql.shop.EditProduct;
import com.example.mysql.database.Constant;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;

public class AdapterShop extends RecyclerView.Adapter<AdapterShop.Products>{

    private final List<Product> products;

    public AdapterShop(List<Product> products) {
        this.products = products;
    }


    @NonNull
    @Override
    public Products onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerProductShopBinding binding = ItemContainerProductShopBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new Products(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Products holder, int position) {
        holder.setData(products.get(position));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        holder.binding.delete.setOnClickListener(v -> {
            Product productDelete = products.get(position);

            database.collection(Constant.KEY_TABLE_PRODUCT)
                    .document(productDelete.id)
                    .delete()
                    .addOnSuccessListener(command -> {

                        products.remove(position);

                        notifyItemRemoved(position);

                        notifyItemRangeChanged(position, products.size());

                        Toast.makeText(holder.binding.getRoot().getContext(), "Sản phẩm đã bị xóa", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(holder.binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        holder.binding.edit.setOnClickListener(v -> {
            Product productEdit = products.get(position);

            String id = productEdit.id;
            String title = productEdit.title;
            String image = productEdit.image;
            String introduction = productEdit.introduction;
            String genre = productEdit.genre;
            String date = productEdit.dateTime;
            Double money = productEdit.money;
            Integer quantity = productEdit.quantity;

            Intent intent = new Intent(holder.binding.getRoot().getContext(), EditProduct.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("image", image);
            intent.putExtra("introduction", introduction);
            intent.putExtra("genre", genre);
            intent.putExtra("money", money);
            intent.putExtra("quantity", quantity);

            holder.binding.getRoot().getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class Products extends RecyclerView.ViewHolder{
        private final ItemContainerProductShopBinding binding;
        public Products(ItemContainerProductShopBinding itemContainerProductShopBinding)
        {
            super(itemContainerProductShopBinding.getRoot());
            binding = itemContainerProductShopBinding;


        }
        void setData(Product product)
        {
            binding.textView.setText(product.title);

            String formattedMoney = String.format(Locale.getDefault(), "%.2f", product.money);
            binding.money.setText(formattedMoney);

            binding.quantity.setText(String.valueOf(product.quantity));

            binding.imageProduct.setImageBitmap(getConversationImage(product.image));
        }

        private Bitmap getConversationImage(String encodedImage)
        {
            if (encodedImage != null)
            {
                byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            }
            else
            {
                return null;
            }
        }

    }
}
