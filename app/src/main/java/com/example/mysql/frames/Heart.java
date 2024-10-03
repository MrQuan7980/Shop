package com.example.mysql.frames;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mysql.OOP.Product;
import com.example.mysql.R;
import com.example.mysql.adapters.AdapterFavorite;
import com.example.mysql.adapters.AdapterProduct;
import com.example.mysql.adapters.AdapterTitle;
import com.example.mysql.databinding.FragmentHeartBinding;
import com.example.mysql.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Heart extends Fragment {
    private FragmentHeartBinding binding;
    private List<Product> productList;
    private AdapterFavorite adapterFavorite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHeartBinding.inflate(inflater, container, false);
        productList = new ArrayList<>();

        loadingLove();

        return binding.getRoot();

    }
    private void loadingLove()
    {

    }
}