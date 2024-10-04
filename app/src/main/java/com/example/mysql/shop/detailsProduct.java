package com.example.mysql.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.mysql.OOP.Product;
import com.example.mysql.activitys.Order;
import com.example.mysql.adapters.AdapterComment;
import com.example.mysql.adapters.AdapterProduct;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityDetailsProductBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class detailsProduct extends AppCompatActivity{
    private ActivityDetailsProductBinding binding;
    private Product product;
    private List<Product> productList;
    private FirebaseFirestore database;
    private Preference preference;
    private String idComment = UUID.randomUUID().toString();
    private AdapterComment adapterComment;
    private AdapterProduct adapterProduct;
    private double so_luong = 0.0;
    private double so_sao = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(this);
        database = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();

        event_hading();
        loadingText();
        loadingComment();
        loading_buy();
        loading_information();
    }
    private void event_hading()
    {
        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

    }
    private void loading_information()
    {
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra(Constant.KEY_TABLE_PRODUCT);

        if (product != null)
        {
            binding.introduction.setText(product.introduction);

            binding.imagePrduct.setImageBitmap(getImage(product.image));

            binding.textTitle.setText(product.title);

            DecimalFormat formatter = new DecimalFormat("###,##0.00");

            binding.textMoney.setText(formatter.format(product.money));

            binding.imageShop.setImageBitmap(getImage(product.shopImage));

            binding.NameShop.setText(product.shopName);

            binding.imageShop.setImageBitmap(getImage(product.shopImage));

            binding.NameShop.setText(product.shopName);

            binding.textAddress.setText(product.cityShop);

            binding.sold.setText(String.valueOf(product.soldShop));

        }
    }
    private void loadingText() {

        binding.btnBluan.setOnClickListener(v -> {

            binding.textEvaluation.setVisibility(View.VISIBLE);
            binding.btnBluan.setVisibility(View.GONE);

        });

        binding.textEvaluation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();

                if (text.isEmpty())
                {
                    binding.btnBluan.setVisibility(View.VISIBLE);
                    binding.textEvaluation.setVisibility(View.GONE);
                    binding.btnEvaluation.setVisibility(View.GONE);
                    binding.rating.setVisibility(View.GONE);
                }
                else
                {
                    binding.btnEvaluation.setVisibility(View.VISIBLE);
                    binding.rating.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.btnEvaluation.setVisibility(View.GONE);
        binding.rating.setVisibility(View.GONE);
        
        binding.btnEvaluation.setOnClickListener(v -> sentComment());
    }
    private void loading_buy()
    {

        binding.btnButton.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), Order.class);

            // truyền vào shop

            intent.putExtra("id_shop", product.shopId);
            intent.putExtra("name_shop", product.shopName);
            intent.putExtra("image_shop", product.shopImage);
            intent.putExtra("phone_shop", product.phoneShop);
            intent.putExtra("city_shop", product.cityShop);
            intent.putExtra("sold_shop", product.soldShop);

            // truyền vào sản phẩm

            intent.putExtra("id_product", product.id);
            intent.putExtra("title_product", product.title);
            intent.putExtra("image_product", product.image);
            intent.putExtra("introduction_product", product.introduction);
            intent.putExtra("genre_product", product.genre);
            intent.putExtra("money_product", product.money);
            intent.putExtra("quantity_product", product.quantity);

            startActivity(intent);

        });
    }
    private void loadingComment()
    {
        String userId = preference.getString(Constant.KEY_USER_ID);

        productList.clear();

        so_sao = 0.0;

        database.collection(Constant.KEY_COLLECTION_COMMENT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot documentSnapshot : task.getResult())
                        {
                            Product productcomment = new Product();

                            String productId = product.id;

                            if (productId.equals(documentSnapshot.getString(Constant.KEY_PRODUCT_ID)))
                            {
                                productcomment.commentId = documentSnapshot.getId();
                                productcomment.userId = documentSnapshot.getString(Constant.KEY_USER_ID);
                                productcomment.productId = documentSnapshot.getString(Constant.KEY_PRODUCT_ID);
                                productcomment.nameUser = documentSnapshot.getString(Constant.KEY_FULL_NAME);
                                productcomment.imageUser = documentSnapshot.getString(Constant.KEY_USER_IMAGE);
                                productcomment.comment = documentSnapshot.getString(Constant.KEY_COMMENT);
                                productcomment.commentTime = documentSnapshot.getString(Constant.KEY_COMMENT_TIME);

                                if (documentSnapshot.contains(Constant.KEY_STAR))
                                {
                                    if (documentSnapshot.get(Constant.KEY_STAR) instanceof Long)
                                    {
                                        productcomment.star = String.valueOf(documentSnapshot.getLong(Constant.KEY_STAR));
                                    }
                                    else if (documentSnapshot.get(Constant.KEY_STAR) instanceof Double)
                                    {
                                        productcomment.star = String.valueOf(documentSnapshot.getDouble(Constant.KEY_STAR));
                                    }

                                    so_sao += Integer.parseInt(productcomment.star);
                                }

                                productList.add(productcomment);

                                loadingRepShop(productcomment.commentId);
                            }
                        }
                    }
                    Collections.sort(productList, (o1, o2) -> o2.commentTime.compareTo(o1.commentTime));

                    binding.conversionRecyclerView.setHasFixedSize(true);

                    binding.conversionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                    adapterComment = new AdapterComment(productList, userId);

                    binding.conversionRecyclerView.setAdapter(adapterComment);

                    binding.conversionRecyclerView.setVisibility(View.VISIBLE);

                    so_luong = productList.size();

                    binding.sumQuantity.setText(String.valueOf((int) so_luong));

                    double tong = so_sao/ so_luong;

                    binding.sumBar.setText(String.format("%.1f", tong));

                });
    }

    private void loadingRepShop(String commentId) {
        String product_id = product.id;

        database.collection(Constant.KEY_REP_COMMENT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Product product_rep_comment = new Product();

                            if (product_id.equals(documentSnapshot.getString(Constant.KEY_PRODUCT_ID))) {
                                product_rep_comment.id = documentSnapshot.getString(Constant.KEY_PRODUCT_ID);
                                product_rep_comment.shopId = documentSnapshot.getString(Constant.KEY_USER_SHOP_ID);
                                product_rep_comment.id_rep_comment = documentSnapshot.getString(Constant.KEY_ID_COMMENT_SENT);
                                product_rep_comment.commentId = documentSnapshot.getString(Constant.KEY_COMMENT_ID);
                                product_rep_comment.nameUser = documentSnapshot.getString(Constant.KEY_FULL_NAME);
                                product_rep_comment.imageUser = documentSnapshot.getString(Constant.KEY_USER_IMAGE);
                                product_rep_comment.comment = documentSnapshot.getString(Constant.KEY_COMMENT);
                                product_rep_comment.commentTime = documentSnapshot.getString(Constant.KEY_COMMENT_TIME);

                                productList.add(product_rep_comment);
                            }
                        }
                        adapterComment.notifyDataSetChanged();
                    }
                });
    }
    private void sentComment()
    {
        String commentText = binding.textEvaluation.getText().toString();
        int star = (int) binding.rating.getRating();

        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String TimeString = dateFormat.format(time);

        if (!commentText.isEmpty()) {
            HashMap<String, Object> comment = new HashMap<>();
            comment.put(Constant.KEY_COMMENT_ID, idComment);
            comment.put(Constant.KEY_PRODUCT_ID, product.id);
            comment.put(Constant.KEY_COMMENT, commentText);
            comment.put(Constant.KEY_FULL_NAME, preference.getString(Constant.KEY_FULL_NAME));
            comment.put(Constant.KEY_USER_IMAGE, preference.getString(Constant.KEY_USER_IMAGE));
            comment.put(Constant.KEY_USER_ID, preference.getString(Constant.KEY_USER_ID));
            comment.put(Constant.KEY_STAR, star);
            comment.put(Constant.KEY_PRODUCT_IMAGE, product.image);
            comment.put(Constant.KEY_PRODUCT_TITLE, product.title);
            comment.put(Constant.KEY_USER_SHOP_ID, product.shopId);
            comment.put(Constant.KEY_COMMENT_TIME, TimeString);

            database.collection(Constant.KEY_COLLECTION_COMMENT)
                    .add(comment)
                    .addOnSuccessListener(documentReference -> {
                        message("Thêm comment thành công");

                        loadingComment();
                    })
                    .addOnFailureListener(e -> {
                        message("Thêm thất bại: " + e.getMessage());
                    });
        }

        binding.rating.setRating(1.0f);
        binding.textEvaluation.setText("");
    }
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
    private Bitmap getImage(String image)
    {
        if (image != null && !image.isEmpty())
        {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        else
        {
            return null;
        }
    }
}