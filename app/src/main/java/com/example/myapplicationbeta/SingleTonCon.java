package com.example.myapplicationbeta;

import java.sql.Connection;

public class SingleTonCon {
    private static SingleTonCon instance;
    private Connection conn;

    private SingleTonCon() {
    }

    public static SingleTonCon getInstance() {
        if (instance == null) {
            synchronized (SingleTonCon.class) {
                if (instance == null) {
                    instance = new SingleTonCon();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }

    public void setConnection(Connection connection) {
        this.conn = connection;
    }
}
