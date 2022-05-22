package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountBtn;
    private EditText InputName ,InputPhoneNo,InputPassword;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountBtn = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_username_input);
        InputPhoneNo = findViewById(R.id.register_phone_number_input);
        InputPassword = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        CreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNo.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Give Your Name", Toast.LENGTH_SHORT).show();
        }
         else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Enter Your PhoneNumber", Toast.LENGTH_SHORT).show();
        }
         else  if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        }else {
             loadingBar.setTitle("Create Account");
             loadingBar.setMessage("Please wait ,while we are checking the credentials");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();

             ValidatePhoneNumber(name,phone,password);
        }
    }

    private void ValidatePhoneNumber(String name, String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists() ))
                {
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("userName",name);
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);


                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congratulation,your account has been created ", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    }else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error, Please try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                }else {
                    Toast.makeText(RegisterActivity.this, "This "+phone+"already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}