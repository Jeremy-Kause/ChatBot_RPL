package com.demikopi.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConfig {

    protected Connection conn;

    {
        try {
            String url = "jdbc:mysql://localhost:3306/demikopi";
            String user = "root";
            String pass = "";

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Koneksi MySQL Berhasil");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Gagal koneksi: " + e.getMessage());
        }
    }

    protected void pastikanKoneksiTersedia() {
        if (conn == null) {
            throw new IllegalStateException("Database tidak terkoneksi. Pastikan MySQL berjalan dan database demikopi sudah dibuat.");
        }
    }
}
// Done
