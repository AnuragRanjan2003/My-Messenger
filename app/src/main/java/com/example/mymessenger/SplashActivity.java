package com.example.mymessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(SplashActivity.this,MainActivity2.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}