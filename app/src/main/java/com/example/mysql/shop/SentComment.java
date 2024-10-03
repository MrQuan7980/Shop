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
import com.example.mysql.R;
import com.example.mysql.adapters.AdapterCommentShop;
import com.example.mysql.adapters.AdapterRepComment;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivitySentCommentBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SentComment extends AppCompatActivity {
    private ActivitySentCommentBinding binding;
    private String id_product, id_comment, name, image, comment, title, image_product;
    private double star = 0.0;
    private FirebaseFirestore database;
    private Preference preference;
    private String id_comment_sent = UUID.randomUUID().toString();
    private String id_Shop = "";
    private AdapterRepComment adapterCommentShop;
    private List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySentCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        preference = new Preference(this);
        productList = new ArrayList<>();

        event_hading();
        loadingSent();
        loaidngCommentShop();
    }
    private void event_hading()
    {
        Intent intent = getIntent();

        id_product = intent.getStringExtra("id_product");
        id_comment = intent.getStringExtra("id_comment");
        name = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        star = intent.getDoubleExtra("star", 0.0);
        comment = intent.getStringExtra("comment");
        title = intent.getStringExtra("title");
        image_product = intent.getStringExtra("image_product");

        binding.textName.setText(name);
        binding.imageProfile.setImageBitmap(getImage(image));
        binding.ratingSao.setRating((float) star);
        binding.textComment.setText(comment);
        binding.imagePrduct.setImageBitmap(getImage(image_product));
        binding.textTitle.setText(title);

        binding.btnEvaluation.setOnClickListener(v -> repComment());
    }
    private void loaidngCommentShop()
    {
        id_Shop = preference.getString(Constant.KEY_USER_SHOP_ID);
        String id = id_comment;

        productList.clear();
        database.collection(Constant.KEY_REP_COMMENT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot documentSnapshot : task.getResult())
                        {
                            Product product = new Product();

                            if (id.equals(documentSnapshot.getString(Constant.KEY_COMMENT_ID)))
                            {

                                product.nameUser = documentSnapshot.getString(Constant.KEY_FULL_NAME);
                                product.imageUser = documentSnapshot.getString(Constant.KEY_USER_IMAGE);
                                product.comment = documentSnapshot.getString(Constant.KEY_COMMENT);
                                product.commentTime = documentSnapshot.getString(Constant.KEY_COMMENT_TIME);

                                productList.add(product);
                            }
                        }
                    }

                    Collections.sort(productList, (o1, o2) -> o2.commentTime.compareTo(o1.commentTime));

                    binding.conversionRecyclerView.setHasFixedSize(true);

                    binding.conversionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                    adapterCommentShop = new AdapterRepComment(productList);

                    binding.conversionRecyclerView.setAdapter(adapterCommentShop);

                    binding.conversionRecyclerView.setVisibility(View.VISIBLE);

                    binding.progressbar.setVisibility(View.INVISIBLE);

                });
    }
    private void repComment()
    {
        String comment = binding.textEvaluation.getText().toString().trim();

        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String TimeString = dateFormat.format(time);

        HashMap<String, Object> sentcomment = new HashMap<>();

        sentcomment.put(Constant.KEY_ID_COMMENT_SENT, id_comment_sent);
        sentcomment.put(Constant.KEY_COMMENT_ID, id_comment);
        sentcomment.put(Constant.KEY_USER_SHOP_ID, preference.getString(Constant.KEY_USER_SHOP_ID));
        sentcomment.put(Constant.KEY_FULL_NAME, preference.getString(Constant.KEY_FULL_NAME));
        sentcomment.put(Constant.KEY_USER_IMAGE, preference.getString(Constant.KEY_USER_IMAGE));
        sentcomment.put(Constant.KEY_COMMENT, comment);
        sentcomment.put(Constant.KEY_COMMENT_TIME, TimeString);

        database.collection(Constant.KEY_REP_COMMENT)
                .add(sentcomment)
                .addOnSuccessListener(documentReference -> {
                    message("Trả lời thành công");

                    loaidngCommentShop();

                })
                .addOnFailureListener(e -> {
                    message("Thêm thất bại: " + e.getMessage());
                });

        binding.textEvaluation.setText("");
    }
    private void loadingSent()
    {
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

                if (!text.isEmpty())
                {
                    binding.btnEvaluation.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.btnEvaluation.setVisibility(View.GONE);
                }
            }
        });
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
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
}