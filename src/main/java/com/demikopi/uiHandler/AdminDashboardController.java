package com.demikopi.uiHandler;

import com.demikopi.dataAccess.FasilitasDAO;
import com.demikopi.dataAccess.InfoDAO;
import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;
import com.demikopi.model.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller khusus halaman dashboard admin.
 */
public class AdminDashboardController extends AdminNavigationController {

    private final MenuDAO menuDAO = new MenuDAO();
    private final FasilitasDAO fasilitasDAO = new FasilitasDAO();
    private final InfoDAO infoDAO = new InfoDAO();
    private final NumberFormat formatAngka = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    @FXML
    private Label lblTanggal;

    @FXML
    private Label totalMenuLabel;

    @FXML
    private Label menuTersediaLabel;

    @FXML
    private Label bestsellerLabel;

    @FXML
    private Label fasilitasLabel;

    @FXML
    private Label databaseStatusLabel;

    @FXML
    private Label topMenuLabel;

    @FXML
    private Label topKategoriLabel;

    @FXML
    private Label infoKedaiStatusLabel;

    @FXML
    private Label agendaLabel;

    @FXML
    private VBox ringkasanOperasionalList;

    @FXML
    private void initialize() {
        if (lblTanggal != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", new Locale("id", "ID"));
            lblTanggal.setText(LocalDate.now().format(formatter));
        }

        loadDashboardData();
    }

    @FXML
    private void handleRefreshDashboard() {
        loadDashboardData();
    }

    private void loadDashboardData() {
        try {
            List<Menu> menus = menuDAO.getAllMenu();
            List<Fasilitas> fasilitas = fasilitasDAO.getAllFasilitas();
            InfoKedai infoKedai = infoDAO.getInfo();

            int totalMenu = menus.size();
            int menuTersedia = hitungMenuTersedia(menus);
            int bestseller = hitungBestseller(menus);
            int menuHabis = totalMenu - menuTersedia;

            totalMenuLabel.setText(formatAngka.format(totalMenu));
            menuTersediaLabel.setText(formatAngka.format(menuTersedia));
            bestsellerLabel.setText(formatAngka.format(bestseller));
            fasilitasLabel.setText(formatAngka.format(fasilitas.size()));
            databaseStatusLabel.setText("Terkoneksi");
            topMenuLabel.setText(cariTopMenu(menus));
            topKategoriLabel.setText(cariTopKategori(menus));
            infoKedaiStatusLabel.setText(infoKedaiLengkap(infoKedai) ? "Lengkap" : "Perlu data");
            agendaLabel.setText(menuHabis > 0 ? menuHabis + " menu perlu dicek" : "Operasional normal");

            isiRingkasanOperasional(totalMenu, menuTersedia, bestseller, fasilitas.size(), infoKedai);
        } catch (IllegalStateException e) {
            resetDashboardData();
            showAlert(Alert.AlertType.ERROR, "Database bermasalah", e.getMessage());
        }
    }

    private int hitungMenuTersedia(List<Menu> menus) {
        int total = 0;
        for (Menu menu : menus) {
            if (menu.isStatusTersedia()) {
                total++;
            }
        }
        return total;
    }

    private int hitungBestseller(List<Menu> menus) {
        int total = 0;
        for (Menu menu : menus) {
            if (menu.isBestseller()) {
                total++;
            }
        }
        return total;
    }

    private String cariTopMenu(List<Menu> menus) {
        for (Menu menu : menus) {
            if (menu.isBestseller() && menu.isStatusTersedia()) {
                return menu.getNamaMenu();
            }
        }

        for (Menu menu : menus) {
            if (menu.isStatusTersedia()) {
                return menu.getNamaMenu();
            }
        }

        return "-";
    }

