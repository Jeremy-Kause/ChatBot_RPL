package com.demikopi.uiHandler;

import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Menu;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminDashboardController {

    @FXML
    private TableView<Menu> menuTable;

    @FXML
    private TableColumn<Menu, String> namaMenuColumn;

    @FXML
    private TableColumn<Menu, String> kategoriColumn;

    @FXML
    private TableColumn<Menu, Integer> hargaColumn;

    @FXML
    private TableColumn<Menu, String> deskripsiMenuColumn;

    @FXML
    private TableColumn<Menu, Menu> aksiColumn;

    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @FXML
    private void initialize() {
        rupiahFormat.setMaximumFractionDigits(0);

        namaMenuColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getNamaMenu()));
        kategoriColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getKategori()));
        hargaColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHarga()));
        deskripsiMenuColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getDeskripsiMenu()));
        aksiColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue()));
        hargaColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer harga, boolean empty) {
                super.updateItem(harga, empty);
                setText(empty || harga == null ? null : rupiahFormat.format(harga));
            }
        });
        deskripsiMenuColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String deskripsi, boolean empty) {
                super.updateItem(deskripsi, empty);
                setText(empty || deskripsi == null ? null : deskripsi);
                setWrapText(true);
            }
        });
        aksiColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            {
                editButton.getStyleClass().add("button-outline");
                editButton.setOnAction(event -> showEditInfo(getItem()));
            }

            @Override
            protected void updateItem(Menu menu, boolean empty) {
                super.updateItem(menu, empty);
                setGraphic(empty || menu == null ? null : editButton);
            }
        });

        loadMenuData();
    }

    private void loadMenuData() {
        try {
            MenuDAO menuDAO = new MenuDAO();
            List<Menu> menus = menuDAO.getAllMenu();
            menuTable.setItems(FXCollections.observableArrayList(menus));
        } catch (RuntimeException e) {
            showAlert("Data menu gagal dimuat", "Periksa koneksi database sebelum membuka dashboard admin.");
            menuTable.setItems(FXCollections.observableArrayList());
        }
    }

    private void showEditInfo(Menu menu) {
        if (menu == null) {
            return;
        }
        showAlert("Edit Menu", "Form edit detail untuk \"" + menu.getNamaMenu() + "\" belum tersedia.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
