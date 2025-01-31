package com.example.bcp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
public class OrderDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Intent intent = getIntent();
        String productName = intent.getStringExtra("prodname");
        String productSize = intent.getStringExtra("sizeprodu");
        byte[] productImage = intent.getByteArrayExtra("produimg");
        int quantity = intent.getIntExtra("quantity", 1);
        double totalOrderPrice = intent.getDoubleExtra("totalprice", 0.0);
        ImageView productImageView = findViewById(R.id.productImageView);
        TextView productNameTextView = findViewById(R.id.productNameTextView);
        TextView productSizeTextView = findViewById(R.id.productSizeTextView);
        TextView quantityTextView = findViewById(R.id.quantityTextView);
        TextView totalOrderPriceTextView = findViewById(R.id.totalOrderPriceTextView);
        Bitmap bitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
        productImageView.setImageBitmap(bitmap);
        productNameTextView.setText(productName);
        productSizeTextView.setText("Size: " + productSize);
        quantityTextView.setText("Quantity: " + quantity);
        totalOrderPriceTextView.setText(getString(R.string.rs_symbol) + String.format("%.2f", totalOrderPrice));
    }
}
