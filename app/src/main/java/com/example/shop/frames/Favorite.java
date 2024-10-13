package com.example.shop.frames;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shop.Adapter.AdapterFavorite;
import com.example.shop.Object.Product;
import com.example.shop.R;
import com.example.shop.database.Constant;
import com.example.shop.database.Preference;
import com.example.shop.databinding.FragmentFavoriteBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Favorite extends Fragment {
    private FragmentFavoriteBinding binding;
    private Preference preference;
    private FirebaseFirestore database;
    private AdapterFavorite adapterFavorite;
    private List<Product> productList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        preference = new Preference(requireContext());
        database = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();

        handle();
        return binding.getRoot();
    }

    private void handle()
    {
        String id = preference.getString(Constant.key_user_id);
        database.collection(Constant.key_collection_favorite)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot documentSnapshot : task.getResult())
                        {
                            if (id.equals(documentSnapshot.getString(Constant.key_user_id)))
                            {
                                Product product = new Product();

                                product.id = documentSnapshot.getString(Constant.key_product_id);
                                product.title = documentSnapshot.getString(Constant.key_product_title);
                                product.image_one = documentSnapshot.getString(Constant.key_product_image_one);
                                product.image_two = documentSnapshot.getString(Constant.key_product_image_two);
                                product.image_three = documentSnapshot.getString(Constant.key_product_image_three);
                                product.image_four = documentSnapshot.getString(Constant.key_product_image_four);
                                product.shop.id = documentSnapshot.getString(Constant.key_user_id_shop);
                                product.shop.name = documentSnapshot.getString(Constant.key_shop_name);
                                product.shop.image = documentSnapshot.getString(Constant.key_shop_image);
                                product.shop.city = documentSnapshot.getString(Constant.key_shop_city);
                                product.money = documentSnapshot.getDouble(Constant.key_product_money);
                                product.favorite_love_date = documentSnapshot.getString(Constant.key_date_favorite);

                                productList.add(product);

                            }
                        }
                    }

                    Collections.sort(productList, (o1, o2) -> o2.favorite_love_date.compareTo(o1.favorite_love_date));

                    binding.conversionRecyclerViewShop.setHasFixedSize(false);

                    binding.conversionRecyclerViewShop.setLayoutManager(new LinearLayoutManager(isAdded() ? requireContext() : getActivity()));

                    adapterFavorite = new AdapterFavorite(productList);

                    binding.conversionRecyclerViewShop.setAdapter(adapterFavorite);

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
}