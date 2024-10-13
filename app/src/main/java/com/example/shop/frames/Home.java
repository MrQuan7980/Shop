package com.example.shop.frames;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shop.Adapter.AdapterProductUser;
import com.example.shop.Adapter.AdapterTypeProduct;
import com.example.shop.Interface.OnClickProduct;
import com.example.shop.Object.Product;
import com.example.shop.activitys.View_Product_Activity;
import com.example.shop.database.Constant;
import com.example.shop.database.Preference;
import com.example.shop.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Home extends Fragment implements OnClickProduct {
    private FragmentHomeBinding binding;
    private FirebaseFirestore database;
    private Preference preference;
    private List<Product> productList;
    private List<Product> productType;
    private AdapterProductUser adapterProductUser;
    private AdapterTypeProduct adapterTypeProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();
        preference = new Preference(getContext());
        productList = new ArrayList<>();
        productType = new ArrayList<>();

        handle();

        loading_product();

        loading_type_product();

        return binding.getRoot();
    }
    private void handle()
    {

    }
    private void loading_product()
    {
        database.collection(Constant.key_collection_product)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot document : task.getResult())
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

                    Collections.sort(productList, (o1, o2) -> o2.sold);

                    binding.conversionRecyclerViewShop.setHasFixedSize(true);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);

                    binding.conversionRecyclerViewShop.setLayoutManager(gridLayoutManager);

                    adapterProductUser = new AdapterProductUser(productList, this::onClick);

                    binding.conversionRecyclerViewShop.setAdapter(adapterProductUser);

                    if (productList.size() > 0)
                    {
                        binding.conversionRecyclerViewShop.setVisibility(View.VISIBLE);
                        binding.progressbarProduct.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        binding.conversionRecyclerViewShop.setVisibility(View.INVISIBLE);
                        binding.progressbarProduct.setVisibility(View.VISIBLE);
                    }
                });


    }
    private void loading_type_product() {
        Set<String> uniqueCategories = new HashSet<>();

        database.collection(Constant.key_collection_product)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot document : task.getResult())
                        {
                            String category = document.getString(Constant.key_product_category);

                            if (category != null && !category.isEmpty()) {
                                uniqueCategories.add(category);
                            }
                        }

                        List<Product> productType = new ArrayList<>();
                        for (String category : uniqueCategories) {
                            Product product = new Product();
                            product.category = category;
                            productType.add(product);
                        }

                        Collections.sort(productType, (o1, o2) -> o2.category.compareTo(o1.category));

                        binding.conversionRecyclerView.setHasFixedSize(true);

                        binding.conversionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

                        adapterTypeProduct = new AdapterTypeProduct(productType);
                        binding.conversionRecyclerView.setAdapter(adapterTypeProduct);

                        binding.conversionRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void message(String mes)
    {
        Toast.makeText(getContext(), mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(getContext(), View_Product_Activity.class);
        intent.putExtra(Constant.key_collection_product, product);
        startActivity(intent);
    }
}