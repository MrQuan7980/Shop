package com.example.mysql.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import com.example.mysql.Encoding.encoding;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivitySignInBinding;
import com.example.mysql.frames.Home;
import com.example.mysql.shop.Shop;
import com.example.mysql.shop.SignUp_Shop;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private Preference preference;
    private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(this);
        database = FirebaseFirestore.getInstance();

        event_halding();
    }
    private void event_halding()
    {
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String emai_shop = intent.getStringExtra("email_shop");

        binding.inputEmail.setText(email);

        binding.inputEmail.setText(emai_shop);

        binding.btnLogIn.setOnClickListener(v -> {
            if (test())
            {
                SignIn_use();
            }
        });
        binding.use.setOnClickListener(v -> {
            Intent intent_signUp_user = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent_signUp_user);
            finish();
        });

        binding.shop.setOnClickListener(v -> {
            Intent intent_signUp_shop = new Intent(getApplicationContext(), SignUp_Shop.class);
            startActivity(intent_signUp_shop);
            finish();
        });

        binding.iconLock.setOnClickListener(v -> {

            binding.inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.iconLock.setVisibility(View.INVISIBLE);
            binding.iconEye.setVisibility(View.VISIBLE);

            binding.inputPassword.setSelection(binding.inputPassword.getText().length());
        });

        binding.iconEye.setOnClickListener(v -> {

            binding.inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.iconEye.setVisibility(View.INVISIBLE);
            binding.iconLock.setVisibility(View.VISIBLE);

            binding.inputPassword.setSelection(binding.inputPassword.getText().length());
        });


    }
    private void SignIn_use()
    {
        loading(true);
        String password = encoding.hashPassword(binding.inputPassword.getText().toString());
        database.collection(Constant.KEY_TABLE_USER)
                .whereEqualTo(Constant.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constant.KEY_PASSWORD, password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocumentChanges().size() > 0) {
                        loading(false);

                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        String subjeck = documentSnapshot.getString(Constant.KEY_SUBJECT);

                        if (subjeck != null)
                        {
                            if (subjeck.equals("use"))
                            {
                                preference.putBoolean(Constant.KEY_USER_SIGN_IN, true);
                                preference.putString(Constant.KEY_USER_ID, documentSnapshot.getId());
                                preference.putString(Constant.KEY_USER_IMAGE, documentSnapshot.getString(Constant.KEY_USER_IMAGE));
                                preference.putString(Constant.KEY_EMAIL, binding.inputEmail.getText().toString());
                                preference.putString(Constant.KEY_HOUSE_NUMBER, documentSnapshot.getString(Constant.KEY_HOUSE_NUMBER));
                                preference.putString(Constant.KEY_DISTRICT, documentSnapshot.getString(Constant.KEY_DISTRICT));
                                preference.putString(Constant.KEY_CITY, documentSnapshot.getString(Constant.KEY_CITY));
                                preference.putString(Constant.KEY_FULL_NAME, documentSnapshot.getString(Constant.KEY_FULL_NAME));
                                preference.putString(Constant.KEY_PHONE, documentSnapshot.getString(Constant.KEY_PHONE));
                                preference.putString(Constant.KEY_DATE, documentSnapshot.getString(Constant.KEY_DATE));
                                preference.putString(Constant.KEY_PASSWORD, binding.inputPassword.getText().toString());
                                preference.putString(Constant.KEY_TIME, documentSnapshot.getString(Constant.KEY_TIME));
                                preference.putString(Constant.KEY_SUBJECT, "use");

                                Intent intent = new Intent(getApplicationContext(), Main.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else if (subjeck.equals("shop"))
                            {
                                preference.putBoolean(Constant.KEY_USER_SIGN_IN, true);
                                preference.putString(Constant.KEY_USER_SHOP_ID, documentSnapshot.getId());
                                preference.putString(Constant.KEY_EMAIL, binding.inputEmail.getText().toString());
                                preference.putString(Constant.KEY_USER_IMAGE, documentSnapshot.getString(Constant.KEY_USER_IMAGE));
                                preference.putString(Constant.KEY_FULL_NAME, documentSnapshot.getString(Constant.KEY_FULL_NAME));
                                preference.putString(Constant.KEY_PHONE, documentSnapshot.getString(Constant.KEY_PHONE));
                                preference.putString(Constant.KEY_HOUSE_NUMBER, documentSnapshot.getString(Constant.KEY_HOUSE_NUMBER));
                                preference.putString(Constant.KEY_DISTRICT, documentSnapshot.getString(Constant.KEY_DISTRICT));
                                preference.putString(Constant.KEY_CITY, documentSnapshot.getString(Constant.KEY_CITY));
                                preference.putString(Constant.KEY_DATE, documentSnapshot.getString(Constant.KEY_DATE));
                                preference.putString(Constant.KEY_PASSWORD, binding.inputPassword.getText().toString());
                                preference.putString(Constant.KEY_TIME, documentSnapshot.getString(Constant.KEY_TIME));
                                preference.putString(Constant.KEY_SUBJECT, "shop");

                                Intent intent = new Intent(getApplicationContext(), Shop.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                message("Đăng nhập thất bại");
                            }
                        }
                    }
                    else
                    {
                        loading(false);
                        message("Thông báo thất bại");
                    }
                });
    }

    private boolean test()
    {
        if(binding.inputEmail.getText().toString().isEmpty())
        {
            message("Vui lòng nhập vào email");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches())
        {
            message("Vui lòng nhập vào email đúng địng dạng");
            return false;
        }
        else if (binding.inputPassword.getText().toString().isEmpty())
        {
            message("Vui lòng nhâp vào mật khẩu");
            return false;
        }
        else
        {
            return true;
        }
    }
    private void loading(boolean progressbar)
    {
        if (progressbar)
        {
            binding.progesbar.setVisibility(View.VISIBLE);
            binding.btnLogIn.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progesbar.setVisibility(View.INVISIBLE);
            binding.btnLogIn.setVisibility(View.VISIBLE);
        }
    }
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
}