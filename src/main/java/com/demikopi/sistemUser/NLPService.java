package com.demikopi.sistemUser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * NLPService memproses input user menjadi intent dan keyword.
 * Algoritma masih rule-based, yaitu pencocokan kata kunci dari teks yang sudah dibersihkan.
 */
public class NLPService {

    private static final String[] KAMUS_SALAM = {
            "halo", "hi", "hey", "hai", "hello", "hallo", "pagi", "siang", "sore", "malam",
            "selamat", "permisi", "assalamualaikum", "assalamu alaikum", "assalam", "morning"
    };
    private static final String[] KAMUS_JAM = {
            "jam", "buka", "tutup", "operasional", "jadwal", "jam buka", "jam tutup",
            "open", "close", "opening", "closing", "masih buka", "sudah tutup", "kapan buka"
    };
    private static final String[] KAMUS_LOKASI = {
            "lokasi", "alamat", "di mana", "dimana", "tempat", "posisi", "maps", "map",
            "google maps", "arah", "rute", "cabang", "patokan"
    };
    private static final String[] KAMUS_FASILITAS = {
            "fasilitas", "wifi", "wi-fi", "parkir", "colokan", "stop kontak", "charger",
            "charging", "mushola", "musholla", "toilet", "kamar mandi", "ac", "indoor",
            "outdoor", "smoking", "smoking area"
    };
    private static final String[] KAMUS_DETAIL = {
            "tentang", "detail", "info", "informasi", "deskripsi", "harga", "harganya",
            "berapa", "berapaan", "komposisi", "bahan", "isi", "ukuran", "porsi"
    };
    private static final String[] KAMUS_REKOMENDASI = {
            "rekomen", "rekomendasi", "saran", "enak", "bagus", "favorit",
            "bestseller", "best seller", "best-seller", "terlaris", "paling laku", "unggulan",
            "recommended", "recommend", "rekomendasiin", "saranin", "pilihan", "pilihin",
            "cocok", "mantap", "favoritnya", "andalan", "populer", "viral", "hits",
            "signature", "spesial", "best", "top", "paling enak", "yang enak"
    };
    private static final String[] KAMUS_BESTSELLER = {
            "bestseller", "best seller", "best-seller", "terlaris", "paling laku", "favorit",
            "favoritnya", "unggulan", "andalan", "populer", "viral", "hits", "signature",
            "best", "top", "paling enak", "yang enak"
    };
    private static final String[] KAMUS_RASA = {
            "manis", "sweet", "gula", "pahit", "bold", "strong", "asam", "acid", "acidic",
            "gurih", "savory", "asin", "creamy", "cream", "milky", "susu", "fruity",
            "buah", "segar", "fresh", "ringan", "cokelat", "chocolate", "caramel", "karamel",
            "nutty", "kacang", "lembut", "mild"
    };
    private static final String[] KAMUS_SUHU = {
            "panas", "hangat", "hot", "warm", "dingin", "iced", "ice", "es", "cold",
            "sejuk", "gerah", "hujan", "mendung"
    };
    private static final String[] KAMUS_MENU = {
            "menu", "daftar", "ada apa aja", "list", "katalog", "pilihan menu", "menunya",
            "jual apa", "tersedia apa"
    };
    private static final String[] KAMUS_KATEGORI = {
            "non-kopi", "non kopi", "tanpa kopi", "selain kopi", "kopi", "coffee", "makanan",
            "cemilan", "snack", "makan", "minuman", "drink", "beverage", "mix"
    };

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
    private static final String[][] ALIAS_SUHU = {
            {"panas", "panas", "hangat", "hot", "warm"},
            {"dingin", "dingin", "iced", "ice", "es", "cold", "sejuk", "gerah"}
    };

    private String inputUser;
    private String processedInput;

    public NLPService(String inputUser) {
        this.inputUser = inputUser;
        this.processedInput = preprocess();
    }

    public enum Intent {
        TANYA_MENU,
        TANYA_KATEGORI,
        TANYA_REKOMENDASI,
        TANYA_DETAIL_MENU,
        TANYA_JAM_BUKA,
        TANYA_LOKASI,
        TANYA_FASILITAS,
        SALAM,
        TIDAK_DIKENAL
    }

    public static class RecommendationQuery {
        private final String kategori;
        private final List<String> rasa;
        private final String suhuSajian;
        private final String konteksCuaca;
        private final boolean bestseller;
        private final String sumberInput;

        public RecommendationQuery(String kategori, List<String> rasa, String suhuSajian,
                                   String konteksCuaca, boolean bestseller, String sumberInput) {
            this.kategori = kosongJadiNull(kategori);
            this.rasa = rasa == null ? List.of() : List.copyOf(rasa);
            this.suhuSajian = kosongJadiNull(suhuSajian);
            this.konteksCuaca = kosongJadiNull(konteksCuaca);
            this.bestseller = bestseller;
            this.sumberInput = sumberInput == null ? "" : sumberInput;
        }

        public static RecommendationQuery empty() {
            return new RecommendationQuery(null, List.of(), null, null, false, "");
        }

