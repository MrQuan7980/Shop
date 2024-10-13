package com.example.shop.frames_shop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shop.R;
import com.example.shop.database.Preference;
import com.example.shop.databinding.FragmentNotificationsShopBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class notifications_shop extends Fragment {
    private FragmentNotificationsShopBinding binding;
    private Preference preference;
    private FirebaseFirestore database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsShopBinding.inflate(inflater, container, false);
        preference = new Preference(requireContext());
        database = FirebaseFirestore.getInstance();
        handle();
        loading_notification();
        return binding.getRoot();
    }
    private void handle()
    {

    }
    private void loading_notification()
    {

    }
}