package com.demikopi.uiHandler;

import com.demikopi.dataAccess.KategoriDAO;
import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Kategori;
import com.demikopi.model.Menu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller khusus halaman Manajemen Menu.
 * Alurnya: load data dari DAO, tampilkan ke tabel, lalu form dipakai untuk tambah/edit/hapus.
 */
public class MenuManagementController extends AdminNavigationController {

    private static final String FILTER_SEMUA = "Semua";
    private static final String FILTER_TERSEDIA = "Tersedia";
    private static final String FILTER_HABIS = "Habis";
    private static final String FILTER_BESTSELLER = "Bestseller";

    private final MenuDAO menuDAO = new MenuDAO();
    private final KategoriDAO kategoriDAO = new KategoriDAO();
    private final ObservableList<Menu> semuaMenu = FXCollections.observableArrayList();
    private final ObservableList<Menu> menuTampil = FXCollections.observableArrayList();

    @FXML
    private TableView<Menu> menuTable;

    @FXML
    private TableColumn<Menu, String> namaMenuColumn;

    @FXML
    private TableColumn<Menu, String> kategoriColumn;

    @FXML
    private TableColumn<Menu, String> hargaColumn;

    @FXML
    private TableColumn<Menu, String> statusColumn;

    @FXML
    private TableColumn<Menu, String> bestsellerColumn;

    @FXML
    private TextField searchInput;

    @FXML
    private ComboBox<String> statusFilterInput;

    @FXML
    private TextField namaMenuInput;

    @FXML
    private ComboBox<String> kategoriInput;

    @FXML
    private TextField profilRasaInput;

    @FXML
    private TextField suhuSajianInput;

    @FXML
    private TextField hargaInput;

    @FXML
    private TextArea deskripsiInput;

    @FXML
    private TextField imagePathInput;

    @FXML
    private CheckBox bestsellerCheck;

    @FXML
    private CheckBox tersediaCheck;

    @FXML
    private Label formTitleLabel;

    @FXML
    private Label statusMenuLabel;

