package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenProducts extends AppCompatActivity {
    DataBaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men_products);
        dbhelper = new DataBaseHelper(this);

        List<Product> womenProducts = dbhelper.getMenProducts();

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
                Intent intent = new Intent(MenProducts.this, DisplayProduct.class);
                int currpos = imgs.getCurrentItem();
                byte[] slctdimg = adapter.getImageAtPosition(currpos);
                intent.putExtra("productName", product.getPname());
                intent.putExtra("ProductPrice", String.valueOf(product.getPrice()));
                intent.putExtra("ProductDesc", product.getDescription());
                intent.putExtra("ProductId", String.valueOf(product.getId()));
                intent.putExtra("ProductImgs", (Serializable) imgslist);
                startActivity(intent);
            }
        });

        LinearLayout productContainer = findViewById(R.id.prodsContainer);
        productContainer.addView(productlayout);
    }

    private List<byte[]> convertByteArrayToBitmapList(byte[] imgData) {
        List<byte[]> imgList = new ArrayList<>();
        imgList.add(imgData);
        return imgList;
    }
}
