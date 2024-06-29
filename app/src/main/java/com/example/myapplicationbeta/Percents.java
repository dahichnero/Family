package com.example.myapplicationbeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Percents {

    static final Connection conn = SingleTonCon.getInstance().getConnection();
    static Boolean isthereuserid = false;

    static Boolean istheresomemoney = false;

    public static void CheckerExistingData(String Login, int summedincome) throws Exception {
        int UserID = CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("Select forcedspendings.userid, moneywishlist.userid from forcedspendings join moneywishlist on forcedspendings.userid=moneywishlist.userid join users on forcedspendings.userid=users.userid where forcedspendings.userid='" + UserID + "';");
        ResultSet RS = PS.executeQuery();

        while (RS.next()) {
            int useridfromSQL = RS.getInt("userid");
            if (useridfromSQL > 0) {
                isthereuserid = true;
                PercentDistribution(Login, summedincome);
            }
            else {
                PercentDistribution(Login, summedincome);
            }
        }
    }

    public static void CheckerExistingDataMoneyBox(String Login) throws Exception {
        int UserID = CheckerUserId(Login);

        PreparedStatement PS = conn.prepareStatement("SELECT moneyinmoneybox FROM public.moneybox where userid = '" + UserID + "';");
        ResultSet RS = PS.executeQuery();

        while (RS.next()) {
            int moneyboxfromSQL = RS.getInt("moneyinmoneybox");
            if (moneyboxfromSQL > 0) {
                istheresomemoney = true;
            }
            else {
                istheresomemoney = false;
            }
        }
        if (istheresomemoney.equals(true))
        {
            PreparedStatement PSDelete = conn.prepareStatement("DELETE FROM public.moneybox WHERE userid = '" + UserID + "';");
            PSDelete.executeUpdate();
        }
    }

    private static void ProcentCalculationsUpdate(int Money, int Procent, int Distributor, String Login) throws Exception {
        int UserID = CheckerUserId(Login);
        int moneyid = 0;
        int checkedmoneyid = 0;
        int CalculatedPercent = Money * Procent / 100;
        if (Distributor == 1) {
            UpdatePercentageIntoForcedSpendings(CalculatedPercent, Login);
        } else if (Distributor == 2) {
            UpdatePercentageIntoMoneyWishlist(CalculatedPercent, Login);
        } else if (Distributor == 3) {
            PreparedStatement PS = conn.prepareStatement("SELECT * FROM public.moneybox where userid = '" + UserID + "';");
            ResultSet rs = PS.executeQuery();
            while(rs.next()) {
                checkedmoneyid = rs.getInt("moneyboxid");
            }
            if (checkedmoneyid == 0) {
                InsertPercentageIntoMoneyBox(CalculatedPercent, Login);
            }
            else {
                UpdatePercentageIntoMoneyBox(CalculatedPercent, Login, checkedmoneyid);
            }
        }

    }

    public static void gettingsummedincomefromSQL(String Login) throws Exception {
        int incomeglobal = 0;
        int incomeusers = 0;
        PreparedStatement ps = conn.prepareStatement("Select SUM(relative.income) as sumincome From relative join users on relative.usersid=users.userid where users.login = '" + Login + "';");
        ResultSet rs = ps.executeQuery();
        int query;
        while(rs.next()) {
            query = rs.getInt("sumincome");
            incomeglobal = query;
        }

        PreparedStatement ps2 = conn.prepareStatement("Select income from users where login = '" + Login + "';");
        ResultSet rs2 = ps2.executeQuery();
        int query2;
        while (rs2.next()) {
            query2 = rs2.getInt("income");
            incomeusers=query2;
        }

        query2 = incomeglobal + incomeusers;
        CheckerExistingData(Login, query2);
    }

    private static void ProcentCalculations(int Money, int Procent, int Distributor, String Login) throws Exception {
        int UserID = CheckerUserId(Login);
        int checkedmoneyid = 0;
        int moneyid = 0;
        int CalculatedPercent = Money * Procent / 100;
        if (Distributor == 1) {
            InsertPercentageIntoForcedSpendings(CalculatedPercent, Login);
        } else if (Distributor == 2) {
            InsertPercentageIntoMoneyWishlist(CalculatedPercent, Login);
        } else if (Distributor == 3) {
            PreparedStatement PS = conn.prepareStatement("SELECT * FROM public.moneybox where userid = '" + UserID + "';");
            ResultSet rs = PS.executeQuery();
            while(rs.next()) {
                checkedmoneyid = rs.getInt("moneyboxid");
            }
            if (checkedmoneyid == 0) {
                InsertPercentageIntoMoneyBox(CalculatedPercent, Login);
            }
            else {
                UpdatePercentageIntoMoneyBox(CalculatedPercent, Login, moneyid);
            }
        }

    }

    private static void RemoveUnnecessary(String strPercent, int digit, int incomeglobal, String Login) throws Exception {
        String Value = strPercent.replaceAll("/", " ");
        String editedvalue = Value;
        int distributor = 0;

        for(int d = 1; d <= digit; d += 2) {
            if (editedvalue.charAt(0) == '0') {
                if (isthereuserid.equals(true)) {
                    ++distributor;
                    ProcentCalculationsUpdate(0, incomeglobal, distributor, Login);
                    editedvalue = editedvalue.replaceFirst("0 ", "");
                } else {
                    ++distributor;
                    ProcentCalculations(0, incomeglobal, distributor, Login);
                    editedvalue = editedvalue.replaceFirst("0 ", "");
                }
            } else {
                String result;
                int resultInt;
                String resString;
                if (isthereuserid.equals(true)) {
                    result = editedvalue.replaceAll(" .+$", "");
                    resultInt = Integer.parseInt(result);
                    ++distributor;
                    ProcentCalculationsUpdate(resultInt, incomeglobal, distributor, Login);
                    resString = Integer.toString(resultInt);
                    if (result.contains(resString)) {
                        editedvalue = editedvalue.replaceAll(resString + " ", "");
                    }
                } else {
                    result = editedvalue.replaceAll(" .+$", "");
                    resultInt = Integer.parseInt(result);
                    ++distributor;
                    ProcentCalculations(resultInt, incomeglobal, distributor, Login);
                    resString = Integer.toString(resultInt);
                    if (result.contains(resString)) {
                        editedvalue = editedvalue.replaceAll(resString + " ", "");
                    }
                }
            }
        }
    }

    public static String procentchecker(String Login) throws Exception {
        PreparedStatement execute = conn.prepareStatement("SELECT procent.procentname FROM users join procent on users.procent=procent.procentid where users.login = '" + Login + "';");
        ResultSet rs = execute.executeQuery();
        if (rs.next()) {
            String Percent = rs.getString("procentname");
            return Percent;
        } else {
            return "";
        }
    }

    public static void PercentDistribution(String Login, int incomefromSQL) throws Exception {
        String Procent = procentchecker(Login);
        boolean hasDigits = false;
        int digit = 0;

        for(int i = 0; i < Procent.length() && !hasDigits; ++i) {
            if (Character.isDigit(Procent.charAt(i))) {
                ++digit;
            }
        }

        if (Procent.length() > 5 && Procent.length() <= 7) {
            RemoveUnnecessary(Procent, digit, incomefromSQL, Login);
        } else if (Procent.length() == 8) {
            RemoveUnnecessary(Procent, digit, incomefromSQL, Login);
        } else if (Procent.length() == 5) {
            CheckerExistingDataMoneyBox(Login);
            RemoveUnnecessary(Procent, digit, incomefromSQL, Login);
        }

    }

    public static void InsertPercentageIntoForcedSpendings(int CalculatedPercent, String Login) throws Exception {
        int UserID = CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("INSERT INTO public.forcedspendings(userid, totalamountofmoney) VALUES (?, ?);");
        PS.setInt(1, UserID);
        PS.setInt(2, CalculatedPercent);
        PS.executeUpdate();
    }

    public static void InsertPercentageIntoMoneyWishlist(int CalculatedPercent, String Login) throws Exception {
        int UserID = CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("UPDATE public.moneywishlist SET moneyforwish=? WHERE userid=?;");
        PS.setInt(1, CalculatedPercent);
        PS.setInt(2, UserID);
        PS.executeUpdate();
    }

    public static void InsertPercentageIntoMoneyBox(int CalculatedPercent, String Login) throws Exception {
        int UserID = CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("INSERT INTO public.moneybox(moneyinmoneybox, userid) VALUES (?, ?);");
        PS.setInt(1, CalculatedPercent);
        PS.setInt(2, UserID);
        PS.executeUpdate();
    }

    public static void UpdatePercentageIntoForcedSpendings(int CalculatedPercent, String Login) throws Exception {
        int UserID = CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("UPDATE public.forcedspendings SET totalamountofmoney=? WHERE userid=?;");
        PS.setInt(1, CalculatedPercent);
        PS.setInt(2, UserID);
        PS.executeUpdate();
    }

    public static void UpdatePercentageIntoMoneyWishlist(int CalculatedPercent, String Login) throws Exception {
        int UserID = CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("UPDATE public.moneywishlist SET moneyforwish=? WHERE userid=?;");
        PS.setInt(1, CalculatedPercent);
        PS.setInt(2, UserID);
        PS.executeUpdate();
    }

    public static void UpdatePercentageIntoMoneyBox(int CalculatedPercent, String Login, int moneyid) throws Exception {
        int UserID = CheckerUserId(Login);
        PreparedStatement PS = conn.prepareStatement("UPDATE public.moneybox SET moneyinmoneybox=? WHERE userid=? AND moneyboxid=?;");
        PS.setInt(1, CalculatedPercent);
        PS.setInt(2, UserID);
        PS.setInt(3, moneyid);
        PS.executeUpdate();
    }

    public static int CheckerUserId(String login) throws Exception{
        int UserID = 0;
        PreparedStatement ps = conn.prepareStatement("Select userid from users where login='" + login + "';");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            UserID = rs.getInt("userid");
            return UserID;
        }
        return 0;
    }

}
