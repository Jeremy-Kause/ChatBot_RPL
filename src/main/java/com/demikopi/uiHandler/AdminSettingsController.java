package com.demikopi.uiHandler;

import com.demikopi.model.Admin;
import com.demikopi.sistemAdmin.AdminController;
import com.demikopi.sistemAdmin.AdminSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class AdminSettingsController extends AdminNavigationController {

    private final AdminController adminController = new AdminController();

    @FXML
    private Label namaAdminLabel;

    @FXML
    private Label usernameAdminLabel;

    @FXML
    private Label statusAkunLabel;

    @FXML
    private PasswordField passwordLamaInput;

    @FXML
    private PasswordField passwordBaruInput;

    @FXML
    private PasswordField konfirmasiPasswordInput;

    @FXML
    private void initialize() {
        muatAkunAdmin();
    }

    @FXML
    private void handleMuatUlangAkun() {
        muatAkunAdmin();
    }

    @FXML
    private void handleGantiPassword() {
        if (!AdminSession.isLoggedIn()) {
            showAlert(Alert.AlertType.WARNING, "Belum login", "Silakan login sebagai admin terlebih dahulu.");
            return;
        }

        String passwordLama = ambilPassword(passwordLamaInput);
        String passwordBaru = ambilPassword(passwordBaruInput);
        String konfirmasiPassword = ambilPassword(konfirmasiPasswordInput);

        if (passwordLama.isEmpty() || passwordBaru.isEmpty() || konfirmasiPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Semua kolom password wajib diisi.");
            return;
        }

        if (passwordBaru.length() < 4) {
            showAlert(Alert.AlertType.WARNING, "Password terlalu pendek", "Password baru minimal 4 karakter.");
            return;
        }

        if (!passwordBaru.equals(konfirmasiPassword)) {
            showAlert(Alert.AlertType.WARNING, "Konfirmasi tidak cocok", "Password baru dan konfirmasi harus sama.");
            return;
        }

        if (passwordLama.equals(passwordBaru)) {
            showAlert(Alert.AlertType.WARNING, "Password sama", "Password baru harus berbeda dari password lama.");
            return;
        }

        try {
            boolean berhasil = adminController.gantiPasswordAdmin(
                    AdminSession.getUsername(),
                    passwordLama,
                    passwordBaru
            );

            if (berhasil) {
                bersihkanFormPassword();
                statusAkunLabel.setText("Password berhasil diganti");
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Password admin berhasil diperbarui.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Gagal", "Password lama tidak sesuai.");
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    @FXML
    private void handleBersihkanPassword() {
        bersihkanFormPassword();
    }

    private void muatAkunAdmin() {
        if (!AdminSession.isLoggedIn()) {
            namaAdminLabel.setText("Belum login");
            usernameAdminLabel.setText("-");
            statusAkunLabel.setText("Tidak ada sesi admin aktif");
            bersihkanFormPassword();
            return;
        }

        usernameAdminLabel.setText(AdminSession.getUsername());
        namaAdminLabel.setText(isiAtau(AdminSession.getNamaLengkap(), "Admin"));
        statusAkunLabel.setText("Admin sedang login");

        try {
            Admin admin = adminController.getAdmin(AdminSession.getUsername());
            if (admin != null) {
                namaAdminLabel.setText(isiAtau(admin.getNamaLengkap(), "Admin"));
                usernameAdminLabel.setText(admin.getUsername());
                statusAkunLabel.setText("Data akun berhasil dimuat");
            }
        } catch (IllegalStateException e) {
            statusAkunLabel.setText("Data akun dari sesi, database belum bisa dimuat");
        }
    }

    private void bersihkanFormPassword() {
        passwordLamaInput.clear();
        passwordBaruInput.clear();
        konfirmasiPasswordInput.clear();
    }

    private String ambilPassword(PasswordField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private String isiAtau(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
