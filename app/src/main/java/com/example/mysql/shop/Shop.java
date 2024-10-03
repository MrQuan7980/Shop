package com.example.mysql.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mysql.OOP.Product;
import com.example.mysql.activitys.SignIn;
import com.example.mysql.adapters.AdapterShop;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityShopBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shop extends AppCompatActivity {
    private ActivityShopBinding binding;
    private Preference preference;
    private List<Product> products;
    private AdapterShop adapterShop;
    private FirebaseFirestore database;
    private int unreadCommentsCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(this);
        products = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        event_hading();
        loadingShop();
        product();

    }
    private void event_hading()
    {
        binding.addProjeck.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Product_Shop.class));
        });

        binding.imageProifile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ProfileShop.class));
            finish();
        });

        binding.comment.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Comment_shop.class);
            startActivity(intent);
        });
    }
    private void loadingShop()
    {
        binding.textName.setText(preference.getString(Constant.KEY_FULL_NAME));

        String baseImage = preference.getString(Constant.KEY_USER_IMAGE);

        if (baseImage != null)
        {
            byte[] bytes = Base64.decode(baseImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            binding.imageProifile.setImageBitmap(bitmap);
        }
        else
        {
            message("Vui lòng có ảnh");
        }
    }
    private void product()
    {
        database.collection(Constant.KEY_TABLE_PRODUCT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        String shopId = preference.getString(Constant.KEY_USER_SHOP_ID);

                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Product product = new Product();

                            if (shopId.equals(documentSnapshot.getString(Constant.KEY_USER_SHOP_ID)))
                            {
                                product.id = documentSnapshot.getId();
                                product.title = documentSnapshot.getString(Constant.KEY_PRODUCT_TITLE);
                                product.image = documentSnapshot.getString(Constant.KEY_PRODUCT_IMAGE);
                                product.quantity = documentSnapshot.getLong(Constant.KEY_PRODUCT_QUANTITY).intValue();
                                product.money = documentSnapshot.getDouble(Constant.KEY_PRODUCT_MONEY);
                                product.introduction = documentSnapshot.getString(Constant.KEY_PRODUCT_INTRODUCTION);
                                product.genre = documentSnapshot.getString(Constant.KEY_PRODUCT_GENRE);
                                product.dateTime = documentSnapshot.getString(Constant.KEY_TIME_PRODUCT);


                                products.add(product);
                            }
                        }

                        Collections.sort(products, (o1, o2) -> o2.dateTime.compareTo(o1.dateTime));

                        binding.conversionRecyclerView.setHasFixedSize(true);
                        binding.conversionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        adapterShop = new AdapterShop(products);
                        binding.conversionRecyclerView.setAdapter(adapterShop);
                        binding.conversionRecyclerView.setVisibility(View.VISIBLE);
                        binding.progressbar.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        message("Không tìm thấy sản phẩm nào");
                    }
                });

    }


    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
}