package com.example.shop.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.Object.Product;
import com.example.shop.databinding.ItemContainerProductShopBinding;

import java.util.List;
import java.util.Locale;

public class AdapterProductShop extends RecyclerView.Adapter<AdapterProductShop.Shop>{

    private final List<Product> productList;

    public AdapterProductShop(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public Shop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerProductShopBinding binding = ItemContainerProductShopBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Shop(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Shop holder, int position) {
        holder.getData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class Shop extends RecyclerView.ViewHolder {

        private final ItemContainerProductShopBinding binding;

        public Shop(ItemContainerProductShopBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void getData(Product product)
        {
            binding.imageProduct.setImageBitmap(getBitMap(product.image_two));
            binding.textTitle.setText(product.getTitle());
            binding.postingDate.setText(product.getPosting_date());
            binding.quantity.setText(String.valueOf(product.getQuantity()));
            String formattedMoney = String.format(Locale.getDefault(), "%.2f", product.getMoney());
            binding.money.setText(formattedMoney);
            binding.sold.setText(String.valueOf(product.getSold()));
        }
        private Bitmap getBitMap(String image)
        {
            if (!image.isEmpty() && image != null)
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
