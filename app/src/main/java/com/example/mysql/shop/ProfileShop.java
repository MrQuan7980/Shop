package com.example.mysql.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mysql.R;
import com.example.mysql.activitys.SignIn;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityProfileShopBinding;

public class ProfileShop extends AppCompatActivity {
    private ActivityProfileShopBinding binding;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(this);

        out();
    }
    private void out()
    {
        binding.tbnOut.setOnClickListener(v -> {
            preference.clearShopId();

            startActivity(new Intent(getApplicationContext(), SignIn.class));
            finish();
        });
    }
}