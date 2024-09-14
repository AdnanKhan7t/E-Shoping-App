package com.MetroEnterprises.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MetroEnterprises.shopping.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgetPassword extends AppCompatActivity {

    private EditText phone_no;
    private EditText createPassword,reCreatePassword;
    private Button submit;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        phone_no = (EditText) findViewById(R.id.forget_phone_number);
        createPassword = (EditText) findViewById(R.id.create_password);
        reCreatePassword = (EditText) findViewById(R.id.re_create_password);
        submit = (Button) findViewById(R.id.password_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

    }
    private void LoginUser()
    {
        String phone = phone_no.getText().toString();
        String createpassword = createPassword.getText().toString();
        String recreatepassword = reCreatePassword.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please write your phone number...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(createpassword)){
            Toast.makeText(this,"Please write your new password...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(recreatepassword)){
            Toast.makeText(this,"Please Re Write your password...",Toast.LENGTH_SHORT).show();
        }
        else {
            AllowAccessToAccount(phone,createpassword,recreatepassword);
        }
    }

    private void AllowAccessToAccount(String phone, String createpassword, String recreatepassword) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists()){
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(usersData .getPhone().equals(phone)){
                        HashMap<String, Object> userdateMap = new HashMap<>();
                        userdateMap.put("password",createpassword);
                        userdateMap.put("new_password",recreatepassword);
                        RootRef.child("Users").child(phone).updateChildren(userdateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ForgetPassword.this, "Your new password is created.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ForgetPassword.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });
                    }
                    else if (usersData.getPhone()==(phone)){
                        Toast.makeText(ForgetPassword.this,"Your number is not registered so you can't create the new password",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

}