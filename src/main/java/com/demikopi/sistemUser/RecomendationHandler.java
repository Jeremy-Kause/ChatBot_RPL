package com.demikopi.sistemUser;

import com.demikopi.model.Menu;
import com.demikopi.sistemUser.NLPService.RecommendationQuery;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Menangani penyaringan rekomendasi menu berdasarkan kategori, rasa, suhu,
 * dan prioritas bestseller.
 */
public class RecomendationHandler {

    private static final String[] KATA_KUNCI_RASA = {"manis", "pahit", "asam", "gurih", "creamy", "fruity", "segar", "cokelat", "caramel", "nutty", "lembut"};
    private static final String[][] ALIAS_RASA = {
            {"manis", "manis", "sweet", "gula"},
            {"pahit", "pahit", "bold", "strong"},
            {"asam", "asam", "acid", "acidic"},
            {"gurih", "gurih", "savory", "asin"},
            {"creamy", "creamy", "cream", "milky", "susu"},
            {"fruity", "fruity", "buah"},
            {"segar", "segar", "fresh", "ringan"},
            {"cokelat", "cokelat", "chocolate"},
            {"caramel", "caramel", "karamel"},
            {"nutty", "nutty", "kacang"},
            {"lembut", "lembut", "mild"}
    };

    private final NumberFormat formatAngka = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    public String getRekomendasi(String keyword) {
        return getRekomendasi(keyword, UserFallbackData.getMenu());
    }

    public String getRekomendasi(String keyword, List<Menu> daftarMenu) {
        return getRekomendasi(parseContext(keyword), daftarMenu, 0);
    }

    public String getRekomendasi(String keyword, List<Menu> daftarMenu, int limit) {
        return getRekomendasi(parseContext(keyword), daftarMenu, limit);
    }

    public List<Menu> getMenuRekomendasi(String keyword, List<Menu> daftarMenu, int limit) {
        return getMenuRekomendasi(parseContext(keyword), daftarMenu, limit);
    }

