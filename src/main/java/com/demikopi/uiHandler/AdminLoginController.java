package com.demikopi.uiHandler;

import com.demikopi.sistemAdmin.AdminAuth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AdminLoginController {

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passInput;

    private final AdminAuth adminAuth = new AdminAuth();

    @FXML
    private void handleLogin() {
        String username = usernameInput.getText();
        String password = passInput.getText();
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            showAlert("Login gagal", "Username dan password wajib diisi.");
            return;
        }

        try {
            boolean loginBerhasil = adminAuth.login(username.trim(), password.trim());
            if (loginBerhasil) {
                bukaDashboardAdmin();
            } else {
                showAlert("Login gagal", "Username atau password salah.");
            }
        } catch (IllegalStateException e) {
            showAlert("Database bermasalah", e.getMessage());
        }
    }

    private void bukaDashboardAdmin() {
        try {
            URL dashboardUrl = getClass().getResource("/com/demikopi/uiHandler/Admin UI/admin-dashboard.fxml");
            if (dashboardUrl == null) {
                showAlert("Error", "File admin-dashboard.fxml tidak ditemukan.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(dashboardUrl);
            Scene scene = new Scene(loader.load(), 1200, 720);
            Stage stage = (Stage) usernameInput.getScene().getWindow();
            stage.setTitle("DemiKopi Admin Dashboard");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka dashboard admin: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
// Done