        public static RecommendationQuery fromLegacy(String kategori, String kriteria) {
            if (kriteria == null || kriteria.isBlank()) {
                return new RecommendationQuery(kategori, List.of(), null, null, false, "");
            }

            String normalized = kriteria.toLowerCase().trim();
            if (isBestsellerText(normalized)) {
                return new RecommendationQuery(kategori, List.of(), null, null, true, "");
            }

            if (normalized.equals("iced")) {
                return new RecommendationQuery(kategori, List.of(), "dingin", null, false, "");
            }
            if (normalized.equals("hot")) {
                return new RecommendationQuery(kategori, List.of(), "panas", null, false, "");
            }
            if (normalized.equals("panas") || normalized.equals("dingin")) {
                return new RecommendationQuery(kategori, List.of(), normalized, null, false, "");
            }

            return new RecommendationQuery(kategori, List.of(normalized), null, null, false, "");
        }

        public String getKategori() {
            return kategori;
        }

        public List<String> getRasa() {
            return rasa;
        }

        public String getSuhuSajian() {
            return suhuSajian;
        }

        public String getKonteksCuaca() {
            return konteksCuaca;
        }

        public boolean isBestseller() {
            return bestseller;
        }

        public String getSumberInput() {
            return sumberInput;
        }

        public boolean hasPreference() {
            return kategori != null || !rasa.isEmpty() || suhuSajian != null || konteksCuaca != null || bestseller;
        }

        public String toLegacyKeyword() {
            if (!hasPreference()) {
                return null;
            }

            String kriteria = null;
            if (bestseller) {
                kriteria = "bestseller";
            } else if (!rasa.isEmpty()) {
                kriteria = rasa.get(0);
            } else if (suhuSajian != null) {
                kriteria = suhuSajian;
            }

            if (kategori == null && kriteria == null) {
                return null;
            }
            return (kategori != null ? kategori : "") + "|" + (kriteria != null ? kriteria : "");
        }

        private static String kosongJadiNull(String value) {
            return value == null || value.isBlank() ? null : value.toLowerCase().trim();
        }

        private static boolean isBestsellerText(String value) {
            return value.equals("bestseller")
                    || value.equals("best seller")
                    || value.equals("best-seller")
                    || value.equals("terlaris")
                    || value.equals("paling laku")
                    || value.equals("favorit")
                    || value.equals("favoritnya")
                    || value.equals("unggulan")
                    || value.equals("andalan")
                    || value.equals("populer")
                    || value.equals("viral")
                    || value.equals("hits")
                    || value.equals("signature")
                    || value.equals("best")
                    || value.equals("top")
                    || value.equals("paling enak")
                    || value.equals("yang enak");
        }
    }

