package com.demikopi.model;

public class Admin {
    private final String username;
    private final String password;
    private final String namaLengkap;

    public Admin(String username, String password, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getNamaLengkap() {
        return namaLengkap;
    }
}
// Done