    public String getRekomendasi(RecommendationQuery query, List<Menu> daftarMenu, int limit) {
        RecommendationQuery safeQuery = query == null ? RecommendationQuery.empty() : query;
        List<Menu> hasil = getMenuRekomendasi(safeQuery, daftarMenu, limit);

        if (hasil.isEmpty()) {
            return "Maaf, saat ini kami belum punya rekomendasi yang pas untuk pencarianmu.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(buildJudulRekomendasi(safeQuery, hasil.size())).append("\n\n");

        int nomor = 1;
        for (Menu menu : hasil) {
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

            sb.append("\n   Alasan: ").append(getAlasanRekomendasi(safeQuery, menu));
            if (menu.getDeskripsiMenu() != null && !menu.getDeskripsiMenu().isBlank()) {
                sb.append("\n   > ").append(menu.getDeskripsiMenu());
            }

            sb.append("\n\n");
            nomor++;
        }

        return sb.toString().trim();
    }

    public List<Menu> getMenuRekomendasi(RecommendationQuery query, List<Menu> daftarMenu, int limit) {
        List<Menu> hasil = rankMenu(query == null ? RecommendationQuery.empty() : query, daftarMenu).stream()
                .map(rankedMenu -> rankedMenu.menu)
                .collect(Collectors.toList());
        if (limit <= 0 || hasil.size() <= limit) {
            return hasil;
        }
        return hasil.stream().limit(limit).collect(Collectors.toList());
    }

    public String getAlasanRekomendasi(RecommendationQuery query, Menu menu) {
        if (menu == null) {
            return "";
        }

        RecommendationQuery safeQuery = query == null ? RecommendationQuery.empty() : query;
        List<String> alasan = new ArrayList<>();

        if (safeQuery.getKategori() != null && matchKategoriContext(menu.getKategori(), safeQuery.getKategori())) {
            alasan.add(formatAlasanKategori(safeQuery.getKategori()));
        }

        for (String rasa : safeQuery.getRasa()) {
            if (matchRasa(menu, rasa)) {
                alasan.add("punya profil rasa " + rasa);
            }
        }

        if (safeQuery.getSuhuSajian() != null && matchSuhu(menu, safeQuery.getSuhuSajian())) {
            if (safeQuery.getKonteksCuaca() != null) {
                alasan.add("bisa disajikan " + safeQuery.getSuhuSajian()
                        + " sehingga cocok untuk cuaca " + safeQuery.getKonteksCuaca());
            } else {
                alasan.add("tersedia sebagai sajian " + safeQuery.getSuhuSajian());
            }
        }

        for (String tujuan : safeQuery.getTujuanPengguna()) {
            if (matchTujuan(menu, tujuan)) {
                alasan.add(formatAlasanTujuan(tujuan));
            }
        }

        if (safeQuery.isBestseller() && menu.isBestseller()) {
            alasan.add("termasuk menu best seller");
        } else if (!safeQuery.isBestseller() && menu.isBestseller()) {
            alasan.add("punya prioritas best seller");
        }

        if (alasan.isEmpty()) {
            String deskripsi = menu.getDeskripsiMenu();
            if (deskripsi != null && !deskripsi.isBlank()) {
                return "paling mendekati preferensimu; " + deskripsi;
            }
            return "paling mendekati preferensimu berdasarkan data menu yang tersedia";
        }

        return gabungDenganDan(alasan);
    }

    private RecommendationQuery parseContext(String keyword) {
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

        return RecommendationQuery.fromLegacy(ctxKategori, ctxKriteria);
    }

    private List<RankedMenu> rankMenu(RecommendationQuery query, List<Menu> daftarMenu) {
        List<Menu> candidates = daftarMenu == null ? List.of() : daftarMenu;
        RecommendationQuery safeQuery = query == null ? RecommendationQuery.empty() : query;

        if (safeQuery.getKategori() != null) {
            String kategori = safeQuery.getKategori();
            candidates = candidates.stream()
                    .filter(menu -> matchKategoriContext(menu.getKategori(), kategori))
                    .collect(Collectors.toList());
        }

        if (!safeQuery.getRasa().isEmpty()) {
            List<Menu> rasaKetat = candidates.stream()
                    .filter(menu -> safeQuery.getRasa().stream().allMatch(rasa -> matchRasa(menu, rasa)))
                    .collect(Collectors.toList());
            if (!rasaKetat.isEmpty()) {
                candidates = rasaKetat;
            } else {
                List<Menu> rasaSebagian = candidates.stream()
                        .filter(menu -> safeQuery.getRasa().stream().anyMatch(rasa -> matchRasa(menu, rasa)))
                        .collect(Collectors.toList());
                if (!rasaSebagian.isEmpty()) {
                    candidates = rasaSebagian;
                }
            }
        }

        if (safeQuery.getSuhuSajian() != null) {
            String suhu = safeQuery.getSuhuSajian();
            List<Menu> suhuCocok = candidates.stream()
                    .filter(menu -> matchSuhu(menu, suhu))
                    .collect(Collectors.toList());
            if (!suhuCocok.isEmpty()) {
                candidates = suhuCocok;
            }
        }

        if (!safeQuery.getTujuanPengguna().isEmpty()) {
            List<Menu> tujuanCocok = candidates.stream()
                    .filter(menu -> matchSalahSatuTujuan(menu, safeQuery.getTujuanPengguna()))
                    .collect(Collectors.toList());
            if (!tujuanCocok.isEmpty()) {
                candidates = tujuanCocok;
            }
        }

        if (safeQuery.isBestseller()) {
            List<Menu> bestSeller = candidates.stream()
                    .filter(Menu::isBestseller)
                    .collect(Collectors.toList());
            if (!bestSeller.isEmpty()) {
                candidates = bestSeller;
            }
        }

        if (!safeQuery.hasPreference()) {
            List<Menu> bestSeller = candidates.stream()
                    .filter(Menu::isBestseller)
                    .collect(Collectors.toList());
            if (!bestSeller.isEmpty()) {
                candidates = bestSeller;
            }
        }

        return candidates.stream()
                .map(menu -> new RankedMenu(menu, hitungSkor(menu, safeQuery)))
                .sorted((m1, m2) -> {
                    int scoreCompare = Integer.compare(m2.score, m1.score);
                    if (scoreCompare != 0) {
                        return scoreCompare;
                    }
                    int bestSellerCompare = Boolean.compare(m2.menu.isBestseller(), m1.menu.isBestseller());
                    if (bestSellerCompare != 0) {
                        return bestSellerCompare;
                    }
                    return Integer.compare(m1.menu.getHarga(), m2.menu.getHarga());
                })
                .collect(Collectors.toList());
    }

    private int hitungSkor(Menu menu, RecommendationQuery query) {
        int skor = 0;

        if (query.getKategori() != null) {
            skor += matchKategoriContext(menu.getKategori(), query.getKategori()) ? 40 : -100;
        }

        for (String rasa : query.getRasa()) {
            skor += matchRasa(menu, rasa) ? 20 : -6;
        }

        if (query.getSuhuSajian() != null) {
            skor += matchSuhu(menu, query.getSuhuSajian()) ? 18 : -5;
        }

        if (query.getKonteksCuaca() != null && query.getSuhuSajian() != null) {
            skor += matchSuhu(menu, query.getSuhuSajian()) ? 8 : 0;
        }

        for (String tujuan : query.getTujuanPengguna()) {
            skor += hitungSkorTujuan(menu, tujuan);
        }

        if (query.isBestseller()) {
            skor += menu.isBestseller() ? 18 : -6;
        } else if (menu.isBestseller()) {
            skor += 4;
        }

        if (!query.hasPreference() && menu.isBestseller()) {
            skor += 20;
        }

        return skor;
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
            return kategori.equals("non-kopi");
        }
        if (ctxKategori.equals("mix")) {
            return kategori.equals("mix");
        }
        return true;
    }

