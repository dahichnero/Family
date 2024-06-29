package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import exam.EncryptorPassword;

public class FirstScreen extends AppCompatActivity {

    final Connection conn = SingleTonCon.getInstance().getConnection();

    public boolean CheckerLogin(String LogGettingText, String Password) throws Exception {
        String login = null;
        String password = null;
        PreparedStatement execute = conn.prepareStatement("SELECT login, salt FROM users where login = '" + LogGettingText + "';");
        ResultSet rs = execute.executeQuery();
        while (rs.next()) {
            login = rs.getString("login");
            password = rs.getString("salt");
            String decryptedpassword = EncryptorPassword.decrypt(password, "orangeprettykitty3506559");
            if(login.equals(LogGettingText) && Password.equals(decryptedpassword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        Button buttonLogin = findViewById(R.id.appCompatButton13);
        buttonLogin.setOnClickListener(v -> {
            EditText editLogin = findViewById(R.id.editText9);
            EditText editPassword = findViewById(R.id.editText8);

            int lengthlogin = editLogin.length();
            int lengthpassword = editPassword.length();

            if (lengthlogin == 0 || editLogin.getText().toString().contains(" ") || lengthlogin > 256)editLogin.setError("Хоть что-то должно быть! (Пробелов быть не должно! Символов должно быть не больше 256!)");
            if (lengthpassword == 0 || editPassword.getText().toString().contains(" ") || lengthpassword > 128) editPassword.setError("Хоть что-то должно быть! (Пробелов быть не должно! Символов должно быть не больше 128!)");

            else {
                String login = editLogin.getText().toString();
                String Passwords = editPassword.getText().toString();

                try {
                    boolean fromSqlLogin = CheckerLogin(login, Passwords);
                    if (fromSqlLogin) {
                        Intent intent = new Intent(FirstScreen.this, SeventhScreen.class);
                        intent.putExtra("login", login);
                        startActivity(intent);
                    }
                    else {
                        Snackbar.make(v, "Логин неверный!", Snackbar.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    Snackbar.make(v, "Интернета всё ещё нет!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        Button buttontoregisteer = findViewById(R.id.appCompatButtonRegist);
        buttontoregisteer.setOnClickListener(v -> {
            Intent intent = new Intent(FirstScreen.this, SecondScreen.class);
            startActivity(intent);
        });
    }
}