package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoRelativesActivity extends AppCompatActivity {
    ArrayList<String> relative = new ArrayList<>();
    Map<String, Integer> relativeid = new HashMap<>();
    static final Connection conn = SingleTonCon.getInstance().getConnection();
    String[] placeholder =  { "placeholder" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_relatives);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");
        int UserID = 0;

        try {
            UserID = Percents.CheckerUserId(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        Spinner spinner = findViewById(R.id.spinner_relative);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, (List) relative);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item12, placeholder);

        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapter1.setDropDownViewResource(R.layout.spinner_item_dropdown12);

        Button ButtonRelative = findViewById(R.id.appCompatButtonNameRelative);
        Button ButtonStatus = findViewById(R.id.appCompatButtonStatusRelative);
        Button ButtonIncome = findViewById(R.id.appCompatButtonIncomeRelative);

        spinner.setAdapter(adapter);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from relative where relative.usersid = '" + UserID + "';;");
            while (rs.next()) {
                relative.add(rs.getString("relativename"));
                relativeid.put(rs.getString("relativename"), rs.getInt("relativeid"));
            }
        } catch (SQLException | NullPointerException h) {
            spinner.setAdapter(adapter1);
        }

        adapter.notifyDataSetChanged();

        int finalUserID = UserID;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                System.out.print(spinner.getSelectedItem() + " " + spinner.getSelectedItemId());
                String finalIncomeRel = "";
                String finalStatusName = "";
                String finalRelativename = "";
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT relative.relativeid, relative.relativename, relative.income, status.statusname FROM relative join status on relative.statusid=status.statusid where relative.usersid = '" + finalUserID + "';");
                    ResultSet RS = ps.executeQuery();
                    while(RS.next()) {
                        finalRelativename = RS.getString("relativename");
                        finalIncomeRel = String.valueOf(RS.getInt("income"));
                        finalStatusName = RS.getString("statusname");
                        if(finalRelativename.equals(item)) {
                            ButtonRelative.setText(finalRelativename);
                            ButtonIncome.setText(finalIncomeRel);
                            ButtonStatus.setText(finalStatusName);
                            break;
                        }
                    }

                }
                catch (Exception e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoRelativesActivity.this);
                    builder.setMessage("Что-то пошло не так!");
                    builder.show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Intent intentBack = new Intent(InfoRelativesActivity.this, EighthScreen.class);
        Button buttonBack = findViewById(R.id.appCompatButton29);
        buttonBack.setOnClickListener(v -> {
            intentBack.putExtra("login", name);
            startActivity(intentBack);
        });
    }
}

