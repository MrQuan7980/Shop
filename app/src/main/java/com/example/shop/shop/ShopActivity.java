package com.example.shop.shop;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shop.R;
import com.example.shop.databinding.ActivityShopBinding;
import com.example.shop.frames_shop.home_shop;
import com.example.shop.frames_shop.notifications_shop;
import com.example.shop.frames_shop.order_shop;
import com.example.shop.frames_shop.person_shop;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShopActivity extends AppCompatActivity {
    private ActivityShopBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handle();
    }
    private void handle()
    {
        binding.bottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int item_shop = item.getItemId();

                if (item_shop == R.id.home_shop)
                {
                    loadingView(new home_shop());
                    return true;
                }
                else if (item_shop == R.id.card_shop)
                {
                    loadingView(new order_shop());
                    return true;
                }
                if (item_shop == R.id.notification)
                {
                    loadingView(new notifications_shop());
                    return true;
                }
                else if (item_shop == R.id.user_shop)
                {
                    loadingView(new person_shop());
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });

        loadingView(new home_shop());
    }

    private void loadingView(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, fragment);

        fragmentTransaction.commit();
    }
}