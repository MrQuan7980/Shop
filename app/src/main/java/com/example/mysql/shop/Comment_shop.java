package com.example.mysql.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mysql.OOP.Product;
import com.example.mysql.R;
import com.example.mysql.adapters.AdapterCommentShop;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityCommentShopBinding;
import com.example.mysql.interfaces.ClickComment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Comment_shop extends AppCompatActivity implements ClickComment {
    private ActivityCommentShopBinding binding;
    private FirebaseFirestore database;
    private Preference preference;
    private AdapterCommentShop adapterCommentShop;
    private List<Product> productList;
    private int quantity = 0;
    private String ShopId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        preference = new Preference(this);

        productList = new ArrayList<>();

        loaidngCommentShop();

        event_hading();

    }
    private void event_hading()
    {
        binding.back.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Shop.class)));
    }
    private void loaidngCommentShop()
    {
        ShopId = preference.getString(Constant.KEY_USER_SHOP_ID);

        database.collection(Constant.KEY_COLLECTION_COMMENT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot documentSnapshot : task.getResult())
                        {

                            Product product = new Product();

                            if (ShopId.equals(documentSnapshot.getString(Constant.KEY_USER_SHOP_ID)))
                            {

                                product.commentId = documentSnapshot.getString(Constant.KEY_COMMENT_ID);
                                product.id = documentSnapshot.getString(Constant.KEY_PRODUCT_ID);
                                product.shopId = documentSnapshot.getString(Constant.KEY_USER_SHOP_ID);
                                product.nameUser = documentSnapshot.getString(Constant.KEY_FULL_NAME);
                                product.imageUser = documentSnapshot.getString(Constant.KEY_USER_IMAGE);
                                product.titleComment = documentSnapshot.getString(Constant.KEY_PRODUCT_TITLE);
                                product.image = documentSnapshot.getString(Constant.KEY_PRODUCT_IMAGE);
                                product.title = documentSnapshot.getString(Constant.KEY_PRODUCT_TITLE);
                                product.comment = documentSnapshot.getString(Constant.KEY_COMMENT);
                                product.commentTime = documentSnapshot.getString(Constant.KEY_COMMENT_TIME);

                                if (documentSnapshot.contains(Constant.KEY_STAR))
                                {
                                    if (documentSnapshot.get(Constant.KEY_STAR) instanceof Long)
                                    {
                                        product.star = String.valueOf(documentSnapshot.getLong(Constant.KEY_STAR));
                                    }
                                    else if (documentSnapshot.get(Constant.KEY_STAR) instanceof Double)
                                    {
                                        product.star = String.valueOf(documentSnapshot.getDouble(Constant.KEY_STAR));
                                    }
                                }

                                productList.add(product);

                            }

                        }
                    }

                    Collections.sort(productList, (o1, o2) -> o2.commentTime.compareTo(o1.commentTime));

                    binding.conversionRecyclerView.setHasFixedSize(true);

                    binding.conversionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                    adapterCommentShop = new AdapterCommentShop(productList, this);

                    binding.conversionRecyclerView.setAdapter(adapterCommentShop);

                    binding.conversionRecyclerView.setVisibility(View.VISIBLE);

                    binding.progressbar.setVisibility(View.INVISIBLE);

                    quantity = productList.size();

                });
    }


    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(@NonNull Product product)
    {
        Intent intent = new Intent(this, SentComment.class);
        intent.putExtra("id_product", product.id);
        intent.putExtra("id_comment", product.commentId);
        intent.putExtra("name", product.nameUser);
        intent.putExtra("image", product.imageUser);
        intent.putExtra("star", Double.parseDouble(product.star));
        intent.putExtra("comment", product.comment);
        intent.putExtra("image_product", product.image);
        intent.putExtra("title", product.title);

        startActivity(intent);
    }
}