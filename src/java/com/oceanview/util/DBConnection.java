package com.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private String url;
    private String username;
    private String password;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Try reading Railway/cloud environment variables first
        String host = System.getenv("MYSQLHOST");
        String port = System.getenv("MYSQLPORT");
        String db = System.getenv("MYSQLDATABASE");
        String user = System.getenv("MYSQLUSER");
        String pass = System.getenv("MYSQLPASSWORD");

        if (host != null && port != null && db != null) {
            this.url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&allowPublicKeyRetrieval=true";
            this.username = user;
            this.password = pass;
        } else {
            // Local fallback for development
            this.url = "jdbc:mysql://localhost:3306/oceanview_db";
            this.username = "root";
            this.password = "";
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}
