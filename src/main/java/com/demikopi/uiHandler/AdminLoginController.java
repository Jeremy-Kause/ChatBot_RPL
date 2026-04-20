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
        String username = usernameInput.getText().trim();
        String password = passInput.getText().trim();

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            showAlert("Login gagal", "Username dan password wajib diisi.");
            return;
        }

        boolean loginBerhasil = adminAuth.login(username, password);

        if (loginBerhasil) {
            bukaDashboardAdmin();
        } else {
            showAlert("Login gagal", "Username atau password salah.");
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
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) usernameInput.getScene().getWindow();
            stage.setTitle("DemiKopi Admin Dashboard");
            stage.setScene(scene);
        } catch (IOException e) {
            showAlert("Error", "Gagal membuka dashboard admin.");
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
