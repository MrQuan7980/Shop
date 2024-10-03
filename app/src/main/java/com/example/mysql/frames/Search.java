package com.example.mysql.frames;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mysql.R;
import com.example.mysql.databinding.FragmentSearchBinding;

public class Search extends Fragment {
    private FragmentSearchBinding binding;
    private String search = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        search();

        return binding.getRoot();
    }
    private void search()
    {
        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                search = s.toString().trim();

                if (search.isEmpty())
                {
                    binding.frameSearch.setVisibility(View.INVISIBLE);
                }
                else
                {
                    binding.frameSearch.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}