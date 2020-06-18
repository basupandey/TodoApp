package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.todoapp.view.MainActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }
    protected void onPause(){
        super.onPause();
        finish();
    }
}

