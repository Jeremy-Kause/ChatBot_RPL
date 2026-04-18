package com.demikopi.sistemAdmin;

import com.demikopi.dataAccess.InfoDAO;
import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Menu;

/**
 * AdminController adalah kelas SERVICE / BUSINESS LOGIC layer untuk fitur admin.
 *
 * Tugasnya:
 *   1. Menerima perintah dari AdminUI
 *   2. Melakukan validasi data (aturan bisnis)
 *   3. Mendelegasikan operasi database ke DAO (MenuDAO, InfoDAO)
 *   4. Mengembalikan hasil ke UI
 *
 * AdminController TIDAK boleh berisi query SQL langsung.
 * Semua query SQL ada di kelas DAO.
 */
public class AdminController {

    // Menggunakan Composition — AdminController "punya" MenuDAO dan InfoDAO
    // bukan "extends", karena AdminController bukan DAO
    private MenuDAO menuDAO = new MenuDAO();
    private InfoDAO infoDAO = new InfoDAO();

    /**
     * Menambahkan menu baru ke database.
     * Di sini tempat validasi bisnis dijalankan sebelum data dikirim ke DAO.
     * Contoh validasi: nama tidak boleh kosong, harga harus > 0.
     */
    public boolean tambahMenu(Menu menu) {
        // TODO: validasi data menu, lalu panggil menuDAO.tambahMenu(menu)
        return false;
    }

    /**
     * Memperbarui data menu yang sudah ada.
     * Validasi: pastikan menu dengan id tersebut memang ada sebelum diupdate.
     */
    public boolean updateMenu(Menu menu) {
        // TODO: validasi, lalu panggil menuDAO.updateMenu(menu)
        return false;
    }

    /**
     * Menghapus menu dari database berdasarkan objek Menu.
     * Validasi: pastikan menu yang dihapus benar-benar ada.
     */
    public void hapusMenu(Menu menu) {
        // TODO: validasi, lalu panggil menuDAO.hapusMenu(menu.getIdMenu())
    }

    /**
     * Mengambil seluruh daftar menu untuk ditampilkan di AdminUI.
     * Mengembalikan List<Menu> dari MenuDAO.
     */
    public void tampilkanMenu() {
        // TODO: panggil menuDAO.getAllMenu() dan kirimkan ke UI
    }
}
