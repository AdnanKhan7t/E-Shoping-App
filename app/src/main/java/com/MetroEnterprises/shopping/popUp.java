package com.MetroEnterprises.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class popUp extends AppCompatActivity {
    Animation AnimE,AnimM,RAM;
    ImageView img_e,img_m;
    TextView Ram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        RAM= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slidedown);
        Ram = findViewById(R.id.ram);
        Ram.startAnimation(RAM);

        AnimE = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        img_e =findViewById(R.id.e);
        img_e.startAnimation(AnimE);

        AnimM = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotaterevers);
        img_m =findViewById(R.id.m);
        img_m.startAnimation(AnimM);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(popUp.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },3000);

    }
}