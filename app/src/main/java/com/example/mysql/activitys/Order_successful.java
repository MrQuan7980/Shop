package com.example.mysql.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.mysql.R;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityOrderSuccessfulBinding;
import com.example.mysql.frames.Home;
import com.example.mysql.frames.Person;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Order_successful extends AppCompatActivity {
    private ActivityOrderSuccessfulBinding binding;
    private Preference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderSuccessfulBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(this);

        Event_hading();
    }
    private void Event_hading()
    {
        Intent intent = getIntent();

        String image = intent.getStringExtra("image");
        String title = intent.getStringExtra("title");
        double money = intent.getDoubleExtra("money", 0.0);
        int quantity = intent.getIntExtra("quantity", 0);
        String dateTime = intent.getStringExtra("dateTime");

        binding.textTitle.setText(title);
        binding.imageProduct.setImageBitmap(getImage(image));
        binding.money.setText(String.valueOf(money));
        binding.textQuantity.setText(String.valueOf(quantity));

        binding.name.setText(preference.getString(Constant.KEY_FULL_NAME));

        binding.phone.setText(preference.getString(Constant.KEY_PHONE));

        binding.email.setText(preference.getString(Constant.KEY_EMAIL));

        binding.dateProduct.setText(dateTime);

        binding.houseNumber.setText(preference.getString(Constant.KEY_HOUSE_NUMBER));

        binding.district.setText(preference.getString(Constant.KEY_DISTRICT));

        binding.city.setText(preference.getString(Constant.KEY_CITY));

        binding.home.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Main.class));
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