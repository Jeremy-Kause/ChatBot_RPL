package com.demikopi.sistemAdmin;

import com.demikopi.dataAccess.FasilitasDAO;
import com.demikopi.dataAccess.InfoDAO;
import com.demikopi.dataAccess.KategoriDAO;
import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;
import com.demikopi.model.Kategori;
import com.demikopi.model.Menu;

import java.util.List;

/**
 * AdminController — Business Logic Layer untuk fitur-fitur Admin.
 *
 * Kelas ini menjadi jembatan antara AdminUI dan DAO.
 * Tugasnya: validasi input, lalu delegasikan ke DAO yang sesuai.
 * 
 * Fitur yang didukung:
 *   1. Manajemen Menu      (tambah, update, hapus, lihat)
 *   2. Manajemen Info Kedai (update, lihat)
 *   3. Manajemen Fasilitas (tambah, update, hapus, lihat)
 *   4. Manajemen Kategori  (tambah, lihat)
 */
public class AdminController {

    private MenuDAO menuDAO = new MenuDAO();
    private InfoDAO infoDAO = new InfoDAO();
    private FasilitasDAO fasilitasDAO = new FasilitasDAO();
    private KategoriDAO kategoriDAO = new KategoriDAO();


    // =========================================================
    //  1. MENU
    // =========================================================

    /**
     * Mengambil seluruh daftar menu dari database.
     * Dipakai AdminUI untuk mengisi tabel/list tampilan menu.
     *
     * TODO: Panggil menuDAO.getAllMenu() dan return hasilnya.
     */
    public List<Menu> getAllMenu() {
        // TODO: return menuDAO.getAllMenu();
        return null;
    }

    /**
     * Menambahkan menu baru ke database.
     *
     * Validasi yang harus dilakukan sebelum memanggil DAO:
     * - menu tidak boleh null
     * - namaMenu tidak boleh null atau kosong
     * - harga harus > 0
     * - kategori tidak boleh null atau kosong
     * - deskripsiMenu tidak boleh null atau kosong
     *
     * TODO: Lakukan validasi di atas, lalu panggil menuDAO.tambahMenu(menu).
     *
     * @return true jika berhasil ditambahkan, false jika validasi gagal atau DB error
     */
    public boolean tambahMenu(Menu menu) {
        // TODO: validasi menu
        // TODO: return menuDAO.tambahMenu(menu);
        return false;
    }

    /**
     * Memperbarui data menu yang sudah ada di database.
     *
     * Validasi yang harus dilakukan:
     * - menu tidak boleh null
     * - idMenu harus > 0 (pastikan ID valid)
     * - namaMenu tidak boleh null atau kosong
     * - harga harus > 0
     * - kategori tidak boleh null atau kosong
     *
     * TODO: Lakukan validasi di atas, lalu panggil menuDAO.updateMenu(menu).
     *
     * @return true jika berhasil diupdate, false jika validasi gagal atau DB error
     */
    public boolean updateMenu(Menu menu) {
        // TODO: validasi menu
        // TODO: return menuDAO.updateMenu(menu);
        return false;
    }

    /**
     * Menghapus menu dari database berdasarkan objek Menu.
     *
     * Validasi yang harus dilakukan:
     * - menu tidak boleh null
     * - idMenu harus > 0
     *
     * TODO: Lakukan validasi di atas, lalu panggil menuDAO.hapusMenu(menu.getIdMenu()).
     *
     * @return true jika berhasil dihapus, false jika validasi gagal atau DB error
     */
    public boolean hapusMenu(Menu menu) {
        // TODO: validasi menu
        // TODO: return menuDAO.hapusMenu(menu.getIdMenu());
        return false;
    }


    // =========================================================
    //  2. INFO KEDAI
    // =========================================================

