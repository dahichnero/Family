package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class menu_register_login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_register_login);

        Button buttonLogin = findViewById(R.id.appCompatButtonLogin);
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(menu_register_login.this, FirstScreen.class);
            startActivity(intent);
        });

        Button buttonRegister = findViewById(R.id.appCompatButtonRegister);
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(menu_register_login.this, SecondScreen.class);
            startActivity(intent);
        });
    }
}