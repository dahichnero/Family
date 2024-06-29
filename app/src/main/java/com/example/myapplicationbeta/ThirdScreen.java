package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;


public class ThirdScreen extends AppCompatActivity {
    static final Connection conn = SingleTonCon.getInstance().getConnection();
    private static String Wish;

    public void insertwish(int userid, String wish) throws Exception {
        PreparedStatement PS = conn.prepareStatement("INSERT INTO public.moneywishlist (userid, targetname) VALUES (?, ?);");
        PS.setInt(1, userid);
        PS.setString(2, wish);
        PS.executeUpdate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_screen);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        CheckBox checker = findViewById(R.id.checkBox);

        int userid = 0;

        try {
            userid = Percents.CheckerUserId(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        EditText editWish = findViewById(R.id.editTextWish);

        Button buttonContinue = findViewById(R.id.appCompatButton);

        Intent intent = new Intent(ThirdScreen.this, ContinueThirdScreen.class);
        Intent intentnorelatives = new Intent(ThirdScreen.this, ForcedActivity.class);
        Intent Back = new Intent(ThirdScreen.this, SecondScreen.class);

        int finalUserid = userid;
        buttonContinue.setOnClickListener(v -> {
            int lengthWish = editWish.length();

            if (lengthWish == 0 || lengthWish > 1024)
                editWish.setError("Хоть что-то должно быть! (Символов должно быть не больше 1024!)");
            else {
                if (checker.isChecked()) {
                    try {
                        Wish = editWish.getText().toString();
                        insertwish(finalUserid, Wish);
                        intent.putExtra("login", name);
                        startActivity(intent);
                    } catch (Exception e) {
                        Snackbar.make(v, "Что-то пошло не так!", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        insertwish(finalUserid, Wish);
                        intentnorelatives.putExtra("login", name);
                        startActivity(intentnorelatives);
                    } catch (Exception e) {
                        Snackbar.make(v, "Что-то пошло не так!", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button buttonBack = findViewById(R.id.appCompatButtonBack);
        buttonBack.setOnClickListener(v -> {
            startActivity(Back);
        });
    }

}