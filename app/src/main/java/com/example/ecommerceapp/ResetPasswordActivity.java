package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.Model.Users;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check ="";
    private TextView pageTitle,titleQuestion;
    private EditText phoneNumber,question1,question2;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        check = getIntent().getStringExtra("check");
        pageTitle = findViewById(R.id.page_title);
        titleQuestion = findViewById(R.id.title_questions);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyBtn = findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings"))
        {
            pageTitle.setText("Set Question");
            titleQuestion.setText("Please set Answer the Following Security Question");
            verifyBtn.setText("Set");
            displayPreviousAnswers();
            
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) 
                {
                    setAnswers();

                }
            });
        }
        
        if (check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });


        }
    }
    private void setAnswers()
    {
        String ansewr1 = question1.getText().toString().toLowerCase();
        String ansewr2 = question2.getText().toString().toLowerCase();

        if (question1.equals("")&&question2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Please the answer Both Questions", Toast.LENGTH_SHORT).show();

        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String,Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1",ansewr1);
            userdataMap.put("answer2",ansewr2);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "You have security question set successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this,HomeActivity.class));
                    }
                }
            });
        }
    }
    private void displayPreviousAnswers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();


                    question1.setText(ans1);
                    question2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void verifyUser(){
        String phone = phoneNumber.getText().toString();
        String ansewr1 = question1.getText().toString().toLowerCase();
        String ansewr2 = question2.getText().toString().toLowerCase();
        if (!phone.equals("") && !ansewr1.equals("") && !ansewr2.equals(""))
        {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        String mPhone = snapshot.child("phone").getValue().toString();
                        if (snapshot.hasChild("Security Questions"))
                        {
                            String ans1 = snapshot.child("Security Questions") .child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();
                            if (!ans1.equals(ansewr1))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Your First Answer is Incorrect ", Toast.LENGTH_SHORT).show();
                            }
                            else if (!ans2.equals(ansewr2))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Your Second Answer is Incorrect ", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write New Password here...");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if (!newPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cnacel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.show();
                            }



                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set teh security questions.please contact admin", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ResetPasswordActivity.this, "Please enter correct phone Number", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });

        }
        else 
        {
            Toast.makeText(this, "Please Enter the Credential", Toast.LENGTH_SHORT).show();
        }

        

    }
}