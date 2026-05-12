package com.demikopi.uiHandler;

import com.demikopi.model.InfoKedai;
import com.demikopi.sistemAdmin.AdminController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class AdminLokasiController extends AdminNavigationController {

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
    private Label kesiapanJawabanLabel;

    @FXML
    private Label cakupanJawabanLabel;

    @FXML
    private Label jamStatusLabel;

    @FXML
    private Label lokasiStatusLabel;

    @FXML
    private Label kontakStatusLabel;

    @FXML
    private void initialize() {
        namaKedaiInput.setText("DemiKopi");
        namaKedaiInput.setEditable(false);

        muatInfoKedai();
        pasangListenerPerubahan();
    }

    @FXML
    private void handleSimpanLokasi() {
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
                perbaruiRingkasanKesiapan();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Lokasi dan info kedai berhasil disimpan.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Gagal", "Data tidak berubah atau tidak ditemukan.");
            }
        } catch (IllegalStateException e) {
            setRingkasanTidakSiap("Gagal memuat", "Data jawaban belum bisa diperiksa");
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    @FXML
    private void handleMuatUlangLokasi() {
        muatInfoKedai();
    }

    private void muatInfoKedai() {
        try {
            infoAktif = adminController.getInfoKedai();

            if (infoAktif == null) {
                kosongkanForm();
                perbaruiRingkasanKesiapan();
                showAlert(Alert.AlertType.WARNING, "Data kosong", "Info kedai belum tersedia di database.");
                return;
            }

            jamOperasionalInput.setText(isiAman(infoAktif.getJamOperasional()));
            lokasiInput.setText(isiAman(infoAktif.getLokasi()));
            kontakInput.setText(isiAman(infoAktif.getKontak()));
            perbaruiRingkasanKesiapan();
        } catch (IllegalStateException e) {
            infoAktif = null;
            kosongkanForm();
            setRingkasanTidakSiap("Gagal memuat", "Data jawaban belum bisa diperiksa");
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
            perbaruiRingkasanKesiapan();
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

    private void perbaruiRingkasanKesiapan() {
        boolean jamTerisi = !ambilText(jamOperasionalInput).isEmpty();
        boolean lokasiTerisi = !ambilText(lokasiInput).isEmpty();
        boolean kontakTerisi = !ambilText(kontakInput).isEmpty();
        int jumlahTerisi = 0;

        if (jamTerisi) {
            jumlahTerisi++;
        }
        if (lokasiTerisi) {
            jumlahTerisi++;
        }
        if (kontakTerisi) {
            jumlahTerisi++;
        }

        if (jumlahTerisi == 3) {
            kesiapanJawabanLabel.setText("Siap digunakan");
        } else if (jumlahTerisi == 0) {
            kesiapanJawabanLabel.setText("Belum siap");
        } else {
            kesiapanJawabanLabel.setText("Perlu dilengkapi");
        }

        cakupanJawabanLabel.setText(jumlahTerisi + " dari 3 data jawaban terisi");
        jamStatusLabel.setText(jamTerisi ? "Terisi" : "Belum");
        lokasiStatusLabel.setText(lokasiTerisi ? "Terisi" : "Belum");
        kontakStatusLabel.setText(kontakTerisi ? "Terisi" : "Belum");
    }

    private void setRingkasanTidakSiap(String judul, String keterangan) {
        kesiapanJawabanLabel.setText(judul);
        cakupanJawabanLabel.setText(keterangan);
        jamStatusLabel.setText("Belum");
        lokasiStatusLabel.setText("Belum");
        kontakStatusLabel.setText("Belum");
    }
}
