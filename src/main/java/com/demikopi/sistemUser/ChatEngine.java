package com.demikopi.sistemUser;

import com.demikopi.dataAccess.FasilitasDAO;
import com.demikopi.dataAccess.InfoDAO;
import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;
import com.demikopi.model.Menu;
import com.demikopi.sistemUser.NLPService.Intent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ChatEngine menjadi pusat routing percakapan chatbot DEMIKOPI.
 * Input user diproses oleh NLPService, lalu diarahkan ke builder respons sesuai intent.
 */
public class ChatEngine {

    private MenuDAO menuDAO;
    private InfoDAO infoDAO;
    private FasilitasDAO fasilitasDAO;
    private RecomendationHandler rekomendasi;

    public ChatEngine() {
        this.menuDAO = new MenuDAO();
        this.infoDAO = new InfoDAO();
        this.fasilitasDAO = new FasilitasDAO();
        this.rekomendasi = new RecomendationHandler();
    }

    public String getResponse(String inputUser) {
        NLPService nlp = new NLPService(inputUser);
        Intent intent = nlp.detectIntent();
        String keyword = nlp.extractKeyword(intent);

        switch (intent) {
            case SALAM:
                return buildResponseSalam();
            case TANYA_MENU:
                return buildResponseSemuaMenu();
            case TANYA_KATEGORI:
                return buildResponseMenuKategori(keyword);
            case TANYA_REKOMENDASI:
                return rekomendasi.getRekomendasi(keyword);
            case TANYA_DETAIL_MENU:
                return buildResponseDetailMenu(keyword);
            case TANYA_JAM_BUKA:
                return buildResponseJamBuka();
            case TANYA_LOKASI:
                return buildResponseLokasi();
            case TANYA_FASILITAS:
                return buildResponseFasilitas();
            default:
                return buildResponseFallback();
        }
    }

    // Respons intent salam.
    private String buildResponseSalam() {
        return "Halo! Selamat datang di DEMIKOPI\n" +
               "Ada yang bisa aku bantu? Kamu bisa tanya soal menu, rekomendasi, " +
               "jam buka, lokasi, atau fasilitas kami!";
    }

    // Respons daftar semua menu.
    private String buildResponseSemuaMenu() {
        List<Menu> menus = menuDAO.getAllMenu();
        if (menus == null || menus.isEmpty()) {
            return "Maaf, belum ada menu yang tersedia saat ini.";
        }

        Map<String, List<Menu>> grouped = menus.stream()
                .collect(Collectors.groupingBy(Menu::getKategori));

        StringBuilder sb = new StringBuilder();
        sb.append("Berikut menu-menu kami:\n\n");
        for (Map.Entry<String, List<Menu>> set : grouped.entrySet()) {
            sb.append("[").append(set.getKey().toUpperCase()).append("]\n");
            int i = 1;
            for (Menu m : set.getValue()) {
                sb.append(i).append(". ").append(m.getNamaMenu()).append(" — Rp ").append(m.getHarga()).append("\n");
                i++;
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    // Respons menu berdasarkan kategori.
    private String buildResponseMenuKategori(String kategori) {
        if (kategori == null || kategori.isEmpty()) {
            return "Bisa tolong perjelas kategori apa yang kamu maksud? (contoh: kopi, makanan)";
        }

        List<Menu> menus = menuDAO.getMenuByKategori(kategori);
        if (menus == null || menus.isEmpty()) {
            return "Maaf, kategori " + kategori + " tidak kami temukan atau sedang kosong saat ini.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Berikut menu kategori ").append(kategori).append(" kami:\n\n");
        int count = 1;
        for (Menu m : menus) {
            sb.append(count).append(". ").append(m.getNamaMenu()).append(" — Rp ").append(m.getHarga()).append("\n");
            count++;
        }
        return sb.toString().trim();
    }

    // Respons detail menu.
    private String buildResponseDetailMenu(String namaMenu) {
        if (namaMenu == null || namaMenu.isEmpty()) {
            return "Bisa tolong sebutkan nama spesifik menunya yang ingin kamu ketahui?";
        }

        Menu menu = menuDAO.getMenuByName(namaMenu);
        if (menu == null) {
            return "Maaf, menu '" + namaMenu + "' tidak kami temukan. Coba nama lain.";
        }

        return "Detail Menu: " + menu.getNamaMenu() + "\n" +
               "Kategori: " + menu.getKategori() + "\n" +
               "Rasa: " + menu.getProfilRasa() + "\n" +
               "Sajian: " + menu.getSuhuSajian() + "\n" +
               "Harga: Rp " + menu.getHarga() + "\n\n" +
               menu.getDeskripsiMenu();
    }

    // Respons jam operasional.
    private String buildResponseJamBuka() {
        InfoKedai info = infoDAO.getInfo();
        if (info == null) {
            return "Maaf, informasi jam buka belum diatur.";
        }
        return "DEMIKOPI buka dengan jadwal berikut: \n" + info.getJamOperasional();
    }

    // Respons lokasi kedai.
    private String buildResponseLokasi() {
        InfoKedai info = infoDAO.getInfo();
        if (info == null) {
            return "Maaf, informasi lokasi belum diatur.";
        }
        return "Kami berlokasi di:\n" + info.getLokasi() + "\nKontak: " + info.getKontak();
    }

    // Respons fasilitas kedai.
    private String buildResponseFasilitas() {
        List<Fasilitas> fasilitas = fasilitasDAO.getAllFasilitas();
        if (fasilitas == null || fasilitas.isEmpty()) {
            return "Maaf, daftar fasilitas kedai belum diperbarui.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Fasilitas di DEMIKOPI:\n\n");
        int count = 1;
        for (Fasilitas f : fasilitas) {
            sb.append(count).append(". ").append(f.getNamaFasilitas()).append(" — ").append(f.getDeskripsiFasilitas()).append("\n\n");
            count++;
        }
        return sb.toString().trim();
    }

    private String buildResponseFallback() {
        return "Maaf, aku belum mengerti maksudmu.\n" +
               "Coba tanya soal: menu, rekomendasi, jam buka, lokasi, atau fasilitas.";
    }
}
