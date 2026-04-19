package com.demikopi.sistemAdmin;

import com.demikopi.dataAccess.AdminDAO;
import com.demikopi.model.Admin;

public class AdminAuth {
    private AdminDAO adminDAO = new AdminDAO();

    public boolean login(String username, String password) {
        Admin admin = adminDAO.getAdmin(username);
        if (admin == null)
            return false;
        return admin.getPassword().equals(password);
    }
}
// Done