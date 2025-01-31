package com.example.bcp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
EditText uname,pass;
Button login,cancl,signUp;
DataBaseHelper DBHelper;
private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname=findViewById(R.id.uname);
        pass=findViewById(R.id.pass);
        login=findViewById(R.id.login);
        cancl=findViewById(R.id.cancl);
        signUp=findViewById(R.id.signUp);
        DBHelper=new DataBaseHelper(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=uname.getText().toString();
                String password=pass.getText().toString();
                if (DBHelper.verifyUser(username,password)){
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,UserHomePage.class);
                    startActivity(intent);
                    sessionManager.setLogin(true);
                } else if (username.equals("ADMIN") && password.equals("ADMINLOGIN")) {
                        Toast.makeText(MainActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, AdminHomePage.class);
                        startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No User Found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname.setText("");
                pass.setText("");
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Signup.class);
                startActivity(intent);
            }
        });
    }
}