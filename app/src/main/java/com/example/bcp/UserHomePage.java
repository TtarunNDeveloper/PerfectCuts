package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class UserHomePage extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton menu;
    CardView men,women,custom;
    RelativeLayout newprods;
    LinearLayout home,products,orders,help,logout,profile;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        sessionManager=new SessionManager(getApplicationContext());
        if (!sessionManager.isLoggedIn()){
            Intent intent=new Intent(UserHomePage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menulogo);
        home=findViewById(R.id.home);
        products=findViewById(R.id.prod);
        orders=findViewById(R.id.orders);
        help=findViewById(R.id.help);
        logout=findViewById(R.id.logout);
        profile=findViewById(R.id.profile);
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
                Intent intent=new Intent(UserHomePage.this,WomenProducts.class);
                startActivity(intent);
            }
        });
        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserHomePage.this,MenProducts.class);
                startActivity(intent);
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserHomePage.this,CustomProducts.class);
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
            public void onClick(View view) {
                recreate();
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomePage.this,NewProducts.class));
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomePage.this, OrderDetails.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomePage.this, Profile.class));
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomePage.this,Help.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setLogin(false);
                Toast.makeText(UserHomePage.this, "LogOut Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserHomePage.this, HomePage.class));
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