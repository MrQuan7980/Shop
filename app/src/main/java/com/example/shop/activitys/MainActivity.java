package com.example.shop.activitys;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shop.R;
import com.example.shop.databinding.ActivityMainBinding;
import com.example.shop.frames.Favorite;
import com.example.shop.frames.Home;
import com.example.shop.frames.Notification;
import com.example.shop.frames.Person;
import com.example.shop.frames.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handle();
    }
    private void handle()
    {
        binding.bottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home)
                {
                    LoadingView(new Home());
                    return true;
                }
                else if (itemId == R.id.search)
                {
                    LoadingView(new Search());
                    return true;
                }
                else if (itemId == R.id.favorite)
                {
                    LoadingView(new Favorite());
                    return true;
                }
                else if (itemId == R.id.notification)
                {
                    LoadingView(new Notification());
                    return true;
                }
                else if (itemId == R.id.person)
                {
                    LoadingView(new Person());
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        LoadingView(new Home());
    }
    private void LoadingView (Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}