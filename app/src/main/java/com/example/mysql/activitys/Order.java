package com.example.mysql.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.example.mysql.R;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityOrderBinding;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Order extends AppCompatActivity {
    private ActivityOrderBinding binding;
    private Preference preference;
    private int sol1 = 0;
    private String id_product, title_product, image_product, introduction_product, genre_product, dateTime_product;
    private String shopId, shopImage, shopName, phoneShop, cityShop;
    private double money;
    private int quantity, soldShop;
    private double sum = 0.0, each_item = 0.0, ship = 3.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(getApplicationContext());

        loadingQuantity();
        event_hading();
        loading_information();
    }
    private void event_hading()
    {
        binding.btnButton.setOnClickListener(v -> {
            if (tests())
            {

            }
        });

        binding.back.setOnClickListener(v -> onBackPressed());
    }
    private void loading_information()
    {
        Intent intent = getIntent();

        id_product = intent.getStringExtra("id_product");
        image_product = intent.getStringExtra("image_product");
        title_product = intent.getStringExtra("title_product");
        money = intent.getDoubleExtra("money_product", 0.0);
        quantity = intent.getIntExtra("quantity_product", 0);
        introduction_product = intent.getStringExtra("introduction_product");
        genre_product = intent.getStringExtra("genre_product");

        shopId = intent.getStringExtra("id_shop");
        shopName = intent.getStringExtra("name_shop");
        shopImage = intent.getStringExtra("image_shop");
        phoneShop = intent.getStringExtra("phone_shop");
        cityShop = intent.getStringExtra("city_shop");
        soldShop = intent.getIntExtra("sold_shop", 0);

        binding.imageProduct.setImageBitmap(getImage(image_product));

        binding.textTitle.setText(title_product);

        binding.quantity.setText(String.valueOf(quantity));

        DecimalFormat formatter = new DecimalFormat("###,##0.00");
        binding.money.setText(formatter.format(money));

        // hiển thị thông tin mua hàng
        binding.name.setText(preference.getString(Constant.KEY_FULL_NAME));

        binding.phone.setText(preference.getString(Constant.KEY_PHONE));

        binding.email.setText(preference.getString(Constant.KEY_EMAIL));

        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String TimeString = dateFormat.format(time);

        binding.dateProduct.setText(TimeString);

        binding.houseNumber.setText(preference.getString(Constant.KEY_HOUSE_NUMBER));

        binding.district.setText(preference.getString(Constant.KEY_DISTRICT));

        binding.city.setText(preference.getString(Constant.KEY_CITY));
    }
    private void loadingQuantity()
    {
        binding.textQuantity.setText(String.valueOf(sol1));

        binding.cong.setOnClickListener(v -> {

            sol1 = sol1 + 1;

            binding.textQuantity.setText(String.valueOf(sol1));

            each_item = money * sol1;

            sum = each_item + ship;

            binding.textMoney.setText(String.format("%.2f đ", each_item));

            binding.grand.setText(String.format("%2.2f d", sum));

        });

        binding.tru.setOnClickListener(v -> {

            if (sol1 > 0)
            {
                sol1 = sol1 - 1;

                binding.textQuantity.setText(String.valueOf(sol1));

                each_item = money * sol1;

                sum = each_item + ship;

                binding.textMoney.setText(String.format("%.2f đ", each_item));

                binding.grand.setText(String.format("%2.2f d", sum));
            }
        });

    }
    private boolean tests()
    {
        int so_luong = Integer.parseInt(binding.textQuantity.getText().toString());

        if (so_luong <= 0)
        {
            message("Vui lòng chọn số lượng cần mua");
            return false;
        }
        else if (so_luong > quantity)
        {
            message("Số lượng bạn chon không đúng");
            return false;
        }
        else
        {
            return true;
        }
    }
    private void message(String mess)
    {
        Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
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