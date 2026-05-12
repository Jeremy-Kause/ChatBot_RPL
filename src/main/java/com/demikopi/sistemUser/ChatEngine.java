package com.demikopi.sistemUser;

import com.demikopi.dataAccess.FasilitasDAO;
import com.demikopi.dataAccess.InfoDAO;
import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;
import com.demikopi.model.Menu;
import com.demikopi.sistemUser.ChatResponse.ChatBlock;
import com.demikopi.sistemUser.NLPService.Intent;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Pusat routing percakapan chatbot DemiKopi.
 * Data utama tetap dari database, tetapi user UI tetap aman saat database belum siap.
 */
public class ChatEngine {

    private static final int BATAS_REKOMENDASI = 3;
    private static final int BATAS_BESTSELLER = 10;

    private final MenuDAO menuDAO;
    private final InfoDAO infoDAO;
    private final FasilitasDAO fasilitasDAO;
    private final RecomendationHandler rekomendasi;
    private final NumberFormat formatAngka;

    public ChatEngine() {
        this.menuDAO = new MenuDAO();
        this.infoDAO = new InfoDAO();
        this.fasilitasDAO = new FasilitasDAO();
        this.rekomendasi = new RecomendationHandler();
        this.formatAngka = NumberFormat.getNumberInstance(new Locale("id", "ID"));
    }

    public String getResponse(String inputUser) {
        return getChatResponse(inputUser).getText();
    }

    public ChatResponse getChatResponse(String inputUser) {
        if (inputUser == null || inputUser.trim().isEmpty()) {
            return ChatResponse.text(buildResponseFallback());
        }

        NLPService nlp = new NLPService(inputUser);
        Intent intent = nlp.detectIntent();

        Menu menuLangsung = cariMenuLangsungJikaPerlu(inputUser, intent);
        if (menuLangsung != null) {
            return buildChatResponseDetailMenu(menuLangsung);
        }

        String keyword = nlp.extractKeyword(intent);

        switch (intent) {
            case SALAM:
                return buildChatResponseSalam();
            case TANYA_MENU:
                return buildChatResponseSemuaMenu();
            case TANYA_KATEGORI:
                return buildChatResponseMenuKategori(keyword);
            case TANYA_REKOMENDASI:
                return buildChatResponseRekomendasi(keyword);
            case TANYA_DETAIL_MENU:
                return buildChatResponseDetailMenu(keyword);
            case TANYA_JAM_BUKA:
                return buildChatResponseJamBuka();
            case TANYA_LOKASI:
                return buildChatResponseLokasi();
            case TANYA_FASILITAS:
                return buildChatResponseFasilitas();
            default:
                return buildChatResponseFallback();
        }
    }

    private ChatResponse buildChatResponseSalam() {
        return ChatResponse.formatted(buildResponseSalam(), List.of(
                ChatBlock.title("Halo! Selamat datang di DEMIKOPI"),
                ChatBlock.paragraph("Aku adalah asisten chatbot yang bisa membantu soal menu, rekomendasi, jam buka, lokasi, atau fasilitas kami.")
        ));
    }

    private String buildResponseSalam() {
        return "Halo! Selamat datang di DEMIKOPI. Aku adalah asisten chatbot.\n" +
                "Kamu bisa tanya soal menu, rekomendasi, jam buka, lokasi, atau fasilitas kami.";
    }

    private ChatResponse buildChatResponseSemuaMenu() {
        List<Menu> menus = ambilMenuTersedia();
        if (menus.isEmpty()) {
            return ChatResponse.formatted("Maaf, belum ada menu yang tersedia saat ini.",
                    List.of(ChatBlock.note("Maaf, belum ada menu yang tersedia saat ini.")));
        }

        Map<String, List<Menu>> grouped = menus.stream()
                .collect(Collectors.groupingBy(Menu::getKategori, LinkedHashMap::new, Collectors.toList()));

        List<ChatBlock> blocks = new ArrayList<>();
        blocks.add(ChatBlock.title("Berikut menu-menu kami"));

        StringBuilder sb = new StringBuilder("Berikut menu-menu kami:\n\n");
        for (Map.Entry<String, List<Menu>> set : grouped.entrySet()) {
            blocks.add(ChatBlock.section(set.getKey().toUpperCase(Locale.ROOT)));
            sb.append("[").append(set.getKey().toUpperCase(Locale.ROOT)).append("]\n");
            int nomor = 1;
            for (Menu menu : set.getValue()) {
                blocks.add(ChatBlock.numberedItem(nomor, menu.getNamaMenu(), formatRupiah(menu.getHarga())));
                sb.append(nomor)
                        .append(". ")
                        .append(menu.getNamaMenu())
                        .append(" - ")
                        .append(formatRupiah(menu.getHarga()))
                        .append("\n");
                nomor++;
            }
            sb.append("\n");
        }
        return ChatResponse.formatted(sb.toString().trim(), blocks);
    }

