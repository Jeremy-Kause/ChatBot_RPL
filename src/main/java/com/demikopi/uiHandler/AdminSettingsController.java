package com.demikopi.uiHandler;

import com.demikopi.model.InfoKedai;
import com.demikopi.sistemAdmin.AdminController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class AdminSettingsController extends AdminNavigationController {

    private final AdminController adminController = new AdminController();

    private InfoKedai infoAktif;

    @FXML
    private TextField namaKedaiInput;

    @FXML
    private TextField jamOperasionalInput;

    @FXML
    private TextField kontakInput;

    @FXML
    private TextArea lokasiInput;

    @FXML
    private Label statusSimpanLabel;

    @FXML
    private Label databaseStatusLabel;

    @FXML
    private Label infoStatusLabel;

    @FXML
    private void initialize() {
        namaKedaiInput.setText("DemiKopi");
        namaKedaiInput.setEditable(false);

        muatInfoKedai();
        pasangListenerPerubahan();
    }

    @FXML
    private void handleSimpanPengaturan() {
        if (infoAktif == null) {
            showAlert(Alert.AlertType.WARNING, "Data belum siap", "Info kedai belum berhasil dimuat.");
            return;
        }

        String jamOperasional = ambilText(jamOperasionalInput);
        String lokasi = ambilText(lokasiInput);
        String kontak = ambilText(kontakInput);

        if (jamOperasional.isEmpty() || lokasi.isEmpty() || kontak.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Jam operasional, lokasi, dan kontak wajib diisi.");
            return;
        }

        InfoKedai infoBaru = new InfoKedai(infoAktif.getIdInfo(), jamOperasional, lokasi, kontak);
        try {
            if (adminController.updateInfoKedai(infoBaru)) {
                infoAktif = infoBaru;
                statusSimpanLabel.setText("Tersimpan");
                infoStatusLabel.setText("Info diperbarui");
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Pengaturan info kedai berhasil disimpan.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Gagal", "Pengaturan tidak berubah atau data tidak ditemukan.");
            }
        } catch (IllegalStateException e) {
            databaseStatusLabel.setText("Tidak terkoneksi");
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    @FXML
    private void handleMuatUlangPengaturan() {
        muatInfoKedai();
    }

    private void muatInfoKedai() {
        try {
            infoAktif = adminController.getInfoKedai();
            databaseStatusLabel.setText("Terkoneksi");

            if (infoAktif == null) {
                kosongkanForm();
                statusSimpanLabel.setText("Info kedai belum ada");
                infoStatusLabel.setText("Belum tersedia");
                showAlert(Alert.AlertType.WARNING, "Data kosong", "Info kedai belum tersedia di database.");
                return;
            }

            jamOperasionalInput.setText(isiAman(infoAktif.getJamOperasional()));
            lokasiInput.setText(isiAman(infoAktif.getLokasi()));
            kontakInput.setText(isiAman(infoAktif.getKontak()));
            statusSimpanLabel.setText("Data terbaru dimuat");
            infoStatusLabel.setText("Info tersedia");
        } catch (IllegalStateException e) {
            infoAktif = null;
            kosongkanForm();
            databaseStatusLabel.setText("Tidak terkoneksi");
            infoStatusLabel.setText("-");
            statusSimpanLabel.setText("Gagal memuat data");
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    private void pasangListenerPerubahan() {
        jamOperasionalInput.textProperty().addListener((observable, oldValue, newValue) -> tandaiBelumDisimpan());
        lokasiInput.textProperty().addListener((observable, oldValue, newValue) -> tandaiBelumDisimpan());
        kontakInput.textProperty().addListener((observable, oldValue, newValue) -> tandaiBelumDisimpan());
    }

    private void tandaiBelumDisimpan() {
        if (infoAktif != null) {
            statusSimpanLabel.setText("Perubahan belum disimpan");
        }
    }

    private void kosongkanForm() {
        jamOperasionalInput.clear();
        lokasiInput.clear();
        kontakInput.clear();
    }

    private String ambilText(TextInputControl field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private String isiAman(String value) {
        return value == null ? "" : value;
    }
}
