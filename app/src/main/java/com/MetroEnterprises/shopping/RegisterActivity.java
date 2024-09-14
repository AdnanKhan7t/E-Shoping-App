package com.MetroEnterprises.shopping;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton,GetOTP,VerifyOTP;
    private EditText InputName, InputPhoneNumber, InputPassword,InputOTP;
    private ProgressDialog loadingBar;
    private String VerificationID;
    CountryCodePicker ccp;
    //  String otpid;
    FirebaseAuth mAuth;
    String countrycode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String codesent;
    String phoneNumber;
    String enteredotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        InputOTP = (EditText) findViewById(R.id.otp_text);
        loadingBar = new ProgressDialog(this);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(InputPhoneNumber);
        mAuth = FirebaseAuth.getInstance();

        //   InputPhoneNumber.setEnabled(false);
        InputPassword.setEnabled(false);
        InputOTP.setEnabled(false);

        countrycode = ccp.getSelectedCountryCodeWithPlus();

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode=ccp.getSelectedCountryCodeWithPlus();
            }
        });

        GetOTP = (Button) findViewById(R.id.otp_btn);
        VerifyOTP = (Button) findViewById(R.id.otp_verify);

        GetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number;
                number = InputPhoneNumber.getText().toString();
                // PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
                if(number.length()!=10)
                {
                    Toast.makeText(RegisterActivity.this,"Please Enter valid Phone Number",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    phoneNumber = countrycode + number;
                    PhoneAuthOptions options =PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L,TimeUnit.SECONDS)
                            .setActivity(RegisterActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);


                }

                InputOTP.setEnabled(true);
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });

        VerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredotp = InputOTP.getText().toString();
                if(enteredotp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter your OTP", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, enteredotp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // how to automatically fetch code here
                // signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(),"OTP is Not Valid",Toast.LENGTH_SHORT).show();
                //   signInWithPhoneAuthCredential(phoneNumber);

            }

            @Override
            public void onCodeSent( @NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken){
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP is Sent",Toast.LENGTH_SHORT).show();
                codesent = s;

            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //   InputPhoneNumber.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                    InputPassword.setEnabled(true);
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


    }

    private void CreateAccount(){
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(name, phone, password);
        }

    }

    private void ValidatephoneNumber(final String name, final String phone,final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, com.MetroEnterprises.shopping.LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
                else {
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, com.MetroEnterprises.shopping.MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
