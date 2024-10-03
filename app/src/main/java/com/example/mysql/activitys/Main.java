package com.example.mysql.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mysql.R;
import com.example.mysql.databinding.ActivityMainBinding;
import com.example.mysql.frames.Heart;
import com.example.mysql.frames.Home;
import com.example.mysql.frames.Person;
import com.example.mysql.frames.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Even_handling();
    }
    private void Even_handling()
    {
        binding.bottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home)
                {
                    LoadingView(new Home());
                    return true;
                }
                else if (itemId == R.id.navigation_search)
                {
                    LoadingView(new Search());
                    return true;
                }
                else if (itemId == R.id.navigation_heart)
                {
                    LoadingView(new Heart());
                    return true;
                }
                else if (itemId == R.id.navigation_profile)
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
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

}