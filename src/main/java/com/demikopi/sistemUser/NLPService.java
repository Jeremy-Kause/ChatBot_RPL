package com.demikopi.sistemUser;

/**
 * NLPService memproses input user menjadi intent dan keyword.
 * Algoritma masih rule-based, yaitu pencocokan kata kunci dari teks yang sudah dibersihkan.
 */
public class NLPService {

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
        if (processedInput.matches(".*\\b(halo|hi|hey|hai|pagi|siang|sore|malam)\\b.*")) {
            return Intent.SALAM;
        } else if (processedInput.contains("jam") || processedInput.contains("buka") || processedInput.contains("tutup") || processedInput.contains("operasional")) {
            return Intent.TANYA_JAM_BUKA;
        } else if (processedInput.contains("lokasi") || processedInput.contains("alamat") || processedInput.contains("di mana") || processedInput.contains("dimana")) {
            return Intent.TANYA_LOKASI;
        } else if (processedInput.contains("fasilitas") || processedInput.contains("wifi") || processedInput.contains("parkir") || processedInput.contains("colokan") || processedInput.contains("mushola")) {
            return Intent.TANYA_FASILITAS;
        } else if (processedInput.contains("tentang") || processedInput.contains("detail ")
                || processedInput.contains("info ") || processedInput.contains("deskripsi")
                || processedInput.contains("harga")) {
            return Intent.TANYA_DETAIL_MENU;
        } else if (processedInput.matches(".*\\b(rekomen|rekomendasi|saran|enak|bestseller|best seller|bagus|favorit)\\b.*") ||
                processedInput.contains("manis") || processedInput.contains("pahit") || processedInput.contains("asam") ||
                processedInput.contains("dingin") || processedInput.contains("panas") || processedInput.contains("iced") ||
                processedInput.matches(".*\\b(pengen|mau|pesen|pesan|cari)\\b.*\\byang\\b.*")) {
            return Intent.TANYA_REKOMENDASI;
        } else if (processedInput.contains("non-kopi") || processedInput.contains("non kopi")
                || processedInput.contains("kopi") || processedInput.contains("makanan")
                || processedInput.contains("minuman") || processedInput.contains("mix")) {
            return Intent.TANYA_KATEGORI;
        } else if (processedInput.contains("menu") || processedInput.contains("daftar") || processedInput.contains("ada apa aja")) {
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

                // Kriteria rasa.
                String[] rasa = {"manis", "pahit", "asam", "gurih", "creamy", "fruity"};
                for (String r : rasa) {
                    if (processedInput.contains(r)) {
                        extractedKriteria = r;
                        break;
                    }
                }

                // Kriteria suhu.
                if (extractedKriteria == null) {
                    String[] suhu = {"panas", "dingin", "iced", "hot"};
                    for (String s : suhu) {
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
}