    private boolean matchRasa(Menu menu, String rasa) {
        if (menu == null || menu.getProfilRasa() == null || rasa == null) {
            return false;
        }

        String profil = menu.getProfilRasa().toLowerCase(Locale.ROOT);
        for (String alias : aliasesUntukRasa(rasa)) {
            if (profil.contains(alias)) {
                return true;
            }
        }
        return false;
    }

    private List<String> aliasesUntukRasa(String rasa) {
        if (rasa == null || rasa.isBlank()) {
            return List.of();
        }

        String normalized = rasa.toLowerCase(Locale.ROOT);
        for (String[] aliasGroup : ALIAS_RASA) {
            if (aliasGroup[0].equals(normalized)) {
                return Arrays.asList(aliasGroup);
            }
        }
        if (Arrays.asList(KATA_KUNCI_RASA).contains(normalized)) {
            return List.of(normalized);
        }
        return List.of(normalized);
    }

    private boolean matchSuhu(Menu menu, String suhu) {
        if (menu == null || menu.getSuhuSajian() == null || suhu == null) {
            return false;
        }

        String target = suhu.toLowerCase(Locale.ROOT);
        if (target.equals("iced") || target.equals("ice") || target.equals("cold")) {
            target = "dingin";
        } else if (target.equals("hot") || target.equals("warm") || target.equals("hangat")) {
            target = "panas";
        }

        String suhuMenu = menu.getSuhuSajian().toLowerCase(Locale.ROOT);
        return suhuMenu.contains(target);
    }

