package com.example.myapplicationbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ContinueThirdScreen extends AppCompatActivity {

    ArrayList<Object> status = new ArrayList<>();
    Map<String, Integer> StatusID = new HashMap<>();
    String[] placeholder = { "placeholder" };
    private static Integer value;
    private static Integer income;
    final Connection conn = SingleTonCon.getInstance().getConnection();

    public enum Status {
        HAS_MONEY(", имеются деньги, "),
        NO_MONEY(", не имеет денег вообще, "),
        CAN_WORK(" может работать, "),
        CANNOT_WORK(" не может работать, "),
        WILL_WORK_FOR_FAMILY(" будет работать для семьи."),
        WILL_NOT_WORK_FOR_FAMILY(" не будет работать для семьи.");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public void insertdata(String RelativeFullName, int income, int StatusId, String Login) throws Exception {
        int UserID = Percents.CheckerUserId(Login);

        PreparedStatement PS = conn.prepareStatement("INSERT INTO public.relative(relativename, income, statusid, usersid) VALUES (?, ?, ?, ?);");
        PS.setString(1, RelativeFullName);
        PS.setInt(2, income);
        PS.setInt(3, StatusId);
        PS.setInt(4, UserID);
        PS.executeUpdate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_third_screen);

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        String name = Objects.requireNonNull(arguments.get("login")).toString();

        Spinner spinner = findViewById(R.id.SpinnerStatus);

        ArrayAdapter<Object> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, status);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item12, placeholder);

        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapter1.setDropDownViewResource(R.layout.spinner_item_dropdown12);

        spinner.setAdapter(adapter);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public.status;");
            while (rs.next()) {
                Status hasMoneyStatus = rs.getBoolean("hasmoney") ? Status.HAS_MONEY : Status.NO_MONEY;
                Status canWorkStatus = rs.getBoolean("canwork") ? Status.CAN_WORK : Status.CANNOT_WORK;
                Status willWorkForFamilyStatus = rs.getBoolean("willworkforfamily") ? Status.WILL_WORK_FOR_FAMILY : Status.WILL_NOT_WORK_FOR_FAMILY;

                String rs1 = rs.getString("statusname");
                String rs2str = hasMoneyStatus.getDescription();
                String rs3str = canWorkStatus.getDescription();
                String rs4str = willWorkForFamilyStatus.getDescription();
                String rs5 = rs1 + rs2str + rs3str + rs4str;
                status.add(rs5);

                StatusID.put(rs5, rs.getInt("statusid"));
            }
        } catch (SQLException | NullPointerException h) {
            spinner.setAdapter(adapter1);
        }

        adapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                value = StatusID.get(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        CheckBox checker = findViewById(R.id.checkBox23);

        Button ButtonContinue = findViewById(R.id.appCompatButton2);
        Button ButtonBack = findViewById(R.id.appCompatButtonBack23);

        Intent intentMoreRelatives = new Intent(ContinueThirdScreen.this, ContinueThirdScreen.class);
        Intent IntentNoMoreRelatives = new Intent(ContinueThirdScreen.this, ForcedActivity.class);

        ButtonContinue.setOnClickListener(v -> {
            EditText EditTextFullNameRelative = findViewById(R.id.editText4);
            EditText EditTextIncome = findViewById(R.id.editText5);

            int lengthFullNameRelative = EditTextFullNameRelative.length();
            int lengthIncome = EditTextIncome.length();

            if (lengthFullNameRelative == 0 || lengthFullNameRelative > 512) EditTextFullNameRelative.setError("Хоть что-то должно быть! (Символов должно быть не больше 512!)");
            if (lengthIncome == 0 || lengthIncome > 128) EditTextIncome.setError("Хоть что-то должно быть!");
            else {
                String FullNameRelative = EditTextFullNameRelative.getText().toString();
                try {
                    income = Integer.parseInt(EditTextIncome.getText().toString());
                }
                catch (NumberFormatException N) {
                    EditTextIncome.setError("Символов должно быть не больше 128!");
                }

                try {
                    insertdata(FullNameRelative, income, value, name);
                    if(checker.isChecked()) {
                        intentMoreRelatives.putExtra("login", name);
                        startActivity(intentMoreRelatives);
                    }
                    else {
                        IntentNoMoreRelatives.putExtra("login", name);
                        startActivity(IntentNoMoreRelatives);
                    }
                }
                catch (Exception e) {
                    Snackbar.make(v, "Что-то пошло не так!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ButtonBack.setOnClickListener(v -> {
            Intent intent = new Intent(ContinueThirdScreen.this, ThirdScreen.class);
            startActivity(intent);
        });
    }
}