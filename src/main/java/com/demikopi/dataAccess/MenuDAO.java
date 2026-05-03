package com.demikopi.dataAccess;

import com.demikopi.model.Menu;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuDAO extends DatabaseConfig {

    public List<Menu> getAllMenu() {
        pastikanKoneksiTersedia();
        List<Menu> allMenu = new ArrayList<>();
        String query = "SELECT m.*, k.nama_kategori FROM menu m " +
                "INNER JOIN kategori k ON m.id_kategori = k.id_kategori";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    int idMenu = myRs.getInt("id_menu");
                    String kategori = myRs.getString("nama_kategori");
                    String namaMenu = myRs.getString("nama_menu");
                    String profilRasa = myRs.getString("profil_rasa");
                    String suhuSajian = myRs.getString("suhu_sajian");
                    boolean bestSeller = myRs.getBoolean("is_bestseller");
                    int harga = myRs.getInt("harga");
                    String deskripsi = myRs.getString("deskripsi");
                    boolean tersedia = myRs.getBoolean("status_tersedia");
                    allMenu.add(new Menu(idMenu, kategori, namaMenu, profilRasa, suhuSajian, bestSeller, harga, deskripsi, tersedia));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal membaca data menu: " + e.getMessage(), e);
        }
        return allMenu;
    }

    public Menu getMenuByName(String nama) {
        return getAllMenu().stream().filter(m -> m.getNamaMenu().equalsIgnoreCase(nama))
                .findFirst()
                .orElse(null);
    }

    public List<Menu> getMenuByKategori(String kategori) {
        return getAllMenu().stream()
                .filter(m -> m.getKategori().equalsIgnoreCase(kategori))
                .collect(Collectors.toList());
    }

    public List<Menu> getMenuByKriteria(String kriteria) {
        String keyword = kriteria.toLowerCase();
        return getAllMenu().stream()
                .filter(m -> m.getProfilRasa() != null &&
                        m.getProfilRasa().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<Menu> getBestSellers() {
        return getAllMenu().stream()
                .filter(Menu::isBestseller)
                .collect(Collectors.toList());
    }

    public List<Menu> getMenuTersedia() {
        return getAllMenu().stream()
                .filter(Menu::isStatusTersedia)
                .collect(Collectors.toList());
    }

    public List<Menu> getMenuByRentangHarga(int minHarga, int maxHarga) {
        return getAllMenu().stream()
                .filter(m -> m.getHarga() >= minHarga && m.getHarga() <= maxHarga)
                .collect(Collectors.toList());
    }

    public boolean tambahMenu(Menu menu) {
        pastikanKoneksiTersedia();

        String query = "INSERT INTO menu (id_kategori, nama_menu, profil_rasa, suhu_sajian, is_bestseller, harga, deskripsi, status_tersedia) " +
                "VALUES ((SELECT id_kategori FROM kategori WHERE nama_kategori = ?), ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, menu.getKategori());
            myStmt.setString(2, menu.getNamaMenu());
            myStmt.setString(3, menu.getProfilRasa());
            myStmt.setString(4, menu.getSuhuSajian());
            myStmt.setBoolean(5, menu.isBestseller());
            myStmt.setInt(6, menu.getHarga());
            myStmt.setString(7, menu.getDeskripsiMenu());
            myStmt.setBoolean(8, menu.isStatusTersedia());
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menambah menu: " + e.getMessage(), e);
        }
    }

    public boolean updateMenu(Menu menu) {
        pastikanKoneksiTersedia();
        String query = "UPDATE menu SET " +
                "id_kategori = (SELECT id_kategori FROM kategori WHERE nama_kategori = ?), " +
                "nama_menu = ?, " +
                "profil_rasa = ?, " +
                "suhu_sajian = ?, " +
                "is_bestseller = ?, " +
                "harga = ?, " +
                "deskripsi = ?, " +
                "status_tersedia = ? " +
                "WHERE id_menu = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, menu.getKategori());
            myStmt.setString(2, menu.getNamaMenu());
            myStmt.setString(3, menu.getProfilRasa());
            myStmt.setString(4, menu.getSuhuSajian());
            myStmt.setBoolean(5, menu.isBestseller());
            myStmt.setInt(6, menu.getHarga());
            myStmt.setString(7, menu.getDeskripsiMenu());
            myStmt.setBoolean(8, menu.isStatusTersedia());
            myStmt.setInt(9, menu.getIdMenu());
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengubah menu: " + e.getMessage(), e);
        }
    }

    public boolean hapusMenu(int idMenu) {
        pastikanKoneksiTersedia();
        String query = "DELETE FROM menu WHERE id_menu = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setInt(1, idMenu);
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menghapus menu: " + e.getMessage(), e);
        }
    }
}
// Done
