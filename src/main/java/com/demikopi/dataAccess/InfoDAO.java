package com.demikopi.dataAccess;

import com.demikopi.model.InfoKedai;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InfoDAO extends DatabaseConfig {

    public InfoKedai getInfo() {
        String query = "SELECT * FROM infokedai LIMIT 1";
        try (PreparedStatement myStmt = conn.prepareStatement(query)) {
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                String idInfo = myRs.getString("id_info");
                String jamOperasional = myRs.getString("jam_operasional");
                String lokasi = myRs.getString("lokasi");
                String kontak = myRs.getString("kontak");
                return new InfoKedai(idInfo, jamOperasional, lokasi, kontak);
            }
        } catch (SQLException e) {
            System.out.println("getAllInfo error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateInfo(InfoKedai info) {
        String query = "UPDATE infokedai SET jam_operasional = ?, lokasi = ?, kontak = ? WHERE id_info = ?";
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
}
// Done