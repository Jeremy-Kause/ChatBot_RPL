package com.demikopi.dataAccess;

import com.demikopi.model.Menu;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO extends DatabaseConfig {

    public List<Menu> getAllMenu() {
        List<Menu> allMenu = new ArrayList<Menu>();
        try {
            // FIX: tambah JOIN ke tabel kategori agar kolom 'nama_kategori' tersedia
            String query = "SELECT m.*, k.nama_kategori FROM menu m " +
                    "INNER JOIN kategori k ON m.id_kategori = k.id_kategori";
            PreparedStatement myStmt = conn.prepareStatement(query);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                int idMenu = myRs.getInt("id_menu");
                String kategori = myRs.getString("nama_kategori");
                String nama_menu = myRs.getString("nama_menu");
                String profilRasa = myRs.getString("profil_rasa");
                String suhuSajian = myRs.getString("suhu_sajian");
                boolean bestSeller = myRs.getBoolean("is_bestseller");
                int harga = myRs.getInt("harga");
                String deskripsi = myRs.getString("deskripsi");
                boolean statusTersedia = myRs.getBoolean("status_tersedia");
                allMenu.add(new Menu(idMenu, kategori, nama_menu, profilRasa, suhuSajian, bestSeller, harga, deskripsi,
                        statusTersedia));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allMenu;
    }

    public Menu getMenuByName(String nama) {
        try {
            String query = "select m.*, k.nama_kategori from menu " +
                    "inner join kategori k on m.id_kategori = k.id_kategori where nama_menu = ?";
            PreparedStatement myStmt = conn.prepareStatement(query);
            myStmt.setString(1, nama);
            ResultSet myRs = myStmt.executeQuery();
            if (myRs.next()) {
                int idMenu = myRs.getInt("id_menu");
                String kategori = myRs.getString("nama_kategori");
                String nama_menu = myRs.getString("nama_menu");
                String profilRasa = myRs.getString("profil_rasa");
                String suhuSajian = myRs.getString("suhu_sajian");
                boolean bestSeller = myRs.getBoolean("is_bestseller");
                int harga = myRs.getInt("harga");
                String deskripsi = myRs.getString("deskripsi");
                boolean statusTersedia = myRs.getBoolean("status_tersedia");
                return new Menu(idMenu, kategori, nama_menu, profilRasa, suhuSajian, bestSeller, harga, deskripsi,
                        statusTersedia);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Menu> getMenuByKategori(String kategori) {
        List<Menu> menuKategori = new ArrayList<Menu>();
        try {
            String query = "select  m.nama_menu, m.harga, m.deskripsi, k.nama_kategori from menu m " +
                    "inner join kategori k on m.id_kategori = k.id_kategori " +
                    "where k.nama_kategori = ?";
            PreparedStatement myStmt = conn.prepareStatement(query);
            myStmt.setString(1, kategori);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                String nama_menu = myRs.getString("nama_menu");
                String kategorie = myRs.getString("nama_kategori");
                int harga = myRs.getInt("harga");
                String deskripsi = myRs.getString("deskripsi");
                Menu menu = new Menu(nama_menu, harga, kategorie, deskripsi);
                menuKategori.add(menu);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return menuKategori;
    }

    public List<Menu> getMenuByKriteria(String kriteria) {
        List<Menu> menuKriteria = new ArrayList<Menu>();
        try {
            String query = "select nama_menu, harga, deskripsi from menu where profil_rasa like ?";
            PreparedStatement myStmt = conn.prepareStatement(query);
            myStmt.setString(1, "%" + kriteria + "%");
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                String nama_menu = myRs.getString("nama_menu");
                int harga = myRs.getInt("harga");
                String deskripsi = myRs.getString("deskripsi");
                String profil_rasa = myRs.getString("profil_rasa");
                String suhuSajian = myRs.getString("suhu_sajian");
                boolean bestSeller = myRs.getBoolean("is_bestseller");
                menuKriteria.add(new Menu(nama_menu, harga, deskripsi, profil_rasa, suhuSajian, bestSeller));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return menuKriteria;
    }

    public List<Menu> getMBestSeller() {
        List<Menu> bestSeller = new ArrayList<Menu>();
        String query = "select *, k.nama_kategori from menu m inner join kategori k on k.id_kategori = m.id_kategori where ";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                String nama_menu = myRs.getString("nama_menu");
                int harga = myRs.getInt("harga");
                String deskripsi = myRs.getString("deskripsi");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bestSeller;
    }

    public boolean tambahMenu(Menu menu) {
        String query = "INSERT INTO menu " +
                "(id_kategori, nama_menu, profil_rasa, suhu_sajian, is_bestseller, harga, deskripsi, status_tersedia) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, menu.getKategori());
            myStmt.setString(2, menu.getNamaMenu());
            myStmt.setString(3, menu.getProfilRasa());
            myStmt.setString(4, menu.getSuhuSajian());
            myStmt.setBoolean(5, menu.isBestseller());
            myStmt.setInt(6, menu.getHarga());
            myStmt.setString(7, menu.getDeskripsiMenu());
            myStmt.setBoolean(8, menu.isStatusTersedia());
            int result = myStmt.executeUpdate();
            if (result == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateMenu(Menu menu) {
        String query = "update menu set id_kategori = (select id_kategori from kategori where nama_menu = ?), " +
                "nama_menu = ?, " +
                "profil_rasa = ?, " +
                "suhu_sajian = ?, " +
                "is_bestseller = ?, " +
                "harga = ?, " +
                "deskripsi = ?, " +
                "status-tersedia = ? where id_menu = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, menu.getKategori());
            myStmt.setString(2, menu.getKategori());
            myStmt.setString(3, menu.getProfilRasa());
            myStmt.setString(4, menu.getSuhuSajian());
            myStmt.setBoolean(5, menu.isBestseller());
            myStmt.setInt(6, menu.getHarga());
            myStmt.setString(7, menu.getDeskripsiMenu());
            myStmt.setBoolean(8, menu.isStatusTersedia());
            myStmt.setInt(9, menu.getIdMenu());
            int result = myStmt.executeUpdate();
            return result != 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean hapusMenu(int idMenu) {
        String query = "delete from menu where id_kategori = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setInt(1, idMenu);
            int result = myStmt.executeUpdate();
            return result != 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<Menu> getBestSellers() {
        List<Menu> menus = getAllMenu();
        List<Menu> menuBestSeller = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.isBestseller()) {
                menuBestSeller.add(menu);
            }
        }
        return menuBestSeller;
    }
}
