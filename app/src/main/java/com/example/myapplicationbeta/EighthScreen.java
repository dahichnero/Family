package com.example.myapplicationbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class EighthScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eighth_screen);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        Intent intentRelatives = new Intent(EighthScreen.this, InfoRelativesActivity.class);
        Intent intentWish = new Intent(EighthScreen.this, InfoWishActivity.class);
        Intent intentYourself = new Intent(EighthScreen.this, InfoActivity.class);
        Intent intentForced = new Intent(EighthScreen.this, ChangeForcedActivity.class);
        Intent intentEditYourself = new Intent(EighthScreen.this, ChangeInformationActivity.class);
        Intent intentEditRelatives = new Intent(EighthScreen.this, ChangeRelativeActivity.class);
        Intent intentEditWish = new Intent(EighthScreen.this, ChangeTargetActivity.class);
        Intent intentBack = new Intent(EighthScreen.this, SeventhScreen.class);

        Button ButtonRelativesInfo = findViewById(R.id.appCompatButtonInfoAboutRelatives);
        Button ButtonWishInfo = findViewById(R.id.appCompatButtonInfoAboutWish);
        Button ButtonYourselfInfo = findViewById(R.id.appCompatButtonInfoAboutYourself);
        Button ButtonForcedEdit = findViewById(R.id.appCompatButtonEditInfoAboutForced);
        Button ButtonYourselfEdit = findViewById(R.id.appCompatButtonEditInfoAboutYourself);
        Button ButtonRelativesEdit = findViewById(R.id.appCompatButtonEditInfoAboutRelatives);
        Button ButtonWishEdit = findViewById(R.id.appCompatButtonEditInfoAboutWish);
        ImageButton ButtonBack = findViewById(R.id.imageButtonBack);

        ButtonRelativesInfo.setOnClickListener(v -> {
            intentRelatives.putExtra("login", name);
            startActivity(intentRelatives);
        });
        ButtonWishInfo.setOnClickListener(v -> {
            intentWish.putExtra("login", name);
            startActivity(intentWish);
        });
        ButtonYourselfInfo.setOnClickListener(v -> {
            intentYourself.putExtra("login", name);
            startActivity(intentYourself);
        });
        ButtonForcedEdit.setOnClickListener(v -> {
            intentForced.putExtra("login", name);
            startActivity(intentForced);
        });
        ButtonYourselfEdit.setOnClickListener(v -> {
            intentEditYourself.putExtra("login", name);
            startActivity(intentEditYourself);
        });
        ButtonRelativesEdit.setOnClickListener(v -> {
            intentEditRelatives.putExtra("login", name);
            startActivity(intentEditRelatives);
        });
        ButtonWishEdit.setOnClickListener(v -> {
            intentEditWish.putExtra("login", name);
            startActivity(intentEditWish);
        });
        ButtonBack.setOnClickListener(v -> {
            intentBack.putExtra("login", name);
            startActivity(intentBack);
        });
    }
}