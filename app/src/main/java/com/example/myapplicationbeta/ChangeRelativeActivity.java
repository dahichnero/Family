package com.example.myapplicationbeta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeRelativeActivity extends AppCompatActivity {

    ArrayList<String> relative = new ArrayList<>();
    Map<String, Integer> relativeid = new HashMap<>();
    ArrayList<Object> status = new ArrayList<>();
    Map<String, Integer> StatusID = new HashMap<>();
    static final Connection conn = SingleTonCon.getInstance().getConnection();
    String[] placeholder =  { "placeholder" };
    private static Integer value;
    private static Integer income;
    private static int relid;

    public void updatedata(String RelativeFullName, int income, int StatusId, String Login, int relid) throws Exception {
        int UserID = Percents.CheckerUserId(Login);

        PreparedStatement PS = conn.prepareStatement("UPDATE public.relative SET relativename=?, income=?, statusid=? WHERE usersid=? and relativeid=?;");
        PS.setString(1, RelativeFullName);
        PS.setInt(2, income);
        PS.setInt(3, StatusId);
        PS.setInt(4, UserID);
        PS.setInt(5, relid);
        PS.executeUpdate();
    }

    public enum Status {
        HAS_MONEY(" Имеются деньги "),
        NO_MONEY(" Не имеет денег вообще "),
        CAN_WORK(" Может работать "),
        CANNOT_WORK(" Не может работать "),
        WILL_WORK_FOR_FAMILY(" Будет работать для семьи "),
        WILL_NOT_WORK_FOR_FAMILY(" Не будет работать для семьи ");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_relative);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String name = extras.getString("login");
        int UserID = 0;

        try {
            UserID = Percents.CheckerUserId(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Spinner spinnerchooserelative = findViewById(R.id.spinner_relative);
        Spinner spinnerchooseStatus = findViewById(R.id._dynamiccc);

        ArrayAdapter<String> adapterRelative = new ArrayAdapter<>(this, R.layout.spinner_item, (List) relative);
        ArrayAdapter<String> adapterplaceholder = new ArrayAdapter<>(this, R.layout.spinner_item12, placeholder);
        ArrayAdapter<Object> adapterStatus = new ArrayAdapter<>(this, R.layout.spinner_item, status);

        adapterRelative.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapterStatus.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapterplaceholder.setDropDownViewResource(R.layout.spinner_item_dropdown12);

        Button ButtonCont = findViewById(R.id.appCompatButton26);
        Button ButtonExit = findViewById(R.id.appCompatButton29);

        spinnerchooserelative.setAdapter(adapterRelative);
        spinnerchooseStatus.setAdapter(adapterStatus);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from relative where relative.usersid = '" + UserID + "';;");
            while (rs.next()) {
                relative.add(rs.getString("relativename"));
                relativeid.put(rs.getString("relativename"), rs.getInt("relativeid"));
                relid = rs.getInt("relativeid");
            }
        } catch (SQLException | NullPointerException h) {
            spinnerchooserelative.setAdapter(adapterplaceholder);
        }

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public.status;");
            while (rs.next()) {
                ChangeRelativeActivity.Status hasMoneyStatus = rs.getBoolean("hasmoney") ? ChangeRelativeActivity.Status.HAS_MONEY : ChangeRelativeActivity.Status.NO_MONEY;
                ChangeRelativeActivity.Status canWorkStatus = rs.getBoolean("canwork") ? ChangeRelativeActivity.Status.CAN_WORK : ChangeRelativeActivity.Status.CANNOT_WORK;
                ChangeRelativeActivity.Status willWorkForFamilyStatus = rs.getBoolean("willworkforfamily") ? ChangeRelativeActivity.Status.WILL_WORK_FOR_FAMILY : ChangeRelativeActivity.Status.WILL_NOT_WORK_FOR_FAMILY;

                String rs1 = rs.getString("statusname");
                String rs2str = hasMoneyStatus.getDescription();
                String rs3str = canWorkStatus.getDescription();
                String rs4str = willWorkForFamilyStatus.getDescription();
                String rs5 = rs1 + rs2str + rs3str + rs4str;
                status.add(rs5);

                StatusID.put(rs5, rs.getInt("statusid"));
            }
        } catch (SQLException | NullPointerException h) {
            spinnerchooseStatus.setAdapter(adapterStatus);
        }

        adapterRelative.notifyDataSetChanged();
        adapterStatus.notifyDataSetChanged();

        EditText editTextnewFNameRel = findViewById(R.id.editText21);
        EditText editTextnewInRel = findViewById(R.id.editText22);

        int finalUserID = UserID;
        spinnerchooserelative.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                String finalIncomeRel = "";
                String finalRelativename = "";
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT relative.relativeid, relative.relativename, relative.income, status.statusname FROM relative join status on relative.statusid=status.statusid where relative.usersid = '" + finalUserID + "';");
                    ResultSet RS = ps.executeQuery();
                    while(RS.next()) {
                        finalRelativename = RS.getString("relativename");
                        finalIncomeRel = String.valueOf(RS.getInt("income"));
                        if(finalRelativename.equals(item)) {
                            editTextnewFNameRel.setText(finalRelativename);
                            editTextnewInRel.setText(finalIncomeRel);
                            break;
                        }
                    }

                }
                catch (Exception e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeRelativeActivity.this);
                    builder.setMessage("Что-то пошло не так!");
                    builder.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerchooseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                value = StatusID.get(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intentcont = new Intent(ChangeRelativeActivity.this, EighthScreen.class);
        Intent intentBack = new Intent(ChangeRelativeActivity.this, EighthScreen.class);

        ButtonCont.setOnClickListener(v -> {
            int lengthFullNameRelative = editTextnewFNameRel.length();
            int lengthIncome = editTextnewInRel.length();

            if (lengthFullNameRelative == 0 || lengthFullNameRelative > 512) editTextnewFNameRel.setError("Хоть что-то должно быть! (Символов должно быть не больше 512!)");
            if (lengthIncome == 0 || lengthIncome > 128) editTextnewInRel.setError("Хоть что-то должно быть!");
            else {
                String FullNameRelative = editTextnewFNameRel.getText().toString();
                try {
                    income = Integer.parseInt(editTextnewInRel.getText().toString());
                }
                catch (NumberFormatException N) {
                    editTextnewInRel.setError("Символов должно быть не больше 128!");
                }

                try {
                    updatedata(FullNameRelative, income, value, name, relid);
                    intentcont.putExtra("login", name);
                    startActivity(intentcont);
                }
                catch (Exception e) {
                    Snackbar.make(v, "Интернета всё ещё нет!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ButtonExit.setOnClickListener(v -> {
            intentBack.putExtra("login", name);
            startActivity(intentBack);
        });
    }
}