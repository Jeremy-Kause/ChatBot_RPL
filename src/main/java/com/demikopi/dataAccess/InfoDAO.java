package com.demikopi.dataAccess;

import com.demikopi.model.InfoKedai;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InfoDAO extends DatabaseConfig {

    public List<InfoKedai> getAllInfo() {
        List<InfoKedai> infoList = new ArrayList<>();
        String query = "SELECT * FROM info_kedai";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                String idInfo = myRs.getString("id_info");
                String jamOperasional = myRs.getString("jam_operasional");
                String lokasi = myRs.getString("lokasi");
                String kontak = myRs.getString("kontak");
                infoList.add(new InfoKedai(idInfo, jamOperasional, lokasi, kontak));
            }
        } catch (SQLException e) {
            System.out.println("getAllInfo error: " + e.getMessage());
        }
        return infoList;
    }

    public InfoKedai getInfoById(String idInfo) {
        String query = "SELECT * FROM info_kedai WHERE id_info = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, idInfo);
            ResultSet myRs = myStmt.executeQuery();
            if (myRs.next()) {
                String jamOperasional = myRs.getString("jam_operasional");
                String lokasi = myRs.getString("lokasi");
                String kontak = myRs.getString("kontak");
                return new InfoKedai(idInfo, jamOperasional, lokasi, kontak);
            }
        } catch (SQLException e) {
            System.out.println("getInfoById error: " + e.getMessage());
        }
        return null;
    }

    public boolean tambahInfo(InfoKedai info) {
        String query = "INSERT INTO info_kedai (id_info, jam_operasional, lokasi, kontak) VALUES (?, ?, ?, ?)";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, info.getIdInfo());
            myStmt.setString(2, info.getJamOperasional());
            myStmt.setString(3, info.getLokasi());
            myStmt.setString(4, info.getKontak());
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("tambahInfo error: " + e.getMessage());
        }
        return false;
    }

    public boolean updateInfo(InfoKedai info) {
        String query = "UPDATE info_kedai SET jam_operasional = ?, lokasi = ?, kontak = ? WHERE id_info = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, info.getJamOperasional());
            myStmt.setString(2, info.getLokasi());
            myStmt.setString(3, info.getKontak());
            myStmt.setString(4, info.getIdInfo());
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("updateInfo error: " + e.getMessage());
        }
        return false;
    }

    public boolean hapusInfo(String idInfo) {
        String query = "DELETE FROM info_kedai WHERE id_info = ?";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            myStmt.setString(1, idInfo);
            return myStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("hapusInfo error: " + e.getMessage());
        }
        return false;
    }
}
// Done