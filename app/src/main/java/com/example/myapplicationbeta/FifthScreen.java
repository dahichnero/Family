package com.example.myapplicationbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

public class FifthScreen extends AppCompatActivity {

    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_screen);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(FifthScreen.this, SeventhScreen.class);
                intent.putExtra("login", name);
                startActivity(intent);
            }
        }, 3000);
    }
}