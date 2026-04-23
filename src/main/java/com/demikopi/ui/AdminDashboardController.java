package com.demikopi.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller navigasi untuk semua halaman admin.
 * Setiap tombol sidebar membuka file FXML halaman penuh pada stage yang sama.
 */
public class AdminDashboardController {

    private static final String BASE_PATH = "/com/demikopi/uiHandler/Admin UI/";
    private static final String PAGE_DASHBOARD = "admin-dashboard.fxml";
    private static final String PAGE_CHAT = "chat-history.fxml";
    private static final String PAGE_MENU = "menu-management.fxml";
    private static final String PAGE_SETTINGS = "settings-view.fxml";
    private static final double DEFAULT_WIDTH = 1200;
    private static final double DEFAULT_HEIGHT = 720;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label lblTanggal;

    @FXML
    private void initialize() {
        if (lblTanggal != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", new Locale("id", "ID"));
            lblTanggal.setText(LocalDate.now().format(formatter));
        }
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        openPage(PAGE_DASHBOARD);
    }

    @FXML
    private void showChat(ActionEvent event) {
        openPage(PAGE_CHAT);
    }

    @FXML
    private void showMenu(ActionEvent event) {
        openPage(PAGE_MENU);
    }

    @FXML
    private void showSettings(ActionEvent event) {
        openPage(PAGE_SETTINGS);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("UI logout belum dihubungkan ke alur autentikasi.");
        alert.showAndWait();
    }

    private void openPage(String fxmlName) {
        if (rootPane == null || rootPane.getScene() == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE_PATH + fxmlName));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene currentScene = stage.getScene();
            double width = currentScene != null ? currentScene.getWidth() : DEFAULT_WIDTH;
            double height = currentScene != null ? currentScene.getHeight() : DEFAULT_HEIGHT;
            stage.setScene(new Scene(root, width, height));
            stage.setTitle("DemiKopi Admin Dashboard");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Gagal membuka halaman " + fxmlName);
            alert.showAndWait();
        }
    }
}
