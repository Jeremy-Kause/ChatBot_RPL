package com.demikopi.dataAccess;

import com.demikopi.model.Kategori;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO extends DatabaseConfig {

    public List<Kategori> getAllKategori() {
        List<Kategori> kategoriList = new ArrayList<>();
        String query = "select * from kategori";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                int id_kategori = myRs.getInt("id_kategori");
                String kategori_name = myRs.getString("nama_kategori");
                kategoriList.add(new Kategori(id_kategori, kategori_name));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return kategoriList;
    }

    public Kategori getKategoriById(int id) {
        List<Kategori> kategoriList = getAllKategori();
        for (Kategori kategori : kategoriList) {
            if (kategori.getIdKategori() == id) {
                return kategori;
            }
        }
        return null;
    }

    public Kategori getKategoriByName(String name) {
        List<Kategori> kategoriList = getAllKategori();
        for (Kategori kategori : kategoriList) {
            if (kategori.getNamaKategori().equals(name)) {
                return kategori;
            }
        }
        return null;
    }

    public int getIdKategoriByName(String name) {
        List<Kategori> kategoriList = getAllKategori();
        for (Kategori kategori : kategoriList) {
            if (kategori.getNamaKategori().equals(name)) {
                return kategori.getIdKategori();
            }
        }
        return -1;
    }
}
// Done