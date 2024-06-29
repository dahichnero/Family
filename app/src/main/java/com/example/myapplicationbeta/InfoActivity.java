package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoActivity extends AppCompatActivity {

    static final Connection conn = SingleTonCon.getInstance().getConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        int UserID;
        try {
            UserID = Percents.CheckerUserId(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final String[] FullNameUser = {""};
        final int[] incomeuser = {0};
        final String[] LoginUser = {""};
        final String[] Procentname = {""};

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        runOnUiThread(() -> {
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT users.name, users.income, users.login, procent.procentname FROM users join procent on users.procent=procent.procentid where users.userid = '" + UserID + "';");
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    FullNameUser[0] = rs.getString("name");
                    incomeuser[0] = rs.getInt("income");
                    LoginUser[0] = rs.getString("login");
                    Procentname[0] = rs.getString("procentname");
                }
            }
            catch (SQLException e)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                builder.setMessage("Что-то пошло не так!");
                builder.show();
            }
        });


        AppCompatButton buttonFullName = findViewById(R.id.appCompatButton14);
        buttonFullName.setText(FullNameUser[0]);

        AppCompatButton buttonLogin = findViewById(R.id.appCompatButton15);
        buttonLogin.setText(LoginUser[0]);

        AppCompatButton buttonIncome = findViewById(R.id.appCompatButton16);
        buttonIncome.setText(incomeuser[0] + "₽");

        AppCompatButton buttonProcent = findViewById(R.id.appCompatButton17);
        buttonProcent.setText(Procentname[0]);

        Intent intentBack = new Intent(InfoActivity.this, EighthScreen.class);
        Button ButtonBack = findViewById(R.id.appCompatButton18);

        ButtonBack.setOnClickListener(v -> {
            intentBack.putExtra("login", name);
            startActivity(intentBack);
        });
    }
}