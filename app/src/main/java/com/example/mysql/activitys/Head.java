package com.example.mysql.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.mysql.databinding.ActivityHeadBinding;

public class Head extends AppCompatActivity {
    private ActivityHeadBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHeadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        event_handing();
    }
    private void event_handing()
    {
        binding.button.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        });
    }
}