    private String buildResponseSemuaMenu() {
        return buildChatResponseSemuaMenu().getText();
    }

    private ChatResponse buildChatResponseMenuKategori(String kategori) {
        if (kategori == null || kategori.isEmpty()) {
            String text = "Bisa tolong perjelas kategori apa yang kamu maksud? Contoh: kopi, non-kopi, makanan, atau mix.";
            return ChatResponse.formatted(text, List.of(ChatBlock.note(text)));
        }

        List<Menu> menus = ambilMenuTersedia().stream()
                .filter(menu -> cocokKategori(menu.getKategori(), kategori))
                .collect(Collectors.toList());

        if (menus.isEmpty()) {
            String text = "Maaf, kategori " + kategori + " tidak kami temukan atau sedang kosong saat ini.";
            return ChatResponse.formatted(text, List.of(ChatBlock.note(text)));
        }

        List<ChatBlock> blocks = new ArrayList<>();
        blocks.add(ChatBlock.title("Menu kategori " + kategori));

        StringBuilder sb = new StringBuilder();
        sb.append("Berikut menu kategori ").append(kategori).append(" kami:\n\n");
        int nomor = 1;
        for (Menu menu : menus) {
            blocks.add(ChatBlock.numberedItem(nomor, menu.getNamaMenu(), formatRupiah(menu.getHarga())));
            sb.append(nomor)
                    .append(". ")
                    .append(menu.getNamaMenu())
                    .append(" - ")
                    .append(formatRupiah(menu.getHarga()))
                    .append("\n");
            nomor++;
        }
        return withMenuImages(sb.toString().trim(), blocks, menus);
    }

    private String buildResponseMenuKategori(String kategori) {
        return buildChatResponseMenuKategori(kategori).getText();
    }

    private String buildResponseDetailMenu(String namaMenu) {
        if (namaMenu == null || namaMenu.isEmpty()) {
            return "Bisa tolong sebutkan nama spesifik menunya yang ingin kamu ketahui?";
        }

        Menu menu = cariMenuByNama(namaMenu);
        if (menu == null) {
            return "Maaf, menu '" + namaMenu + "' tidak kami temukan. Coba nama lain.";
        }

        return buildResponseDetailMenu(menu);
    }

    private ChatResponse buildChatResponseDetailMenu(String namaMenu) {
        if (namaMenu == null || namaMenu.isEmpty()) {
            return ChatResponse.text("Bisa tolong sebutkan nama spesifik menunya yang ingin kamu ketahui?");
        }

        Menu menu = cariMenuByNama(namaMenu);
        if (menu == null) {
            return ChatResponse.text("Maaf, menu '" + namaMenu + "' tidak kami temukan. Coba nama lain.");
        }

        return buildChatResponseDetailMenu(menu);
    }

    private ChatResponse buildChatResponseDetailMenu(Menu menu) {
        String responseText = buildResponseDetailMenu(menu);
        List<ChatBlock> blocks = buildBlocksDetailMenu(menu);
        String imagePath = imagePathMenu(menu);
        if (imagePath == null || imagePath.isBlank()) {
            return ChatResponse.formatted(responseText, blocks);
        }
        return ChatResponse.withImage(responseText, imagePath, blocks);
    }

    private ChatResponse buildChatResponseRekomendasi(String keyword) {
        List<Menu> menus = ambilMenuTersedia();

        if (permintaanBestseller(keyword)) {
            return buildChatResponseBestseller(keyword, menus);
        }

        String responseText = rekomendasi.getRekomendasi(keyword, menus, BATAS_REKOMENDASI);
        List<Menu> menuRekomendasi = rekomendasi.getMenuRekomendasi(keyword, menus, BATAS_REKOMENDASI);
        List<ChatBlock> blocks = buildBlocksRekomendasi(responseText, menuRekomendasi);
        List<ChatResponse.ChatImage> images = menuRekomendasi.stream()
                .map(menu -> new ChatResponse.ChatImage(
                        menu.getNamaMenu(),
                        isiAtauStrip(menu.getKategori()) + " - " + formatRupiah(menu.getHarga()),
                        imagePathMenu(menu)
                ))
                .filter(image -> image.getImagePath() != null && !image.getImagePath().isBlank())
                .collect(Collectors.toList());

        if (images.isEmpty()) {
            return ChatResponse.formatted(responseText, blocks);
        }
        return ChatResponse.withImages(responseText, images, blocks);
    }

