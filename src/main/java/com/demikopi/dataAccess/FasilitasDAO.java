package com.demikopi.dataAccess;

import com.demikopi.model.Fasilitas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FasilitasDAO extends DatabaseConfig {

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

    public boolean tambahFasilitas(Fasilitas fasilitas) {
        String query = "INSERT INTO fasilitas " +
                "(nama_fasilitas, deskripsi_fasilitas) " +
                "VALUES (?, ?)";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, fasilitas.getNamaFasilitas());
            myStmt.setString(2, fasilitas.getDeskripsiFasilitas());
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateFasilitas(Fasilitas fasilitas) {
        String query = "UPDATE fasilitas SET nama_fasilitas = ?, deskripsi_fasilitas = ? " +
                "WHERE id_fasilitas = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, fasilitas.getNamaFasilitas());
            myStmt.setString(2, fasilitas.getDeskripsiFasilitas());
            myStmt.setString(3, fasilitas.getIdFasilitas());
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean hapusFasilitas(String idFasilitas) {
        String query = "DELETE FROM fasilitas WHERE id_fasilitas = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, idFasilitas);
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
// Done 