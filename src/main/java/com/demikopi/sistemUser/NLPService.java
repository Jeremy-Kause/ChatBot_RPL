package com.demikopi.sistemUser;

/**
 * NLPService memproses input user menjadi intent dan keyword.
 * Algoritma masih rule-based, yaitu pencocokan kata kunci dari teks yang sudah dibersihkan.
 */
public class NLPService {

    private static final String[] KAMUS_SALAM = {"halo", "hi", "hey", "hai", "pagi", "siang", "sore", "malam"};
    private static final String[] KAMUS_JAM = {"jam", "buka", "tutup", "operasional"};
    private static final String[] KAMUS_LOKASI = {"lokasi", "alamat", "di mana", "dimana"};
    private static final String[] KAMUS_FASILITAS = {"fasilitas", "wifi", "parkir", "colokan", "mushola", "musholla"};
    private static final String[] KAMUS_DETAIL = {"tentang", "detail", "info", "deskripsi", "harga"};
    private static final String[] KAMUS_REKOMENDASI = {
            "rekomen", "rekomendasi", "saran", "enak", "bagus", "favorit",
            "bestseller", "best seller", "best-seller", "terlaris", "paling laku", "unggulan"
    };
    private static final String[] KAMUS_BESTSELLER = {
            "bestseller", "best seller", "best-seller", "terlaris", "paling laku", "favorit", "unggulan"
    };
    private static final String[] KAMUS_RASA = {"manis", "pahit", "asam", "gurih", "creamy", "fruity", "segar"};
    private static final String[] KAMUS_SUHU = {"panas", "dingin", "iced", "hot"};
    private static final String[] KAMUS_MENU = {"menu", "daftar", "ada apa aja", "list"};
    private static final String[] KAMUS_KATEGORI = {"non-kopi", "non kopi", "kopi", "makanan", "minuman", "mix"};

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

    private String preprocess() {
        if (inputUser == null) {
            return "";
        }
        return inputUser.toLowerCase().trim().replaceAll("[?.!]", "");
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
                || containsAny(KAMUS_RASA)
                || containsAny(KAMUS_SUHU)
                || processedInput.matches(".*\\b(pengen|mau|pesen|pesan|cari)\\b.*\\byang\\b.*")) {
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
                String extractedKategori = null;

                // Konteks kategori rekomendasi.
                if (processedInput.contains("makanan") || processedInput.contains("cemilan") || processedInput.contains("ngemil") || processedInput.contains("makan")) {
                    extractedKategori = "makanan";
                } else if (processedInput.contains("non-kopi") || processedInput.contains("non kopi") || processedInput.contains("selain kopi")) {
                    extractedKategori = "non-kopi";
                } else if (processedInput.contains("kopi")) {
                    extractedKategori = "kopi";
                } else if (processedInput.contains("minuman") || processedInput.contains("minum")) {
                    extractedKategori = "minuman";
                }

                String extractedKriteria = null;
                if (containsAny(KAMUS_BESTSELLER)) {
                    extractedKriteria = "bestseller";
                }

                // Kriteria rasa.
                if (extractedKriteria == null) {
                    for (String r : KAMUS_RASA) {
                        if (processedInput.contains(r)) {
                            extractedKriteria = r;
                            break;
                        }
                    }
                }

                // Kriteria suhu.
                if (extractedKriteria == null) {
                    for (String s : KAMUS_SUHU) {
                        if (processedInput.contains(s)) {
                            extractedKriteria = s;
                            break;
                        }
                    }
                }

                if (extractedKategori == null && extractedKriteria == null) {
                    return null;
                }
                return (extractedKategori != null ? extractedKategori : "") + "|" + (extractedKriteria != null ? extractedKriteria : "");

            case TANYA_KATEGORI:
                if (processedInput.contains("non-kopi") || processedInput.contains("non kopi")) {
                    return "non-kopi";
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
                String[] triggers = {"tentang ", "detail ", "info ", "deskripsi ", "harga "};

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

    private boolean containsAny(String[] kamus) {
        for (String kata : kamus) {
            if (processedInput.contains(kata)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAnyWord(String[] kamus) {
        for (String kata : kamus) {
            if (processedInput.matches(".*\\b" + kata + "\\b.*")) {
                return true;
            }
        }
        return false;
    }
}