    @FXML
    private void initialize() {
        siapkanKolomTabelMenu();
        siapkanFilterMenu();
        loadKategoriMenu();
        loadMenuData();

        tersediaCheck.setSelected(true);
        menuTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, menuDipilih) -> {
            if (menuDipilih != null) {
                tampilkanMenuKeForm(menuDipilih);
            }
        });
    }

    @FXML
    private void handleSimpanMenu() {
        Menu menuDipilih = menuTable.getSelectionModel().getSelectedItem();
        Menu menuDariForm = bacaFormMenu(menuDipilih);

        if (menuDariForm == null) {
            return;
        }

        try {
            boolean berhasil = menuDipilih == null
                    ? menuDAO.tambahMenu(menuDariForm)
                    : menuDAO.updateMenu(menuDariForm);

            if (berhasil) {
                loadMenuData();
                bersihkanFormMenu();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data menu berhasil disimpan.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Gagal", "Data menu tidak berubah.");
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    @FXML
    private void handleHapusMenu() {
        Menu menuDipilih = menuTable.getSelectionModel().getSelectedItem();

        if (menuDipilih == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih menu", "Pilih satu menu yang ingin dihapus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Hapus Menu");
        confirm.setHeaderText(null);
        confirm.setContentText("Hapus menu " + menuDipilih.getNamaMenu() + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            if (menuDAO.hapusMenu(menuDipilih.getIdMenu())) {
                loadMenuData();
                bersihkanFormMenu();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Menu berhasil dihapus.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Gagal", "Menu tidak berhasil dihapus.");
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    @FXML
    private void handleBersihkanForm() {
        bersihkanFormMenu();
    }

    @FXML
    private void handleRefreshMenu() {
        loadMenuData();
    }

    @FXML
    private void handleCariMenu() {
        terapkanFilterMenu();
    }

    @FXML
    private void handleFilterMenu() {
        terapkanFilterMenu();
    }

    private void siapkanKolomTabelMenu() {
        namaMenuColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaMenu()));
        kategoriColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKategori()));
        hargaColumn.setCellValueFactory(data -> new SimpleStringProperty(formatRupiah(data.getValue().getHarga())));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isStatusTersedia() ? "Tersedia" : "Habis"));
        bestsellerColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isBestseller() ? "Ya" : "-"));

        menuTable.setItems(menuTampil);
    }

    private void siapkanFilterMenu() {
        statusFilterInput.setItems(FXCollections.observableArrayList(
                FILTER_SEMUA,
                FILTER_TERSEDIA,
                FILTER_HABIS,
                FILTER_BESTSELLER
        ));
        statusFilterInput.getSelectionModel().select(FILTER_SEMUA);
    }

    private void loadKategoriMenu() {
        try {
            kategoriInput.getItems().clear();
            for (Kategori kategori : kategoriDAO.getAllKategori()) {
                kategoriInput.getItems().add(kategori.getNamaKategori());
            }

            if (!kategoriInput.getItems().isEmpty()) {
                kategoriInput.getSelectionModel().selectFirst();
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    private void loadMenuData() {
        try {
            semuaMenu.setAll(menuDAO.getAllMenu());
            terapkanFilterMenu();
        } catch (IllegalStateException e) {
            semuaMenu.clear();
            menuTampil.clear();
            updateStatusMenu();
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    private void terapkanFilterMenu() {
        String keyword = ambilText(searchInput).toLowerCase();
        String filterStatus = statusFilterInput.getValue();

        menuTampil.clear();
        for (Menu menu : semuaMenu) {
            if (cocokDenganPencarian(menu, keyword) && cocokDenganFilterStatus(menu, filterStatus)) {
                menuTampil.add(menu);
            }
        }

        updateStatusMenu();
    }

    private boolean cocokDenganPencarian(Menu menu, String keyword) {
        if (keyword.isEmpty()) {
            return true;
        }

        return menu.getNamaMenu().toLowerCase().contains(keyword)
                || menu.getKategori().toLowerCase().contains(keyword)
                || aman(menu.getProfilRasa()).toLowerCase().contains(keyword)
                || aman(menu.getSuhuSajian()).toLowerCase().contains(keyword);
    }

    private boolean cocokDenganFilterStatus(Menu menu, String filterStatus) {
        if (filterStatus == null || FILTER_SEMUA.equals(filterStatus)) {
            return true;
        }

        if (FILTER_TERSEDIA.equals(filterStatus)) {
            return menu.isStatusTersedia();
        }

        if (FILTER_HABIS.equals(filterStatus)) {
            return !menu.isStatusTersedia();
        }

        if (FILTER_BESTSELLER.equals(filterStatus)) {
            return menu.isBestseller();
        }

        return true;
    }

    private void tampilkanMenuKeForm(Menu menu) {
        formTitleLabel.setText("Edit Menu");
        namaMenuInput.setText(menu.getNamaMenu());
        kategoriInput.getSelectionModel().select(menu.getKategori());
        profilRasaInput.setText(aman(menu.getProfilRasa()));
        suhuSajianInput.setText(aman(menu.getSuhuSajian()));
        hargaInput.setText(String.valueOf(menu.getHarga()));
        deskripsiInput.setText(aman(menu.getDeskripsiMenu()));
        imagePathInput.setText(aman(menu.getImagePath()));
        bestsellerCheck.setSelected(menu.isBestseller());
        tersediaCheck.setSelected(menu.isStatusTersedia());
    }

    private Menu bacaFormMenu(Menu menuDipilih) {
        String namaMenu = ambilText(namaMenuInput);
        String kategori = kategoriInput.getValue();
        String profilRasa = ambilText(profilRasaInput);
        String suhuSajian = ambilText(suhuSajianInput);
        String deskripsi = ambilText(deskripsiInput);
        String imagePath = kosongJadiNull(ambilText(imagePathInput));

        if (namaMenu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Nama menu wajib diisi.");
            return null;
        }

        if (kategori == null || kategori.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Kategori wajib dipilih.");
            return null;
        }

        int harga = bacaHarga();
        if (harga <= 0) {
            return null;
        }

        int idMenu = menuDipilih == null ? 0 : menuDipilih.getIdMenu();
        return new Menu(
                idMenu,
                kategori,
                namaMenu,
                profilRasa,
                suhuSajian,
                bestsellerCheck.isSelected(),
                harga,
                deskripsi,
                imagePath,
                tersediaCheck.isSelected()
        );
    }

    private int bacaHarga() {
        String hargaText = ambilText(hargaInput).replaceAll("[^0-9]", "");

        if (hargaText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data belum lengkap", "Harga wajib diisi.");
            return -1;
        }

        try {
            return Integer.parseInt(hargaText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Harga tidak valid", "Harga harus berupa angka.");
            return -1;
        }
    }

    private void bersihkanFormMenu() {
        menuTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Tambah Menu");
        namaMenuInput.clear();
        profilRasaInput.clear();
        suhuSajianInput.clear();
        hargaInput.clear();
        deskripsiInput.clear();
        imagePathInput.clear();
        bestsellerCheck.setSelected(false);
        tersediaCheck.setSelected(true);

        if (!kategoriInput.getItems().isEmpty()) {
            kategoriInput.getSelectionModel().selectFirst();
        }
    }

    private void updateStatusMenu() {
        statusMenuLabel.setText("Menampilkan " + menuTampil.size() + " dari " + semuaMenu.size() + " menu");
    }

    private String ambilText(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private String ambilText(TextArea field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private String aman(String value) {
        return value == null ? "" : value;
    }

    private String kosongJadiNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private String formatRupiah(int harga) {
        return "Rp " + String.format("%,d", harga).replace(',', '.');
    }
}
