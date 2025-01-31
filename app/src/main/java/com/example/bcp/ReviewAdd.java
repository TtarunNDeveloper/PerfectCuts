package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;

import java.util.ArrayList;

public class ReviewAdd extends AppCompatActivity {
TextView p_name,p_id,p_desc,p_size,p_price,p_cate;
ViewPager viewPager;
SliderAdapter imageSlider;
NumberPicker quant;
Button btnadd;
private DataBaseHelper dbHelper;
private static final long AUTO_SCROLL_DELAY = 3000;
private Handler autoScrollHandler;
private Runnable autoScrollRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);
        dbHelper=new DataBaseHelper(this);
        viewPager=findViewById(R.id.viewpager);
        quant=findViewById(R.id.quantity);
        btnadd=findViewById(R.id.radd);
        p_name=findViewById(R.id.Name);
        p_id=findViewById(R.id.Id);
        p_desc=findViewById(R.id.Desc);
        p_size=findViewById(R.id.Sizes);
        p_price=findViewById(R.id.Price);
        p_cate=findViewById(R.id.cate);
        Intent intent=getIntent();
        String prod_id=intent.getStringExtra("p_id");
        String prod_name= intent.getStringExtra("p_name");
        String prod_desc=intent.getStringExtra("p_desc");
        String prod_size=intent.getStringExtra("p-sizes");
        String prod_price=intent.getStringExtra("p_price");
        String prod_cate=intent.getStringExtra("p_cate");
        ArrayList<byte[]> prod_imgs=(ArrayList<byte[]>) intent.getSerializableExtra("p_images");
        imageSlider=new SliderAdapter(this,prod_imgs);
        viewPager.setAdapter(imageSlider);
        p_id.setText(prod_id);
        p_name.setText(prod_name);
        p_desc.setText(prod_desc);
        p_size.setText(prod_size);
        p_price.setText(prod_price);
        p_cate.setText(prod_cate);
        quant.setMaxValue(100);
        quant.setMinValue(1);
        autoScrollHandler=new Handler(Looper.getMainLooper());
        startAutoScroll();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToDatabase();
            }
        });
    }
    private void addProductToDatabase(){
        String productName = p_name.getText().toString();
        double price = Double.parseDouble(p_price.getText().toString());
        String description = p_desc.getText().toString();
        String sizes = p_size.getText().toString();
        String category=p_cate.getText().toString();
        String quantity = String.valueOf(quant.getValue());
        ArrayList<byte[]> prod_imgs = (ArrayList<byte[]>) getIntent().getSerializableExtra("p_images");
        if (dbHelper!=null) {
            long productId = dbHelper.insertProductsWithImages(productName, price, description, sizes, category, quantity, prod_imgs);
            if (productId != -1) {
                Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AdminHomePage.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Product was not added", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Error in Database, please try again after some time", Toast.LENGTH_SHORT).show();
        }
    }
    private void startAutoScroll() {
        autoScrollRunnable = () -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = imageSlider.getCount();

            if (currentItem < totalItems - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                viewPager.setCurrentItem(0);
            }

            autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
        };

        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoScroll();
    }

    private void stopAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }
}