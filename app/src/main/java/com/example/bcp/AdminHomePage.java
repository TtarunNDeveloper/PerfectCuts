package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class AdminHomePage extends AppCompatActivity {
DrawerLayout drawerLayout;
ImageButton menu;
CardView men,women,custom;
RelativeLayout newprods;
LinearLayout home,logout,newprod,stockprod,orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menulogo);
        home=findViewById(R.id.home);
        newprod=findViewById(R.id.addProd);
        stockprod=findViewById(R.id.add2Stock);
        orders=findViewById(R.id.ordrData);
        logout=findViewById(R.id.logout);
        men=findViewById(R.id.shopMen);
        women=findViewById(R.id.shopWomen);
        custom=findViewById(R.id.shopCustom);
        newprods=findViewById(R.id.newprod);
        ImageSlider slider=findViewById(R.id.imgsld);
        ArrayList<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.logoclean, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.default1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.women, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.men, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.custom, ScaleTypes.FIT));
        slider.setImageList(slideModels,ScaleTypes.FIT);
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomePage.this,WomenProducts.class);
                startActivity(intent);
            }
        });
        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomePage.this,MenProducts.class);
                startActivity(intent);
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomePage.this,CustomProducts.class);
                startActivity(intent);
            }
        });
        newprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomePage.this,NewProducts.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        newprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomePage.this,NewProductAdmin.class);
                startActivity(intent);
            }
        });
        stockprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomePage.this,AddStock.class);
                startActivity(intent);
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomePage.this,OrderDetails.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUserSession();
                Intent intent=new Intent(AdminHomePage.this, MainActivity.class);
                startActivity(intent);
                finish();
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
    private void clearUserSession(){
        SharedPreferences sharedPreferences=getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }


}