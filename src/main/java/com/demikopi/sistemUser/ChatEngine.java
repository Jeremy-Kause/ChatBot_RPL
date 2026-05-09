package com.demikopi.sistemUser;

import com.demikopi.dataAccess.FasilitasDAO;
import com.demikopi.dataAccess.InfoDAO;
import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;
import com.demikopi.model.Menu;
import com.demikopi.sistemUser.NLPService.Intent;

import java.text.NumberFormat;
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
            return buildResponseDetailMenuDenganGambar(menuLangsung);
        }

        String keyword = nlp.extractKeyword(intent);

        switch (intent) {
            case SALAM:
                return ChatResponse.text(buildResponseSalam());
            case TANYA_MENU:
                return ChatResponse.text(buildResponseSemuaMenu());
            case TANYA_KATEGORI:
                return ChatResponse.text(buildResponseMenuKategori(keyword));
            case TANYA_REKOMENDASI:
                return buildResponseRekomendasi(keyword);
            case TANYA_DETAIL_MENU:
                if (isKeywordKategori(keyword)) {
                    return ChatResponse.text(buildResponseMenuKategori(normalisasiKeywordKategori(keyword)));
                }
                return buildResponseDetailMenuDenganGambar(keyword);
            case TANYA_JAM_BUKA:
                return ChatResponse.text(buildResponseJamBuka());
            case TANYA_LOKASI:
                return ChatResponse.text(buildResponseLokasi());
            case TANYA_FASILITAS:
                return ChatResponse.text(buildResponseFasilitas());
            default:
                return ChatResponse.text(buildResponseFallback());
        }
    }

    private String buildResponseSalam() {
        return "Halo! Selamat datang di DEMIKOPI. Aku adalah asisten chatbot.\n" +
                "Kamu bisa tanya soal menu, rekomendasi, jam buka, lokasi, atau fasilitas kami.";
    }

    private String buildResponseSemuaMenu() {
        List<Menu> menus = ambilMenuTersedia();
        if (menus.isEmpty()) {
            return "Maaf, belum ada menu yang tersedia saat ini.";
        }

        Map<String, List<Menu>> grouped = menus.stream()
                .collect(Collectors.groupingBy(Menu::getKategori, LinkedHashMap::new, Collectors.toList()));

        StringBuilder sb = new StringBuilder("Berikut menu-menu kami:\n\n");
        for (Map.Entry<String, List<Menu>> set : grouped.entrySet()) {
            sb.append("[").append(set.getKey().toUpperCase()).append("]\n");
            int nomor = 1;
            for (Menu menu : set.getValue()) {
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
        return sb.toString().trim();
    }

    private String buildResponseMenuKategori(String kategori) {
        if (kategori == null || kategori.isEmpty()) {
            return "Bisa tolong perjelas kategori apa yang kamu maksud? Contoh: kopi, non-kopi, makanan, atau mix.";
        }

        List<Menu> menus = ambilMenuTersedia().stream()
                .filter(menu -> cocokKategori(menu.getKategori(), kategori))
                .collect(Collectors.toList());

        if (menus.isEmpty()) {
            return "Maaf, kategori " + kategori + " tidak kami temukan atau sedang kosong saat ini.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Berikut menu kategori ").append(kategori).append(" kami:\n\n");
        int nomor = 1;
        for (Menu menu : menus) {
            sb.append(nomor)
                    .append(". ")
                    .append(menu.getNamaMenu())
                    .append(" - ")
                    .append(formatRupiah(menu.getHarga()))
                    .append("\n");
            nomor++;
        }
        return sb.toString().trim();
    }

    private ChatResponse buildResponseRekomendasi(String keyword) {
        List<Menu> menuTersedia = ambilMenuTersedia();
        String teks = rekomendasi.getRekomendasi(keyword, menuTersedia, 3);
        List<ChatResponse.ChatImage> gambarRekomendasi = rekomendasi.getMenuRekomendasi(keyword, menuTersedia, 3).stream()
                .map(menu -> new ChatResponse.ChatImage(
                        menu.getNamaMenu(),
                        formatRupiah(menu.getHarga()),
                        buildPathGambarMenu(menu)
                ))
                .collect(Collectors.toList());

        if (gambarRekomendasi.isEmpty()) {
            return ChatResponse.text(teks);
        }
        return ChatResponse.withImages(teks, gambarRekomendasi);
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

    private ChatResponse buildResponseDetailMenuDenganGambar(String namaMenu) {
        if (namaMenu == null || namaMenu.isEmpty()) {
            return ChatResponse.text("Bisa tolong sebutkan nama spesifik menunya yang ingin kamu ketahui?");
        }

        Menu menu = cariMenuByNama(namaMenu);
        if (menu == null) {
            return ChatResponse.text("Maaf, menu '" + namaMenu + "' tidak kami temukan. Coba nama lain.");
        }

        return buildResponseDetailMenuDenganGambar(menu);
    }

    private ChatResponse buildResponseDetailMenuDenganGambar(Menu menu) {
        return ChatResponse.withImage(buildResponseDetailMenu(menu), buildPathGambarMenu(menu));
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

    private String buildResponseJamBuka() {
        InfoKedai info = ambilInfoKedai();
        if (info == null || info.getJamOperasional() == null || info.getJamOperasional().isBlank()) {
            return "Maaf, informasi jam buka belum diatur.";
        }
        return "DEMIKOPI buka dengan jadwal berikut:\n" + info.getJamOperasional();
    }

    private String buildResponseLokasi() {
        InfoKedai info = ambilInfoKedai();
        if (info == null || info.getLokasi() == null || info.getLokasi().isBlank()) {
            return "Maaf, informasi lokasi belum diatur.";
        }
        return "Kami berlokasi di:\n" + info.getLokasi() + "\nKontak: " + isiAtauStrip(info.getKontak());
    }

    private String buildResponseFasilitas() {
        List<Fasilitas> fasilitas = ambilFasilitas();
        if (fasilitas.isEmpty()) {
            return "Maaf, daftar fasilitas kedai belum diperbarui.";
        }

        StringBuilder sb = new StringBuilder("Fasilitas di DEMIKOPI:\n\n");
        int nomor = 1;
        for (Fasilitas item : fasilitas) {
            sb.append(nomor)
                    .append(". ")
                    .append(item.getNamaFasilitas())
                    .append(" - ")
                    .append(isiAtauStrip(item.getDeskripsiFasilitas()))
                    .append("\n\n");
            nomor++;
        }
        return sb.toString().trim();
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
            if (cocokNamaMenuDalamInput(teks, menu.getNamaMenu())) {
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

        for (Menu menu : ambilMenuTersedia()) {
            if (cocokNamaMenuDalamInput(target, menu.getNamaMenu())) {
                return menu;
            }
        }
        return null;
    }

    private boolean cocokNamaMenuDalamInput(String teks, String namaMenu) {
        String nama = normalisasi(namaMenu);
        if (nama.isEmpty()) {
            return false;
        }

        if (teks.equals(nama) || teks.contains(nama)) {
            return true;
        }

        List<String> tokenInput = tokenSignifikan(teks);
        List<String> tokenNama = tokenSignifikan(nama);
        if (tokenInput.isEmpty() || tokenNama.isEmpty()) {
            return false;
        }

        for (String token : tokenNama) {
            boolean adaYangCocok = tokenInput.stream()
                    .anyMatch(inputToken -> tokenCocok(token, inputToken));
            if (!adaYangCocok) {
                return false;
            }
        }
        return true;
    }

    private List<String> tokenSignifikan(String teks) {
        if (teks == null || teks.isBlank()) {
            return List.of();
        }

        return List.of(teks.split("\\s+")).stream()
                .filter(token -> token.length() >= 3)
                .filter(token -> !isStopwordNamaMenu(token))
                .collect(Collectors.toList());
    }

    private boolean isStopwordNamaMenu(String token) {
        return token.equals("menu")
                || token.equals("minuman")
                || token.equals("makanan")
                || token.equals("kopi")
                || token.equals("saya")
                || token.equals("mau")
                || token.equals("cek")
                || token.equals("lihat")
                || token.equals("cari")
                || token.equals("info")
                || token.equals("detail");
    }

    private boolean tokenCocok(String tokenMenu, String tokenInput) {
        if (tokenMenu.equals(tokenInput)) {
            return true;
        }

        if (tokenMenu.length() < 5 || tokenInput.length() < 5) {
            return false;
        }

        int batasJarak = tokenMenu.length() >= 8 || tokenInput.length() >= 8 ? 2 : 1;
        if (Math.abs(tokenMenu.length() - tokenInput.length()) > batasJarak) {
            return false;
        }

        return hitungJarakEdit(tokenMenu, tokenInput) <= batasJarak;
    }

    private int hitungJarakEdit(String kiri, String kanan) {
        int[][] jarak = new int[kiri.length() + 1][kanan.length() + 1];

        for (int i = 0; i <= kiri.length(); i++) {
            jarak[i][0] = i;
        }
        for (int j = 0; j <= kanan.length(); j++) {
            jarak[0][j] = j;
        }

        for (int i = 1; i <= kiri.length(); i++) {
            for (int j = 1; j <= kanan.length(); j++) {
                int biayaGanti = kiri.charAt(i - 1) == kanan.charAt(j - 1) ? 0 : 1;
                jarak[i][j] = Math.min(
                        Math.min(jarak[i - 1][j] + 1, jarak[i][j - 1] + 1),
                        jarak[i - 1][j - 1] + biayaGanti
                );
            }
        }

        return jarak[kiri.length()][kanan.length()];
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

    private String buildPathGambarMenu(Menu menu) {
        if (menu.getImagePath() != null && !menu.getImagePath().isBlank()) {
            return menu.getImagePath();
        }
        return "/com/demikopi/uiHandler/asset/menu/" + buatSlugMenu(menu.getNamaMenu()) + ".png";
    }

    private String buatSlugMenu(String namaMenu) {
        return normalisasi(namaMenu).replace(" ", "-");
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

    private boolean isKeywordKategori(String keyword) {
        return normalisasiKeywordKategori(keyword) != null;
    }

    private String normalisasiKeywordKategori(String keyword) {
        String value = normalisasi(keyword);
        if (value.equals("non kopi") || value.equals("non-kopi")) {
            return "non-kopi";
        }
        if (value.equals("kopi")
                || value.equals("makanan")
                || value.equals("minuman")
                || value.equals("mix")) {
            return value;
        }
        return null;
    }

    private String isiAtauStrip(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
