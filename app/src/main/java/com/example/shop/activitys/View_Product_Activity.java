package com.example.shop.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shop.Adapter.AdapterComment;
import com.example.shop.Object.Comment;
import com.example.shop.Object.Product;
import com.example.shop.R;
import com.example.shop.database.Constant;
import com.example.shop.database.Preference;
import com.example.shop.databinding.ActivityViewProductBinding;
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

public class View_Product_Activity extends AppCompatActivity {
    private ActivityViewProductBinding binding;
    private Product product;
    private String comment;
    private Preference preference;
    private FirebaseFirestore database;
    private String id = UUID.randomUUID().toString();
    private List<Comment> commentList;
    private AdapterComment adapterComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        commentList = new ArrayList<>();
        handle();
        loaidng_product();
        textComment();
    }
    private void handle()
    {
        binding.imageBack.setOnClickListener(v -> onBackPressed());

        binding.imageFovarite.setOnClickListener(v -> {
            favorite();
        });
    }
    private void loaidng_product()
    {
        Intent intent = getIntent();

        product = (Product) intent.getSerializableExtra(Constant.key_collection_product);

        binding.textTitle.setText(product.title);
        binding.imageOne.setImageBitmap(getImage(product.image_one));
        binding.imageTwo.setImageBitmap(getImage(product.image_two));
        binding.imageThree.setImageBitmap(getImage(product.image_three));
        binding.imageFour.setImageBitmap(getImage(product.image_four));

        binding.imageProduct.setImageBitmap(getImage(product.image_two));

        binding.imageOne.setOnClickListener(v -> {
            binding.imageProduct.setImageBitmap(getImage(product.image_one));
        });

        binding.imageTwo.setOnClickListener(v -> {
            binding.imageProduct.setImageBitmap(getImage(product.image_two));
        });

        binding.imageThree.setOnClickListener(v -> {
            binding.imageProduct.setImageBitmap(getImage(product.image_three));
        });

        binding.imageFour.setOnClickListener(v -> {
            binding.imageProduct.setImageBitmap(getImage(product.image_four));
        });

        binding.textIntroduct.setText(product.introduce);

        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");

        binding.money.setText(decimalFormat.format(product.money));

        binding.imageShop.setImageBitmap(getImage(product.shop.image));

        binding.city.setText(product.shop.city);

        binding.nameShop.setText(product.shop.name);

    }

    private void favorite()
    {
        HashMap<String, Object> favorite = new HashMap<>();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String date_time = dateFormat.format(date);

        favorite.put(Constant.key_user_id, preference.getString(Constant.key_user_id));
        favorite.put(Constant.key_product_id, product.id);
        favorite.put(Constant.key_user_id_shop, product.shop.id);
        favorite.put(Constant.key_product_image_one, product.image_one);
        favorite.put(Constant.key_product_image_two, product.image_two);
        favorite.put(Constant.key_product_image_three, product.image_three);
        favorite.put(Constant.key_product_image_four, product.image_four);
        favorite.put(Constant.key_product_title, product.title);
        favorite.put(Constant.key_product_money, product.money);
        favorite.put(Constant.key_shop_image, product.shop.image);
        favorite.put(Constant.key_shop_name, product.shop.name);
        favorite.put(Constant.key_shop_city, product.shop.city);
        favorite.put(Constant.key_date_favorite, date_time);

        database.collection(Constant.key_collection_favorite)
                .add(favorite)
                .addOnSuccessListener(command -> {
                    message("add success");

                })
                .addOnFailureListener(e -> {
                    message("add failure");
                });
    }
    private void loading_comment()
    {

        commentList.clear();

        String id = product.id;
        database.collection(Constant.key_collection_comment)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        for (DocumentSnapshot documentSnapshot : task.getResult())
                        {
                            if (id.equals(documentSnapshot.getString(Constant.key_product_id)))
                            {
                                Comment list_comment = new Comment();

                                list_comment.id = documentSnapshot.getString(Constant.key_comment_id);
                                list_comment.product.id = documentSnapshot.getString(Constant.key_product_id);
                                list_comment.product.title = documentSnapshot.getString(Constant.key_product_title);
                                list_comment.product.image_one = documentSnapshot.getString(Constant.key_product_image_one);
                                list_comment.product.image_two = documentSnapshot.getString(Constant.key_product_image_two);
                                list_comment.product.image_three = documentSnapshot.getString(Constant.key_product_image_three);
                                list_comment.product.image_four = documentSnapshot.getString(Constant.key_product_image_four);
                                list_comment.product.shop.id = documentSnapshot.getString(Constant.key_user_id_shop);
                                list_comment.product.shop.name = documentSnapshot.getString(Constant.key_shop_name);
                                list_comment.comment = documentSnapshot.getString(Constant.key_comment_message);
                                list_comment.user.id = documentSnapshot.getString(Constant.key_user_id);
                                list_comment.user.name = documentSnapshot.getString(Constant.key_name);
                                list_comment.user.image = documentSnapshot.getString(Constant.key_image);
                                list_comment.date = documentSnapshot.getString(Constant.key_comment_date);
                                list_comment.rating = documentSnapshot.getDouble(Constant.key_comment_rating);

                                commentList.add(list_comment);

                            }
                        }
                    }

                    Collections.sort(commentList, (o1, o2) -> o2.date.compareTo(o1.date));

                    binding.conversionRecycler.setHasFixedSize(true);

                    binding.conversionRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    adapterComment = new AdapterComment(commentList);

                    binding.conversionRecycler.setAdapter(adapterComment);

                    if (commentList.size() > 0)
                    {
                        binding.conversionRecycler.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        binding.conversionRecycler.setVisibility(View.GONE);
                    }

                });
    }
    private void add_comment()
    {
        progressbar(true);
        HashMap<String, Object> comment = new HashMap<>();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM", Locale.getDefault()); // dd-mm-yyyy
        String date_time = dateFormat.format(date);

        comment.put(Constant.key_comment_id, id);
        comment.put(Constant.key_product_id, product.id);
        comment.put(Constant.key_product_title, product.title);
        comment.put(Constant.key_product_image_one, product.image_one);
        comment.put(Constant.key_product_image_two, product.image_two);
        comment.put(Constant.key_product_image_three, product.image_three);
        comment.put(Constant.key_product_image_four, product.image_four);
        comment.put(Constant.key_user_id_shop, product.shop.id);
        comment.put(Constant.key_shop_name, product.shop.name);
        comment.put(Constant.key_user_id, preference.getString(Constant.key_user_id));
        comment.put(Constant.key_name, preference.getString(Constant.key_name));
        comment.put(Constant.key_image, preference.getString(Constant.key_image));
        comment.put(Constant.key_comment_message, binding.inputComment.getText().toString());
        comment.put(Constant.key_comment_date, date_time);
        comment.put(Constant.key_comment_rating, binding.ratingComment.getRating());

        database.collection(Constant.key_collection_comment)
                .add(comment)
                .addOnSuccessListener(command -> {
                    progressbar(false);

                    message("add success");

                    loading_comment();

                    binding.ratingComment.setRating(0);
                    binding.inputComment.setText("");

                }).addOnFailureListener(command -> {
                    progressbar(false);

                    message("add failure");
                });

    }
    private void textComment()
    {
        binding.inputComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                comment = s.toString().trim();

                if (!comment.isEmpty())
                {
                    binding.btnComment.setVisibility(View.VISIBLE);
                    binding.ratingComment.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.btnComment.setVisibility(View.GONE);
                    binding.ratingComment.setVisibility(View.GONE);
                }
            }

        });

        binding.btnComment.setOnClickListener(v -> add_comment());

        loading_comment();

    }
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
    private Bitmap getImage(String image)
    {
        if (image != null && !image.isEmpty())
        {
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else
        {
            return null;
        }
    }
    private void progressbar(boolean loading)
    {
        if (loading)
        {
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.btnComment.setVisibility(View.GONE);
        }
        else
        {
            binding.btnComment.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.GONE);
        }
    }
}