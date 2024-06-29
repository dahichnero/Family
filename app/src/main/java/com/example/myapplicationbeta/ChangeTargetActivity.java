package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ChangeTargetActivity extends AppCompatActivity {
    static final Connection conn = SingleTonCon.getInstance().getConnection();
    private static int Wish;

    private static void UpdateWish(String wishName, String Login) throws Exception {
        int UserID = Percents.CheckerUserId(Login);

        PreparedStatement ps = conn.prepareStatement("UPDATE public.moneywishlist SET targetname=? WHERE userid=?;");
        ps.setString(1, wishName);
        ps.setInt(2, UserID);
        ps.executeUpdate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_target);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");

        EditText EditTextWish = findViewById(R.id.editText20);
        Button ButtonCont = findViewById(R.id.appCompatButton24);
        Button ButtonEx = findViewById(R.id.appCompatButton25);

        Intent intentCont = new Intent(ChangeTargetActivity.this, EighthScreen.class);
        Intent intentEx = new Intent(ChangeTargetActivity.this, EighthScreen.class);

        ButtonCont.setOnClickListener(v -> {
            int lengthWish = EditTextWish.length();

            if (lengthWish == 0) EditTextWish.setError("Хоть что-то должно быть!");
            else {
                String Wish = EditTextWish.getText().toString();
                try {
                    UpdateWish(Wish, name);
                    intentCont.putExtra("login", name);
                    startActivity(intentCont);
                }
                catch (Exception e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeTargetActivity.this);
                    builder.setMessage("Что-то пошло не так!");
                    builder.show();
                }
            }
        });

        ButtonEx.setOnClickListener(v -> {
            intentEx.putExtra("login", name);
            startActivity(intentEx);
        });
    }
}