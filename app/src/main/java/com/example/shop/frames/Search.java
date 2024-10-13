package com.example.shop.frames;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shop.Adapter.AdapterProductUser;
import com.example.shop.Interface.OnClickProduct;
import com.example.shop.Object.Product;
import com.example.shop.R;
import com.example.shop.activitys.View_Product_Activity;
import com.example.shop.database.Constant;
import com.example.shop.databinding.FragmentSearchBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment implements OnClickProduct {
    private FragmentSearchBinding binding;
    private List<Product> productList;
    private AdapterProductUser adapterProductUser;
    private FirebaseFirestore database;
    private String text_search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        loading_edittext_search();

        return binding.getRoot();
    }
    private void handle()
    {
        productList.clear();

        database.collection(Constant.key_collection_product)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot document : task.getResult())
                        {
                            if (document.getString(Constant.key_product_title).toLowerCase().contains(text_search.toLowerCase()))
                            {
                                Product product = new Product();

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
                                product.money = document.getDouble(Constant.key_product_money);
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


                    binding.recyclerviewShop.setHasFixedSize(true);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);

                    binding.recyclerviewShop.setLayoutManager(gridLayoutManager);

                    adapterProductUser = new AdapterProductUser(productList, this::onClick);

                    binding.recyclerviewShop.setAdapter(adapterProductUser);

                    if (productList.size() > 0)
                    {
                        binding.recyclerviewShop.setVisibility(View.VISIBLE);
                        binding.progressbar.setVisibility(View.GONE);
                    }
                    else
                    {
                        binding.recyclerviewShop.setVisibility(View.INVISIBLE);
                        binding.progressbar.setVisibility(View.VISIBLE);
                    }

                });


    }
    private void loading_edittext_search()
    {
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                text_search = s.toString().trim();

                if (text_search.isEmpty())
                {
                    binding.btnSearch.setVisibility(View.GONE);
                    productList.clear();

                    adapterProductUser.notifyDataSetChanged();
                }
                else
                {
                    binding.btnSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.btnSearch.setOnClickListener(v -> {
            handle();
        });
    }

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(getContext(), View_Product_Activity.class);
        intent.putExtra(Constant.key_collection_product, product);
        startActivity(intent);
    }
    private void message(String mes)
    {
        Toast.makeText(requireContext(), mes, Toast.LENGTH_SHORT).show();
    }
}