package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WomenProducts extends AppCompatActivity {
    DataBaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women_products);
        dbhelper = new DataBaseHelper(this);

        List<Product> womenProducts = dbhelper.getWomenProducts();

        for (Product product : womenProducts) {
            addProductLayout(product);
        }
    }

    private void addProductLayout(Product product) {
        View productlayout = getLayoutInflater().inflate(R.layout.prodsdisplay, null);
        TextView pnameText = productlayout.findViewById(R.id.namehere);
        TextView priceText = productlayout.findViewById(R.id.pricehere);
        ViewPager imgs = productlayout.findViewById(R.id.productimgs);

        List<byte[]> imgslist = product.getImgdata();

        ImagePagerAdapter adapter = new ImagePagerAdapter(this, imgslist);
        imgs.setAdapter(adapter);

        pnameText.setText(product.getPname());
        priceText.setText(String.valueOf(product.getPrice()));

        productlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WomenProducts.this, DisplayProduct.class);
                int currpos = imgs.getCurrentItem();
                byte[] slctdimgs = adapter.getImageAtPosition(currpos);
                intent.putExtra("productName", product.getPname());
                intent.putExtra("ProductPrice", String.valueOf(product.getPrice()));
                intent.putExtra("ProductDesc", product.getDescription());
                intent.putExtra("ProductId", String.valueOf(product.getId()));
                intent.putExtra("ProductImgs", (ArrayList<byte[]>)imgslist);
                Log.d("ImageListSize", "Size: " + (imgslist != null ? imgslist.size() : 0));
                startActivity(intent);
            }
        });

        LinearLayout productContainer = findViewById(R.id.prodsContainer);
        productContainer.addView(productlayout);
    }
}