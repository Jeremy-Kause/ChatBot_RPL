package com.demikopi.sistemAdmin;

import com.demikopi.dataAccess.AdminDAO;
import com.demikopi.model.Admin;

public class AdminAuth {
    private AdminDAO adminDAO = new AdminDAO();

    public boolean login(String username, String password) {
        Admin admin = adminDAO.getAdmin(username);
        if (admin == null) {
            AdminSession.clear();
            return false;
        }

        boolean passwordCocok = admin.getPassword().equals(password);
        if (passwordCocok) {
            AdminSession.setAdmin(admin);
        } else {
            AdminSession.clear();
        }
        return passwordCocok;
    }
}
// Done
