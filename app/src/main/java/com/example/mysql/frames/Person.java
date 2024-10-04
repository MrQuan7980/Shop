package com.example.mysql.frames;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mysql.activitys.SignIn;
import com.example.mysql.database.Constant;
import com.example.mysql.shop.SignUp_Shop;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.FragmentPersonBinding;

public class Person extends Fragment {
    private FragmentPersonBinding binding;
    private Preference preference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonBinding.inflate(inflater, container, false);

        preference = new Preference(requireContext());

        event_hading();
        out();

        return binding.getRoot();
    }
    public void event_hading()
    {
        binding.imagePerson.setImageBitmap(getImage(preference.getString(Constant.KEY_USER_IMAGE)));

        binding.name.setText(preference.getString(Constant.KEY_FULL_NAME));

        binding.email.setText(preference.getString(Constant.KEY_EMAIL));
    }

    private void out()
    {
        binding.tbnOut.setOnClickListener(v -> {
            preference.clear();

            startActivity(new Intent(requireContext(), SignIn.class));
        });
    }
    private Bitmap getImage(String image)
    {
        if (image != null && !image.isEmpty())
        {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        else
        {
            return null;
        }
    }
}