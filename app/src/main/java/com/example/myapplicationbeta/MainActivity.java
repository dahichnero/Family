package com.example.myapplicationbeta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer mTimer;
    private static Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        runOnUiThread(() -> {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found " + e);
            }
            try {
                final String jdbcUrl = "jdbc:postgresql://195.58.41.169:5432/family";
                final String username = "postgres";
                final String password = "orangeprettykitty3506559";
                conn = DriverManager.getConnection(jdbcUrl, username, password);
            } catch (SQLException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("У вас нет интернета, проверьте подключение к сети");
                builder.show();
            }
        }
        );
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SingleTonCon.getInstance().setConnection(conn);
                Intent intent = new Intent(MainActivity.this, menu_register_login.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
