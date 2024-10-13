package com.example.shop.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.database.Constant;
import com.example.shop.databinding.ActivityResetPasswordBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;
    private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        handle();
    }
    private void handle()
    {
        binding.imageBack.setOnClickListener(v -> onBackPressed());

        binding.btnResetPassword.setOnClickListener(v -> {
            if (check_email_and_phone())
            {
                check_existence();
            }
        });
    }
    public void check_existence()
    {
        progressbar_reset(true);

        database.collection(Constant.key_collection_user)
                .whereEqualTo(Constant.key_email, binding.email.getText().toString())
                .whereEqualTo(Constant.key_phone, binding.phone.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        progressbar_reset(false);

                        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);

                        intent.putExtra("phone", binding.phone.getText().toString());

                        startActivity(intent);
                    }
                    else
                    {
                        progressbar_reset(false);

                        message("email or phone not accurate");
                        return;
                    }
                });
    }

    private boolean check_email_and_phone()
    {
        if (binding.email.getText().toString().isEmpty())
        {
            message("please add email");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches())
        {
            message("please enter correct format");
            return false;
        }
        else if (binding.phone.getText().toString().isEmpty())
        {
            message("please add phone`");
            return false;
        }
        else
        {
            return true;
        }
    }
    private void progressbar_reset(boolean loading)
    {
        if (loading)
        {
            binding.progressSignIn.setVisibility(View.VISIBLE);
            binding.btnResetPassword.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progressSignIn.setVisibility(View.INVISIBLE);
            binding.btnResetPassword.setVisibility(View.VISIBLE);
        }
    }
    private void message(String mes)
    {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }
}