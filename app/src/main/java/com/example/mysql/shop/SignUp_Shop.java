package com.example.mysql.shop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mysql.Encoding.encoding;
import com.example.mysql.activitys.SignIn;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivitySignUpShopBinding;
import com.example.mysql.shop.Shop;
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

public class SignUp_Shop extends AppCompatActivity {
    private ActivitySignUpShopBinding binding;
    private Preference preference;
    private FirebaseFirestore database;
    private String enImage = "";
    private String id = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(this);
        database = FirebaseFirestore.getInstance();

        loading1();
        event_hading();

    }
    private void event_hading()
    {
        binding.btnSignUp.setOnClickListener(v -> {
            if (test())
            {
                test_email();
            }
        });

        binding.imageProifile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.imageBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
            finish();
        });
    }
    private void test_email()
    {
        loading(true);
        database.collection(Constant.KEY_TABLE_USER)
                .whereEqualTo(Constant.KEY_EMAIL, binding.inputEmail.getText().toString())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty())
                    {
                        loading(false);
                        message("email đã đăng kí");
                    }
                    else
                    {
                        loading(false);
                        signUp();
                    }
                });
    }
    private void signUp()
    {
        loading(true);

        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String TimeString = dateFormat.format(time);

        HashMap<String, Object> user = new HashMap<>();
        user.put(Constant.KEY_USER_SHOP_ID, id);
        user.put(Constant.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constant.KEY_USER_IMAGE, enImage);
        user.put(Constant.KEY_FULL_NAME, binding.inputFullname.getText().toString());
        user.put(Constant.KEY_HOUSE_NUMBER, binding.houseNumber.getText().toString());
        user.put(Constant.KEY_DISTRICT, binding.district.getText().toString());
        user.put(Constant.KEY_CITY, binding.city.getText().toString());
        user.put(Constant.KEY_PHONE, binding.inputPhone.getText().toString());
        user.put(Constant.KEY_DATE, binding.inputDate.getText().toString());
        user.put(Constant.KEY_PASSWORD, encoding.hashPassword(binding.inputPassword.getText().toString()));
        user.put(Constant.KEY_SUBJECT, "shop");
        user.put(Constant.KEY_TIME, TimeString);

        database.collection(Constant.KEY_TABLE_USER)
                .add(user)
                .addOnSuccessListener(documentReference ->
                {
                    loading(false);

                    preference.putBoolean(Constant.KEY_USER_SIGN_IN, true);
                    preference.putString(Constant.KEY_USER_SHOP_ID, id);
                    preference.putString(Constant.KEY_USER_IMAGE, enImage);
                    preference.putString(Constant.KEY_EMAIL, binding.inputEmail.getText().toString());
                    preference.putString(Constant.KEY_FULL_NAME, binding.inputFullname.getText().toString());
                    preference.putString(Constant.KEY_PHONE, binding.inputPhone.getText().toString());
                    preference.putString(Constant.KEY_DATE, binding.inputDate.getText().toString());
                    preference.putString(Constant.KEY_HOUSE_NUMBER, binding.houseNumber.getText().toString());
                    preference.putString(Constant.KEY_DISTRICT, binding.district.getText().toString());
                    preference.putString(Constant.KEY_CITY, binding.city.getText().toString());
                    preference.putString(Constant.KEY_PASSWORD, binding.inputPassword.getText().toString());
                    preference.putString(Constant.KEY_TIME, TimeString);

                    Intent intent = new Intent(getApplicationContext(), Shop.class);
                    intent.putExtra("email_shop", binding.inputEmail.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                })
                .addOnFailureListener(command ->
                {
                    loading(false);
                    message("Đăng kí thất bại");
                });
    }

    private boolean test()
    {
        if (enImage == null)
        {
            message("Vui lòng thêm vào ảnh");
            return false;
        }
        else if (binding.inputEmail.getText().toString().isEmpty())
        {
            message("Vui lòng nhập vào email");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches())
        {
            message("Vui lòng nhập đúng định dạng email");
            return false;
        }
        else if (binding.inputFullname.getText().toString().isEmpty())
        {
            message("Vui lòng nhập vào họ & tên");
            return false;
        }
        else if (binding.inputPhone.getText().toString().isEmpty())
        {
            message("Vui lòng nhập vào số điện thoại");
            return false;
        }
        else if (binding.inputDate.getText().toString().isEmpty())
        {
            message("Vui lòng nhập vào ngày tháng năm");
            return false;
        }
        else if (binding.inputPassword.getText().toString().isEmpty())
        {
            message("Vui lòng nhập vào họ & tên");
            return false;
        }
        else if (binding.houseNumber.getText().toString().isEmpty() || binding.district.getText().toString().isEmpty() || binding.city.getText().toString().isEmpty())
        {
            message("Vui lòng nhập vào địa chỉ cụ thể");
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
                            binding.imageProifile.setImageBitmap(bitmap);
                            enImage = codeImage(bitmap);
                        }catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void loading1()
    {
        binding.checkBook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    binding.btnSign.setVisibility(View.GONE);
                    binding.btnSignUp.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.btnSign.setVisibility(View.VISIBLE);
                    binding.btnSignUp.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private void loading(boolean progressbar)
    {
        if (progressbar)
        {
            binding.progesbar.setVisibility(View.VISIBLE);
            binding.btnSignUp.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progesbar.setVisibility(View.INVISIBLE);
            binding.btnSignUp.setVisibility(View.VISIBLE);
        }
    }
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
}