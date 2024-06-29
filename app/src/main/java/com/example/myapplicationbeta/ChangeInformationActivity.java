package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChangeInformationActivity extends AppCompatActivity {

    ArrayList<String> percent = new ArrayList<>();
    Map<String, Integer> PercentID = new HashMap<>();
    String[] placeholder = { "placeholder" };
    private static Integer value;
    private static int income;
    private static String name;
    static final Connection conn = SingleTonCon.getInstance().getConnection();

    private static void updateinfo(String login, String fullname) throws Exception {
        PreparedStatement insertData = conn.prepareStatement("UPDATE public.users SET login=?, name=?, income=?, procent=? WHERE userid=?;");
        insertData.setString(1, login);
        insertData.setString(2, fullname);
        insertData.setInt(3, income);
        insertData.setInt(4, value);
        insertData.setInt(5, Percents.CheckerUserId(name));
        insertData.executeUpdate();
    }

    public String CheckerLogin(String LogGettingText, EditText editLogin) throws Exception {
        String StringSQL = null;
        PreparedStatement ps = conn.prepareStatement("Select login from users");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            StringSQL = rs.getString("login");
            if(LogGettingText.equals(StringSQL)) {
                editLogin.setError("Данный логин уже существует!");
                return "";
            }
        }
        return StringSQL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        name = extras.getString("login");

        Spinner spinner = findViewById(R.id._dynamicc);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, percent);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item12, placeholder);

        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapter1.setDropDownViewResource(R.layout.spinner_item_dropdown12);

        spinner.setAdapter(adapter);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from procent;");
            while (rs.next()) {
                percent.add(rs.getString("procentname"));
                PercentID.put(rs.getString("procentname"), rs.getInt("procentid"));
            }
        } catch (SQLException | NullPointerException h) {
            spinner.setAdapter(adapter1);
        }

        adapter.notifyDataSetChanged();
        System.out.println(PercentID);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                value = PercentID.get(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Button buttonRegister = findViewById(R.id.appCompatButton23);
        buttonRegister.setOnClickListener(v -> {
            EditText editFullName = findViewById(R.id.editText17);
            EditText editLogin = findViewById(R.id.editText18);
            EditText editIncome = findViewById(R.id.editText19);

            int lengthname = editFullName.length();
            int lengthlogin = editLogin.length();
            int lengthIncome = editIncome.length();

            if (lengthname == 0 || lengthname > 256) editFullName.setError("Хоть что-то должно быть! (Символов должно быть не больше 256!)");
            if (lengthlogin == 0 || editLogin.getText().toString().contains(" ") || lengthlogin > 256)editLogin.setError("Хоть что-то должно быть! (Пробелов быть не должно! Символов должно быть не больше 256!)");
            if (lengthIncome == 0 || lengthIncome > 128) editIncome.setError("Хоть что-то должно быть!");
            else {
                String login = editLogin.getText().toString();
                String fullname = editFullName.getText().toString();

                try {
                    income = Integer.parseInt(editIncome.getText().toString());
                }
                catch (NumberFormatException N) {
                    editIncome.setError("Символов должно быть не больше 128!");
                }

                try {
                    String fromSqlLogin = CheckerLogin(login, editLogin);
                    if (!fromSqlLogin.isEmpty()) {
                        updateinfo(login, fullname);
                        Intent intent = new Intent(ChangeInformationActivity.this, EighthScreen.class);
                        intent.putExtra("login", login);
                        startActivity(intent);
                    }
                }
                catch (Exception e) {
                    Snackbar.make(v, "Интернета всё ещё нет!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        Button buttontologin = findViewById(R.id.appcompatbuttonbackfromeditininform);
        buttontologin.setOnClickListener(v -> {
            Intent intent = new Intent(ChangeInformationActivity.this, EighthScreen.class);
            intent.putExtra("login", name);
            startActivity(intent);
        });

    }
}