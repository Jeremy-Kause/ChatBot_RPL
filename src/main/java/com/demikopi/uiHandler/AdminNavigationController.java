package com.demikopi.uiHandler;

import com.demikopi.sistemAdmin.AdminSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller navigasi admin.
 * Semua halaman admin bisa memakai tombol sidebar yang sama.
 */
public class AdminNavigationController {

    private static final String BASE_PATH = "/com/demikopi/uiHandler/Admin UI/";
    private static final String PAGE_LOGIN = "login.fxml";
    private static final String PAGE_DASHBOARD = "admin-dashboard.fxml";
    private static final String PAGE_MENU = "menu-management.fxml";
    private static final String PAGE_FASILITAS = "facility-management.fxml";
    private static final String PAGE_LOKASI = "admin-lokasi.fxml";
    private static final String PAGE_SETTINGS = "settings-view.fxml";
    private static final String PAGE_USER_DASHBOARD = "/com/demikopi/uiHandler/USER UI/dashboard.fxml";
    private static final double DEFAULT_WIDTH = 1000;
    private static final double DEFAULT_HEIGHT = 650;
    private static final double LOGIN_WIDTH = 900;
    private static final double LOGIN_HEIGHT = 600;

    @FXML
    protected BorderPane rootPane;

    @FXML
    public void showDashboard(ActionEvent event) {
        openPage(PAGE_DASHBOARD);
    }

    @FXML
    public void showMenu(ActionEvent event) {
        openPage(PAGE_MENU);
    }

    @FXML
    public void showFasilitas(ActionEvent event) {
        openPage(PAGE_FASILITAS);
    }

    @FXML
    public void showLokasi(ActionEvent event) {
        openPage(PAGE_LOKASI);
    }

    @FXML
    public void showSettings(ActionEvent event) {
        openPage(PAGE_SETTINGS);
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        AdminSession.clear();
        openLoginPage();
    }

    @FXML
    public void showChatbot(ActionEvent event) {
        openUserDashboard();
    }

    protected void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
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
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman " + fxmlName);
        }
    }

    private void openLoginPage() {
        if (rootPane == null || rootPane.getScene() == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE_PATH + PAGE_LOGIN));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT));
            stage.setTitle("DemiKopi Admin");
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal logout dan membuka halaman login.");
        }
    }

    private void openUserDashboard() {
        if (rootPane == null || rootPane.getScene() == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PAGE_USER_DASHBOARD));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene currentScene = stage.getScene();
            double width = currentScene != null ? currentScene.getWidth() : 900;
            double height = currentScene != null ? currentScene.getHeight() : 640;
            stage.setTitle("DemiKopi Coffee Assistant");
            stage.setScene(new Scene(root, width, height));
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman chatbot.");
        }
    }
}
