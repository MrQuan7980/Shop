package com.example.mysql.frames;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mysql.activitys.SignIn;
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

    }

    private void out()
    {
        binding.tbnOut.setOnClickListener(v -> {
            preference.clear();

            startActivity(new Intent(requireContext(), SignIn.class));
        });
    }
}