package com.example.bcp;

import android.graphics.Bitmap;

import java.util.List;

public class Product {
    private long id;
    private String pname;
    private double price;
    private String category;
    private List<byte[]> imgdata;
    private String description;
    public Product(long id, String pname, double price, String category,String description, List<byte[]> imgdata){
        this.id=id;
        this.pname=pname;
        this.price=price;
        this.imgdata=imgdata;
        this.category= category;
        this.description=description;
    }
    public long getId() {
        return id;
    }

    public String getPname() {
        return pname;
    }

    public double getPrice() {
        return price;
    }

    public List<byte[]> getImgdata() {
        return imgdata;
    }
    public  String getCategory(){return category;}
    public String getDescription(){return description;}
}
