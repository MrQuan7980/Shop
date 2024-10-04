package com.example.mysql.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.mysql.R;
import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityOrderBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Order extends AppCompatActivity {
    private ActivityOrderBinding binding;
    private Preference preference;
    private int sol1 = 0;
    private String id_product, title_product, image_product, introduction_product, genre_product, dateTime;
    private String shopId, shopImage, shopName, phoneShop, cityShop;
    private double money;
    private int quantity, soldShop;
    private double sum = 0.0, each_item = 0.0, ship = 3.0;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(getApplicationContext());
        database = FirebaseFirestore.getInstance();

        loadingQuantity();
        event_hading();
        loading_information();
    }
    private void event_hading()
    {
        binding.btnButton.setOnClickListener(v -> {
            if (tests())
            {
                order();
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
        dateTime = dateFormat.format(time);

        binding.dateProduct.setText(dateTime);

        binding.houseNumber.setText(preference.getString(Constant.KEY_HOUSE_NUMBER));

        binding.district.setText(preference.getString(Constant.KEY_DISTRICT));

        binding.city.setText(preference.getString(Constant.KEY_CITY));
    }
    private void order()
    {
        Progessbar(true);

        HashMap<String, Object> order = new HashMap<>();

        order.put(Constant.KEY_PRODUCT_ID, id_product);
        order.put(Constant.KEY_USER_SHOP_ID, shopId);
        order.put(Constant.KEY_PRODUCT_TITLE, title_product);
        order.put(Constant.KEY_PRODUCT_IMAGE, image_product);
        order.put(Constant.KEY_USER_ID, preference.getString(Constant.KEY_USER_ID));
        order.put(Constant.KEY_FULL_NAME, preference.getString(Constant.KEY_FULL_NAME));
        order.put(Constant.KEY_PHONE, preference.getString(Constant.KEY_PHONE));
        order.put(Constant.KEY_EMAIL, preference.getString(Constant.KEY_EMAIL));
        order.put(Constant.KEY_TIME, dateTime);
        order.put(Constant.KEY_HOUSE_NUMBER, preference.getString(Constant.KEY_HOUSE_NUMBER));
        order.put(Constant.KEY_DISTRICT, preference.getString(Constant.KEY_DISTRICT));
        order.put(Constant.KEY_CITY, preference.getString(Constant.KEY_CITY));
        order.put(Constant.KEY_PURCHASE_QUANTITY, sol1);
        order.put(Constant.KEY_ORDER, "Đã đặt hàng");

        database.collection(Constant.KEY_ORDER_SUCCESSFUL)
                .add(order)
                .addOnSuccessListener(command -> {
                    Progessbar(false);

                    updateSoluong(sol1);

                    Intent intent = new Intent(getApplicationContext(), Order_successful.class);
                    intent.putExtra("image", image_product);
                    intent.putExtra("title", image_product);
                    intent.putExtra("money", money);
                    intent.putExtra("quantity", sol1);
                    intent.putExtra("dateTime", dateTime);

                    startActivity(intent);

                }).addOnFailureListener(command -> {
                    Progessbar(false);
                    message("Đặt đơn thất bại");
                });

    }
    private void updateSoluong(int soluongmua)
    {
        database.collection(Constant.KEY_TABLE_PRODUCT)
                .document(id_product)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists())
                    {
                        int so_luong_ban = documentSnapshot.getLong(Constant.KEY_SOLD).intValue();
                        int so_luong_hien_tai = documentSnapshot.getLong(Constant.KEY_PRODUCT_QUANTITY).intValue();

                        int daban = so_luong_ban + soluongmua;
                        int conlai = so_luong_hien_tai - soluongmua;


                        HashMap<String, Object> update = new HashMap<>();

                        update.put(Constant.KEY_SOLD, daban);
                        update.put(Constant.KEY_PRODUCT_QUANTITY, conlai);

                        database.collection(Constant.KEY_TABLE_PRODUCT)
                                .document(id_product)
                                .update(update)
                                .addOnSuccessListener(aVoid -> {
                                    message("Update thành công");
                                });
                    }
                    else
                    {
                        message("Sai");
                    }
                });
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
    private void Progessbar(boolean loaidng)
    {
        if (loaidng)
        {
            binding.progesbar.setVisibility(View.VISIBLE);
            binding.btnButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progesbar.setVisibility(View.INVISIBLE);
            binding.btnButton.setVisibility(View.VISIBLE);
        }
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