    private ChatResponse buildChatResponseBestseller(String keyword, List<Menu> menus) {
        List<Menu> menuBestseller = rekomendasi.getMenuRekomendasi(keyword, menus, BATAS_BESTSELLER);
        String responseText = "Top 10 best seller DEMIKOPI";
        List<ChatBlock> blocks = List.of(ChatBlock.title(responseText));
        List<ChatResponse.ChatImage> images = menuBestseller.stream()
                .map(menu -> new ChatResponse.ChatImage(
                        menu.getNamaMenu(),
                        isiAtauStrip(menu.getKategori()) + " - " + formatRupiah(menu.getHarga()),
                        imagePathMenu(menu)
                ))
                .filter(image -> image.getImagePath() != null && !image.getImagePath().isBlank())
                .collect(Collectors.toList());

        if (images.isEmpty()) {
            return ChatResponse.formatted(responseText, blocks);
        }
        return ChatResponse.withImages(responseText, images, blocks);
    }

    private String buildResponseDetailMenu(Menu menu) {
        StringBuilder sb = new StringBuilder();
        sb.append("Detail Menu: ").append(menu.getNamaMenu()).append("\n")
                .append("Kategori: ").append(isiAtauStrip(menu.getKategori())).append("\n")
                .append("Rasa: ").append(isiAtauStrip(menu.getProfilRasa())).append("\n")
                .append("Sajian: ").append(isiAtauStrip(menu.getSuhuSajian())).append("\n")
                .append("Harga: ").append(formatRupiah(menu.getHarga()));

        if (menu.getDeskripsiMenu() != null && !menu.getDeskripsiMenu().isBlank()) {
            sb.append("\n\n").append(menu.getDeskripsiMenu());
        }
        return sb.toString();
    }

    private List<ChatBlock> buildBlocksDetailMenu(Menu menu) {
        List<ChatBlock> blocks = new ArrayList<>();
        blocks.add(ChatBlock.title(menu.getNamaMenu()));
        blocks.add(ChatBlock.detailRow("Kategori", isiAtauStrip(menu.getKategori())));
        blocks.add(ChatBlock.detailRow("Rasa", isiAtauStrip(menu.getProfilRasa())));
        blocks.add(ChatBlock.detailRow("Sajian", isiAtauStrip(menu.getSuhuSajian())));
        blocks.add(ChatBlock.detailRow("Harga", formatRupiah(menu.getHarga())));
        if (menu.getDeskripsiMenu() != null && !menu.getDeskripsiMenu().isBlank()) {
            blocks.add(ChatBlock.paragraph(menu.getDeskripsiMenu()));
        }
        return blocks;
    }

    private List<ChatBlock> buildBlocksRekomendasi(String responseText, List<Menu> menus) {
        List<ChatBlock> blocks = new ArrayList<>();
        blocks.add(ChatBlock.title(ambilBarisPertama(responseText, "Rekomendasi menu untukmu")));
        if (menus == null || menus.isEmpty()) {
            return blocks;
        }

        int nomor = 1;
        for (Menu menu : menus) {
            String subtitle = isiAtauStrip(menu.getKategori()) + " - " + formatRupiah(menu.getHarga());
            if (menu.isBestseller()) {
                subtitle += " - Best Seller";
            }
            blocks.add(ChatBlock.numberedItem(nomor, menu.getNamaMenu(), subtitle));
            nomor++;
        }
        return blocks;
    }

    private ChatResponse buildChatResponseJamBuka() {
        String text = buildResponseJamBuka();
        if (text.startsWith("Maaf")) {
            return ChatResponse.formatted(text, List.of(ChatBlock.note(text)));
        }

        InfoKedai info = ambilInfoKedai();
        List<ChatBlock> blocks = new ArrayList<>();
        blocks.add(ChatBlock.title("Jam buka DEMIKOPI"));
        blocks.add(ChatBlock.section("Jadwal"));

        for (String jadwal : pecahJadwalOperasional(info == null ? null : info.getJamOperasional())) {
            blocks.add(buatBlockJadwal(jadwal));
        }

        return ChatResponse.formatted(text, blocks);
    }

