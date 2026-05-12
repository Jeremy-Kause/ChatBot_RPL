package com.demikopi.sistemAdmin;

import com.demikopi.model.Admin;

/**
 * Menyimpan admin yang sedang login selama aplikasi admin berjalan.
 */
public final class AdminSession {

    private static String username;
    private static String namaLengkap;

    private AdminSession() {
    }

    public static void setAdmin(Admin admin) {
        if (admin == null) {
            clear();
            return;
        }

        username = admin.getUsername();
        namaLengkap = admin.getNamaLengkap();
    }

    public static boolean isLoggedIn() {
        return username != null && !username.isBlank();
    }

    public static String getUsername() {
        return username;
    }

    public static String getNamaLengkap() {
        return namaLengkap;
    }

    public static void clear() {
        username = null;
        namaLengkap = null;
    }
}