    /**
     * Mengambil data info kedai dari database.
     * Dipakai AdminUI untuk menampilkan info saat ini sebelum diedit.
     *
     * TODO: Panggil infoDAO.getInfoKedai() dan return hasilnya.
     *
     * @return objek InfoKedai, atau null jika data tidak ditemukan
     */
    public InfoKedai getInfoKedai() {
        // TODO: return infoDAO.getInfoKedai();
        return null;
    }

    /**
     * Memperbarui data info kedai (jam operasional, lokasi, kontak).
     *
     * Validasi yang harus dilakukan:
     * - infoKedai tidak boleh null
     * - jamOperasional tidak boleh null atau kosong
     * - lokasi tidak boleh null atau kosong
     * - kontak tidak boleh null atau kosong
     *
     * TODO: Lakukan validasi di atas, lalu panggil infoDAO.updateInfo(infoKedai).
     *
     * @return true jika berhasil diupdate, false jika validasi gagal atau DB error
     */
    public boolean updateInfoKedai(InfoKedai infoKedai) {
        // TODO: validasi infoKedai
        // TODO: return infoDAO.updateInfo(infoKedai);
        return false;
    }


    // =========================================================
    //  3. FASILITAS
    // =========================================================

    /**
     * Mengambil seluruh daftar fasilitas dari database.
     * Dipakai AdminUI untuk mengisi tabel/list tampilan fasilitas.
     *
     * TODO: Panggil fasilitasDAO.getAllFasilitas() dan return hasilnya.
     */
    public List<Fasilitas> getAllFasilitas() {
        // TODO: return fasilitasDAO.getAllFasilitas();
        return null;
    }

    /**
     * Menambahkan fasilitas baru ke database.
     *
     * Validasi yang harus dilakukan:
     * - fasilitas tidak boleh null
     * - idFasilitas tidak boleh null atau kosong
     * - namaFasilitas tidak boleh null atau kosong
     * - deskripsiFasilitas tidak boleh null atau kosong
     *
     * TODO: Lakukan validasi di atas, lalu panggil fasilitasDAO.tambahFasilitas(fasilitas).
     *
     * @return true jika berhasil ditambahkan, false jika validasi gagal atau DB error
     */
    public boolean tambahFasilitas(Fasilitas fasilitas) {
        // TODO: validasi fasilitas
        // TODO: return fasilitasDAO.tambahFasilitas(fasilitas);
        return false;
    }

    /**
     * Memperbarui data fasilitas yang sudah ada.
     *
     * Validasi yang harus dilakukan:
     * - fasilitas tidak boleh null
     * - idFasilitas tidak boleh null atau kosong
     * - namaFasilitas tidak boleh null atau kosong
     *
     * TODO: Lakukan validasi di atas, lalu panggil fasilitasDAO.updateFasilitas(fasilitas).
     *
     * @return true jika berhasil diupdate, false jika validasi gagal atau DB error
     */
    public boolean updateFasilitas(Fasilitas fasilitas) {
        // TODO: validasi fasilitas
        // TODO: return fasilitasDAO.updateFasilitas(fasilitas);
        return false;
    }

    /**
     * Menghapus fasilitas dari database berdasarkan id.
     *
     * Validasi yang harus dilakukan:
     * - idFasilitas tidak boleh null atau kosong
     *
     * TODO: Lakukan validasi di atas, lalu panggil fasilitasDAO.hapusFasilitas(idFasilitas).
     *
     * @return true jika berhasil dihapus, false jika validasi gagal atau DB error
     */
    public boolean hapusFasilitas(String idFasilitas) {
        // TODO: validasi idFasilitas
        // TODO: return fasilitasDAO.hapusFasilitas(idFasilitas);
        return false;
    }


    // =========================================================
    //  4. KATEGORI
    // =========================================================

    /**
     * Mengambil seluruh daftar kategori dari database.
     * Dipakai AdminUI untuk mengisi dropdown kategori saat tambah/edit menu.
     *
     * TODO: Panggil kategoriDAO.getAllKategori() dan return hasilnya.
     */
    public List<Kategori> getAllKategori() {
        // TODO: return kategoriDAO.getAllKategori();
        return null;
    }
}
