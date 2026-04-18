package com.demikopi;

import com.demikopi.dataAccess.AdminDAO;
import com.demikopi.model.Admin;

public class DemiKopi {
    public static void main(String[] args) {
        // Koneksi dibuat otomatis saat AdminDAO pertama kali digunakan
        AdminDAO adao = new AdminDAO();
        Admin admin = adao.getAdmin("Jeremy");
        if (admin != null) {
            System.out.println(admin.getUsername());
            System.out.println(admin.getPassword());
            System.out.println(admin.getNamaLengkap());
        } else {
            System.out.println("Ada yang salah");
        }
    }
}
