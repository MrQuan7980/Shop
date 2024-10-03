package com.example.mysql.shop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityProductShopBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class Product_Shop extends AppCompatActivity {
    private ActivityProductShopBinding binding;
    private FirebaseFirestore database;
    private String Genre = "";
    private String image_product = "";
    private Preference preference;
    private String id = UUID.randomUUID().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        preference = new Preference(this);

        event_hading();
        getLoading_item();
    }

    private void event_hading()
    {
        binding.imageBack.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Shop.class));
        });

        binding.imageShop.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.btnAdd.setOnClickListener(v -> {
            if (test())
            {
                add_product();
            }
        });
    }
    private void getLoading_item()
    {
        String[] genres = new String[]{"Sofas", "Chairs", "Tables", "Kitchen", "Doors"};

        binding.textGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Product_Shop.this);
                builder.setTitle("Thể loại");

                builder.setItems(genres, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Genre = genres[which];
                        binding.textGenre.setText(Genre);

                    }
                });

                builder.show();
            }
        });
    }

    private void add_product()
    {
        loading(true);

        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String TimeString = dateFormat.format(time);

        String quantityString = binding.textQuantity.getText().toString();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String moneyString = binding.textMoney.getText().toString();
        double money = 0.0;
        try {
            money = Double.parseDouble(moneyString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> product = new HashMap<>();
        product.put(Constant.KEY_PRODUCT_ID, id);
        product.put(Constant.KEY_PRODUCT_TITLE, binding.textTitle.getText().toString());
        product.put(Constant.KEY_PRODUCT_GENRE, Genre);
        product.put(Constant.KEY_PRODUCT_QUANTITY, quantity);
        product.put(Constant.KEY_PRODUCT_IMAGE, image_product);
        product.put(Constant.KEY_PRODUCT_INTRODUCTION, binding.textIntroduction.getText().toString());
        product.put(Constant.KEY_PRODUCT_MONEY, money);
        product.put(Constant.KEY_TIME_PRODUCT, TimeString);
        product.put(Constant.KEY_USER_SHOP_ID, preference.getString(Constant.KEY_USER_SHOP_ID));
        product.put(Constant.KEY_USER_IMAGE, preference.getString(Constant.KEY_USER_IMAGE));
        product.put(Constant.KEY_FULL_NAME, preference.getString(Constant.KEY_FULL_NAME));
        product.put(Constant.KEY_CITY, preference.getString(Constant.KEY_CITY));
        product.put(Constant.KEY_PHONE, preference.getString(Constant.KEY_PHONE));
        product.put(Constant.KEY_SOLD, 0);


        database.collection(Constant.KEY_TABLE_PRODUCT)
                .add(product)
                .addOnSuccessListener(command  -> {

                    loading(false);
                    preference.putString(Constant.KEY_PRODUCT_ID, id);
                    preference.putString(Constant.KEY_PRODUCT_TITLE, binding.textTitle.getText().toString());
                    preference.putString(Constant.KEY_PRODUCT_GENRE, Genre);
                    preference.putString(Constant.KEY_PRODUCT_QUANTITY, binding.textQuantity.getText().toString());
                    preference.putString(Constant.KEY_PRODUCT_IMAGE, image_product);
                    preference.putString(Constant.KEY_PRODUCT_INTRODUCTION, binding.textIntroduction.getText().toString());
                    preference.putString(Constant.KEY_PRODUCT_MONEY, binding.textMoney.getText().toString());
                    preference.putString(Constant.KEY_TIME_PRODUCT, TimeString);
                    preference.putString(Constant.KEY_USER_SHOP_ID, preference.getString(Constant.KEY_USER_SHOP_ID));
                    preference.putString(Constant.KEY_USER_IMAGE, preference.getString(Constant.KEY_USER_IMAGE));
                    preference.putString(Constant.KEY_FULL_NAME, preference.getString(Constant.KEY_FULL_NAME));
                    preference.putString(Constant.KEY_CITY, preference.getString(Constant.KEY_CITY));
                    preference.putString(Constant.KEY_PHONE, preference.getString(Constant.KEY_PHONE));
                    preference.putString(Constant.KEY_SOLD, String.valueOf(0));


                    Intent intent = new Intent(getApplicationContext(), Shop.class);
                    startActivity(intent);
                    finish();

                }).addOnFailureListener(command -> {
                    loading(false);
                    message("Thêm sản phẩm thất bại");
                });
    }
    public String codeImage(Bitmap bitmap)
    {
        int rong = 150;
        int dai = bitmap.getHeight() * rong / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, rong, dai,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK)
                {
                    if (result.getData() != null)
                    {
                        Uri imageUri = result.getData().getData();

                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageShop.setImageBitmap(bitmap);
                            image_product = codeImage(bitmap);
                        }catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private boolean test()
    {
        if (image_product == null)
        {
            message("please add a photo");
            return false;
        }
        else if (binding.textTitle.getText().toString().isEmpty())
        {
            message("please add a title");
            return false;
        }
        else if (Genre == null)
        {
            message("please add a genre");
            return false;
        }
        else if (binding.textQuantity.getText().toString().isEmpty() || Integer.parseInt(binding.textQuantity.getText().toString()) < 0)
        {
            message("please add a quantity");
            return false;
        }
        else if (binding.textIntroduction.getText().toString().isEmpty())
        {
            message("please add a introduction");
            return false;
        }
        else if (binding.textMoney.getText().toString().isEmpty() || Double.parseDouble(binding.textMoney.getText().toString()) < 0)
        {
            message("please add a money");
            return false;
        }
        else
        {
            return true;
        }
    }
    private void loading (boolean loading)
    {
        if (loading)
        {
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.btnAdd.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progressbar.setVisibility(View.INVISIBLE);
            binding.btnAdd.setVisibility(View.VISIBLE);
        }
    }
    private void message(String mess)
    {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }
}