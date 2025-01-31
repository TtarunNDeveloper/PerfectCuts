package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DisplayProduct extends AppCompatActivity {
TextView pname,pprice,pdesc;
EditText p_size;
Button cartadd;
ImageView cart,bck;
ViewPager pager;

ArrayList<CheckBox> selectedSizesChck=new ArrayList<>();
CheckBox p_sizeB;
    private static final long AUTO_SCROLL_DELAY = 3000;
    private int currentPage = 0;
    private ImagePagerAdapter adapter;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);
        pname=findViewById(R.id.intendedname);
        pprice=findViewById(R.id.intendedprice);
        pdesc=findViewById(R.id.intendeddesc);
        cartadd=findViewById(R.id.add2cart);
        cart=findViewById(R.id.shopcart);
        bck=findViewById(R.id.bckbtn);
        pager=findViewById(R.id.sproductimgs);
        CheckBox cS=findViewById(R.id.sizeS);
        CheckBox cM=findViewById(R.id.sizeM);
        CheckBox cL=findViewById(R.id.sizeL);
        CheckBox cXL=findViewById(R.id.sizeXL);
        selectedSizesChck.add(cS);
        selectedSizesChck.add(cM);
        selectedSizesChck.add(cL);
        selectedSizesChck.add(cXL);
        p_size=findViewById(R.id.custom_size);
        p_sizeB=findViewById(R.id.radio_custom);
        Intent intent=getIntent();
        String name= intent.getStringExtra("productName");
        String price= intent.getStringExtra("ProductPrice");
        String description=intent.getStringExtra("ProductDesc");
        int productId = intent.getIntExtra("ProductId", -1);
        List<byte[]> productImgs = (List<byte[]>) intent.getSerializableExtra("ProductImgs");
        Log.d("ImageListSize", "Size: " + (productImgs != null ? productImgs.size() : 0));
        adapter = new ImagePagerAdapter(this, productImgs);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        startAutoScroll();
        pname.setText(name);
        pprice.setText(price);
        pdesc.setText(description);
        p_sizeB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    p_size.setVisibility(View.VISIBLE);
                }else {
                    p_size.setVisibility(View.GONE);
                }
            }
        });
        cartadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(DisplayProduct.this,CartActivity.class);
                StringBuilder selectedSize=new StringBuilder();
                for (CheckBox checkBox:selectedSizesChck){
                    if (checkBox.isChecked()){
                        selectedSize.append(checkBox.getText()).append(", ");
                    }
                }
                if (p_sizeB.isChecked()){
                    selectedSize.append(p_size.getText().toString());
                }
                if (selectedSize.length()>0){
                    selectedSize.delete(selectedSize.length()-2,selectedSize.length());
                }
                if (productImgs != null && productImgs.size() > 0) {
                    byte[] firstImg = productImgs.get(0);
                    intent1.putExtra("productImage", firstImg);
                }
                String name= intent.getStringExtra("productName");
                String price= intent.getStringExtra("ProductPrice");
                String size=selectedSize.toString();
                intent1.putExtra("namethere",name);
                intent1.putExtra("sizethere",size);
                intent1.putExtra("pricethere",price);
                cartadd.setText("Go To Cart");
                cartadd.setEnabled(false);
                startActivity(intent1);
            }
        });
    }
    private void startAutoScroll() {
        Log.d("AutoScroll","startAutoScroll called");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pager.getAdapter() != null) {
                    int nextPage = (currentPage + 1) % pager.getAdapter().getCount();
                    pager.setCurrentItem(nextPage, true);
                    currentPage = nextPage;
                    handler.postDelayed(this, AUTO_SCROLL_DELAY);
                }
            }
        }, AUTO_SCROLL_DELAY);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