    private String buildResponseJamBuka() {
        InfoKedai info = ambilInfoKedai();
        if (info == null || info.getJamOperasional() == null || info.getJamOperasional().isBlank()) {
            return "Maaf, informasi jam buka belum diatur.";
        }

        StringBuilder sb = new StringBuilder("DEMIKOPI buka dengan jadwal berikut:\n");
        for (String jadwal : pecahJadwalOperasional(info.getJamOperasional())) {
            sb.append(jadwal).append("\n");
        }
        return sb.toString().trim();
    }

    private List<String> pecahJadwalOperasional(String jamOperasional) {
        if (jamOperasional == null || jamOperasional.isBlank()) {
            return List.of("-");
        }

        List<String> hasil = new ArrayList<>();
        String[] jadwalParts = jamOperasional.split("\\|");
        for (String jadwal : jadwalParts) {
            String jadwalBersih = jadwal.trim();
            if (!jadwalBersih.isEmpty()) {
                hasil.add(jadwalBersih);
            }
        }

        return hasil.isEmpty() ? List.of("-") : hasil;
    }

    private ChatBlock buatBlockJadwal(String jadwal) {
        if (jadwal == null || jadwal.isBlank()) {
            return ChatBlock.scheduleRow("-", "");
        }

        int pemisah = jadwal.indexOf(":");
        if (pemisah <= 0 || pemisah == jadwal.length() - 1) {
            return ChatBlock.scheduleRow(jadwal.trim(), "");
        }

        String hari = jadwal.substring(0, pemisah).trim();
        String jam = jadwal.substring(pemisah + 1).trim();
        return ChatBlock.scheduleRow(hari, jam);
    }

    private ChatResponse buildChatResponseLokasi() {
        String text = buildResponseLokasi();
        if (text.startsWith("Maaf")) {
            return ChatResponse.formatted(text, List.of(ChatBlock.note(text)));
        }

        InfoKedai info = ambilInfoKedai();
        return ChatResponse.formatted(text, List.of(
                ChatBlock.title("Lokasi DEMIKOPI"),
                ChatBlock.paragraph(info == null ? "-" : isiAtauStrip(info.getLokasi())),
                ChatBlock.detailRow("Kontak", info == null ? "-" : isiAtauStrip(info.getKontak()))
        ));
    }

    private String buildResponseLokasi() {
        InfoKedai info = ambilInfoKedai();
        if (info == null || info.getLokasi() == null || info.getLokasi().isBlank()) {
            return "Maaf, informasi lokasi belum diatur.";
        }
        return "Kami berlokasi di:\n" + info.getLokasi() + "\nKontak: " + isiAtauStrip(info.getKontak());
    }

    private ChatResponse buildChatResponseFasilitas() {
        List<Fasilitas> fasilitas = ambilFasilitas();
        if (fasilitas.isEmpty()) {
            String text = "Maaf, daftar fasilitas kedai belum diperbarui.";
            return ChatResponse.formatted(text, List.of(ChatBlock.note(text)));
        }

        List<ChatBlock> blocks = new ArrayList<>();
        blocks.add(ChatBlock.title("Fasilitas di DEMIKOPI"));

        StringBuilder sb = new StringBuilder("Fasilitas di DEMIKOPI:\n\n");
        int nomor = 1;
        for (Fasilitas item : fasilitas) {
            blocks.add(ChatBlock.numberedDetailItem(nomor, item.getNamaFasilitas(), isiAtauStrip(item.getDeskripsiFasilitas())));
            sb.append(nomor)
                    .append(". ")
                    .append(item.getNamaFasilitas())
                    .append(" - ")
                    .append(isiAtauStrip(item.getDeskripsiFasilitas()))
                    .append("\n\n");
            nomor++;
        }
        return ChatResponse.formatted(sb.toString().trim(), blocks);
    }

    private String buildResponseFasilitas() {
        return buildChatResponseFasilitas().getText();
    }

    private ChatResponse buildChatResponseFallback() {
        return ChatResponse.formatted(buildResponseFallback(), List.of(
                ChatBlock.note("Maaf, aku belum mengerti maksudmu."),
                ChatBlock.paragraph("Coba tanya soal: menu, rekomendasi, jam buka, lokasi, atau fasilitas.")
        ));
    }

    private String buildResponseFallback() {
        return "Maaf, aku belum mengerti maksudmu.\n" +
                "Coba tanya soal: menu, rekomendasi, jam buka, lokasi, atau fasilitas.";
    }

    private List<Menu> ambilMenuTersedia() {
        try {
            List<Menu> menus = menuDAO.getMenuTersedia();
            return menus == null ? List.of() : menus;
        } catch (RuntimeException e) {
            return UserFallbackData.getMenu();
        }
    }

