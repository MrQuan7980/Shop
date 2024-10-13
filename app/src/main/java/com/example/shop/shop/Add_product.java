package com.example.shop.shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shop.R;
import com.example.shop.database.Constant;
import com.example.shop.database.Preference;
import com.example.shop.databinding.ActivityAddProductBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Add_product extends AppCompatActivity {
    private ActivityAddProductBinding binding;
    private String image_one, image_two, image_three, image_four;
    private Preference preference;
    private FirebaseFirestore database;
    private String id = UUID.randomUUID().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        preference = new Preference(this);

        handle();
    }
    public void handle()
    {
        binding.btnAddProduct.setOnClickListener(v -> {
            if (check_input_product())
            {
                add_product();
            }
        });

        binding.imageProductOne.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.imageProductTwo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage_two.launch(intent);
        });

        binding.imageProductThree.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage_three.launch(intent);
        });

        binding.imageProductFour.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage_four.launch(intent);
        });
    }
    private void add_product()
    {
        loading_add_product(true);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date_product = dateFormat.format(date);

        String moneyString = binding.textMoney.getText().toString();
        double money = 0.0;
        try {
            money = Double.parseDouble(moneyString);
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        HashMap<String, Object> product = new HashMap<>();
        product.put(Constant.key_product_id, id);
        product.put(Constant.key_product_image_one, image_one);
        product.put(Constant.key_product_image_two, image_two);
        product.put(Constant.key_product_image_three, image_three);
        product.put(Constant.key_product_image_four, image_four);
        product.put(Constant.key_product_title, binding.inputTitleProduct.getText().toString());
        product.put(Constant.key_product_category, binding.inputType.getText().toString());
        product.put(Constant.key_product_introduce, binding.inputIntroduction.getText().toString());
        product.put(Constant.key_product_sold, 0);
        product.put(Constant.key_product_quantity, Integer.parseInt(binding.inputQuantity.getText().toString()));
        product.put(Constant.key_product_money, binding.textMoney.getText().toString());
        product.put(Constant.key_user_id_shop, preference.getString(Constant.key_user_id_shop));
        product.put(Constant.key_shop_name, preference.getString(Constant.key_shop_name));
        product.put(Constant.key_shop_image, preference.getString(Constant.key_shop_image));
        product.put(Constant.key_shop_email, preference.getString(Constant.key_shop_email));
        product.put(Constant.key_shop_phone, preference.getString(Constant.key_shop_phone));
        product.put(Constant.key_shop_house, preference.getString(Constant.key_shop_house));
        product.put(Constant.key_shop_district, preference.getString(Constant.key_shop_district));
        product.put(Constant.key_shop_city, preference.getString(Constant.key_shop_city));
        product.put(Constant.key_shop_date, date_product);


        database.collection(Constant.key_collection_product)
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    loading_add_product(false);
                    message("add success");

                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                    startActivity(intent);
                    finish();

                }).addOnFailureListener(e -> {
                    loading_add_product(false);
                    message("add failure");

                });
    }
    private boolean check_input_product()
    {
        if (image_one.isEmpty())
        {
            message("Add image one");
            return false;
        }
        else if (image_two.isEmpty())
        {
            message("Add image two");
            return false;
        }
        else if (image_three.isEmpty())
        {
            message("Add image three");
            return false;
        }
        else if (image_four.isEmpty())
        {
            message("Add image four");
            return false;
        }
        else if (binding.inputTitleProduct.getText().toString().isEmpty())
        {
            message("Add title product");
            return false;
        }
        else if (binding.inputType.getText().toString().isEmpty())
        {
            message("Add type product");
            return false;
        }
        else if (binding.inputIntroduction.getText().toString().isEmpty())
        {
            message("Add introduction product");
            return false;
        }
        else if (binding.inputMoney.getText().toString().isEmpty() || Double.parseDouble(binding.inputMoney.getText().toString()) < 0.0)

        {
            message("Add money product");
            return false;
        }
        else if (binding.inputQuantity.getText().toString().isEmpty() || Integer.parseInt(binding.inputQuantity.getText().toString()) < 0)
        {
            message("Add quantity product");
            return false;
        }
        else
        {
            return true;
        }
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
                            binding.imageProductOne.setImageBitmap(bitmap);
                            image_one = codeImage(bitmap);
                        }catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public final ActivityResultLauncher<Intent> pickImage_two = registerForActivityResult(
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
                            binding.imageProductTwo.setImageBitmap(bitmap);
                            image_two = codeImage(bitmap);
                        }catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public final ActivityResultLauncher<Intent> pickImage_three = registerForActivityResult(
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
                            binding.imageProductThree.setImageBitmap(bitmap);
                            image_three = codeImage(bitmap);
                        }catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public final ActivityResultLauncher<Intent> pickImage_four = registerForActivityResult(
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
                            binding.imageProductFour.setImageBitmap(bitmap);
                            image_four = codeImage(bitmap);
                        }catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private void loading_add_product(boolean loading)
    {
        if (loading)
        {
            binding.btnAddProduct.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.btnAddProduct.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.INVISIBLE);
        }
    }
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
}