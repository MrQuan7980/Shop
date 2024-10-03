package com.example.mysql.shop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.mysql.database.Constant;
import com.example.mysql.database.Preference;
import com.example.mysql.databinding.ActivityEditProductBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditProduct extends AppCompatActivity {
    private ActivityEditProductBinding binding;
    private String id, title, introduction, genre;
    private double money;
    private int quantity;
    private String image;
    private Preference preference;
    private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preference(this);
        database = FirebaseFirestore.getInstance();

        getLoading_item();
        event_hading();
        loading();
    }
    private void event_hading()
    {
        binding.btnExit.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Shop.class));
            finish();
        });

        binding.imageShop.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.btnAdd.setOnClickListener(v -> {
            if (test())
            {
                Product();
            }
        });
    }
    private void loading()
    {
        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");
        introduction = intent.getStringExtra("introduction");
        genre = intent.getStringExtra("genre");
        money = intent.getDoubleExtra("money", 0.0);
        quantity = intent.getIntExtra("quantity", 0);

        binding.textTitle.setText(title);
        binding.textQuantity.setText(Integer.toString(quantity));
        binding.textIntroduction.setText(introduction);
        binding.textGenre.setText(genre);
        binding.textMoney.setText(String.format(Locale.getDefault(), "%.2f", money));
        binding.imageShop.setImageBitmap(getImage(image));

    }

    private void Product() {
        HashMap<String, Object> product = new HashMap<>();

        loading(true);

        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String TimeString = dateFormat.format(time);


        product.put(Constant.KEY_PRODUCT_TITLE, binding.textTitle.getText().toString());
        product.put(Constant.KEY_PRODUCT_GENRE, binding.textGenre.getText().toString());
        product.put(Constant.KEY_PRODUCT_QUANTITY, Integer.parseInt(binding.textQuantity.getText().toString()));
        product.put(Constant.KEY_PRODUCT_IMAGE, image);
        product.put(Constant.KEY_PRODUCT_INTRODUCTION, binding.textIntroduction.getText().toString());
        product.put(Constant.KEY_PRODUCT_MONEY, Double.parseDouble(binding.textMoney.getText().toString()));
        product.put(Constant.KEY_TIME_PRODUCT, TimeString);

        database.collection(Constant.KEY_TABLE_PRODUCT)
                .document(id)
                .update(product)
                .addOnSuccessListener(command -> {
                    loading(false);

                    startActivity(new Intent(getApplicationContext(), Shop.class));
                    finish();

                    message("Sửa thành công");
                })
                .addOnFailureListener(command -> {
                    loading(false);

                    message("Sửa thất bại");
                });
    }

    private void getLoading_item()
    {
        String[] genres = new String[]{"Sofas", "Chairs", "Tables", "Kitchen", "Doors"};

        binding.textGenre.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
            builder.setTitle("Thể loại");

            builder.setItems(genres, (dialog, which) -> {
                genre = genres[which];
                binding.textGenre.setText(genre);
            });

            builder.show();
        });
    }
    private boolean test()
    {
        if (image == null)
        {
            message("Vui lòng teeem vào ảnh");
            return false;
        }
        else if (binding.textTitle.getText().toString().isEmpty())
        {
            message("please add a title");
            return false;
        }
        else if (binding.textQuantity.getText().toString().isEmpty() || Integer.parseInt(binding.textQuantity.getText().toString()) < 0)
        {
            message("please add a quantity");
            return false;
        }
        else if (binding.textIntroduction.getText().toString().isEmpty())
        {
            message("please add a introduction");
            return false;
        }
        else if (binding.textMoney.getText().toString().isEmpty() || Double.parseDouble(binding.textMoney.getText().toString()) < 0)
        {
            message("please add a money");
            return false;
        }
        else
        {
            return true;
        }
    }
    private void loading (boolean loading)
    {
        if (loading)
        {
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.btnAdd.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.progressbar.setVisibility(View.INVISIBLE);
            binding.btnAdd.setVisibility(View.VISIBLE);
        }
    }

    private void message(String mess)
    {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }
    public String codeImage(Bitmap bitmap)
    {
        int rong = 150;
        int dai = bitmap.getHeight() * rong / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, rong, dai,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK)
                {
                    if (result.getData() != null)
                    {
                        Uri imageUri = result.getData().getData();

                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageShop.setImageBitmap(bitmap);
                            image = codeImage(bitmap);
                        }catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private Bitmap getImage(String codeImage)
    {
        byte[] bytes = Base64.decode(codeImage, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        options.inSampleSize = calculateInSampleSize(options, 1920, 1080);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float aspectRatio = (float) width / height;
        int scaledWidth = 1920;
        int scaledHeight = 1080;
        if (width > height) {
            scaledHeight = (int) (scaledWidth / aspectRatio);
        } else {
            scaledWidth = (int) (scaledHeight * aspectRatio);
        }

        // Scale the bitmap to the calculated dimensions
        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
