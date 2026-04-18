package com.demikopi.sistemAdmin;

import com.demikopi.dataAccess.AdminDAO;
import com.demikopi.dataAccess.DatabaseConfig;
import com.demikopi.model.Admin;

import java.sql.Connection;

public class AdminAuth {
    AdminDAO adminDAO = new AdminDAO();

    public boolean login(String username, String password) {
        Admin admin = adminDAO.getAdmin(username);
        if (admin == null)
            return false;
        return admin.getPassword().equals(password);
    }
}
