package com.example.bcp;

import static com.example.bcp.AdminHomePage.openDrawer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewProductAdmin extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton menu;
    LinearLayout home,logout,newprod,stockprod,orders;
    Spinner spinner;
    TextView idView;
    EditText p_name,p_desc,p_price,p_size;
    ArrayList<CheckBox> selectedSizesChck=new ArrayList<>();
    CheckBox p_sizeB;
    ImageView img1,img2,img3,img4,img5;
    Button addImg,reView,clrBtn;
    ArrayList<Bitmap> selectedImgs;
    ArrayList<ImageView> imgViews;
    SharedPreferences sharedPreferences;
    private  int p_Counter=1;
    private static final int REQUEST_CODE_IMAGE_PICKER = 1;
    private static final String CURRENT_PRODUCT_ID="current_product_id";
    private static final String LOCKED_PRODUCT_ID="locked_product_id";
    private static final int MAX_IMGS=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_admin);
        sharedPreferences=getSharedPreferences("prodID_preferences",MODE_PRIVATE);
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menulogo);
        home=findViewById(R.id.home);
        newprod=findViewById(R.id.addProd);
        stockprod=findViewById(R.id.add2Stock);
        orders=findViewById(R.id.ordrData);
        logout=findViewById(R.id.logout);
        spinner=findViewById(R.id.category_spinner);
        idView=findViewById(R.id.product_id);
        p_name=findViewById(R.id.product_name);
        p_desc=findViewById(R.id.product_description);
        p_price=findViewById(R.id.product_price);
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
        img1=findViewById(R.id.Iv01);
        img2=findViewById(R.id.Iv02);
        img3=findViewById(R.id.Iv03);
        img4=findViewById(R.id.Iv04);
        img5=findViewById(R.id.Iv05);
        addImg=findViewById(R.id.add_images_button);
        reView=findViewById(R.id.review_button);
        clrBtn=findViewById(R.id.clear_button);
        selectedImgs=new ArrayList<>();
        ArrayAdapter<CharSequence> categoryAdapter=ArrayAdapter.createFromResource(this,R.array.category_options, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePID(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(NewProductAdmin.this, "Select category", Toast.LENGTH_SHORT).show();
            }
        });
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
        imgViews=new ArrayList<>();
        imgViews.add(img1);
        imgViews.add(img2);
        imgViews.add(img3);
        imgViews.add(img4);
        imgViews.add(img5);
        for (ImageView imageView:imgViews){
            imageView.setVisibility(View.GONE);
        }
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImgs.size()<MAX_IMGS) {
                    openImgPicker();
                }else {
                    Toast.makeText(NewProductAdmin.this, "Cannot add more than 5 images", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateProdDetails()) {
                    Intent intent = new Intent(NewProductAdmin.this, ReviewAdd.class);
                    StringBuilder selectedSize=new StringBuilder();
                    for (CheckBox checkBox:selectedSizesChck){
                        if (checkBox.isChecked()){
                            selectedSize.append(checkBox.getText()).append(", ");
                        }
                    }
                    String currentpId=idView.getText().toString();
                    if (isProductIdLocked(currentpId)){
                        Toast.makeText(NewProductAdmin.this, "ProductId is used", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    lockedProductId(currentpId);
                    if (p_sizeB.isChecked()){
                        selectedSize.append(p_size.getText().toString());
                    }
                    if (selectedSize.length()>0){
                        selectedSize.delete(selectedSize.length()-2,selectedSize.length());
                    }
                    ArrayList<byte[]> imgByteList=new ArrayList<>();
                    for (Bitmap img:selectedImgs){
                        byte[] imgBytes = convertBitmaptoByte(img);
                        imgByteList.add(imgBytes);
                    }
                    String cate=spinner.getSelectedItem().toString();
                    intent.putExtra("p_id", idView.getText().toString());
                    intent.putExtra("p_name", p_name.getText().toString());
                    intent.putExtra("p_desc", p_desc.getText().toString());
                    intent.putExtra("p_price", p_price.getText().toString());
                    intent.putExtra("p_sizes",selectedSize.toString());
                    intent.putExtra("p_images",imgByteList);
                    intent.putExtra("p_cate",cate);
                    startActivity(intent);
                }
            }
        });
        clrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_name.setText("");
                p_desc.setText("");
                p_price.setText("");
                p_size.setText("");
                p_size.setVisibility(View.GONE);
                for (CheckBox checkBox:selectedSizesChck){
                    checkBox.setChecked(false);
                }
                selectedImgs.clear();
                displaySelectedImgs();
            }
        });
        loadCurrentId();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewProductAdmin.this,AdminHomePage.class);
                startActivity(intent);
            }
        });
        newprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               recreate();
            }
        });
        stockprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewProductAdmin.this,AddStock.class);
                startActivity(intent);
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewProductAdmin.this,OrderDetails.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewProductAdmin.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean isProductIdLocked(String pId) {
        Set<String> lockedProdId = sharedPreferences.getStringSet(LOCKED_PRODUCT_ID, new HashSet<>());

        if (lockedProdId.contains(pId)) {
            int counter = sharedPreferences.getInt(spinner.getSelectedItem().toString(), 0);
            while (lockedProdId.contains(pId)) {
                counter++;
                pId = generateProductId(counter);
            }
        }

        return false;
    }
    private String generateProductId(int counter) {
        String selectedCateg = spinner.getSelectedItem().toString();
        String formattedCounter = String.format("%03d", counter);
        String catInitial = selectedCateg.substring(0, 1).toUpperCase();
        return catInitial + formattedCounter;
    }
    private void lockedProductId(String pId){
        Set<String> lockedProdId=sharedPreferences.getStringSet(LOCKED_PRODUCT_ID,new HashSet<>());
        lockedProdId.add(pId);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putStringSet(LOCKED_PRODUCT_ID,lockedProdId);
        editor.apply();
    }
    private byte[] convertBitmaptoByte(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }
    private boolean validateProdDetails(){
        String name=p_name.getText().toString();
        if (name.isEmpty()){
            Toast.makeText(this, "Product name is missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        String descr=p_desc.getText().toString();
        if (descr.isEmpty() || descr.split("\\s+").length<5){
            Toast.makeText(this, "Enter product descrption with atleast 5 words", Toast.LENGTH_SHORT).show();
            return false;
        }
        String price=p_price.getText().toString();
        if(price.isEmpty()){
            Toast.makeText(this, "Price is missing", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedSizesChck.isEmpty()){
            Toast.makeText(this, "Select at least one size", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedImgs.size()<2){
            Toast.makeText(this, "Select more than 1 image to continue", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedImgs.size()>MAX_IMGS){
            Toast.makeText(this, "Upload only 5 images", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void openImgPicker(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selected Images"),REQUEST_CODE_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE_PICKER && resultCode==RESULT_OK && data!= null){
            if (data.getClipData()!=null){
                int count=data.getClipData().getItemCount();
                if (count>MAX_IMGS){
                    Toast.makeText(this, "Cannot add more than 5 images", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i=0;i<count;i++){
                    Uri imgUri=data.getClipData().getItemAt(i).getUri();
                    Bitmap img= convertUritoBitmap(imgUri);
                    selectedImgs.add(img);
                }
            } else if (data.getData()!=null) {
                Uri imgUri=data.getData();
                Bitmap img=convertUritoBitmap(imgUri);
                selectedImgs.add(img);
            }
            displaySelectedImgs();
        }
    }
    private Bitmap convertUritoBitmap(Uri uri){
        try {
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
    private void displaySelectedImgs() {
        int numSelectedImgs = selectedImgs.size();

        // Iterate through all image views
        for (int i = 0; i < imgViews.size(); i++) {
            ImageView imageView = imgViews.get(i);

            // If there is a corresponding selected image, set it and make the ImageView visible
            if (i < numSelectedImgs) {
                imageView.setImageBitmap(selectedImgs.get(i));
                imageView.setVisibility(View.VISIBLE);
            } else {
                // If there is no corresponding image, hide the ImageView
                imageView.setVisibility(View.GONE);
            }
        }
    }
    private void loadCurrentId(){
        String currentID=sharedPreferences.getString(CURRENT_PRODUCT_ID,"");
        if(!currentID.isEmpty()){
            idView.setText(currentID);
        }
    }
    private void updatePID(int position) {
        String selectedCateg = spinner.getSelectedItem().toString();
        int currentCounter = sharedPreferences.getInt(selectedCateg, 0);
        currentCounter++;
        saveCurrentID(selectedCateg, currentCounter);
        String formattedCounter = String.format("%03d", currentCounter);
        String catInitial = selectedCateg.substring(0, 1).toUpperCase();
        idView.setText(catInitial + formattedCounter);
    }

    private void saveCurrentID(String category, int counter) {
        String formattedCounter = String.format("%03d", counter);
        String catInitial = category.substring(0, 1).toUpperCase();
        String currentId = catInitial + formattedCounter;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_PRODUCT_ID, currentId);
        editor.putInt(category, counter); // Save the updated counter for the category
        editor.apply();
    }
}