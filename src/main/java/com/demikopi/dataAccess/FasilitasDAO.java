package com.demikopi.dataAccess;

import com.demikopi.model.Fasilitas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * FasilitasDAO adalah class Data Access Object untuk tabel fasilitas.
 *
 * Tugas utama class ini:
 * 1. Mengambil data fasilitas dari database.
 * 2. Menambahkan data fasilitas baru.
 * 3. Mengubah data fasilitas yang sudah ada.
 * 4. Menghapus data fasilitas berdasarkan id.
 *
 * Catatan:
 * - DAO tidak menyimpan data fasilitas sebagai field.
 * - Data fasilitas disimpan di model Fasilitas.
 * - DAO hanya bertugas menjalankan query SQL ke database.
 */
public class FasilitasDAO extends DatabaseConfig {

    /**
     * Mengambil semua data fasilitas dari tabel fasilitas.
     *
     * Query:
     * SELECT id_fasilitas, nama_fasilitas, deskripsi_fasilitas FROM fasilitas
     *
     * TODO:
     * - Pastikan nama kolom di database sama dengan query ini.
     * - Gunakan method ini di ChatEngine atau InfoDAO saat user bertanya fasilitas.
     *
     * @return List berisi semua objek Fasilitas dari database.
     */
    public List<Fasilitas> getAllFasilitas() {
        List<Fasilitas> fasilitasList = new ArrayList<>();

        String query = "SELECT id_fasilitas, nama_fasilitas, deskripsi_fasilitas FROM fasilitas";

        try (PreparedStatement myStmt = conn.prepareStatement(query);
             ResultSet myRs = myStmt.executeQuery()) {

            while (myRs.next()) {
                String idFasilitas = myRs.getString("id_fasilitas");
                String namaFasilitas = myRs.getString("nama_fasilitas");
                String deskripsiFasilitas = myRs.getString("deskripsi_fasilitas");

                Fasilitas fasilitas = new Fasilitas(idFasilitas, namaFasilitas, deskripsiFasilitas);
                fasilitasList.add(fasilitas);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return fasilitasList;
    }

    /**
     * Mengambil satu data fasilitas berdasarkan id_fasilitas.
     *
     * Query:
     * SELECT id_fasilitas, nama_fasilitas, deskripsi_fasilitas
     * FROM fasilitas
     * WHERE id_fasilitas = ?
     *
     * TODO:
     * - Pakai method ini jika admin ingin melihat detail satu fasilitas.
     * - Return null menandakan fasilitas tidak ditemukan.
     *
     * @param idFasilitas id fasilitas yang ingin dicari.
     * @return objek Fasilitas jika ditemukan, null jika tidak ada.
     */
    public Fasilitas getFasilitasById(String idFasilitas) {
        String query = "SELECT id_fasilitas, nama_fasilitas, deskripsi_fasilitas " +
                "FROM fasilitas WHERE id_fasilitas = ?";

        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, idFasilitas);

            ResultSet myRs = myStmt.executeQuery();

            if (myRs.next()) {
                String id = myRs.getString("id_fasilitas");
                String nama = myRs.getString("nama_fasilitas");
                String deskripsi = myRs.getString("deskripsi_fasilitas");

                return new Fasilitas(id, nama, deskripsi);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Menambahkan fasilitas baru ke tabel fasilitas.
     *
     * Query:
     * INSERT INTO fasilitas (id_fasilitas, nama_fasilitas, deskripsi_fasilitas)
     * VALUES (?, ?, ?)
     *
     * TODO:
     * - Validasi data di layer controller sebelum memanggil method ini.
     * - Pastikan id_fasilitas belum dipakai agar tidak terjadi duplicate key.
     *
     * @param fasilitas objek fasilitas yang akan disimpan.
     * @return true jika insert berhasil, false jika gagal.
     */
    public boolean tambahFasilitas(Fasilitas fasilitas) {
        String query = "INSERT INTO fasilitas " +
                "(id_fasilitas, nama_fasilitas, deskripsi_fasilitas) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, fasilitas.getIdFasilitas());
            myStmt.setString(2, fasilitas.getNamaFasilitas());
            myStmt.setString(3, fasilitas.getDeskripsiFasilitas());

            return myStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Memperbarui data fasilitas berdasarkan id_fasilitas.
     *
     * Query:
     * UPDATE fasilitas
     * SET nama_fasilitas = ?, deskripsi_fasilitas = ?
     * WHERE id_fasilitas = ?
     *
     * TODO:
     * - Pastikan data fasilitas yang akan diupdate sudah ada di database.
     * - Validasi nama dan deskripsi agar tidak kosong.
     *
     * @param fasilitas objek fasilitas berisi data terbaru.
     * @return true jika ada baris yang berhasil diupdate, false jika gagal.
     */
    public boolean updateFasilitas(Fasilitas fasilitas) {
        String query = "UPDATE fasilitas SET nama_fasilitas = ?, deskripsi_fasilitas = ? " +
                "WHERE id_fasilitas = ?";

        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, fasilitas.getNamaFasilitas());
            myStmt.setString(2, fasilitas.getDeskripsiFasilitas());
            myStmt.setString(3, fasilitas.getIdFasilitas());

            return myStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Menghapus fasilitas berdasarkan id_fasilitas.
     *
     * Query:
     * DELETE FROM fasilitas WHERE id_fasilitas = ?
     *
     * TODO: 
     * - Tambahkan konfirmasi di UI sebelum menghapus data.
     * - Pertimbangkan soft delete jika data fasilitas tidak boleh hilang permanen.
     *
     * @param idFasilitas id fasilitas yang akan dihapus.
     * @return true jika delete berhasil, false jika gagal.
     */
    public boolean hapusFasilitas(String idFasilitas) {
        String query = "DELETE FROM fasilitas WHERE id_fasilitas = ?";

        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, idFasilitas);

            return myStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