    private InfoKedai ambilInfoKedai() {
        try {
            return infoDAO.getInfo();
        } catch (RuntimeException e) {
            return UserFallbackData.getInfo();
        }
    }

    private List<Fasilitas> ambilFasilitas() {
        try {
            List<Fasilitas> fasilitas = fasilitasDAO.getAllFasilitas();
            return fasilitas == null ? List.of() : fasilitas;
        } catch (RuntimeException e) {
            return UserFallbackData.getFasilitas();
        }
    }

    private Menu cariMenuDariInput(String inputUser) {
        String teks = normalisasi(inputUser);
        if (teks.isEmpty()) {
            return null;
        }

        for (Menu menu : ambilMenuTersedia()) {
            String namaMenu = normalisasi(menu.getNamaMenu());
            if (!namaMenu.isEmpty() && (teks.equals(namaMenu) || teks.contains(namaMenu))) {
                return menu;
            }
        }
        return null;
    }

    private Menu cariMenuLangsungJikaPerlu(String inputUser, Intent intent) {
        if (intent == Intent.TANYA_DETAIL_MENU
                || intent == Intent.TANYA_KATEGORI
                || intent == Intent.TANYA_REKOMENDASI
                || intent == Intent.TIDAK_DIKENAL) {
            return cariMenuDariInput(inputUser);
        }
        return null;
    }

    private Menu cariMenuByNama(String namaMenu) {
        String target = normalisasi(namaMenu);
        if (target.isEmpty()) {
            return null;
        }

        for (Menu menu : ambilMenuTersedia()) {
            String nama = normalisasi(menu.getNamaMenu());
            if (nama.equals(target)) {
                return menu;
            }
        }

        for (Menu menu : ambilMenuTersedia()) {
            String nama = normalisasi(menu.getNamaMenu());
            if (target.contains(nama) || (target.length() >= 5 && nama.contains(target))) {
                return menu;
            }
        }
        return null;
    }

    private String normalisasi(String teks) {
        if (teks == null) {
            return "";
        }
        return teks.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String formatRupiah(int harga) {
        return "Rp " + formatAngka.format(harga);
    }

    private boolean cocokKategori(String kategoriMenu, String kategoriDicari) {
        if (kategoriMenu == null || kategoriDicari == null) {
            return false;
        }

        String kategori = kategoriMenu.toLowerCase(Locale.ROOT);
        String dicari = kategoriDicari.toLowerCase(Locale.ROOT);
        if (dicari.equals("minuman")) {
            return kategori.equals("kopi") || kategori.equals("non-kopi") || kategori.equals("mix");
        }
        return kategori.equals(dicari);
    }

    private boolean permintaanBestseller(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }

        String keywordLower = keyword.toLowerCase(Locale.ROOT);
        return keywordLower.contains("bestseller")
                || keywordLower.contains("best seller")
                || keywordLower.contains("best-seller")
                || keywordLower.contains("terlaris")
                || keywordLower.contains("paling laku")
                || keywordLower.contains("favorit")
                || keywordLower.contains("unggulan");
    }

    private String isiAtauStrip(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String ambilBarisPertama(String text, String fallback) {
        if (text == null || text.isBlank()) {
            return fallback;
        }

        String firstLine = text.split("\\R", 2)[0].trim();
        return firstLine.isEmpty() ? fallback : firstLine.replace(":", "");
    }

    private ChatResponse withMenuImages(String responseText, List<ChatBlock> blocks, List<Menu> menus) {
        List<ChatResponse.ChatImage> images = menus.stream()
                .map(menu -> new ChatResponse.ChatImage(
                        menu.getNamaMenu(),
                        isiAtauStrip(menu.getKategori()) + " - " + formatRupiah(menu.getHarga()),
                        imagePathMenu(menu)
                ))
                .filter(image -> image.getImagePath() != null && !image.getImagePath().isBlank())
                .collect(Collectors.toList());

        if (images.isEmpty()) {
            return ChatResponse.formatted(responseText, blocks);
        }
        return ChatResponse.withImages(responseText, images, blocks);
    }

    private String imagePathMenu(Menu menu) {
        if (menu == null) {
            return null;
        }
        if (menu.getImagePath() != null && !menu.getImagePath().isBlank()) {
            return menu.getImagePath();
        }
        return "asset/menu/" + slugNamaMenu(menu.getNamaMenu()) + ".jpg";
    }

    private String slugNamaMenu(String namaMenu) {
        if (namaMenu == null || namaMenu.isBlank()) {
            return "";
        }
        return namaMenu.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
