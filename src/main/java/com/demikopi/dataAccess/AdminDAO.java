package com.demikopi.dataAccess;

import com.demikopi.model.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO extends DatabaseConfig {

    public Admin getAdmin(String username) {
        String query = "SELECT username, password, nama_lengkap FROM admin WHERE username = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, username);
            ResultSet myRs = myStmt.executeQuery();
            if (myRs.next()) {
                String pass = myRs.getString("password");
                String namaLengkap = myRs.getString("nama_lengkap");
                return new Admin(username, pass, namaLengkap);
            }
        } catch (SQLException e) {
            System.out.println("getAdmin error: " + e.getMessage());
        }
        return null;
    }
}
// Done