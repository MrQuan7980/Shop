package com.example.shop.frames;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shop.R;
import com.example.shop.activitys.MainActivity;
import com.example.shop.activitys.SignInActivity;
import com.example.shop.database.Constant;
import com.example.shop.database.Preference;
import com.example.shop.databinding.FragmentPersonBinding;

public class Person extends Fragment {
    private FragmentPersonBinding binding;
    private Preference preference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonBinding.inflate(inflater, container, false);
        preference = new Preference(getContext());

        handle();
        return binding.getRoot();
    }
    private void handle()
    {
        binding.imageProfile.setImageBitmap(getImage(preference.getString(Constant.key_image)));

        binding.textName.setText(preference.getString(Constant.key_name));
        binding.textEmail.setText(preference.getString(Constant.key_email));

        binding.home.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        binding.btnLogOutAccount.setOnClickListener(v -> {
            preference.clear();
            Intent intent = new Intent(getContext(), SignInActivity.class);
            startActivity(intent);
        });
    }
    private Bitmap getImage(String image)
    {
        if (image != null && !image.isEmpty())
        {
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else
        {
            return null;
        }
    }

}