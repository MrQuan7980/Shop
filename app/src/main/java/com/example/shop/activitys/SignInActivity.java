package com.example.shop.activitys;

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

import com.example.shop.shop.ShopActivity;
import com.example.shop.database.Constant;
import com.example.shop.database.Preference;
import com.example.shop.databinding.ActivitySignInBinding;
import com.example.shop.shop.Shop_SignUp_Activity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private String image;
    private FirebaseFirestore database;
    private Preference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        preference = new Preference(this);

        loading_visibility();
        handle();
    }
    private void handle()
    {
        binding.imageProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.btnRegister.setOnClickListener(v -> {
            if (check_registration())
            {
                check_email();
            }
        });
        binding.btnSignIn.setOnClickListener(v -> {
            if (check_sign_in())
            {
                sign_in();
            }

        });
        Intent intent = getIntent();

        String email = intent.getStringExtra("email");

        binding.email.setText(email);

        binding.btnChangePass.setOnClickListener(v -> {
            Intent intent_change = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            startActivity(intent_change);
            finish();
        });

        binding.image.setOnClickListener(v -> {
            Intent intent_shop = new Intent(getApplicationContext(), Shop_SignUp_Activity.class);
            startActivity(intent_shop);

        });
    }
    private void sign_in()
    {
        progressbar_sign_in(true);

        database.collection(Constant.key_collection_user)
                .whereEqualTo(Constant.key_email, binding.email.getText().toString())
                .whereEqualTo(Constant.key_password, binding.inputPass.getText().toString())
                .whereEqualTo(Constant.key_status, "user")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().getDocumentChanges().isEmpty())
                    {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        progressbar_sign_in(false);

                        preference.putBoolean(Constant.key_user_sign_in, true);
                        preference.putString(Constant.key_user_id, documentSnapshot.getId());
                        preference.putString(Constant.key_email, binding.email.getText().toString());
                        preference.putString(Constant.key_name, documentSnapshot.getString(Constant.key_name));
                        preference.putString(Constant.key_image, documentSnapshot.getString(Constant.key_image));
                        preference.putString(Constant.key_phone, documentSnapshot.getString(Constant.key_phone));
                        preference.putString(Constant.key_date, documentSnapshot.getString(Constant.key_date));
                        preference.putString(Constant.key_house_number, documentSnapshot.getString(Constant.key_house_number));
                        preference.putString(Constant.key_district, documentSnapshot.getString(Constant.key_district));
                        preference.putString(Constant.key_city, documentSnapshot.getString(Constant.key_city));

                        Intent intent_sign_in = new Intent(getApplicationContext(), MainActivity.class);
                        intent_sign_in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent_sign_in);
                        finish();
                    }
                    else
                    {
                        progressbar_sign_in(false);

                        sign_shop();
                    }
                });

    }

    private void sign_shop()
    {
        progressbar_sign_in(true);

        database.collection(Constant.key_collection_user)
                .whereEqualTo(Constant.key_shop_email, binding.email.getText().toString())
                .whereEqualTo(Constant.key_shop_password, binding.inputPass.getText().toString())
                .whereEqualTo(Constant.key_status, "shop")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().getDocuments().isEmpty())
                    {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        progressbar_sign_in(false);

                        preference.putString(Constant.key_user_id_shop, documentSnapshot.getId());
                        preference.putString(Constant.key_shop_image, documentSnapshot.getString(Constant.key_shop_image));
                        preference.putString(Constant.key_shop_email, binding.email.getText().toString());
                        preference.putString(Constant.key_shop_name, documentSnapshot.getString(Constant.key_shop_name));
                        preference.putString(Constant.key_shop_phone, documentSnapshot.getString(Constant.key_shop_phone));
                        preference.putString(Constant.key_shop_card, documentSnapshot.getString(Constant.key_shop_card));
                        preference.putString(Constant.key_shop_date, documentSnapshot.getString(Constant.key_shop_date));
                        preference.putString(Constant.key_shop_house, documentSnapshot.getString(Constant.key_shop_house));
                        preference.putString(Constant.key_shop_district, documentSnapshot.getString(Constant.key_shop_district));
                        preference.putString(Constant.key_shop_city, documentSnapshot.getString(Constant.key_shop_city));
                        preference.putString(Constant.key_create_time_shop, documentSnapshot.getString(Constant.key_create_time_shop));

                        Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                    else
                    {
                        progressbar_sign_in(false);
                        message("sign in failed");
                    }
                });
    }
    private void register()
    {
        progressbar_register(true);

        HashMap<String, Object> user = new HashMap<>();

        user.put(Constant.key_image, image);
        user.put(Constant.key_email, binding.inputEmail.getText().toString());
        user.put(Constant.key_name, binding.inputName.getText().toString());
        user.put(Constant.key_phone, binding.inputPhone.getText().toString());
        user.put(Constant.key_date, binding.inputDate.getText().toString());
        user.put(Constant.key_house_number, binding.inputHouseNumber.getText().toString());
        user.put(Constant.key_district, binding.inputDistrict.getText().toString());
        user.put(Constant.key_city, binding.inputCity.getText().toString());
        user.put(Constant.key_password, binding.password.getText().toString());
        user.put(Constant.key_status, "user");

        database.collection(Constant.key_collection_user)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    progressbar_register(false);

                    preference.putBoolean(Constant.key_user_sign_in, true);
                    preference.putString(Constant.key_user_id, documentReference.getId());
                    preference.putString(Constant.key_image, image);
                    preference.putString(Constant.key_email, binding.inputEmail.getText().toString());
                    preference.putString(Constant.key_name, binding.inputName.getText().toString());
                    preference.putString(Constant.key_phone, binding.inputPhone.getText().toString());
                    preference.putString(Constant.key_date, binding.inputDate.getText().toString());
                    preference.putString(Constant.key_house_number, binding.inputHouseNumber.getText().toString());
                    preference.putString(Constant.key_district, binding.inputDistrict.getText().toString());
                    preference.putString(Constant.key_city, binding.inputCity.getText().toString());

                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.putExtra("email", binding.inputEmail.getText().toString());
                    startActivity(intent);

                    message("Registration success");

                    binding.buttonSignIn.setVisibility(View.VISIBLE);

                    binding.signInFrom.setVisibility(View.VISIBLE);

                    binding.btnRegisten.setVisibility(View.GONE);

                    binding.register.setVisibility(View.GONE);

                }).addOnFailureListener(command -> {
                    progressbar_register(false);

                    message("Registration failed");
                });
    }

    private void check_email()
    {
        database.collection(Constant.key_collection_user)
                .whereEqualTo(Constant.key_email, binding.inputEmail.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        if (!task.getResult().isEmpty())
                        {
                            message("email exists");
                            return;
                        }
                        else
                        {
                            check_phone();
                        }
                    }
                    else
                    {
                        message("error register");
                    }
                });
    }

    private void check_phone()
    {
        database.collection(Constant.key_collection_user)
                .whereEqualTo(Constant.key_phone, binding.inputPhone.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        if (!task.getResult().isEmpty())
                        {
                            message("phone exists");
                            return;
                        }
                        else
                        {
                            register();
                        }
                    }
                    else
                    {
                        message("error phone");
                    }
                });
    }

    private void loading_visibility()
    {
        binding.buttonSignIn.setOnClickListener(v -> {
            binding.signInFrom.setVisibility(View.VISIBLE);
        });

        binding.registerTransitions.setOnClickListener(v -> {
            binding.buttonSignIn.setVisibility(View.GONE);
            binding.btnRegisten.setVisibility(View.VISIBLE);

            binding.register.setVisibility(View.VISIBLE);

            binding.signInFrom.setVisibility(View.GONE);

            resetSignIn();

            binding.signInTransitions.setOnClickListener(v1 -> {
                binding.signInFrom.setVisibility(View.VISIBLE);
                binding.buttonSignIn.setVisibility(View.VISIBLE);

                binding.btnRegisten.setVisibility(View.GONE);

                binding.register.setVisibility(View.GONE);

                binding.signInFrom.setVisibility(View.VISIBLE);

                resetRegister();
            });
        });


        binding.btnChangePass.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            startActivity(intent);
        });
    }

    private boolean check_sign_in()
    {
        if (binding.email.getText().toString().isEmpty())
        {
            message("please add email");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches())
        {
            message("enter correct gmail format");
            return false;
        }
        else if (binding.inputPass.getText().toString().isEmpty())
        {
            message("please add password");
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean check_registration()
    {
        if (image.isEmpty())
        {
            message("please add photo");
            return false;
        }
        else if (!binding.checkbox.isChecked())
        {
            message("please agree to the terms");
            return false;
        }
        else if (binding.inputEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches())
        {
            message("please add email");
            return false;
        }
        else if (binding.inputName.getText().toString().isEmpty())
        {
            message("please add name");
            return false;
        }
        else if (binding.inputPhone.getText().toString().isEmpty())
        {
            message("please add phone");
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
        else if (binding.password.getText().toString().isEmpty())
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

    private void progressbar_register(boolean loading)
    {
        if (loading)
        {
            binding.progessRegister.setVisibility(View.VISIBLE);
            binding.btnRegister.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progessRegister.setVisibility(View.INVISIBLE);
            binding.btnRegister.setVisibility(View.VISIBLE);
        }
    }

    private void progressbar_sign_in(boolean loading)
    {
        if (loading)
        {
            binding.progressSignIn.setVisibility(View.VISIBLE);
            binding.btnSignIn.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progressSignIn.setVisibility(View.INVISIBLE);
            binding.btnSignIn.setVisibility(View.VISIBLE);
        }
    }
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }

    private void resetRegister()
    {
        binding.imageProfile.setImageBitmap(null);
        binding.inputEmail.setText("");
        binding.inputName.setText("");
        binding.inputPhone.setText("");
        binding.inputDate.setText("");
        binding.inputHouseNumber.setText("");
        binding.inputDistrict.setText("");
        binding.inputCity.setText("");
        binding.inputPass.setText("");
        binding.checkbox.setChecked(false);
    }
    private void resetSignIn()
    {
        binding.email.setText("");
        binding.password.setText("");
    }
}