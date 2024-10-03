package com.example.mysql.frames;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mysql.OOP.Product;
import com.example.mysql.adapters.AdapterProduct;
import com.example.mysql.adapters.AdapterTitle;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.FragmentHomeBinding;
import com.example.mysql.interfaces.ProductInterface;
import com.example.mysql.shop.detailsProduct;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Home extends Fragment implements ProductInterface {
    private FragmentHomeBinding binding;
    private List<Product> productList;
    private AdapterProduct adapterProduct;
    private FirebaseFirestore database;
    private AdapterTitle adapterTitle;
    private Preference preference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();
        preference = new Preference(requireContext());

        productList = new ArrayList<>();

        loadinggenre();
        loading_product();

        return binding.getRoot();
    }

    private void loadinggenre()
    {

        String[] genres = new String[]{"Sofas", "Chairs", "Tables", "Kitchen", "Doors"};

        List<String> strings = Arrays.asList(genres);

        adapterTitle = new AdapterTitle(strings);

        binding.conversionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        binding.conversionRecyclerView.setAdapter(adapterTitle);

        binding.conversionRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loading_product()
    {
        database.collection(Constant.KEY_TABLE_PRODUCT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Product product = new Product();

                            product.id = documentSnapshot.getId();
                            product.title = documentSnapshot.getString(Constant.KEY_PRODUCT_TITLE);
                            product.image = documentSnapshot.getString(Constant.KEY_PRODUCT_IMAGE);
                            product.quantity = documentSnapshot.getLong(Constant.KEY_PRODUCT_QUANTITY).intValue();
                            product.money = documentSnapshot.getDouble(Constant.KEY_PRODUCT_MONEY);
                            product.introduction = documentSnapshot.getString(Constant.KEY_PRODUCT_INTRODUCTION);
                            product.genre = documentSnapshot.getString(Constant.KEY_PRODUCT_GENRE);
                            product.dateTime = documentSnapshot.getString(Constant.KEY_TIME_PRODUCT);
                            product.shopId = documentSnapshot.getString(Constant.KEY_USER_SHOP_ID);
                            product.shopName = documentSnapshot.getString(Constant.KEY_FULL_NAME);
                            product.shopImage = documentSnapshot.getString(Constant.KEY_USER_IMAGE);
                            product.cityShop = documentSnapshot.getString(Constant.KEY_CITY);
                            product.phoneShop = documentSnapshot.getString(Constant.KEY_PHONE);
                            product.soldShop = String.valueOf(documentSnapshot.getLong(Constant.KEY_SOLD));

                            productList.add(product);
                        }
                    }


                    Collections.sort(productList, (o1, o2) -> o2.dateTime.compareTo(o1.dateTime));

                    binding.conversionRecyclerViewShop.setHasFixedSize(true);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
                    binding.conversionRecyclerViewShop.setLayoutManager(gridLayoutManager);

                    adapterProduct = new AdapterProduct(productList,this);
                    binding.conversionRecyclerViewShop.setAdapter(adapterProduct);

                    binding.conversionRecyclerViewShop.setVisibility(View.VISIBLE);
                    binding.progesbar.setVisibility(View.INVISIBLE);
                });
    }



    private void message(String mess)
    {
        Toast.makeText(requireContext() ,mess, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClickProduct(Product product)
    {
        Intent intent = new Intent(requireContext(), detailsProduct.class);
        intent.putExtra(Constant.KEY_TABLE_PRODUCT, product);
        startActivity(intent);
    }
}