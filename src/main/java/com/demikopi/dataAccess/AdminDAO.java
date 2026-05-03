package com.demikopi.dataAccess;

import com.demikopi.model.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO extends DatabaseConfig {

    public Admin getAdmin(String username) {
        pastikanKoneksiTersedia();

        String query = "SELECT username, password, nama_lengkap FROM admin WHERE username = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, username);
            try (ResultSet myRs = myStmt.executeQuery()) {
                if (myRs.next()) {
                    String pass = myRs.getString("password");
                    String namaLengkap = myRs.getString("nama_lengkap");
                    return new Admin(username, pass, namaLengkap);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal membaca data admin: " + e.getMessage(), e);
        }
        return null;
    }
}
// Done
