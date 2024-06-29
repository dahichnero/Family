package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoWishActivity  extends AppCompatActivity {

    static final Connection conn = SingleTonCon.getInstance().getConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_wish);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        int UserID;
        try {
            UserID = Percents.CheckerUserId(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int alreadearned = 0;
        String Wishlist = "";

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT moneyforwish, targetname FROM public.moneywishlist where userid = '" + UserID + "';");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                alreadearned = rs.getInt("moneyforwish");
                Wishlist = rs.getString("targetname");
            }
        }
        catch (SQLException e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(InfoWishActivity.this);
            builder.setMessage("Что-то пошло не так!");
            builder.show();
        }

        AppCompatButton button1 = findViewById(R.id.appCompatButtonInfoAlreadyEarned);
        button1.setText("Заработано: " + alreadearned + "₽");

        AppCompatButton button2 = findViewById(R.id.appCompatButtonInfoTargetName);
        button2.setText("Название цели: " + Wishlist);

        Intent intentBack = new Intent(InfoWishActivity.this, EighthScreen.class);
        Button ButtonBack = findViewById(R.id.appCompatButton30);

        ButtonBack.setOnClickListener(v -> {
            intentBack.putExtra("login", name);
            startActivity(intentBack);
        });
    }
}
