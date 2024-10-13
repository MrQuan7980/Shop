package com.example.shop.frames_shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shop.Adapter.AdapterProductShop;
import com.example.shop.Object.Product;
import com.example.shop.Object.Shop;
import com.example.shop.database.Constant;
import com.example.shop.database.Preference;
import com.example.shop.databinding.FragmentHomeShopBinding;
import com.example.shop.shop.Add_product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class home_shop extends Fragment {
    private FragmentHomeShopBinding binding;
    private Preference preference;
    private FirebaseFirestore database;
    private List<Product> productList;
    private AdapterProductShop adapterProductShop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeShopBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();
        preference = new Preference(getContext());

        productList = new ArrayList<>();

        loading_shop();

        handle();

        loading_product_shop();
        return binding.getRoot();
    }
    private void handle()
    {
        binding.imageAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Add_product.class);
            startActivity(intent);
        });


    }
    private void loading_shop()
    {
        binding.imageShop.setImageBitmap(getBitMap(preference.getString(Constant.key_shop_image)));
    }
    private void loading_product_shop()
    {
        String id = preference.getString(Constant.key_user_id_shop);

        database.collection(Constant.key_collection_product)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot document : task.getResult())
                        {
                            if (id.equals(document.getString(Constant.key_user_id_shop)))
                            {
                                Product product = new Product();

                                product.shop = new Shop();
                                product.id = document.getString(Constant.key_product_id);
                                product.title = document.getString(Constant.key_product_title);
                                product.category = document.getString(Constant.key_product_category);
                                product.image_one = document.getString(Constant.key_product_image_one);
                                product.image_two = document.getString(Constant.key_product_image_two);
                                product.image_three = document.getString(Constant.key_product_image_three);
                                product.image_four = document.getString(Constant.key_product_image_four);
                                product.introduce = document.getString(Constant.key_product_introduce);
                                product.posting_date = document.getString(Constant.key_shop_date);
                                product.sold = document.getLong(Constant.key_product_sold).intValue();
                                product.quantity = document.getLong(Constant.key_product_quantity).intValue();

                                Object moneyObj = document.get(Constant.key_product_money);
                                if (moneyObj instanceof Number)
                                {
                                    product.money = ((Number) moneyObj).doubleValue();
                                }
                                else
                                {
                                    product.money = 0.0;
                                }
                                product.shop.id = document.getString(Constant.key_user_id_shop);
                                product.shop.name = document.getString(Constant.key_shop_name);
                                product.shop.phone = document.getString(Constant.key_shop_phone);
                                product.shop.email = document.getString(Constant.key_shop_email);
                                product.shop.image = document.getString(Constant.key_shop_image);
                                product.shop.house = document.getString(Constant.key_shop_house);
                                product.shop.district = document.getString(Constant.key_shop_district);
                                product.shop.city = document.getString(Constant.key_shop_city);

                                productList.add(product);
                            }
                        }
                    }

                    Collections.sort(productList, (o1, o2) -> o1.posting_date.compareTo(o2.posting_date));

                    binding.recyclerviewShop.setHasFixedSize(true);

                    binding.recyclerviewShop.setLayoutManager(new LinearLayoutManager(isAdded() ? requireContext() : getActivity()));

                    adapterProductShop = new AdapterProductShop(productList);

                    binding.recyclerviewShop.setAdapter(adapterProductShop);

                    if (productList.size() > 0)
                    {
                        binding.recyclerviewShop.setVisibility(View.VISIBLE);
                        binding.progressbar.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        binding.recyclerviewShop.setVisibility(View.INVISIBLE);
                        binding.progressbar.setVisibility(View.VISIBLE);
                    }
                });
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