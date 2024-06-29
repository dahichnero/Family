package com.example.myapplicationbeta;

import android.content.Intent;
import android.health.connect.datatypes.HydrationRecord;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ForcedActivity extends AppCompatActivity {
    static final Connection conn = SingleTonCon.getInstance().getConnection();
    private static int Home;
    private static int Food;
    private static int Transport;
    private static int Chemistry;
    private static int Taxes;
    private static int Extra;

    public static void UpdateDataForced(int homespend, int foodspend, int transportspend,
                                        int chemistryspend, int taxesspend, int extraspend, String Login) throws Exception {
        int UserID = Percents.CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("INSERT INTO public.forcedspendings(userid, homespending, foodspending, transportspending, hygienespending, taxesspending, extraspendings) VALUES (?, ?, ?, ?, ?, ?, ?);");
        PS.setInt(1, UserID);
        PS.setInt(2, homespend);
        PS.setInt(3, foodspend);
        PS.setInt(4, transportspend);
        PS.setInt(5, chemistryspend);
        PS.setInt(6, taxesspend);
        PS.setInt(7, extraspend);
        PS.executeUpdate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forced);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        Button buttoncontinue = findViewById(R.id.appCompatButton22);
        Button buttonback = findViewById(R.id.appCompatButtonBack12);

        EditText edittextHome = findViewById(R.id.editText2);
        EditText edittextFood = findViewById(R.id.editText3);
        EditText edittextTransport = findViewById(R.id.editText7);
        EditText edittextChemistry = findViewById(R.id.editText15);
        EditText edittextTaxes = findViewById(R.id.editTextTaxes);
        EditText edittextExtra = findViewById(R.id.editTextExtra);

        Intent intentcontinue = new Intent(ForcedActivity.this, FifthScreen.class);
        Intent intentback = new Intent(ForcedActivity.this, ThirdScreen.class);

        buttoncontinue.setOnClickListener(v -> {

            int lengthHome = edittextHome.length();
            int lengthFood = edittextFood.length();
            int lengthTransport = edittextTransport.length();
            int lengthChemistry = edittextChemistry.length();
            int lengthTaxes = edittextTaxes.length();
            int lengthExtra = edittextExtra.length();

            if (lengthHome == 0) edittextHome.setError("Хоть что-то должно быть!");
            if (lengthFood == 0) edittextFood.setError("Хоть что-то должно быть!");
            if (lengthTransport == 0) edittextTransport.setError("Хоть что-то должно быть!");
            if (lengthChemistry == 0) edittextChemistry.setError("Хоть что-то должно быть!");
            if (lengthTaxes == 0) edittextTaxes.setError("Хоть что-то должно быть!");
            if (lengthExtra == 0) edittextExtra.setError("Хоть что-то должно быть!");
            else {

                try {
                    Home = Integer.parseInt(edittextHome.getText().toString());
                }
                catch (NumberFormatException N) {
                    edittextHome.setError("Символов должно быть не больше 256!");
                }

                try {
                    Food = Integer.parseInt(edittextFood.getText().toString());
                }
                catch (NumberFormatException N) {
                    edittextFood.setError("Символов должно быть не больше 256!");
                }

                try {
                    Transport = Integer.parseInt(edittextTransport.getText().toString());
                }
                catch (NumberFormatException N) {
                    edittextTransport.setError("Символов должно быть не больше 256!");
                }

                try {
                    Chemistry = Integer.parseInt(edittextChemistry.getText().toString());
                }
                catch (NumberFormatException N) {
                    edittextChemistry.setError("Символов должно быть не больше 256!");
                }

                try {
                    Taxes = Integer.parseInt(edittextTaxes.getText().toString());
                }
                catch (NumberFormatException N) {
                    edittextTaxes.setError("Символов должно быть не больше 256!");
                }

                try {
                    Extra = Integer.parseInt(edittextExtra.getText().toString());
                }
                catch (NumberFormatException N) {
                    edittextExtra.setError("Символов должно быть не больше 256!");
                }

                try {
                    UpdateDataForced(Home, Food, Transport, Chemistry, Taxes, Extra, name);
                    intentcontinue.putExtra("login", name);
                    startActivity(intentcontinue);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonback.setOnClickListener(v -> {
            intentback.putExtra("login", name);
            startActivity(intentback);
        });
    }
}