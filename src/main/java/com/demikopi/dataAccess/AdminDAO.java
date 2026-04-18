package com.demikopi.dataAccess;

import com.demikopi.model.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO extends DatabaseConfig {

    public Admin getAdmin(String username) {
        try {
            String query = "select username, password, nama_lengkap from admin where username = ?";
            PreparedStatement myStmt = conn.prepareStatement(query);
            myStmt.setString(1, username);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                String un = myRs.getString("username");
                String pass = myRs.getString("password");
                String namaLengkap = myRs.getString("nama_lengkap");
                return new Admin(username, pass, namaLengkap);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
// Done