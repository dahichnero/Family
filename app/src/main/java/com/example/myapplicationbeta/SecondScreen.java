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

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import exam.EncryptorPassword;

public class SecondScreen extends AppCompatActivity {
    ArrayList<String> percent = new ArrayList<>();
    Map<String, Integer> PercentID = new HashMap<>();
    String[] placeholder = { "У вас, к сожалению, нет подключения к интернету." };
    private static Integer value;
    private static int income;
    final Connection conn = SingleTonCon.getInstance().getConnection();



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
        setContentView(R.layout.activity_second_screen);

        Spinner spinner = findViewById(R.id._dynamic);

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


        Button buttonRegister = findViewById(R.id.appCompatButton12);
        buttonRegister.setOnClickListener(v -> {
            EditText editFullName = findViewById(R.id.editText11);
            EditText editLogin = findViewById(R.id.editText12);
            EditText editIncome = findViewById(R.id.editText13);
            EditText editPassword = findViewById(R.id.editText14);
            EditText editfigure = findViewById(R.id.editTextFigure);

            int lengthname = editFullName.length();
            int lengthlogin = editLogin.length();
            int lengthIncome = editIncome.length();
            int lengthpassword = editPassword.length();
            int lengthfigure = editfigure.length();

            if (lengthname == 0 || lengthname > 256) editFullName.setError("Хоть что-то должно быть! (Символов должно быть не больше 256!)");
            if (lengthlogin == 0 || editLogin.getText().toString().contains(" ") || lengthlogin > 256)editLogin.setError("Хоть что-то должно быть! (Пробелов быть не должно! Символов должно быть не больше 256!)");
            if (lengthIncome == 0 || lengthIncome > 128) editIncome.setError("Хоть что-то должно быть!");
            if (lengthpassword == 0 || editPassword.getText().toString().contains(" ") || lengthpassword > 128) editPassword.setError("Хоть что-то должно быть! (Пробелов быть не должно! Символов должно быть не больше 128!)");
            if (lengthfigure == 0 || lengthfigure > 128) editfigure.setError("Хоть что-то должно быть!");
            else {
                String login = editLogin.getText().toString();
                String Passwords = editPassword.getText().toString();
                String fullname = editFullName.getText().toString();

                try {
                    income = Integer.parseInt(editIncome.getText().toString());
                }
                catch (NumberFormatException N) {
                    editIncome.setError("Символов должно быть не больше 128!");
                }

                String figure = editfigure.getText().toString();

                try {
                    String fromSqlLogin = CheckerLogin(login, editLogin);
                    if (!fromSqlLogin.isEmpty()) {
                        String encryptedTextBase64 = EncryptorPassword.encrypt(Passwords.getBytes(StandardCharsets.UTF_8), "orangeprettykitty3506559");
                        PreparedStatement insertData = conn.prepareStatement("INSERT INTO users(login, salt, name, role, income, procent, figure) VALUES (?, ?, ?, 1, ?, ?, ?)");
                        insertData.setString(1, login);
                        insertData.setString(2, encryptedTextBase64);
                        insertData.setString(3, fullname);
                        insertData.setInt(4, income);
                        insertData.setInt(5, value);
                        insertData.setString(6, figure);
                        insertData.executeUpdate();
                        Intent intent = new Intent(SecondScreen.this, ThirdScreen.class);
                        intent.putExtra("login", login);
                        startActivity(intent);
                    }
                }
                catch (Exception e) {
                    Snackbar.make(v, "Интернета всё ещё нет!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        Button buttontologin = findViewById(R.id.appCompatButtonToLogin);
        buttontologin.setOnClickListener(v -> {
            Intent intent = new Intent(SecondScreen.this, FirstScreen.class);
            startActivity(intent);
        });

    }
}