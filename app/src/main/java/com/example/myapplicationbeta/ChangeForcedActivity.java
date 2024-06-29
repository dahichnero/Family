package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChangeForcedActivity extends AppCompatActivity {
    static final Connection conn = SingleTonCon.getInstance().getConnection();
    private static int Home;
    private static int Food;
    private static int Transport;
    private static int Chemistry;
    private static int Taxes;
    private static int TotalAmount;

    public static void UpdateDataForced(int homespend, int foodspend, int transportspend,
                                        int chemistryspend, int taxesspend, String Login) throws Exception {
        int UserID = Percents.CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("UPDATE public.forcedspendings SET" +
                " homespending=?, foodspending=?, transportspending=?, hygienespending=?," +
                " taxesspending=? WHERE userid=?;");
        PS.setInt(1, homespend);
        PS.setInt(2, foodspend);
        PS.setInt(3, transportspend);
        PS.setInt(4, chemistryspend);
        PS.setInt(5, taxesspend);
        PS.setInt(6, UserID);
        PS.executeUpdate();
    }

    public static int TotalAmount(String Login) throws Exception {
        int UserID = Percents.CheckerUserId(Login);

        PreparedStatement ps = conn.prepareStatement("SELECT totalamountofmoney FROM public.forcedspendings where userid = '" + UserID + "';");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int totalamounfromsqlusers = rs.getInt("totalamountofmoney");
            return totalamounfromsqlusers;
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_forced);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        Button buttoncontinue = findViewById(R.id.appCompatButton27);
        Button buttonback = findViewById(R.id.appcompatbuttonbackfromeditingforced);

        EditText edittextHome = findViewById(R.id.editText23);
        EditText edittextFood = findViewById(R.id.editText24);
        EditText edittextTransport = findViewById(R.id.editText25);
        EditText edittextChemistry = findViewById(R.id.editText26);
        EditText edittextTaxes = findViewById(R.id.editText27);

        Intent intentcontinue = new Intent(ChangeForcedActivity.this, EighthScreen.class);
        Intent intentback = new Intent(ChangeForcedActivity.this, EighthScreen.class);

        buttoncontinue.setOnClickListener(v -> {
            int lengthHome = edittextHome.length();
            int lengthFood = edittextFood.length();
            int lengthTransport = edittextTransport.length();
            int lengthChemistry = edittextChemistry.length();
            int lengthTaxes = edittextTaxes.length();

            if (lengthHome == 0) edittextHome.setError("Хоть что-то должно быть!");
            if (lengthFood == 0) edittextFood.setError("Хоть что-то должно быть!");
            if (lengthTransport == 0) edittextTransport.setError("Хоть что-то должно быть!");
            if (lengthChemistry == 0) edittextChemistry.setError("Хоть что-то должно быть!");
            if (lengthTaxes == 0) edittextTaxes.setError("Хоть что-то должно быть!");
            else {
                try {
                    TotalAmount = TotalAmount(name);
                } catch (Exception e) {
                    Snackbar.make(v, "Что-то пошло не так!", Snackbar.LENGTH_LONG).show();
                }

                try {
                    Home = Integer.parseInt(edittextHome.getText().toString());
                } catch (NumberFormatException N) {
                    edittextHome.setError("Символов должно быть не больше 256!");
                }

                try {
                    Food = Integer.parseInt(edittextFood.getText().toString());
                } catch (NumberFormatException N) {
                    edittextFood.setError("Символов должно быть не больше 256!");
                }

                try {
                    Transport = Integer.parseInt(edittextTransport.getText().toString());
                } catch (NumberFormatException N) {
                    edittextTransport.setError("Символов должно быть не больше 256!");
                }

                try {
                    Chemistry = Integer.parseInt(edittextChemistry.getText().toString());
                } catch (NumberFormatException N) {
                    edittextChemistry.setError("Символов должно быть не больше 256!");
                }

                try {
                    Taxes = Integer.parseInt(edittextTaxes.getText().toString());
                } catch (NumberFormatException N) {
                    edittextTaxes.setError("Символов должно быть не больше 256!");
                }

                try {
                    if (Home + Food + Transport + Chemistry + Taxes <= TotalAmount) {
                        UpdateDataForced(Home, Food, Transport, Chemistry, Taxes, name);
                        intentcontinue.putExtra("login", name);
                        startActivity(intentcontinue);
                    } else {
                        Snackbar.make(v, "Сумма чисел всех трат больше чем выделенные траты", Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeForcedActivity.this);
                    builder.setMessage("Что-то пошло не так!");
                    builder.show();
                }
            }
        });

        buttonback.setOnClickListener(v -> {
            intentback.putExtra("login", name);
            startActivity(intentback);
        });
    }
}