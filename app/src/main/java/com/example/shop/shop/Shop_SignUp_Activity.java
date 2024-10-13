package com.example.shop.shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.activitys.SignInActivity;
import com.example.shop.database.Constant;
import com.example.shop.database.Preference;
import com.example.shop.databinding.ActivityShopSignUpBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Shop_SignUp_Activity extends AppCompatActivity {
    private ActivityShopSignUpBinding binding;
    private FirebaseFirestore database;
    private String image;
    private String date_time;
    private Preference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        preference = new Preference(this);

        handle();
    }
    private void handle()
    {
        binding.back.setOnClickListener(v -> onBackPressed());

        binding.imageProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.btnRegister.setOnClickListener(v -> {
            if (check_register())
            {
                register();
            }
        });
    }
    private void register()
    {
        progressbar_register_shop(true);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        date_time = dateFormat.format(date);

        HashMap<String, Object> create_shop_account = new HashMap<>();
        create_shop_account.put(Constant.key_shop_image, image);
        create_shop_account.put(Constant.key_shop_email, binding.inputEmail.getText().toString());
        create_shop_account.put(Constant.key_shop_name, binding.inputName.getText().toString());
        create_shop_account.put(Constant.key_shop_phone, binding.inputPhone.getText().toString());
        create_shop_account.put(Constant.key_shop_card, binding.inputCard.getText().toString());
        create_shop_account.put(Constant.key_shop_date, binding.inputDate.getText().toString());
        create_shop_account.put(Constant.key_shop_house, binding.inputHouseNumber.getText().toString());
        create_shop_account.put(Constant.key_shop_district, binding.inputDistrict.getText().toString());
        create_shop_account.put(Constant.key_shop_city, binding.inputCity.getText().toString());
        create_shop_account.put(Constant.key_shop_password, binding.password.getText().toString());
        create_shop_account.put(Constant.key_create_time_shop, date_time);
        create_shop_account.put(Constant.key_status, "shop");

        database.collection(Constant.key_collection_user)
                .add(create_shop_account)
                .addOnSuccessListener(documentReference -> {
                    progressbar_register_shop(false);

                    preference.putBoolean(Constant.key_user_sign_in, true);
                    preference.putString(Constant.key_user_id_shop, documentReference.getId());
                    preference.putString(Constant.key_shop_image, image);
                    preference.putString(Constant.key_shop_email, binding.inputEmail.getText().toString());
                    preference.putString(Constant.key_shop_name, binding.inputName.getText().toString());
                    preference.putString(Constant.key_shop_phone, binding.inputPhone.getText().toString());
                    preference.putString(Constant.key_shop_card, binding.inputCard.getText().toString());
                    preference.putString(Constant.key_shop_date, binding.inputDate.getText().toString());
                    preference.putString(Constant.key_shop_house, binding.inputHouseNumber.getText().toString());
                    preference.putString(Constant.key_shop_district, binding.inputDistrict.getText().toString());
                    preference.putString(Constant.key_shop_city, binding.inputCity.getText().toString());
                    preference.putString(Constant.key_create_time_shop, date_time);

                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }).addOnFailureListener(command -> {
                    progressbar_register_shop(false);

                    message("Registration failed");
                });
    }
    private boolean check_register()
    {
        if (image.isEmpty())
        {
            message("please add image");
            return false;
        }
        else if (binding.inputEmail.getText().toString().isEmpty())
        {
            message("please add email");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches())
        {
            message("please add email in correct format");
            return false;
        }
        else if (binding.inputName.getText().toString().isEmpty())
        {
            message("please add name");
            return false;
        }
        else if (binding.inputPhone.getText().toString().isEmpty() || binding.inputPhone.getText().toString().length() != 10)
        {
            message("please add phone");
            return false;
        }
        else if (binding.inputCard.getText().toString().isEmpty())
        {
            message("please add card");
            return false;
        }
        else if (binding.inputDate.getText().toString().isEmpty())
        {
            message("please add date");
            return false;
        }
        else if (binding.inputHouseNumber.getText().toString().isEmpty())
        {
            message("please add house number");
            return false;
        }
        else if (binding.inputDistrict.getText().toString().isEmpty())
        {
            message("please add district");
            return false;
        }
        else if (binding.inputCity.getText().toString().isEmpty())
        {
            message("please add city");
            return false;
        }
        else if (binding.password.getText().toString().isEmpty() || binding.password.getText().toString().length() < 5)
        {
            message("please add password");
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
                            binding.imageProfile.setImageBitmap(bitmap);
                            image = codeImage(bitmap);
                        }catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void progressbar_register_shop(boolean loading)
    {
        if (loading)
        {
            binding.progressRegister.setVisibility(View.VISIBLE);
            binding.btnRegister.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progressRegister.setVisibility(View.INVISIBLE);
            binding.btnRegister.setVisibility(View.VISIBLE);
        }
    }
    private void message(String mess)
    {
        Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
    }
}