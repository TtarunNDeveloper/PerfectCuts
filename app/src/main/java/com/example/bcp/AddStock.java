package com.example.bcp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.List;

public class AddStock extends AppCompatActivity {
    private DrawerLayout drawer;
    private ImageButton menu;
    private DataBaseHelper dbHelper;
    private LinearLayout home, newprod, stockprod, orders, logout;
    private HandlerThread handlerThread;
    private Handler backgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        drawer = findViewById(R.id.drawer);
        menu = findViewById(R.id.menulogo);
        home = findViewById(R.id.home);
        newprod = findViewById(R.id.addProd);
        stockprod = findViewById(R.id.add2Stock);
        orders = findViewById(R.id.ordrData);
        logout = findViewById(R.id.logout);

        handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawer);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStock.this, AdminHomePage.class);
                startActivity(intent);
            }
        });

        newprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStock.this, NewProductAdmin.class);
                startActivity(intent);
            }
        });

        stockprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStock.this, OrderDetails.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStock.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Log.d("AddStock", "Starting Database operation");

        dbHelper = new DataBaseHelper(this);
        List<Product> productList = dbHelper.getAllProductWithImages();
        for (Product product : productList) {
            addProductLayout(product);
        }

        Log.d("AddStock", "Database operation completed");
    }

    private void addProductLayout(Product product) {
        View productlayout = getLayoutInflater().inflate(R.layout.item_product_layout, null);
        List<byte[]> imgData = product.getImgdata();
        TextView pnameText = productlayout.findViewById(R.id.pname);
        TextView pId = productlayout.findViewById(R.id.pId);
        TextView pamt = productlayout.findViewById(R.id.pamt);
        TextView pactegory = productlayout.findViewById(R.id.pcateg);
        ImageView pimg = productlayout.findViewById(R.id.pimage);
        NumberPicker newquant = productlayout.findViewById(R.id.pstock);
        Button stockupdater = productlayout.findViewById(R.id.addstock);
        newquant.setMinValue(1);
        newquant.setMaxValue(10);

        pnameText.setText(product.getPname());
        pId.setText(String.valueOf(product.getId()));
        pamt.setText(String.valueOf(product.getPrice()));
        pactegory.setText(String.valueOf(product.getCategory()));

        if (imgData != null && !imgData.isEmpty()) {
            byte[] firstImg = imgData.get(0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(firstImg, 0, firstImg.length);
            if (bitmap != null) {
                pimg.setImageBitmap(bitmap);
            } else {
                pimg.setImageResource(R.drawable.logoclean);
            }
        } else {
            pimg.setImageResource(R.drawable.women);
        }

        stockupdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedQuantity = newquant.getValue();
                long produId = product.getId();

                backgroundHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateStockQuantityInBackground(produId, selectedQuantity);
                    }
                });
            }
        });

        LinearLayout productContainer = findViewById(R.id.productContainer);
        productContainer.addView(productlayout);
    }

    private void updateStockQuantityInBackground(long produId, int slctedquant) {
        int currquant = dbHelper.getProductQuantity(produId);
        int newquant = currquant + slctedquant;
        boolean success = dbHelper.updateStock(produId, newquant);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    Toast.makeText(AddStock.this, "Stock Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddStock.this, "Couldn't add stock", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawer);
    }
    @Override
    protected void onDestroy() {
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }

        super.onDestroy();
    }
}
