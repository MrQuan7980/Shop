package com.example.shop.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shop.R;
import com.example.shop.database.Constant;
import com.example.shop.databinding.ActivityChangePasswordBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    private FirebaseFirestore database;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();

        handle();
    }
    private void handle()
    {
        binding.back.setOnClickListener(v -> onBackPressed());

        binding.btnChangePassword.setOnClickListener(v -> {
            if (check_password())
            {
                update_password();
            }
        });

        Intent intent = getIntent();

        phone = intent.getStringExtra("phone");
    }

    private void update_password()
    {
        progressbar_change(true);

        HashMap<String, Object> update = new HashMap<>();

        update.put(Constant.key_password, binding.password.getText().toString());

        database.collection(Constant.key_collection_user)
                .whereEqualTo(Constant.key_phone, phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty())
                    {
                        progressbar_change(true);
                        String documenId = task.getResult().getDocuments().get(0).getId();

                        database.collection(Constant.key_collection_user)
                                .document(documenId)
                                .update(update)
                                .addOnCompleteListener(update_phone -> {

                                    if (update_phone.isSuccessful())
                                    {
                                        progressbar_change(false);

                                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                        startActivity(intent);
                                        finish();

                                        message("update successfully");
                                    }
                                    else
                                    {
                                        progressbar_change(false);

                                        message("unsuccessful exchange");
                                    }

                                });
                    }
                    else
                    {
                        progressbar_change(false);
                        message("phone number does not exist");
                    }
                });
    }
    private boolean check_password()
    {
        if (binding.password.getText().toString().isEmpty())
        {
            message("please add password");
            return false;
        }
        else if (binding.confirmPassword.getText().toString().isEmpty())
        {
            message("please enter confirm password");
            return false;
        }
        else if (!binding.password.getText().toString().equals(binding.confirmPassword.getText().toString()))
        {
            message("the password you entered does not match");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void progressbar_change(boolean loading)
    {
        if (loading)
        {
            binding.progressChange.setVisibility(View.VISIBLE);
            binding.btnChangePassword.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progressChange.setVisibility(View.INVISIBLE);
            binding.btnChangePassword.setVisibility(View.VISIBLE);
        }
    }
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
}