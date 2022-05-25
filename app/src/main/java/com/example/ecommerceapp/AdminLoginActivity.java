package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber1, InputPassword1;
    private Button LoginButton1;

    private TextView NotAdminLink;

    private String userpass = "admin123";
    private String userPhone = "9600458863";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);



                   LoginButton1 = findViewById(R.id.admin1_login_btn);
            InputPassword1 = findViewById(R.id.admin1_login_password_input);
            InputPhoneNumber1 = findViewById(R.id.admin1_login_phone_number_input);

            NotAdminLink = findViewById(R.id.admin1_not_adminlink);

            NotAdminLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(AdminLoginActivity.this, LoginActivity.class));
                }
            });

            LoginButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String PhoneNo = InputPhoneNumber1.getText().toString();
                    String Pass = InputPassword1.getText().toString();
                    if (PhoneNo.equals(userPhone)&& Pass.equals(userpass))
                    {
                        startActivity(new Intent(AdminLoginActivity.this,AdminCategoryActivity.class));
                    }else {
                        Toast.makeText(AdminLoginActivity.this, "Check phoneNo And Password Credential", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }



    }