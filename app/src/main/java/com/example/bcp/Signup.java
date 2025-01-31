package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
EditText uname,pass,verify,email,phone,addres;
Button signup,clr,bck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        uname=findViewById(R.id.uname);
        pass=findViewById(R.id.pass);
        verify=findViewById(R.id.veriPass);
        email=findViewById(R.id.mailId);
        phone=findViewById(R.id.phoNo);
        addres=findViewById(R.id.addRess);
        signup=findViewById(R.id.signUp);
        clr=findViewById(R.id.cancl);
        bck=findViewById(R.id.backBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=uname.getText().toString();
                String emailid=email.getText().toString();
                String phone_number=phone.getText().toString();
                String address=addres.getText().toString();
                String password=pass.getText().toString();
                String verify_password=verify.getText().toString();
                if(password.isEmpty() && verify_password.isEmpty()) {
                    Toast.makeText(Signup.this, "Please Enter the password", Toast.LENGTH_SHORT).show();
                    pass.setError("This field is required");
                    verify.setError("This field is required");
                }else {
                    if (isValidPassword(password)) {
                        if (password.equals(verify_password)) {
                            Toast.makeText(Signup.this, "Your Password Is Verified", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Signup.this, "Verify Password Is Wrong", Toast.LENGTH_SHORT).show();
                            verify.setText("");
                        }
                    } else {
                        pass.setError("Password must contain 1 uppercase, 1 special character, 2 numbers, and be 8 characters long");
                    }
                }
                EditText[] editTexts={uname,email,phone,addres};
                boolean alltexts=true;
                for (EditText editText:editTexts){
                    if (editText.getText().toString().isEmpty())
                    {
                        alltexts=false;
                        break;
                    }
                }
                if (alltexts)
                {
                    DataBaseHelper dataBaseHelper=new DataBaseHelper(Signup.this);
                    boolean inserted=dataBaseHelper.insertData(username,password,emailid,phone_number,address);
                    if (inserted)
                    {
                        Toast.makeText(Signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Signup.this, UserHomePage.class);
                        intent.putExtra("username",username);
                        intent.putExtra("password",password);
                        intent.putExtra("emailid",emailid);
                        intent.putExtra("phone",phone_number);
                        intent.putExtra("address",address);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(Signup.this, "Please Fill All The Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname.setText("");
                pass.setText("");
                verify.setText("");
                email.setText("");
                phone.setText("");
                addres.setText("");
            }
        });
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signup.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}