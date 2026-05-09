package com.demikopi.sistemUser;

import com.demikopi.model.Menu;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Menangani penyaringan rekomendasi menu berdasarkan kategori, rasa, suhu,
 * dan prioritas bestseller.
 */
public class RecomendationHandler {

    private static final String[] KATA_KUNCI_RASA = {"manis", "pahit", "asam", "gurih", "creamy", "fruity"};
    private static final String[] KATA_KUNCI_SUHU = {"panas", "dingin", "iced", "hot"};
    private static final String[] KATA_KUNCI_BESTSELLER = {"bestseller", "best seller", "best-seller", "terlaris", "paling laku", "favorit", "unggulan"};

    private final NumberFormat formatAngka = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    public String getRekomendasi(String keyword) {
        return getRekomendasi(keyword, UserFallbackData.getMenu());
    }

    public String getRekomendasi(String keyword, List<Menu> daftarMenu) {
        RekomendasiContext context = parseContext(keyword);
        return getRekomendasiFiltered(context.kategori, context.kriteria, daftarMenu, 0);
    }

    public String getRekomendasi(String keyword, List<Menu> daftarMenu, int limit) {
        RekomendasiContext context = parseContext(keyword);
        return getRekomendasiFiltered(context.kategori, context.kriteria, daftarMenu, limit);
    }

    public List<Menu> getMenuRekomendasi(String keyword, List<Menu> daftarMenu, int limit) {
        RekomendasiContext context = parseContext(keyword);
        List<Menu> hasil = getMenuFiltered(context.kategori, context.kriteria, daftarMenu);
        if (limit <= 0 || hasil.size() <= limit) {
            return hasil;
        }
        return hasil.stream().limit(limit).collect(Collectors.toList());
    }

    private RekomendasiContext parseContext(String keyword) {
        String ctxKategori = null;
        String ctxKriteria = null;

        if (keyword != null && keyword.contains("|")) {
            String[] parts = keyword.split("\\|");
            if (parts.length > 0 && !parts[0].isEmpty()) {
                ctxKategori = parts[0];
            }
            if (parts.length > 1 && !parts[1].isEmpty()) {
                ctxKriteria = parts[1];
            }
        } else if (keyword != null && !keyword.isBlank()) {
            ctxKriteria = keyword;
        }

        return new RekomendasiContext(ctxKategori, ctxKriteria);
    }

    private String getRekomendasiFiltered(String ctxKategori, String ctxKriteria, List<Menu> daftarMenu, int limit) {
        List<Menu> sortedList = getMenuFiltered(ctxKategori, ctxKriteria, daftarMenu);
        if (limit > 0 && sortedList.size() > limit) {
            sortedList = sortedList.stream().limit(limit).collect(Collectors.toList());
        }

        if (sortedList.isEmpty()) {
            return "Maaf, saat ini kami belum punya rekomendasi yang pas untuk pencarianmu.";
        }

        StringBuilder sb = new StringBuilder();
        if (ctxKategori == null && (ctxKriteria == null || isKriteriaBestseller(ctxKriteria))) {
            sb.append(limit > 0 ? "Ini 3 rekomendasi menu best seller di DEMIKOPI:\n\n"
                    : "Ini rekomendasi menu best seller di DEMIKOPI:\n\n");
        } else {
            sb.append("Tentu! Kalau kamu cari ");
            if (ctxKategori != null) {
                sb.append(ctxKategori).append(" ");
            }
            if (ctxKriteria != null) {
                sb.append("yang ").append(formatKriteria(ctxKriteria)).append(" ");
            }
            sb.append("aku merekomendasikan:\n\n");
        }

        int nomor = 1;
        for (Menu menu : sortedList) {
            sb.append(nomor)
                    .append(". ")
                    .append(menu.getNamaMenu())
                    .append(" (")
                    .append(menu.getKategori())
                    .append(") - ")
                    .append(formatRupiah(menu.getHarga()));

            if (menu.isBestseller()) {
                sb.append(" [Best Seller]");
            }

            if (menu.getDeskripsiMenu() != null && !menu.getDeskripsiMenu().isBlank()) {
                sb.append("\n   > ").append(menu.getDeskripsiMenu());
            }

            sb.append("\n\n");
            nomor++;
        }

        return sb.toString().trim();
    }

    private List<Menu> getMenuFiltered(String ctxKategori, String ctxKriteria, List<Menu> daftarMenu) {
        List<Menu> sourceList = daftarMenu == null ? List.of() : daftarMenu;

        if (ctxKategori != null) {
            String finalCtxKategori = ctxKategori;
            sourceList = sourceList.stream()
                    .filter(menu -> matchKategoriContext(menu.getKategori(), finalCtxKategori))
                    .collect(Collectors.toList());
        }

        if (ctxKriteria != null) {
            String finalCtx = ctxKriteria.toLowerCase(Locale.ROOT);
            if (isKriteriaBestseller(finalCtx)) {
                sourceList = sourceList.stream()
                        .filter(Menu::isBestseller)
                        .collect(Collectors.toList());
            } else if (Arrays.asList(KATA_KUNCI_SUHU).contains(finalCtx)) {
                String targetSuhu = finalCtx.equals("iced") ? "dingin" : finalCtx.equals("hot") ? "panas" : finalCtx;
                sourceList = sourceList.stream()
                        .filter(menu -> menu.getSuhuSajian() != null
                                && menu.getSuhuSajian().toLowerCase(Locale.ROOT).contains(targetSuhu))
                        .collect(Collectors.toList());
            } else if (Arrays.asList(KATA_KUNCI_RASA).contains(finalCtx)) {
                sourceList = sourceList.stream()
                        .filter(menu -> menu.getProfilRasa() != null
                                && menu.getProfilRasa().toLowerCase(Locale.ROOT).contains(finalCtx))
                        .collect(Collectors.toList());
            }
        } else {
            sourceList = sourceList.stream()
                    .filter(Menu::isBestseller)
                    .collect(Collectors.toList());
        }

        return sourceList.stream()
                .sorted((m1, m2) -> Boolean.compare(m2.isBestseller(), m1.isBestseller()))
                .collect(Collectors.toList());
    }

    private boolean matchKategoriContext(String dbKategori, String ctxKategori) {
        if (dbKategori == null || ctxKategori == null) {
            return false;
        }

        String kategori = dbKategori.toLowerCase(Locale.ROOT);
        if (ctxKategori.equals("makanan")) {
            return kategori.equals("makanan");
        }
        if (ctxKategori.equals("minuman")) {
            return kategori.equals("kopi") || kategori.equals("non-kopi") || kategori.equals("mix");
        }
        if (ctxKategori.equals("kopi")) {
            return kategori.equals("kopi") || kategori.equals("mix");
        }
        if (ctxKategori.equals("non-kopi")) {
            return kategori.equals("non-kopi") || kategori.equals("mix");
        }
        return true;
    }

    private String formatRupiah(int harga) {
        return "Rp " + formatAngka.format(harga);
    }

    private boolean isKriteriaBestseller(String kriteria) {
        return kriteria != null && Arrays.asList(KATA_KUNCI_BESTSELLER).contains(kriteria.toLowerCase(Locale.ROOT));
    }

    private String formatKriteria(String kriteria) {
        return isKriteriaBestseller(kriteria) ? "best seller" : kriteria;
    }

    private static class RekomendasiContext {
        private final String kategori;
        private final String kriteria;

        private RekomendasiContext(String kategori, String kriteria) {
            this.kategori = kategori;
            this.kriteria = kriteria;
        }
    }
}
