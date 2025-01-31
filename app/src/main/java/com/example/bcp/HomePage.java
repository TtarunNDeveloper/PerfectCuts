package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton menu;

    CardView men,women,custom;
    RelativeLayout newprods;
    LinearLayout login,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menulogo);
        login=findViewById(R.id.login);
        exit=findViewById(R.id.exit);
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
                Intent intent=new Intent(HomePage.this,WomenProducts.class);
                startActivity(intent);
            }
        });
        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this,MenProducts.class);
                startActivity(intent);
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this,CustomProducts.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, MainActivity.class));
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
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
        closeDrawer(drawerLayout);
    }


}