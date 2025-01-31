package com.example.bcp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    private LinearLayout productsContainer;
    private EditText addressEditText;
    private EditText contactNumberEditText;
    private Button orderNowButton;

    private double orderPrice = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        productsContainer = findViewById(R.id.productsContainer);
        addressEditText = findViewById(R.id.addressEditText);
        contactNumberEditText = findViewById(R.id.contactNumberEditText);
        orderNowButton = findViewById(R.id.orderNowButton);
        Intent intent = getIntent();
        if (intent != null) {
            byte[] prodimg=intent.getByteArrayExtra("productImage");
            String productName = intent.getStringExtra("namethere");
            String productPrice = intent.getStringExtra("pricethere");
            String productSize = intent.getStringExtra("sizethere");
            addProductItem(productName, productPrice, productSize,prodimg);
        }
        orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userhomepage=new Intent(CartActivity.this, UserHomePage.class);
                startActivity(userhomepage);
                Intent orderdetails=new Intent(CartActivity.this, OrderDetails.class);
                orderdetails.putExtra("prodname",intent.getStringExtra("namethere"));
                orderdetails.putExtra("sizeprodu",intent.getStringExtra("sizethere"));
                orderdetails.putExtra("produimg",intent.getByteArrayExtra("productImage"));
                Toast.makeText(CartActivity.this, "Order Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProductItem(String productName, String productPrice, String productSize,byte[] productImage) {
        View productItemLayout = getLayoutInflater().inflate(R.layout.item_product_summary, null);

        TextView productNameTextView = productItemLayout.findViewById(R.id.productName);
        TextView productPriceTextView = productItemLayout.findViewById(R.id.productPrice);
        TextView productSizeTextView = productItemLayout.findViewById(R.id.productSize);
        NumberPicker quantityPicker = productItemLayout.findViewById(R.id.quantityPicker);
        ImageView productImageView = productItemLayout.findViewById(R.id.productImage);
        Bitmap bitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
        productImageView.setImageBitmap(bitmap);
        productNameTextView.setText(productName);
        productPriceTextView.setText(productPrice);
        productSizeTextView.setText(productSize);
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(10);

        double productPriceDouble = Double.parseDouble(productPrice.replace(getString(R.string.rs_symbol), ""));
        orderPrice += productPriceDouble * quantityPicker.getValue();
        Intent orderdetails=new Intent(CartActivity.this, OrderDetails.class);
        orderdetails.putExtra("quantity",quantityPicker.getValue());
        updateOrderSummary();

        productsContainer.addView(productItemLayout);
    }

    private void updateOrderSummary() {
        double deliveryCharges = (orderPrice < 500) ? 70.0 : 0.0;

        double totalOrderPrice = orderPrice + deliveryCharges;
        Intent orderdetails=new Intent(CartActivity.this, OrderDetails.class);
        orderdetails.putExtra("totalprice",totalOrderPrice);
        TextView orderPriceTextView = findViewById(R.id.orderPrice);
        TextView taxTextView = findViewById(R.id.charges);
        TextView totalAmountTextView = findViewById(R.id.totalprice);

        orderPriceTextView.setText(getString(R.string.rs_symbol) + String.format("%.2f", orderPrice));
        taxTextView.setText(getString(R.string.rs_symbol) + String.format("%.2f", deliveryCharges));
        totalAmountTextView.setText(getString(R.string.rs_symbol) + String.format("%.2f", totalOrderPrice));
    }
}