    private boolean matchSalahSatuTujuan(Menu menu, List<String> tujuanPengguna) {
        if (tujuanPengguna == null || tujuanPengguna.isEmpty()) {
            return true;
        }

        for (String tujuan : tujuanPengguna) {
            if (matchTujuan(menu, tujuan)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchTujuan(Menu menu, String tujuan) {
        return hitungSkorTujuan(menu, tujuan) > 0;
    }

    private int hitungSkorTujuan(Menu menu, String tujuan) {
        if (menu == null || tujuan == null || tujuan.isBlank()) {
            return 0;
        }

        String normalizedTujuan = tujuan.toLowerCase(Locale.ROOT);
        String teksMenu = teksMenu(menu);
        int skor = 0;

        if (normalizedTujuan.equals("belajar") || normalizedTujuan.equals("kerja")) {
            if (kategoriBerbasisKopi(menu)) {
                skor += 18;
            }
            if (mengandungSalahSatu(teksMenu, "latte", "cappuccino", "americano", "v60", "pour over",
                    "cold brew", "kopi susu", "mocha", "hazelnut", "caramel", "espresso")) {
                skor += 8;
            }
            if (menu.isBestseller()) {
                skor += 4;
            }
            if (mengandungSalahSatu(teksMenu, "dessert", "ice cream", "affogato")) {
                skor -= 6;
            }
            return skor > 0 ? skor : -8;
        }

        if (normalizedTujuan.equals("melek")) {
            if (kategoriBerbasisKopi(menu)) {
                skor += 14;
            }
            if (mengandungSalahSatu(teksMenu, "espresso", "americano", "v60", "pour over", "cold brew",
                    "bold", "strong", "intense", "pahit")) {
                skor += 16;
            }
            if (mengandungSalahSatu(teksMenu, "ringan", "mild", "dessert", "ice cream")) {
                skor -= 5;
            }
            return skor > 0 ? skor : -10;
        }

        if (normalizedTujuan.equals("meeting")) {
            if (kategoriMinuman(menu)) {
                skor += 12;
            }
            if (mengandungSalahSatu(teksMenu, "latte", "cappuccino", "americano", "matcha", "mocha", "teh")) {
                skor += 8;
            }
            if (menu.isBestseller()) {
                skor += 4;
            }
            return skor > 0 ? skor : -6;
        }

        if (normalizedTujuan.equals("santai")) {
            if (mengandungSalahSatu(teksMenu, "creamy", "manis", "lembut", "cokelat", "caramel",
                    "dessert", "smooth", "flaky", "fudgy")) {
                skor += 18;
            }
            if (menu.isBestseller()) {
                skor += 4;
            }
            return skor > 0 ? skor : -4;
        }

        if (normalizedTujuan.equals("nongkrong")) {
            if (menu.isBestseller()) {
                skor += 12;
            }
            if (kategoriMakanan(menu)) {
                skor += 8;
            }
            if (mengandungSalahSatu(teksMenu, "sharing", "snack", "crispy", "cheese", "manis", "creamy")) {
                skor += 7;
            }
            return skor > 0 ? skor : -3;
        }

        return 0;
    }

    private String buildJudulRekomendasi(RecommendationQuery query, int jumlah) {
        RecommendationQuery safeQuery = query == null ? RecommendationQuery.empty() : query;
        if (!safeQuery.hasPreference()) {
            return "Ini " + jumlah + " rekomendasi menu best seller di DEMIKOPI:";
        }

        StringBuilder judul = new StringBuilder("Ini ");
        judul.append(jumlah).append(" rekomendasi ");
        judul.append(safeQuery.getKategori() == null ? "menu" : safeQuery.getKategori());

        List<String> kondisi = new ArrayList<>();
        if (!safeQuery.getRasa().isEmpty()) {
            kondisi.add("yang " + gabungDenganDan(safeQuery.getRasa()));
        }
        if (safeQuery.getKonteksCuaca() != null) {
            kondisi.add("cocok untuk cuaca " + safeQuery.getKonteksCuaca());
        } else if (safeQuery.getSuhuSajian() != null) {
            kondisi.add("disajikan " + safeQuery.getSuhuSajian());
        }
        if (!safeQuery.getTujuanPengguna().isEmpty()) {
            kondisi.add("cocok untuk " + gabungDenganDan(
                    safeQuery.getTujuanPengguna().stream()
                            .map(this::formatTujuan)
                            .collect(Collectors.toList())
            ));
        }
        if (safeQuery.isBestseller()) {
            kondisi.add("best seller");
        }

        if (!kondisi.isEmpty()) {
            judul.append(" ").append(gabungDenganDan(kondisi));
        }
        judul.append(":");
        return judul.toString();
    }

    private String formatAlasanKategori(String kategori) {
        if ("kopi".equals(kategori)) {
            return "masuk pilihan berbasis kopi";
        }
        if ("non-kopi".equals(kategori)) {
            return "masuk pilihan non-kopi";
        }
        if ("minuman".equals(kategori)) {
            return "termasuk menu minuman";
        }
        if ("makanan".equals(kategori)) {
            return "termasuk menu makanan";
        }
        return "sesuai kategori " + kategori;
    }

    private String formatAlasanTujuan(String tujuan) {
        if ("belajar".equals(tujuan)) {
            return "cocok untuk belajar atau mengerjakan tugas karena tetap memberi dorongan kopi";
        }
        if ("kerja".equals(tujuan)) {
            return "cocok untuk kerja atau fokus lama";
        }
        if ("melek".equals(tujuan)) {
            return "cocok saat butuh minuman yang lebih membantu tetap melek";
        }
        if ("meeting".equals(tujuan)) {
            return "cocok untuk meeting atau diskusi karena mudah dinikmati sambil ngobrol";
        }
        if ("santai".equals(tujuan)) {
            return "cocok untuk santai karena profil rasanya nyaman diminum pelan-pelan";
        }
        if ("nongkrong".equals(tujuan)) {
            return "cocok untuk nongkrong bersama teman";
        }
        return "sesuai tujuan " + tujuan;
    }

    private String formatTujuan(String tujuan) {
        if ("belajar".equals(tujuan)) {
            return "belajar/mengerjakan tugas";
        }
        if ("kerja".equals(tujuan)) {
            return "kerja atau fokus";
        }
        if ("melek".equals(tujuan)) {
            return "tetap melek";
        }
        if ("meeting".equals(tujuan)) {
            return "meeting/diskusi";
        }
        if ("santai".equals(tujuan)) {
            return "santai";
        }
        if ("nongkrong".equals(tujuan)) {
            return "nongkrong";
        }
        return tujuan;
    }

    private boolean kategoriBerbasisKopi(Menu menu) {
        String kategori = menu == null || menu.getKategori() == null ? "" : menu.getKategori().toLowerCase(Locale.ROOT);
        return kategori.equals("kopi") || kategori.equals("mix");
    }

    private boolean kategoriMinuman(Menu menu) {
        String kategori = menu == null || menu.getKategori() == null ? "" : menu.getKategori().toLowerCase(Locale.ROOT);
        return kategori.equals("kopi") || kategori.equals("non-kopi") || kategori.equals("mix");
    }

    private boolean kategoriMakanan(Menu menu) {
        String kategori = menu == null || menu.getKategori() == null ? "" : menu.getKategori().toLowerCase(Locale.ROOT);
        return kategori.equals("makanan");
    }

    private String teksMenu(Menu menu) {
        if (menu == null) {
            return "";
        }
        return (menu.getNamaMenu() + " "
                + menu.getKategori() + " "
                + menu.getProfilRasa() + " "
                + menu.getSuhuSajian() + " "
                + menu.getDeskripsiMenu()).toLowerCase(Locale.ROOT);
    }

    private boolean mengandungSalahSatu(String text, String... keywords) {
        if (text == null || keywords == null) {
            return false;
        }

        for (String keyword : keywords) {
            if (keyword != null && !keyword.isBlank() && text.contains(keyword.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private String gabungDenganDan(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        if (items.size() == 1) {
            return items.get(0);
        }
        if (items.size() == 2) {
            return items.get(0) + " dan " + items.get(1);
        }

        String awal = String.join(", ", items.subList(0, items.size() - 1));
        return awal + ", dan " + items.get(items.size() - 1);
    }

    private String formatRupiah(int harga) {
        return "Rp " + formatAngka.format(harga);
    }

    private static class RankedMenu {
        private final Menu menu;
        private final int score;

        private RankedMenu(Menu menu, int score) {
            this.menu = menu;
            this.score = score;
        }
    }
}
