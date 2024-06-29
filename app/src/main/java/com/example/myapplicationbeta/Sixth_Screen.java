package com.example.myapplicationbeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.TextView;

public class Sixth_Screen extends AppCompatActivity {

    AppCompatButton buttonHome = findViewById(R.id.appCompatButton3);
    AppCompatButton buttonFood = findViewById(R.id.appCompatButton4);
    AppCompatButton buttonTransport = findViewById(R.id.appCompatButton5);
    AppCompatButton buttonTaxes = findViewById(R.id.appCompatButton6);
    AppCompatButton buttonChemistry = findViewById(R.id.appCompatButton7);
    AppCompatButton buttonExtra = findViewById(R.id.appCompatButton8);
    AppCompatButton buttonFull = findViewById(R.id.appCompatButton9);
    TextView TextAll = findViewById(R.id.textView9);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth_screen_temporary_useless);
    }
}