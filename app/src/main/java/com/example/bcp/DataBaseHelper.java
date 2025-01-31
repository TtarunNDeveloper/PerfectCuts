package com.example.bcp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_PRODUCTS="products";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_QUANTITY="quantity";
    public DataBaseHelper(@Nullable Context context) {
        super(context, "User.db", null, 3);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("CREATE TABLE USER_DETAILS(USERNAME TEXT UNIQUE,PASSWORD TEXT,EMAILID TEXT,PHONE TEXT PRIMARY KEY,ADDRESS TEXT)");
        db.execSQL("CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_name TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "description TEXT NOT NULL," +
                "sizes TEXT NOT NULL," +
                "category TEXT NOT NULL," +
                "quantity TEXT NOT NULL,"+
                "UNIQUE(id) ON CONFLICT REPLACE" +
                ");");
        db.execSQL("CREATE TABLE product_images (" +
                "image_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_id TEXT NOT NULL," +
                "image_data BLOB NOT NULL," +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USER_DETAILS");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS product_images");
        onCreate(db);
    }
    public long insertProductsWithImages(String productName, double price, String description, String sizes, String category,String quantity, List<byte[]> imageList) {
        SQLiteDatabase db = this.getWritableDatabase();
        long productId = -1;
        ContentValues productValues = new ContentValues();
        productValues.put("product_name", productName);
        productValues.put("price", price);
        productValues.put("description", description);
        productValues.put("sizes", sizes);
        productValues.put("category", category);
        productValues.put("quantity",quantity);
        productId = db.insert("products", null, productValues);
        if (productId != -1) {
            for (byte[] imageData : imageList) {
                ContentValues imageValues = new ContentValues();
                imageValues.put("product_id", productId);
                imageValues.put("image_data", imageData);
                db.insert("product_images", null, imageValues);
            }
        }
        db.close();
        return productId;
    }
    public boolean updateStock(long prodId,int quantity){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("quantity",quantity);
        int rowsAffected= db.update("products",values,"id=?",new String[]{String.valueOf(prodId)});
        db.close();
        return rowsAffected>0;
    }
    public int getProductQuantity(long pId){
        SQLiteDatabase db=this.getReadableDatabase();
        int quantit=-1;
        Cursor cursor=db.query(TABLE_PRODUCTS,new String[]{COLUMN_QUANTITY},COLUMN_ID+"=?",new String[]{String.valueOf(pId)},null,null,null);
        if (cursor!=null) {
            if (cursor.moveToFirst()) {
                int quantityColumnIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
                if (quantityColumnIndex != -1) {
                    quantit = cursor.getInt(quantityColumnIndex);
                } else {
                    Log.e("DBHelper", "Column" + COLUMN_QUANTITY + "not found");
                }
            }
            cursor.close();
        }else {
            Log.e("DBHelper","Cursor is null.");
        }
        return quantit;
    }
    public List<Product> getAllProductWithImages(){
        List<Product> productList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT p.id,p.product_name,p.price,p.category,i.image_data FROM products p LEFT JOIN product_images i ON p.id=i.product_id GROUP BY p.id",null);
        try {
            if (cursor.moveToFirst()) {
                int idIndex= cursor.getColumnIndex("id");
                int nameIndex= cursor.getColumnIndex("product_name");
                int priceIndex= cursor.getColumnIndex("price");
                int imageIndex= cursor.getColumnIndex("image_data");
                int categor=cursor.getColumnIndex("category");
                do {
                    if (idIndex!=-1 && nameIndex!=-1 && priceIndex!=-1 && imageIndex!=-1) {
                        long id = cursor.getLong(idIndex);
                        String pname = cursor.getString(nameIndex);
                        double price = cursor.getDouble(priceIndex);
                        String category=cursor.getString(categor);
                        byte[] imgdata = cursor.getBlob(imageIndex);
                        List<byte[]> imgs=new ArrayList<>();
                        imgs.add(imgdata);
                        Product product = new Product(id, pname, price,category,null,imgs);
                        productList.add(product);
                    }else {
                        throw new RuntimeException("One or more column not found");
                    }
                } while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
            db.close();
        }
        return productList;
    }
    public List<Product> getWomenProducts(){
        List<Product> productList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String query = "SELECT p.id, p.product_name, p.price, p.category, p.description, i.image_data " +
                "FROM products p LEFT JOIN product_images i ON p.id = i.product_id " +
                "WHERE p.category = ? " +
                "GROUP BY p.id";
        Cursor cursor=db.rawQuery(query,new String[]{"Women"});
        try {
            if (cursor.moveToFirst()){
                int idIndex= cursor.getColumnIndex("id");
                int nameIndex= cursor.getColumnIndex("product_name");
                int priceIndex=cursor.getColumnIndex("price");
                int descrIndex= cursor.getColumnIndex("description");
                int imageIndex= cursor.getColumnIndex("image_data");
                int categIndex= cursor.getColumnIndex("category");
                do {
                    if (idIndex!=-1 && nameIndex!=-1 && priceIndex!=-1 && descrIndex!=-1 && imageIndex!=-1 && categIndex!=-1){
                        long id= cursor.getLong(idIndex);
                        String name= cursor.getString(nameIndex);
                        double price= cursor.getDouble(priceIndex);
                        String category= cursor.getString(categIndex);
                        String description= cursor.getString(descrIndex);
                        byte[] imgs= cursor.getBlob(imageIndex);
                        List<byte[]> imgsdata=new ArrayList<>();
                        imgsdata.add(imgs);
                        Product product=new Product(id,name,price,category,description,imgsdata);
                        productList.add(product);
                    }else {
                        throw new RuntimeException("One or more column not found");
                    }
                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
            db.close();
        }
        return productList;
    }
    public List<Product> getMenProducts(){
        List<Product> productList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String query = "SELECT p.id, p.product_name, p.price, p.category, p.description, i.image_data " +
                "FROM products p LEFT JOIN product_images i ON p.id = i.product_id " +
                "WHERE p.category = ? " +
                "GROUP BY p.id";
        Cursor cursor=db.rawQuery(query,new String[]{"Men"});
        try {
            if (cursor.moveToFirst()){
                int idIndex= cursor.getColumnIndex("id");
                int nameIndex= cursor.getColumnIndex("product_name");
                int priceIndex=cursor.getColumnIndex("price");
                int descrIndex= cursor.getColumnIndex("description");
                int imageIndex= cursor.getColumnIndex("image_data");
                int categIndex= cursor.getColumnIndex("category");
                do {
                    if (idIndex!=-1 && nameIndex!=-1 && priceIndex!=-1 && descrIndex!=-1 && imageIndex!=-1 && categIndex!=-1){
                        long id= cursor.getLong(idIndex);
                        String name= cursor.getString(nameIndex);
                        double price= cursor.getDouble(priceIndex);
                        String category= cursor.getString(categIndex);
                        String description= cursor.getString(descrIndex);
                        byte[] imgs= cursor.getBlob(imageIndex);
                        List<byte[]> imgsdata=new ArrayList<>();
                        imgsdata.add(imgs);
                        Product product=new Product(id,name,price,category,description,imgsdata);
                        productList.add(product);
                    }else {
                        throw new RuntimeException("One or more column not found");
                    }
                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
            db.close();
        }
        return productList;
    }
    public boolean insertData(String uname,String pass,String mailid,String phno,String addr){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("USERNAME",uname);
        values.put("PASSWORD",pass);
        values.put("EMAILID",mailid);
        values.put("PHONE",phno);
        values.put("ADDRESS",addr);
        long res=database.insert("USER_DETAILS",null,values);
        if (res==-1){
            return false;
        }else {
            return true;
        }
    }
    public boolean updateData(String uname,String pass,String phno,String addr){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("PASSWORD",pass);
        values.put("PHONE",phno);
        values.put("ADDRESS",addr);
        Cursor cursor=database.rawQuery("SELECT * FROM USER_DETAILS WHERE USERNAME=?",new String[]{uname});
        if (cursor.getCount()>0){
            long res=database.update("USER_DETAILS",values,"USERNAME=?",new String[]{uname});
            if (res==-1){
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }
    public Cursor viewData(){
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("SELECT * FROM USER_DETAILS",null);
        return cursor;
    }
    public Boolean verifyUser(String username,String password){
        SQLiteDatabase database=this.getReadableDatabase();
        String selection="USERNAME=? AND PASSWORD=?";
        String[] selectionArgs={username,password};
        Cursor cursor=database.query("USER_DETAILS",null,selection,selectionArgs,null,null,null,null);
        int count=cursor.getCount();
        cursor.close();
        database.close();
        return count>0;
    }
}