    private String preprocess() {
        if (inputUser == null) {
            return "";
        }
        return inputUser.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public Intent detectIntent() {
        if (processedInput == null || processedInput.isEmpty()) {
            return Intent.TIDAK_DIKENAL;
        }
        if (containsAnyWord(KAMUS_SALAM)) {
            return Intent.SALAM;
        } else if (containsAny(KAMUS_JAM)) {
            return Intent.TANYA_JAM_BUKA;
        } else if (containsAny(KAMUS_LOKASI)) {
            return Intent.TANYA_LOKASI;
        } else if (containsAny(KAMUS_FASILITAS)) {
            return Intent.TANYA_FASILITAS;
        } else if (containsAny(KAMUS_REKOMENDASI)
                || containsAnyAlias(ALIAS_RASA)
                || containsAnyAlias(ALIAS_SUHU)
                || processedInput.matches(".*\\b(pengen|pengin|mau|ingin|pesen|pesan|beli|cari|nyari)\\b.*\\byang\\b.*")) {
            return Intent.TANYA_REKOMENDASI;
        } else if (containsAny(KAMUS_DETAIL)) {
            return Intent.TANYA_DETAIL_MENU;
        } else if (containsAny(KAMUS_KATEGORI)) {
            return Intent.TANYA_KATEGORI;
        } else if (containsAny(KAMUS_MENU)) {
            return Intent.TANYA_MENU;
        }

        return Intent.TIDAK_DIKENAL;
    }

    public String extractKeyword(Intent intent) {
        if (processedInput == null || processedInput.isEmpty()) {
            return null;
        }

        switch (intent) {
            case TANYA_REKOMENDASI:
                RecommendationQuery query = extractRecommendationQuery();
                return query.toLegacyKeyword();

            case TANYA_KATEGORI:
                if (processedInput.contains("non-kopi")
                        || processedInput.contains("non kopi")
                        || processedInput.contains("tanpa kopi")
                        || processedInput.contains("selain kopi")) {
                    return "non-kopi";
                }

                if (processedInput.contains("cemilan")
                        || processedInput.contains("snack")
                        || processedInput.contains("makan")) {
                    return "makanan";
                }

                if (processedInput.contains("drink") || processedInput.contains("beverage")) {
                    return "minuman";
                }

                if (processedInput.contains("coffee")) {
                    return "kopi";
                }

                String[] kategori = {"kopi", "makanan", "minuman", "mix"};
                for (String k : kategori) {
                    if (processedInput.contains(k)) {
                        return k;
                    }
                }
                break;

            case TANYA_DETAIL_MENU:
                String entity = processedInput;
                String[] triggers = {
                        "tentang ", "detail ", "info ", "informasi ", "deskripsi ", "harga ",
                        "harganya ", "komposisi ", "bahan ", "isi ", "ukuran ", "porsi "
                };

                for (String t : triggers) {
                    if (entity.contains(t)) {
                        entity = entity.substring(entity.indexOf(t) + t.length()).trim();
                        break;
                    }
                }

                if (entity.startsWith("menu ")) {
                    entity = entity.substring(5).trim();
                }

                // Bersihkan kata tambahan agar tersisa nama menu.
                entity = entity.replaceAll("(?i)\\b(menu|dong|min|kak|ya|sih|tolong|pliss|please|doang)\\b", "").trim();
                return entity;

            default:
                return null;
        }
        return null;
    }

    public RecommendationQuery extractRecommendationQuery() {
        if (processedInput == null || processedInput.isEmpty()) {
            return RecommendationQuery.empty();
        }

        String kategori = extractKategoriRekomendasi();
        List<String> rasa = findCanonicalAliases(ALIAS_RASA);
        String konteksCuaca = extractCuacaContext();
        String suhuSajian = mapCuacaKeSuhuSajian(konteksCuaca);
        if (suhuSajian == null) {
            suhuSajian = findCanonicalAlias(ALIAS_SUHU);
        }

        boolean bestseller = containsAny(KAMUS_BESTSELLER);
        return new RecommendationQuery(kategori, rasa, suhuSajian, konteksCuaca, bestseller, processedInput);
    }

    private String extractKategoriRekomendasi() {
        if (processedInput.contains("makanan")
                || processedInput.contains("cemilan")
                || processedInput.contains("snack")
                || processedInput.contains("ngemil")
                || processedInput.contains("makan")) {
            return "makanan";
        }
        if (processedInput.contains("non-kopi")
                || processedInput.contains("non kopi")
                || processedInput.contains("tanpa kopi")
                || processedInput.contains("selain kopi")) {
            return "non-kopi";
        }
        if (processedInput.contains("kopi") || processedInput.contains("coffee")) {
            return "kopi";
        }
        if (processedInput.contains("minuman")
                || processedInput.contains("minum")
                || processedInput.contains("drink")
                || processedInput.contains("beverage")) {
            return "minuman";
        }
        if (processedInput.contains("mix")) {
            return "mix";
        }
        return null;
    }

    private boolean containsAny(String[] kamus) {
        for (String kata : kamus) {
            if (containsKeyword(kata)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAnyWord(String[] kamus) {
        for (String kata : kamus) {
            if (containsKeyword(kata)) {
                return true;
            }
        }
        return false;
    }

    private String findCanonicalAlias(String[][] aliasGroups) {
        for (String[] aliasGroup : aliasGroups) {
            String canonical = aliasGroup[0];
            for (int i = 1; i < aliasGroup.length; i++) {
                if (containsKeyword(aliasGroup[i])) {
                    return canonical;
                }
            }
        }
        return null;
    }

    private List<String> findCanonicalAliases(String[][] aliasGroups) {
        List<String> hasil = new ArrayList<>();
        for (String[] aliasGroup : aliasGroups) {
            String canonical = aliasGroup[0];
            for (int i = 1; i < aliasGroup.length; i++) {
                if (containsKeyword(aliasGroup[i])) {
                    if (!hasil.contains(canonical)) {
                        hasil.add(canonical);
                    }
                    break;
                }
            }
        }
        return hasil;
    }

    private boolean containsAnyAlias(String[][] aliasGroups) {
        return findCanonicalAlias(aliasGroups) != null;
    }

    private String extractSuhuContext() {
        String suhuDariCuaca = mapCuacaKeSuhuSajian(extractCuacaContext());
        return suhuDariCuaca == null ? findCanonicalAlias(ALIAS_SUHU) : suhuDariCuaca;
    }

    private String extractCuacaContext() {
        boolean adaKonteksCuaca = processedInput.matches(".*\\b(cuaca|cuacanya|hari|udara|udaranya)\\b.*")
                || containsKeyword("hujan")
                || containsKeyword("mendung");
        if (!adaKonteksCuaca) {
            return null;
        }

        if (containsKeyword("panas") || containsKeyword("gerah") || containsKeyword("terik")) {
            return "panas";
        }
        if (containsKeyword("dingin") || containsKeyword("sejuk") || containsKeyword("hujan") || containsKeyword("mendung")) {
            return "dingin";
        }
        return null;
    }

    private String mapCuacaKeSuhuSajian(String cuaca) {
        if ("panas".equals(cuaca)) {
            return "dingin";
        }
        if ("dingin".equals(cuaca)) {
            return "panas";
        }
        return null;
    }

    private boolean containsKeyword(String keyword) {
        return processedInput.matches(".*\\b" + Pattern.quote(keyword) + "\\b.*");
    }
}