    private String cariTopKategori(List<Menu> menus) {
        Map<String, Integer> jumlahPerKategori = new LinkedHashMap<>();
        for (Menu menu : menus) {
            String kategori = isiAtau(menu.getKategori(), "Tanpa kategori");
            jumlahPerKategori.put(kategori, jumlahPerKategori.getOrDefault(kategori, 0) + 1);
        }

        String kategoriTeratas = "-";
        int jumlahTeratas = 0;
        for (Map.Entry<String, Integer> kategori : jumlahPerKategori.entrySet()) {
            if (kategori.getValue() > jumlahTeratas) {
                kategoriTeratas = kategori.getKey();
                jumlahTeratas = kategori.getValue();
            }
        }

        return jumlahTeratas == 0 ? "-" : kategoriTeratas + " (" + jumlahTeratas + ")";
    }

    private void isiRingkasanOperasional(int totalMenu, int menuTersedia, int bestseller, int totalFasilitas, InfoKedai infoKedai) {
        ringkasanOperasionalList.getChildren().clear();
        ringkasanOperasionalList.getChildren().add(buatRingkasanRow(
                "Menu tersedia",
                menuTersedia + " dari " + totalMenu + " menu dapat ditampilkan ke user",
                menuTersedia > 0 ? "Aktif" : "Kosong",
                menuTersedia > 0 ? "pill-green" : "pill-orange"
        ));
        ringkasanOperasionalList.getChildren().add(buatRingkasanRow(
                "Bestseller",
                bestseller + " menu ditandai sebagai rekomendasi utama",
                bestseller > 0 ? "Siap" : "Cek",
                bestseller > 0 ? "pill-brown" : "pill-orange"
        ));
        ringkasanOperasionalList.getChildren().add(buatRingkasanRow(
                "Fasilitas",
                totalFasilitas + " fasilitas tersedia untuk jawaban chatbot",
                totalFasilitas > 0 ? "Aktif" : "Kosong",
                totalFasilitas > 0 ? "pill-green" : "pill-orange"
        ));
        ringkasanOperasionalList.getChildren().add(buatRingkasanRow(
                "Info kedai",
                infoKedaiLengkap(infoKedai) ? "Jam operasional, lokasi, dan kontak sudah terisi" : "Lengkapi pengaturan info kedai",
                infoKedaiLengkap(infoKedai) ? "Lengkap" : "Perlu data",
                infoKedaiLengkap(infoKedai) ? "pill-green" : "pill-orange"
        ));
    }

    private HBox buatRingkasanRow(String bagian, String keterangan, String status, String statusClass) {
        Label bagianLabel = new Label(bagian);
        bagianLabel.getStyleClass().addAll("row-title", "flex-label");

        Label keteranganLabel = new Label(keterangan);
        keteranganLabel.setWrapText(true);
        keteranganLabel.getStyleClass().addAll("row-text", "list-wide");

        Label statusLabel = new Label(status);
        statusLabel.getStyleClass().addAll("row-pill", statusClass);

        HBox row = new HBox(bagianLabel, keteranganLabel, statusLabel);
        row.getStyleClass().add("list-row");
        return row;
    }

    private boolean infoKedaiLengkap(InfoKedai infoKedai) {
        return infoKedai != null
                && !kosong(infoKedai.getJamOperasional())
                && !kosong(infoKedai.getLokasi())
                && !kosong(infoKedai.getKontak());
    }

    private void resetDashboardData() {
        totalMenuLabel.setText("--");
        menuTersediaLabel.setText("--");
        bestsellerLabel.setText("--");
        fasilitasLabel.setText("--");
        databaseStatusLabel.setText("Tidak terkoneksi");
        topMenuLabel.setText("--");
        topKategoriLabel.setText("--");
        infoKedaiStatusLabel.setText("--");
        agendaLabel.setText("--");
        ringkasanOperasionalList.getChildren().clear();
        ringkasanOperasionalList.getChildren().add(buatRingkasanRow(
                "Database",
                "Data dashboard belum bisa dimuat",
                "Error",
                "pill-orange"
        ));
    }

    private boolean kosong(String value) {
        return value == null || value.isBlank();
    }

    private String isiAtau(String value, String fallback) {
        return kosong(value) ? fallback : value;
    }
}
