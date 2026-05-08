package com.demikopi.uiHandler;

import com.demikopi.dataAccess.FasilitasDAO;
import com.demikopi.model.Fasilitas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller halaman Manajemen Fasilitas.
 */
public class FacilityManagementController extends AdminNavigationController {

    private final FasilitasDAO fasilitasDAO = new FasilitasDAO();
    private final ObservableList<Fasilitas> semuaFasilitas = FXCollections.observableArrayList();
    private final ObservableList<Fasilitas> fasilitasTampil = FXCollections.observableArrayList();

    @FXML
    private TableView<Fasilitas> fasilitasTable;

    @FXML
    private TableColumn<Fasilitas, String> idFasilitasColumn;

    @FXML
    private TableColumn<Fasilitas, String> namaFasilitasColumn;

    @FXML
    private TableColumn<Fasilitas, String> deskripsiFasilitasColumn;

    @FXML
    private TextField searchInput;

    @FXML
    private TextField namaFasilitasInput;

    @FXML
    private TextArea deskripsiFasilitasInput;

    @FXML
    private Label formTitleLabel;

    @FXML
    private Label statusFasilitasLabel;

    @FXML
    private void initialize() {
        siapkanKolomTabelFasilitas();
        loadFasilitasData();

        fasilitasTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, fasilitasDipilih) -> {
            if (fasilitasDipilih != null) {
                tampilkanFasilitasKeForm(fasilitasDipilih);
            }
        });
    }

    @FXML
    private void handleSimpanFasilitas() {
        Fasilitas fasilitasDipilih = fasilitasTable.getSelectionModel().getSelectedItem();
        Fasilitas fasilitasDariForm = bacaFormFasilitas(fasilitasDipilih);

        if (fasilitasDariForm == null) {
            return;
        }

        try {
            boolean berhasil = fasilitasDipilih == null
                    ? fasilitasDAO.tambahFasilitas(fasilitasDariForm)
                    : fasilitasDAO.updateFasilitas(fasilitasDariForm);

            if (berhasil) {
                loadFasilitasData();
                bersihkanFormFasilitas();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data fasilitas berhasil disimpan.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Gagal", "Data fasilitas tidak berubah.");
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    @FXML
    private void handleHapusFasilitas() {
        Fasilitas fasilitasDipilih = fasilitasTable.getSelectionModel().getSelectedItem();

        if (fasilitasDipilih == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih fasilitas", "Pilih satu fasilitas yang ingin dihapus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Hapus Fasilitas");
        confirm.setHeaderText(null);
        confirm.setContentText("Hapus fasilitas " + fasilitasDipilih.getNamaFasilitas() + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            if (fasilitasDAO.hapusFasilitas(fasilitasDipilih.getIdFasilitas())) {
                loadFasilitasData();
                bersihkanFormFasilitas();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Fasilitas berhasil dihapus.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Gagal", "Fasilitas tidak berhasil dihapus.");
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    @FXML
    private void handleBersihkanForm() {
        bersihkanFormFasilitas();
    }

    @FXML
    private void handleRefreshFasilitas() {
        loadFasilitasData();
    }

    @FXML
    private void handleCariFasilitas() {
        terapkanFilterFasilitas();
    }

    private void siapkanKolomTabelFasilitas() {
        idFasilitasColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdFasilitas()));
        namaFasilitasColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaFasilitas()));
        deskripsiFasilitasColumn.setCellValueFactory(data -> new SimpleStringProperty(isiAman(data.getValue().getDeskripsiFasilitas())));

        fasilitasTable.setItems(fasilitasTampil);
    }

    private void loadFasilitasData() {
        try {
            semuaFasilitas.setAll(fasilitasDAO.getAllFasilitas());
            terapkanFilterFasilitas();
        } catch (IllegalStateException e) {
            semuaFasilitas.clear();
            fasilitasTampil.clear();
            updateStatusFasilitas();
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    private void terapkanFilterFasilitas() {
        String keyword = ambilText(searchInput).toLowerCase();

        fasilitasTampil.clear();
        for (Fasilitas fasilitas : semuaFasilitas) {
            if (cocokDenganPencarian(fasilitas, keyword)) {
                fasilitasTampil.add(fasilitas);
            }
        }

        updateStatusFasilitas();
    }

    private boolean cocokDenganPencarian(Fasilitas fasilitas, String keyword) {
        if (keyword.isEmpty()) {
            return true;
        }

        return isiAman(fasilitas.getIdFasilitas()).toLowerCase().contains(keyword)
                || isiAman(fasilitas.getNamaFasilitas()).toLowerCase().contains(keyword)
                || isiAman(fasilitas.getDeskripsiFasilitas()).toLowerCase().contains(keyword);
    }

    private void tampilkanFasilitasKeForm(Fasilitas fasilitas) {
        formTitleLabel.setText("Edit Fasilitas");
        namaFasilitasInput.setText(fasilitas.getNamaFasilitas());
        deskripsiFasilitasInput.setText(isiAman(fasilitas.getDeskripsiFasilitas()));
    }

    private Fasilitas bacaFormFasilitas(Fasilitas fasilitasDipilih) {
        String namaFasilitas = ambilText(namaFasilitasInput);
        String deskripsiFasilitas = ambilText(deskripsiFasilitasInput);

        if (namaFasilitas.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Nama fasilitas wajib diisi.");
            return null;
        }

        if (deskripsiFasilitas.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Deskripsi fasilitas wajib diisi.");
            return null;
        }

        String idFasilitas = fasilitasDipilih == null ? "" : fasilitasDipilih.getIdFasilitas();
        return new Fasilitas(idFasilitas, namaFasilitas, deskripsiFasilitas);
    }

    private void bersihkanFormFasilitas() {
        fasilitasTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Tambah Fasilitas");
        namaFasilitasInput.clear();
        deskripsiFasilitasInput.clear();
    }

    private void updateStatusFasilitas() {
        statusFasilitasLabel.setText("Menampilkan " + fasilitasTampil.size() + " dari " + semuaFasilitas.size() + " fasilitas");
    }

    private String ambilText(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private String ambilText(TextArea field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private String isiAman(String value) {
        return value == null ? "" : value;
    }
}
