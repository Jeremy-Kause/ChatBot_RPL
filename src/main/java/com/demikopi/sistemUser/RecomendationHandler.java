package com.demikopi.sistemUser;

import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Menu;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RecomendationHandler menangani penyaringan rekomendasi menu
 * berdasarkan kategori, rasa, suhu, dan prioritas bestseller.
 */
public class RecomendationHandler {

    private MenuDAO menuDAO;

    private static final String[] KATA_KUNCI_RASA = {"manis", "pahit", "asam", "gurih", "creamy", "fruity"};
    private static final String[] KATA_KUNCI_SUHU = {"panas", "dingin", "iced", "hot"};

    public RecomendationHandler() {
        this.menuDAO = new MenuDAO();
    }

    public String getRekomendasi(String keyword) {
        String ctxKategori = null;
        String ctxKriteria = null;

        // Keyword dari NLPService dikirim dengan format kategori|kriteria.
        if (keyword != null && keyword.contains("|")) {
            String[] parts = keyword.split("\\|");
            if (parts.length > 0 && !parts[0].isEmpty()) {
                ctxKategori = parts[0];
            }
            if (parts.length > 1 && !parts[1].isEmpty()) {
                ctxKriteria = parts[1];
            }
        } else if (keyword != null) {
            ctxKriteria = keyword;
        }

        return getRekomendasiFiltered(ctxKategori, ctxKriteria);
    }

    private String getRekomendasiFiltered(String ctxKategori, String ctxKriteria) {
        List<Menu> sourceList = menuDAO.getAllMenu();

        // Filter kategori.
        if (ctxKategori != null) {
            String finalCtxKategori = ctxKategori;
            sourceList = sourceList.stream()
                    .filter(m -> matchKategoriContext(m.getKategori(), finalCtxKategori))
                    .collect(Collectors.toList());
        }

        // Filter rasa atau suhu.
        if (ctxKriteria != null) {
            String finalCtx = ctxKriteria.toLowerCase();
            if (Arrays.asList(KATA_KUNCI_SUHU).contains(finalCtx)) {
                String tgtSuhu = finalCtx.equals("iced") ? "dingin" : finalCtx.equals("hot") ? "panas" : finalCtx;
                sourceList = sourceList.stream()
                        .filter(m -> m.getSuhuSajian() != null && m.getSuhuSajian().toLowerCase().contains(tgtSuhu))
                        .collect(Collectors.toList());
            } else if (Arrays.asList(KATA_KUNCI_RASA).contains(finalCtx)) {
                sourceList = sourceList.stream()
                        .filter(m -> m.getProfilRasa() != null && m.getProfilRasa().toLowerCase().contains(finalCtx))
                        .collect(Collectors.toList());
            }
        } else {
            sourceList = sourceList.stream().filter(Menu::isBestseller).collect(Collectors.toList());
        }

        if (sourceList.isEmpty()) {
            return "Maaf, saat ini kami belum punya rekomendasi yang pas untuk pencarianmu.";
        }

        // Bestseller ditampilkan lebih dulu.
        List<Menu> sortedList = sourceList.stream()
                .sorted((m1, m2) -> Boolean.compare(m2.isBestseller(), m1.isBestseller()))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        if (ctxKategori == null && ctxKriteria == null) {
            sb.append("Kalau kamu bingung, ini nih Menu Bestseller paling direkomendasikan di DEMIKOPI:\n\n");
        } else {
            sb.append("Tentu! Kalau kamu cari ");
            if (ctxKategori != null) {
                sb.append(ctxKategori).append(" ");
            }
            if (ctxKriteria != null) {
                sb.append("yang ").append(ctxKriteria).append(" ");
            }
            sb.append("aku sangat merekomendasikan:\n\n");
        }

        int count = 1;
        for (Menu menu : sortedList) {
            sb.append(count).append(". ").append(menu.getNamaMenu())
                    .append(" (").append(menu.getKategori()).append(") — Rp ").append(menu.getHarga());

            if (menu.isBestseller()) {
                sb.append(" [Bestseller]");
            }
            sb.append("\n   >> ").append(menu.getDeskripsiMenu()).append("\n\n");
            count++;
        }

        return sb.toString().trim();
    }

    private boolean matchKategoriContext(String dbKategori, String ctxKategori) {
        dbKategori = dbKategori.toLowerCase();
        if (ctxKategori.equals("makanan")) {
            return dbKategori.equals("makanan");
        } else if (ctxKategori.equals("minuman")) {
            return dbKategori.equals("kopi") || dbKategori.equals("non-kopi") || dbKategori.equals("mix");
        } else if (ctxKategori.equals("kopi")) {
            return dbKategori.equals("kopi") || dbKategori.equals("mix");
        } else if (ctxKategori.equals("non-kopi")) {
            return dbKategori.equals("non-kopi") || dbKategori.equals("mix");
        }
        return true;
    }
}
