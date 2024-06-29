package com.example.myapplicationbeta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeventhScreen extends AppCompatActivity {

    static final Connection conn = SingleTonCon.getInstance().getConnection();

    public static int TotalAmount(String Login) throws Exception {
        int incomeglobal = 0;
        int incomeusers = 0;

        PreparedStatement ps = conn.prepareStatement("Select SUM(relative.income) as sumincome From relative join users on relative.usersid=users.userid where users.login = '" + Login +"';");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            incomeglobal = rs.getInt("sumincome");
        }
        PreparedStatement ps2 = conn.prepareStatement("Select income from users where login = '" + Login + "';");
        ResultSet rs2 = ps2.executeQuery();
        while(rs2.next())
        {
            incomeusers = rs2.getInt("income");
        }

        int summedincome = incomeglobal + incomeusers;
        return summedincome;
    }

    public static int InMoneyBox(String Login) throws Exception {
        int UserID = Percents.CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("SELECT moneyinmoneybox FROM public.moneybox where userid = '" + UserID + "';");
        ResultSet rs = PS.executeQuery();
        while (rs.next()) {
            int returnedmoney = rs.getInt("moneyinmoneybox");
            return returnedmoney;
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh_screen);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        runOnUiThread(() -> {
            try {
                Percents.gettingsummedincomefromSQL(name);
            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SeventhScreen.this);
                builder.setMessage("Что-то пошло не так!");
                builder.show();
            }
        });

        int UserID = 0;
        try {
            UserID = Percents.CheckerUserId(name);
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SeventhScreen.this);
            builder.setMessage("Что-то пошло не так!");
            builder.show();
        }

        int homespend = 0;
        int foodspend = 0;
        int transportspend = 0;
        int hygienespend = 0;
        int taxesspend = 0;
        int totalamount = 0;
        try {totalamount = TotalAmount(name);} catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SeventhScreen.this);
            builder.setMessage("Что-то пошло не так!");
            builder.show();
        }
        int totalamounfromsqlusers = 0;
        int extraspend = 0;
        int inmoneybox = 0;
        try {inmoneybox = InMoneyBox(name);}
        catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SeventhScreen.this);
            builder.setMessage("Что-то пошло не так!");
            builder.show();
        }

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT homespending, foodspending, transportspending, hygienespending, taxesspending, totalamountofmoney, extraspendings FROM public.forcedspendings where userid = '" + UserID + "';");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                homespend = rs.getInt("homespending");
                foodspend = rs.getInt("foodspending");
                transportspend = rs.getInt("transportspending");
                hygienespend = rs.getInt("hygienespending");
                taxesspend = rs.getInt("taxesspending");
                totalamounfromsqlusers = rs.getInt("totalamountofmoney");
                extraspend = rs.getInt("extraspendings");
            }
        }
        catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SeventhScreen.this);
            builder.setMessage("Что-то пошло не так!");
            builder.show();
        }

        TextView textViewGeneral = findViewById(R.id.textView9);
        textViewGeneral.setText(totalamount + "₽");

        AppCompatButton button1 = findViewById(R.id.appCompatButton3);
        button1.setText("Дом: " + homespend + "₽");

        AppCompatButton button2 = findViewById(R.id.appCompatButton4);
        button2.setText("Еда: " + foodspend + "₽");

        AppCompatButton button3 = findViewById(R.id.appCompatButton5);
        button3.setText("Транспорт: " + transportspend + "₽");

        AppCompatButton button4 = findViewById(R.id.appCompatButton7);
        button4.setText("Быт.хим.: " + hygienespend + "₽");

        AppCompatButton button5 = findViewById(R.id.appCompatButton6);
        button5.setText("Налоги: " + taxesspend + "₽");

        AppCompatButton button6 = findViewById(R.id.appCompatButton8);
        button6.setText("Экстра: " + extraspend + "₽");

        AppCompatButton button7 = findViewById(R.id.appCompatButton9);
        button7.setText("Полная сумма: " + totalamounfromsqlusers + "₽");

        TextView textViewMoneyBox = findViewById(R.id.textView11);
        textViewMoneyBox.setText(inmoneybox + "₽");

        Intent intentEdit = new Intent(SeventhScreen.this, EighthScreen.class);
        Button buttonEdit = findViewById(R.id.appCompatButtonEdit);
        buttonEdit.setOnClickListener(v -> {
            intentEdit.putExtra("login", name);
            startActivity(intentEdit);
        });
    }
}