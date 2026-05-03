package com.demikopi.uiHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller khusus halaman dashboard admin.
 */
public class AdminDashboardController extends AdminNavigationController {

    @FXML
    private Label lblTanggal;

    @FXML
    private void initialize() {
        if (lblTanggal != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", new Locale("id", "ID"));
            lblTanggal.setText(LocalDate.now().format(formatter));
        }
    }
}
